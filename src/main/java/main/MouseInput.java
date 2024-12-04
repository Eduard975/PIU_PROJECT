package main;

import graphic.Window;
import map.Level;
import math.Vector3f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.opengl.GLUtil;

public class MouseInput extends GLFWCursorPosCallback {

    private float mouseX, mouseY;

    @Override
    public void invoke(long window, double x, double y) {
        this.mouseX = (float)x;
        this.mouseY = (float)y;
        System.out.println("x: " + x + " y: " + y);
    }

    public Vector3f getMousePos(){
        return new Vector3f(mouseX, mouseY, 0.0f);
    }

    public Vector3f getMousePosition() {

        float worldX = mouseX;
        float worldY = mouseY;



        return new Vector3f(worldX, worldY, 0);
    }
}
