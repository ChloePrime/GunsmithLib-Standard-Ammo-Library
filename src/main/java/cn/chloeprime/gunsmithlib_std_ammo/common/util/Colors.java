package cn.chloeprime.gunsmithlib_std_ammo.common.util;

import org.joml.Vector4f;

import java.awt.*;

public class Colors {
    public static Color awtFromIntOpaque(int packed) {
        return new Color(packed, false);
    }

    public static Vector4f vecFromAwtOpaque(Color color, Vector4f outputDestination) {
        return outputDestination.set(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1);
    }

    public static Vector4f vecFromIntOpaque(int packed, Vector4f outputDestination) {
        return vecFromAwtOpaque(awtFromIntOpaque(packed), outputDestination);
    }

    public static Color vecToAwt(Vector4f vec) {
        return new Color(ftoi(vec.x()), ftoi(vec.y()), ftoi(vec.z()), ftoi(vec.w()));
    }

    public static int awtToPacked(Color color) {
        return color.getRGB();
    }

    public static int vecToPacked(Vector4f vec) {
        return awtToPacked(vecToAwt(vec));
    }

    private static int ftoi(float color) {
        return (int) ((color + 1e-4F) * 255);
    }
}
