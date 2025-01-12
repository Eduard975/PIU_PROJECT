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

    private final VertexArray firstBackground;
    private final Texture firstBackgroundTexture;

    public static final int TILE_SIZE = Window.WIDTH / 8;
    public static final int xBounds = Window.WIDTH;
    public static final int yBounds = Window.HEIGHT;

    public Level() {

        float localXBound = xBounds + 150;
        float localYBound = yBounds + 150;


        float[] vertices = new float[]{
                localXBound, -localYBound, 0.0f,
                localXBound, localYBound, 0.0f,
                -localXBound, localYBound, 0.0f,
                -localXBound, -localYBound, 0.0f,
        };

        localXBound = xBounds * 2f;
        localYBound = yBounds * 2f;

        float[] vertices2 = new float[]{
                localXBound, -localYBound, 0.0f,
                localXBound, localYBound, 0.0f,
                -localXBound, localYBound, 0.0f,
                -localXBound, -localYBound, 0.0f,
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
        firstBackground = new VertexArray(vertices2, indices, textures);
        backgroundTexture = new Texture("src/main/resources/images/bg.jpg");
        firstBackgroundTexture = new Texture("src/main/resources/images/firstbg.png");
    }

    public void update() {
    }

    public void render() {
        firstBackgroundTexture.bind();
        Shader.BACKGROUND.enable();
        firstBackground.bind();

        Shader.BACKGROUND.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(0.0f, 0.0f, 0.0f)));

        firstBackground.draw();
        Shader.BACKGROUND.disable();
        backgroundTexture.unbind();

        backgroundTexture.bind();
        Shader.BACKGROUND.enable();


        background.bind();

        Shader.BACKGROUND.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(0.0f, 0.0f, 0.0f)));

        background.draw();

        Shader.BACKGROUND.disable();
        backgroundTexture.unbind();
    }
}
