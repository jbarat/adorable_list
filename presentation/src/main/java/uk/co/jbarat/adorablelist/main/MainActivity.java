package uk.co.jbarat.adorablelist.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import javax.inject.Inject;

import uk.co.jbarat.adorablelist.R;
import uk.co.jbarat.adorablelist.application.AdorableListApplication;
import uk.co.jbarat.adorablelist.detail.DetailsActivity;
import uk.co.jbarat.adorablelist.di.MainComponent;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements MainView {

    @Inject MainPresenter mainPresenter;
    @Inject PostListAdapter postListAdapter;

    private MainComponent component;

    private ProgressBar progressBar;
    private RecyclerView postListRecyclerView;
    private Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_container);

        component().inject(this);

        postListRecyclerView = (RecyclerView) findViewById(R.id.posts);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        retryButton = (Button) findViewById(R.id.retry);

        postListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postListRecyclerView.setAdapter(postListAdapter);

        mainPresenter.attach(this, postListAdapter.getSelection(), RxView.clicks(retryButton));
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
    public void updatePostsList(List<ListViewModel> posts) {
        progressBar.setVisibility(GONE);

        if (posts.size() > 0) {
            postListRecyclerView.setVisibility(VISIBLE);
            retryButton.setVisibility(GONE);
            postListAdapter.updateList(posts);
        } else {
            retryButton.setVisibility(VISIBLE);
        }
    }

    @Override
    public void startPostDetailsActivity(int postId) {
        Intent intent = DetailsActivity.getStaringIntent(this, postId);
        startActivity(intent);
    }
}
