package game;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Terrain;
import model.EntityModel;
import model.ModelTexture;
import model.TexturedModel;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.util.vector.Vector3f;
import renderengine.MasterRenderer;
import texture.TextureLoader;
import toolbox.OBJLoader;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class MainGameWindow {
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 650;
    public static final String WINDOW_TITLE = "Game Demo";

    private long window;

    public void run() {
        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, NULL, NULL);
        if(window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        try(MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void loop() {
        GL.createCapabilities();

        ModelTexture textureDragon = new ModelTexture(TextureLoader.loadTexture("/res/texture_dragon.png"));
        EntityModel entitytDragon = OBJLoader.loadObjModel("/res/dragon.obj");
        TexturedModel modelDragon = new TexturedModel(entitytDragon, textureDragon);
        Entity dragon = new Entity(modelDragon, new Vector3f(1, -35, -550), 0, 0, 0, 10);

        ModelTexture modelTerrain = new ModelTexture(TextureLoader.loadTexture("/res/texture_rocks.png"));
        Terrain terrainModel = new Terrain(new Vector3f(0, -50, -50), modelTerrain, "/res/heightmap.png");
        modelTerrain.setShineDamper(200);
        modelTerrain.setReflectivity(50);

        Camera camera = new Camera(window);
        Light light = new Light(new Vector3f(2000, 2000, 1000), new Vector3f(1f, 1f, 1f));

        MasterRenderer renderer = new MasterRenderer();

        while(!glfwWindowShouldClose(window)) {
            camera.move();
            dragon.increaseRotation(0, 1, 0);

            renderer.processEntity(dragon);
            renderer.render(camera, light, terrainModel);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        renderer.clean();
    }
}