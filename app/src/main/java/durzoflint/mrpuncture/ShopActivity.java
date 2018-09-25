package durzoflint.mrpuncture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static durzoflint.mrpuncture.SelectServiceActivity.SHOP;

public class ShopActivity extends AppCompatActivity {

    ArrayList<String> shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Intent intent = getIntent();
        shop = intent.getStringArrayListExtra(SHOP);

        TextView name = findViewById(R.id.name);
        TextView distance = findViewById(R.id.distance);
        ImageView badge = findViewById(R.id.badge);
        Button select = findViewById(R.id.select);

        name.setText(shop.get(1));
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
                //Todo: Notify the guy
            }
        });
    }
}
