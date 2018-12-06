package durzoflint.mrpuncture;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OnGoingOrderActivity extends AppCompatActivity {
    public static final int CALL_REQUEST_CODE = 1;
    String phone;
    String orderid;
    String shopid;
    String userid;
    String address;
    Check check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_going_order);

        Intent intent = getIntent();
        orderid = intent.getStringExtra(HomeActivity.ORDER_ID);
        String status = intent.getStringExtra(HomeActivity.STATUS);
        userid = intent.getStringExtra(HomeActivity.USER_ID);
        shopid = intent.getStringExtra(HomeActivity.SHOP_ID);
        String name = intent.getStringExtra(HomeActivity.NAME);
        phone = intent.getStringExtra(HomeActivity.PHONE);
        address = intent.getStringExtra(HomeActivity.ADDRESS);
        String badge = intent.getStringExtra(HomeActivity.BADGE);

        updateUI(badge, status, name);

        check = new Check();
        check.execute();
    }

    private void updateUI(String badge, String status, String name) {
        ImageView badgeIV = findViewById(R.id.badge);
        TextView messageTV = findViewById(R.id.message);
        TextView phoneTV = findViewById(R.id.phone);
        TextView addressTV = findViewById(R.id.address);
        final LinearLayout call = findViewById(R.id.call);
        Button cancel = findViewById(R.id.cancel);

        switch (badge) {
            case "bronze":
                badgeIV.setImageResource(R.drawable.bronze);
                break;
            case "silver":
                badgeIV.setImageResource(R.drawable.silver);
                break;
            case "gold":
                badgeIV.setImageResource(R.drawable.gold);
                break;
            case "platinum":
                badgeIV.setImageResource(R.drawable.platinum);
                break;
        }

        String message = "";
        switch (status) {
            case "pending":
                message = "Your request is pending approval from " + name;
                break;
            case "approved":
                message = "Your request has been accepted by " + name;
                break;
            case "completed":
                message = "Your request has been completed by " + name;
                check.cancel(true);
                Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage
                        (getBaseContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case "cancelled_by_shop":
                message = "Your requested has been called by " + name;
                check.cancel(true);
                Intent intent2 = getBaseContext().getPackageManager().getLaunchIntentForPackage
                        (getBaseContext().getPackageName());
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                finish();
                break;
            case "cancelled_by_user":
                message = "Your request has been cancelled by you";
                check.cancel(true);
                Intent intent3 = getBaseContext().getPackageManager().getLaunchIntentForPackage
                        (getBaseContext().getPackageName());
                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent3);
                finish();
                break;
        }

        messageTV.setText(message);
        phoneTV.setText(phone);
        addressTV.setText("Address: " + address);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CancelRequest().execute("cancelled_by_user");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        check.cancel(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == CALL_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call();
            }
        }
    }

    class CancelRequest extends AsyncTask<String, Void, Void> {
        String baseUrl = "http://www.mrpuncture.com/app/";
        String webPage = "";

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                String myURL = baseUrl + "changeorderstatus.php?i=" + orderid + "&s=" + strings[0];
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

            new Notify().execute(shopid, ShopActivity.SHOPS, "Request Cancelled", "User cancelled request");

            Toast.makeText(OnGoingOrderActivity.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(OnGoingOrderActivity.this, HomeActivity.class));
            finish();
        }
    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    CALL_REQUEST_CODE);
        } else
            startActivity(intent);
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
    }

    class Check extends AsyncTask<Void, Void, Void> {
        String baseUrl = "http://www.mrpuncture.com/app/";
        String webPage = "";

        @Override
        protected Void doInBackground(Void... voids) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                String myURL = baseUrl + "updateorder.php?i=" + orderid;
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

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (!webPage.trim().isEmpty()) {
                String response[] = webPage.split("<br>");
                for (int i = 0, k = 0; k < response.length / 7; i += 7, k++) {
                    orderid = response[i];
                    String status = response[i + 1];
                    shopid = response[i + 2];
                    String name = response[i + 3];
                    phone = response[i + 4];
                    String email = response[i + 5];
                    String badge = response[i + 6];

                    updateUI(badge, status, name);
                }
            }

            check = new Check();
            check.execute();
        }
    }
}