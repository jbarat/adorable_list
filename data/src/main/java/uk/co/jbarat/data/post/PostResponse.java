package uk.co.jbarat.data.post;

class PostResponse {
    private int id;
    private int userId;
    private String title;
    private String body;

    PostResponse(int id, int userId, String title, String body) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.body = body;
    }

    int getId() {
        return id;
    }

    int getUserId() {
        return userId;
    }

    String getTitle() {
        return title;
    }

    String getBody() {
        return body;
    }
}
