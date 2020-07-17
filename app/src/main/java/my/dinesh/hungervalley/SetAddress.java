package my.dinesh.hungervalley;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SetAddress extends AppCompatActivity {

    int flags;
    private Toolbar toolbar;
    Spinner spinner;
    EditText locality, landmark, mobile;
    DatabaseReference mUserDatabase;
    DatabaseReference mAdminDatabase;
    String uId;
    String selectedItemText;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_address);

        flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(Color.WHITE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        // Get reference of widgets from XML layout
        spinner = (Spinner) findViewById(R.id.spinner);
        locality = (EditText) findViewById(R.id.locality);
        landmark = (EditText) findViewById(R.id.landmark);
        mobile = (EditText) findViewById(R.id.mobile);
        save = (Button) findViewById(R.id.save);

        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        uId = (shared.getString("user_id", ""));

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uId);
        mAdminDatabase = FirebaseDatabase.getInstance().getReference().child("Admin").child("Locations");


        mAdminDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("areaName").getValue(String.class);
                    areas.add(areaName);
                }
                // ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(SetAddress.this, android.R.layout.simple_spinner_item, areas);
                //areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //spinner.setAdapter(areasAdapter);


                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(SetAddress.this, R.layout.spinner_item, areas) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            // Disable the first item from Spinner
                            // First item will be use for hint
                            return false;
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if (position == 0) {
                            // Set the hint text color gray
                            tv.setTextColor(Color.GRAY);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }


                };

                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinner.setAdapter(spinnerArrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Initializing a String Array
        String[] plants = new String[]{
                "Choose Location",
                "Ancholi (near bridge)",
                "Aps Road",
                "Bharkatiya/kusoli",
                "Bhatkot",
                "Bhadelwada",
                "Bin",
                "Chandak",
                "Cinema Line",
                "Collage Road",
                "Ghanta Ghar",
                "Jakhni",
                "Kasni",
                "Kumour",
                "Link Road",
                "Linthura",
                "Panda(Check Post)",
                "Police Line",
                "Rai",
                "Siltham",
                "Takana",
                "Tildhukri"

        };

        /*final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, plantsList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
*/

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    //Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String txt_locality = locality.getText().toString();
                String txt_landmark = landmark.getText().toString();
                String txt_mobile = mobile.getText().toString();

                if (selectedItemText.equals("Choose Location")) {

                    Toast.makeText(SetAddress.this, "Choose Your Location !", Toast.LENGTH_SHORT).show();


                } else if (txt_locality.isEmpty()) {

                    locality.setError("Enter Locality");
                    locality.requestFocus();
                    return;

                } else if (txt_landmark.isEmpty()) {

                    landmark.setError("Enter Landmark");
                    landmark.requestFocus();
                    return;

                } else if (txt_mobile.isEmpty() || txt_mobile.length() < 10) {

                    mobile.setError("Enter a valid mobile number");
                    mobile.requestFocus();

                } else {

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("location", selectedItemText);
                    map.put("locality", txt_locality);
                    map.put("landmark", txt_landmark);
                    map.put("Mobile", txt_mobile);

                    mUserDatabase.child("Address").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(SetAddress.this, "Address Saved Successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SetAddress.this, CartActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(SetAddress.this, "Something Went Wrong, Try Again.", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


                }

            }
        });


    }

}

