package my.dinesh.hungervalley;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetPasswordActivity extends AppCompatActivity {

  private Toolbar toolbar;
  int flags;
  EditText password, confrm_password;
  Button btn;
  ProgressBar progressBar;
  String uId, txt_password, txt_confrm_password;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reset_password);


    flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
    flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
    getWindow().getDecorView().setSystemUiVisibility(flags);
    getWindow().setStatusBarColor(Color.WHITE);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    password = (EditText) findViewById(R.id.password);
    confrm_password = (EditText) findViewById(R.id.confrm_password);
    btn = (Button) findViewById(R.id.btn);

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    Intent intent = getIntent();
    uId = intent.getStringExtra("userid");
    //temp_key = intent.getStringExtra("key");

    DatabaseReference mRequestDatabase = database.getReference("Users");
    DatabaseReference mDatabase = database.getReference("User Requests");

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    toolbar.setTitle("");

    progressBar = (ProgressBar) findViewById(R.id.progressbar);

    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        progressBar.setVisibility(View.VISIBLE);

        txt_password = password.getText().toString().trim();
        txt_confrm_password = confrm_password.getText().toString().trim();


        if (txt_password.isEmpty() || txt_password.length() < 6) {

          progressBar.setVisibility(View.GONE);
          password.setError("Enter your 6 digit password");
          password.requestFocus();
          return;



        } else if (!txt_confrm_password.equals(txt_password)) {
          progressBar.setVisibility(View.GONE);

          confrm_password.setError("Password is not matched");
          confrm_password.requestFocus();
          return;

        } else {

          mRequestDatabase.child(uId).child("password").setValue(txt_password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

              mDatabase.child(uId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                  progressBar.setVisibility(View.GONE);

                  Toast.makeText(ResetPasswordActivity.this, "Password Changed Successfully, Login to continue", Toast.LENGTH_LONG).show();
                  Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                  startActivity(intent);
                  finish();


                }
              });



            }
          });


        }


      }
    });


  }

  public void ShowHidePass(View view){

    if(view.getId()==R.id.show_pass_btn){

      if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){

        ((ImageView)(view)).setImageResource(R.drawable.hide);

        //Show Password
        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
      }
      else{
        ((ImageView)(view)).setImageResource(R.drawable.show);

        //Hide Password
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

      }
    }
  }

  public void ShowHidePass1(View view){

    if(view.getId()==R.id.show_con_pass_btn){

      if(confrm_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){

        ((ImageView)(view)).setImageResource(R.drawable.show);

        //Show Password
        confrm_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
      }
      else{
        ((ImageView)(view)).setImageResource(R.drawable.hide);

        //Hide Password
        confrm_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

      }
    }
  }
}
