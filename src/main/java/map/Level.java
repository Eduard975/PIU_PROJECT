package map;

import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import math.Matrix4f;
import math.Vector3f;

public class Level {

    private VertexArray background;
    private Texture backgroundTexture;
    public int xScroll;
    public int yScroll;
    private int map;


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
    }

    public void update() {
//        xScroll--;
//        if (-xScroll % 335 == 0) map++;
    }

    public void render() {
        backgroundTexture.bind();
        Shader.BACKGROUND.enable();
        background.bind();
        for(int i = map; i < map + 4; i++){
            Shader.BACKGROUND.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));
            background.draw();
        }
        background.render();
        Shader.BACKGROUND.disable();
        backgroundTexture.unbind();
    }
}
