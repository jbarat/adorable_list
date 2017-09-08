package uk.co.jbarat.adorablelist.di;

import dagger.Subcomponent;
import uk.co.jbarat.adorablelist.detail.DetailsActivity;
import uk.co.jbarat.adorablelist.detail.DetailsModule;
import uk.co.jbarat.domain.scope.Activity;

@Activity
@Subcomponent
        (modules = {
                DetailsModule.class,
        })
public interface DetailsComponent {
    void inject(DetailsActivity detailsActivity);
}
