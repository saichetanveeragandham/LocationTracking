package com.computer_network.myapplication;

import android.hardware.Sensor;
import android.hardware.SensorManager;

public class SensorFusion {

    private float[] accelerometerValues = new float[3];
    private float[] magnetometerValues = new float[3];
    private float[] gyroscopeValues = new float[3];

    private double latitude = 0;
    private double longitude = 0;

    private static final double EARTH_RADIUS = 6371000; // Approximate Earth radius in meters

    public void updateSensors(float[] values) {
        if (values.length == 3) {
            if (values[0] == Sensor.TYPE_ACCELEROMETER) {
                accelerometerValues = values.clone();
            } else if (values[0] == Sensor.TYPE_MAGNETIC_FIELD) {
                magnetometerValues = values.clone();
            } else if (values[0] == Sensor.TYPE_GYROSCOPE) {
                gyroscopeValues = values.clone();
            }
        }
    }

    public float[] calculateOrientation() {
        float[] rotationMatrix = new float[9];
        float[] inclinationMatrix = new float[9];
        float[] orientationValues = new float[3];

        // Get the rotation matrix
        boolean success = SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, accelerometerValues, magnetometerValues);

        if (success) {
            // Get the orientation
            SensorManager.getOrientation(rotationMatrix, orientationValues);

            // Convert radians to degrees
            float roll = (float) Math.toDegrees(orientationValues[0]);
            float pitch = (float) Math.toDegrees(orientationValues[1]);
            float yaw = (float) Math.toDegrees(orientationValues[2]);

            return new float[]{roll, pitch, yaw};
        } else {
            return null;
        }
    }

    public void estimatePosition(float roll, float pitch, float yaw) {
        // Estimate the change in latitude and longitude
        double dlat = pitch * EARTH_RADIUS / 180;
        double dlon = yaw * EARTH_RADIUS * Math.cos(latitude) / 180;

        // Update the latitude and longitude
        latitude += dlat;
        longitude += dlon;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
