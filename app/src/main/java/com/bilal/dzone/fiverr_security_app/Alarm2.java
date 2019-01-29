package com.bilal.dzone.fiverr_security_app;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bilal.dzone.fiverr_security_app.JsonParsing.Check_internet_connection;
import com.bilal.dzone.fiverr_security_app.JsonParsing.JsonParser;
import com.bilal.dzone.fiverr_security_app.JsonParsing.Url;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Alarm2 extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    SharedPreferences sharedPreferences;
    Button button;
    List<Name> names;
    private DatabaseHelper db;
    private NameAdapter nameAdapter;
    CountDownTimer countDownTimer;

    protected static final String TAG = "location-updates-sample";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    private final static String LOCATION_KEY = "location-key";
    private final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 10;
    //    private ActivityMainBinding mBinding;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    private String mLastUpdateTime;
    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private String mLastUpdateTimeLabel;
    double lati, longi;


    /////////////////////
    boolean server_check=false;
    JSONObject jp_obj;
    JSONArray jar_array;
    String[] name
            ,lat
            ,lon
            ,token;
    int i=0;
    static double PI_RAD = Math.PI / 180.0;
    String server_response="0"
            ,server_response_text,name_;
    ArrayList<Get_Set> setMap = new ArrayList<>();
    ArrayList<Get_Set> setMap1 = new ArrayList<>();
    String all_tokens;


    TextView timer;
    EditText pass;
    String pass_,et_pass;
    public int counter = 15;
    String cityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm2);


        sharedPreferences = this.getSharedPreferences("DataStore" , Context.MODE_PRIVATE);
        pass_ = sharedPreferences.getString("pass", "Ni Mila Kuch");
        name_ = sharedPreferences.getString("name", "Ni Mila Kuch");



        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }




        //start getting location
        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mLastUpdateTimeLabel = getResources().getString(R.string.last_update_time_label);
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        updateValuesFromBundle(savedInstanceState);
        buildGoogleApiClient();

        startLocationUpdates();




        button = findViewById(R.id.btn);
        timer = findViewById(R.id.timer);
        pass = findViewById(R.id.pass);


        //start timer
        countDownTimer = new CountDownTimer(15000, 1000){
            public void onTick(long millisUntilFinished){
                timer.setText(String.valueOf(counter));
                counter--;
            }
            public  void onFinish(){
                timer.setText("Informing Contacts!");

                //send sms
                for (int i = 0; i < names.size(); i++) {
//                    Toast.makeText(Alarm2.this, names.get(i).getNum()+"", Toast.LENGTH_SHORT).show();

                    sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
                    if (sharedPreferences.getString("toggle", "Ni Mila Kuch").equals("on")){
                        new SendNotification().execute();
                    }


                    SmsManager sms = SmsManager.getDefault();  // using android SmsManager
                        sms.sendTextMessage(names.get(i).getNum(), null,
                                "AYUDA URGENTE, estoy en"+" "+cityName, null, null);  // adding number and text

                }
                Intent intent = new Intent(Alarm2.this,MainActivity.class);
                startActivity(intent);
                Alarm2.this.finish();
            }
        }.start();

        //database
        db = new DatabaseHelper(this);
        names = new ArrayList<>();

        loadNames();





        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(lati,
                            longi, 1);
                    if (addresses.size() > 0) {

                        cityName = addresses.get(0).getAddressLine(0);
//                        Toast.makeText(Alarm2.this, cityName, Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(Alarm2.this, "GPS Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, 2000);




        if (!sharedPreferences.getString("name", "").equals("")) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (new Check_internet_connection(getApplicationContext()).isNetworkAvailable()) {

                        new Get_List().execute();

                    } else {

                        Toast.makeText(getApplicationContext(),
                                "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }


                }
            }, 4000);

        }








        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_pass = pass.getText().toString().trim();

                if(et_pass.equals(pass_)){

                    countDownTimer.cancel();
                    Toast.makeText(Alarm2.this, "Alarm Stopped", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Alarm2.this, MainActivity.class);
                    startActivity(intent);
                    Alarm2.this.finish();
                }
                else
                    Toast.makeText(Alarm2.this, "Wrong Password", Toast.LENGTH_SHORT).show();
            }
        });





    }



    private void loadNames() {
//        names.clear();

        Cursor cursor = db.getNames();
        if (cursor.moveToFirst()) {
            do {
                Name name = new Name(

                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUM)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID))

                );
                names.add(name);
            } while (cursor.moveToNext());
        }

        nameAdapter = new NameAdapter(this, R.layout.names, names);
//        Toast.makeText(this, names.size()+"", Toast.LENGTH_SHORT).show();
    }






    //////////////////////////////////////////////////////////////
