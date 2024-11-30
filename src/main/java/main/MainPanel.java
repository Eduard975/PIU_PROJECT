package main;


import graphic.Shader;
import graphic.Window;
import map.Level;
import math.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_VERSION;
import static org.lwjgl.opengl.GL11C.glGetString;

public class MainPanel implements Runnable {
    public static final int TARGET_FPS = 75;
    public static final int TARGET_UPS = 30;

    private GLFWErrorCallback errorCallback;

    protected boolean running;

    protected Window window;

    private Level level;

    public void start() {
        init();
        run();
        dispose();
    }

    public void dispose() {

        /* Release window and its callbacks */
        window.destroy();

        /* Terminate GLFW and release the error callback */
        glfwTerminate();
        errorCallback.free();
    }

    public void init() {
        /* Set error callback */
        errorCallback = GLFWErrorCallback.createPrint();
        glfwSetErrorCallback(errorCallback);

        /* Initialize GLFW */
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW!");
        }

        window = new Window(640, 480, "NecroLord");
        Shader.loadAll();

        Matrix4f pr_matrix = Matrix4f.orthographic(-16.0f, 16.0f, -9.0f, 9.0f, -1.0f, 1.0f );

        Shader.BACKGROUND.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.BACKGROUND.setUniformMat4f("tile_color", Matrix4f.identity()); // Default color

        running = true;

        level = new Level();
    }


    @Override
    public void run() {
        float delta;

        while (running) {
            /* Check if game should close */
            if (window.isClosing()) {
                running = false;
            }

            window.update();
            update();
            render();

        }
    }

    private void update() {
        long windowId = GLFW.glfwGetCurrentContext();
        if (glfwGetKey(windowId, GLFW_KEY_UP) == GLFW_PRESS) {
            System.out.println(glGetString(GL_VERSION));
        }
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        level.render();
    }
}
