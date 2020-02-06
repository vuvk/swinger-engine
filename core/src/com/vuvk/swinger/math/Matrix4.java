package com.vuvk.swinger.math;

import com.vuvk.swinger.utils.ArrayUtils;
import java.util.Arrays;

/**
 *
 * @author Shcherbatykh
 */
public class Matrix4 {
    private double[] data = new double[16];

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
    
    public void identity() {
        data[1]  = data[2]  = data[3]  = data[4]  =
        data[6]  = data[7]  = data[8]  = data[9]  =
        data[11] = data[12] = data[13] = data[14] = 0.0;

        data[0] = data[5] = data[10] = data[15] = 1.0;
    }
    
    public Matrix4 cpy() {        
        Matrix4 tmp = new Matrix4();
        ArrayUtils.copy(data, 0, tmp.data, 0, data.length);
        return tmp;        
    }

    public Matrix4 mul(double scalar) {
        Matrix4 tmp = this.cpy();
        for (int i = 0; i < data.length; ++i) {
            tmp.data[i] *= scalar;            
        }
        return tmp;
    }
    
    public Vector4 mul(final Vector4 vec) {
    /*
        | a b c d |   | x |     | a*x + b*y + c*z + d*w |
        | e f g h | * | y |  =  | e*x + f*y + g*z + h*w |
        | i j k l |   | z |     | i*x + j*y + k*z + l*w |
        | m n o q |   | w |     | m*x + n*y + o*z + q*w |
    */

        double[] temp = new double[4];
        double[] result = new double[4];

        for (int i = 0; i < 4; ++i) {
            temp[i] = data[i]      * vec.x +
                      data[4 + i]  * vec.y +
                      data[8 + i]  * vec.z +
                      data[12 + i] * vec.w;
        }

        ArrayUtils.copy(temp, 0, result, 0, temp.length);

        if (result[3] != 0.0 && result[3] != 1.0) {
            double w;
            if (temp[3] != 0.0)
                w = 1.0 / temp[3];
            else
                w = 1.0;

            for (int i = 0; i < 4; ++i) {
                result[i] *= w;
            }
        }

        return new Vector4(result);
    }
    
    public Matrix4 mul(Matrix4 other) {
        Matrix4 tmp = new Matrix4();
	for (int col = 0; col < 4; ++col) {
            for (int row = 0; row < 4; ++row) {
                for (int i = 0; i < 4; ++i) {
                    tmp.data[col * 4 + row] += data[i * 4 + row] * other.data[col * 4 + i];
                }
            }
	}
        return tmp;
    }
    
    public Matrix4 translate(double x, double y, double z) {
        Matrix4 tmp = new Matrix4();
        tmp.identity();
        
        tmp.data[12] = x;
        tmp.data[13] = y;
        tmp.data[14] = z;

        return this.mul(tmp);
    }
    
    public Matrix4 translate(Vector3 vec) {
        return translate(vec.x, vec.y, vec.z);
    }
    
    public Matrix4 scale(double x, double y, double z) {
        Matrix4 tmp = new Matrix4();
        tmp.identity();
        
        tmp.data[0] = x;
        tmp.data[5] = y;
        tmp.data[10] = z;

        return this.mul(tmp);
    }
    
    public Matrix4 scale(Vector3 vec) {
        return scale(vec.x, vec.y, vec.z);
    }
    
    public static Matrix4 frustum(double left, double right, double bottom, double top, double zNear, double zFar) {
	/*if ((right - left) == 0.0 ||
              (top - bottom) == 0.0 ||
              (zFar - zNear) == 0.0) {
		return null;
	}*/
        
        Matrix4 result = new Matrix4();

	result.data[0]  =  2.0 * zNear / (right - left);
        result.data[5]  =  2.0 * zNear / (top - bottom);
        result.data[8]  =  (right + left) / (right - left);
        result.data[9]  =  (top + bottom) / (top - bottom);
        result.data[10] = -(zFar + zNear) / (zFar - zNear);
        result.data[11] = -1.0;
        result.data[14] = -(2.0 * zFar * zNear) / (zFar - zNear);

        return result;
    }

    public static Matrix4 perspective(double fovy, double aspect, double zNear, double zFar) {
        if (fovy <= 0.0 || fovy >= 180.0) {
            return null;
        }

        double xmin, xmax, ymin, ymax;
        ymax =  zNear * Math.tan(fovy * Math.PI / 360.0);
        ymin = -ymax;
        xmin =  ymin * aspect;
        xmax =  ymax * aspect;

        return frustum(xmin, xmax, ymin, ymax, zNear, zFar);
    }

    public static Matrix4 lookAt(double eyeX, double eyeY, double eyeZ, 
                          double targetX, double targetY, double targetZ, 
                          double upX, double upY, double upZ) {
        
        Vector3 forward = new Vector3(targetX - eyeX, targetY - eyeY, targetZ - eyeZ), 
                side, 
                up = new Vector3(upX, upY, upZ);

        forward = forward.normalize();
        side = forward.cross(up).normalize();
        up = side.cross(forward);

        Matrix4 result = new Matrix4();

        result.data[0] =  side.x;
        result.data[1] =  up.x;
        result.data[2] = -forward.x;
        result.data[4] =  side.y;
        result.data[5] =  up.y;
        result.data[6] = -forward.y;
        result.data[8] =  side.z;
        result.data[9] =  up.z;
        result.data[10] = -forward.z;
        result.data[15] = 1.0f;

        return result.translate(-eyeX, -eyeY, -eyeZ);
    }
}