////////////get location

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);

            }

            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
            updateUI();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void startUpdatesButtonHandler() {

        if (!isPlayServicesAvailable(this)) return;
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
        } else {
            return;
        }

        if (Build.VERSION.SDK_INT < 23) {

            startLocationUpdates();
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            startLocationUpdates();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                showRationaleDialog();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }

    public void stopUpdatesButtonHandler(View view) {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;

            stopLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates");

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        if (ContextCompat.checkSelfPermission(Alarm2.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, Alarm2.this);
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            status.startResolutionForResult(Alarm2.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }



    private void updateUI() {
        if (mCurrentLocation == null) return;


        lati=mCurrentLocation.getLatitude();
        longi=mCurrentLocation.getLongitude();
        String str = "Latitude: "+lati+" Longitude: "+longi;

//        Toast.makeText(Alarm2.this, str, Toast.LENGTH_LONG).show();

    }

    protected void stopLocationUpdates() {
        Log.i(TAG, "stopLocationUpdates");
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startLocationUpdates();
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        mRequestingLocationUpdates = false;
                        Toast.makeText(Alarm2.this, "Allow all permissions!", Toast.LENGTH_SHORT).show();
                    } else {
                        showRationaleDialog();
                    }
                }
                break;
            }
        }
    }

    private void showRationaleDialog() {
        new android.app.AlertDialog.Builder(this)
                .setPositiveButton("Allow Location Access!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(Alarm2.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                })

                .setCancelable(false)
                .setMessage("Allow Location Access! otherwise you will not be able to do survey")
                .show();
    }

    public static boolean isPlayServicesAvailable(Context context) {
        // Google Play Service APKが有効かどうかチェックする
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog((Activity) context, resultCode, 2).show();
            return false;
        }
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();


    }

    @Override
    public void onResume() {
        super.onResume();
        isPlayServicesAvailable(this);
//        Toast.makeText(this, "resume", Toast.LENGTH_SHORT).show();

        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
//        if (mGoogleApiClient.isConnected()) {
//            stopLocationUpdates();
//        }
    }

    @Override
    protected void onStop() {
//        stopLocationUpdates();
//        mGoogleApiClient.disconnect();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
//            updateUI();
        }

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
//        Toast.makeText(this, getResources().getString(R.string.location_updated_message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }



////////////////////////////////////////////////////////////////////


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }




    //get  list
    public class Get_List extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "list");

                String str_req = JsonParser.multipartFormRequestForFindFriends(Url.ulr, "UTF-8", obj, null);

                jp_obj = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;

                name   = new String[(jar_array.length()-1)];
                lat         = new String[(jar_array.length()-1)];
                lon         = new String[(jar_array.length()-1)];
                token         = new String[(jar_array.length()-1)];


                c = jar_array.getJSONObject(0);

                if (c.length() > 0) {

                    server_response = c.getString("response");

                    if (server_response.equals("0")) {
                        server_response_text = c.getString("response-text");

                    }
                }

                int j = 1;

                if (server_response.equals("1")) {
                    for (i = 0; j < jar_array.length(); i++) {

                        c = jar_array.getJSONObject(j);

                        if (c.length() > 0) {

                            name[i]       = c.getString("name");
                            lat[i]        = c.getString("lat");
                            lon[i]        = c.getString("lon");
                            token[i]    = c.getString("token");

                        }

                        j++;
                    }


                    for (int k = 0; k < name.length; k++) {

                        setMap.add(new Get_Set(token[k],lat[k], lon[k]));


                    }

                }


                server_check = true;


            } catch (Exception e) {
                e.printStackTrace();

            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {

//            progressDialog.dismiss();

            if (server_check) {

                if (server_response.equals("1")) {


                    for(int i =0;i<setMap.size();i++) {



                        double phi1 = lati * PI_RAD;
                        double phi2 = Double.parseDouble(setMap.get(i).getLat()) * PI_RAD;
                        double lam1 = longi * PI_RAD;
                        double lam2 = Double.parseDouble(setMap.get(i).getLong()) * PI_RAD;

                        double a = 6371.01 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1));


                        if(a <= 1){

                            setMap1.add(new Get_Set(setMap.get(i).getToken(),setMap.get(i).getLat(),
                                    setMap.get(i).getLong()));


                        }


                    }


                    if(setMap1.size() > 0){

                        //add arraylist to array
                        String[] tokens = new String[setMap1.size()];
                        for(int i=0;i<setMap1.size();i++){

                            tokens[i] = setMap1.get(i).getToken();
                        }

                        //seperate token with ,
                        getArrayAsString(tokens,",");



                    }
                    else
                        Toast.makeText(Alarm2.this, "No Nearby Users Found", Toast.LENGTH_SHORT).show();



                } else {
                    Toast.makeText(Alarm2.this, server_response_text, Toast.LENGTH_SHORT).show();

                }


            } else {

                Toast.makeText(Alarm2.this, "Error while loading data", Toast.LENGTH_SHORT).show();
            }

        }
    }




    //send notifications
    public class SendNotification extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "notify");
                obj.put("token", all_tokens);
                obj.put("name", name_);
                obj.put("lat", lati);
                obj.put("lon", longi);

                String str_req = JsonParser.multipartFormRequestForFindFriends(Url.ulr, "UTF-8", obj, null);

                jp_obj = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;



                c = jar_array.getJSONObject(0);

                if (c.length() > 0) {

                    server_response = c.getString("response");

                    if (server_response.equals("0")) {
                        server_response_text = c.getString("response-text");

                    }
                }




                server_check = true;


            } catch (Exception e) {
                e.printStackTrace();

            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {



        }
    }





    //adding seperator , to string
    public String getArrayAsString(String[] stringArray, String separator) {
        if(stringArray == null || stringArray.length == 0) return "";

        StringBuilder finalString = new StringBuilder(stringArray[0]);

        for(int i = 1; i < stringArray.length; ++i)
            finalString.append(separator).append(stringArray[i]);

        all_tokens = finalString.toString();

        return finalString.toString();
    }








    @Override
    public void onBackPressed() {

        Toast.makeText(this, "Deactivate Alarm By Password", Toast.LENGTH_SHORT).show();
    }


}
