package com.computer_network.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;

import android.os.Bundle;
import android.widget.Toast;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity3 extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;

    private Geocoder geocoder;
    private Sensor accelerometer;
    private Sensor magnetometer;

    private Sensor gyroscope;

    private float[] gravityValues = new float[3];
    private float[] magneticValues = new float[3];

    private float[] gyroscopeValues = new float[3];

    private TextView locationTextView;
    private static final float RAD_TO_DEG = (float) (180.0 / Math.PI);

    private static final double EARTH_RADIUS = 6371000; // Approximate Earth radius in meters
    private double referenceLatitude = 38.6358242; // Set this to the known initial latitude
    private double referenceLongitude = -90.2327020;

    private static final float FILTER_ALPHA = 0.1f; // Low-pass filter constant (adjust as needed)
    private double filteredLat;
    private double filteredLon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        locationTextView = findViewById(R.id.locationTextView);
        geocoder = new Geocoder(this, Locale.getDefault());

        // Initialize sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Register sensor listeners
        if (accelerometer != null && magnetometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            locationTextView.setText("Accelerometer or magnetometer not available on this device.");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravityValues = event.values.clone();
        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magneticValues = event.values.clone();
                magneticValues = event.values.clone();
            } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                gyroscopeValues = event.values.clone();
            }
        }

        float[] rotationMatrix = new float[9];
        float[] inclinationMatrix = new float[9];
        float[] orientationValues = new float[3];

        // Get the rotation matrix
        boolean success = SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, gravityValues, magneticValues);

        if (success) {
            // Get the orientation
            SensorManager.getOrientation(rotationMatrix, orientationValues);

            // Convert radians to degrees
            float azimuth = (float) Math.toDegrees(orientationValues[0]);

            float pitch = (float) Math.toDegrees(orientationValues[1]);
            float roll = (float) Math.toDegrees(orientationValues[2]);

            // Use gyroscope data for better rotation tracking
            float gyroX = gyroscopeValues[0];
            float gyroY = gyroscopeValues[1];
            float gyroZ = gyroscopeValues[2];
            // Integrate gyroscope data to update the orientation
            float dt = 1.0f / 30.0f; // Assuming a sensor rate of 30 Hz
            azimuth += gyroX * dt;
            pitch += gyroY * dt;
            roll += gyroZ * dt;

            // Estimate latitude and longitude
            double latEstimate = referenceLatitude + (pitch / RAD_TO_DEG) * 0.0001;
            double lonEstimate = referenceLongitude+(roll / RAD_TO_DEG) * 0.0001;
            // Apply low-pass filter
            filteredLat = lowPass(latEstimate, filteredLat, FILTER_ALPHA);
            filteredLon = lowPass(lonEstimate, filteredLon, FILTER_ALPHA);
            // Display the results
            locationTextView.setText("Azimuth: " + azimuth +
                    "\nEst. Latitude: " + filteredLat + "\nEst. Longitude: " + filteredLon);

            // Get readable address in a background task
            new GetAddressTask().execute(filteredLat, filteredLon);



        }
    }

    private class GetAddressTask extends AsyncTask<Double, Void, String>{

        @Override
        protected String doInBackground(Double... params) {
            double lat = params[0];
            double lon = params[1];

            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    return address.getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                locationTextView.append("\nAddress: " + result);
            } else {
               //  locationTextView.append("\nNo address found");
            }
        }
    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null && magnetometer != null && gyroscope != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    private double lowPass(double current, double last, float alpha) {
        return last + alpha * (current - last);
    }
}