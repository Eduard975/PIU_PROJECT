package entities;

import ai.PathFinder;
import graphic.*;
import math.Matrix4f;
import math.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Slime extends EnemyBase {
    private final SpriteSheet spriteSheet;
    private int currentFrame;
    private int totalFrames;
    private long animationSpeed;
    private long lastFrameTime;

    private static final int DEFAULT_SPRITE_WIDTH = 64;
    private static final int DEFAULT_SPRITE_HEIGHT = 64;
    private static final float NODE_REACH_THRESHOLD = 4.0f;
    private static final float DIRECT_FOLLOW_DISTANCE = 100.0f;

    private int spriteWidth;
    private int spriteHeight;
    private int offsetX;
    private int offsetY;
    private float moveSpeed = 200.0f;
    private float pathUpdateInterval = 0.25f;
    private float pathUpdateTimer = 0;
    private List<Vector3f> currentPath;
    private int currentPathIndex;
    private static PathFinder pathFinder;
    private Vector3f targetPlayerPos;
    private int baseXp = 2;
    private boolean isDebug = false;

    static {
        pathFinder = new PathFinder(1280f, 720f, 16f);
    }

    public Slime(Vector3f pos) {
        this(pos, 25, 25);
    }

    public Slime(Vector3f pos, int mySpriteWidth, int mySpriteHeight) {
        super(pos);
        attackDamage = 15;
        attackCooldown = 4000;
//        TODO SCALE
        xpWorth = baseXp;

        float[] vertices = new float[]{
                -SIZE / 2.0f, -SIZE / 2.0f, 0.2f,
                -SIZE / 2.0f, SIZE / 2.0f, 0.2f,
                SIZE / 2.0f, SIZE / 2.0f, 0.2f,
                SIZE / 2.0f, -SIZE / 2.0f, 0.2f
        };

        byte[] indices = new byte[]{
                0, 1, 2,
                2, 3, 0
        };

        spriteWidth = mySpriteWidth;
        spriteHeight = mySpriteHeight;
        offsetX = (DEFAULT_SPRITE_WIDTH - spriteWidth) / 2;
        offsetY = (DEFAULT_SPRITE_HEIGHT - spriteHeight) / 2;

        spriteSheet = new SpriteSheet(
                new Texture("src/main/resources/images/slime.png"),
                DEFAULT_SPRITE_WIDTH, DEFAULT_SPRITE_HEIGHT, 0
        );

        totalFrames = spriteSheet.getSpriteNum();
        currentFrame = 0;
        animationSpeed = 100;
        lastFrameTime = System.currentTimeMillis();

        sprite = spriteSheet.getSpriteWithOffset(currentFrame, offsetX, offsetY, spriteWidth, spriteHeight);
        texture = sprite.getTexture();
        texCoords = sprite.getTexCoords();
        tcs = sprite.getTcs();

        mesh = new VertexArray(vertices, indices, tcs);

        currentPath = new ArrayList<>();
        currentPathIndex = 0;
        targetPlayerPos = new Vector3f(0.0f, 0.0f, 0.0f);
    }

    private void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime >= animationSpeed) {
            currentFrame = (currentFrame + 1) % totalFrames;
            lastFrameTime = currentTime;

            sprite = spriteSheet.getSpriteWithOffset(currentFrame, offsetX, offsetY, spriteWidth, spriteHeight);
            texCoords = sprite.getTexCoords();
            tcs = sprite.getTcs();
            mesh.updateTexCoords(tcs);
        }
    }



    @Override
    public void update() {
        if (stunDuration > 0) {
            stunDuration--;
            return;
        }
        updateAnimation();
        float delta = 1.0f / 60.0f;

        if (targetPlayerPos == null) {
            if (isDebug) System.out.println("No player position set");
            return;
        }

        // Simple distance check
        float dx = targetPlayerPos.x - position.x;
        float dy = targetPlayerPos.y - position.y;
        float distanceToPlayer = (float) Math.sqrt(dx * dx + dy * dy);

        if (isDebug) {
            System.out.println("Distance to player: " + distanceToPlayer);
            System.out.println("Current position: " + position);
            System.out.println("Target position: " + targetPlayerPos);
        }

        if (distanceToPlayer > 1.0f) {
            moveTowardsPoint(targetPlayerPos, delta);
            if (isDebug) System.out.println("Moving towards player");
        }
    }

    private void moveTowardsPoint(Vector3f target, float delta) {
        if (target == null) return;

        // Calculate direction
        float dx = target.x - position.x;
        float dy = target.y - position.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            float moveX = (dx / distance) * moveSpeed * delta;
            float moveY = (dy / distance) * moveSpeed * delta;

            // Update position
            position.x += moveX;
            position.y += moveY;

            // Update angle for sprite rotation
            angle = (float) Math.toDegrees(Math.atan2(dy, dx));

            if (isDebug) {
                System.out.println("Move amount: " + moveX + ", " + moveY);
                System.out.println("New position: " + position);
            }
        }
    }


    public void updatePlayerPos(Vector3f playerPos) {
        if (playerPos == null) {
            if (isDebug) System.out.println("Received null player position");
            return;
        }

        if (this.targetPlayerPos == null) {
            this.targetPlayerPos = new Vector3f(playerPos.x, playerPos.y, playerPos.z);
        } else {
            this.targetPlayerPos.x = playerPos.x;
            this.targetPlayerPos.y = playerPos.y;
            this.targetPlayerPos.z = playerPos.z;
        }

        if (isDebug) {
            System.out.println("Updated player position to: " + this.targetPlayerPos);
        }
    }

    private void updatePath() {
        // Only update path if we have a valid player position
        if (targetPlayerPos != null) {
            // Clamp target position to window bounds
            float clampedX = Math.max(SIZE/2, Math.min(targetPlayerPos.x, Window.WIDTH - SIZE/2));
            float clampedY = Math.max(SIZE/2, Math.min(targetPlayerPos.y, Window.HEIGHT - SIZE/2));
            Vector3f clampedTarget = new Vector3f(clampedX, clampedY, targetPlayerPos.z);

            List<Vector3f> newPath = pathFinder.findPath(position, clampedTarget);
            if (newPath != null && !newPath.isEmpty()) {
                currentPath = newPath;
                currentPathIndex = 0;
            } else {
                // Clear the current path if we couldn't find a new one
                currentPath = null;
                currentPathIndex = 0;
            }
        }
    }
    @Override
    public void render() {
        Shader.SLIME.enable();
        Shader.SLIME.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(angle)));
        Shader.SLIME.setUniform1i("isAlly", 0);

        texture.bind();
        mesh.render();
        Shader.SLIME.disable();
    }
}