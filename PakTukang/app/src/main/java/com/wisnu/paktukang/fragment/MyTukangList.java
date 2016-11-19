package com.wisnu.paktukang.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by private on 13/06/2016.
 */
public class MyTukangList extends TukangListFragment {

    public MyTukangList() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("profil");
    }
}
