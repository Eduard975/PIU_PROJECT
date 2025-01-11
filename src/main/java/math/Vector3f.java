/*
 * The MIT License (MIT)
 *
 * Copyright © 2015-2017, Heiko Brumme
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package math;

import java.nio.FloatBuffer;


public class Vector3f {

    public float x;
    public float y;
    public float z;


    public Vector3f() {
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public float lengthSquared() {
        return x * x + y * y + z * z;
    }

    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }


    public Vector3f normalize() {
        float length = length();
        return divide(length);
    }

    public Vector3f add(Vector3f other) {
        float x = this.x + other.x;
        float y = this.y + other.y;
        float z = this.z + other.z;
        return new Vector3f(x, y, z);
    }

    public Vector3f scale(float scalar) {
        float x = this.x * scalar;
        float y = this.y * scalar;
        float z = this.z * scalar;
        return new Vector3f(x, y, z);
    }


    public Vector3f divide(float scalar) {
        return scale(1f / scalar);
    }

    
    public void toBuffer(FloatBuffer buffer) {
        buffer.put(x).put(y).put(z);
        buffer.flip();
    }

}
