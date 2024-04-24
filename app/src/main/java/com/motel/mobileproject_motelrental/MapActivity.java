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

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.motel.mobileproject_motelrental.Item.InfoMotelItem;
import com.motel.mobileproject_motelrental.databinding.ActivityMapBinding;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String TAG = "MapActivity";
    private GoogleMap googleMap;
    private ActivityMapBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker currentMarker;
    double latitude = 0;
    double longitude = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMapHome);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);

        binding.btnHienTai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
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
        getLastLocation();
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                //LatLng newLatLng = googleMap.getCameraPosition().target;
            }
        });
        addPermanentMarkers();
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onInfoWindowClick(Marker marker) {
                PopupMenu popupMenu = new PopupMenu(MapActivity.this, binding.getRoot(), Gravity.CENTER);
                popupMenu.inflate(R.menu.ld_menu_marker);
                try {
                    popupMenu.getClass().getDeclaredMethod("setForceShowIcon", boolean.class).invoke(popupMenu, true);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemIdid = item.getItemId();
                        if (itemIdid == R.id.action_view_details) {
                            // Lấy id từ tag của marker
                            String id = (String) marker.getTag();
                            Intent intent = new Intent(MapActivity.this, DetailRomeActivity.class);
                            intent.putExtra("motelId", id);
                            startActivity(intent);
                            return true;
                        }
                        else {
                                return false;
                        }
                    }
                });
                popupMenu.show();
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
        Query query = db.collection(Constants.KEY_COLLECTION_MOTELS).whereEqualTo(Constants.KEY_STATUS_MOTEL, true);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        double la = document.getDouble(Constants.KEY_LATITUDE);
                        double lo = document.getDouble(Constants.KEY_LONGTITUDE);
                        String title = document.getString(Constants.KEY_TITLE);
                        LatLng latLng = new LatLng(la, lo);
                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(title)
                                .draggable(false)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_icon_marker)));
                        marker.setTag(id);
                    }

                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}