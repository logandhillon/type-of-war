package com.logandhillon.typeofwar.networking;

import com.logandhillon.typeofwar.entity.EndResultEntity;
import com.logandhillon.typeofwar.entity.PlayerObject;
import com.logandhillon.typeofwar.networking.proto.EndGameProto;
import javafx.scene.paint.Color;

/**
 * @author Logan Dhillon
 */
public class NetUtils {
    public static EndResultEntity endStatProtoToEntity(EndGameProto.PlayerStats p) {
        return new EndResultEntity(
                p.getWpm(), p.getAccuracy(), p.getWords(),
                new PlayerObject(p.getPlayerName(), Color.color(p.getR(), p.getG(), p.getB())));
    }
}
