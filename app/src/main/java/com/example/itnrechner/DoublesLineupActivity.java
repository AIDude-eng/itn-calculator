package com.example.itnrechner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class DoublesLineupActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        Spinner modeSpinner = findViewById(R.id.modeSpinner);
        // Set selection to Doubles when returning to this Activity
        modeSpinner.setSelection(2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubles_lineup);

        Spinner modeSpinner = findViewById(R.id.modeSpinner);
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (modeSpinner.getSelectedItem().toString().equals("Einzel")) {
                    // Change to Doubles Activity
                    Intent intent = new Intent(DoublesLineupActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (modeSpinner.getSelectedItem().toString().equals("Doppel")) {
                    // Change to Doubles Activity
                    Intent intent = new Intent(DoublesLineupActivity.this, DoublesActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}