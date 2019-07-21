package my.dinesh.hungervalley;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private Toolbar toolbar;
    int flags;

    EditText editTextMobile;
    EditText editTextMobileConfrim, editTextUsername, editTextPassword;

    String mobile, mobile_confrm, password, username;
    Button btn_continue;
    ProgressBar progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(Color.WHITE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle("");

        editTextMobile = findViewById(R.id.mobile);
        editTextMobileConfrim = findViewById(R.id.mobile_confirm);
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        btn_continue = findViewById(R.id.buttonContinue);
        progressbar = findViewById(R.id.progressbar);


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference usersRef = database.getReference("Users");


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mobile = editTextMobile.getText().toString().trim();
                mobile_confrm = editTextMobileConfrim.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();
                username = editTextUsername.getText().toString().trim();

                if (username.isEmpty()) {

                    editTextUsername.setError("Enter username");
                    editTextUsername.requestFocus();
                    return;

                } else if (mobile.isEmpty() || mobile.length() < 10) {

                    editTextMobile.setError("Enter a valid mobile number");
                    editTextMobile.requestFocus();
                    return;
                } else if (!(mobile.equals(mobile_confrm))) {

                    editTextMobileConfrim.setError("Enter Correct mobile number");
                    editTextMobileConfrim.requestFocus();
                    return;
                } else if (password.isEmpty() || password.length() < 6) {

                    editTextPassword.setError("Enter your 6 digit password");
                    editTextPassword.requestFocus();

                } else {


                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(mobile_confrm)) {


                                Toast.makeText(SignupActivity.this, "Account already exist with this mobile number!", Toast.LENGTH_LONG).show();

                                editTextMobile.setError("Account Already exist!");
                                editTextMobile.requestFocus();

                            } else {

                                progressbar.setVisibility(View.VISIBLE);

                                SharedPreferences mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = mPrefs.edit();
                                editor.putBoolean("is_logged_before", true); //this line will do trick
                                editor.commit();


                                String userid = editTextMobileConfrim.getText().toString().trim();

                                Map userMap = new HashMap();
                                userMap.put("username", username);
                                userMap.put("mobile_number", mobile_confrm);
                                userMap.put("password", password);

                                usersRef.child(userid).setValue(userMap);

                                progressbar.setVisibility(View.GONE);

                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                //intent.putExtra("mobile", mobile);
                                Toast.makeText(SignupActivity.this, "Account created successfully. Login To Continue.", Toast.LENGTH_LONG).show();

                                startActivity(intent);
                                finish();


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


            }
        });
    }

}