package graphic;

import math.Matrix4f;
import math.Vector3f;

import java.util.HashMap;

public class FontRenderer {
    private HashMap<Character, Texture> characterTextures;
    private float characterWidth;
    private float characterHeight;


    public void init(){
        for (char c = 'A'; c <= 'Z'; c++) {
            String imagePath = "src/main/resources/images/" + c + ".png";
            loadCharacterTexture(c, imagePath);
        }

        for (char c = '0'; c <= '9'; c++) {
            String imagePath = "src/main/resources/images/" + c + ".png";
            loadCharacterTexture(c, imagePath);
        }

        loadCharacterTexture('!', "src/main/resources/images/!.png");
    }

    public FontRenderer(float characterWidth, float characterHeight) {
        this.characterWidth = characterWidth;
        this.characterHeight = characterHeight;
        this.characterTextures = new HashMap<>();
    }

    public void loadCharacterTexture(char c, String imagePath) {
        characterTextures.put(c, new Texture(imagePath));
    }

    public Texture getTextureForCharacter(char c) {
        return characterTextures.get(c);
    }

    public void drawString(String text, float startX, float startY, float scale) {
        float xOffset = startX;

        for (char c : text.toCharArray()) {
            Texture texture = characterTextures.get(c);

            if (texture != null) {
                Shader.TEXT.enable();

//                Shader.TEXT.setUniformMat4f("ml_matrix", Matrix4f.translate(new Vector3f(xOffset, startY, 0))
//                        .multiply(Matrix4f.scale(new Vector3f(scale, scale, 1.0f))));
                Shader.TEXT.setUniformMat4f("ml_matrix",Matrix4f.translate(new Vector3f(xOffset, startY, 0)).multiply(Matrix4f.scale(scale, scale, 1.0f)));
                texture.bind();
                renderQuad();
                texture.unbind();

                Shader.TEXT.disable();

                xOffset += characterWidth * scale;
            }
        }
    }

    public float getStringWidth(String text){
        return text.length() * characterWidth;
    }

    private void renderQuad() {
        float[] vertices = {
                0.0f, 0.0f, 0.0f,
                0.0f, characterHeight, 0.0f,
                characterWidth, characterHeight, 0.0f,
                characterWidth, 0.0f, 0.0f
        };

        byte[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        float[] tcs = {
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        VertexArray quad = new VertexArray(vertices, indices, tcs);
        quad.render();
    }


}