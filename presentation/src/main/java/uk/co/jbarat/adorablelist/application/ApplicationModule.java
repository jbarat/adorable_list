package uk.co.jbarat.adorablelist.application;

import android.content.Context;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import uk.co.jbarat.adorablelist.BuildConfig;
import uk.co.jbarat.data.logger.DebugLogger;
import uk.co.jbarat.domain.logger.Logger;

@Module
public class ApplicationModule {
    private final AdorableListApplication adorableListApplication;

    ApplicationModule(AdorableListApplication adorableListApplication) {
        this.adorableListApplication = adorableListApplication;
    }

    @Provides
    Context context() {
        return adorableListApplication;
    }

    @Provides
    Logger logger(DebugLogger debugLogger) {
        return debugLogger;
    }

    @Provides
    Locale locale() {
        return Locale.UK;
    }

    @Provides
    Picasso picasso(Context context) {
        return Picasso.with(context);
    }

    @Named("io")
    @Provides
    Scheduler io() {
        return Schedulers.io();
    }

    @Named("main")
    @Provides
    Scheduler main() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Named("debug")
    Boolean debug() {
        return BuildConfig.DEBUG;
    }
}