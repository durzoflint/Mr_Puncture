package durzoflint.mrpuncture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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

        Button four = findViewById(R.id.four);
        Button three = findViewById(R.id.three);
        Button two = findViewById(R.id.two);

        four.setOnClickListener(this);
        three.setOnClickListener(this);
        two.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        Intent intent = new Intent(this, SelectRaduisActivity.class);
        intent.putExtra(LATI, lati);
        intent.putExtra(LONGI, longi);

        switch (id) {
            case R.id.four:
                intent.putExtra(VEHICAL_TYPE, 4);
                startActivity(intent);
                break;
            case R.id.three:
                intent.putExtra(VEHICAL_TYPE, 3);
                startActivity(intent);
                break;
            case R.id.two:
                intent.putExtra(VEHICAL_TYPE, 2);
                startActivity(intent);
                break;
        }
    }
}
