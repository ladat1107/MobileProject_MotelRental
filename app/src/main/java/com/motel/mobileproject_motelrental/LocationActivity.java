package com.motel.mobileproject_motelrental;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.motel.mobileproject_motelrental.databinding.ActivityLocationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LocationActivity extends AppCompatActivity implements Comparator<String>, OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ActivityLocationBinding binding;
    GoogleMap ggmap;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker currentMarker;
    private List<String> provinceList, districtList, wardList;
    private static final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;
    private Spinner spinnerProvince, spinnerDistrict, spinnerWard;
    SearchView searchView;
    double latitude =0;
    double longitude=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        spinnerProvince = findViewById(R.id.cmbTinh);
        spinnerDistrict = findViewById(R.id.cmbQuan);
        spinnerWard = findViewById(R.id.cmbXa);
        loadJSONData();
        setUpSpinners();
        // Initialize search view------------------------------------------
        searchView = findViewById(R.id.search_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.ggmap);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(LocationActivity.this);
        getLastLocation();
        binding.btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
        binding.txtDiaChi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    moveCameraToSelectedWard(binding.txtDiaChi.getText().toString() + ", " + spinnerWard.getSelectedItem().toString() + ", " +
                            spinnerDistrict.getSelectedItem().toString() + ", " + spinnerProvince.getSelectedItem().toString());
                    return true;
                }
                return false;
            }
        });
        spinnerWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                   moveCameraToSelectedWard(spinnerWard.getSelectedItem().toString() + ", " +
                            spinnerDistrict.getSelectedItem().toString() + ", " + spinnerProvince.getSelectedItem().toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.btnVtHienTai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addresses = null;
                if (location != null) {
                    Geocoder geocoder = new Geocoder(LocationActivity.this);
                    try {
                        addresses = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        // Xóa Marker cũ nếu tồn tại
                        if (currentMarker != null) {
                            currentMarker.remove();
                        }
                        // Thêm Marker mới
                        currentMarker = ggmap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(location)
                                .draggable(false)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_icon_marker)));
                        ggmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        ggmap = googleMap;
        //Cho phép di chuyển Bản đồ
        ggmap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                // Lấy vị trí mới của trung tâm bản đồ
                LatLng newLatLng = ggmap.getCameraPosition().target;
                updateMarkerPosition(newLatLng);
            }
        });
    }

    @Override
    public void onCameraIdle() {
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(LocationActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(LocationActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_LOCATION);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(LocationActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    // Xóa Marker cũ nếu tồn tại
                    if (currentMarker != null) {
                        currentMarker.remove();
                    }
                    // Thêm Marker mới
                    currentMarker = ggmap.addMarker(new MarkerOptions().position(latLng)
                            .title("Your current location")
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_icon_marker)));
                    ggmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                }
            }
        });
    }
    //---------------------------------KẾT THÚC OnCreate-----------------------------------------------------------------

    // Phương thức để cập nhật vị trí của Marker
    private void updateMarkerPosition(LatLng latLng) {
        // Kiểm tra xem Marker đã được tạo hay chưa
        if (currentMarker == null) {
            currentMarker = ggmap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Current Location")
                    .draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.img_icon_marker))
            );
        } else {
            currentMarker.setPosition(latLng);
        }
        latitude = latLng.latitude;
        longitude = latLng.longitude;
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
        Collections.sort(provinceList, LocationActivity.this);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinceList);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(provinceAdapter);
        spinnerProvince.setSelection(-1);
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProvince = (String) parent.getItemAtPosition(position);
                loadDistricts(selectedProvince);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        spinnerDistrict.setAdapter(districtAdapter);
        spinnerDistrict.setSelection(-1);
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
        spinnerWard.setAdapter(wardAdapter);
        spinnerWard.setSelection(-1);
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
    private void moveCameraToSelectedWard(String selectedWard) {
        // Kiểm tra xem đã có vị trí của spinnerWard chưa
        if (ggmap != null && !selectedWard.equals("-- Chọn phường/xã --")) {
            // Tìm vị trí của phường/xã trên bản đồ
            Geocoder geocoder = new Geocoder(this);
            try {
                List<Address> addresses = geocoder.getFromLocationName(selectedWard, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    ggmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn chắc chắn đã ghim đúng vị trí trên bản đồ?");
        builder.setIcon(R.drawable.ld_notification);

        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý khi người dùng xác nhận đã ghim đúng vị trí
                // Ví dụ: Gọi một phương thức hoặc thực hiện hành động tiếp theo ở đây
            }
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý khi người dùng chọn không
                dialog.dismiss(); // Đóng hộp thoại
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
