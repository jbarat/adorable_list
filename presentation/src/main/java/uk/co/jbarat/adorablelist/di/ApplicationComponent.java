package uk.co.jbarat.adorablelist.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.jbarat.adorablelist.application.ApplicationModule;
import uk.co.jbarat.data.NetworkModule;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        NetworkModule.class
})
public interface ApplicationComponent {

    Context context();

    MainComponent mainComponent();
}
