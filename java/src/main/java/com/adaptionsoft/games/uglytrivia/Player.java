package com.adaptionsoft.games.uglytrivia;

public class Player {
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

    public void sentToPenaltyBox() {
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
