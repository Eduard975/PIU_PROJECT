package main;

import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import math.Matrix4f;
import math.Vector3f;
import player.AbilityBase;
import player.AbilityManager;
import player.Player;

import java.util.List;

public class HUD {
    private VertexArray hpBar, mpBar, inventoryBar, abilityIcon;

    private Player player;

    private AbilityManager abilityManager;


    private Texture inventoryTexture;

    private float[] hpVertices, mpVertices, invVertices, iconVertices;
    private List<AbilityBase> abilities;
    float invWidth = 316 * 0.75f;
    float invHeight = 84 * 0.75f;
    float iconSize = 64 * 0.75f;
    float resourceBarHeight = 15f;
    float resourceBarWidth = 200f;

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


    public HUD(Player player, AbilityManager abilityManager) {
        this.player = player;
        this.abilityManager = abilityManager;

        abilities = abilityManager.getAbilities();

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

        iconVertices = new float[]{
                0.0f, 0.0f, 0.0f,
                0.0f, iconSize, 0.0f,
                iconSize, iconSize, 0.0f,
                iconSize, 0.0f, 0.0f
        };


        hpBar = new VertexArray(hpVertices, indices, tcs);
        mpBar = new VertexArray(mpVertices, indices, tcs);

        abilityIcon = new VertexArray(iconVertices, indices, tcs);

        inventoryBar = new VertexArray(invVertices, indices, tcs);
        inventoryTexture = new Texture("src/main/resources/inventory.png");
    }

    private void drawHpBar() {
        Shader.HP.enable();
        float hpRatio = player.hp / player.maxHp;

        hpVertices[6] = hpVertices[9] = resourceBarWidth * hpRatio;
        hpBar.updateVertices(hpVertices);

        Shader.HP.setUniformMat4f("ml_matrix",
                Matrix4f.translate(new Vector3f(-Camera.WIDTH + 15,
                        Camera.HEIGHT - resourceBarHeight - 10f, 0.9f)));
        hpBar.render();
        Shader.HP.disable();
    }

    public void drawMpBar() {
        Shader.MP.enable();
        float mpRatio = (float) player.mp / player.maxMp;
        mpVertices[6] = mpVertices[9] = resourceBarWidth * mpRatio;
        mpBar.updateVertices(mpVertices);

        Shader.MP.setUniformMat4f("ml_matrix",
                Matrix4f.translate(new Vector3f(-Camera.WIDTH + 15,
                        Camera.HEIGHT - resourceBarHeight * 2 - 20f, 0.9f)));
        mpBar.render();
        Shader.MP.disable();
    }

    public void drawIcon(List<AbilityBase> abilities) {
        int offset = 0;
        Shader.ICON.enable();

        for (AbilityBase ability : abilities) {
            Texture icon = ability.getIcon();
            Vector3f iconTranslate = new Vector3f(
                    Camera.WIDTH - invWidth + 12 * 0.75f + offset - 15,
                    -Camera.HEIGHT + invHeight - iconSize + 10 * 0.75f, 0.9f
            );

            offset += iconSize + 12 * 0.75;

            Shader.ICON.setUniformMat4f("ml_matrix",
                    Matrix4f.translate(iconTranslate));
            
            boolean isUsable = ability.canUse(player.mp);
            Shader.ICON.setUniform1i("isUsable", isUsable ? 1 : 0);

            icon.bind();

            abilityIcon.render();

        }
        Shader.ICON.disable();
    }

    public void render() {
        drawHpBar();
        drawMpBar();
        Shader.INVENTORY.enable();
        Vector3f invTranslate = new Vector3f(
                Camera.WIDTH - invWidth - 15,
                -Camera.HEIGHT + 15, 0.9f
        );

        Shader.INVENTORY.setUniformMat4f("ml_matrix", Matrix4f.translate(invTranslate));
        inventoryTexture.bind();
        inventoryBar.render();

        Shader.INVENTORY.disable();

        drawIcon(abilities);
    }

}
