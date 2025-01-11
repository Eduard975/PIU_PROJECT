package entities;

import graphic.Shader;
import graphic.SpriteSheet;
import graphic.Texture;
import graphic.VertexArray;
import math.Matrix4f;
import math.Vector3f;

import java.util.List;

public class Ally extends EnemyBase {
    private final SpriteSheet spriteSheet;
    private int currentFrame;
    private int totalFrames;
    private long animationSpeed; // Time between frames in milliseconds
    private long lastFrameTime;
    private static final float MOVE_SPEED = 200.0f;
    private static final float TARGET_RANGE = 300.0f;

    private Vector3f targetEnemyPos;
    private EnemyBase targetEnemy;
    private boolean isDebug = false;

    int defaultSpriteWidth = 64;
    int defaultSpriteHeight = 64;

    int spriteWidth;
    int spriteHeight;
    int offsetX;
    int offsetY;

    public Ally(Vector3f pos) {
        this(pos, 25, 25);
    }

    public Ally(Vector3f pos, int hp, int attack, float scalingFactor) {
        this(pos, 25, 25);
        this.hp = hp + (int)(scalingFactor * 2.25);
        this.attackDamage = attack + (int)(scalingFactor * 1.75);

    }

    public Ally(Vector3f pos, int mySpriteWidth, int mySpriteHeight) {
        super(pos);
        attackCooldown = 2000;

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
        offsetX = (defaultSpriteWidth - spriteWidth) / 2;
        offsetY = (defaultSpriteHeight - spriteHeight) / 2;

        spriteSheet = new SpriteSheet(
                new Texture("src/main/resources/images/slime.png"),
                defaultSpriteWidth, defaultSpriteHeight, 0
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

    public void findNewTarget(List<EnemyBase> enemies) {
        float closestDistance = TARGET_RANGE;
        EnemyBase closestEnemy = null;

        for (EnemyBase enemy : enemies) {
            if (enemy instanceof Ally) continue; // Skip other allies

            float dx = enemy.position.x - position.x;
            float dy = enemy.position.y - position.y;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance < closestDistance) {
                closestDistance = distance;
                closestEnemy = enemy;
            }
        }

        targetEnemy = closestEnemy;
        if (targetEnemy != null) {
            targetEnemyPos = targetEnemy.position;
        }
    }

    private void moveTowardsEnemy(float delta) {
        if (targetEnemyPos == null) return;

        float dx = targetEnemyPos.x - position.x;
        float dy = targetEnemyPos.y - position.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        float moveX = (dx / distance) * MOVE_SPEED * delta;
        float moveY = (dy / distance) * MOVE_SPEED * delta;

        // Update position
        position.x += moveX;
        position.y += moveY;

        angle = (float) Math.toDegrees(Math.atan2(dy, dx));

        if (isDebug) {
            System.out.println("Moving towards enemy at: " + targetEnemyPos);
            System.out.println("Current position: " + position);
        }
    }

    @Override
    public void update() {

        updateAnimation();
        float delta = 1.0f / 60.0f;

        // Clear target if enemy becomes inactive
        if (targetEnemy != null && targetEnemy.hp <= 0) {
            targetEnemy = null;
            targetEnemyPos = null;
        }

        if (targetEnemy != null) {
            targetEnemyPos = targetEnemy.position;
            moveTowardsEnemy(delta);
        }
    }

    @Override
    public void render() {
        Shader.SLIME.enable();
        Shader.SLIME.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(angle)));
        Shader.SLIME.setUniform1i("isAlly", 1);

        texture.bind();
        mesh.render();
        Shader.SLIME.disable();
    }

    @Override
    public boolean canAttack(){
        if (attackCooldownTimer >= attackCooldown) {
            attackCooldownTimer = 0;
            return true;
        }
        attackCooldownTimer += System.currentTimeMillis() - lastFrameTime;
        return false;
    }
}
