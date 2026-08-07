package cn.chloeprime.gunsmithlib_std_ammo.common.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Function;

@SuppressWarnings("unused")
public final class CompoundUtil {
    public static Optional<Boolean> optGetBoolean(CompoundTag compound, String key) {
        return compound.contains(key, Tag.TAG_ANY_NUMERIC) ? Optional.of(compound.getBoolean(key)) : Optional.empty();
    }

    public static OptionalInt optGetInt(CompoundTag compound, String key) {
        return compound.contains(key, Tag.TAG_ANY_NUMERIC) ? OptionalInt.of(compound.getInt(key)) : OptionalInt.empty();
    }

    public static OptionalLong optGetLong(CompoundTag compound, String key) {
        return compound.contains(key, Tag.TAG_ANY_NUMERIC) ? OptionalLong.of(compound.getLong(key)) : OptionalLong.empty();
    }

    public static OptionalDouble optGetDouble(CompoundTag compound, String key) {
        return compound.contains(key, Tag.TAG_ANY_NUMERIC) ? OptionalDouble.of(compound.getDouble(key)) : OptionalDouble.empty();
    }

    public static Optional<String> optGetString(CompoundTag compound, String key) {
        return compound.contains(key, Tag.TAG_STRING) ? Optional.of(compound.getString(key)) : Optional.empty();
    }

    public static Optional<CompoundTag> optGetCompound(CompoundTag compound, String key) {
        return compound.contains(key, Tag.TAG_COMPOUND) ? Optional.of(compound.getCompound(key)) : Optional.empty();
    }

    public static Function<CompoundTag, Optional<CompoundTag>> mapCompound(String key) {
        return tag -> optGetCompound(tag, key);
    }

    private CompoundUtil() {
    }
}
