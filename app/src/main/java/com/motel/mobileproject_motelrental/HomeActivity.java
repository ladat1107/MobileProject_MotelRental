package com.motel.mobileproject_motelrental;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    EditText edtSearch;
    TextView txtFillter;
    Button btnPhoBien, btnYeuThich, btnDanhGia;
    Button btnFind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        edtSearch = (EditText)findViewById(R.id.edTextSearch);
        txtFillter=(TextView) findViewById(R.id.txtViewFilter);
        btnPhoBien=(Button) findViewById(R.id.btnViewPhoBien);
        btnYeuThich=(Button) findViewById(R.id.btnViewYeuThich);
        btnDanhGia=(Button) findViewById(R.id.btnViewDanhGia);
        btnFind=(Button) findViewById(R.id.btnFind);
        //setActive(btnPhoBien, btnYeuThich, btnDanhGia);
        btnPhoBien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setActive(btnPhoBien, btnYeuThich, btnDanhGia);
                Toast.makeText(getApplicationContext(), " duong",Toast.LENGTH_LONG).show();
            }
        });

        btnYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActive(btnPhoBien, btnYeuThich, btnDanhGia);
            }
        });

        btnDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActive(btnPhoBien, btnYeuThich, btnDanhGia);
            }
        });

    }

    private void setActive(TextView ed1, TextView ed2, TextView ed3){
        float si=ed1.getTextSize();
        float newSi=si*1.5f;
        ed1.setTextSize(TypedValue.COMPLEX_UNIT_SP,newSi);
        ed2.setTextSize(10);
        ed3.setTextSize(10);
    }
}