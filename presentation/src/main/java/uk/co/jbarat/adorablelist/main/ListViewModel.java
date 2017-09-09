package uk.co.jbarat.adorablelist.main;

import android.support.annotation.NonNull;

class ListViewModel implements Comparable<ListViewModel>{
    private final int id;
    private final String title;
    private final String userEmail;

    ListViewModel(@NonNull int id, String title, String userEmail) {
        this.id = id;
        this.title = title;
        this.userEmail = userEmail;
    }

    String getTitle() {
        return title;
    }

    String getUserEmail() {
        return userEmail;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(@NonNull ListViewModel o) {
        return  Integer.compare(this.id, o.getId());
    }
}
