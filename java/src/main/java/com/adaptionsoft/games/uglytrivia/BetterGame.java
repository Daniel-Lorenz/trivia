package com.adaptionsoft.games.uglytrivia;

import io.vavr.Tuple2;
import io.vavr.collection.Queue;

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
        return DeckBuilder.aDeck()
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
        System.out.println(currentPlayer().getName() + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (currentPlayer().isInPenaltyBox()) {
            if (roll % 2 == 0) {
                System.out.println(currentPlayer().getName() + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
                return;
            } else {
                isGettingOutOfPenaltyBox = true;
                System.out.println(currentPlayer().getName() + " is getting out of the penalty box");
            }
        }

        currentPlayer().move(roll);

        displayPosition();
        displayCurrentCategory();
        askQuestion();

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
        if (currentCategory().equals(QuestionTypes.POP))
            System.out.println(popQuestions.drawOne());
        if (currentCategory().equals(QuestionTypes.SCIENCE))
            System.out.println(scienceQuestions.drawOne());
        if (currentCategory().equals(QuestionTypes.SPORTS))
            System.out.println(sportsQuestions.drawOne());
        if (currentCategory().equals(QuestionTypes.ROCK))
            System.out.println(rockQuestions.drawOne());
    }

    enum QuestionTypes{
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

    private QuestionTypes currentCategory() {
        if (getPosition() == 0) return QuestionTypes.POP;
        if (getPosition() == 4) return QuestionTypes.POP;
        if (getPosition() == 8) return QuestionTypes.POP;
        if (getPosition() == 1) return QuestionTypes.SCIENCE;
        if (getPosition() == 5) return QuestionTypes.SCIENCE;
        if (getPosition() == 9) return QuestionTypes.SCIENCE;
        if (getPosition() == 2) return QuestionTypes.SPORTS;
        if (getPosition() == 6) return QuestionTypes.SPORTS;
        if (getPosition() == 10) return QuestionTypes.SPORTS;
        return QuestionTypes.ROCK;
    }

    private int getPosition() {
        return currentPlayer().getPosition();
    }

    @Override
    public boolean wasCorrectlyAnswered() {
        boolean playerHasNotWon = asd();
        nextPlayersTurn();
        return playerHasNotWon;
    }

    private boolean asd() {
        if (currentPlayer().isInPenaltyBox() && !isGettingOutOfPenaltyBox) {

            return true;
        } else {

            displaySuccessMsg();
            currentPlayer().scoreOnePoint();
            displayPoints();

            return notYetWon();
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

    private boolean notYetWon() {
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

    static class Deck {

        private final Queue<String> cards;
        private Queue<String> drawPile;

        Deck(Queue<String> cards) {
            this.cards = cards;
            this.drawPile = cards;
        }

        public String drawOne() {
            Tuple2<String, Queue<String>> cardAndPile = drawPile.dequeueOption().getOrElse(cards::dequeue);
            drawPile = cardAndPile._2;
            return cardAndPile._1;
        }
    }

    static class DeckBuilder {
        private Queue<String> cards = Queue.empty();

        private DeckBuilder() {
        }

        public static DeckBuilder aDeck() {
            return new DeckBuilder();
        }

        public DeckBuilder withQuestions(List<String> strings) {
            cards = cards.appendAll(strings);
            return this;
        }

        public Deck build() {
            return new Deck(cards);
        }
    }
}
