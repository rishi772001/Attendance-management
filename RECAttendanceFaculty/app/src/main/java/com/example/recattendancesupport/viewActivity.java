package com.example.recattendancesupport;

import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.firebase.database.*;

public class viewActivity extends AppCompatActivity {

    private static final String TAG = "viewActivity";
    Spinner month,year;
    EditText display;
    Button view;
    String uname,getYear,getMonth,attendance,path;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    public static final String[] months = {"1", "2", "3","4","5","6","7","8","9","10","11","12"};

    public static final String[] years = {"2015","2016","2017","2018","2019","2020","2021","2022","2023","2024","2025","2026"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        month = findViewById(R.id.month);
        year = findViewById(R.id.year);
        view =  findViewById(R.id.button4);
        display = findViewById(R.id.editText);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uname = extras.getString("uname");//The key argument here must match that used in the other activity
        }
        ArrayAdapter<String> madapter = new ArrayAdapter<String>(viewActivity.this,
                android.R.layout.simple_spinner_item,months);
        ArrayAdapter<String> yadapter = new ArrayAdapter<String>(viewActivity.this,
                android.R.layout.simple_spinner_item,years);

        madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month.setAdapter(madapter);
        month.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,
                                               int position, long id) {
                        Object item = adapterView.getItemAtPosition(position);
                        if (item != null) {
                            getMonth = item.toString();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // TODO Auto-generated method stub

                    }
                }
        );
        yadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(yadapter);
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    getYear = item.toString();
//                    path = "faculty/"+uname+"/attendance/"+getYear+"/"+getMonth+"/4" ;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });



        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                path = "faculty/"+uname+"/attendance/"+getYear+"/"+getMonth ;
                DatabaseReference myRef = database.getReference(path);

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        try {
                            attendance = "";
                            Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();

                            for (Map.Entry<String, Object> entry : td.entrySet()) {

                                String key = entry.getKey();
                                Object value = entry.getValue();
                                //Log.d(TAG, "base key:" + key);
                                attendance += key + "-" + getMonth + "-" + getYear + "\n\n";

                                if (value instanceof Map) {
                                    Map<String, Object> mapObj = (Map<String, Object>) value;
                                    for (Map.Entry<String, Object> ent : mapObj.entrySet()) {
                                        String k = ent.getKey();
                                        Object v = ent.getValue();
                                        attendance += "\t" + k + "\n";
                                        //                                    Log.d(TAG, "child key:" + k);
                                        //                                    Log.d(TAG, "child value:" + v);
                                    }
                                }
                                attendance += "\n";


                            }
                            display.setText(attendance);
                        }
                        catch (Exception e)
                        {
                            Log.d(TAG,e.toString());
                            Toast.makeText(viewActivity.this, "Invalid Data",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.d(TAG, "Failed to read value.", error.toException());
                    }
                });

            }
        });

    }


}
