package math;

import org.joml.Vector4f;
import utils.BufferUtils;

import java.nio.FloatBuffer;
import java.util.Arrays;

public class Matrix4f {
    public static final int SIZE = 16;
    public float[] elements = new float[SIZE];

//    0 1 2 3
//    4 5 6 7
//    8 9 10 11
//    12 13 14 15

    public Matrix4f() {

    }

    public Matrix4f(Vector3f input) {
        Arrays.fill(elements, 0.0f);
        elements[0] = input.x;
        elements[5] = input.y;
        elements[10] = input.z;
    }


    public static Matrix4f identity() {
        Matrix4f m = new Matrix4f();

        Arrays.fill(m.elements, 0.0f);

        m.elements[0] = 1.0f;
        m.elements[5] = 1.0f;
        m.elements[10] = 1.0f;
        m.elements[15] = 1.0f;

        return m;
    }

    public static Matrix4f scale(float sx, float sy, float sz) {
        Matrix4f m = identity();

        // Set scaling values
        m.elements[0] = sx; // Scale X axis
        m.elements[5] = sy; // Scale Y axis
        m.elements[10] = sz; // Scale Z axis

        return m;
    }

    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f m = identity();

        m.elements[0] = 2.0f / (right - left);
        m.elements[5] = 2.0f / (top - bottom);
        m.elements[10] = 2.0f / (near - far);

        m.elements[12] = (right + left) / (left - right);
        m.elements[13] = (bottom + top) / (bottom - top);
        m.elements[14] = (near + far) / (far - near);

        return m;
    }

    public static Matrix4f translate(Vector3f vector) {
        Matrix4f m = identity();

        m.elements[12] = vector.x;
        m.elements[13] = vector.y;
        m.elements[14] = vector.z;

        return m;
    }

    public static Matrix4f rotate(float angle) {
        Matrix4f m = identity();

        float r = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(r);
        float sin = (float) Math.sin(r);

        m.elements[0] = cos;
        m.elements[1] = sin;

        m.elements[4] = -sin;
        m.elements[5] = cos;

        return m;
    }

    public Matrix4f multiply(Matrix4f matrix) {
        Matrix4f m = new Matrix4f();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                float sum = 0.0f;
                for (int e = 0; e < 4; e++) {
                    sum += this.elements[x + e * 4] * matrix.elements[e + y * 4];
                }
                m.elements[x + y * 4] = sum;
            }
        }

        return m;
    }

    public static Matrix4f scale(Vector3f vector) {
        Matrix4f matrix = new Matrix4f();
        matrix.elements[0] = vector.x;
        matrix.elements[5] = vector.y;
        matrix.elements[10] = vector.z;
        return matrix;
    }

    public FloatBuffer toFloatBuffer() {
        return BufferUtils.createFloatBuffer(elements);
    }

    public Matrix4f invert() {
        Matrix4f result = new Matrix4f();

        float[] m = this.elements;

        float t0 = m[0] * m[5] - m[1] * m[4];
        float t1 = m[0] * m[6] - m[2] * m[4];
        float t2 = m[0] * m[7] - m[3] * m[4];
        float t3 = m[1] * m[6] - m[2] * m[5];
        float t4 = m[1] * m[7] - m[3] * m[5];
        float t5 = m[2] * m[7] - m[3] * m[6];
        float t6 = m[8] * m[13] - m[9] * m[12];
        float t7 = m[8] * m[14] - m[10] * m[12];
        float t8 = m[8] * m[15] - m[11] * m[12];
        float t9 = m[9] * m[14] - m[10] * m[13];
        float t10 = m[9] * m[15] - m[11] * m[13];
        float t11 = m[10] * m[15] - m[11] * m[14];

        float det = t0 * t11 - t1 * t10 + t2 * t9 + t3 * t8 - t4 * t7 + t5 * t6;

        float invDet = 1.0f / det;

        result.elements[0]  = (m[5] * t11 - m[6] * t10 + m[7] * t9) * invDet;
        result.elements[1]  = (-m[1] * t11 + m[2] * t10 - m[3] * t9) * invDet;
        result.elements[2]  = (m[13] * t5 - m[14] * t4 + m[15] * t3) * invDet;
        result.elements[3]  = (-m[9] * t5 + m[10] * t4 - m[11] * t3) * invDet;
        result.elements[4]  = (-m[4] * t11 + m[6] * t8 - m[7] * t7) * invDet;
        result.elements[5]  = (m[0] * t11 - m[2] * t8 + m[3] * t7) * invDet;
        result.elements[6]  = (-m[12] * t5 + m[14] * t2 - m[15] * t1) * invDet;
        result.elements[7]  = (m[8] * t5 - m[10] * t2 + m[11] * t1) * invDet;
        result.elements[8]  = (m[4] * t10 - m[5] * t8 + m[7] * t6) * invDet;
        result.elements[9]  = (-m[0] * t10 + m[1] * t8 - m[3] * t6) * invDet;
        result.elements[10] = (m[12] * t4 - m[13] * t2 + m[15] * t0) * invDet;
        result.elements[11] = (-m[8] * t4 + m[9] * t2 - m[11] * t0) * invDet;
        result.elements[12] = (-m[4] * t9 + m[5] * t7 - m[6] * t6) * invDet;
        result.elements[13] = (m[0] * t9 - m[1] * t7 + m[2] * t6) * invDet;
        result.elements[14] = (-m[12] * t3 + m[13] * t1 - m[14] * t0) * invDet;
        result.elements[15] = (m[8] * t3 - m[9] * t1 + m[10] * t0) * invDet;

        return result;
    }

    public Vector3f multiply(Vector3f vector) {
        float x = elements[0] * vector.x + elements[4] * vector.y + elements[8]  * vector.z;
        float y = elements[1] * vector.x + elements[5] * vector.y + elements[9]  * vector.z;
        float z = elements[2] * vector.x + elements[6] * vector.y + elements[10] * vector.z;

        return new Vector3f(x, y, z);
    }
}
