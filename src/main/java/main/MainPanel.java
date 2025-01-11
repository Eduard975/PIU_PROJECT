package main;


import entities.EnemyManager;
import graphic.Shader;
import graphic.SpriteSheet;
import graphic.Texture;
import math.Vector3f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import player.AbilityManager;
import player.Player;
import graphic.Shader;
import graphic.Window;
import map.Level;
import math.Matrix4f;
import math.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import player.Player;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class MainPanel implements Runnable {
    public static final double TARGET_FPS = 60;
    public static final double TARGET_UPS = 60;

    private static final boolean RENDER_TIME = true;

    private GLFWErrorCallback errorCallback;

    protected boolean running;

    protected Window window;

    private Level level;

    private Player player;

    private EnemyManager enemyManager;

    private CollisionManager collisionManager;

    private Camera camera;

    private MouseInput cursorPos;

    private HUD hud;

    private SpriteSheet spriteSheet;

    private AbilityManager abilityManager;


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


        window = new Window("NecroLord");
        running = true;

        collisionManager = new CollisionManager();
        player = new Player();
        level = new Level(player);
        hud = new HUD(player);
        enemyManager = new EnemyManager(player);
        abilityManager = new AbilityManager(player, enemyManager, collisionManager);



        camera = new Camera(new Vector3f(0, 0, 0), player);

        glfwSetCursorPosCallback(window.id, cursorPos = new MouseInput());

        glActiveTexture(GL_TEXTURE1);
        Shader.loadAll();

        Matrix4f pr_matrix = camera.getProjectionMatrix();

        Shader.BACKGROUND.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.BACKGROUND.setUniform1i("tex", 1);

        Shader.PLAYER.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.PLAYER.setUniform1i("tex", 1);

        Shader.SLIME.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.SLIME.setUniform1i("tex", 1);

        Shader.PROJECTILE.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.PROJECTILE.setUniform1i("tex", 1);

        Shader.HP.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.MP.setUniformMat4f("pr_matrix", pr_matrix);

        Shader.INVENTORY.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.INVENTORY.setUniform1i("tex", 1);
    }


    @Override
    public void run() {

        long initialTime = System.nanoTime();
        final double timeU = 1000000000 / TARGET_UPS;
        final double timeF = 1000000000 / TARGET_FPS;
        double deltaU = 0, deltaF = 0;
        int frames = 0, ticks = 0;
        long timer = System.currentTimeMillis();

        while (running) {
            if (window.isClosing()) {
                running = false;
            }

            long currentTime = System.nanoTime();
            deltaU += (currentTime - initialTime) / timeU;
            deltaF += (currentTime - initialTime) / timeF;
            initialTime = currentTime;

            if (deltaU >= 1) {
                update();
                ticks++;
                deltaU--;
            }

            if (deltaF >= 1) {
                render();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                if (RENDER_TIME) {
//                    System.out.println(String.format("UPS: %s, FPS: %s", ticks, frames));
                }
                frames = 0;
                ticks = 0;
                timer += 1000;
            }
        }
    }


    private void update() {
        window.update();
        player.update();
        level.update();
        collisionManager.checkProjectilesCollision(player.projectiles, enemyManager.enemies);
        enemyManager.update();
        camera.update();
        abilityManager.update();
        collisionManager.checkPlayerEnemyCollision(player, enemyManager.enemies);


        Vector3f mousePos = cursorPos.getMouseWorldPosition(camera.getProjectionMatrix(), camera.position);
        player.setAngle(cursorPos.calculateAngleToMouse(player.getX(), player.getY()));
        player.setMousePosition(mousePos);
//        System.out.println("Angle :" + cursorPos.calculateAngleToMouse(player.getX(), player.getY()));

//        System.out.println("Mouse X: " + mousePos.x + ", Mouse Y: " + mousePos.y);
//        System.out.println("Player X: " + player.getX() + ", Player Y: " + player.getY());
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        level.render();
        player.render();
        enemyManager.render();
        hud.render();
    }
}
