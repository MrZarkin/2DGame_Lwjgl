package jade;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window
{
    private final int width, height;
    private final String title;
    private long glfwWindow;

    private static Window window = null;

    private Window()
    {
        this.height = 800;
        this.width = 600;
        this.title = "Mario";
    }

    public static Window get()
    {
        if(Window.window == null)
        {
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run() {

        init();
        loop();
    }

    public void init()
    {
        // Setting up error Callbacks
        GLFWErrorCallback.createPrint(System.err).set();

        // Init glfw
        if(!glfwInit())
        {
            throw new IllegalStateException("Unable to init GLFW");
        }

        // Config GLFW
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Config Window
        glfwWindow = GLFW.glfwCreateWindow(this.width, this.height, this.title, 0, 0);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable Vsync
        glfwSwapInterval(1);
        // Make the window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
    }

    public void loop()
    {
        while(!glfwWindowShouldClose(glfwWindow))
        {
            // Poll Events
            glfwPollEvents();

            glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(glfwWindow);
        }
    }

}
