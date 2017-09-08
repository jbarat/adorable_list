package uk.co.jbarat.adorablelist.main;

import java.util.List;

interface MainView {
    void updatePostsList(List<PostListViewModel> posts);

    void startPostDetailsActivity(int postId);
}
