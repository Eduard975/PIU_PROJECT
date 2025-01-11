package entities;

import ai.PathFinder;
import graphic.Shader;
import graphic.SpriteSheet;
import graphic.Texture;
import graphic.VertexArray;
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

    private int spriteWidth;
    private int spriteHeight;
    private int offsetX;
    private int offsetY;
    private float moveSpeed = 100.0f;
    private float pathUpdateInterval = 0.25f;
    private float pathUpdateTimer = 0;
    private List<Vector3f> currentPath;
    private int currentPathIndex;
    private static PathFinder pathFinder;
    private Vector3f targetPlayerPos;
    private int baseXp = 2;

    static {
        pathFinder = new PathFinder(800f, 600f, 16f);
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

    private void updatePath() {
        List<Vector3f> newPath = pathFinder.findPath(position, targetPlayerPos);
        if (!newPath.isEmpty()) {
            currentPath = newPath;
            currentPathIndex = 0;
        }
    }

    private void moveTowardsPoint(Vector3f target, float delta) {
        float dx = target.x - position.x;
        float dy = target.y - position.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            float moveX = (dx / distance) * moveSpeed * delta;
            float moveY = (dy / distance) * moveSpeed * delta;

            position.x += moveX;
            position.y += moveY;

            angle = (float) Math.toDegrees(Math.atan2(dy, dx));
        }
    }

    @Override
    public void update() {
        if (stunDuration > 0) {
            stunDuration--;
            return;
        }
        // Update animation
        updateAnimation();

        float delta = 1.0f / 60.0f; // Fixed delta time if not provided

        // Update pathfinding
        pathUpdateTimer += delta;
        if (pathUpdateTimer >= pathUpdateInterval) {
            updatePath();
            pathUpdateTimer = 0;
        }

        // Update movement
        if (!currentPath.isEmpty() && currentPathIndex < currentPath.size()) {
            Vector3f targetPos = currentPath.get(currentPathIndex);
            float dx = targetPos.x - position.x;
            float dy = targetPos.y - position.y;
            float distanceSquared = dx * dx + dy * dy;

            if (distanceSquared < NODE_REACH_THRESHOLD) {
                currentPathIndex++;
            } else {
                moveTowardsPoint(targetPos, delta);
            }
        } else {
            // If no path is available, move directly towards the player
            moveTowardsPoint(targetPlayerPos, delta);
        }
    }

    public void updatePlayerPos(Vector3f playerPos) {
        this.targetPlayerPos.x = playerPos.x;
        this.targetPlayerPos.y = playerPos.y;
        this.targetPlayerPos.z = playerPos.z;
    }

    public static void setObstacle(Vector3f position, boolean isObstacle) {
        pathFinder.setObstacle(position, isObstacle);
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