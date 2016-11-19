package com.wisnu.paktukang2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wisnu.paktukang2.model.DataDiri;
import com.wisnu.paktukang2.model.User;

import java.util.HashMap;
import java.util.Map;

public class ProfilActivity extends BaseActivity {

    private static final String REQUIRED = "Required";
    private static final String TAG = "ProfilActivity";

    private Button bLanjut;
    private Button bSimpan;

    private EditText etNama;
    private EditText etNoHp;
    private EditText etKeahlian;

    Spinner spinnerKabupaten;
    ArrayAdapter<CharSequence> adapter;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_diri);

        spinnerKabupaten = (Spinner) findViewById(R.id.sDaerah);
        adapter = ArrayAdapter.createFromResource(this, R.array.kabupaten,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKabupaten.setAdapter(adapter);

        etNama = (EditText) findViewById(R.id.tfNama);
        etNoHp = (EditText) findViewById(R.id.tfNoHp);
        etKeahlian = (EditText) findViewById(R.id.tfKeahlian);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        bSimpan = (Button) findViewById(R.id.bSimpan);
        bLanjut = (Button) findViewById(R.id.bKeluar);

        bLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilActivity.this, PesananActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                mAuth.signOut();
                loadLoginView();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void simpan() {
        final String nama = etNama.getText().toString();
        final String noHp = etNoHp.getText().toString();
        final String keahlian = etKeahlian.getText().toString();
        final String sDaerah = spinnerKabupaten.getSelectedItem().toString();

        if (TextUtils.isEmpty(nama)) {
            etNama.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(noHp)) {
            etNoHp.setError(REQUIRED);
            return;
        }

        final String userId = getUid();

        mDatabase.child("users1").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user==null){
                    Log.e(TAG, "User " + userId + " is unexpectedly null");
                    Toast.makeText(ProfilActivity.this,
                            "Error: could not fetch user.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    writeProfil(userId, user.username, nama, sDaerah, noHp, keahlian );
                }

                //kemana gitu
                Toast.makeText(ProfilActivity.this,
                        "Data kesimpen cuy",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfilActivity.this, PesananActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        });

    }

    private void writeProfil(String userId, String username, String nama, String sDaerah, String noHp, String keahlian ) {
        String key = mDatabase.child("profil").push().getKey();
        DataDiri dataDiri = new DataDiri(userId, username, nama, sDaerah, noHp, keahlian);
        Map<String, Object> postValues = dataDiri.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/profil/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}
