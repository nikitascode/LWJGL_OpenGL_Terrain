package shaders;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.MatrixMath;

public class SkyBoxShader extends ShaderProgram{

    private static final String VERTEX_FILE = "/shaders/skyBoxVertexShader.txt";
    private static final String FRAGMENT_FILE = "/shaders/skyBoxFragmentShader.txt";

    private static final float ROTATION_SPEED = 1f;
    private static final float SENSIVITY = 0.04f;

    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int locationFogColour;
    private int locationBlendFactor;
    private int locationCubeMap1;
    private int locationCubeMap2;

    private float rotation = 0;

    public SkyBoxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(locationProjectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = MatrixMath.createViewMatrix(camera);
        matrix.m30 = 0; matrix.m31 = 0; matrix.m32 = 0;
        rotation += ROTATION_SPEED * SENSIVITY;
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
        super.loadMatrix(locationViewMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
        locationFogColour = super.getUniformLocation("fogColour");
        locationBlendFactor = super.getUniformLocation("blendFactor");
        locationCubeMap1 = super.getUniformLocation("cubeMap");
        locationCubeMap2 = super.getUniformLocation("cubeMap2");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "vertices");
    }

    public void loadFogColour(float r, float g, float b) {
        super.loadVector(locationFogColour, new Vector3f(r, g, b));
    }

    public void connectTextureUnits() {
        super.loadInt(locationCubeMap1, 0);
        super.loadInt(locationCubeMap2, 1);
    }

    public void loadBlendFactor(float blendFactor) {
        super.loadFloat(locationBlendFactor, blendFactor);
    }
}