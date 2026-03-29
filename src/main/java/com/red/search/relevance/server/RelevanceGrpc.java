/*
 * Copyright 2026 Vipshop Inc. All Rights Reserved.
 */

package com.red.search.relevance.server;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import io.grpc.protobuf.ProtoUtils;

/**
 */

@io.grpc.stub.annotations.GrpcGenerated
public final class RelevanceGrpc {

  private RelevanceGrpc() {}

  public static final String SERVICE_NAME = "relevance.server.Relevance";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.red.search.relevance.server.RelevanceProto.RelevanceRequest,
      com.red.search.relevance.server.RelevanceProto.RelevanceReply> getPredictMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Predict",
      requestType = com.red.search.relevance.server.RelevanceProto.RelevanceRequest.class,
      responseType = com.red.search.relevance.server.RelevanceProto.RelevanceReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.red.search.relevance.server.RelevanceProto.RelevanceRequest,
      com.red.search.relevance.server.RelevanceProto.RelevanceReply> getPredictMethod() {
    io.grpc.MethodDescriptor<com.red.search.relevance.server.RelevanceProto.RelevanceRequest, com.red.search.relevance.server.RelevanceProto.RelevanceReply> getPredictMethod;
    if ((getPredictMethod = RelevanceGrpc.getPredictMethod) == null) {
      synchronized (RelevanceGrpc.class) {
        if ((getPredictMethod = RelevanceGrpc.getPredictMethod) == null) {
          RelevanceGrpc.getPredictMethod = getPredictMethod =
              io.grpc.MethodDescriptor.<com.red.search.relevance.server.RelevanceProto.RelevanceRequest, com.red.search.relevance.server.RelevanceProto.RelevanceReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Predict"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.red.search.relevance.server.RelevanceProto.RelevanceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.red.search.relevance.server.RelevanceProto.RelevanceReply.getDefaultInstance()))
              .setSchemaDescriptor(new RelevanceMethodDescriptorSupplier("Predict"))
              .build();
        }
      }
    }
    return getPredictMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RelevanceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RelevanceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RelevanceStub>() {
        @Override
        public RelevanceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RelevanceStub(channel, callOptions);
        }
      };
    return RelevanceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RelevanceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RelevanceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RelevanceBlockingStub>() {
        @Override
        public RelevanceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RelevanceBlockingStub(channel, callOptions);
        }
      };
    return RelevanceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RelevanceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RelevanceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RelevanceFutureStub>() {
        @Override
        public RelevanceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RelevanceFutureStub(channel, callOptions);
        }
      };
    return RelevanceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void predict(com.red.search.relevance.server.RelevanceProto.RelevanceRequest request,
        io.grpc.stub.StreamObserver<com.red.search.relevance.server.RelevanceProto.RelevanceReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPredictMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service Relevance.
   */
  public static abstract class RelevanceImplBase
      implements io.grpc.BindableService, AsyncService {

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return RelevanceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service Relevance.
   */
  public static final class RelevanceStub
      extends io.grpc.stub.AbstractAsyncStub<RelevanceStub> {
    private RelevanceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RelevanceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RelevanceStub(channel, callOptions);
    }

    /**
     */
    public void predict(com.red.search.relevance.server.RelevanceProto.RelevanceRequest request,
        io.grpc.stub.StreamObserver<com.red.search.relevance.server.RelevanceProto.RelevanceReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPredictMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service Relevance.
   */
  public static final class RelevanceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<RelevanceBlockingStub> {
    private RelevanceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RelevanceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RelevanceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.red.search.relevance.server.RelevanceProto.RelevanceReply predict(com.red.search.relevance.server.RelevanceProto.RelevanceRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPredictMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service Relevance.
   */
  public static final class RelevanceFutureStub
      extends io.grpc.stub.AbstractFutureStub<RelevanceFutureStub> {
    private RelevanceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RelevanceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RelevanceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.red.search.relevance.server.RelevanceProto.RelevanceReply> predict(
        com.red.search.relevance.server.RelevanceProto.RelevanceRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPredictMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PREDICT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PREDICT:
          serviceImpl.predict((com.red.search.relevance.server.RelevanceProto.RelevanceRequest) request,
              (io.grpc.stub.StreamObserver<com.red.search.relevance.server.RelevanceProto.RelevanceReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getPredictMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.red.search.relevance.server.RelevanceProto.RelevanceRequest,
              com.red.search.relevance.server.RelevanceProto.RelevanceReply>(
                service, METHODID_PREDICT)))
        .build();
  }

  private static abstract class RelevanceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RelevanceBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.red.search.relevance.server.RelevanceProto.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Relevance");
    }
  }

  private static final class RelevanceFileDescriptorSupplier
      extends RelevanceBaseDescriptorSupplier {
    RelevanceFileDescriptorSupplier() {}
  }

  private static final class RelevanceMethodDescriptorSupplier
      extends RelevanceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RelevanceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RelevanceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RelevanceFileDescriptorSupplier())
              .addMethod(getPredictMethod())
              .build();
        }
      }
    }
    return result;
  }
}
