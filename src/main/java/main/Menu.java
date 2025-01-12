package main;

import graphic.FontRenderer;
import graphic.Window;

public class Menu {
    private FontRenderer fontRenderer;
    private boolean gameOver = false;
    private static final float TITLE_SCALE = 3.0f;
    private static final float OPTION_SCALE = 2.0f;

    public Menu(FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
        this.gameOver = false;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void render() {
        float stringWidthTitle = fontRenderer.getStringWidth("NECROLORD") * TITLE_SCALE;
        float stringWidthPlay = fontRenderer.getStringWidth("PLAY") * OPTION_SCALE;
        float stringWidthExit = fontRenderer.getStringWidth("EXIT") * OPTION_SCALE;
        float stringWidthGameOver = fontRenderer.getStringWidth("GAME OVER") * TITLE_SCALE;
        float stringWidthRestart = fontRenderer.getStringWidth("PRESS ENTER TO RESTART") * OPTION_SCALE;

        if(gameOver) {
            fontRenderer.drawString(
                    "GAME OVER",
                    0 - stringWidthGameOver / 2,
                    Camera.HEIGHT - 200,
                    TITLE_SCALE
            );

            fontRenderer.drawString(
                    "PRESS ENTER TO RESTART",
                    0 - stringWidthRestart / 2,
                    Camera.HEIGHT - 250,
                    OPTION_SCALE
            );
        } else {
            fontRenderer.drawString("NECROLORD", 0 - stringWidthTitle/2, Camera.HEIGHT - 200, TITLE_SCALE);
            fontRenderer.drawString("PLAY", 0 - stringWidthPlay/2, Camera.HEIGHT - 250, OPTION_SCALE);
            fontRenderer.drawString("EXIT", 0 - stringWidthExit/2, Camera.HEIGHT - 300, OPTION_SCALE);

        }



    }
}