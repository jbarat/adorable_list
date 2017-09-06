package uk.co.jbarat.data;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.jbarat.data.comment.CommentWebService;
import uk.co.jbarat.data.post.PostRepository;
import uk.co.jbarat.data.post.PostWebService;
import uk.co.jbarat.data.user.UserRepository;
import uk.co.jbarat.data.user.UserWebService;
import uk.co.jbarat.domain.post.PostUseCase;
import uk.co.jbarat.domain.user.UserUseCase;

@Module
public class NetworkModule {

    @Provides
    CommentWebService commentWebService(Retrofit retrofit) {
        return retrofit.create(CommentWebService.class);
    }

    @Provides
    PostWebService postWebService(Retrofit retrofit) {
        return retrofit.create(PostWebService.class);
    }

    @Provides
    UserWebService userWebService(Retrofit retrofit) {
        return retrofit.create(UserWebService.class);
    }

    @Provides
    PostUseCase.PostDataSource postDataSource(PostRepository postRepository) {
        return postRepository;
    }

    @Provides
    UserUseCase.UserDataSource userDataSource(UserRepository userRepository) {
        return userRepository;
    }

    @Provides
    @Singleton
    Retrofit retrofit(OkHttpClient secureClient) {
        return new Retrofit.Builder()
                .client(secureClient)
                .baseUrl(NetworkConstants.URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient okHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(NetworkConstants.TIMEOUT_SECOND, TimeUnit.SECONDS)
                .readTimeout(NetworkConstants.TIMEOUT_SECOND, TimeUnit.SECONDS)
                .writeTimeout(NetworkConstants.TIMEOUT_SECOND, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor);

        return builder.build();
    }

    @Provides
    HttpLoggingInterceptor httpLoggingInterceptor(@Named("debug") Boolean debug) {
        return new HttpLoggingInterceptor()
                .setLevel(debug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
    }
}
