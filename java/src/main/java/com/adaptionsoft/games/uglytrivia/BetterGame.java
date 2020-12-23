package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class BetterGame implements IGame {
    List<Player> players = new ArrayList<>();
    int[] purses  = new int[6];
    boolean[] inPenaltyBox  = new boolean[6];

    LinkedList<String> popQuestions = new LinkedList<>();
    LinkedList<String> scienceQuestions = new LinkedList<>();
    LinkedList<String> sportsQuestions = new LinkedList<>();
    LinkedList<String> rockQuestions = new LinkedList<>();

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public  BetterGame(){
        IntStream.range(0, 50).forEach(i -> {
            popQuestions.addLast("Pop Question " + i);
            scienceQuestions.addLast("Science Question " + i);
            sportsQuestions.addLast("Sports Question " + i);
            rockQuestions.addLast("Rock Question " + i);
        });
    }

    static class Player{
        private final String name;
        private int positionOnBoard;

        Player(String name) {
            this.name = name;
            this.positionOnBoard = 0;
        }

        public String getName() {
            return name;
        }


        public int getPosition() {
            return positionOnBoard;
        }

        public void move(int roll){
            positionOnBoard = positionOnBoard + roll;
            if (positionOnBoard >= 12) {
                positionOnBoard = positionOnBoard - 12;
            }

        }
    }

    @Override
    public boolean add(String playerName) {

        players.add(new Player(playerName));
        purses[players.size()] = 0;
        inPenaltyBox[players.size()] = false;

        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
        return true;
    }

    @Override
    public void roll(int roll) {
        System.out.println(currentPlayer().getName() + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (inPenaltyBox[currentPlayer]) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;

                System.out.println(currentPlayer().getName() + " is getting out of the penalty box");

                moveCurrentPlayer(roll);

                System.out.println(currentPlayer().getName()
                        + "'s new location is "
                        + getPosition());
                System.out.println("The category is " + currentCategory());
                askQuestion();
            } else {
                System.out.println(currentPlayer().getName() + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }

        } else {

            moveCurrentPlayer(roll);

            System.out.println(currentPlayer().getName()
                    + "'s new location is "
                    + getPosition());
            System.out.println("The category is " + currentCategory());
            askQuestion();
        }

    }

    private void moveCurrentPlayer(int roll) {
        currentPlayer().move(roll);
    }

    private Player currentPlayer() {
        return players.get(currentPlayer);
    }

    private void askQuestion() {
        if (currentCategory().equals("Pop"))
            System.out.println(popQuestions.removeFirst());
        if (currentCategory().equals("Science"))
            System.out.println(scienceQuestions.removeFirst());
        if (currentCategory().equals("Sports"))
            System.out.println(sportsQuestions.removeFirst());
        if (currentCategory().equals("Rock"))
            System.out.println(rockQuestions.removeFirst());
    }


    private String currentCategory() {
        if (getPosition() == 0) return "Pop";
        if (getPosition() == 4) return "Pop";
        if (getPosition() == 8) return "Pop";
        if (getPosition() == 1) return "Science";
        if (getPosition() == 5) return "Science";
        if (getPosition() == 9) return "Science";
        if (getPosition() == 2) return "Sports";
        if (getPosition() == 6) return "Sports";
        if (getPosition() == 10) return "Sports";
        return "Rock";
    }

    private int getPosition() {
        return currentPlayer().getPosition();
    }

    @Override
    public boolean wasCorrectlyAnswered() {
        if (inPenaltyBox[currentPlayer]){
            if (isGettingOutOfPenaltyBox) {
                System.out.println("Answer was correct!!!!");
                purses[currentPlayer]++;
                System.out.println(currentPlayer().getName()
                        + " now has "
                        + purses[currentPlayer]
                        + " Gold Coins.");

                boolean winner = didPlayerWin();
                currentPlayer++;
                if (currentPlayer == players.size()) currentPlayer = 0;

                return winner;
            } else {
                currentPlayer++;
                if (currentPlayer == players.size()) currentPlayer = 0;
                return true;
            }



        } else {

            System.out.println("Answer was corrent!!!!");
            purses[currentPlayer]++;
            System.out.println(currentPlayer().getName()
                    + " now has "
                    + purses[currentPlayer]
                    + " Gold Coins.");

            boolean winner = didPlayerWin();
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;

            return winner;
        }
    }

    @Override
    public boolean wrongAnswer(){
        System.out.println("Question was incorrectly answered");
        System.out.println(currentPlayer().getName()+ " was sent to the penalty box");
        inPenaltyBox[currentPlayer] = true;

        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
        return true;
    }


    private boolean didPlayerWin() {
        return !(purses[currentPlayer] == 6);
    }
}
