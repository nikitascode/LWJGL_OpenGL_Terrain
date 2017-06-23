package renderengine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Terrain;
import model.TexturedModel;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import shaders.TerrainShader;
import toolbox.MatrixMath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClearColor;

public class MasterRenderer {
    public static float RED = 0.5f;
    public static float GREEN = 0.5f;
    public static float BLUE = 0.5f;

    private Matrix4f projectionMatrix;

    private StaticShader shader = new StaticShader();
    private TerrainShader terrainShader = new TerrainShader();

    private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();

    private EntityRenderer renderer;
    private TerrainRenderer terrainRenderer;
    private SkyBoxRenderer skyBoxRenderer;

    public MasterRenderer() {
        prepareCullFace();
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        skyBoxRenderer = new SkyBoxRenderer(projectionMatrix);
    }

    public void render(Camera camera, Light light, Terrain terrain) {
        prepare();
        shader.start();
        shader.loadSkyColour(RED, GREEN, BLUE);
        shader.loadLight(light);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();
        terrainShader.start();
        terrainShader.loadSkyColour(RED, GREEN, BLUE);
        terrainShader.loadLight(light);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrain);
        terrainShader.stop();
        skyBoxRenderer.render(camera, RED, GREEN, BLUE);
        entities.clear();
    }

    public void clean() {
        for(TexturedModel texturedModel : entities.keySet()) {
            texturedModel.getModel().clean();
            texturedModel.getTexture().clean();
        }
        shader.clean();
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getTexturedModel();
        List<Entity> batch = entities.get(entityModel);
        if(batch != null) batch.add(entity);
        else {
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    private void createProjectionMatrix() {
        projectionMatrix = MatrixMath.createProjectionMatrix();
    }

    private void prepare() {
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(RED, GREEN, BLUE, 1);
    }

    private void prepareCullFace() {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }
}
