package com.example.itnrechner;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerSearchTask extends AsyncTask<PlayerSearchData.PlayerName, Void, PlayerSearchData>  {

    private AppCompatActivity activity;
    public PlayerSearchTask(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    protected PlayerSearchData doInBackground(PlayerSearchData.PlayerName... playerNames) {
        PlayerSearchData.PlayerName player = playerNames[0];
        return PlayerSearchData.searchPlayers(player.getLastname(), player.getFirstname());
    }

    @Override
    protected void onPostExecute(PlayerSearchData playerSearchData) {
        if (playerSearchData.data.getPlayers().length != 0 ){
            PlayerSearchData.Player player = playerSearchData.data.getPlayers()[0];
            ListView l = activity.findViewById(R.id.playerResults);
            ArrayAdapter<String> arr;
            String[] test = new String[] {player.getFirstname() + " " + player.getLastname() + " " + player.getFedRank()};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                    android.R.layout.simple_list_item_1, test);
            l.setAdapter(adapter);
        }
    }
}
