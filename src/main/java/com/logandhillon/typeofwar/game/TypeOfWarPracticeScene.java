package com.logandhillon.typeofwar.game;

import com.logandhillon.typeofwar.TypeOfWar;
import com.logandhillon.typeofwar.entity.EndHeaderEntity;
import com.logandhillon.typeofwar.entity.EndResultEntity;
import com.logandhillon.typeofwar.entity.PlayerObject;
import javafx.scene.paint.Color;

import java.util.List;

public class TypeOfWarPracticeScene extends TypeOfWarScene{
    private final int computerWPM;
    private final float secondsPerCharacter;
    private float updateTimer;
    private float elapsedSeconds;
    private float wordsCounter;
    public TypeOfWarPracticeScene(TypeOfWar game, int computerWPM) {
        super(game, List.of(new PlayerObject("Player1", Color.CYAN)), List.of(new PlayerObject("Computer", Color.GREY))); //TODO: Connect this to main menu values
        this.computerWPM = computerWPM;
        secondsPerCharacter = 60f / (this.computerWPM * 5);

    }

    @Override
    protected void onUpdate(float dt) {
        super.onUpdate(dt);
        elapsedSeconds += dt;
        updateTimer += dt;
        if(updateTimer >= secondsPerCharacter){
             moveRope(false);
             updateTimer = 0f;
             wordsCounter += 0.2f;
        }
    }

    public void endGame(boolean won){
        EndResultEntity[] playerResults = new EndResultEntity[1];
        playerResults[0] = stats.toEndResultEntity(new PlayerObject("Player1", Color.CYAN)); //TODO #6: Change this to work with multiplayer

        EndResultEntity[] computerResults = new EndResultEntity[1];
        computerResults[0] = new EndResultEntity(this.computerWPM,-1, Math.round(wordsCounter), (new PlayerObject("COMPUTER", Color.GREY)));

        this.game.setScene(new EndGameScene(game, playerResults, computerResults, new EndHeaderEntity(won)));
    }
}
