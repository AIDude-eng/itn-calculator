package com.example.itnrechner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import java.util.Optional;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        Spinner modeSpinner = findViewById(R.id.modeSpinner);
        // Set selection to Singles when returning to this Activity
        modeSpinner.setSelection(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner modeSpinner = findViewById(R.id.modeSpinner);
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (modeSpinner.getSelectedItem().toString().equals("Doppel")) {
                    // Change to Doubles Activity
                    Intent intent = new Intent(MainActivity.this, DoublesActivity.class);
                    startActivity(intent);
                } else if (modeSpinner.getSelectedItem().toString().equals("Doppel Teams")) {
                    // Change to Doubles Activity
                    Intent intent = new Intent(MainActivity.this, DoublesLineupActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button searchButton = findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                EditText editTextLastname = findViewById(R.id.lastname);
                String lastname = editTextLastname.getText().toString().trim();
                EditText editTextFirstname= findViewById(R.id.firstname);
                String firstname = editTextFirstname.getText().toString().trim();
                new PlayerSearchTask(MainActivity.this).execute(new PlayerSearchData.PlayerName(lastname, firstname));
            }
        });

        Button calcButton = findViewById(R.id.buttonCalculate);
        RadioGroup rgWinner = findViewById(R.id.radioWinner);
        // Click Listener for Calculating
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextItnYou = findViewById(R.id.editTextDu);
                EditText editTextItnOpponent = findViewById(R.id.editTextGegner);
                String itnYouString = editTextItnYou.getText().toString();
                String itnOpponentString = editTextItnOpponent.getText().toString();

                Spinner sikYouSpinner = findViewById(R.id.sikSpinnerYou);
                String sikYou = sikYouSpinner.getSelectedItem().toString();
                Spinner sikOpponentSpinner = findViewById(R.id.sikSpinnerOpponent);
                String sikOpponent = sikOpponentSpinner.getSelectedItem().toString();

                if (ITNCalculator.isNumeric(itnYouString) && ITNCalculator.isNumeric(itnOpponentString)) {
                    DecimalFormat df = new DecimalFormat("###.###");
                    boolean won = rgWinner.getCheckedRadioButtonId() == R.id.radioWon;
                    float itnYou = Float.parseFloat(itnYouString);
                    float itnOpponent = Float.parseFloat(itnOpponentString);
                    if (itnYou >= 1 && itnOpponent >= 1) {
                        float itnDiff = ITNCalculator.calcITNDif(itnYou, itnOpponent, won);
                        float itnDiffSik = ITNCalculator.calcITNDifAfterSik(itnDiff, sikYou, sikOpponent);
                        TextView itnDiffText = findViewById(R.id.textItnDif);

                        itnDiffText.setText(String.format("ITN Veränderung: %s%s", won ? "-" : "+", df.format(itnDiffSik)));


                        TextView newItnText = findViewById(R.id.textNewItn);
                        float newItn = won ? itnYou - itnDiffSik : itnYou + itnDiffSik;
                        newItnText.setText("Neue ITN: " + df.format(newItn));
                    } else {
                        Toast.makeText(MainActivity.this, "ITN muss größer 1 sein!", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "ITN muss eine Zahl sein!", Toast.LENGTH_LONG).show();

                }
            }
        });

        // check default license number in shared preferences
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "";
        String default_license = sharedPref.getString("default_license", defaultValue);
        if (!default_license.isEmpty()) {
            // if default license stored then search for player and insert itn into home field
            new PlayerLicenseSearchTask(MainActivity.this).execute(new PlayerSearchData.PlayerName(default_license));
        }
    }
}