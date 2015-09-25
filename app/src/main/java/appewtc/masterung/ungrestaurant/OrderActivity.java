package appewtc.masterung.ungrestaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    private TextView officerTextView;
    private Spinner deskSpinner;
    private ListView foodListView;
    private String officerString, deskString, foodString, itemString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        bindWidget();

        showOfficer();

        createSpinner();

        createListView();

    }   // onCreate

    private void createListView() {

        FoodTABLE objFoodTABLE = new FoodTABLE(this);

        final String[] foodStrings = objFoodTABLE.readAllFood();
        String[] sourceStrings = objFoodTABLE.readAllSource();
        String[] priceStrings = objFoodTABLE.readAllPrice();

        MyAdapter objMyAdapter = new MyAdapter(OrderActivity.this, sourceStrings, foodStrings, priceStrings);
        foodListView.setAdapter(objMyAdapter);

        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                foodString = foodStrings[i];
                chooseItem(foodStrings[i]);
            }
        });

    }   // createListview

    private void chooseItem(String foodString) {

        final CharSequence[] choiceCharSequences = {"1 set", "2 set", "3 set", "4 set", "5 set"};
        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setTitle(foodString);
        objBuilder.setSingleChoiceItems(choiceCharSequences, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int intItem = i + 1;
                itemString = Integer.toString(intItem);
                confirmOrder();
                dialogInterface.dismiss();
            }
        });
        objBuilder.show();
    }

    private void confirmOrder() {
        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.restaurant);
        objBuilder.setTitle("Confirm Order");
        objBuilder.setMessage("Officer = " + officerString + "\n" +
                "Desk = " + deskString + "\n" +
                "Food = " + foodString + "\n" +
                "Item = " + itemString);
        objBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        objBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateOrder();
                dialogInterface.dismiss();
            }
        });
        objBuilder.show();

    }

    private void updateOrder() {

        try {
            ArrayList<NameValuePair> objNameValuePairs = new ArrayList<NameValuePair>();
            objNameValuePairs.add(new BasicNameValuePair("isAdd", "true"));
            objNameValuePairs.add(new BasicNameValuePair("Officer", officerString));
            objNameValuePairs.add(new BasicNameValuePair("Desk", deskString));
            objNameValuePairs.add(new BasicNameValuePair("Food", foodString));
            objNameValuePairs.add(new BasicNameValuePair("Item", itemString));
            HttpClient objHttpClient = new DefaultHttpClient();
            HttpPost objHttpPost = new HttpPost("http://swiftcodingthai.com/24Sep/php_add_data_restaurant.php");
            objHttpPost.setEntity(new UrlEncodedFormEntity(objNameValuePairs, "UTF-8"));
            objHttpClient.execute(objHttpPost);
            Toast.makeText(OrderActivity.this, "Update Order Finish", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(OrderActivity.this, "Cannot Update Order", Toast.LENGTH_SHORT).show();
        }

    }   // updateOrder

    private void createSpinner() {

        final String[] myDesk = {"1A", "2A", "3A", "4A"};
        ArrayAdapter<String> deskAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myDesk);
        deskSpinner.setAdapter(deskAdapter);

        deskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                deskString = myDesk[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                deskString = myDesk[0];
            }
        });

    }   // createSpinner


    private void showOfficer() {
        officerString = getIntent().getStringExtra("Name");
        officerTextView.setText(officerString);
    }

    private void bindWidget() {
        officerTextView = (TextView) findViewById(R.id.textView);
        deskSpinner = (Spinner) findViewById(R.id.spinner);
        foodListView = (ListView) findViewById(R.id.listView);
    }

}   // Main Class
