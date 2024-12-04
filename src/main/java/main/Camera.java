package main;

import graphic.Shader;
import graphic.Window;
import map.Level;
import math.Matrix4f;
import math.Vector3f;
import player.Player;

public class Camera {

    public Vector3f position;

    private final Player player;

    public Camera(Vector3f position, Player player) {
        this.player = player;
        this.position = position;
    }

    public void setPosition(Vector3f newPosition) {
        this.position = newPosition;
    }

    public Matrix4f getProjectionMatrix() {
        float left = position.x - Window.WIDTH / 2f;
        float right = position.x + Window.WIDTH /2f;
        float top = position.y - Window.HEIGHT / 2f;
        float bottom = position.y + Window.HEIGHT /2f;

        return Matrix4f.orthographic(left, right, top, bottom, -1.0f, 1.0f);
    }

    public void update(){
        float playerX = player.getX();
        float playerY = player.getY();
        float newX = position.x;
        float newY = position.y;

        if(playerX >= -Level.xBounds && playerX <= Level.xBounds ){
           newX = playerX;
        }
        if(playerY >= -Level.yBounds && playerY <= Level.yBounds){
            newY = playerY;
        }

        setPosition(new Vector3f(newX, newY, 0));

        Matrix4f pr_matrix = getProjectionMatrix();
        Shader.BACKGROUND.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.PLAYER.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.SLIME.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.PROJECTILE.setUniformMat4f("pr_matrix", pr_matrix);
    }
}
