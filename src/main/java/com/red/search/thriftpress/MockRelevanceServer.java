package com.red.search.thriftpress;

import com.red.search.relevance.server.RelevanceGrpc;
import com.red.search.relevance.server.RelevanceProto;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;

public class MockRelevanceServer {
    private Server server;
    private int port;

    public MockRelevanceServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new RelevanceImpl())
                .build()
                .start();
        System.out.println("Mock gRPC服务器已启动，监听端口：" + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("正在关闭gRPC服务器...");
            MockRelevanceServer.this.stop();
            System.err.println("gRPC服务器已关闭");
        }));
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    // 等待服务器终止
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    // 实现RelevanceGrpc.RelevanceImplBase接口
    private static class RelevanceImpl extends RelevanceGrpc.RelevanceImplBase {
        @Override
        public void predict(RelevanceProto.RelevanceRequest request, StreamObserver<RelevanceProto.RelevanceReply> responseObserver) {
            // 创建一个简单的响应，返回一个包含1.0的分数列表
            RelevanceProto.RelevanceReply reply = RelevanceProto.RelevanceReply.newBuilder()
                    .addScores(1.0f)
                    .build();

            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }

    // 添加main方法，使服务器可以独立运行
    public static void main(String[] args) throws IOException, InterruptedException {
        // 默认端口50051
        int port = 50051;

        // 如果命令行参数提供了端口，则使用该端口
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("无效的端口号，使用默认端口50051");
            }
        }

        // 创建并启动服务器
        MockRelevanceServer server = new MockRelevanceServer(port);
        server.start();
        server.blockUntilShutdown();
    }
}
