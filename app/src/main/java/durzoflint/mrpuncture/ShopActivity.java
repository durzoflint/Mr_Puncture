package durzoflint.mrpuncture;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static durzoflint.mrpuncture.LoginActivity.LOGIN_PREFS;
import static durzoflint.mrpuncture.LoginActivity.USER_ID;
import static durzoflint.mrpuncture.SelectServiceActivity.SHOP;

public class ShopActivity extends AppCompatActivity {

    public static final String SHOPS = "shops";
    public static final String NUMBER = "number";
    public static final String NAME = "name";
    private ArrayList<String> shop;
    private String id, name, number;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Intent intent = getIntent();
        shop = intent.getStringArrayListExtra(SHOP);

        TextView nameT = findViewById(R.id.name);
        TextView distance = findViewById(R.id.distance);
        ImageView badge = findViewById(R.id.badge);
        Button select = findViewById(R.id.select);

        id = shop.get(0);
        name = shop.get(1);
        number = shop.get(4);

        nameT.setText(name);
        distance.setText(shop.get(2));

        switch (shop.get(3)) {
            case "bronze":
                badge.setImageResource(R.drawable.bronze);
                break;
            case "silver":
                badge.setImageResource(R.drawable.silver);
                break;
            case "gold":
                badge.setImageResource(R.drawable.gold);
                break;
            case "platinum":
                badge.setImageResource(R.drawable.platinum);
                break;
        }

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Notify().execute(id, SHOPS, "New Request", "A user needs a puncture fixed");
                SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_PREFS, Context
                        .MODE_PRIVATE);
                String id = sharedPreferences.getString(USER_ID, "");
                new PlaceOrder().execute(id);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null)
            progressDialog.dismiss();
        super.onDestroy();
    }

    private class PlaceOrder extends AsyncTask<String, Void, Void> {
        String webPage = "";
        String baseUrl = "http://www.mrpuncture.com/app/";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ShopActivity.this, "Please Wait", "Notifying " +
                    "Shop");
        }

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                String myURL = baseUrl + "placeorder.php?i=" + strings[0] + "&s=" + id;
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
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection
                        .getInputStream()));
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
            progressDialog.dismiss();
        }
    }

    class Notify extends AsyncTask<String, Void, Void> {
        String baseUrl = "http://www.mrpuncture.com/app/";
        String webPage = "";

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                String myURL = baseUrl + "sendnotification.php?i=" + strings[0] + "&t=" + strings[1]
                        + "&title=" + strings[2] + "&body=" + strings[3];
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
            Intent intent = new Intent(ShopActivity.this, StatusActivity.class);
            intent.putExtra(NAME, name);
            intent.putExtra(NUMBER, number);
            startActivity(intent);
        }
    }
}
