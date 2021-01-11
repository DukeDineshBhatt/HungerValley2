package my.dinesh.hungervalley;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends BaseActivity {

    Button btn_login;
    TextView textViewUsername, textViewPhone, txt_location, txt_locality, txt_landmark, price, no_order,mailText;
    int flags;
    LinearLayout address;
    Button add_address, edit;
    LinearLayout logout_layout, share,mail;
    DatabaseReference mCartDatabase;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        textViewPhone = (TextView) findViewById(R.id.phone);
        textViewUsername = (TextView) findViewById(R.id.username);
        txt_location = (TextView) findViewById(R.id.location);
        txt_locality = (TextView) findViewById(R.id.locality);
        txt_landmark = (TextView) findViewById(R.id.landmark);
        price = (TextView) findViewById(R.id.price);
        address = (LinearLayout) findViewById(R.id.address);
        add_address = (Button) findViewById(R.id.add_address);
        share = (LinearLayout) findViewById(R.id.share);
        mail = (LinearLayout) findViewById(R.id.mail);
        edit = (Button) findViewById(R.id.edit);
        btn_login = (Button) findViewById(R.id.btn_login);
        //delete_address = (Button) findViewById(R.id.delete);
        logout_layout = (LinearLayout) findViewById(R.id.logout_layout);
        txt = (TextView) findViewById(R.id.txt);
        mailText = (TextView) findViewById(R.id.mailText);

        flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(Color.WHITE);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        String userId = (shared.getString("user_id", ""));

        DatabaseReference mOrderDatabase = database.getReference("Orders List").child("User View").child(userId);

        mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(userId);


        DatabaseReference usersRef = database.getReference("Users").child(userId);


       /* Typeface Hindi = Typeface.createFromAsset(getAssets(), "font/mangal.ttf");
        txt.setTypeface(Hindi);
        txt.setText("दगड़िया");*/


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Try this Amazing App for Amazing Food." +
                        " https://play.google.com/store/apps/details?id=my.dinesh.hungervalley";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "your_email"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                startActivity(intent);

            }
        });

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String username = dataSnapshot.child("username").getValue().toString();
                String mobile = dataSnapshot.child("mobile_number").getValue().toString();
                textViewUsername.setText(username);
                textViewPhone.setText(mobile);

                if (dataSnapshot.child("Address").exists()) {

                    add_address.setVisibility(View.GONE);
                    address.setVisibility(View.VISIBLE);

                    txt_location.setText(dataSnapshot.child("Address").child("location").getValue().toString());
                    txt_locality.setText(dataSnapshot.child("Address").child("locality").getValue().toString());
                    txt_landmark.setText(dataSnapshot.child("Address").child("landmark").getValue().toString());

                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(AccountActivity.this, EditAddress.class);
                            startActivity(intent);

                        }
                    });


                } else {

                    add_address.setVisibility(View.VISIBLE);

                    address.setVisibility(View.GONE);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*mOrderDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.hasChildren()) {


                    order_layout.setVisibility(View.VISIBLE);
                    no_order.setVisibility(View.GONE);


                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        if (!dataSnapshot1.getKey().equals("Total Price") && !dataSnapshot1.getKey().equals("Status")) {

                        }

                        price.setText(" Rs " + dataSnapshot1.getValue().toString());

                    }


                } else {

                    order_layout.setVisibility(View.GONE);
                    no_order.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(AccountActivity.this)
                        .setMessage("are you sure want to logout?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                AccountActivity.super.onBackPressed();

                                SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putBoolean("hasLoggedIn", false);


                                SharedPreferences mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = mPrefs.edit();
                                editor1.remove("user_id");

                                editor.commit();
                                editor1.commit();

                                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        }).create().show();


            }
        });

        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AccountActivity.this, SetAddress.class);
                startActivity(intent);

            }
        });


    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(AccountActivity.this, MainActivity.class);
        startActivity(intent);
        finish();


    }

    @Override
    int getContentViewId() {
        return R.layout.activity_account;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.account;
    }
}