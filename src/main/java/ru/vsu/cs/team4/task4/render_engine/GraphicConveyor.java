package ru.vsu.cs.team4.task4.render_engine;

import ru.vsu.cs.team4.task4.Affine.AffineBuilder;
import ru.vsu.cs.team4.task4.math.Point2f;
import ru.vsu.cs.team4.task4.math.matrix.Matrix4f;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;

public class GraphicConveyor {

    public static Matrix4f rotateScaleTranslate(Vector3f scaleV, Vector3f rotateV, Vector3f translateV) throws Exception {
        AffineBuilder affineBuilder = new AffineBuilder().scale(scaleV.getX(), scaleV.getY(), scaleV.getZ());
        affineBuilder.rotateX(rotateV.getX()).rotateY(rotateV.getY()).rotateZ(rotateV.getZ());
        affineBuilder.move(translateV.getX(), translateV.getY(), translateV.getZ());
        return affineBuilder.build().getMatrix();
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target) {
        return lookAt(eye, target, new Vector3f(0F, 1.0F, 0F));
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target, Vector3f up) {

        Vector3f resultZ = Vector3f.residual(target, eye);
        Vector3f resultX = Vector3f.crossProduct(up, resultZ);
        Vector3f resultY = Vector3f.crossProduct(resultZ, resultX);

        resultX.normalized();
        resultY.normalized();
        resultZ.normalized();

        float[][] matrix = new float[][]{
                {resultX.getX(), resultY.getX(), resultZ.getX(), -resultX.dotProduct(eye)},
                {resultX.getY(), resultY.getY(), resultZ.getY(), -resultY.dotProduct(eye)},
                {resultX.getZ(), resultY.getZ(), resultZ.getZ(), -resultZ.dotProduct(eye)},
                {0, 0, 0, 1}};
        return new Matrix4f(matrix);
    }

    public static Matrix4f perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));
        return new Matrix4f(new float[][]{
                {tangentMinusOnDegree / aspectRatio, 0, 0, 0},
                {0, tangentMinusOnDegree, 0, 0},
                {0, 0, (farPlane + nearPlane) / (farPlane - nearPlane), 2 * farPlane * nearPlane / (nearPlane - farPlane)},
                {0, 0, 1, 0}});
    }

    public static Vector3f multiplyMVPMatrixByVertex(final Matrix4f matrix, final Vector3f vertex) {
        final float x = (vertex.getX() * matrix.get(0,0)) + (vertex.getY() * matrix.get(1,0)) + (vertex.getZ() * matrix.get(2,0)) + matrix.get(3,0);
        final float y = (vertex.getX() * matrix.get(0,1)) + (vertex.getY() * matrix.get(1,1)) + (vertex.getZ() * matrix.get(2,1)) + matrix.get(3,1);
        final float z = (vertex.getX() * matrix.get(0,2)) + (vertex.getY() * matrix.get(1,2)) + (vertex.getZ() * matrix.get(2,2)) + matrix.get(3,2);
        final float w = (vertex.getX() * matrix.get(0,3)) + (vertex.getY() * matrix.get(1,3)) + (vertex.getZ() * matrix.get(2,3)) + matrix.get(3,3);
        return new Vector3f(x / w, y / w, z / w);
    }

    public static Point2f vertexToPoint(final Vector3f vertex, final int width, final int height) {
        System.out.println(vertex.getX() + " " + vertex.getY());
        return new Point2f((int) (vertex.getX() * width + width / 2.0F), (int) (-vertex.getY() * height + height / 2.0F));
    }
}
