package com.motel.mobileproject_motelrental.diachi;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.motel.mobileproject_motelrental.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityDiaChi extends AppCompatActivity {
    Spinner spTinh, spHuyen, spXa;
    DiaChiAdapter tinhAdapter, huyenAdapter, xaAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinhsuathongtin);
        SelectedTinh();
        SelectedHuyen();
        SelectedXa();
    }
    private  void SelectedTinh(){
        spTinh = (Spinner) findViewById(R.id.spTinhThanhPho);
        tinhAdapter = new DiaChiAdapter(this, R.layout.item_selected, GetListTinh(), "Tỉnh/Thành phố");
        spTinh.setAdapter(tinhAdapter);

        spTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tinhAdapter.SetSelect(true);
                Toast.makeText(ActivityDiaChi.this, tinhAdapter.getItem(position).getName(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tinhAdapter.SetSelect(false);
            }
        });
    }
    private  void SelectedHuyen(){
        spHuyen = (Spinner) findViewById(R.id.spQuanHuyen);
        huyenAdapter = new DiaChiAdapter(this, R.layout.item_selected, GetListTinh(), "Quận/Huyện");
        spHuyen.setAdapter(huyenAdapter);
        spHuyen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                huyenAdapter.SetSelect(true);
                Toast.makeText(ActivityDiaChi.this, huyenAdapter.getItem(position).getName(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                huyenAdapter.SetSelect(false);
            }
        });
    }
    private  void SelectedXa(){
        spXa = (Spinner) findViewById(R.id.spXaPhuong);
        xaAdapter = new DiaChiAdapter(this, R.layout.item_selected, GetListTinh(), "Xã/Phường");
        spXa.setAdapter(xaAdapter);
        spXa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                xaAdapter.SetSelect(true);
                Toast.makeText(ActivityDiaChi.this, xaAdapter.getItem(position).getName(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                xaAdapter.SetSelect(false);
            }
        });
    }
    private List<DiaChi> GetListTinh(){
        List<DiaChi> tinh = new ArrayList<>();
        tinh.add(new DiaChi("Đồng Nai"));
        tinh.add(new DiaChi("TP.Hồ Chí Minh"));
        tinh.add(new DiaChi("Hà Tĩnh"));
        return tinh;
    }
}
