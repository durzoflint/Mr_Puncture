package durzoflint.mrpuncture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static durzoflint.mrpuncture.ShopActivity.NAME;
import static durzoflint.mrpuncture.ShopActivity.NUMBER;

public class StatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        Intent intent = getIntent();
        String name = intent.getStringExtra(NAME);
        String number = intent.getStringExtra(NUMBER);

        TextView status = findViewById(R.id.status);
        status.setText(name + " has been notified.\n\n Contact: " + number);
    }
}