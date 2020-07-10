package com.example.recattendancesupport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.Random;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class homeActivity extends AppCompatActivity {
    Button btnLogout,btnGenerate,btnStop,btnView;
    TextView txtCode,txtText;
    String uname;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("faculty");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uname = extras.getString("uname");//The key argument here must match that used in the other activity
        }
        btnLogout = findViewById(R.id.button2);
        btnGenerate = findViewById(R.id.btngenerate);
        btnView = findViewById(R.id.button3);
        btnStop = findViewById(R.id.btnstop);
        txtCode = findViewById(R.id.txtcode);
        txtText = findViewById(R.id.txttext);
        btnStop.setEnabled(false);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inttomain = new Intent(homeActivity.this,MainActivity.class);
                startActivity(inttomain);
            }
        });

        btnGenerate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Random r = new Random();
                int code = r.nextInt(1000000);
                txtCode.setText(code+"");
                myRef.child(uname).child("code").setValue(code+"");
                btnGenerate.setEnabled(false);
                btnStop.setEnabled(true);
                Toast.makeText(homeActivity.this, "Code Generated Successfully, Kindly ask students to enter the code below",
                        Toast.LENGTH_LONG).show();
                txtText.setText("Click below stop button to disable code!");
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                myRef.child(uname).child("code").setValue("0");
                btnGenerate.setEnabled(true);
                btnStop.setEnabled(false);
                txtCode.setText("");
                txtText.setText("");
                Toast.makeText(homeActivity.this, "Code Disabled Successfully",
                        Toast.LENGTH_LONG).show();
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inttoview = new Intent(homeActivity.this,viewActivity.class);
                inttoview.putExtra("uname",uname);
                startActivity(inttoview);
            }
        });

    }
}
