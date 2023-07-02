package com.example.itnrechner;

import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class PlayerLicenseSearchTask extends PlayerSearchTask {
    public PlayerLicenseSearchTask(AppCompatActivity activity) {
        super(activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPostExecute(PlayerSearchData playerSearchData) {
        EditText itnEdit = activity.findViewById(R.id.editTextDu);
        TextView defaultTextView = activity.findViewById(R.id.playerDefaultContainer);
        if (playerSearchData.data.getPlayers().length != 0 ){
            PlayerSearchData.Player p = playerSearchData.data.getPlayers()[0];
            itnEdit.setText(String.valueOf(p.getFedRank()));
            String defaultPlayerText = p.getFirstname() + " " + p.getLastname() + " (" + p.getBirthYear()+")" + " - " + p.getClubName() + "\nITN: " + p.getFedRank();
            defaultTextView.setText(defaultPlayerText);
        }
    }
}
