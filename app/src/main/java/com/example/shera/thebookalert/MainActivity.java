package com.example.shera.thebookalert;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button b;

    EditText p;
    dbhelper helper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b=(Button)findViewById(R.id.submit);

        p= (EditText) findViewById(R.id.pnoneText);
        helper = new  dbhelper(this);

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Intent inti = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(inti);
        }


        Cursor c = helper.GetUser();
        if(c.moveToFirst()){
            Toast.makeText(MainActivity.this, "Saved number found", Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, c.getString(c.getColumnIndex(dbhelper.contact)), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(),hiddenmsg.class);
            startService(i);
        }
        else {
            Toast.makeText(MainActivity.this, "No saved number found", Toast.LENGTH_SHORT).show();
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (p.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this,"Please provide a Valid number",Toast.LENGTH_LONG).show();

                }
                else
                {


                    try {

                        AlertDialog.Builder builder = new  AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("We recommend to use mobile number other then the current one because its probably going to get stolen. Do you wish to edit number?");
                        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ;

                            }
                        });

                        builder.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                boolean b = helper.update(p.getText().toString());
                                if (b)
                                {
                                    Toast.makeText(MainActivity.this, "Record updated successfully", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(),hiddenmsg.class);
                                    startService(i);
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "There was some problem inserting the row", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();



                    }
                    catch (Exception exp)
                    {
                        Toast.makeText(MainActivity.this,exp.toString(), Toast.LENGTH_SHORT).show();
                    }
                }


            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RelativeLayout relativeLayout= (RelativeLayout) findViewById(R.id.relativeLayout);
        if (item.getItemId()== R.id.aboutapp)
        {
            AlertDialog.Builder builder = new  AlertDialog.Builder(MainActivity.this);
            builder.setMessage("BootAlert is an anti-theft app that can help you track your phone in case of losing it or if someone steals it.\nYou can add a phone number to receive " +
                    "texts containing phone's current geographical coordinates and the serial number of the sim in the phone on that specific time.")
                    .setTitle("About BootAlert")
                    .setPositiveButton("BACK",null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkpremision(this, Manifest.permission.ACCESS_FINE_LOCATION, 2);
    }

    public void checkpremision (Activity activity, String permision, int requeacode)
    {
        if (ActivityCompat.checkSelfPermission(activity,permision)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, new String[]{permision},requeacode);
        }
        else
        {
            Toast.makeText(MainActivity.this, "premission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==2)
        {
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {

                Toast.makeText(MainActivity.this, "Granted", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
