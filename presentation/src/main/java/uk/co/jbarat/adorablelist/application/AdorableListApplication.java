package uk.co.jbarat.adorablelist.application;

import android.app.Application;

import uk.co.jbarat.adorablelist.di.ApplicationComponent;
import uk.co.jbarat.adorablelist.di.DaggerApplicationComponent;

public class AdorableListApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initDagger();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    private void initDagger() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }
}
