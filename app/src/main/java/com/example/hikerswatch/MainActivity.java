package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    TextView textViewLat, textViewLon, textViewAddress, textViewAlt, textViewAcc;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    //ask for location permission, if permission is granted get GPS data
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(grantResults.length >0 && grantResults[0]  == PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateLocation();
    }

    public void updateLocation(){
        //create textview for each GPS data set
        textViewLat = findViewById(R.id.textLat);
        textViewLon = findViewById(R.id.textLon);
        textViewAcc = findViewById(R.id.textAcc);
        textViewAlt = findViewById(R.id.textAlt);
        textViewAddress = findViewById(R.id.textAddress);

        //get location service
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //find current location
                //LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                //record current location
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                Log.i("My location ", location.toString());
                List<Address> addressList = null;
                try {
                    addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addressList != null && addressList.size() > 0) {
                    String fullAddress = "";

                    textViewLat.setText("Latitude: " + location.getLatitude());
                    textViewLon.setText("Longitude: " + location.getLongitude());
                    textViewAcc.setText("Accuracy: " + location.getAccuracy());
                    textViewAlt.setText("Altitude: " + location.getAltitude());
                    fullAddress += addressList.get(0).getAddressLine(0);
                    textViewAddress.setText("Address: " + fullAddress + "\r\n");
                    Log.i("address line ", fullAddress);
                    //Toast.makeText(MapsActivity.this, fullAddress, Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }
            @Override
            public void onProviderEnabled(String s) {
            }
            @Override
            public void onProviderDisabled(String s) {
            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            //Location lastknownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }

    }
}
