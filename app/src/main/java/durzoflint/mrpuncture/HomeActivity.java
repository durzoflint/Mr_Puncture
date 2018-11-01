package durzoflint.mrpuncture;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import durzoflint.mrpuncture.firebase.MyFirebaseMessagingService;

import static durzoflint.mrpuncture.LoginActivity.LOGIN_PREFS;
import static durzoflint.mrpuncture.LoginActivity.USER_ID;
import static durzoflint.mrpuncture.firebase.MyFirebaseMessagingService.TOKEN;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String LONGI = "longi";
    public static final String LATI = "lati";
    int REQUEST_LOCATION = 1;
    LatLng target;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private View mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(USER_ID, "");
        if (id.isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            SharedPreferences firebasePreferences = getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE);
            String token = firebasePreferences.getString(TOKEN, "");
            if (!token.isEmpty()) {
                new SendTokenToServer().execute(token, id);
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

        Button useThis = findViewById(R.id.use_this);
        useThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                target = mMap.getCameraPosition().target;
                Intent intent = new Intent(HomeActivity.this, SelectVehicalTypeActivity.class);
                intent.putExtra(LATI, target.latitude + "");
                intent.putExtra(LONGI, target.longitude + "");
                startActivity(intent);
            }
        });
    }

    class SendTokenToServer extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String baseUrl = "http://www.mrpuncture.com/app/";
            String webPage = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                String myURL = baseUrl + "addfirebaseid.php?i=" + strings[1] + "&t=" + MyFirebaseMessagingService.USERS + "&s=" + strings[0];
                myURL = myURL.replaceAll(" ", "%20");
                myURL = myURL.replaceAll("\'", "%27");
                myURL = myURL.replaceAll("\'", "%22");
                myURL = myURL.replaceAll("\\(", "%28");
                myURL = myURL.replaceAll("\\)", "%29");
                myURL = myURL.replaceAll("\\{", "%7B");
                myURL = myURL.replaceAll("\\}", "%7B");
                myURL = myURL.replaceAll("\\]", "%22");
                myURL = myURL.replaceAll("\\[", "%22");
                url = new URL(myURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String data;
                while ((data = br.readLine()) != null)
                    webPage = webPage + data;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableUserLocation();
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            //To Place the MyLocationButton to the bottom right
            if (mapView != null &&
                    mapView.findViewById(Integer.parseInt("1")) != null) {
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                        locationButton.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                layoutParams.setMargins(0, 0, 30, 30);
            }

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng user = new LatLng(location.getLatitude(), location.getLongitude());

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user, 16f));
                    } else {
                        /*LatLng fipola = new LatLng(13.092498, 80.2179698);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fipola, 16f));*/
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.previous:
                startActivity(new Intent(this, PreviousServicesActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
            }
        }
    }
}
