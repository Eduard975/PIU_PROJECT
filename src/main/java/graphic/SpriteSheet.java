package graphic;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {
    private Texture texture;
    private List<Sprite> sprites;

    public SpriteSheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        this.sprites = new ArrayList<>();

        this.texture = texture;
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;
        for (int i = 0; i < numSprites; i++) {
            // Calculate full sprite texture coordinates
            float topY = (currentY + spriteHeight) / (float) texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float) texture.getWidth();
            float leftX = currentX / (float) texture.getWidth();
            float bottomY = currentY / (float) texture.getHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };
            Sprite sprite = new Sprite(this.texture, texCoords);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if (currentX >= texture.getWidth()) {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index) {
        return this.sprites.get(index);
    }

    public int getSpriteNum() {
        return this.sprites.size();
    }

    public Sprite getSpriteWithOffset(int index, int offsetX, int offsetY, int width, int height) {
        Sprite baseSprite = this.sprites.get(index);

        // Calculate texture coordinates for the sub-region
        Vector2f[] baseCoords = baseSprite.getTexCoords();

        float textureLeft = baseCoords[2].x + (offsetX / (float) texture.getWidth());
        float textureRight = textureLeft + (width / (float) texture.getWidth());
        float textureBottom = baseCoords[2].y + (offsetY / (float) texture.getHeight());
        float textureTop = textureBottom + (height / (float) texture.getHeight());

        Vector2f[] texCoords = {
                new Vector2f(textureRight, textureTop),     // Top-right
                new Vector2f(textureRight, textureBottom), // Bottom-right
                new Vector2f(textureLeft, textureBottom),  // Bottom-left
                new Vector2f(textureLeft, textureTop)      // Top-left
        };

        return new Sprite(baseSprite.getTexture(), texCoords);
    }
}
