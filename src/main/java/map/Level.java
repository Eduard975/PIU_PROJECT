package map;

import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import graphic.Window;
import math.Matrix4f;
import math.Vector3f;
import org.lwjgl.glfw.GLFW;
import player.Player;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class Level {

    private VertexArray background;
    private Texture backgroundTexture;


    public static final int  TILE_SIZE = Window.WIDTH/8;
    public static final int xBounds = 8 * TILE_SIZE;
    public static final int yBounds = 8 * TILE_SIZE;
    private float xScroll = 0;
    private float yScroll = 0;

    private int[][] map;

    private Player player;

//    long windowId = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();

//    private int map;


    public Level(Player player) {
            this.player = player;

            float[] vertices = new float[] {
                    -TILE_SIZE, -TILE_SIZE, 0.0f,
                    -TILE_SIZE,  TILE_SIZE, 0.0f,
                      0.0f,  TILE_SIZE, 0.0f,
                      0.0f, -TILE_SIZE, 0.0f,
            };

            byte[] indices = new byte[] {
                    0, 1, 2,
                    2, 3, 0
            };

            float[] textures = new float[] {
                    0, 1,
                    0, 0,
                    1, 0,
                    1, 1
            };

            background = new VertexArray(vertices, indices, textures);
            backgroundTexture = new Texture("src/main/resources/bg.jpeg");

            map = new int[][]{
                    {0, 1, 0, 0, 2, 2, 0, 1},
                    {0, 0, 1, 0, 2, 0, 1, 0},
                    {2, 2, 1, 1, 0, 0, 1, 1},
                    {0, 1, 0, 2, 2, 0, 0, 0},
                    {0, 0, 0, 1, 1, 2, 2, 0},
                    {1, 0, 2, 2, 1, 0, 1, 1},
                    {0, 2, 1, 0, 0, 1, 2, 0},
                    {1, 1, 0, 0, 2, 1, 0, 0},
            };
    }




    public void update() {
//        xScroll = player.getX() - xBounds / 2.0f;
//        yScroll = player.getY() - yBounds / 2.0f;
//
//        xScroll = Math.max(0, Math.min(xScroll, map[0].length * TILE_SIZE - xBounds));
//        yScroll = Math.max(0, Math.min(yScroll, map.length * TILE_SIZE - yBounds));


    }

    public void render() {
        backgroundTexture.bind();
        Shader.BACKGROUND.enable();
        background.bind();

        int rows = map.length;
        int cols = map[0].length;


//        int xStart = (int) Math.max(0, (-xScroll - xBounds) / tileSize);
//        int xEnd = (int) Math.min(cols - 1, (xBounds - xScroll) / tileSize);
//        int yStart = (int) Math.max(0, (-yScroll - yBounds) / tileSize);
//        int yEnd = (int) Math.min(rows - 1, (yBounds - yScroll) / tileSize);
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                Shader.BACKGROUND.setUniformMat4f("vw_matrix",
                        Matrix4f.translate(new Vector3f(x * TILE_SIZE + xScroll, y * TILE_SIZE + yScroll, 0.0f)).multiply(Matrix4f.rotate(xScroll))
                );
                background.draw();
            }
        }

//        background.render();
        Shader.BACKGROUND.disable();
        backgroundTexture.unbind();
    }
}
