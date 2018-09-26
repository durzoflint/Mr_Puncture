package durzoflint.mrpuncture;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

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

import static durzoflint.mrpuncture.HomeActivity.LATI;
import static durzoflint.mrpuncture.HomeActivity.LONGI;
import static durzoflint.mrpuncture.SelectRaduisActivity.SEARCH_RADIUS;
import static durzoflint.mrpuncture.SelectVehicalTypeActivity.VEHICAL_TYPE;

public class SelectServiceActivity extends AppCompatActivity {

    public static final String SHOP = "SHOP";
    String vehicalType, searchRadius, lati, longi;
    List<Store> stores;
    LinearLayout progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);

        Intent intent = getIntent();
        lati = intent.getStringExtra(LATI);
        longi = intent.getStringExtra(LONGI);
        vehicalType = intent.getIntExtra(VEHICAL_TYPE, 0) + "";
        searchRadius = intent.getIntExtra(SEARCH_RADIUS, 0) + "";

        progress = findViewById(R.id.progress_layout);
        progress.setVisibility(View.VISIBLE);

        new FetchServices().execute("test@user.com", vehicalType, searchRadius, lati, longi);
    }

    private void setupRecyclerView(final List<Store> stores) {
        progress.setVisibility(View.GONE);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Recycler_View_Adapter(stores, this, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ArrayList<String> shop = new ArrayList<>();
                shop.add(stores.get(position).id);
                shop.add(stores.get(position).name);
                shop.add(stores.get(position).distance);
                shop.add(stores.get(position).badge);
                shop.add(stores.get(position).number);

                Intent intent = new Intent(SelectServiceActivity.this, ShopActivity.class);
                intent.putExtra(SHOP, shop);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
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
            for (int i = 0, k = 0; k < response.length / 5; i += 5, k++) {
                double distance = Double.parseDouble(response[i + 2]);
                String text;
                if (distance < 1 && distance > 0.005)
                    text = Math.round(distance * 1000) + " meters away";
                else if (distance < 0.005)
                    text = "Less than 5 meters away";
                else
                    text = Math.round(distance) + " km away";
                stores.add(new Store(response[i], response[i + 1], text, response[i + 3], response[i + 4]));
            }
            setupRecyclerView(stores);
        }
    }
}