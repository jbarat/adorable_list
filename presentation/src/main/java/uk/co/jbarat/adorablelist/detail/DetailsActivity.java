package uk.co.jbarat.adorablelist.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import uk.co.jbarat.adorablelist.R;
import uk.co.jbarat.adorablelist.application.AdorableListApplication;
import uk.co.jbarat.adorablelist.di.DetailsComponent;
import uk.co.jbarat.data.NetworkConstants;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class DetailsActivity extends AppCompatActivity implements DetailsView {

    public static final String POST_ID = "PostId";

    @Inject DetailsPresenter presenter;
    @Inject Picasso picasso;

    private DetailsComponent component;

    private TextView title;
    private TextView body;
    private TextView author;
    private ImageView image;
    private TextView comments;
    private TextView preComments;
    private ProgressBar progress;

    public static Intent getStaringIntent(Activity from, int postId) {
        Intent intent = new Intent(from, DetailsActivity.class);
        intent.putExtra(POST_ID, postId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_container);

        title = (TextView) findViewById(R.id.title);
        body = (TextView) findViewById(R.id.body);
        author = (TextView) findViewById(R.id.author);
        image = (ImageView) findViewById(R.id.image);
        comments = (TextView) findViewById(R.id.comments);
        preComments = (TextView) findViewById(R.id.comments_pre);
        progress = (ProgressBar) findViewById(R.id.progress);

        component().inject(this);

        presenter.attach(this, getIntent().getIntExtra(POST_ID, 0));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.deAttach();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return component();
    }

    private DetailsComponent component() {
        if (component == null) {
            component = (DetailsComponent) getLastCustomNonConfigurationInstance();
            if (component == null) {
                component = ((AdorableListApplication) getApplication()).getApplicationComponent()
                        .detailsComponent();
            }
        }
        return component;
    }

    @Override
    public void updateDetails(DetailViewModel detailViewModel) {
        title.setText(detailViewModel.getTitle());
        body.setText(detailViewModel.getBody());
        author.setText(detailViewModel.getName());
        comments.setText(String.valueOf(detailViewModel.getComments()));

        picasso.load(NetworkConstants.ADORABLE_AVATAR_URL + detailViewModel.getEmail())
                .placeholder(R.drawable.placeholder).into(image);

        showViews();
    }

    private void showViews() {
        title.setVisibility(VISIBLE);
        body.setVisibility(VISIBLE);
        author.setVisibility(VISIBLE);
        comments.setVisibility(VISIBLE);
        preComments.setVisibility(VISIBLE);
        image.setVisibility(VISIBLE);

        progress.setVisibility(GONE);
    }
}
