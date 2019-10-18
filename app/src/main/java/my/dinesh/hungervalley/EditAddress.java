package my.dinesh.hungervalley;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class EditAddress extends AppCompatActivity {

    int flags;
    private Toolbar toolbar;
    Spinner spinner;
    EditText locality, landmark, mobile;
    DatabaseReference mUserDatabase;
    String uId;
    String selectedItemText;
    Button save;
    ProgressBar progressbar;

    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);


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
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        uId = (shared.getString("user_id", ""));

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uId);

        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressbar.setVisibility(View.VISIBLE);

                locality.setText(dataSnapshot.child("Address").child("locality").getValue().toString());
                landmark.setText(dataSnapshot.child("Address").child("landmark").getValue().toString());
                mobile.setText(dataSnapshot.child("Address").child("Mobile").getValue().toString());
                location = dataSnapshot.child("Address").child("location").getValue().toString();

                progressbar.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressbar.setVisibility(View.GONE);

            }
        });

        // Initializing a String Array
        String[] plants = new String[]{
                "Choose Location",
                "Ancholi (near bridge)",
                "Aps Road",
                "Bharkatiya",
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
                "kusoli",
                "Link Road",
                "Linthura",
                "Naya Bazar",
                "Panda(Check Post)",
                "Police Line",
                "Rai",
                "Siltham",
                "Simalgair",
                "Takana",
                "Tildhukri",
                "Wadda(Rs 30 extra charge)"

        };

        List<String> plantsList = new ArrayList<>(Arrays.asList(plants));

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
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

                    Toast.makeText(EditAddress.this, "Choose Your Location !", Toast.LENGTH_SHORT).show();


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

                                Toast.makeText(EditAddress.this, "Address Saved Successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(EditAddress.this, CartActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(EditAddress.this, "Something Went Wrong, Try Again.", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


                }

            }
        });


    }

}
