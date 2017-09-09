package uk.co.jbarat.adorablelist.detail;

class DetailViewModel {

    private final String title;
    private final String body;
    private final String name;
    private final String email;
    private final int comments;

    DetailViewModel(String title, String body, String name, String email, int comments) {
        this.title = title;
        this.body = body;
        this.name = name;
        this.email = email;
        this.comments = comments;
    }

    String getTitle() {
        return title;
    }

    String getBody() {
        return body;
    }

    String getName() {
        return name;
    }

    String getEmail() {
        return email;
    }

    int getComments() {
        return comments;
    }
}
