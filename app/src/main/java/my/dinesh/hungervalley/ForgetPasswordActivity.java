package my.dinesh.hungervalley;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ForgetPasswordActivity extends AppCompatActivity {

    private Toolbar toolbar;
    int flags;
    EditText mobile;
    OtpEditText editTextOtp;
    Button btn, change;
    ProgressBar progressBar;
    LinearLayout layout_two;
    String temp_key, txt;
    int randomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(Color.WHITE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        layout_two = (LinearLayout) findViewById(R.id.layout_two);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle("");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btn = (Button) findViewById(R.id.btn);
        change = (Button) findViewById(R.id.change);
        mobile = (EditText) findViewById(R.id.mobile);
        editTextOtp = (OtpEditText) findViewById(R.id.key);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference usersRef = database.getReference("Users");


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                txt = mobile.getText().toString().trim();


                if (txt.isEmpty() || txt.length() < 10) {

                    progressBar.setVisibility(View.GONE);

                    mobile.setError("Enter valid mobile number");
                    mobile.requestFocus();

                    return;

                } else {

                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChild(txt)) {

                /*mRequestDatabase.child(txt).child("key").setValue(getSaltString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {

                    mRequestDatabase.child(txt).child("key").addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        layout_two.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);


                        change.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {

                            mRequestDatabase.child(txt).addListenerForSingleValueEvent(new ValueEventListener() {
                              @Override
                              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                temp_key = key.getText().toString().trim();
                                String s = dataSnapshot.child("key").getValue().toString().trim();

                                if (temp_key.isEmpty()) {

                                  Toast.makeText(ForgetPasswordActivity.this, "Enter key", Toast.LENGTH_SHORT).show();


                                } else if (s.equals(temp_key)) {

                                  Intent intent = new Intent(ForgetPasswordActivity.this, ResetPasswordActivity.class);
                                  intent.putExtra("userid", txt);
                                  startActivity(intent);
                                } else {

                                  Toast.makeText(ForgetPasswordActivity.this, "Incorrect key", Toast.LENGTH_SHORT).show();


                                }


                              }

                              @Override
                              public void onCancelled(@NonNull DatabaseError databaseError) {

                              }
                            });


                          }
                        });


                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {

                      }
                    });

                  }
                });

*/
                                try {
                                    // Construct data
                                    //int randomNumber;
                                    String apiKey = "apikey=" + "C5ksL1aQZqQ-aoi8riQqvQtwzjQkjfC99pZuoPp9Zf";
                                    Random random = new Random();
                                    randomNumber = random.nextInt(999999);
                                    String message = "&message=" + "Your Hunger Valley Account Rest Key is " + randomNumber;
                                    String sender = "&sender=" + "TXTLCL";
                                    String numbers = "&numbers=" + mobile.getText().toString();


                                    // Send data
                                    HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
                                    String data = apiKey + numbers + message + sender;
                                    conn.setDoOutput(true);
                                    conn.setRequestMethod("POST");
                                    conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                                    conn.getOutputStream().write(data.getBytes("UTF-8"));
                                    final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                    final StringBuffer stringBuffer = new StringBuffer();
                                    String line;
                                    while ((line = rd.readLine()) != null) {
                                        stringBuffer.append(line);
                                    }
                                    rd.close();
                                    Toast.makeText(getApplicationContext(), "OTP SEND SUCCESSFULLY", Toast.LENGTH_LONG).show();
                                    Log.d("OTP : ", " " + randomNumber);


                                    layout_two.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);

                                    change.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            if (randomNumber == Integer.valueOf(editTextOtp.getText().toString())) {

                                                Intent intent = new Intent(ForgetPasswordActivity.this, ResetPasswordActivity.class);
                                                intent.putExtra("userid", txt);
                                                startActivity(intent);

                                            } else {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "wrong Reset Key", Toast.LENGTH_LONG).show();
                                            }


                                        }
                                    });

                                } catch (Exception e) {
                                    progressBar.setVisibility(View.GONE);

                                    Toast.makeText(getApplicationContext(), "ERROR SENDING OTP TO THIS NUMBER!", Toast.LENGTH_LONG).show();


                                }


                            } else {

                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(ForgetPasswordActivity.this, "Please enter your Registered number", Toast.LENGTH_LONG).show();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            progressBar.setVisibility(View.GONE);
                        }
                    });


                }

            }
        });


    }

    protected String getSaltString() {
        String SALTCHARS = "ABcDeFGHIJKLmNOpQRSTUVWxYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 5) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
