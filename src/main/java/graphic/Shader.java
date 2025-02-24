package graphic;

import math.Matrix4f;
import math.Vector3f;
import utils.ShaderUtils;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    public static final int VERTEX_ATTRIB = 0;
    public static final int TCOORD_ATTRIB = 1;

    public static Shader BACKGROUND, PLAYER, SLIME, PROJECTILE, TILE, HP, MP, SKILLS, ICON, XP, TEXT;

    private boolean enabled = false;

    private final int ID;
    private Map<String, Integer> locationCache = new HashMap<String, Integer>();

    public Shader(String vertex, String fragment) {
        ID = ShaderUtils.load(vertex, fragment);
    }

    public static void loadAll() {
        BACKGROUND = new Shader("src/main/shaders/bg.vert", "src/main/shaders/bg.frag");
        SKILLS = new Shader("src/main/shaders/skills.vert", "src/main/shaders/skills.frag");

        PLAYER = new Shader("src/main/shaders/player.vert", "src/main/shaders/player.frag");
        SLIME = new Shader("src/main/shaders/slime.vert", "src/main/shaders/slime.frag");
        PROJECTILE = new Shader("src/main/shaders/projectile.vert", "src/main/shaders/projectile.frag");
        TILE = new Shader("src/main/shaders/tile.vert", "src/main/shaders/tile.frag");
        HP = new Shader("src/main/shaders/hp.vert", "src/main/shaders/hp.frag");
        MP = new Shader("src/main/shaders/hp.vert", "src/main/shaders/mp.frag");

        ICON = new Shader("src/main/shaders/icon.vert", "src/main/shaders/icon.frag");

        XP = new Shader("src/main/shaders/hp.vert", "src/main/shaders/xp.frag");
        TEXT = new Shader("src/main/shaders/text.vert", "src/main/shaders/text.frag");
    }

    public int getUniform(String name) {
        if (locationCache.containsKey(name))
            return locationCache.get(name);

        int result = glGetUniformLocation(ID, name);
        if (result == -1)
            System.err.println("Could not find uniform variable '" + name + "'!");
        else
            locationCache.put(name, result);
        return result;
    }

    public void setUniform1i(String name, int value) {
        if (!enabled) enable();
        glUniform1i(getUniform(name), value);
    }


    public void setUniform1f(String name, float value) {
        if (!enabled) enable();
        glUniform1f(getUniform(name), value);
    }

    public void setUniform2f(String name, float x, float y) {
        if (!enabled) enable();
        glUniform2f(getUniform(name), x, y);
    }

    public void setUniform3f(String name, Vector3f vector) {
        if (!enabled) enable();
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }

    public void setUniformMat4f(String name, Matrix4f matrix) {
        if (!enabled) enable();
        glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
    }

    public void enable() {
        glUseProgram(ID);
        enabled = true;
    }

    public void disable() {
        glUseProgram(0);
        enabled = false;
    }

}
