package uk.co.jbarat.adorablelist.main;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.jbarat.adorablelist.R;
import uk.co.jbarat.data.NetworkConstants;

import static java.util.Collections.emptyList;

class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    private final Picasso picasso;

    private List<PostViewModel> posts = emptyList();

    PostListAdapter(Picasso picasso) {
        this.picasso = picasso;
    }


    @Override
    public PostListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_post_item, parent, false);
        return new ViewHolder(view, picasso, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d("asd", "getItemCount() called " + posts.size());
        return posts.size();
    }

    void updateList(List<PostViewModel> posts) {
        this.posts = posts;

        notifyDataSetChanged();
    }

    private void emitSelect(PostViewModel item) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Picasso picasso;
        private final PostListAdapter parent;

        final TextView title;
        final ImageView image;

        PostViewModel item;

        ViewHolder(View view, Picasso picasso, PostListAdapter postListAdapter) {
            super(view);
            this.picasso = picasso;

            parent = postListAdapter;

            title = view.findViewById(R.id.textView);
            image = view.findViewById(R.id.imageView);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            parent.emitSelect(item);
        }

        void bind(PostViewModel postViewModel) {
            item = postViewModel;

            title.setText(item.getTitle());

            picasso.load(NetworkConstants.CUTE_AVATAR_URL + postViewModel.getUserEmail())
                    .placeholder(R.drawable.placeholder).into(image);
        }
    }
}
