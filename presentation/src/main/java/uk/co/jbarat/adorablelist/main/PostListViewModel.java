package uk.co.jbarat.adorablelist.main;

class PostListViewModel {
    private final int id;
    private final String body;
    private final String title;
    private final String userEmail;

    PostListViewModel(int id, String body, String title, String userEmail) {
        this.id = id;
        this.body = body;
        this.title = title;
        this.userEmail = userEmail;
    }

    public String getTitle() {
        return title;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getId() {
        return id;
    }
}
