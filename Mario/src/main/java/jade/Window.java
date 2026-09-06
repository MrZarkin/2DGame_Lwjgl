package jade;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window
{
    private final int width, height;
    private final String title;
    private long glfwWindow;
    private boolean fadeToBlack = false;

    public float r, g, b, a;

    private static Window window = null;
    private static Scene currentScene;

    private Window()
    {
        this.height = 800;
        this.width = 600;
        this.title = "Mario";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public static void changeScene(int newScene)
    {
        switch(newScene)
        {
            case 0:
                currentScene = new LevelEditorScene();
                // currentScene.init();
                break;

            case 1:
                currentScene = new LevelScene();
                break;

            default:
                assert false: "Unknown scene '" + newScene + "'";
                break;
        }
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

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
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
        if(glfwWindow == 0)
        {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable Vsync
        glfwSwapInterval(1);
        // Make the window visible
        glfwShowWindow(glfwWindow);

        /*
        * This line is critical for LWJGL's interoperation with GLFW's
        * OpenGL context, or any context that is managed externally
        * LWJGL detects the context that is current in the current thread,
        * creates the GLCapabilities instance and makes the OpenGL
        * bindings available for use
        * */
        GL.createCapabilities();

        Window.changeScene(0);
    }

    public void loop()
    {
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow))
        {
            // Poll Events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt >= 0)
            {
                currentScene.update(dt);
            }

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

}