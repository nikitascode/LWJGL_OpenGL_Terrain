package renderengine;

import entities.Entity;
import model.EntityModel;
import model.ModelTexture;
import model.TexturedModel;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import toolbox.MatrixMath;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class EntityRenderer {

    private Matrix4f transformationMatrix;
    private StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Map<TexturedModel, List<Entity>> entities) {
        for(TexturedModel model : entities.keySet()) {
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for(Entity entity : batch) {
                prepareInstance(entity);
            }
            unbindTxturedModel(model);
        }
    }

    private void prepareTexturedModel(TexturedModel texturedModel) {
        EntityModel model = texturedModel.getModel();
        ModelTexture texture = texturedModel.getTexture();

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        texturedModel.getTexture().bind(0);
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

        glBindBuffer(GL_ARRAY_BUFFER, model.getVboID());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, model.getTextureID());
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, model.getNormalsID());
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, model.getIndicesID());
    }

    private void prepareInstance(Entity entity) {
        transformationMatrix = MatrixMath.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
                entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);

        glDrawElements(GL_TRIANGLES, entity.getTexturedModel().getModel().getCount(), GL_UNSIGNED_INT, 0);
    }

    private void unbindTxturedModel(TexturedModel model) {
        model.getTexture().unbind();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
    }
}
