package cn.chloeprime.gunsmithlib_std_ammo.common.entity;

import cn.chloeprime.gunsmithlib_std_ammo.client.entity.SlicingWarheadClient;
import cn.chloeprime.gunsmithlib_std_ammo.common.util.Basis;
import com.google.gson.annotations.SerializedName;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tacz.guns.api.GunProperties;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.resource.pojo.data.gun.ExtraDamage;
import mod.chloeprime.gunsmithlib.api.util.GunInfo;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SlicingWarhead extends MarkerPetBase {
    public record Properties(
            @SerializedName("damage")           float damage,
            @SerializedName("armor_piercing")   float armorPiercing,
            @SerializedName("period")           int period,
            @SerializedName("times")            int times
    ) {
        public static final Codec<Properties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("damage").forGetter(Properties::damage),
                Codec.FLOAT.fieldOf("armor_piercing").forGetter(Properties::armorPiercing),
                Codec.INT.fieldOf("period").forGetter(Properties::period),
                Codec.INT.fieldOf("times").forGetter(Properties::times)
        ).apply(instance, Properties::new));

        public static Properties fromGunshot(
                GunInfo gun, LivingEntity shooter,
                float damage, int period, int times
        ) {
            var dataHolder = IGunOperator.fromLivingEntity(shooter).getDataHolder();
            var iGun = gun.gunItem();
            var origAp = Optional.ofNullable(gun.index().getBulletData().getExtraDamage())
                    .map(ExtraDamage::getArmorIgnore)
                    .orElse(0F);
            var ap = iGun.modifyProperty(dataHolder, gun.gunStack(), shooter, GunProperties.ARMOR_IGNORE, Float.class, origAp);
            return new Properties(damage, ap, period, times);
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class AttributeInit {
        @SubscribeEvent
        public static void onInitAttributes(EntityAttributeCreationEvent event) {
            event.put(TYPE, createMobAttributes().build());
        }
    }

    private static final EntityDataAccessor<Integer> DATA_TARGET = SynchedEntityData.defineId(SlicingWarhead.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Vector3f> DATA_RELATIVE_POS = SynchedEntityData.defineId(SlicingWarhead.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Vector3f> DATA_NORMAL = SynchedEntityData.defineId(SlicingWarhead.class, EntityDataSerializers.VECTOR3);
    private int attackCount;
    private @Nullable UUID targetId;
    private @Nullable Properties properties;

    public static final EntityType<SlicingWarhead> TYPE = EntityType.Builder
            .<SlicingWarhead>of(SlicingWarhead::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(5)
            .fireImmune()
            .noSave()
            .build("slicing_warhead");

    public SlicingWarhead(EntityType<? extends SlicingWarhead> type, Level level) {
        super(TYPE, level);
    }

    public SlicingWarhead(Level level, Properties properties) {
        this(TYPE, level, properties);
    }

    public SlicingWarhead(EntityType<? extends SlicingWarhead> type, Level level, Properties properties) {
        super(type, level);
        this.properties = properties;
    }

    public Optional<Entity> getSlicingTarget() {
        var level = level();
        if (level.isClientSide()) {
            return Optional.ofNullable(level.getEntity(entityData.get(DATA_TARGET)));
        } else if (level instanceof ServerLevel sl) {
            return Optional.ofNullable(targetId).map(sl::getEntity);
        }
        return Optional.empty();
    }

    public void setSlicingTarget(Entity target) {
        if (level().isClientSide()) {
            return;
        }
        this.targetId = target.getUUID();
        entityData.set(DATA_TARGET, target.getId());
    }

    public Vec3 getHitNormal() {
        var target = getSlicingTarget().orElse(null);
        if (target == null) {
            return new Vec3(0, 1, 0);
        }
        var basis = Basis.fromEntityBody(target);
        var data = entityData.get(DATA_NORMAL);
        return basis.toGlobal(new Vec3(data.x(), data.y(), data.z()));
    }

    public void setHitInfo(Entity target, Vec3 hitPos, Vec3 normal) {
        if (level().isClientSide()) {
            setPos(hitPos);
            return;
        }
        var basis = Basis.fromEntityBody(target);
        var relPos = basis.toLocal(hitPos.subtract(target.position()));
        var relNorm = basis.toLocal(normal);
        entityData.set(DATA_RELATIVE_POS, new Vector3f((float) relPos.x(), (float) relPos.y(), (float) relPos.z()));
        entityData.set(DATA_NORMAL, new Vector3f((float) relNorm.x(), (float) relNorm.y(), (float) relNorm.z()));
    }

    public Optional<UUID> getSlicingTargetId() {
        return Optional.ofNullable(targetId);
    }

    public void setSlicingTargetId(UUID targetId) {
        if (level() instanceof ServerLevel sl) {
            var target = sl.getEntity(targetId);
            if (target != null) {
                setSlicingTarget(target);
                return;
            }
        }
        this.targetId = targetId;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.5F;
    }

    private static final UUID AP_MODIFIER_ID = UUID.fromString("2ab9c03d-75ee-4005-b05d-19677dff61f0");

    public void tick() {
        var level = level();
        var props = this.properties;
        if (props == null && !level.isClientSide()) {
            if (!isRemoved()) {
                discard();
            }
            return;
        }
        var target = getSlicingTarget().orElse(null);
        if (target != null) {
            var basis = Basis.fromEntityBody(target);
            var data = entityData.get(DATA_RELATIVE_POS);
            setPos(target.position().add(basis.toGlobal(new Vec3(data.x(), data.y(), data.z()))));
        }
        if (level.isClientSide()) {
            SlicingWarheadClient.onClientTick(this, getHitNormal());
        } else {
            if (target != null && !target.isAlive()) {
                discard();
                return;
            }
            Objects.requireNonNull(props);
            var life = tickCount;
            if (life % props.period() != 0) {
                return;
            }
            attackCount++;
            if (attackCount > props.times()) {
                discard();
                return;
            }

            if (target != null) {
                gsaAttack(target, props);
            }
        }
    }

    private void gsaAttack(Entity target, Properties props) {
        var source = damageSources().mobAttack(this);
        if (target instanceof LivingEntity victim) {
            var armor = victim.getAttribute(Attributes.ARMOR);
            var tough = victim.getAttribute(Attributes.ARMOR_TOUGHNESS);
            if (armor != null && tough != null) {
                var modifier = new AttributeModifier(AP_MODIFIER_ID, "Armor Piercing", props.armorPiercing - 1, AttributeModifier.Operation.MULTIPLY_TOTAL);
                try {
                    armor.addTransientModifier(modifier);
                    tough.addTransientModifier(modifier);
                    victim.invulnerableTime = 0;
                    victim.hurt(source, props.damage());
                    return;
                } finally {
                    armor.removeModifier(modifier);
                    tough.removeModifier(modifier);
                    victim.invulnerableTime = 0;
                }
            }
        }
        target.hurt(source, props.damage());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag dataSource) {
        super.readAdditionalSaveData(dataSource);
        Optional.ofNullable(dataSource.get("properties"))
                .map(propTag -> Properties.CODEC.decode(NbtOps.INSTANCE, propTag))
                .flatMap(DataResult::result)
                .map(Pair::getFirst)
                .ifPresent(props -> this.properties = props);
        if (dataSource.contains("attack_count", Tag.TAG_INT)) {
            this.attackCount = dataSource.getInt("attack_count");
        }
        if (dataSource.hasUUID("target_id")) {
            setSlicingTargetId(dataSource.getUUID("target_id"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag outTarget) {
        super.addAdditionalSaveData(outTarget);
        Optional.ofNullable(properties)
                .map(prop -> Properties.CODEC.encodeStart(NbtOps.INSTANCE, prop))
                .flatMap(DataResult::result)
                .ifPresent(serialized -> outTarget.put("properties", serialized));
        outTarget.putInt("attack_count", this.attackCount);
        getSlicingTargetId().ifPresent(targetId -> outTarget.putUUID("target_id", targetId));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_TARGET, 0);
        entityData.define(DATA_RELATIVE_POS, new Vector3f(0, 0, 0));
        entityData.define(DATA_NORMAL, new Vector3f(0, 0, 0));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
