package com.esoxjem.movieguide.network;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class NetworkModule_ProvideRequestHandlerFactory implements Factory<RequestHandler> {
  private final NetworkModule module;

  private final Provider<OkHttpClient> okHttpClientProvider;

  public NetworkModule_ProvideRequestHandlerFactory(
      NetworkModule module, Provider<OkHttpClient> okHttpClientProvider) {
    assert module != null;
    this.module = module;
    assert okHttpClientProvider != null;
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public RequestHandler get() {
    return Preconditions.checkNotNull(
        module.provideRequestHandler(okHttpClientProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<RequestHandler> create(
      NetworkModule module, Provider<OkHttpClient> okHttpClientProvider) {
    return new NetworkModule_ProvideRequestHandlerFactory(module, okHttpClientProvider);
  }
}
