package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class BetterGame implements IGame {
    List<Player> players = new ArrayList<>();

    LinkedList<String> popQuestions = new LinkedList<>();
    LinkedList<String> scienceQuestions = new LinkedList<>();
    LinkedList<String> sportsQuestions = new LinkedList<>();
    LinkedList<String> rockQuestions = new LinkedList<>();

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public BetterGame() {
        IntStream.range(0, 50).forEach(i -> {
            popQuestions.addLast("Pop Question " + i);
            scienceQuestions.addLast("Science Question " + i);
            sportsQuestions.addLast("Sports Question " + i);
            rockQuestions.addLast("Rock Question " + i);
        });
    }

    @Override
    public boolean add(String playerName) {

        players.add(new Player(playerName));

        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
        return true;
    }

    @Override
    public void roll(int roll) {
        System.out.println(currentPlayer().getName() + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (currentPlayer().isInPenaltyBox()) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;

                System.out.println(currentPlayer().getName() + " is getting out of the penalty box");

                moveCurrentPlayer(roll);

                displayPosition();
                displayCurrentCategory();
                askQuestion();
            } else {
                System.out.println(currentPlayer().getName() + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }

        } else {

            moveCurrentPlayer(roll);

            displayPosition();
            displayCurrentCategory();
            askQuestion();
        }

    }

    private void displayCurrentCategory() {
        System.out.println("The category is " + currentCategory());
    }

    private void displayPosition() {
        System.out.println(currentPlayer().getName()
                + "'s new location is "
                + getPosition());
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
        if (currentPlayer().isInPenaltyBox()) {
            if (isGettingOutOfPenaltyBox) {
                displaySuccessMsg();
                scorePoint();
                displayPoints();

                boolean winner = didPlayerWin();
                nextPlayersTurn();

                return winner;
            } else {
                nextPlayersTurn();
                return true;
            }


        } else {

            displaySuccessMsg();
            scorePoint();
            displayPoints();

            boolean winner = didPlayerWin();
            nextPlayersTurn();

            return winner;
        }
    }

    private void displaySuccessMsg() {
        System.out.println("Answer was correct!!!!");
    }

    private void displayPoints() {
        System.out.println(currentPlayer().getName()
                + " now has "
                + currentPlayer().getPoints()
                + " Gold Coins.");
    }

    private void scorePoint() {
        currentPlayer().scoreOnePoint();
    }

    private void nextPlayersTurn() {
        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
    }

    @Override
    public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.println(currentPlayer().getName() + " was sent to the penalty box");
        sentToPenaltyBox();

        nextPlayersTurn();
        return true;
    }

    private void sentToPenaltyBox() {
        currentPlayer().sentToPenaltyBox();
    }

    private boolean didPlayerWin() {
        return !(currentPlayer().getPoints() == 6);
    }

    static class Player {
        private final String name;
        private int positionOnBoard;
        private int points;
        private boolean inPenaltyBox;

        Player(String name) {
            this.name = name;
            this.positionOnBoard = 0;
            this.points = 0;
            this.inPenaltyBox = false;
        }

        public void sentToPenaltyBox(){
            this.inPenaltyBox = true;
        }

        public boolean isInPenaltyBox() {
            return inPenaltyBox;
        }

        public String getName() {
            return name;
        }

        public int getPoints() {
            return points;
        }

        public int getPosition() {
            return positionOnBoard;
        }

        public void move(int roll) {
            positionOnBoard = positionOnBoard + roll;
            if (positionOnBoard >= 12) {
                positionOnBoard = positionOnBoard - 12;
            }
        }

        public void scoreOnePoint() {
            this.points++;
        }
    }
}
