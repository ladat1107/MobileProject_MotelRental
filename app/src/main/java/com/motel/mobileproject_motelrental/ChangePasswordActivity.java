package com.motel.mobileproject_motelrental;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePasswordActivity extends AppCompatActivity {
    Button btnBack, btnThayDoi;
    EditText etOldPass, etNewPass, etRepeatPass;

    PreferenceManager preferenceManager;
    private ProgressDialog progressDialog;
    TextInputLayout layoutOldPass, layoutNewPass, layoutRepeatPass;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btnQuenMatKhau;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thaydoimatkhau);
        preferenceManager = new PreferenceManager(getApplicationContext());
        etOldPass = findViewById(R.id.etMatKhauCu);
        etNewPass = findViewById(R.id.etMatKhauMoi);
        etRepeatPass = findViewById(R.id.etReMatKhauMoi);
        layoutOldPass = findViewById(R.id.layoutMatKhauCu);
        layoutNewPass = findViewById(R.id.layoutMatKhauMoi);
        layoutRepeatPass = findViewById(R.id.layoutReMatKhauMoi);
        EditTextAction(etNewPass, layoutNewPass);
        EditTextAction(etOldPass, layoutOldPass);
        EditTextAction(etRepeatPass, layoutRepeatPass);
        UpdatePass();
        GetBack();
        QuenMatKhau();
    }
    private void GetBack(){
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @SuppressLint("ClickableViewAccessibility")
    private void EditTextAction(EditText editText, TextInputLayout layout){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                layout.setBoxStrokeColor(Color.parseColor("#0c7094"));
                layout.setHelperText("");
                return false;
            }
        });
    }
    private void QuenMatKhau(){
        btnQuenMatKhau = findViewById(R.id.btnQuenMatKhau);
        btnQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EnterMailActivity.class);
                startActivity(intent);
            }
        });
    }
    private void UpdatePass(){
        btnThayDoi = (Button) findViewById(R.id.btnThayDoi);
        btnThayDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserPass();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(btnThayDoi.getWindowToken(), 0);
            }
        });
    }
    private  void UpdateUserPass(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating...");
        progressDialog.show();
        try {
            DocumentReference docRef = db.collection("users").document(preferenceManager.getString(Constants.KEY_USER_ID));
            if(etOldPass.getText().toString().isEmpty()){
                etOldPass.requestFocus();
                layoutOldPass.setBoxStrokeColor(Color.parseColor("#ff5d6c"));
                layoutOldPass.setHelperText("Bắt buộc!");
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                return;
            }
            else if(!preferenceManager.getString(Constants.KEY_PASSWORD).equals(etOldPass.getText().toString())){
                etOldPass.requestFocus();
                layoutOldPass.setBoxStrokeColor(Color.parseColor("#ff5d6c"));
                layoutOldPass.setHelperText("Mật khẩu sai!");
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                etOldPass.setText("");
                etNewPass.setText("");
                etRepeatPass.setText("");
                return;
            }
            else if(etNewPass.getText().toString().isEmpty()){
                etNewPass.requestFocus();
                layoutNewPass.setBoxStrokeColor(Color.parseColor("#ff5d6c"));
                layoutNewPass.setHelperText("Bắt buộc!");
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                return;
            }
            else if(etRepeatPass.getText().toString().isEmpty()) {
                etRepeatPass.requestFocus();
                layoutRepeatPass.setBoxStrokeColor(Color.parseColor("#ff5d6c"));
                layoutRepeatPass.setHelperText("Bắt buộc!");
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                return;
            }
            else if(!(etRepeatPass.getText().toString().equals(etNewPass.getText().toString()))){
                etRepeatPass.requestFocus();
                layoutRepeatPass.setBoxStrokeColor(Color.parseColor("#ff5d6c"));
                layoutRepeatPass.setHelperText("Mật khẩu không khớp!");
                etRepeatPass.setText("");
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                return;
            }
            docRef.update(Constants.KEY_PASSWORD, etRepeatPass.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    preferenceManager.putString(Constants.KEY_PASSWORD, etRepeatPass.getText().toString());
                    Toast.makeText(ChangePasswordActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                    etOldPass.clearFocus();
                    etNewPass.clearFocus();
                    etRepeatPass.clearFocus();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(ChangePasswordActivity.this, "Failed to Updated", Toast.LENGTH_SHORT).show();
        }
    }
}