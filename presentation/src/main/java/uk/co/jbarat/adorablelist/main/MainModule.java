package uk.co.jbarat.adorablelist.main;

import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @Provides
    PostListAdapter postListAdapter(Picasso picasso){
        return new PostListAdapter(picasso);
    }
}
