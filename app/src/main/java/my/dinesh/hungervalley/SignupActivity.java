package my.dinesh.hungervalley;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
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

public class SignupActivity extends AppCompatActivity {

  private Toolbar toolbar;
  int flags;

  EditText editTextMobile;
  EditText  editTextUsername, editTextPassword;

  String mobile, mobile_confrm, password, username,confrm_password;
  Button btn_continue;
  ProgressBar progressbar;
  int randomNumber;


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
    //editTextMobileConfrim = findViewById(R.id.mobile_confirm);
    editTextUsername = findViewById(R.id.username);
    editTextPassword = findViewById(R.id.password);
    //editTextConfrmPassword = findViewById(R.id.confrm_password);
    btn_continue = findViewById(R.id.buttonContinue);
    progressbar = findViewById(R.id.progressbar);

    FirebaseApp.initializeApp(this);
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference usersRef = database.getReference("Users");

    StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);

    btn_continue.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        progressbar.setVisibility(View.VISIBLE);

        mobile = editTextMobile.getText().toString().trim();
        //mobile_confrm = editTextMobileConfrim.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        username = editTextUsername.getText().toString().trim();
        //confrm_password = editTextConfrmPassword.getText().toString().trim();

        if (username.isEmpty()) {
          progressbar.setVisibility(View.GONE);

          editTextUsername.setError("Enter username");
          editTextUsername.requestFocus();
          return;

        } else if (mobile.isEmpty() || mobile.length() < 10) {
          progressbar.setVisibility(View.GONE);

          editTextMobile.setError("Enter a valid mobile number");
          editTextMobile.requestFocus();
          return;

        } else if (password.isEmpty() || password.length() < 6) {
          progressbar.setVisibility(View.GONE);

          editTextPassword.setError("Enter 6 digit password");
          editTextPassword.requestFocus();

        } else {

          usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

              if (snapshot.hasChild(mobile)) {

                progressbar.setVisibility(View.GONE);

                Toast.makeText(SignupActivity.this, "Account already exist with this mobile number!", Toast.LENGTH_LONG).show();

                editTextMobile.setError("Account Already exist!");
                editTextMobile.requestFocus();

              } else {


                try {
                  // Construct data
                  //int randomNumber;
                  String apiKey = "apikey=" + "C5ksL1aQZqQ-aoi8riQqvQtwzjQkjfC99pZuoPp9Zf";
                  Random random= new Random();
                  randomNumber= random.nextInt(999999);
                  String message = "&message=" + "Your Verification Code for Hunger Valley Account is "+ randomNumber;
                  String sender = "&sender=" + "TXTLCL";
                  String numbers = "&numbers=" +editTextMobile.getText().toString();

                  Log.d("NUMBER",editTextMobile.getText().toString());

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
                  Toast.makeText(getApplicationContext(),"OTP SEND SUCCESSFULLY",Toast.LENGTH_LONG).show();
                  Log.d("OTP : " ," " +randomNumber);

                  Intent intent = new Intent(SignupActivity.this, OTP.class);
                  intent.putExtra("OTP", randomNumber);
                  intent.putExtra("username", username);
                  intent.putExtra("password", password);
                  intent.putExtra("mobile", mobile);
                  startActivity(intent);

                  //return stringBuffer.toString();
                } catch (Exception e) {
                  progressbar.setVisibility(View.GONE);
                  //System.out.println("Error SMS "+e);
                  // return "Error "+e;
                  Toast.makeText(getApplicationContext(), "ERROR SENDING OTP TO THIS NUMBER!", Toast.LENGTH_LONG).show();
                  //Toast.makeText(getApplicationContext(), "ERROR" +e, Toast.LENGTH_LONG).show();

                }
/*
                SharedPreferences mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putBoolean("is_logged_before", true); //this line will do trick
                editor.commit();


                String userid = editTextMobile.getText().toString().trim();

                Map userMap = new HashMap();
                userMap.put("username", username);
                userMap.put("mobile_number", mobile_confrm);
                userMap.put("password", password);

                usersRef.child(userid).setValue(userMap);



                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                //intent.putExtra("mobile", mobile);
                Toast.makeText(SignupActivity.this, "Account created successfully. Login To Continue.", Toast.LENGTH_LONG).show();

                startActivity(intent);
                finish();
*/



              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

              progressbar.setVisibility(View.GONE);
            }
          });
        }


      }
    });
  }

  public void ShowHidePass(View view){

    if(view.getId()==R.id.show_pass_btn){

      if(editTextPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){

        (( ImageView)(view)).setImageResource(R.drawable.show);

        //Show Password
        editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
      }
      else{
        ((ImageView)(view)).setImageResource(R.drawable.hide);

        //Hide Password
        editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

      }
    }
  }

}