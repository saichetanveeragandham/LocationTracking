package com.computer_network.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

import com.mapbox.geojson.Point;

public class MainActivity2 extends AppCompatActivity {



    private SensorManager sensorManager;
    private Sensor accelerometer, gyroscope, magnetometer;
    private

    float[] accelerometerReading = new

            float[3];
    private

    float[] gyroscopeReading = new

            float[3];
    private

    float[] magnetometerReading = new

            float[3];
    private

    float[] rotationMatrix = new

            float[9];
    private float[] orientationValues = new float[3];

    private double latitude = 37.7749, longitude = -122.4194; // Initial position
    private float heading = 0; // Initial heading
    private

    long timestamp = System.currentTimeMillis();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gyroscopeListener, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(magnetometerListener, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);

        updateMap();


    }

    private SensorEventListener accelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            accelerometerReading = event.values;
            getOrientation();
            updateMap();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private SensorEventListener gyroscopeListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            gyroscopeReading = event.values;
            getOrientation();
            updateMap();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private SensorEventListener magnetometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            magnetometerReading = event.values;
            getOrientation();
            updateMap();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private void getOrientation(){
        SensorManager.getRotationMatrix(rotationMatrix,

                null, accelerometerReading, magnetometerReading);
        SensorManager.getOrientation(rotationMatrix, orientationValues);
        heading = (float) Math.toDegrees(orientationValues[0]);


    }

    private void updateMap() {
        double newLatitude = latitude + (heading / 360) * 360 * Math.cos(latitude * Math.PI / 180);
        double newLongitude = longitude + (heading / 360) * 360 * Math.sin(latitude * Math.PI / 180);

        Point point = Point.fromLngLat(newLongitude, newLatitude);


        // Update the current position and timestamp
        latitude = newLatitude;
        longitude = newLongitude;
        timestamp = System.currentTimeMillis();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }
}