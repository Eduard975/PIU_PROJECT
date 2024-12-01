package map;

import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import math.Matrix4f;
import math.Vector3f;
import player.Projectile;

public class Tile {
    private VertexArray mesh;
    private Texture texture;

    public Tile(int type) {
        float[] vertices = new float[] {
                -10.0f, -10.0f, 0.0f,
                -10.0f, 10.0f, 0.0f,
                0.0f, 10.0f, 0.0f,
                0.0f, -10.0f, 0.0f,
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
        texture = new Texture("src/main/resources/bg.jpeg");
        mesh = new VertexArray(vertices, indices, textures);


//        switch (type){
//                case 1:
//                    texture = new Texture("src/main/resources/bg.jpeg");
//                    break;
//                case 2:
//
//            default:
//                texture = new Texture("src/main/resources/bg.jpeg");
//        }
    }

//    public void render() {
//        Shader.TILE.enable();
//        Shader.TILE.setUniformMat4f("ml_matrix", absolutePosition);
//        texture.bind();
//        mesh.render();
//        Shader.TILE.disable();
//    }
}

