package com.motel.mobileproject_motelrental;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class BasicInformation extends AppCompatActivity {

    DateFormat fmtDateAndTime = DateFormat.getDateTimeInstance();
    EditText etTieuDe, etDienTich, etGiaThue, etNgayChuyenToi, etNoiBat, etMoTa;
    ImageView btnLich;
    Button btnTiepTuc;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String formattedDate = dateFormat.format(myCalendar.getTime());

            etNgayChuyenToi.setText(formattedDate);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_information);
        etTieuDe = findViewById(R.id.etTieuDe);
        etDienTich = findViewById(R.id.etDienTich);
        etGiaThue = findViewById(R.id.etGiaThue);
        etNgayChuyenToi = findViewById(R.id.etNgayChuyenToi);
        etNoiBat = findViewById(R.id.etNoiBat);
        etMoTa = findViewById(R.id.etMoTa);
        btnLich = findViewById(R.id.btnLich);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        btnLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(BasicInformation.this, d,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}