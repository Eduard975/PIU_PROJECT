package main;

import graphic.Window;
import map.Level;
import math.Vector3f;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MouseInput extends GLFWCursorPosCallback {

    private double mouseX, mouseY;

    @Override
    public void invoke(long window, double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
        System.out.println("x: " + x + " y: " + y);
    }

    public Vector3f getMousePosition() {
        float worldX = (float) (mouseX / Window.WIDTH * Level.xBounds - Level.xBounds / 2.0f);
        float worldY = (float) (-mouseY / Window.HEIGHT * Level.yBounds - Level.yBounds / 2.0f);
        return new Vector3f(worldX, worldY, 0);
    }
}
