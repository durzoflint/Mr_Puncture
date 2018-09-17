package durzoflint.mrpuncture;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static durzoflint.mrpuncture.SelectRaduisActivity.SEARCH_RADIUS;
import static durzoflint.mrpuncture.SelectVehicalType.VEHICAL_TYPE;

public class SelectServiceActivity extends AppCompatActivity {

    int vehicalType, searchRadius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);

        Intent intent = getIntent();

        vehicalType = intent.getIntExtra(VEHICAL_TYPE, 0);
        searchRadius = intent.getIntExtra(SEARCH_RADIUS, 0);

        //new FetchServices().execute("", "", "", "", "");
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
    }
}
