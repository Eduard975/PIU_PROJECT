package map;

import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import math.Matrix4f;
import math.Vector3f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class Level {

    private VertexArray background;
    private Texture backgroundTexture;

    public static final int xBounds = 250;
    public static final int yBounds = 250;
    private int xScroll = 0;
    private int yScroll = 0;

    private int[][] map;



    long windowId = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();

//    private int map;


    public Level() {
            float[] vertices = new float[] {
                    -16.0f, -9.0f, 0.0f,
                    -16.0f,  9.0f, 0.0f,
                      0.0f,  9.0f, 0.0f,
                      0.0f, -9.0f, 0.0f,
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
//        if (glfwGetKey(windowId, GLFW_KEY_UP) == GLFW_PRESS) {
//            yScroll++;
//        }
//        if (glfwGetKey(windowId, GLFW_KEY_DOWN) == GLFW_PRESS) {
//            yScroll--;
//        }
//        if (glfwGetKey(windowId, GLFW_KEY_LEFT) == GLFW_PRESS) {
//            xScroll--;
//        }
//        if (glfwGetKey(windowId, GLFW_KEY_RIGHT) == GLFW_PRESS) {
//            xScroll++;
//        }


    }

    public void render() {
        backgroundTexture.bind();
        Shader.BACKGROUND.enable();
        background.bind();

        int rows = map.length;
        int cols = map[0].length;

        int tileSize = 16;

        int xStart = Math.max(0, (-xScroll - xBounds) / tileSize);
        int xEnd = Math.min(cols - 1, (xBounds - xScroll) / tileSize);
        int yStart = Math.max(0, (-yScroll - yBounds) / tileSize);
        int yEnd = Math.min(rows - 1, (yBounds - yScroll) / tileSize);

        for (int y = yStart; y <= yEnd; y++) {
            for (int x = xStart; x <= xEnd; x++) {
                backgroundTexture.bind();
                Shader.BACKGROUND.setUniformMat4f("vw_matrix",
                        Matrix4f.translate(new Vector3f(x * tileSize + xScroll, y * tileSize + yScroll, 0.0f))
                );
                background.draw();
                backgroundTexture.unbind();
            }
        }

//        background.render();
        Shader.BACKGROUND.disable();
        backgroundTexture.unbind();
    }
}
