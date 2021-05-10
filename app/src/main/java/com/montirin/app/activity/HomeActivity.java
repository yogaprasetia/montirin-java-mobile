package com.montirin.app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.montirin.app.R;
import com.montirin.app.adapter.HomeAdapter;
import com.montirin.app.model.HomeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageSlider mainSlider;
    private static final int REQUEST_LOCATION = 1;
    RelativeLayout menuMitra;
    ArrayList<HomeItem> datahome;
    GridLayoutManager gridLayoutManager;
    HomeAdapter adapter;
    public static Double latitude;
    public static Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mainSlider = findViewById(R.id.image_slider);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "Aktifkan GPS Untuk menikmati layanan kami", Toast.LENGTH_LONG).show();
            OnGPS();
            return;
        } else {
            getLocation();
        }
        final List<SlideModel> remoteImages = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Slider")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren())
                        {
                            remoteImages.add(new SlideModel(Objects.requireNonNull(data.child("url").getValue()).toString(), ScaleTypes.FIT));
                            mainSlider.setImageList(remoteImages,ScaleTypes.FIT);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        recyclerView = findViewById(R.id.rv_home);
        addData();
        gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new HomeAdapter(datahome);
        menuMitra = findViewById(R.id.menu_mitra);
        menuMitra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null){
                    startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                } else {
                    startActivity(new Intent(HomeActivity.this,AccountActivity.class));
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Aktifkan GPS").setCancelable(false).setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    HomeActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION
            );
        } else {
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Anda Menolak Izin Lokasi, Maka Aplikasi Keluar", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(HomeActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(HomeActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0){
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double lat =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longi =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            latitude = lat;
                            longitude = longi;
                            if (latitude == null){
                                getLocation();
                                return;
                            }
                        }
                    }
                }, Looper.getMainLooper());
    }

    public void addData(){
        datahome = new ArrayList<>();
        datahome.add(new HomeItem("Servis Mobil","logohome1"));
        datahome.add(new HomeItem("Servis Motor","logohome2"));
        datahome.add(new HomeItem("Servis Elektronik","logohome3"));
        datahome.add(new HomeItem("Servis AC","logohome4"));
        datahome.add(new HomeItem("Servis HP","logohome5"));
        datahome.add(new HomeItem("Lainnya","logohome6"));
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}