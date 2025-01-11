package main;

import graphic.Window;
import map.Level;
import math.Matrix4f;
import math.Vector3f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.opengl.GLUtil;

public class MouseInput extends GLFWCursorPosCallback {

    private float mouseX, mouseY;

    private float worldX, worldY;

    @Override
    public void invoke(long window, double x, double y) {
        this.mouseX = (float)x;
        this.mouseY = (float)y;
//        System.out.println("x: " + x + " y: " + y);
    }

    public Vector3f getMousePos(){
        return new Vector3f(mouseX, mouseY, 0.0f);
    }

    public Vector3f getMousePosition() {

        float worldX = mouseX;
        float worldY = mouseY;

        return new Vector3f(worldX, worldY, 0);
    }

    public Vector3f getMouseWorldPosition(Matrix4f projectionMatrix, Vector3f cameraPosition) {
        float ndcX = (2.0f * mouseX) / Window.WIDTH - 1.0f;
        float ndcY = 1.0f - (2.0f * mouseY) / Window.HEIGHT;

        Vector3f ndc = new Vector3f(ndcX, ndcY, 0.0f);


        Matrix4f invertedProjection = projectionMatrix.invert();

        Vector3f worldPos = invertedProjection.multiply(ndc);

        worldPos.x += cameraPosition.x;
        worldPos.y += cameraPosition.y;

        worldX = worldPos.x;
        worldY = worldPos.y;

        return new Vector3f(worldPos.x, worldPos.y, worldPos.z);
    }

    public float calculateAngleToMouse(float x, float y) {
        float dx = worldX - x;
        float dy = worldY - y;

        return (float) Math.toDegrees(Math.atan2(dy, dx));
    }
}
