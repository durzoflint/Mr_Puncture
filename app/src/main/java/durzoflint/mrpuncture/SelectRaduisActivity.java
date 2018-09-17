package durzoflint.mrpuncture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static durzoflint.mrpuncture.SelectVehicalType.VEHICAL_TYPE;

public class SelectRaduisActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SEARCH_RADIUS = "search_radius";
    int vehicalType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_raduis);

        Intent intent = getIntent();
        vehicalType = intent.getIntExtra(VEHICAL_TYPE, 0);

        Button two = findViewById(R.id.two);
        Button three = findViewById(R.id.three);
        Button four = findViewById(R.id.four);
        Button five = findViewById(R.id.five);
        Button ten = findViewById(R.id.ten);

        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        ten.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        Intent intent = new Intent(this, SelectServiceActivity.class);

        switch (id) {
            case R.id.two:
                intent.putExtra(SEARCH_RADIUS, 2);
                intent.putExtra(VEHICAL_TYPE, vehicalType);
                startActivity(intent);
                break;
            case R.id.three:
                intent.putExtra(SEARCH_RADIUS, 2);
                intent.putExtra(VEHICAL_TYPE, vehicalType);
                startActivity(intent);
                break;
            case R.id.four:
                intent.putExtra(SEARCH_RADIUS, 2);
                intent.putExtra(VEHICAL_TYPE, vehicalType);
                startActivity(intent);
                break;
            case R.id.five:
                intent.putExtra(SEARCH_RADIUS, 2);
                intent.putExtra(VEHICAL_TYPE, vehicalType);
                startActivity(intent);
                break;
            case R.id.ten:
                intent.putExtra(SEARCH_RADIUS, 2);
                intent.putExtra(VEHICAL_TYPE, vehicalType);
                startActivity(intent);
                break;
        }
    }
}
