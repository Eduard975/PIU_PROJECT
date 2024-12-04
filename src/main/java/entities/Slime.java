package entities;

import graphic.Shader;
import graphic.SpriteSheet;
import graphic.Texture;
import graphic.VertexArray;
import math.Matrix4f;
import math.Vector3f;

public class Slime extends EnemyBase {

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

    public Slime(Vector3f pos) {
        this(pos, 25, 25); // Default width = 25, height = 25
    }

    public Slime(Vector3f pos, int mySpriteWidth, int mySpriteHeight) {
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

        spriteWidth = mySpriteWidth; // Size of a single sprite frame
        spriteHeight = mySpriteHeight;
        offsetX = (defaultSpriteWidth - spriteWidth) / 2;
        offsetY = (defaultSpriteHeight - spriteHeight) / 2;

        spriteSheet = new SpriteSheet(
                new Texture("src/main/resources/slime.png"),
                64, 64, 24, 0 // Adjust these values to match your spritesheet
        );

        totalFrames = spriteSheet.getSpriteNum(); // Number of frames in the animation
        currentFrame = 0; // Start at the first frame
        animationSpeed = 100; // Animation speed: 100ms per frame
        lastFrameTime = System.currentTimeMillis();

        // Initialize the first sprite frame
        sprite = spriteSheet.getSpriteWithOffset(currentFrame, offsetX, offsetY, spriteWidth, spriteHeight);

        texture = sprite.getTexture();
        texCoords = sprite.getTexCoords();

        float[] tcs = new float[]{
                texCoords[3].x, texCoords[3].y, // Top-left
                texCoords[2].x, texCoords[2].y, // Bottom-left
                texCoords[1].x, texCoords[1].y, // Bottom-right
                texCoords[0].x, texCoords[0].y  // Top-right
        };

        mesh = new VertexArray(vertices, indices, tcs);
    }

    @Override
    public void update() {
        // Handle animation timing
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime >= animationSpeed) {
            currentFrame = (currentFrame + 1) % totalFrames; // Loop through frames
            lastFrameTime = currentTime;

            // Update the sprite for the current frame
            sprite = spriteSheet.getSpriteWithOffset(currentFrame, offsetX, offsetY, spriteWidth, spriteHeight);
            texCoords = sprite.getTexCoords();

            float[] tcs = new float[]{
                    texCoords[3].x, texCoords[3].y, // Top-left
                    texCoords[2].x, texCoords[2].y, // Bottom-left
                    texCoords[1].x, texCoords[1].y, // Bottom-right
                    texCoords[0].x, texCoords[0].y  // Top-right
            };

            // Update texture coordinates in the mesh
            mesh.updateTexCoords(tcs);
        }
    }

    @Override
    public void render() {
        Shader.SLIME.enable();
        Shader.SLIME.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(angle)));
        texture.bind();
        mesh.render();
        Shader.SLIME.disable();
    }
}
