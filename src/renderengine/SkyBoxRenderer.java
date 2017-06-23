package renderengine;

import entities.Camera;
import model.CubeModel;
import model.CubeTextureModel;
import model.EntityModel;
import org.lwjgl.util.vector.Matrix4f;
import shaders.SkyBoxShader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;

public class SkyBoxRenderer {
    private static final float SENSIVITY = 0.04f;

    private static String[] TEXTURE_FILES = { "right", "left", "top", "bottom", "back", "front" };
    private static String[] MOON_TEXTURE_FILES = { "MoonRight", "MoonLeft", "MoonTop", "MoonBottom", "MoonBack", "MoonFront" };

    private CubeModel texture;
    private CubeModel moonTexture;
    private SkyBoxShader shader;

    private float time = 0;

    public SkyBoxRenderer(Matrix4f projectionMatrix) {
        texture = new CubeModel(TEXTURE_FILES);
        moonTexture = new CubeModel(MOON_TEXTURE_FILES);
        shader = new SkyBoxShader();
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Camera camera, float r, float g, float b) {
        EntityModel model = texture.getModel();
        CubeTextureModel moonTex = moonTexture.getTexture();
        CubeTextureModel tex = texture.getTexture();

        shader.start();
        shader.loadViewMatrix(camera);
        shader.loadFogColour(r, g, b);
        glEnableVertexAttribArray(0);

        bindTextures(tex, moonTex);

        glBindBuffer(GL_ARRAY_BUFFER, model.getVboID());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glDrawArrays(GL_TRIANGLES, 0, model.getCount());

        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
        glDisableVertexAttribArray(0);

        shader.stop();
    }

    private void bindTextures(CubeTextureModel tex, CubeTextureModel moonTex){
        time += SENSIVITY * 1000;
        time %= 24000;
        int texture1;
        int texture2;
        float blendFactor;
        if(time >= 0 && time < 5000){
            texture1 = moonTex.getCubeTextureID();
            texture2 = moonTex.getCubeTextureID();
            MasterRenderer.RED = 0.5f;
            MasterRenderer.GREEN = 0.5f;
            MasterRenderer.BLUE = 0.5f;
            blendFactor = (time - 0)/(5000 - 0);
        }else if(time >= 5000 && time < 8000){
            MasterRenderer.RED = 0.8f;
            MasterRenderer.GREEN = 0.8f;
            MasterRenderer.BLUE = 0.8f;
            texture1 = moonTex.getCubeTextureID();
            texture2 = tex.getCubeTextureID();
            blendFactor = (time - 5000)/(8000 - 5000);
        }else if(time >= 8000 && time < 21000){
            MasterRenderer.RED = 0.7f;
            MasterRenderer.GREEN = 0.7f;
            MasterRenderer.BLUE = 0.7f;
            texture1 = tex.getCubeTextureID();
            texture2 = tex.getCubeTextureID();
            blendFactor = (time - 8000)/(21000 - 8000);
        }else{
            MasterRenderer.RED = 0.5f;
            MasterRenderer.GREEN = 0.5f;
            MasterRenderer.BLUE = 0.5f;
            texture1 = tex.getCubeTextureID();
            texture2 = moonTex.getCubeTextureID();
            blendFactor = (time - 21000)/(24000 - 21000);
        }

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texture1);
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texture2);
        shader.loadBlendFactor(blendFactor);
    }

    public void clean(CubeTextureModel tex, CubeTextureModel moonTex) {
        shader.clean();
        glDeleteTextures(moonTex.getCubeTextureID());
        glDeleteTextures(tex.getCubeTextureID());
    }
}
