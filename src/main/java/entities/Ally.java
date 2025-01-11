package entities;

import graphic.Shader;
import graphic.SpriteSheet;
import graphic.Texture;
import graphic.VertexArray;
import math.Matrix4f;
import math.Vector3f;

public class Ally extends EnemyBase {
    private final SpriteSheet spriteSheet;
    private int currentFrame;
    private int totalFrames;
    private long animationSpeed; // Time between frames in milliseconds
    private long lastFrameTime;

    int defaultSpriteWidth = 64;
    int defaultSpriteHeight = 64;

    int spriteWidth;
    int spriteHeight;
    int offsetX;
    int offsetY;

    public Ally(Vector3f pos) {
        this(pos, 25, 25);
    }

    public Ally(Vector3f pos, int mySpriteWidth, int mySpriteHeight) {
        super(pos);
        attackDamage = 15;
        attackCooldown = 4000;

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
                new Texture("src/main/resources/slime.png"),
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

    @Override
    public void update() {
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
    public void render() {
        Shader.SLIME.enable();
        Shader.SLIME.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(angle)));
        Shader.SLIME.setUniform1i("isAlly", 1);

        texture.bind();
        mesh.render();
        Shader.SLIME.disable();
    }
}
