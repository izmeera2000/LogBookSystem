package net.simplifiedlearning.logbooksystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LogBookMain extends AppCompatActivity {




    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work
    private  String URL_PRODUCTS = "http://saraart.000webhostapp.com/api2.php?registerndata="  ;


    //a list to store all the products
    private List<Product2> productList2;

    //the recyclerview
    private RecyclerView recyclerView;
    private ProductsAdapter2.RecyclerViewClickListener listener2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_book_main);

        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recylcerViewLog);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton addbutt2 = findViewById(R.id.addbuttonlog);

        //initializing the productlist
        productList2 = new ArrayList<>();



        //this method will fetch and parse json
        //to display it in recyclerview
        loadProducts();
        addbutt2.setOnClickListener(v -> {
            String username = null;
            Bundle extra = getIntent().getExtras();

            if (extra != null){
                username = extra.getString("username");
            }
            String registerndata = null;
            Bundle extra2 = getIntent().getExtras();

            if (extra2 != null){
                registerndata = extra2.getString("registerndata");
            }
            Intent intent = new Intent(getApplicationContext(),AddLogBook.class);
            intent.putExtra("username", username);
            intent.putExtra("registerndata", registerndata);


            startActivity(intent);

        });
    }

    private void loadProducts() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        String registerndata = "not set";
        Bundle extra = getIntent().getExtras();

        if (extra != null){
            registerndata = extra.getString("registerndata");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS + registerndata,
                new Response.Listener<String>() {
                    @Override



                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product2 = array.getJSONObject(i);

                                //adding the product to product list
                                productList2.add(new Product2(
                                        product2.getInt("id"),
                                        product2.getString("username"),
                                        product2.getString("registern"),
                                        product2.getString("date"),
                                        product2.getString("starttime"),
                                        product2.getString("endtime")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                           // setOnClickListener();
                            ProductsAdapter2 adapter = new ProductsAdapter2(LogBookMain.this, productList2);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

   // private void setOnClickListener() {
   //     listener2 = new ProductsAdapter2.RecyclerViewClickListener() {
    //        @Override
     //     public void onClick(View v, int position) {
          //   Intent intent = new Intent( getApplicationContext(), LogBookMain.class);
           //  intent.putExtra("registerndata", productList2.get(position).getRating());
            //         startActivity(intent);
    //        }
    //    };
   // }


}