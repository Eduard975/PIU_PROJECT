package utils;

import graphic.Sprite;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import static org.lwjgl.opengl.ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB;
import static org.lwjgl.opengl.GL11.*;

public class SpriteSheetUtils {
    private static final Map<String, Sprite> spriteMap = new HashMap<String, Sprite>();
    private static final String SPRITESHEET_IMAGE_LOCATION = "resources/slime.png";
    private static final String SPRITESHEET_XML_LOCATION = "res/images/slime.xml";
    private static int spritesheet;
    private static Sprite currentSprite;

    private static void cleanUp(boolean asCrash) {
        glDeleteTextures(spritesheet);
        System.exit(asCrash ? 1 : 0);
    }

    public static int glLoadLinearPNG(String location) {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_RECTANGLE_ARB, texture);
        InputStream in = null;
        try {
            in = new FileInputStream(location);
            PNGDecoder decoder = new PNGDecoder(in);
            ByteBuffer buffer = BufferUtils.createByteBuffer(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_RECTANGLE_ARB, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA,
                    GL_UNSIGNED_BYTE, buffer);
        } catch (FileNotFoundException e) {
            System.err.println("Texture file could not be found.");
            return -1;
        } catch (IOException e) {
            System.err.print("Failed to load the texture file.");
            return -1;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        glBindTexture(GL_TEXTURE_RECTANGLE_ARB, 0);
        return texture;
    }

    private static void setUpSpriteSheets() {
        spritesheet = glLoadLinearPNG(SPRITESHEET_IMAGE_LOCATION);
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = builder.build(new File(SPRITESHEET_XML_LOCATION));
            Element root = document.getRootElement();
            for (Object spriteObject : root.getChildren()) {
                Element spriteElement = (Element) spriteObject;
                String name = spriteElement.getAttributeValue("n");
                int x = spriteElement.getAttribute("x").getIntValue();
                int y = spriteElement.getAttribute("y").getIntValue();
                int w = spriteElement.getAttribute("w").getIntValue();
                int h = spriteElement.getAttribute("h").getIntValue();
                Sprite sprite = new Sprite(name, x, y, w, h);
                spriteMap.put(sprite.getName(), sprite);
            }
        } catch (JDOMException e) {
            e.printStackTrace();
            cleanUp(true);
        } catch (IOException e) {
            e.printStackTrace();
            cleanUp(true);
        }
    }


}
