package main;

import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import map.Level;
import math.Matrix4f;
import math.Vector3f;
import player.Player;
import player.Projectile;

public class HUD {
    private VertexArray hpBar, mpBar;

    private Player player;

    private float[] hpVertices, mpVertices;

    public HUD(Player player) {
        this.player = player;

        byte[] indices = new byte[] {
                0, 1, 2,
                2, 3, 0
        };

        float[] tcs = new float[] {
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        hpVertices = new float[] {
                0.0f, 0.0f, 0.0f,
                0.0f, 10.0f, 0.0f,
                100.0f, 10.0f, 0.0f,
                100.0f, 0.0f, 0.0f
        };

        mpVertices = new float[] {
                0.0f, 0.0f, 0.0f,
                0.0f, 10.0f, 0.0f,
                100.0f, 10.0f, 0.0f,
                100.0f, 0.0f, 0.0f
        };

        hpBar = new VertexArray(hpVertices, indices, tcs);
        mpBar = new VertexArray(mpVertices, indices, tcs);
    }

    public void render() {
        Shader.HP.enable();
        float hpRatio = player.hp / player.maxHp;

        hpVertices[6] = hpVertices[9] = 100.0f * hpRatio;
        hpBar.updateVertices(hpVertices);

        //      TODO SCHIMBA VALORILE EMPIRICE CU O FORMULA REALA
        Shader.HP.setUniformMat4f("ml_matrix", Matrix4f.translate(new Vector3f(-600.0f, 325.0f ,0.9f)));
        hpBar.render();
        Shader.HP.disable();

        Shader.MP.enable();
        float mpRatio = (float) player.mp / player.maxMp;
        mpVertices[6] = mpVertices[9] = 100.0f * mpRatio;
        mpBar.updateVertices(mpVertices);
        //      TODO SCHIMBA VALORILE EMPIRICE CU O FORMULA REALA
        Shader.MP.setUniformMat4f("ml_matrix", Matrix4f.translate(new Vector3f(-600.0f, 300.0f ,0.9f)));
        mpBar.render();
        Shader.MP.disable();
    }

}
