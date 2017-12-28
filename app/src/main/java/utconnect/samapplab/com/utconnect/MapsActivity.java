package utconnect.samapplab.com.utconnect;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import android.location.Location;
import android.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener {

    private GoogleMap mMap;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference refServices = rootRef.child("ShopService");


    HashMap<String,Object> serviceData = new HashMap<String,Object>();

    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,1,1);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        refServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mMap.clear();

                serviceData = (HashMap<String, Object>) dataSnapshot.getValue();
                for (Object obj: serviceData.values()){
                    String name = (String)((HashMap<String,Object>) obj).get("name");
                    String campus = (String)((HashMap<String,Object>) obj).get("campus");
                    String address = (String)((HashMap<String,Object>) obj).get("address");
                    String waitStat = (String)((HashMap<String,Object>) obj).get("number");
                    HashMap<String,String> location = (HashMap<String,String>)((HashMap<String,Object>) obj).get("location");
                    String latitude = location.get("latitude");
                    String longitude = location.get("longitude");

                    String statusWait;
                    if (waitStat.equals("0.0")){
                        statusWait = "(Closed)";
                    }
                    else if (waitStat.equals("1.0")){
                        statusWait = "(Open)";
                    }
                    else if (waitStat.equals("2.0")){
                        statusWait = "(Open)";
                    }
                    else if (waitStat.equals("3.0")){
                        statusWait = "(Moderate)";
                    }
                    else if (waitStat.equals("4.0")){
                        statusWait = "(Packed)";
                    }
                    else if (waitStat.equals("5.0")){
                        statusWait = "(Full)";
                    }
                    else{
                        statusWait = "Normal";
                    }


                    LatLng service = new LatLng(Double.valueOf(latitude),Double.valueOf(longitude));
                    MarkerOptions serviceMark = new MarkerOptions();
                    serviceMark.position(service);
                    serviceMark.title(name+statusWait+" "+waitStat);
                    mMap.addMarker(serviceMark);


                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,1,1);
        mMap.setMyLocationEnabled(true);
        //mMap.moveCamera();




    }

    private boolean doesUserHavePermission()
    {
        int result = getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;

    }
}
