package player;

import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import map.Level;
import math.Matrix4f;
import math.Vector3f;

public class Explosion {
    private float SIZE = Level.TILE_SIZE/8.0f;
    private VertexArray mesh;
    private Texture texture;

    public Vector3f position;

    public float radius;

    public float damage = 100;

    public int explosionFrames = 60;

    public Explosion(Vector3f position, float radius) {
        this.position = new Vector3f(position.x, position.y, position.z);
        this.radius = radius;

        SIZE = SIZE + radius;

        float[] vertices = new float[] {
                -SIZE / 2.0f, -SIZE / 2.0f, 0.5f,
                -SIZE / 2.0f,  SIZE / 2.0f, 0.5f,
                SIZE / 2.0f,  SIZE / 2.0f, 0.5f,
                SIZE / 2.0f, -SIZE / 2.0f, 0.5f
        };

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

        mesh = new VertexArray(vertices, indices, tcs);
        texture = new Texture("src/main/resources/projectile.png");
    }

    public void update(){

    }

    public void render() {
        if(explosionFrames > 0){

            Shader.PROJECTILE.enable();
            Shader.PROJECTILE.setUniformMat4f("ml_matrix", Matrix4f.translate(position));
            texture.bind();
            mesh.render();
            Shader.PROJECTILE.disable();
            explosionFrames--;
        }
    }
}
