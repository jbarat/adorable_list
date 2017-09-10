package uk.co.jbarat.adorablelist.main;

import java.util.List;

interface MainView {
    void updatePostsList(List<ListViewModel> posts);

    void startPostDetailsActivity(int postId);

    void setToProgress();
}
