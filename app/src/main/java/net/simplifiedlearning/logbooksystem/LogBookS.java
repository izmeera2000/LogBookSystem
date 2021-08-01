package net.simplifiedlearning.logbooksystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class LogBookS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_book_s);
        TextView nameTxt = findViewById(R.id.nameTextView);

        String registerndata = "not set";

        Bundle extras = getIntent().getExtras();
        if(extras != null )
        {
            registerndata = extras.getString("registerndata");
        }

    }
}