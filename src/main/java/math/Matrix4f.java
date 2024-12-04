package math;

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


    public FloatBuffer toFloatBuffer() {
        return BufferUtils.createFloatBuffer(elements);
    }
}
