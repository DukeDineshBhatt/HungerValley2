package my.dinesh.hungervalley;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgetPasswordActivity extends AppCompatActivity {


    EditText email,mobile;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        btn = (Button)findViewById(R.id.btn);
        email = (EditText)findViewById(R.id.email);
        mobile = (EditText)findViewById(R.id.mobile);


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference mRequestDatabase = database.getReference("User Requests");

        DatabaseReference usersRef = database.getReference("Users");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String txt_email = email.getText().toString().trim();
                String txt_mobile = mobile.getText().toString().trim();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (txt_email.matches(emailPattern))
                {

                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            if (dataSnapshot.hasChild(txt_mobile)) {




                            }else {

                                Toast.makeText(ForgetPasswordActivity.this,"This Number Is not Registered! Enter Your Registered number",Toast.LENGTH_SHORT).show();

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    email.setError("Enter valid mobile number");
                    email.requestFocus();

                }
                else
                {

                }

            }
        });
    }
}
