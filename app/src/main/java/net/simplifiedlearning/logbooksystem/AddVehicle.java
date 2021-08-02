package net.simplifiedlearning.logbooksystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddVehicle extends AppCompatActivity {
    TextInputEditText textInputEditTextImageLink, textInputEditTextBrand, textInputEditTextModel, textInputEditTextRegistern,textInputEditTextDepartment;
    Button buttonAdd;
    TextView textViewLogin;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);


        textInputEditTextImageLink = findViewById(R.id.imagelink);
        textInputEditTextBrand = findViewById(R.id.brand);
        textInputEditTextModel = findViewById(R.id.model);
        textInputEditTextRegistern = findViewById(R.id.registern);
        textInputEditTextDepartment= findViewById(R.id.department);
        buttonAdd = findViewById(R.id.buttonADD);
        progressBar = findViewById(R.id.progress);

       // textViewLogin = findViewById(R.id.loginText);
        //textViewLogin.setOnClickListener(v -> {
           // Intent intent = new Intent(getApplicationContext(),Login.class);
          //  startActivity(intent);
          //  finish();
       // });

        buttonAdd.setOnClickListener(v -> {

            String imagelink , brand , model , registern, department;
            imagelink = String.valueOf(textInputEditTextImageLink.getText());
            brand = String.valueOf(textInputEditTextBrand.getText());
            model= String.valueOf(textInputEditTextModel.getText());
            registern = String.valueOf(textInputEditTextRegistern.getText());
            department = String.valueOf(textInputEditTextDepartment.getText());

            if (!brand.equals("") && !model.equals("")  && !registern.equals("") && !department.equals("") ) {
                progressBar.setVisibility(View.VISIBLE);
                //Start ProgressBar first (Set visibility VISIBLE)
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[5];
                    field[0] = "imagelink";
                    field[1] = "brand";
                    field[2] = "model";
                    field[3] = "registern";
                    field[4] = "department";
                    //Creating array for data
                    String[] data = new String[5];
                    data[0] = imagelink;
                    data[1] = brand;
                    data[2] = model;
                    data[3] = registern;
                    data[4] = department;
                    PutData putData = new PutData("https://saraart.000webhostapp.com/addnewv.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            progressBar.setVisibility(View.GONE);
                            String result = putData.getResult();
                            if (result.equals("New Vehicle Successfully Added")){
                                Toast.makeText(getApplicationContext(), "New Vehicle Successfully Added", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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