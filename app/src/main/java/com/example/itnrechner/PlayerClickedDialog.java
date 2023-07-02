package com.example.itnrechner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class PlayerClickedDialog extends DialogFragment {

    private PlayerSearchData.Player player;
    AppCompatActivity activity;
    private final String[] options = new String[] {"Heim", "Gast", "Default Heim"};

    public PlayerClickedDialog(AppCompatActivity activity, PlayerSearchData.Player player) {
        this.player = player;
        this.activity = activity;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(player.getFirstname() + " " + player.getLastname() + " - " + player.getFedRank())
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText l;
                        if (which == 0) {
                            // Heim
                            l = activity.findViewById(R.id.editTextDu);
                            l.setText(String.valueOf(player.getFedRank()));
                        } else if (which == 1) {
                            // Gast
                            l = activity.findViewById(R.id.editTextGegner);
                            l.setText(String.valueOf(player.getFedRank()));
                        } else {
                            // Default Heim storing license number
                            String license = player.getLicenceNr();
                            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("default_license", license);
                            editor.apply();

                            // update ui fields
                            EditText itnEdit = activity.findViewById(R.id.editTextDu);
                            TextView defaultTextView = activity.findViewById(R.id.playerDefaultContainer);
                            PlayerSearchData.Player p = player;
                            itnEdit.setText(String.valueOf(p.getFedRank()));
                            String defaultPlayerText = p.getFirstname() + " " + p.getLastname() + " (" + p.getBirthYear()+")" + " - " + p.getClubName() + "\nITN: " + p.getFedRank();
                            defaultTextView.setText(defaultPlayerText);
                        }

                    }
                });
        return builder.create();
    }
}
