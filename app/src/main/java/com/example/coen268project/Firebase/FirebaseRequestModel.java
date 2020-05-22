package com.example.coen268project.Firebase;
import com.google.firebase.database.Query;

public class FirebaseRequestModel {
    private Object listener;
    private Query query;

    public FirebaseRequestModel(Object listener, Query query) {
        this.listener = listener;
        this.query = query;
    }

    public Object getListener() {
        return listener;
    }

    public void setListener(Object listener) {
        this.listener = listener;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
