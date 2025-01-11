package map;

import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import graphic.Window;
import math.Matrix4f;
import math.Vector3f;

public class Level {

    private final VertexArray background;
    private final Texture backgroundTexture;

    public static final int TILE_SIZE = Window.WIDTH / 8;
    public static final int xBounds = Window.WIDTH;
    public static final int yBounds = Window.HEIGHT;
    private float xScroll = 0;
    private float yScroll = 0;


    public Level() {

        float[] vertices = new float[]{
                xBounds, -yBounds, 0.0f,
                xBounds, yBounds, 0.0f,
                -xBounds, yBounds, 0.0f,
                -xBounds, -yBounds, 0.0f,
        };

        byte[] indices = new byte[]{
                0, 1, 2,
                2, 3, 0
        };

        float[] textures = new float[]{
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        background = new VertexArray(vertices, indices, textures);
        backgroundTexture = new Texture("src/main/resources/images/bg.jpeg");
    }

    public void update() {
    }

    public void render() {
        backgroundTexture.bind();
        Shader.BACKGROUND.enable();
        background.bind();

        Shader.BACKGROUND.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(xScroll, yScroll, 0.0f)));

        background.draw();

        Shader.BACKGROUND.disable();
        backgroundTexture.unbind();
    }
}
