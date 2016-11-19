package com.wisnu.paktukang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PesanTukang extends BaseActivity {

    //public static final String EXTRA_POST_KEY = "post_key";

    private EditText mNama;
    private EditText mNoHp;
    private EditText mEmail;
    private EditText mAlamat;
    private EditText mPesan;

    private Button bOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_tukang);

        mNama = (EditText) findViewById(R.id.nama_p);
        mNoHp = (EditText) findViewById(R.id.nomer_hp_p);
        mEmail = (EditText) findViewById(R.id.email_p);
        mAlamat = (EditText) findViewById(R.id.alamat_p);
        mPesan = (EditText) findViewById(R.id.pesan_p);

        bOrder = (Button) findViewById(R.id.order_p);

        bOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PesanTukang.this, UserAreaActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
