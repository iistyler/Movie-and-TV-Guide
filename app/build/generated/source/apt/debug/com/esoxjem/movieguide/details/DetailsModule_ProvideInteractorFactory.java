package com.esoxjem.movieguide.details;

import com.esoxjem.movieguide.network.RequestHandler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DetailsModule_ProvideInteractorFactory
    implements Factory<MovieDetailsInteractor> {
  private final DetailsModule module;

  private final Provider<RequestHandler> requestHandlerProvider;

  public DetailsModule_ProvideInteractorFactory(
      DetailsModule module, Provider<RequestHandler> requestHandlerProvider) {
    assert module != null;
    this.module = module;
    assert requestHandlerProvider != null;
    this.requestHandlerProvider = requestHandlerProvider;
  }

  @Override
  public MovieDetailsInteractor get() {
    return Preconditions.checkNotNull(
        module.provideInteractor(requestHandlerProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<MovieDetailsInteractor> create(
      DetailsModule module, Provider<RequestHandler> requestHandlerProvider) {
    return new DetailsModule_ProvideInteractorFactory(module, requestHandlerProvider);
  }
}
