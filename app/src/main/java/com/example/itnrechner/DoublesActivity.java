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

public class DoublesActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        Spinner modeSpinner = findViewById(R.id.modeSpinner);
        // Set selection to Doubles when returning to this Activity
        modeSpinner.setSelection(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubles);

        Spinner modeSpinner = findViewById(R.id.modeSpinner);
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (modeSpinner.getSelectedItem().toString().equals("Einzel")) {
                    // Change to Doubles Activity
                    Intent intent = new Intent(DoublesActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button calcButton = findViewById(R.id.buttonCalculate);
        RadioGroup rgWinner = findViewById(R.id.radioWinner);
        // Click Listener for Calculating
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextItnYou = findViewById(R.id.editTextDu);
                EditText editTextItnPartner = findViewById(R.id.editTextPartner);
                EditText editTextItnOpponent1 = findViewById(R.id.editTextGegner1);
                EditText editTextItnOpponent2 = findViewById(R.id.editTextGegner2);
                String itnYouString = editTextItnYou.getText().toString();
                String itnPartnerString = editTextItnPartner.getText().toString();
                String itnOpponent1String = editTextItnOpponent1.getText().toString();
                String itnOpponent2String = editTextItnOpponent2.getText().toString();


                if (ITNCalculator.isNumeric(itnYouString) && ITNCalculator.isNumeric(itnPartnerString)
                        && ITNCalculator.isNumeric(itnOpponent1String) && ITNCalculator.isNumeric(itnOpponent2String)) {
                    DecimalFormat df = new DecimalFormat("###.###");
                    boolean won = rgWinner.getCheckedRadioButtonId() == R.id.radioWon;
                    float itnYou = Float.parseFloat(itnYouString);
                    float itnPartner = Float.parseFloat(itnPartnerString);
                    float itnOpponent1 = Float.parseFloat(itnOpponent1String);
                    float itnOpponent2 = Float.parseFloat(itnOpponent2String);
                    if (itnYou >= 1 && itnPartner >= 1 && itnOpponent1 >= 1 && itnOpponent2 >= 1) {
                        float itnMeanHome = (itnYou + itnPartner) / 2;
                        float itnMeanOpponents = (itnOpponent1 + itnOpponent2) / 2;
                        float itnDiff = (float)(ITNCalculator.calcITNDif(itnMeanHome, itnMeanOpponents, won) * 0.25);
                        TextView itnDiffText = findViewById(R.id.textItnDif);

                        itnDiffText.setText(String.format("ITN Veränderung: %s%s", won ? "-" : "+", df.format(itnDiff)));


                        TextView newItnTextYou = findViewById(R.id.textNewItnDu);
                        float newItnYou = won ? itnYou - itnDiff : itnYou + itnDiff;
                        newItnTextYou.setText("Neue ITN Du: " + df.format(newItnYou));
                        TextView newItnTextPartner = findViewById(R.id.textNewItnPartner);
                        float newItnPartner = won ? itnPartner - itnDiff : itnPartner + itnDiff;
                        newItnTextPartner.setText("Neue ITN Partner: " + df.format(newItnPartner));
                    } else {
                        Toast.makeText(DoublesActivity.this, "ITN muss größer 1 sein!", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(DoublesActivity.this, "ITN muss eine Zahl sein!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}