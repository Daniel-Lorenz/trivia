package com.adaptionsoft.games.uglytrivia;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {

    @Test
    void shouldRunOldAndNewGameWithSameOutput() {
        IntStream.range(0, 10000).forEach(seed -> {
            String expected = runGameAndCaptureOutput(new Random(seed), new Game());
            String actual = runGameAndCaptureOutput(new Random(seed), new BetterGame());
            assertEquals(expected, actual);
        });
    }


    String runGameAndCaptureOutput(Random rand, IGame aGame) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (PrintStream inmemory = new PrintStream(baos)) {
            System.setOut(inmemory);

            boolean notAWinner;

            aGame.add("Chet");
            aGame.add("Pat");
            aGame.add("Sue");

            do {

                aGame.roll(rand.nextInt(5) + 1);

                if (rand.nextInt(9) == 7) {
                    notAWinner = aGame.wrongAnswer();
                } else {
                    notAWinner = aGame.wasCorrectlyAnswered();
                }

            } while (notAWinner);
        }
        return new String(baos.toByteArray());
    }

}