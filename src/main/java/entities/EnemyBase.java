package entities;

import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import math.Matrix4f;
import math.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public abstract class EnemyBase {
    public float SIZE = 4.0f;
    public VertexArray mesh;
    public Texture texture;
    public int hp = 100;

    public int angle;

    public Vector3f position;


    abstract public void update();

    abstract public void render();
}
