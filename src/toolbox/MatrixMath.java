package toolbox;

import entities.Camera;
import game.MainGameWindow;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class MatrixMath {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();

        Vector3f cameraPosition = camera.getPosition();

        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0),
                matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0),
                matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1),
                matrix, matrix);

        Vector3f negativeCameraPosition = new Vector3f(-cameraPosition.x, -cameraPosition.y,
                -cameraPosition.z);
        Matrix4f.translate(negativeCameraPosition, matrix, matrix);
        return matrix;
    }

    public static Matrix4f createProjectionMatrix() {
        float aspectRatio = (float) MainGameWindow.WINDOW_WIDTH / (float) MainGameWindow.WINDOW_HEIGHT;
        float yscale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float xscale = yscale / aspectRatio;
        float frustrum_length = FAR_PLANE - NEAR_PLANE;

        Matrix4f matrix = new Matrix4f();
        matrix.m00 = xscale; matrix.m11 = yscale;
        matrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustrum_length);
        matrix.m23 = - 1; matrix.m33 = 0;
        matrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustrum_length);

        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx,
                                                      float ry, float rz, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();

        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);

        return matrix;
    }
}
