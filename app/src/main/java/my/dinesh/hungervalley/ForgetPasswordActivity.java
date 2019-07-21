package my.dinesh.hungervalley;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ForgetPasswordActivity extends AppCompatActivity {

    private Toolbar toolbar;
    int flags;
    EditText mobile;
    Button btn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(Color.WHITE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle("");


        btn = (Button) findViewById(R.id.btn);
        mobile = (EditText) findViewById(R.id.mobile);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference mRequestDatabase = database.getReference("User Requests");

        DatabaseReference usersRef = database.getReference("Users");


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                String txt = mobile.getText().toString().trim();


                if (txt.isEmpty() || txt.length() < 10) {

                    mobile.setError("Enter valid mobile number");
                    mobile.requestFocus();

                    return;


                } else {


                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            if (dataSnapshot.hasChild(txt)) {

                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(ForgetPasswordActivity.this, "Mobile Number found", Toast.LENGTH_SHORT).show();


                            } else {

                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(ForgetPasswordActivity.this, "This Number Is not Registered! Enter Your Registered number", Toast.LENGTH_LONG).show();

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
}
