package shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.io.*;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;

public abstract class ShaderProgram {
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);;

    public ShaderProgram(String vertexFile, String fragmentFile) {
        vertexShaderID = loadShaders(vertexFile, GL_VERTEX_SHADER);
        fragmentShaderID = loadShaders(fragmentFile, GL_FRAGMENT_SHADER);
        createProgram();
        getAllUniformLocations();
    }

    public void start() {
        glUseProgram(programID);
    }

    public void stop() {
        glUseProgram(0);
    }

    public void clean() {
        stop();
        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragmentShaderID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
        glDeleteProgram(programID);
    }

    protected abstract void bindAttributes();
    protected abstract void getAllUniformLocations();

    protected void bindAttribute(int attribute, String variableName) {
        glBindAttribLocation(programID, attribute, variableName);
    }

    protected void loadInt(int location, int value) { glUniform1i(location, value); }

    protected void loadFloat(int location, float value) {
        glUniform1f(location, value);
    }

    protected void loadVector(int location, Vector3f vector) {
        glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void loadBoolean(int location, boolean value) {
        float toLoad = 0;
        if(value) toLoad = 1;
        glUniform1f(location, toLoad);
    }

    protected void loadMatrix(int location, Matrix4f matrix) {
        matrix.store(matrixBuffer); matrixBuffer.flip();
        glUniformMatrix4fv(location, false, matrixBuffer);
    }

    protected int getUniformLocation(String uniformName) {
        return glGetUniformLocation(programID, uniformName);
    }

    private void createProgram() {
        programID = glCreateProgram();
        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        glLinkProgram(programID);
        checkStatus(GL_LINK_STATUS);
        glValidateProgram(programID);
        checkStatus(GL_VALIDATE_STATUS);
    }

    private void checkStatus(int type) {
        if(glGetProgrami(programID, type) == GL_FALSE) {
            System.err.println(glGetProgramInfoLog(programID));
            System.exit(-1);
        }
    }

    private static int loadShaders(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            InputStream inputStream = ShaderProgram.class.getResourceAsStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);

        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader");
            System.exit(-1);
        }
        return shaderID;
    }
}
