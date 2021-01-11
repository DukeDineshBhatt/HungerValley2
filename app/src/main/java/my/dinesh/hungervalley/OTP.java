package my.dinesh.hungervalley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class OTP extends AppCompatActivity {

    private Toolbar toolbar;
    int flags;
    ProgressBar progressBar;
    Button btncnt;
    int otp;
    OtpEditText editTextOtp;
    String password, username, editOtp, mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(Color.WHITE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        btncnt = (Button) findViewById(R.id.buttonContinue);
        editTextOtp = (OtpEditText) findViewById(R.id.et_otp);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle("");

        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference usersRef = database.getReference("Users");

        Intent mIntent = getIntent();
        otp = mIntent.getIntExtra("OTP", 0);

        username = mIntent.getStringExtra("username");
        password = mIntent.getStringExtra("password");
        mobile = mIntent.getStringExtra("mobile");


        btncnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                editOtp = editTextOtp.getText().toString();

                if (editOtp.isEmpty()) {

                    progressBar.setVisibility(View.GONE);
                    editTextOtp.setError("Enter valid OTP");
                    editTextOtp.requestFocus();

                } else {


                    if (otp == Integer.valueOf(editTextOtp.getText().toString())) {

                        SharedPreferences mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putString("user_id", mobile);
                        editor.putBoolean("is_logged_before", true); //this line will do trick
                        editor.commit();

                        String userid = String.valueOf(mobile);

                        Map userMap = new HashMap();
                        userMap.put("username", username);
                        userMap.put("mobile_number", mobile);
                        userMap.put("password", password);

                        usersRef.child(userid).setValue(userMap);



                        Intent intent = new Intent(OTP.this, MainActivity.class);
                        //intent.putExtra("mobile", mobile);
                        Toast.makeText(OTP.this, "Account created successfully. Login To Continue.", Toast.LENGTH_LONG).show();

                        startActivity(intent);
                        finish();

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "wrong OTP", Toast.LENGTH_LONG).show();
                    }


                }


            }
        });

    }
}
