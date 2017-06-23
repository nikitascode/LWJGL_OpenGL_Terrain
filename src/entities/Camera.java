package entities;

import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.glfw.GLFW.*;


public class Camera {
    private static final float SENSIVITY = 5.0f;

    private Vector3f position;
    private float pitch;
    private float yaw;
    private float roll;

    private long window;

    public Camera(long window) {
        this.window = window;
        position = new Vector3f(0, 0, 0);
    }

    public void move() {
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
            if(key == GLFW_KEY_W && action != GLFW_RELEASE) {
                position.x += Math.sin(Math.toRadians(yaw)) * SENSIVITY;
                position.z -= Math.cos(Math.toRadians(yaw)) * SENSIVITY;
            }
            if(key == GLFW_KEY_S && action != GLFW_RELEASE) {
                position.x -= Math.sin(Math.toRadians(yaw)) * SENSIVITY;
                position.z += Math.cos(Math.toRadians(yaw)) * SENSIVITY;
            }
            if(key == GLFW_KEY_D && action != GLFW_RELEASE) {
                position.x += Math.sin(Math.toRadians(yaw + 90)) * SENSIVITY;
                position.z -= Math.cos(Math.toRadians(yaw + 90)) * SENSIVITY;
            }
            if(key == GLFW_KEY_A && action != GLFW_RELEASE) {
                position.x += Math.sin(Math.toRadians(yaw - 90)) * SENSIVITY;
                position.z -= Math.cos(Math.toRadians(yaw - 90)) * SENSIVITY;
            }
            if(key == GLFW_KEY_E && action != GLFW_RELEASE) yaw += 2.0f;
            if(key == GLFW_KEY_Q && action != GLFW_RELEASE) yaw -= 2.0f;
        });
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
