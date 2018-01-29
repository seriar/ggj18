package com.sergii.fgjx.sb.client.world;

import com.sergii.fgjx.sb.client.rendering.Shader;
import com.sergii.fgjx.sb.client.rendering.Texture;

public class TransmissionHud implements Screen{

    protected TextItem selectWeapon;
    protected TextItem transmission;
    protected TextItem code;
    protected TextItem activation;
    public static final int FONT_SIZE = 16;
    private final int posX;
    private final int posY;
    private final int codePosX;
    private final int codePosY;
    private String codeText;

    public TransmissionHud() {
        this(-World.WIDTH/2 , -World.HEIGHT / 2 + 2 * TransmissionHud.FONT_SIZE);
    }

    public TransmissionHud(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.codePosX = -FONT_SIZE*13;
        this.codePosY = FONT_SIZE*13;
        FontTexture fontTexture = new FontTexture(1f / 16f,1f / 16f,16,16,
                new Texture("font.png"));
        transmission = new TextItem(40, 1, 1, 1, fontTexture);
        transmission.setText(" Incoming activation code transmission: ");
        selectWeapon = new TextItem(40, 2, 1, 1, fontTexture);
        selectWeapon.setText(" Select the weapon:                     " +
                             " 1:Laser   2:Plasma canon   3:Torpedoes ");
        activation = new TextItem(40, 2, 1, 1, fontTexture);
        activation.setText(  " Enter the activation code:             " +
                "> ");

        code = new TextItem(13, 1, 1, 1, fontTexture);
        codeText = World.getStaticCode();
        code.setText(codeText);

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(Shader shader) {
//        transmission.render(shader, posX, posY, FONT_SIZE);
//        selectWeapon.render(shader, posX, posY, FONT_SIZE);
//        activation.render(shader, posX, posY, FONT_SIZE);
//        code.render(shader, codePosX, codePosY, FONT_SIZE * 4);
    }

    public void renderWeapon(Shader shader) {
        selectWeapon.render(shader, posX, posY, FONT_SIZE);
    }
    public void renderAttack(Shader shader) {
        activation.render(shader, posX, posY, FONT_SIZE);
    }
    public void renderAttackTransmission(Shader shader) {
        renderCode(shader);
        transmission.render(shader, posX, posY, FONT_SIZE);
    }

    private void renderCode(Shader shader) {
        String staticCode = World.getStaticCode();
        if (!staticCode.equalsIgnoreCase(codeText)) {
            code.setText(staticCode);
        }
        code.render(shader, codePosX, codePosY, FONT_SIZE * 2);
    }

    public void renderDefence(Shader shader) {
        activation.render(shader, posX, posY, FONT_SIZE);
    }
    public void renderDefenceTransmission(Shader shader) {
        transmission.render(shader, posX, posY, FONT_SIZE);
        renderCode(shader);
    }
}
