package model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class CubeTextureModel {
    private int cubeTextureID;

    public CubeTextureModel(int cubeTextureID) {
        this.cubeTextureID = cubeTextureID;
    }

    public int getCubeTextureID() {
        return cubeTextureID;
    }

    public void setCubeTextureID(int cubeTextureID) {
        this.cubeTextureID = cubeTextureID;
    }

    public void bind(int sampler) {
        if(sampler >= 0 && sampler <= 31) glActiveTexture(GL_TEXTURE0 + sampler);
        glBindTexture(GL_TEXTURE_CUBE_MAP, cubeTextureID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
    }

    public void clean() {
        unbind();
        glDeleteTextures(cubeTextureID);
    }
}
