package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
    private Vector3f position;
    private Vector3f colour;

    private float angle;

    public Light(Vector3f position, Vector3f colour) {
        this.position = position;
        this.colour = colour;

        angle = 1.0f;
    }
    public void increasePosition(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColour() {
        return colour;
    }

    public void setColour(Vector3f colour) {
        this.colour = colour;
    }
}
