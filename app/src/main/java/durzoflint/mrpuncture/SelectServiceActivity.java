package durzoflint.mrpuncture;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import durzoflint.mrpuncture.Adapters.RecyclerViewItemClickListener;
import durzoflint.mrpuncture.Adapters.Recycler_View_Adapter;
import durzoflint.mrpuncture.Adapters.Store;

import static durzoflint.mrpuncture.SelectRaduisActivity.SEARCH_RADIUS;
import static durzoflint.mrpuncture.SelectVehicalType.VEHICAL_TYPE;

public class SelectServiceActivity extends AppCompatActivity {

    String vehicalType, searchRadius;
    List<Store> stores;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Intent intent = getIntent();

        vehicalType = intent.getIntExtra(VEHICAL_TYPE, 0) + "";
        searchRadius = intent.getIntExtra(SEARCH_RADIUS, 0) + "";

        enableUserLocation();
    }

    private void setupRecyclerView(List<Store> stores) {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Recycler_View_Adapter(stores, this, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                int id = view.getId();
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                new FetchServices().execute("test@user.com", vehicalType, searchRadius,
                                        location.getLatitude() + "", location.getLongitude() + "");
                            } else {
                                Toast.makeText(SelectServiceActivity.this, "Location Cannot be Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
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

    class FetchServices extends AsyncTask<String, Void, Void> {
        String baseUrl = "http://www.mrpuncture.com/app/";
        String webPage = "";

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                String myURL = baseUrl + "fetchshops.php?e=" + strings[0] + "&v=" + strings[1]
                        + "&r=" + strings[2] + "&l1=" + strings[3] + "&l2=" + strings[4];
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String response[] = webPage.split("<br>");
            stores = new ArrayList<>();
            for (int i = 0, k = 0; k < response.length / 3; i += 3, k++)
                stores.add(new Store(response[i], response[i + 1], response[i + 2]));
            setupRecyclerView(stores);
        }
    }
}
