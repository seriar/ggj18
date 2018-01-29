package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.client.rendering.Shader;
import com.sergii.fgjx.sb.client.world.battle.Ship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BattleScreen {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final int SIZE = 25;
    private final Ship [] ships = new Ship[4];
    private final TransmissionHud transmissionHud;

    public BattleScreen() {
        ships[0] = new Ship(SIZE, -123, 94, true);
        ships[1] = new Ship(SIZE, -94, -111, true);

        ships[2] = new Ship(SIZE, 102, 111, false);
        ships[3] = new Ship(SIZE, 83, -90, false);


        transmissionHud = new TransmissionHud();
    }

    public void render(Shader shader) {
        for (int i = 0; i < 4; i++) {
            ships[i].render(shader);
        }
    }

    public void renderWeapon(Shader shader) {
        render(shader);
        transmissionHud.renderWeapon(shader);
    }
    public void renderAttackTransmission(Shader shader) {
        logger.trace("rendering attack trans");
        render(shader);
        transmissionHud.renderAttackTransmission(shader);
    }
    public void renderAttack(Shader shader) {
        logger.trace("rendering attack ");
        render(shader);
        transmissionHud.renderAttack(shader);
    }
    public void renderDefenceTransmission(Shader shader) {
        logger.trace("rendering defence trans");

        render(shader);
        transmissionHud.renderDefenceTransmission(shader);
    }
    public void renderDefence(Shader shader) {
        logger.trace("rendering defence ");

        render(shader);
        transmissionHud.renderDefence(shader);
    }


    public void update(float delta){
        transmissionHud.update(delta);
    }




}
