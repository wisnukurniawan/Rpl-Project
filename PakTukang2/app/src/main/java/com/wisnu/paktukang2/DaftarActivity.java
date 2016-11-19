package com.wisnu.paktukang2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wisnu.paktukang2.model.User;

public class DaftarActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabase;

    private EditText mEmail;
    private EditText mPassword;
    private EditText mKonfirmPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        mEmail = (EditText) findViewById(R.id.fEmail);
        mPassword = (EditText) findViewById(R.id.fPassword);
        mKonfirmPassword =(EditText) findViewById(R.id.fKonfirmPassword);

        findViewById(R.id.bbDaftar).setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mKonfirmPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bbDaftar:
                createAccount();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount() {
        Log.d(TAG, "sign up");
        if (!validateForm()) {
            return;
        }

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if(task.isSuccessful()){
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(DaftarActivity.this, R.string.gagal_autentifikasi,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        writeNewUser(user.getUid(), username, user.getEmail());
        startActivity(new Intent(DaftarActivity.this, ProfilActivity.class));
        finish();

    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users1").child(userId).setValue(user);
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean valid = true;
        String email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.wajib));
            valid = false;
        } else {
            mEmail.setError(null);
        }

        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.wajib));
            valid = false;
        } else {
            mPassword.setError(null);
        }

        String konfirmPassword = mKonfirmPassword.getText().toString();
        if (TextUtils.isEmpty(konfirmPassword)) {
            mKonfirmPassword.setError(getString(R.string.wajib));
            valid = false;
        } else {
            mKonfirmPassword.setError(null);
        }

        if (password.length() < 6) {
            mPassword.setError(getString(R.string.minimal_6_kar));
            valid = false;
        } else if (!password.equals(konfirmPassword)) {
            mPassword.setError(getString(R.string.password_tidak_sama));
            valid = false;
        }

        return valid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }
}
