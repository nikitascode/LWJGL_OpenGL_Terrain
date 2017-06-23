package texture;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

public class TextureLoader {

    public static int loadCubeMap(String[] textureFile) {
        int textureCubeId = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, textureCubeId);

        for(int i = 0; i < textureFile.length; i++) {
            TextureData data = decodeTextureFile("/res/"+textureFile[i]+".png");
            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA,
                    data.getWidth(), data.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
                    data.getBuffer());
        }

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
        return textureCubeId;
    }

    private static TextureData decodeTextureFile(String path) {
        int width = 0;
        int height = 0;

        ByteBuffer buffer = null;
        try {
            InputStream in = TextureLoader.class.getResourceAsStream(path);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new TextureData(buffer, width, height);
    }

    public static int loadTexture(String path) {
        ByteBuffer buffer = null;
        int width = 0;
        int height = 0;

        try {
            BufferedImage texture = ImageIO.read(BufferedImage.class.getResourceAsStream(path));
            width = texture.getWidth(); height = texture.getHeight();

            int[] pixels = new int[width * height * 4];
            pixels = texture.getRGB(0, 0, width, height, null, 0, width);
            buffer = BufferUtils.createByteBuffer(width * height * 4);

            int pixel;
            for(int x = 0; x < width; x++) {
                for(int y = 0; y < height; y++) {
                    pixel = pixels[x * width + y];
                    buffer.put((byte)((pixel >> 16) & 0xFF)); //RED
                    buffer.put((byte)((pixel >> 8) & 0xFF));  //GREEN
                    buffer.put((byte)(pixel & 0xFF));         //BLUE
                    buffer.put((byte)((pixel >> 24) & 0xFF)); //ALPHA
                }
            }

            buffer.flip();
        } catch (IOException e) {
            e.printStackTrace();
        }

        glEnable(GL_TEXTURE_2D);
        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
        return textureID;
    }
}
