package net.killarexe.littlerage.engine.renderer;

import net.killarexe.littlerage.engine.util.Logger;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    private int shaderProgramID;
    private boolean beingUsed = false;

    private String vertexSource;
    private String fragementSource;
    private String filePath;

    Logger logger = new Logger(getClass());

    public Shader(String filePath) {
        this.filePath = filePath;
        logger.info("Loading Shader: " + filePath);
        try {
            String source = new String(Files.readAllBytes(Path.of(filePath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            //Find the first Pattern
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            //Find the second Pattern
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                fragementSource = splitString[1];
            } else {
                logger.error("Unexpected Token '" + firstPattern + "' in '" + filePath + "'");
                throw new IOException("Unexpected Token '" + firstPattern + "' in '" + filePath + "'");
            }

            if (secondPattern.equals("vertex")) {
                vertexSource = splitString[2];
            } else if (secondPattern.equals("fragement")) {
                fragementSource = splitString[2];
            } else {
                logger.error("Unexpected Token '" + secondPattern + "' in '" + filePath + "'");
                throw new IOException("Unexpected Token '" + secondPattern + "' in '" + filePath + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Couldn't open file for shader '" + filePath + "'";
        }
    }

    public void compile() {
        int vertexID, fragementID;

        //First load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //Pass the shader src to GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        //Check for error
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            logger.error("'" + filePath + "'\n\tVertex shader compilation failed");
            logger.info(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        //First load and compile the fragment shader
        fragementID = glCreateShader(GL_FRAGMENT_SHADER);
        //Pass the shader src to GPU
        glShaderSource(fragementID, fragementSource);
        glCompileShader(fragementID);

        //Check for error
        success = glGetShaderi(fragementID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragementID, GL_INFO_LOG_LENGTH);
            logger.error("'" + filePath + "'\n\tFragement shader compilation failed");
            logger.info(glGetShaderInfoLog(fragementID, len));
            assert false : "";
        }

        //link shaders
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragementID);
        glLinkProgram(shaderProgramID);

        //Check for errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            logger.error("'" + filePath + "'\n\tLinking shaders failed");
            logger.info(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }

    }

    public void use () {if(!beingUsed)glUseProgram(shaderProgramID);beingUsed = true;}

    public void detach () {glUseProgram(0);beingUsed = false;}

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec4f){
        int varLoaction = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varLoaction, vec4f.x, vec4f.y, vec4f.z, vec4f.w);
    }

    public void uploadVec3f(String varName, Vector3f vec3f){
        int varLoaction = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varLoaction, vec3f.x, vec3f.y, vec3f.z);
    }

    public void uploadVec2f(String varName, Vector2f vec2f){
        int varLoaction = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2f(varLoaction, vec2f.x, vec2f.y);
    }

    public void uploadFloat(String varName, float val){
        int varLoaction = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLoaction, val);
    }

    public void uploadInt(String varName, int val){
        int varLoaction = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLoaction, val);
    }

    public void uploadTexture(String varName, int slot){
        int varLoaction = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLoaction, slot);
    }
}
