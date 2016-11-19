package com.wisnu.paktukang2.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.wisnu.paktukang2.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by private on 14/06/2016.
 */
@IgnoreExtraProperties
public class DataDiri extends BaseActivity {
    public String uid;
    public String username;
    public String nama;
    public String daerah;
    public String noHp;
    public String keahlian;

    public DataDiri() {
    }

    public DataDiri(String uid, String username, String nama, String daerah, String noHp, String keahlian) {
        this.uid = uid;
        this.username = username;
        this.nama = nama;
        this.daerah = daerah;
        this.noHp = noHp;
        this.keahlian = keahlian;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("nama", nama);
        result.put("daerah", daerah);
        result.put("noHp", noHp);
        result.put("keahlian", keahlian);

        return result;
    }
}