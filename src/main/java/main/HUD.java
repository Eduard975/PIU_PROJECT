package main;

import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import math.Matrix4f;
import math.Vector3f;
import player.Player;

public class HUD {
    private VertexArray hpBar, mpBar, inventoryBar;

    private Player player;

    private Texture inventoryTexture;

    private float[] hpVertices, mpVertices, invVertices;

    float invWidth = 316 * 0.75f;
    float invHeight = 84 * 0.75f;

    float resourceBarHeight = 15f;
    float resourceBarWidth = 200f;


    public HUD(Player player) {
        this.player = player;

        byte[] indices = new byte[]{
                0, 1, 2,
                2, 3, 0
        };

        float[] tcs = new float[]{
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        hpVertices = new float[]{
                0.0f, 0.0f, 0.0f,
                0.0f, resourceBarHeight, 0.0f,
                resourceBarWidth, resourceBarHeight, 0.0f,
                resourceBarWidth, 0.0f, 0.0f
        };

        mpVertices = new float[]{
                0.0f, 0.0f, 0.0f,
                0.0f, resourceBarHeight, 0.0f,
                resourceBarWidth, resourceBarHeight, 0.0f,
                resourceBarWidth, 0.0f, 0.0f
        };

        invVertices = new float[]{
                0.0f, 0.0f, 0.0f,
                0.0f, invHeight, 0.0f,
                invWidth, invHeight, 0.0f,
                invWidth, 0.0f, 0.0f
        };


        hpBar = new VertexArray(hpVertices, indices, tcs);
        mpBar = new VertexArray(mpVertices, indices, tcs);

        inventoryBar = new VertexArray(invVertices, indices, tcs);
        inventoryTexture = new Texture("src/main/resources/inventory.png");
    }

    public void render() {
        Shader.HP.enable();
        float hpRatio = player.hp / player.maxHp;

        hpVertices[6] = hpVertices[9] = resourceBarWidth * hpRatio;
        hpBar.updateVertices(hpVertices);

        Shader.HP.setUniformMat4f("ml_matrix",
                Matrix4f.translate(new Vector3f(-Camera.WIDTH + 15,
                        Camera.HEIGHT - resourceBarHeight - 10f, 0.9f)));
        hpBar.render();
        Shader.HP.disable();

        Shader.MP.enable();
        float mpRatio = (float) player.mp / player.maxMp;
        mpVertices[6] = mpVertices[9] = resourceBarWidth * mpRatio;
        mpBar.updateVertices(mpVertices);

        Shader.MP.setUniformMat4f("ml_matrix",
                Matrix4f.translate(new Vector3f(-Camera.WIDTH + 15,
                        Camera.HEIGHT - resourceBarHeight * 2 - 20f, 0.9f)));
        mpBar.render();
        Shader.MP.disable();

        Shader.INVENTORY.enable();
        Vector3f invTranslate = new Vector3f(
                Camera.WIDTH - invWidth - 15,
                -Camera.HEIGHT + 15, 0.9f
        );

        Shader.INVENTORY.setUniformMat4f("ml_matrix", Matrix4f.translate(invTranslate));
        inventoryTexture.bind();
        inventoryBar.render();

        Shader.INVENTORY.disable();
    }

}
