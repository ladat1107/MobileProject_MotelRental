package com.motel.mobileproject_motelrental;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.motel.mobileproject_motelrental.Adapter.InfoMotelAdapter;
import com.motel.mobileproject_motelrental.Interface.OnItemClickListener;
import com.motel.mobileproject_motelrental.Item.InfoMotelItem;
import com.motel.mobileproject_motelrental.databinding.ActivityMapBinding;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String TAG = "MapActivity";
    private GoogleMap googleMap;
    private ActivityMapBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker currentMarker;
    double latitude = 0;
    double longitude = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Marker> markerList = new ArrayList<>();
    List<InfoMotelItem> motelList = new ArrayList<>();
    InfoMotelAdapter adapterPhoBien = new InfoMotelAdapter(motelList);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getStringExtra("lo") != null)
            longitude = Double.parseDouble(getIntent().getStringExtra("lo"));
        if (getIntent().getStringExtra("la") != null)
            latitude = Double.parseDouble(getIntent().getStringExtra("la"));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMapHome);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);
        binding.btnHienTai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });
        binding.searchViewHome.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = binding.searchViewHome.getQuery().toString();
                List<Address> addresses = null;
                if (location != null) {
                    Geocoder geocoder = new Geocoder(MapActivity.this);
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
                        currentMarker = googleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .draggable(false)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_ld_current_marker)));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                        latitude = address.getLatitude();
                        longitude = address.getLongitude();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        if (longitude != 0 || latitude != 0) {
            LatLng latLng = new LatLng(latitude, longitude);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
        } else {
            getLastLocation();
        }

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                //LatLng newLatLng = googleMap.getCameraPosition().target;
            }
        });
        addPermanentMarkers();
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String id = (String) marker.getTag();
                for (Marker marker1 : markerList) {
                    if (marker1.getTag() != null && marker1.getTag().equals(id)) {
                        binding.recyclerView.scrollToPosition(markerList.indexOf(marker1));
                    }
                }
                return false;
            }
        });
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                // Lấy id từ tag của marker
                String id = (String) marker.getTag();

                Intent intent = new Intent(MapActivity.this, DetailRomeActivity.class);
                intent.putExtra("motelId", id);
                startActivity(intent);
            }
        });
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(MapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MapActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(MapActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if (currentMarker != null) {
                        currentMarker.remove();
                    }
                    currentMarker = googleMap.addMarker(new MarkerOptions().position(latLng)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_ld_current_marker)));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                }
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        });
    }

    private void addPermanentMarkers() {
        LinearLayoutManager layoutManagerPhoBien = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(layoutManagerPhoBien);
        Query query = db.collection(Constants.KEY_COLLECTION_MOTELS).whereEqualTo(Constants.KEY_STATUS_MOTEL, true);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        double la = document.getDouble(Constants.KEY_LATITUDE);
                        double lo = document.getDouble(Constants.KEY_LONGTITUDE);
                        String motelAddress = document.getString(Constants.KEY_MOTEL_NUMBER) + ", " + document.getString(Constants.KEY_WARD_NAME) + ", " + document.getString(Constants.KEY_DISTRICT_NAME) + ", " + document.getString(Constants.KEY_CITY_NAME);
                        int like = document.getLong(Constants.KEY_COUNT_LIKE).intValue();
                        String title = document.getString(Constants.KEY_TITLE);
                        LatLng latLng = new LatLng(la, lo);
                        long price = document.getLong(Constants.KEY_PRICE);
                        List<String> imageUrls = (List<String>) document.get(Constants.KEY_IMAGE_LIST);
                        String imgRes = imageUrls.get(0);

                        InfoMotelItem motel = new InfoMotelItem(id, imgRes, title, like, price, motelAddress, 0, true);
                        motelList.add(motel);

                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(title)
                                .draggable(false)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_icon_marker)));
                        marker.setTag(id);
                        markerList.add(marker);
                    }
                    binding.recyclerView.setAdapter(adapterPhoBien);
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }

            }

        });

        adapterPhoBien.setOnItemRecycleClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String motelId = motelList.get(position).getId();
                Marker marker = selectMarkerById(motelId);
                if (marker != null) {
                    moveCameraToMarker(marker);
                }
            }
        });

    }

    private void moveCameraToMarker(Marker marker) {
        LatLng markerPosition = marker.getPosition();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 18));
    }

    private Marker selectMarkerById(String id) {
        for (Marker marker : markerList) {
            if (marker.getTag() != null && marker.getTag().equals(id)) {
                return marker;
            }
        }
        return null;
    }


}