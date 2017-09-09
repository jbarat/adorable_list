package uk.co.jbarat.adorablelist.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import uk.co.jbarat.adorablelist.R;
import uk.co.jbarat.data.NetworkConstants;

import static java.util.Collections.emptyList;

class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    private final Picasso picasso;
    private final BehaviorSubject<ListViewModel> selectedItem = BehaviorSubject.create();

    private List<ListViewModel> posts = emptyList();

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
        return posts.size();
    }

    void updateList(List<ListViewModel> posts) {
        this.posts = posts;

        notifyDataSetChanged();
    }

    Observable<ListViewModel> getSelection() {
        return selectedItem;
    }

    private void emitSelect(ListViewModel item) {
        selectedItem.onNext(item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Picasso picasso;
        private final PostListAdapter parent;

        final TextView title;
        final ImageView image;

        ListViewModel item;

        ViewHolder(View view, Picasso picasso, PostListAdapter postListAdapter) {
            super(view);
            this.picasso = picasso;

            parent = postListAdapter;

            title = view.findViewById(R.id.textView);
            image = view.findViewById(R.id.image);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            parent.emitSelect(item);
        }

        void bind(ListViewModel listViewModel) {
            item = listViewModel;

            title.setText(item.getTitle());

            picasso.load(NetworkConstants.ADORABLE_AVATAR_URL + listViewModel.getUserEmail())
                    .placeholder(R.drawable.placeholder).into(image);
        }
    }
}
