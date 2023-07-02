package com.example.itnrechner;

import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Arrays;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

import com.google.gson.Gson;

/**
 * Class object representing json returned by oötv api player response
 * Example url: https://ooetv.oetv.at/?oetvappapi=1&apikey=asHwzb75fybdUz96HSfC3kvNvgavT69z&method=nu-players&lastname=Engertsberger
 */
public class PlayerSearchData {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static class PlayerName {
        private Optional<String> lastname = Optional.empty();
        private Optional<String> firstname = Optional.empty();
        private Optional<String> license = Optional.empty();

        public PlayerName(String lastname, String firstname) {
            this.lastname = Optional.of(lastname);
            this.firstname = Optional.of(firstname);
        }

        public PlayerName(String license) {
            this.license = Optional.of(license);
        }

        public Optional<String> getFirstname() {
            return firstname;
        }

        public Optional<String> getLastname() {
            return lastname;
        }

        public Optional<String> getLicense() {
            return license;
        }
    }

    public static class Data {
        private Data() {

        }

        private long totalResults;
        private long firstResult;
        private long maxResults;
        private Player[] players;

        public long getTotalResults() {
            return totalResults;
        }

        public long getFirstResult() {
            return firstResult;
        }

        public long getMaxResults() {
            return maxResults;
        }

        public Player[] getPlayers() {
            return players;
        }
    }

    public static class Player {
        public String getPlayerId() {
            return playerId;
        }

        public String getLicenceNr() {
            return licenceNr;
        }

        public String getFirstname() {
            return firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public String getNationality() {
            return nationality;
        }

        public String getClubName() {
            return clubName;
        }

        public String getFedNickname() {
            return fedNickname;
        }

        public double getFedRank() {
            return fedRank;
        }

        public String getBirthYear() {
            return birthYear;
        }

        private Player() {

        }

        private String playerId;
        private String licenceNr;
        private String firstname;
        private String lastname;
        private String nationality;
        private String clubName;
        private String fedNickname;
        private double fedRank;
        private String birthYear;

    }
    public PlayerSearchData() {

    }
    public boolean success;
    public Data data;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static PlayerSearchData searchPlayers(Optional<String> lastname, Optional<String> firstname, Optional<String> license) throws IllegalArgumentException {
        String URL = "https://ooetv.oetv.at/?oetvappapi=1&apikey=asHwzb75fybdUz96HSfC3kvNvgavT69z&method=nu-players";

        if (license.isPresent()) {
            URL += "&licence=" + license.get();
        } else {
            if (!lastname.isPresent()) {
                throw new IllegalArgumentException("lastname must be provided!");
            }
            URL += "&lastname=" + lastname.get();
            if (firstname.isPresent()) {
                URL += "&firstname=" + firstname.get();
            }
        }
        try {
            java.net.URL url = new java.net.URL(URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept-Encoding", "gzip");
            BufferedReader reader = null;

            if ("gzip".equals(con.getContentEncoding())) {
                reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(con.getInputStream())));
            } else {
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            }
            Gson gson = new Gson();
            PlayerSearchData data = gson.fromJson(reader, PlayerSearchData.class);

            // correct results for lastname as ötv does not do it correctly
            Player[] players = data.data.getPlayers();
            if (lastname.isPresent()) {
                data.data.players = Arrays.stream(players).filter(p -> p.getLastname().toLowerCase().contains(lastname.get().toLowerCase()) && p.getFedRank() >= 1).toArray(Player[]::new);
            }
            return data;

        } catch (MalformedURLException ex) {
            System.err.println("Malformed URL: " + URL);
            ex.printStackTrace();
        } catch (ProtocolException ex) {
            System.err.println("Protocol exception:");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.err.println("IO exception:");
            ex.printStackTrace();
        }
        return new PlayerSearchData();
    }

}

