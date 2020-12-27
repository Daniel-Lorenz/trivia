package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BetterGame implements IGame {
    private final Deck popQuestions;
    private final Deck scienceQuestions;
    private final Deck sportsQuestions;
    private final Deck rockQuestions;

    private final List<Player> players = new ArrayList<>();

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public BetterGame() {
        popQuestions = buildQuestionDeck("Pop Question ");
        scienceQuestions = buildQuestionDeck("Science Question ");
        sportsQuestions = buildQuestionDeck("Sports Question ");
        rockQuestions = buildQuestionDeck("Rock Question ");
    }

    private Deck buildQuestionDeck(String commonPart) {
        return Deck.DeckBuilder.aDeck()
                .withQuestions(IntStream.range(0, 50)
                        .mapToObj((i) -> commonPart + i)
                        .collect(Collectors.toList()))
                .build();
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
        displayCurrentPlayerName();
        displayRolledValue(roll);

        if (currentPlayer().isInPenaltyBox()) {
            if (roll % 2 == 0) {
                playerIsNotGettingOutOfPenaltyBox();
                return;
            } else {
                playerIsGettingOutOfPenaltyBox();
            }
        }

        currentPlayer().move(roll);
        displayPosition();
        displayCurrentCategory();
        askQuestion();

    }

    private void playerIsGettingOutOfPenaltyBox() {
        isGettingOutOfPenaltyBox = true;
        System.out.println(currentPlayer().getName() + " is getting out of the penalty box");
    }

    private void playerIsNotGettingOutOfPenaltyBox() {
        System.out.println(currentPlayer().getName() + " is not getting out of the penalty box");
        isGettingOutOfPenaltyBox = false;
    }

    private void displayRolledValue(int roll) {
        System.out.println("They have rolled a " + roll);
    }

    private void displayCurrentPlayerName() {
        System.out.println(currentPlayer().getName() + " is the current player");
    }

    private void displayCurrentCategory() {
        System.out.println("The category is " + currentCategory().getType());
    }

    private void displayPosition() {
        System.out.println(currentPlayer().getName()
                + "'s new location is "
                + getPosition());
    }

    private Player currentPlayer() {
        return players.get(currentPlayer);
    }

    private void askQuestion() {
        System.out.println(drawQuestionOfCurrentCategory());
    }

    private String drawQuestionOfCurrentCategory() {
        return switch (currentCategory()) {
            case ROCK -> rockQuestions.drawOne();
            case SPORTS -> sportsQuestions.drawOne();
            case SCIENCE -> scienceQuestions.drawOne();
            case POP -> popQuestions.drawOne();
        };
    }

    private QuestionTypes currentCategory() {
        if (getPosition() % 4 == 0) return QuestionTypes.POP;
        if (getPosition() % 4 == 1) return QuestionTypes.SCIENCE;
        if (getPosition() % 4 == 2) return QuestionTypes.SPORTS;
        return QuestionTypes.ROCK;
    }

    private int getPosition() {
        return currentPlayer().getPosition();
    }

    @Override
    public boolean wasCorrectlyAnswered() {
        if(playerIsNotAllowedToWin()) {
            nextPlayersTurn();
            return true;
        }

        displaySuccessMsg();
        currentPlayer().scoreOnePoint();
        displayPoints();

        boolean playerHasNotWon = hasCurrentPlayerNotWon();
        nextPlayersTurn();
        return playerHasNotWon;
    }

    private boolean playerIsNotAllowedToWin() {
        return currentPlayer().isInPenaltyBox() && !isGettingOutOfPenaltyBox;
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

    private void nextPlayersTurn() {
        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
    }

    @Override
    public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.println(currentPlayer().getName() + " was sent to the penalty box");

        currentPlayer().sentToPenaltyBox();

        nextPlayersTurn();
        return true;
    }

    private boolean hasCurrentPlayerNotWon() {
        return !(currentPlayer().getPoints() == 6);
    }

    enum QuestionTypes {
        POP("Pop"),
        SCIENCE("Science"),
        SPORTS("Sports"),
        ROCK("Rock");

        private final String type;

        QuestionTypes(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

}
