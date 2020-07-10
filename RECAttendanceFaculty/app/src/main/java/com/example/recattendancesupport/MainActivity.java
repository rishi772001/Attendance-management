package com.example.recattendancesupport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.*;
import android.os.Bundle;
import android.widget.*;
import com.google.firebase.database.*;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText emailId, password;
    Button btnlogin;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference users = database.getReference("faculty");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        emailId = findViewById(R.id.editText2);
        password = findViewById(R.id.editText3);
        btnlogin = findViewById(R.id.button);


        btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String email = emailId.getText().toString();
                    final String pwd = password.getText().toString();
                    if (email.isEmpty()) {
                        emailId.setError("Please Enter Email Id");
                        emailId.requestFocus();
                    } else if (pwd.isEmpty()) {
                        password.setError("Please Enter Password");
                        password.requestFocus();
                    } else if (!(email.isEmpty() && pwd.isEmpty())) {
                        users.addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                try {
                                    if ((dataSnapshot.child(email).child("password").getValue().toString()).equals(pwd)) {
                                        Intent inttomain = new Intent(MainActivity.this, homeActivity.class);
                                        inttomain.putExtra("uname", email);
                                        startActivity(inttomain);
                                        Toast.makeText(MainActivity.this, "Login Successful",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Incorrect Password, Please Try Again!!",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                                catch (Exception e){
                                    Log.d(TAG,e.toString());
                                    Toast.makeText(MainActivity.this, "Invalid Data",
                                            Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w(TAG, "Failed to read value.", error.toException());
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Something went wrong, Please Try Again!!",
                                Toast.LENGTH_LONG).show();
                    }
                }
        });
    }
}