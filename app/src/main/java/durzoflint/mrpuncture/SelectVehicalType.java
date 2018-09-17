package durzoflint.mrpuncture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SelectVehicalType extends AppCompatActivity implements View.OnClickListener {

    public static final String VEHICAL_TYPE = "vehical_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
