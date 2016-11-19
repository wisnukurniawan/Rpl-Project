package com.wisnu.paktukang2;

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

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private EditText mFieldEmail;
    private EditText mFIeldPassword;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFieldEmail = (EditText) findViewById(R.id.f_email);
        mFIeldPassword = (EditText) findViewById(R.id.f_password);

        findViewById(R.id.b_masuk).setOnClickListener(this);
        findViewById(R.id.tvDaftar).setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_masuk:
                signIn();
                break;
            case R.id.tvDaftar:
                Intent intent = new Intent(LoginActivity.this, DaftarActivity.class);
                startActivity(intent);
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

    private void signIn(){
        Log.d(TAG, "Signin: ");
        if(!validateForm()){
            return;
        }
        showProgressDialog();
        String email = mFieldEmail.getText().toString();
        String password = mFIeldPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "sign in with email:on complete: "+ task.isSuccessful());
                        hideProgressDialog();

                        if(task.isSuccessful()){
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.gagal_autentifikasi,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        // Go to MainActivity
        startActivity(new Intent(LoginActivity.this, ProfilActivity.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);
        mDatabase.child("users1").child(userId).setValue(user);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mFieldEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mFieldEmail.setError(getString(R.string.wajib));
            valid = false;
        } else {
            mFieldEmail.setError(null);
        }

        String password = mFIeldPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mFIeldPassword.setError(getString(R.string.wajib));
            valid = false;
        } else {
            mFIeldPassword.setError(null);
        }

        if (password.length() < 6) {
            mFIeldPassword.setError(getString(R.string.minimal_6_kar));
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