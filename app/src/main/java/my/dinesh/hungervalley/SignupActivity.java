package my.dinesh.hungervalley;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.util.Strings;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SignupActivity extends AppCompatActivity {

    private Toolbar toolbar;
    int flags;
    String base;
    EditText editTextMobile;
    EditText editTextUsername, editTextPassword;

    String mobile, mobile_confrm, password, username, confrm_password;
    Button btn_continue;
    ProgressBar progressbar;
    int randomNumber;
    DatabaseReference mOtpdatabase;
    String sender_id,message,authorization;

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

        mOtpdatabase  = database.getReference("Admin").child("OTP");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mOtpdatabase.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            sender_id = dataSnapshot.child("sender_id").getValue().toString();
            message = dataSnapshot.child("message").getValue().toString();
            authorization = dataSnapshot.child("authorization").getValue().toString();
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
        });

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


                                Random random = new Random();
                                randomNumber = random.nextInt(99999);

                                Application b = (Application) getApplicationContext();

                                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                                logging.level(HttpLoggingInterceptor.Level.HEADERS);
                                logging.level(HttpLoggingInterceptor.Level.BODY);

                                OkHttpClient client = new OkHttpClient.Builder().writeTimeout(1000, TimeUnit.SECONDS).readTimeout(1000, TimeUnit.SECONDS).connectTimeout(1000, TimeUnit.SECONDS).addInterceptor(logging).build();

                                base = b.baseurl;

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(b.baseurl)
                                        .client(client)
                                        .addConverterFactory(ScalarsConverterFactory.create())
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                                Call<OtpBean> call = cr.getOtp(sender_id, "english", "qt", editTextMobile.getText().toString(),
                                        message, "{#AA#}", String.valueOf(randomNumber), authorization);
                                call.enqueue(new Callback<OtpBean>() {
                                    @Override
                                    public void onResponse(@NotNull Call<OtpBean> call, @NotNull Response<OtpBean> response) {

                                        if (response.body().getMessage().get(0).equals("Message sent successfully")) {


                                            Intent intent = new Intent(SignupActivity.this, OTP.class);
                                            intent.putExtra("OTP", randomNumber);
                                            intent.putExtra("username", username);
                                            intent.putExtra("password", password);
                                            intent.putExtra("mobile", mobile);
                                            startActivity(intent);

                                        } else {
                                            Toast.makeText(SignupActivity.this, "Please try again", Toast.LENGTH_SHORT).show();

                                        }

                                        progressbar.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onFailure(Call<OtpBean> call, Throwable t) {
                                        progressbar.setVisibility(View.GONE);
                                        Toast.makeText(SignupActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();

                                    }
                                });



                /*try {

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
                  //intent.putExtra("OTP", randomNumber);
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

    public void ShowHidePass(View view) {

        if (view.getId() == R.id.show_pass_btn) {

            if (editTextPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {

                ((ImageView) (view)).setImageResource(R.drawable.show);

                //Show Password
                editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.hide);

                //Hide Password
                editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }

}