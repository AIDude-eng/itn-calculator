package com.example.itnrechner;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DoublesLineupCalculator {

    public static class Player {
        private final String name;
        private final double itn;

        public Player(String name, double itn) {
            this.name = name;
            this.itn = itn;
        }

        public String getName() {
            return name;
        }

        public double getItn() {
            return itn;
        }

        @Override
        public String toString() {
            return name + " - " + itn;
        }
    }

    public static class PlayerTeam implements Comparable<PlayerTeam> {
        private final Player player1;
        private final Player player2;

        public PlayerTeam(Player player1, Player player2) {
            this.player1 = player1;
            this.player2 = player2;
        }

        public double getTeamITN() {
            return player1.getItn() + player2.getItn();
        }

        public Player getPlayer1() {
            return player1;
        }

        public Player getPlayer2() {
            return player2;
        }

        @Override
        public String toString() {
            return "[P1: " + player1.toString() + ", P2: " + player2.toString() + ", Gesamt ITN: " + getTeamITN() +  "]";
        }

        @Override
        public int compareTo(PlayerTeam p) {
            return Double.compare(p.getTeamITN(), this.getTeamITN());
        }
    }

    public static class DoubleLineup {
        private List<PlayerTeam> lineup = new ArrayList<>();

        public void add(PlayerTeam playerTeam) {
            lineup.add(playerTeam);
        }

        public void sortTeams() {
            Collections.sort(lineup);
        }

        public List<PlayerTeam> getLineup() {
            return lineup;
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            for (PlayerTeam team : lineup) {
                b.append(team.toString());
                b.append('\n');
            }
            return b.toString();
        }
    }

    private static class DoubleCombinations {

        public static List<List<List<Integer>>> generateLineups(int[] players) {
            List<List<List<Integer>>> lineups = new ArrayList<>();

            int numPlayers = players.length;
            int numDoubles = 3;
            int numPerDouble = 2;

            // Generate all possible combinations of doubles
            List<List<Integer>> doublesCombinations = new ArrayList<>();
            generateCombinations(players, numPerDouble, 0, new ArrayList<>(), doublesCombinations);

            // Generate all possible lineups using the doubles combinations
            generateLineupsRecursive(doublesCombinations, numDoubles, 0, new ArrayList<>(), lineups);

            return lineups;
        }

        private static void generateCombinations(int[] players, int k, int start, List<Integer> current, List<List<Integer>> combinations) {
            if (k == 0) {
                combinations.add(new ArrayList<>(current));
                return;
            }

            for (int i = start; i <= players.length - k; i++) {
                current.add(players[i]);
                generateCombinations(players, k - 1, i + 1, current, combinations);
                current.remove(current.size() - 1);
            }
        }

        private static void generateLineupsRecursive(List<List<Integer>> doublesCombinations, int numDoubles, int start, List<List<Integer>> current, List<List<List<Integer>>> lineups) {
            if (numDoubles == 0) {
                lineups.add(new ArrayList<>(current));
                return;
            }

            for (int i = start; i <= doublesCombinations.size() - numDoubles; i++) {
                List<Integer> doubles = doublesCombinations.get(i);

                if (!hasDuplicatePlayers(current, doubles)) {
                    current.add(doubles);
                    generateLineupsRecursive(doublesCombinations, numDoubles - 1, i + 1, current, lineups);
                    current.remove(current.size() - 1);
                }
            }
        }

        private static boolean hasDuplicatePlayers(List<List<Integer>> lineups, List<Integer> doubles) {
            for (List<Integer> existingDoubles : lineups) {
                for (Integer player : doubles) {
                    if (existingDoubles.contains(player)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private final List<DoubleLineup> lineups = new ArrayList<>();
    private final List<List<List<Integer>>> lineupCombis = DoubleCombinations.generateLineups(new int[]{0, 1, 2, 3, 4, 5});
    private final Player[] players;

    public DoublesLineupCalculator(Player... players) throws IllegalArgumentException {
        if (players.length != 6) {
            throw new IllegalArgumentException("Number of players must be 6");
        }
        this.players = players;
    }

    public void generateLineup() {
        for (List<List<Integer>> lineupCombi : lineupCombis) {
            DoubleLineup lineup = new DoubleLineup();
            for (List<Integer> doublesCombi : lineupCombi) {
                // get players using integers
                Player player1 = players[doublesCombi.get(0)];
                Player player2 = players[doublesCombi.get(1)];
                lineup.add(new PlayerTeam(player1, player2));
            }
            lineups.add(lineup);
        }
    }

    public List<DoubleLineup> getLineups() {
        return lineups;
    }
}
