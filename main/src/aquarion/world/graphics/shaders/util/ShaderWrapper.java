package aquarion.world.graphics.shaders.util;

import arc.graphics.Color;
import arc.graphics.gl.Shader;
import arc.math.Mat;
import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import arc.util.Reflect;
import mindustry.graphics.Shaders;

import java.nio.FloatBuffer;

public abstract class ShaderWrapper extends Shaders.LoadShader {

    public Shader wrapped;

    public ShaderWrapper(Shader wrapped) {
        super("screenspace", "screenspace");
        this.wrapped = wrapped;
    }

    @Override
    public void apply() {
        wrapped.apply();
    }

    @Override
    public String getLog() {
        return wrapped.getLog();
    }

    @Override
    public boolean isCompiled() {
        return wrapped == null || wrapped.isCompiled();
    }

    @Override
    public int fetchUniformLocation(String name, boolean pedantic) {
        return wrapped.fetchUniformLocation(name, pedantic);
    }

    @Override
    public void setUniformi(String name, int value) {
        wrapped.setUniformi(name, value);
    }

    @Override
    public void setUniformi(int location, int value) {
        wrapped.setUniformi(location, value);
    }

    @Override
    public void setUniformi(String name, int value1, int value2) {
        wrapped.setUniformi(name, value1, value2);
    }

    @Override
    public void setUniformi(int location, int value1, int value2) {
        wrapped.setUniformi(location, value1, value2);
    }

    @Override
    public void setUniformi(String name, int value1, int value2, int value3) {
        wrapped.setUniformi(name, value1, value2, value3);
    }

    @Override
    public void setUniformi(int location, int value1, int value2, int value3) {
        wrapped.setUniformi(location, value1, value2, value3);
    }

    @Override
    public void setUniformi(String name, int value1, int value2, int value3, int value4) {
        wrapped.setUniformi(name, value1, value2, value3, value4);
    }

    @Override
    public void setUniformi(int location, int value1, int value2, int value3, int value4) {
        wrapped.setUniformi(location, value1, value2, value3, value4);
    }

    @Override
    public void setUniformf(String name, float value) {
        wrapped.setUniformf(name, value);
    }

    @Override
    public void setUniformf(int location, float value) {
        wrapped.setUniformf(location, value);
    }

    @Override
    public void setUniformf(String name, float value1, float value2) {
        wrapped.setUniformf(name, value1, value2);
    }

    @Override
    public void setUniformf(int location, float value1, float value2) {
        wrapped.setUniformf(location, value1, value2);
    }

    @Override
    public void setUniformf(String name, float value1, float value2, float value3) {
        wrapped.setUniformf(name, value1, value2, value3);
    }

    @Override
    public void setUniformf(int location, float value1, float value2, float value3) {
        wrapped.setUniformf(location, value1, value2, value3);
    }

    @Override
    public void setUniformf(String name, float value1, float value2, float value3, float value4) {
        wrapped.setUniformf(name, value1, value2, value3, value4);
    }

    @Override
    public void setUniformf(int location, float value1, float value2, float value3, float value4) {
        wrapped.setUniformf(location, value1, value2, value3, value4);
    }

    @Override
    public void setUniform1fv(String name, float[] values, int offset, int length) {
        wrapped.setUniform1fv(name, values, offset, length);
    }

    @Override
    public void setUniform1fv(int location, float[] values, int offset, int length) {
        wrapped.setUniform1fv(location, values, offset, length);
    }

    @Override
    public void setUniform2fv(String name, float[] values, int offset, int length) {
        wrapped.setUniform2fv(name, values, offset, length);
    }

    @Override
    public void setUniform2fv(int location, float[] values, int offset, int length) {
        wrapped.setUniform2fv(location, values, offset, length);
    }

    @Override
    public void setUniform3fv(String name, float[] values, int offset, int length) {
        wrapped.setUniform3fv(name, values, offset, length);
    }

    @Override
    public void setUniform3fv(int location, float[] values, int offset, int length) {
        wrapped.setUniform3fv(location, values, offset, length);
    }

    @Override
    public void setUniform4fv(String name, float[] values, int offset, int length) {
        wrapped.setUniform4fv(name, values, offset, length);
    }

