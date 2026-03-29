package com.red.search.thriftpress;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import org.json.JSONArray;
import org.json.JSONObject;

//import com.google.common.util.concurrent.RateLimiter;
import com.red.search.relevance.server.RelevanceGrpc;
import com.red.search.relevance.server.RelevanceGrpc.RelevanceBlockingStub;
import com.red.search.relevance.server.RelevanceProto;
import com.red.search.relevance.server.RelevanceProto.RelevanceRequest;
import com.red.search.relevance.server.RelevanceProto.RelevanceReply;

import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import perf.presstool.PressStat;

public class RelevanceGrpcPressCall implements Callable<PressStat> {
    private ManagedChannel channel;
    private RelevanceBlockingStub client;

//    private final RateLimiter limit;
    private final SimpleRateLimiter limit;
    private String host;
    private int port;
    private int threshold;
    private List<String> queries;
    private int pressCount;
    private long durationMillis;
    private long warmUpMillis;


//    public RelevanceGrpcPressCall(RateLimiter limit, String host, int port, int threshold, List<String> queries, JSONObject grpcConf, long durationSeconds) {
        public RelevanceGrpcPressCall(SimpleRateLimiter limit, String host, int port, int threshold, List<String> queries, JSONObject grpcConf, long durationSeconds, long warmUpSeconds) {
        this.limit = limit;
        this.host = host;
        this.port = port;
        this.threshold = threshold;
        this.queries = queries;
        this.pressCount = grpcConf.getInt("press_count");
        this.durationMillis = durationSeconds > 0 ? durationSeconds * 1000L : 0L;
            this.warmUpMillis = warmUpSeconds > 0 ? warmUpSeconds * 1000L : 0L;
//        System.out.println("Ctor: thread=" + Thread.currentThread().getName()
//                + ", limiter=" + System.identityHashCode(this.limit));
    }

    @Override
    public PressStat call() throws Exception {
//        System.out.println("Call start: thread=" + Thread.currentThread().getName()
//                + ", limiter=" + System.identityHashCode(limit));
        long realStart = System.currentTimeMillis();
//        System.out.println("Thread " + Thread.currentThread().getName()
//                + " start, durationMillis = " + durationMillis
//                + ", pressCount = " + pressCount);

        final long startTime = System.currentTimeMillis();
        final long endTimeMillis;
        if (durationMillis > 0) {
            endTimeMillis = startTime + durationMillis;
        } else {
            // 如果没配 duration，就给一个非常大的值，相当于“跑很久”
            endTimeMillis = Long.MAX_VALUE;
        }
        int maxCount = (pressCount > 0) ? pressCount : Integer.MAX_VALUE;
//        PressStat stat;
//        if (pressCount > 0) {
//            stat = new PressStat(threshold, pressCount);
//        } else {
//            stat = new PressStat(threshold);  // warmUp 使用默认 1
//        }
        PressStat stat = new PressStat(threshold);
        int i = 0;
        int idx = 0;
        while (System.currentTimeMillis() < endTimeMillis && i < maxCount) {
            String queryJson = queries.get(idx);
            idx = (idx + 1) % queries.size();
            RelevanceRequest request = createRequest(queryJson);
//            long beforeAcquire = System.nanoTime();
            limit.acquire();
//            long afterAcquire = System.nanoTime();
//            if (i < 3) { // 每个线程前3个打一下
//                System.out.println("thread=" + Thread.currentThread().getName()
//                        + " i=" + i
//                        + " acquireCost=" + (afterAcquire - beforeAcquire) / 1_000_000 + " ms");
//            }
            long start = System.currentTimeMillis();
            boolean inWarmUp = (start - startTime) < warmUpMillis;
            try {
                RelevanceBlockingStub client = getClient();
                RelevanceReply result = client.predict(request);
                if (i == 0) {
                    System.out.println(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
//                stat.collectFail();
                if (!inWarmUp) {          // 预热期内的错误也不记
                    stat.collectFail();
                }
                shutdownChannel();
                client = null;
                ++i;
                continue;
            }

            long end = System.currentTimeMillis();
            if (!inWarmUp) {
                stat.collect(queryJson, (int)(end - start));
            }
//            stat.collect(queryJson, (int)(end - start));
            ++i;
        }
        long realEnd = System.currentTimeMillis();
        System.out.println("Thread " + Thread.currentThread().getName()
                + " end, actualDuration = " + (realEnd - realStart)
                + " ms, totalRequests = " + i);
        System.out.println("Thread " + Thread.currentThread().getName()
                + " stat = " + stat);
        shutdownChannel();
        return stat;
    }

    private RelevanceRequest createRequest(String queryJson) {
        JSONObject json = new JSONObject(queryJson);

        JSONObject dispatchJson = json.getJSONObject("dispatch");

        // 构建RelevanceDispatch
        RelevanceProto.RelevanceDispatch dispatch = RelevanceProto.RelevanceDispatch.newBuilder()
                .setFeatureVersion(dispatchJson.getString("feature_version"))
                .setModelName(dispatchJson.getString("model_name"))
                .setKkvName(dispatchJson.getString("kkv_name"))
                .setAnnName(dispatchJson.getString("ann_name"))
                .build();

        // 构建RelevanceRequest
        RelevanceRequest.Builder requestBuilder = RelevanceRequest.newBuilder()
                .setDispatch(dispatch)
                .setQpInfo(json.getString("qp_info"));

        JSONArray queryAnnArray = json.getJSONArray("query_ann");
        for (int i = 0; i < queryAnnArray.length(); i++) {
            requestBuilder.addQueryAnn((float) queryAnnArray.getDouble(i));
        }
        JSONArray idsArray = json.getJSONArray("ids");
        for (int i = 0; i < idsArray.length(); i++) {
            requestBuilder.addIds(idsArray.getString(i));
        }
        return requestBuilder.build();
    }

    private RelevanceBlockingStub getClient() throws IOException {
        if (client == null) {
            client = createClient(host, port);
        }
        return client;
    }

    private RelevanceBlockingStub createClient(String host, int port) {
        String target = host + ":" + port;
        channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create())
                .build();
        return RelevanceGrpc.newBlockingStub(channel);
    }

    private void shutdownChannel() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
    }
}
