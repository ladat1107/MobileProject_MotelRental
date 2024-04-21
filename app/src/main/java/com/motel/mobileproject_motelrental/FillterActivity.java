package com.motel.mobileproject_motelrental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.material.slider.RangeSlider;
import com.motel.mobileproject_motelrental.databinding.ActivityFillterBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FillterActivity extends AppCompatActivity implements Comparator<String> {
    private ActivityFillterBinding binding;
    private List<String> provinceList, districtList, wardList;
    DecimalFormat decimalFormat = new DecimalFormat("#,###");
    int gia = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFillterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadJSONData();
        setUpSpinners();

        binding.sliderGia.setValueFrom(0);
        binding.sliderGia.setValueTo(500);

        List<String> listDichVu = new ArrayList<>();
        listDichVu.add("Tất cả");
        listDichVu.add("Phòng trọ");
        listDichVu.add("Chung cư");
        listDichVu.add("Nhà nguyên căn");
        listDichVu.add("Ở ghép");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listDichVu);
        binding.cmbDichVu.setAdapter(adapter);
        binding.imgBack.setOnClickListener(v -> onBackPressed());
        binding.sliderGia.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(RangeSlider slider, float value, boolean fromUser) {
                // Lấy giá trị từ RangeSlider
                int minValue = binding.sliderGia.getValues().get(0).intValue() * 100000;
                int maxValue = binding.sliderGia.getValues().get(1).intValue() * 100000;

                // Format giá trị
                String formattedMinValue = decimalFormat.format(minValue);
                String formattedMaxValue = decimalFormat.format(maxValue);

                // Cập nhật giá trị cho EditText tương ứng
                binding.editGiaFrom.setText(formattedMinValue);
                binding.editGiaTo.setText(formattedMaxValue);
            }
        });

        binding.btnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> infoFill = new ArrayList<>();
                //Lấy giá trị từ spinner
                String tinh = binding.cmbTinh.getSelectedItem().toString();
                String quan = binding.cmbQuan.getSelectedItem().toString();
                String xa = binding.cmbXa.getSelectedItem().toString();

                infoFill.add(tinh);
                infoFill.add(quan);
                infoFill.add(xa);

                //Lấy giá trị từ RangeSlider
                int minValue = binding.sliderGia.getValues().get(0).intValue() * 100000;
                int maxValue = binding.sliderGia.getValues().get(1).intValue() * 100000;

                infoFill.add(String.valueOf(minValue));
                infoFill.add(String.valueOf(maxValue));

                //Lấy giá trị từ loại dịch vụ
                String loaiDichVu = binding.cmbDichVu.getSelectedItem().toString();
                infoFill.add(loaiDichVu);

                // Lấy giá trị từ ListChip
                if (binding.chiptulanh.isChecked()) {
                    infoFill.add("Tủ lạnh");
                }
                if (binding.chipmaylanh.isChecked()) {
                    infoFill.add("Máy lạnh");
                }
                if (binding.chipmaygiat.isChecked()) {
                    infoFill.add("Máy giặt");
                }
                if (binding.chipwifi.isChecked()) {
                    infoFill.add("Wifi");
                }
                if (binding.chipgac.isChecked()) {
                    infoFill.add("Có gác");
                }
                if (binding.chipgio.isChecked()) {
                    infoFill.add("Giờ giấc quy định");
                }
                if (binding.chipdexe.isChecked()) {
                    infoFill.add("Chỗ để xe");
                }

                String[] arrListChip = new String[infoFill.size()];
                for (int i = 0; i < infoFill.size(); i++) {
                    String chip = infoFill.get(i);
                    arrListChip[i] = chip;
                }

                int putType = 1;

                Intent intent = new Intent(FillterActivity.this, Fillter2Activity.class);
                intent.putExtra("putType", putType);
                intent.putExtra("infoFill", arrListChip);
                startActivity(intent);
            }
        });
    }

    private void loadJSONData() {
        try {
            InputStream inputStream = getAssets().open("data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            parseJSON(json);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseJSON(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray dataArray = jsonObject.getJSONArray("data");

        provinceList = new ArrayList<>();
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject provinceObject = dataArray.getJSONObject(i);
            String provinceName = provinceObject.getString("name");
            provinceList.add(provinceName);
        }
    }

    private void setUpSpinners() {
        provinceList.add("-- Chọn tỉnh/thành phố --");
        Collections.sort(provinceList, FillterActivity.this);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinceList);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.cmbTinh.setAdapter(provinceAdapter);
        binding.cmbTinh.setSelection(-1);
        binding.cmbTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProvince = (String) parent.getItemAtPosition(position);
                loadDistricts(selectedProvince);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.cmbQuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDistrict = (String) parent.getItemAtPosition(position);
                loadWards(selectedDistrict);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadDistricts(String selectedProvince) {
        districtList = new ArrayList<>();
        districtList.add("--Chọn quận/huyện --");
        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset("data.json"));
            JSONArray dataArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject provinceObject = dataArray.getJSONObject(i);
                String provinceName = provinceObject.getString("name");
                if (provinceName.equals(selectedProvince)) {
                    JSONArray districtArray = provinceObject.getJSONArray("level2s");
                    for (int j = 0; j < districtArray.length(); j++) {
                        JSONObject districtObject = districtArray.getJSONObject(j);
                        String districtName = districtObject.getString("name");
                        districtList.add(districtName);
                    }
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districtList);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.cmbQuan.setAdapter(districtAdapter);
        binding.cmbQuan.setSelection(-1);
    }

    private void loadWards(String selectedDistrict) {
        wardList = new ArrayList<>();
        wardList.add("-- Chọn xã/phường --");
        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset("data.json"));
            JSONArray dataArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject provinceObject = dataArray.getJSONObject(i);
                JSONArray districtArray = provinceObject.getJSONArray("level2s");
                for (int j = 0; j < districtArray.length(); j++) {
                    JSONObject districtObject = districtArray.getJSONObject(j);
                    String districtName = districtObject.getString("name");
                    if (districtName.equals(selectedDistrict)) {
                        JSONArray wardArray = districtObject.getJSONArray("level3s");
                        for (int k = 0; k < wardArray.length(); k++) {
                            JSONObject wardObject = wardArray.getJSONObject(k);
                            String wardName = wardObject.getString("name");
                            wardList.add(wardName);
                        }
                        break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> wardAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, wardList);
        wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.cmbXa.setAdapter(wardAdapter);
        binding.cmbXa.setSelection(-1);
    }

    private String loadJSONFromAsset(String filename) {
        String json = null;
        try {
            InputStream inputStream = getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public int compare(String s1, String s2) {
        String city1 = s1.replaceAll("^(Thành phố|Tỉnh)\\s*", "");
        String city2 = s2.replaceAll("^(Thành phố|Tỉnh)\\s*", "");
        return city1.compareToIgnoreCase(city2);
    }
}