    @Override
    public void setUniform4fv(int location, float[] values, int offset, int length) {
        wrapped.setUniform4fv(location, values, offset, length);
    }

    @Override
    public void setUniformMatrix(String name, Mat matrix) {
        wrapped.setUniformMatrix(name, matrix);
    }

    @Override
    public void setUniformMatrix(String name, Mat matrix, boolean transpose) {
        wrapped.setUniformMatrix(name, matrix, transpose);
    }

    @Override
    public void setUniformMatrix(int location, Mat matrix) {
        wrapped.setUniformMatrix(location, matrix);
    }

    @Override
    public void setUniformMatrix(int location, Mat matrix, boolean transpose) {
        wrapped.setUniformMatrix(location, matrix, transpose);
    }

    @Override
    public void setUniformMatrix4(String name, float[] val) {
        wrapped.setUniformMatrix4(name, val);
    }

    @Override
    public void setUniformMatrix4(String name, Mat mat) {
        wrapped.setUniformMatrix4(name, mat);
    }

    @Override
    public void setUniformMatrix4(String name, Mat mat, float near, float far) {
        wrapped.setUniformMatrix4(name, mat, near, far);
    }

    @Override
    public void setUniformMatrix3fv(String name, FloatBuffer buffer, int count, boolean transpose) {
        wrapped.setUniformMatrix3fv(name, buffer, count, transpose);
    }

    @Override
    public void setUniformMatrix4fv(String name, FloatBuffer buffer, int count, boolean transpose) {
        wrapped.setUniformMatrix4fv(name, buffer, count, transpose);
    }

    @Override
    public void setUniformMatrix4fv(int location, float[] values, int offset, int length) {
        wrapped.setUniformMatrix4fv(location, values, offset, length);
    }

    @Override
    public void setUniformMatrix4fv(String name, float[] values, int offset, int length) {
        wrapped.setUniformMatrix4fv(name, values, offset, length);
    }

    @Override
    public void setUniformf(String name, Vec2 values) {
        wrapped.setUniformf(name, values);
    }

    @Override
    public void setUniformf(int location, Vec2 values) {
        wrapped.setUniformf(location, values);
    }

    @Override
    public void setUniformf(String name, Vec3 values) {
        wrapped.setUniformf(name, values);
    }

    @Override
    public void setUniformf(int location, Vec3 values) {
        wrapped.setUniformf(location, values);
    }

    @Override
    public void setUniformf(String name, Color values) {
        wrapped.setUniformf(name, values);
    }

    @Override
    public void setUniformf(int location, Color values) {
        wrapped.setUniformf(location, values);
    }

    @Override
    public void bind() {
        wrapped.bind();
    }

    @Override
    public void dispose() {
        wrapped.dispose();
    }

    @Override
    public boolean isDisposed() {
        return wrapped.isDisposed();
    }

    @Override
    public void disableVertexAttribute(String name) {
        wrapped.disableVertexAttribute(name);
    }

    @Override
    public boolean hasAttribute(String name) {
        return wrapped.hasAttribute(name);
    }

    @Override
    public int getAttributeType(String name) {
        return wrapped.getAttributeType(name);
    }

    @Override
    public int getAttributeLocation(String name) {
        return wrapped.getAttributeLocation(name);
    }

    @Override
    public int getAttributeSize(String name) {
        return wrapped.getAttributeSize(name);
    }

    @Override
    public boolean hasUniform(String name) {
        return wrapped.hasUniform(name);
    }

    @Override
    public int getUniformType(String name) {
        return wrapped.getUniformType(name);
    }

    @Override
    public int getUniformLocation(String name) {
        return wrapped.getUniformLocation(name);
    }

    @Override
    public int getUniformSize(String name) {
        return wrapped.getUniformSize(name);
    }

    @Override
    public String[] getAttributes() {
        return wrapped.getAttributes();
    }

    @Override
    public String[] getUniforms() {
        return wrapped.getUniforms();
    }

    @Override
    public String getVertexShaderSource() {
        return wrapped.getVertexShaderSource();
    }

    @Override
    public String getFragmentShaderSource() {
        return wrapped.getFragmentShaderSource();
    }
}
