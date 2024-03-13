package com.motel.mobileproject_motelrental;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class Password_Page_Activity extends AppCompatActivity {

    ImageButton ibeye;
    EditText etPassword;
    Button btnDangNhap;
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_page);
        ibeye = findViewById(R.id.ibeye);
        etPassword = findViewById(R.id.etPassword);
        btnDangNhap= (Button) findViewById(R.id.btnDangNhap);
        logic();
    }
    private void logic() {
        ibeye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    ibeye.setImageResource(R.drawable.eyeclose); // Thay đổi hình ảnh của ImageButton khi ẩn mật khẩu
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    ibeye.setImageResource(R.drawable.eye); // Thay đổi hình ảnh của ImageButton khi hiển thị mật khẩu
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                isPasswordVisible = !isPasswordVisible; // Đảo ngược trạng thái
                etPassword.setSelection(etPassword.getText().length()); // Đặt con trỏ văn bản vào cuối
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwd = s.toString();
                if(pwd.isEmpty())
                    btnDangNhap.setEnabled(false);
                else
                    btnDangNhap.setEnabled(true);
            }
        });
    }

}