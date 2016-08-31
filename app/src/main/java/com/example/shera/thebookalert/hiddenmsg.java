package com.example.shera.thebookalert;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by shera on 8/23/2016.
 */
public class hiddenmsg extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(hiddenmsg.this, "ServiceCreated", Toast.LENGTH_SHORT).show();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(hiddenmsg.this, "Service Started", Toast.LENGTH_SHORT).show();

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String SimSerialNumber = tm.getSimSerialNumber();

        final dbhelper helper = new dbhelper(this);

        Cursor c = helper.GetUser();
        if(c.moveToFirst()) {
            String num = c.getString(c.getColumnIndex(dbhelper.contact));
            sendSMS(num, "Couldn't get the location.\nSim Serial Number:"+SimSerialNumber);
        }
        else{
            Toast.makeText(hiddenmsg.this, "naah, skipped if", Toast.LENGTH_SHORT).show();
        }



        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            String loc = null;
            @Override
            public void onLocationChanged(Location location) {
                //Toast.makeText(hiddenmsg.this, "Entered Location Listener", Toast.LENGTH_SHORT).show();

                if(loc == null){

                    loc = "\nLatitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude();

                    TelephonyManager telemamanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String getSimSerialNumber = telemamanger.getSimSerialNumber();
                    String getSimNumber = telemamanger.getLine1Number();

                    String message = getSimNumber + "\n" + getSimSerialNumber + loc;

                    Cursor c = helper.GetUser();
                    if(c.moveToFirst()) {
                        String num = c.getString(c.getColumnIndex(dbhelper.contact));
                        sendSMS(num, loc);
                    }
                    else{
                        Toast.makeText(hiddenmsg.this, "naah, skipped if", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return 0;
        }
        locationManager.requestLocationUpdates("gps", 0, 0, locationListener);


        return START_STICKY;
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
}
