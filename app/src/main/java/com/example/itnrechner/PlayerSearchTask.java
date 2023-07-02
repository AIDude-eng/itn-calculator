package com.example.itnrechner;

import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class PlayerSearchTask extends AsyncTask<PlayerSearchData.PlayerName, Void, PlayerSearchData>  {

    protected AppCompatActivity activity;
    public PlayerSearchTask(AppCompatActivity activity) {
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected PlayerSearchData doInBackground(PlayerSearchData.PlayerName... playerNames) {
        PlayerSearchData.PlayerName player = playerNames[0];
        return PlayerSearchData.searchPlayers(player.getLastname(), player.getFirstname(), player.getLicense());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPostExecute(PlayerSearchData playerSearchData) {
        LinearLayout l = activity.findViewById(R.id.playerResultsContainer);
        l.removeAllViews();
        if (playerSearchData.data.getPlayers().length != 0 ){
            PlayerSearchData.Player[] players = playerSearchData.data.getPlayers();
            String[] results = Arrays.stream(players).map(p -> p.getFirstname() + " " + p.getLastname() + " (" + p.getBirthYear()+")" + " - " + p.getClubName()
                    + "\nITN: " + p.getFedRank()).toArray(String[]::new);
            for (int i = 0; i < results.length; i++) {
                String result = results[i];
                PlayerSearchData.Player player = players[i];
                TextView playerText = new TextView(activity);
                playerText.setText(result);
                playerText.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                playerText.setClickable(true);
                playerText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new PlayerClickedDialog(activity, player).show(activity.getSupportFragmentManager(), "Test");
                    }
                });
                l.addView(playerText);
            }
        }
    }
}
