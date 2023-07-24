package com.example.itnrechner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DoublesLineupActivity extends AppCompatActivity {
    public static final int[] playerNameids = new int[] {R.id.player1Name, R.id.player2Name, R.id.player3Name, R.id.player4Name, R.id.player5Name, R.id.player6Name};
    public static final int[] playerITNids = new int[] {R.id.player1ITN, R.id.player2ITN, R.id.player3ITN, R.id.player4ITN, R.id.player5ITN, R.id.player6ITN};

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

        Button calcButton = findViewById(R.id.buttonCalculate);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                // get names and itns from fields
                String[] names = new String[6];
                String[] itns = new String[6];

                for (int i = 0; i < 6; i++) {
                    EditText playerNameInput = findViewById(playerNameids[i]);
                    String name = playerNameInput.getText().toString();
                    EditText playerITNInput = findViewById(playerITNids[i]);
                    String itn = playerITNInput.getText().toString();
                    names[i] = name;
                    itns[i] = itn;
                }

                // check that all inputs correct
                boolean namesNonEmpty = Arrays.stream(names).allMatch(name -> !name.isEmpty());
                boolean itnsValid = Arrays.stream(itns).allMatch(itn -> ITNCalculator.isNumeric(itn));
                if (namesNonEmpty && itnsValid) {
                    // convert itns to doubles
                    double[] itnsNumeric = Arrays.stream(itns).mapToDouble(i -> Double.parseDouble(i)).toArray();
                    // create Players from inputs
                    DoublesLineupCalculator.Player[] players = new DoublesLineupCalculator.Player[6];
                    for (int i = 0; i < players.length; i++) {
                        players[i] = new DoublesLineupCalculator.Player(names[i], itnsNumeric[i]);
                    }
                    // calculate lineup
                    DoublesLineupCalculator dlc = new DoublesLineupCalculator(players);
                    dlc.generateLineup();
                    List<DoublesLineupCalculator.DoubleLineup> lineups = dlc.getLineups();

                    // render lineups
                    LinearLayout l = findViewById(R.id.lineupResultsContainer);
                    l.removeAllViews();
                    int i = 1;
                    for (DoublesLineupCalculator.DoubleLineup lineup : lineups) {
                        lineup.sortTeams();
                        String result = "Aufstellung " + i + ":" + "\n" + lineup.toString();
                        TextView lineupText = new TextView(DoublesLineupActivity.this);
                        lineupText.setText(result);
                        lineupText.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        lineupText.setTextIsSelectable(true);
                        l.addView(lineupText);
                        i++;
                    }

                    // store names and itns as one string in shared preferences when valid input and calculated
                    String namesCSV = Arrays.stream(names).collect(Collectors.joining(";"));
                    String itnsCSV = Arrays.stream(itns).collect(Collectors.joining(";"));
                    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("doubles_lineup_player_names", namesCSV);
                    editor.putString("doubles_lineup_player_itns", itnsCSV);
                    editor.apply();
                } else {
                    Toast.makeText(DoublesLineupActivity.this, "Namen dürfen nicht leer sein und ITNs müssen Zahlen sein!", Toast.LENGTH_LONG).show();

                }
            }
        });

        // load stored previous lineup that was entered
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "";
        String namesCSV = sharedPref.getString("doubles_lineup_player_names", defaultValue);
        String itnsCSV = sharedPref.getString("doubles_lineup_player_itns", defaultValue);

        if (!namesCSV.isEmpty() && !itnsCSV.isEmpty()) {
            // split csv string into arrays
            String[] names = namesCSV.split(";");
            String[] itns = itnsCSV.split(";");

            // put names and itns into ui
            if (names.length == 6 && itns.length == 6) {
                for (int i = 0; i < 6; i++) {
                    EditText playerNameInput = findViewById(playerNameids[i]);
                    playerNameInput.setText(names[i]);
                    EditText playerITNInput = findViewById(playerITNids[i]);
                    playerITNInput.setText(itns[i]);
                }
            }
        }
    }
}