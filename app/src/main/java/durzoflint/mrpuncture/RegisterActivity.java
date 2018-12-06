package durzoflint.mrpuncture;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {
    EditText name, email, phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    new Register().execute(name.getText().toString(), email.getText().toString(),
                            phone.getText().toString(), password.getText().toString());
                }
            }
        });
    }

    private boolean validate() {
        if (name.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "The name cannot empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (email.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "The Email cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phone.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "The Phone cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "The password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    class Register extends AsyncTask<String, Void, Void> {
        String baseUrl = "http://www.mrpuncture.com/app/";
        String webPage = "";

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                String myURL = baseUrl + "adduser.php?name=" + strings[0] + "&email=" +
                        strings[1] + "&phone=" + strings[2] + "&pass=" + strings[3];
                myURL = myURL.replaceAll(" ", "%20");
                myURL = myURL.replaceAll("\'", "%27");
                myURL = myURL.replaceAll("\'", "%22");
                myURL = myURL.replaceAll("\\+", "%2B");
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

            if (!webPage.equals("success")) {
                Toast.makeText(RegisterActivity.this, "Some Error Occured while registering", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        }
    }
}