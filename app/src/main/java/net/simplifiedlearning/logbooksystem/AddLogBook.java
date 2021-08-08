package net.simplifiedlearning.logbooksystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddLogBook extends AppCompatActivity {
    TextInputEditText textstarttime, textendtime;
    Button buttonAdd;

    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);


        textstarttime = findViewById(R.id.starttime);
        textendtime = findViewById(R.id.endtime);

        buttonAdd = findViewById(R.id.buttonADD);
        progressBar = findViewById(R.id.progress);


        String username = null;
        Bundle extra = getIntent().getExtras();

        if (extra != null){
            username = extra.getString("username");
        }
        String registern = null;
        Bundle extra2 = getIntent().getExtras();

        if (extra2 != null){
            registern = extra2.getString("registerndata");
        }
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String date2 = df.format(date);


        String finalUsername = username;
        String finalRegistern = registern;
        buttonAdd.setOnClickListener(v -> {
            String starttime , endtime;
            starttime = String.valueOf(textstarttime.getText());
            endtime = String.valueOf(textendtime.getText());


            if (!starttime.equals("") && !endtime.equals("")  ) {
                progressBar.setVisibility(View.VISIBLE);
                //Start ProgressBar first (Set visibility VISIBLE)
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[5];
                    field[0] = "username";
                    field[1] = "registern";
                    field[2] = "date";
                    field[3] = "starttime";
                    field[4] = "endtime";
                    //Creating array for data
                    String[] data = new String[5];
                    data[0] = finalUsername;
                    data[1] = finalRegistern;
                    data[2] = date2;
                    data[3] = starttime;
                    data[4] = endtime;
                    PutData putData = new PutData("https://saraart.000webhostapp.com/addnewlog.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            progressBar.setVisibility(View.GONE);
                            String result = putData.getResult();
                            if (result.equals("New Entry Added")){

                                Toast.makeText(getApplicationContext(), "New Entry for " + finalRegistern + " successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LogBookMain.class);
                                intent.putExtra("registerndata", finalRegistern);
                                intent.putExtra("username", finalUsername);
                                startActivity(intent);
                                finish();

                            }
                            else {
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    //End Write and Read data with URL
                });
            }
            else{
                Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            }
        });


    }
}