package durzoflint.mrpuncture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import static durzoflint.mrpuncture.HomeActivity.LATI;
import static durzoflint.mrpuncture.HomeActivity.LONGI;

public class SelectVehicalTypeActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String VEHICAL_TYPE = "vehical_type";
    String lati, longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vehical_type);

        Intent intent = getIntent();
        lati = intent.getStringExtra(LATI);
        longi = intent.getStringExtra(LONGI);

        LinearLayout bike = findViewById(R.id.bike);
        LinearLayout car = findViewById(R.id.car);
        LinearLayout van = findViewById(R.id.van);
        LinearLayout auto = findViewById(R.id.auto);

        bike.setOnClickListener(this);
        car.setOnClickListener(this);
        van.setOnClickListener(this);
        auto.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        Intent intent = new Intent(this, SelectRaduisActivity.class);
        intent.putExtra(LATI, lati);
        intent.putExtra(LONGI, longi);

        switch (id) {
            case R.id.van:
                intent.putExtra(VEHICAL_TYPE, 4);
                startActivity(intent);
                break;
            case R.id.car:
                intent.putExtra(VEHICAL_TYPE, 4);
                startActivity(intent);
                break;
            case R.id.auto:
                intent.putExtra(VEHICAL_TYPE, 3);
                startActivity(intent);
                break;
            case R.id.bike:
                intent.putExtra(VEHICAL_TYPE, 2);
                startActivity(intent);
                break;
        }
    }
}
