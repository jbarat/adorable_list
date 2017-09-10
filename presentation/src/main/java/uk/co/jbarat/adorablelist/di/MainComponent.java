package uk.co.jbarat.adorablelist.di;

import dagger.Subcomponent;
import uk.co.jbarat.adorablelist.main.MainActivity;
import uk.co.jbarat.adorablelist.main.MainModule;
import uk.co.jbarat.data.logger.LoggerModule;
import uk.co.jbarat.domain.scope.Activity;

@Activity
@Subcomponent
        (modules = {
                MainModule.class,
                LoggerModule.class
        })
public interface MainComponent {
    void inject(MainActivity mainActivity);
}
