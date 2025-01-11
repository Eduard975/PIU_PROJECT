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
    private final VertexArray hpBar, mpBar, skillBar, abilityIcon, xpBar;

    private final Player player;

    private AbilityManager abilityManager;
    private final List<AbilityBase> abilities;

    private final Texture inventoryTexture;

    private final float[] hpVertices, mpVertices, xpVertices, sbVertices, iconVertices;

    float scale = 0.75f;
    float sbWidth = 316 * scale;
    float sbHeight = 84 * scale;
    float iconSize = 64 * scale;
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

        xpVertices = new float[]{
                0.0f, 0.0f, 0.0f,
                0.0f, resourceBarHeight, 0.0f,
                resourceBarWidth, resourceBarHeight, 0.0f,
                resourceBarWidth, 0.0f, 0.0f
        };

        sbVertices = new float[]{
                0.0f, 0.0f, 0.0f,
                0.0f, sbHeight, 0.0f,
                sbWidth, sbHeight, 0.0f,
                sbWidth, 0.0f, 0.0f
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
        xpBar = new VertexArray(xpVertices, indices, tcs);

        skillBar = new VertexArray(sbVertices, indices, tcs);
        inventoryTexture = new Texture("src/main/resources/images/inventory.png");
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

    private void drawMpBar() {
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

    public void drawXpBar() {
        Shader.XP.enable();
        float fullRatio = 1.0f;
        float[] maxXpVertices = xpVertices.clone();
        maxXpVertices[6] = maxXpVertices[9] = resourceBarWidth * fullRatio;

        xpBar.updateVertices(maxXpVertices);

        Shader.XP.setUniformMat4f("ml_matrix",
                Matrix4f.translate(new Vector3f(-Camera.WIDTH + 15,
                        Camera.HEIGHT - resourceBarHeight * 4 - 15f, 0.9f)));
        xpBar.render();
        Shader.XP.disable();

        Shader.XP.setUniform3f("barColor", new Vector3f(0.0f, 1.0f, 0.0f));
        Shader.XP.enable();
        float xpRatio = (float) player.xp / player.nextLevelXp;
        xpVertices[6] = xpVertices[9] = resourceBarWidth * xpRatio;
        xpBar.updateVertices(xpVertices);

        Shader.XP.setUniformMat4f("ml_matrix",
                Matrix4f.translate(new Vector3f(-Camera.WIDTH + 15,
                        Camera.HEIGHT - resourceBarHeight * 4 - 15f, 0.9f)));
        xpBar.render();
        Shader.XP.disable();

        Shader.XP.setUniform3f("barColor", new Vector3f(0.5f, 0.5f, 0.5f));
    }


    private void drawIcon() {
        int offset = 0;
        Shader.ICON.enable();

        for (AbilityBase ability : abilities) {
            Texture icon = ability.getIcon();
            Vector3f iconTranslate = new Vector3f(
                    Camera.WIDTH - sbWidth + 12 * scale + offset - 15,
                    -Camera.HEIGHT + sbHeight - iconSize + 10 * scale, 0.9f
            );

            offset += iconSize + 12 * scale;

            Shader.ICON.setUniformMat4f("ml_matrix",
                    Matrix4f.translate(iconTranslate));

            boolean isUsable = ability.canUse(player.mp);
            Shader.ICON.setUniform1i("isUsable", isUsable ? 1 : 0);

            icon.bind();

            abilityIcon.render();

        }
        Shader.ICON.disable();
    }

    private void drawSkillBar() {
        Shader.SKILLS.enable();
        Vector3f invTranslate = new Vector3f(
                Camera.WIDTH - sbWidth - 15,
                -Camera.HEIGHT + 15, 0.9f
        );

        Shader.SKILLS.setUniformMat4f("ml_matrix", Matrix4f.translate(invTranslate));
        inventoryTexture.bind();
        skillBar.render();

        Shader.SKILLS.disable();
    }

    public void render() {
        drawHpBar();
        drawMpBar();
        drawSkillBar();
        drawXpBar();
        drawIcon();
    }
}
