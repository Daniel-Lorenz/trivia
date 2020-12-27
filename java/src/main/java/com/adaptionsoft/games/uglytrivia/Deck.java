package com.adaptionsoft.games.uglytrivia;

import io.vavr.Tuple2;
import io.vavr.collection.Queue;

import java.util.List;

public class Deck {

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
