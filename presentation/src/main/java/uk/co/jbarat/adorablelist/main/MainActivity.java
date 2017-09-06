package uk.co.jbarat.adorablelist.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;

import uk.co.jbarat.adorablelist.R;
import uk.co.jbarat.adorablelist.application.AdorableListApplication;
import uk.co.jbarat.adorablelist.di.MainComponent;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements MainView {

    @Inject MainPresenter mainPresenter;
    @Inject PostListAdapter postListAdapter;

    private MainComponent component;

    private ProgressBar progressBar;
    private RecyclerView postListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_container);

        component().inject(this);

        postListRecyclerView = (RecyclerView) findViewById(R.id.main_posts);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);

        progressBar.setVisibility(VISIBLE);

        postListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postListRecyclerView.setAdapter(postListAdapter);
        postListRecyclerView.setVisibility(GONE);


        mainPresenter.attach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.deAttach();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return component();
    }

    private MainComponent component() {
        if (component == null) {
            component = (MainComponent) getLastCustomNonConfigurationInstance();
            if (component == null) {
                component = ((AdorableListApplication) getApplication()).getApplicationComponent()
                        .mainComponent();
            }
        }
        return component;
    }

    @Override
    public void updatePostsList(List<PostViewModel> posts) {
        if (posts.size() > 0) {
            progressBar.setVisibility(GONE);
            postListRecyclerView.setVisibility(VISIBLE);

            postListAdapter.updateList(posts);
        } else {
            progressBar.setVisibility(VISIBLE);
            postListRecyclerView.setVisibility(GONE);
        }
    }
}
