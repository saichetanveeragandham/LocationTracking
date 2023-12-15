package com.computer_network.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class First_Page extends AppCompatActivity {

    private Button fusedBtn,gpsOff,intAndGpsOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        fusedBtn = findViewById(R.id.fused);
        gpsOff = findViewById(R.id.gpsOff);
        intAndGpsOff  = findViewById(R.id.gpsandnetOff);

        fusedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(First_Page.this,MainActivity5.class);
                startActivity(intent);
            }
        });

        gpsOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(First_Page.this,MainActivity3.class);
                startActivity(intent);
            }
        });

        intAndGpsOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(First_Page.this,MapBox.class);
                startActivity(intent);
            }
        });
    }
}