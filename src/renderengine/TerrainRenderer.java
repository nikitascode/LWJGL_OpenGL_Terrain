package renderengine;

import entities.Terrain;
import model.EntityModel;
import model.ModelTexture;
import org.lwjgl.util.vector.Matrix4f;
import shaders.TerrainShader;
import toolbox.MatrixMath;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class TerrainRenderer {
    private TerrainShader shader;
    private Matrix4f transformationMatrix;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Terrain terrain) {
        prepareTerrainModel(terrain);
        loadModelMatrix(terrain);
        unbindTxturedModel(terrain);
    }

    private void prepareTerrainModel(Terrain terrain) {
        EntityModel model = terrain.getModel();
        ModelTexture texture = terrain.getTexture();

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        texture.bind(0);
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

        glBindBuffer(GL_ARRAY_BUFFER, model.getVboID());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, model.getTextureID());
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, model.getNormalsID());
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, model.getIndicesID());
    }

    private void loadModelMatrix(Terrain terrain) {
        transformationMatrix = MatrixMath.createTransformationMatrix(terrain.getPosition(), 0, 135, 0, 1);
        shader.loadTransformationMatrix(transformationMatrix);

        glDrawElements(GL_TRIANGLES, terrain.getModel().getCount(), GL_UNSIGNED_INT, 0);
    }

    private void unbindTxturedModel(Terrain terrain) {
        terrain.getTexture().unbind();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
    }
}
