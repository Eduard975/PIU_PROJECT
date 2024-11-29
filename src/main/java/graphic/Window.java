package graphic;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private final long id;
    private final GLFWKeyCallback keyCallback;

    public Window(int width, int height, CharSequence title, boolean vsync){
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        long temp = glfwCreateWindow(1, 1, "", NULL, NULL);
        glfwMakeContextCurrent(temp);
        GL.createCapabilities();
        GLCapabilities caps = GL.getCapabilities();
        glfwDestroyWindow(temp);

        glfwDefaultWindowHints();
        if (caps.OpenGL32) {
            /* Hints for OpenGL 3.2 core profile */
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        } else if (caps.OpenGL21) {
            /* Hints for legacy OpenGL 2.1 */
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        } else {
            throw new RuntimeException("Neither OpenGL 3.2 nor OpenGL 2.1 is "
                    + "supported, you may want to update your graphics driver.");
        }
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        /* Create window with specified OpenGL context */
        id = glfwCreateWindow(width, height, title, NULL, NULL);
        if (id == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window!");
        }

        /* Center window on screen */
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(id,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        /* Create OpenGL context */
        glfwMakeContextCurrent(id);
        GL.createCapabilities();

        /* Enable v-sync */
        if (vsync) {
            glfwSwapInterval(1);
        }

        /* Set key callback */
        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                    glfwSetWindowShouldClose(window, true);
                }
            }
        };
        glfwSetKeyCallback(id, keyCallback);
    }

    public boolean isClosing() {
        return glfwWindowShouldClose(id);
    }

    public void setTitle(CharSequence title) {
        glfwSetWindowTitle(id, title);
    }

    public void update() {
        glfwSwapBuffers(id);
        glfwPollEvents();
    }

    public void destroy() {
        glfwDestroyWindow(id);
        keyCallback.free();
    }
}
