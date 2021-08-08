package net.simplifiedlearning.logbooksystem;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.content.pm.PackageManager;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;

import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LogBookMain extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_CODE = 200;


    //a list to store all the products
    private List<Product2> productList2;

    //the recyclerview
    private RecyclerView recyclerView;


    private Button btnDownload;
    String URL = "https://saraart.000webhostapp.com/fpdf183/tutorial/tuto1.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_book_main);

        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recylcerViewLog);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton addbutt2 = findViewById(R.id.addbuttonlog);


        //pdf


        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        //initializing the productlist
        productList2 = new ArrayList<>();

        final Button pdfbutt = findViewById(R.id.pdfbutton);
        pdfbutt.setOnClickListener(v -> {

            // Code here executes on main thread after user presses button

            //generatePDF();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(URL));
            String title = URLUtil.guessFileName(URL,null ,null);
            request.setTitle(title);
            request.setDescription("Downloading file please wait");
            String cookie = CookieManager.getInstance().getCookie(URL);
            request.addRequestHeader("cookie", cookie);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);


            DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
            downloadManager.enqueue(request);

            Toast.makeText(LogBookMain.this, "Dwl started.", Toast.LENGTH_SHORT).show();



        });

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
            finish();

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
        //this is the JSON Data URL
        //make sure you are using the correct ip else it will not work
        String URL_PRODUCTS = "http://saraart.000webhostapp.com/api2.php?registerndata=";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS + registerndata,
                response -> {
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
                },
                error -> {

                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }



    private void generatePDF() {




        //REQUEST
        String registerndata = "not set";
        Bundle extra = getIntent().getExtras();

        if (extra != null){
            registerndata = extra.getString("registerndata");
        }



        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.

        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(842,     595, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(15);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(this, R.color.purple_200));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.


        //adding our stringrequest to queue
        canvas.drawText(registerndata, 209, 100, title);
        canvas.drawText("Geeks for Geeks", 209, 80, title);


        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.purple_200));
        title.setTextSize(15);

        // below line is used for setting
        // our text to center of PDF.
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("This is sample document which we have created.", 396, 560, title);

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.

            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "BETA.pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
           // Toast.makeText(LogBookMain.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();

            Toast.makeText(LogBookMain.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }


}
