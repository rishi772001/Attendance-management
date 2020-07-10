package com.example.recattendance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

import java.util.Calendar;

public class homeActivity extends AppCompatActivity {
    private static final String TAG = "homeActivity";
    Button btnLogout;
    EditText code;
    EditText subject;
    Button btnSubmit;
    String rollno;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    private void attendance(String check){

        if(code.getText().toString().isEmpty())
        {
            Toast.makeText(homeActivity.this,"Enter Code",Toast.LENGTH_SHORT).show();
        }
        else if(check.equals(code.getText().toString())){
            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            String d = Integer.toString(day);
            int month = c.get(Calendar.MONTH);
            month++;
            String m = Integer.toString(month);
            int year = c.get(Calendar.YEAR);
            String y = Integer.toString(year);

            myRef.child("attendance").child(y).child(m).child(d).child(rollno).setValue("present");

            Toast.makeText(homeActivity.this,"Attendance Provided",Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(homeActivity.this,"Invalid Code, Please try again!!",Toast.LENGTH_SHORT).show();
        }
    }
    private void retrieve(String path){
        myRef = database.getReference(path);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    String checkCode = dataSnapshot.child("code").getValue(String.class);
                    if (checkCode != null)
                        attendance(checkCode);
                    else
                        Toast.makeText(homeActivity.this, "Null", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(homeActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                }
            }


            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.d(TAG, "loadPost:onCancelled", error.toException());
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rollno = extras.getString("uname");//The key argument here must match that used in the other activity
        }

        code = findViewById(R.id.editText);
        subject = (EditText) findViewById(R.id.sub);
        btnLogout = findViewById(R.id.button2);
        btnSubmit = findViewById(R.id.button3);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent inttomain = new Intent(homeActivity.this,MainActivity.class);
                startActivity(inttomain);
            }
        });



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = subject.getText().toString();
                String path = "faculty/"+s;
                if(s == null || s == "")
                    Toast.makeText(homeActivity.this, "Subject not found", Toast.LENGTH_SHORT).show();
                else
                    retrieve(path);
            }
        });
    }
}
