package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.entity.EndResultEntity;
import com.logandhillon.typeofwar.entity.PlayerObject;
import com.logandhillon.typeofwar.resource.WordGen;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.List;

public class TypeOfWarPracticeScene extends TypeOfWarScene {

    private final int computerWPM;
    private final float secondsPerCharacter;

    private float updateTimer;
    private float wordsCounter;

    private volatile boolean isEndScreenQueued = false;

    public TypeOfWarPracticeScene(TypeOfWar game, int computerWPM) throws IOException {
        super(game, List.of(new PlayerObject(System.getProperty("user.name"), Color.CYAN)),
              // TODO: Connect this to main menu values
              List.of(new PlayerObject("Computer", Color.GREY)),
              WordGen.generateSentence(10000), 1);
        this.computerWPM = computerWPM;
        secondsPerCharacter = 60f / (this.computerWPM * 5);
    }

    @Override
    protected void onUpdate(float dt) {
        try {
            super.onUpdate(dt);
        } catch (ConcurrentModificationException ignored) {
        }

        updateTimer += dt;
        if (updateTimer >= secondsPerCharacter) {
            moveRope(false);
            updateTimer = 0f;
            wordsCounter += 0.2f;
        }
    }

    /**
     * In practice mode, this just moves the rope left; there are no other players to account key-presses for.
     */
    @Override
    public void sendCorrectKeyPress() {
        if (!isCountdownOver) return;
        moveRope(true);
    }

    @Override
    public void moveRope(boolean team1) {
        if (!isCountdownOver) return;
        super.moveRope(team1);
    }

    /**
     * In practice mode, this does not act as a "signal," rather immediately shows the {@link EndGameScene}.
     *
     * @param winningTeam the team# of the winner
     */
    @Override
    public void signalGameEnd(int winningTeam) {
        if (isEndScreenQueued) return; // only run ONCE
        this.game.setScene(new EndGameScene(
                game,
                List.of(stats.toEndResultEntity(new PlayerObject("Player1", Color.CYAN))),
                List.of(new EndResultEntity(
                        this.computerWPM,
                        -1,
                        Math.round(wordsCounter),
                        new PlayerObject("COMPUTER", Color.GREY))),
                winningTeam == 1)); // since player is always team 1, them winning is if team 1 won

        isEndScreenQueued = true;
    }
}
