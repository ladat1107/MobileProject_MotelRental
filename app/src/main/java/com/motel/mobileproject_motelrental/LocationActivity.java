package com.motel.mobileproject_motelrental;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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

public class LocationActivity extends AppCompatActivity implements Comparator<String>,OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private ActivityLocationBinding binding;
    GoogleMap ggmap;
    //FloatingActionButton btnVtHienTai;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Marker currentMarker;
    private List<String> provinceList,districtList, wardList;
    private static final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;
    private Spinner spinnerProvince, spinnerDistrict, spinnerWard;
    SearchView searchView;
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
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Xử lý vị trí được trả về
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    // Đã có vị trí hiện tại
                }
            }
        };
        binding.txtDiaChi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchView.setQuery(binding.txtDiaChi.getText().toString() + ", " + spinnerWard.getSelectedItem().toString() + ", " +
                            spinnerDistrict.getSelectedItem().toString() + ", " + spinnerProvince.getSelectedItem().toString(), false);
                    return true;
                }
                return false;
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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String location = searchView.getQuery().toString();
                List<Address> addresses = null;
                if (location!=null){
                    Geocoder geocoder = new Geocoder(LocationActivity.this);
                    try {
                        addresses = geocoder.getFromLocationName(location,1);
                    } catch (IOException e){
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
                        currentMarker = ggmap.addMarker(new MarkerOptions().position(latLng).title(location).draggable(false));
                        ggmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                    }
                }
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
                // Cập nhật vị trí của Marker
                updateMarkerPosition(newLatLng);
            }
        });;
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
                    currentMarker = ggmap.addMarker(new MarkerOptions().position(latLng).title("Your current location").draggable(false));
                    ggmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                }
            }
        });




    }
    //---------------------------------KẾT THÚC OnCreate-----------------------------------------------------------------

    // Phương thức để cập nhật vị trí của Marker
    private void updateMarkerPosition(LatLng latLng) {
        // Kiểm tra xem Marker đã được tạo hay chưa
        if (currentMarker == null) {
            // Nếu chưa, tạo mới Marker
            currentMarker = ggmap.addMarker(new MarkerOptions().position(latLng).title("Current Location").draggable(false));
        } else {
            // Nếu đã có, cập nhật vị trí của Marker
            currentMarker.setPosition(latLng);
        }
        //Toast.makeText(this, "Vị trí mới: +" + latLng.latitude + ":" + latLng.longitude, Toast.LENGTH_SHORT).show();
        // Ở đây bạn có thể thực hiện việc lưu trữ vị trí mới vào một biến toàn cục hoặc cơ sở dữ liệu nào đó phù hợp với ứng dụng của bạn.
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

        Collections.sort(provinceList, LocationActivity.this);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, provinceList);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(provinceAdapter);


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
        // Find the province object in JSON data
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
        spinnerWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchView.setQuery(spinnerWard.getSelectedItem().toString()+", "+ spinnerDistrict.getSelectedItem().toString()
                        +", "+spinnerProvince.getSelectedItem().toString(),false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadWards(String selectedDistrict) {
        wardList = new ArrayList<>();
        // Find the district object in JSON data
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

        // So sánh theo thứ tự từ A đến Z
        return city1.compareToIgnoreCase(city2);
    }
}