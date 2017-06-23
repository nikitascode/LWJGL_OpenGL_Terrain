package model;

public class TexturedModel {

    EntityModel model;
    ModelTexture texture;
    CubeTextureModel cubeTextureModel;

    public TexturedModel(EntityModel model, ModelTexture texture) {
        this.model = model;
        this.texture = texture;
    }

    public TexturedModel(EntityModel model, CubeTextureModel textureModel) {
        this.model = model;
        this.cubeTextureModel = textureModel;
    }

    public EntityModel getModel() {
        return model;
    }

    public ModelTexture getTexture() {
        return texture;
    }
}
