package appewtc.masterung.ungrestaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private UserTABLE objUserTABLE;
    private FoodTABLE objFoodTABLE;
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Widget
        bindWidget();

        //Create & Connected SQLite
        createAndConnected();

        //Test Add New Value
        //testAddNewValue();

        //Delete All data
        deleteAllData();

        //Synchronize JSON to SQLite
        synJSONtoSQLite();

    }   // onCreate

    public void clickLogin(View view) {

        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        if (userString.equals("") || passwordString.equals("") ) {

            //Have Space
            myAlertDialog("มีช่องว่าง", "กรุณากรอกทุกช่อง นะคะ");

        } else {

            //No Space
            checkUser();

        }

    }   // clickLogin

    private void checkUser() {

        try {

            String[] strMyResult = objUserTABLE.searchUser(userString);

            if (passwordString.equals(strMyResult[2])) {

                welcome(strMyResult[3]);

            } else {
                myAlertDialog("Password False", "Please Try agains Password False");
            }

        } catch (Exception e) {
            myAlertDialog("No User", "No " + userString+ " in my Database");
        }

    } // checkUser

    private void welcome(final String strName) {

        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.restaurant);
        objBuilder.setTitle("Welcome");
        objBuilder.setMessage("Welcome " + strName);
        objBuilder.setCancelable(false);
        objBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Intent to Order Activity
                Intent objIntent = new Intent(MainActivity.this, OrderActivity.class);
                objIntent.putExtra("Name", strName);
                startActivity(objIntent);

                dialogInterface.dismiss();
                finish();

            }
        });
        objBuilder.show();

    }   // welcome

    private void myAlertDialog(String strTitle, String strMessage) {

        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.danger);
        objBuilder.setTitle(strTitle);
        objBuilder.setMessage(strMessage);
        objBuilder.setCancelable(false);
        objBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        objBuilder.show();

    }   // myAlertDialog


    private void bindWidget() {
        userEditText = (EditText) findViewById(R.id.editText);
        passwordEditText = (EditText) findViewById(R.id.editText2);
    }

    private void synJSONtoSQLite() {

        //0. Change Policy
        StrictMode.ThreadPolicy myPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(myPolicy);

        int intTimes = 0;
        while (intTimes <= 1) {

            //Constant
            InputStream objInputStream = null;
            String strJSON = null;
            String strUrlUser = "http://swiftcodingthai.com/24Sep/php_get_data_master.php";
            String strUrlFood = "http://swiftcodingthai.com/24Sep/php_get_data_food_master.php";
            HttpPost objHttpPost;

            //1. Create InputStream
            try {

                HttpClient objHttpClient = new DefaultHttpClient();

                if (intTimes != 1) {
                    objHttpPost = new HttpPost(strUrlUser);
                } else {
                    objHttpPost = new HttpPost(strUrlFood);
                }

                HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
                HttpEntity objHttpEntity = objHttpResponse.getEntity();
                objInputStream = objHttpEntity.getContent();

            } catch (Exception e) {
                Log.d("Rest", "InputStream ==> " + e.toString());
            }

            //2. Create strJSON
            try {

                BufferedReader objBufferedReader = new BufferedReader(new InputStreamReader(objInputStream, "UTF-8"));
                StringBuilder objStringBuilder = new StringBuilder();
                String strLine = null;

                while ((strLine = objBufferedReader.readLine()) != null ) {
                    objStringBuilder.append(strLine);
                }   // while
                objInputStream.close();
                strJSON = objStringBuilder.toString();

            } catch (Exception e) {
                Log.d("Rest", "strJSON ==> " + e.toString());
            }

            //3. Update SQLite
            try {

                final JSONArray objJsonArray = new JSONArray(strJSON);

                for (int i = 0; i < objJsonArray.length(); i++) {

                    JSONObject object = objJsonArray.getJSONObject(i);

                    if (intTimes < 1) {

                        //For userTABLE
                        String strUser = object.getString("User");
                        String strPassword = object.getString("Password");
                        String strName = object.getString("Name");
                        objUserTABLE.addNewUser(strUser, strPassword, strName);

                    } else {

                        //For foodTABLE
                        String strFood = object.getString("Food");
                        String strSource = object.getString("Source");
                        String strPrice = object.getString("Price");
                        objFoodTABLE.addNewFood(strFood, strSource, strPrice);

                    }

                }   // for

            } catch (Exception e) {
                Log.d("Rest", "Update ==> " + e.toString());
            }




            intTimes += 1;
        }   //while

    }   // synJSONtoSQLite

    private void deleteAllData() {
        SQLiteDatabase objSqLiteDatabase = openOrCreateDatabase("Restaurant.db", MODE_PRIVATE, null);
        objSqLiteDatabase.delete("userTABLE", null, null);
        objSqLiteDatabase.delete("foodTABLE", null, null);
    }

    private void testAddNewValue() {
        objUserTABLE.addNewUser("testUser", "testPass", "โดรามอน");
        objFoodTABLE.addNewFood("testFood", "testSource", "1234");
    }

    private void createAndConnected() {
        objUserTABLE = new UserTABLE(this);
        objFoodTABLE = new FoodTABLE(this);
    }

}   // Main Class
