package player;

import graphic.Texture;
import graphic.VertexArray;
import graphic.Shader;
import math.Matrix4f;
import math.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Player {
    private float SIZE = 4.0f;
    private VertexArray mesh;
    private Texture texture;

    private ArrayList<Projectile> projectiles = new ArrayList<>();

    private float angle;
    private Vector3f position = new Vector3f();

    long windowId = GLFW.glfwGetCurrentContext();

    public int hp = 100;
    public int mp = 25;

    public Player(){
        float[] vertices = new float[] {
                -SIZE / 2.0f, -SIZE / 2.0f, 0.2f,
                -SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
                 SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
                 SIZE / 2.0f, -SIZE / 2.0f, 0.2f
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
        texture = new Texture("src/main/resources/player.png");
    }

    public void update(){
        if (glfwGetKey(windowId, GLFW_KEY_DOWN) == GLFW_PRESS) {
            position.y--;
        }
        if (glfwGetKey(windowId, GLFW_KEY_UP) == GLFW_PRESS) {
            position.y++;
        }
        if (glfwGetKey(windowId, GLFW_KEY_LEFT) == GLFW_PRESS) {
            position.x--;
        }
        if (glfwGetKey(windowId, GLFW_KEY_RIGHT) == GLFW_PRESS) {
            position.x++;
        }
        if (glfwGetKey(windowId, GLFW_KEY_E) == GLFW_PRESS) {
            angle++;
            projectiles.add(new Projectile(position, angle));
        }

        for(Projectile projectile : projectiles){
            projectile.update();
        }
    }

    public void render() {
        Shader.PLAYER.enable();
        Shader.PLAYER.setUniformMat4f("ml_matrix", Matrix4f.translate(position));
        Shader.PLAYER.setUniformMat4f("rot_matrix", Matrix4f.rotate(angle));
        texture.bind();
        mesh.render();
        for(Projectile projectile : projectiles){
            projectile.render();
        }
        Shader.PLAYER.disable();
    }
}
