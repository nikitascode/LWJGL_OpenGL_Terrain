package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.MatrixMath;

public class TerrainShader extends ShaderProgram {
    public static final String VERTEX_FILE = "/shaders/terrainVertexShader.txt";
    public static final String FRAGMENT_FILE = "/shaders/terrainFragmentShader.txt";

    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int locationLightPosition;
    private int locationLightColour;
    private int locationShineDamper;
    private int locationReflectivity;
    private int locationSkyColour;

    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "vertices");
        super.bindAttribute(1, "textures");
        super.bindAttribute(2, "normals");
    }

    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
        locationLightPosition = super.getUniformLocation("lightPosition");
        locationLightColour = super.getUniformLocation("lightColour");
        locationShineDamper = super.getUniformLocation("shineDamper");
        locationReflectivity = super.getUniformLocation("reflectivity");
        locationSkyColour = super.getUniformLocation("skyColour");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(locationTransformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(locationProjectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f matrix = MatrixMath.createViewMatrix(camera);
        super.loadMatrix(locationViewMatrix, matrix);
    }

    public void loadLight(Light light) {
        super.loadVector(locationLightPosition, light.getPosition());
        super.loadVector(locationLightColour, light.getColour());
    }

    public void loadShineVariables(float damper, float reflectivity) {
        super.loadFloat(locationShineDamper, damper);
        super.loadFloat(locationReflectivity, reflectivity);
    }

    public void loadSkyColour(float r, float g, float b) {
        super.loadVector(locationSkyColour, new Vector3f(r, g, b));
    }
}
