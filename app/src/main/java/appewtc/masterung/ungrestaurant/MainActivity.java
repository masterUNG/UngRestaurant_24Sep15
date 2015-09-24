package appewtc.masterung.ungrestaurant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private UserTABLE objUserTABLE;
    private FoodTABLE objFoodTABLE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create & Connected SQLite
        createAndConnected();

        //Test Add New Value
        testAddNewValue();


    }   // onCreate

    private void testAddNewValue() {
        objUserTABLE.addNewUser("testUser", "testPass", "โดรามอน");
        objFoodTABLE.addNewFood("testFood", "testSource", "1234");
    }

    private void createAndConnected() {
        objUserTABLE = new UserTABLE(this);
        objFoodTABLE = new FoodTABLE(this);
    }

}   // Main Class
