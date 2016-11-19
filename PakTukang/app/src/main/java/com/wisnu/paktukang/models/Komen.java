package com.wisnu.paktukang.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by private on 13/06/2016.
 */
@IgnoreExtraProperties
public class Komen {
    public String uid;
    public String author;
    public String text;

    public Komen(String text, String uid, String author) {
        this.text = text;
        this.uid = uid;
        this.author = author;
    }

    public Komen() {

    }
}
