package cn.chloeprime.gunsmithlib_std_ammo.common.entity;

import cn.chloeprime.commons.async.TaskScheduler;
import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import cn.chloeprime.gunsmithlib_std_ammo.common.GSACommonConfig;
import cn.chloeprime.gunsmithlib_std_ammo.common.GSASoundEvents;
import cn.chloeprime.gunsmithlib_std_ammo.common.entity.ai.GunfightGoal;
import cn.chloeprime.gunsmithlib_std_ammo.common.entity.ai.GunfightMob;
import cn.chloeprime.gunsmithlib_std_ammo.common.item.GSABulletPriceDatabase;
import cn.chloeprime.gunsmithlib_std_ammo.common.item.GSAVillagerTrades;
import cn.chloeprime.gunsmithlib_std_ammo.common.rpg.GSADamageTypeTags;
import cn.chloeprime.gunsmithlib_std_ammo.common.util.CompoundUtil;
import com.google.common.base.Suppliers;
import com.tacz.guns.api.item.IAttachment;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.api.item.builder.AttachmentItemBuilder;
import com.tacz.guns.api.item.builder.GunItemBuilder;
import com.tacz.guns.api.item.gun.FireMode;
import mod.chloeprime.gunsmithlib.api.util.Gunsmith;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.function.Supplier;

public class BulletMerchant extends AbstractVillager implements GunfightMob {
    public static final ResourceLocation SKIN_NORMAL_VERSION = GunsmithLibStdAmmoMod.loc("textures/entity/bullet_merchant/skin_normal.png");
    public static final ResourceLocation SKIN_PRIMER_VERSION = GunsmithLibStdAmmoMod.loc("textures/entity/bullet_merchant/skin_primer.png");

    @ApiStatus.Experimental
    public static final double PRIMER_PROBABILITY = 0.1;
    
    public static final EntityType<BulletMerchant> TYPE = EntityType.Builder
            .of(BulletMerchant::new, MobCategory.CREATURE)
            .sized(0.6F, 1.8F)
            .clientTrackingRange(10)
            .build("bullet_merchant");

    public BulletMerchant(EntityType<? extends AbstractVillager> type, Level level) {
        super(type, level);
        reassessWeaponGoal();
    }

    public long getDespawnProtection() {
        return despawnProtection;
    }

    public boolean isPrimerVersion() {
        return this.entityData.get(IS_PRIMER_VERSION);
    }

    public void setDespawnProtection(long despawnProtection) {
        this.despawnProtection = despawnProtection;
    }

    public void setIsPrimerVersion(boolean value) {
        this.entityData.set(IS_PRIMER_VERSION, value);
    }

    public ResourceLocation getSkinLocation() {
        return isPrimerVersion() ? SKIN_PRIMER_VERSION : SKIN_NORMAL_VERSION;
    }

    private static final EntityDataAccessor<Boolean> IS_PRIMER_VERSION = SynchedEntityData.defineId(BulletMerchant.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_PRIMER_VERSION, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        // this.goalSelector.addGoal(1, new PanicGoal(this, 0.5));
        this.goalSelector.addGoal(1, new LookAtTradingPlayerGoal(this));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.35));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.35));
        this.goalSelector.addGoal(9, new InteractGoal(this, Player.class, 3, 1));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Zombie.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Evoker.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Vindicator.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Vex.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Pillager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Illusioner.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Zoglin.class, true));
    }

    // Defending

    public boolean shouldDodge(DamageSource source) {
        return (getTarget() != null || source.getEntity() instanceof Player) && isMeleeAttack(source);
    }

    public void dodge(DamageSource source) {
        var direct = source.getDirectEntity();
        var actual = source.getEntity();
        if (direct == null && actual == null) {
            this.jumpControl.jump();
            return;
        }
        var entity = Objects.requireNonNullElse(direct, actual);
        var normal = entity.position().subtract(this.position()).with(Direction.Axis.Y, 0).normalize();
        if (normal.equals(Vec3.ZERO)) {
            this.jumpControl.jump();
            return;
        }
        final double strafeStrength = 0.5;
        this.setDeltaMovement(normal.yRot((float) ((getRandom().nextInt(2) * 2 - 1) * Math.PI / 2)).scale(strafeStrength));
        this.jumpControl.jump();
        if (level() instanceof ServerLevel srvLevel) {
            srvLevel.sendParticles(ParticleTypes.POOF, getX(), getY(0.25), getZ(), 16, 0.0, 0.0, 0.0, 1.0);
            srvLevel.playSound(null, this, GSASoundEvents.BULLET_MERCHANT_DODGE.get(), getSoundSource(), 1, getVoicePitch());
        }
    }

    @Override
    public boolean hurt(@Nonnull DamageSource source, float amount) {
        if (source.getEntity() == this) {
            return false;
        }
        if (level().getDifficulty() == Difficulty.PEACEFUL) {
            if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                return false;
            }
        }
        if (!level().isClientSide()) {
            // 进入战斗状态后闪避近战攻击
            if (!source.isCreativePlayer() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                if (shouldDodge(source)) {
                    dodge(source);
                    return false;
                }
            }
        }
        var oow = source.is(DamageTypes.FELL_OUT_OF_WORLD);
        return super.hurt(source, amount * (oow ? 25 : 1));
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean isMeleeAttack(DamageSource source) {
        var direct = source.getDirectEntity();
        var actual = source.getEntity();
        if (direct != actual || direct == null) {
            return false;
        }
        if (source.is(DamageTypeTags.IS_PROJECTILE) || source.is(GSADamageTypeTags.TACZ_BULLETS)) {
            return false;
        }
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        var level = Objects.requireNonNull(level());
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            if (level.getGameTime() % 20 == 0) {
                if (isAlive() && getHealth() < getMaxHealth() / 4) {
                    heal(getMaxHealth() / 20);
                    serverLevel.sendParticles(
                            ParticleTypes.HAPPY_VILLAGER,
                            getX(), getY() + 1, getZ(), 20,
                            0.3, 0.5, 0.3, 0);
                }
            }
            tickDespawnProtection();
        }
    }

    private static final Supplier<ItemStack> WEAPON_TEMPLATE = Suppliers.memoize(BulletMerchant::createEquippedWeapon);

    private static ItemStack createEquippedWeapon() {
        var laser = AttachmentItemBuilder.create()
                .setId(ResourceLocation.tryParse("tacz:laser_nightstick"))
                .build();
        if (laser.getItem() instanceof IAttachment athInterface) {
            athInterface.setLaserColor(laser, 0xFF2B2B);
        }
        var gun = Gunsmith.getGunInfo(GunItemBuilder.create()
                .setId(GunsmithLibStdAmmoMod.loc("scar_h_m1158"))
                .putAttachment(AttachmentType.EXTENDED_MAG, ResourceLocation.tryParse("tacz:ammo_mod_he"))
                .setAmmoCount(25)
                .setAmmoInBarrel(true)
                .setFireMode(FireMode.AUTO)
                .forceBuild()).orElse(null);
        if (gun == null) {
            return ItemStack.EMPTY;
        }
        gun.gunItem().installAttachment(gun.gunStack(), laser);
        return gun.gunStack();
    }

    private int isRangedMode = -1;
    private final GunfightGoal<BulletMerchant> shootGoal = new GunfightGoal<>(this, 0.25, 32.0F);
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1, false) {
        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            super.stop();
            BulletMerchant.this.setAggressive(false);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            super.start();
            BulletMerchant.this.setAggressive(true);
        }
    };

    public static AttributeSupplier.Builder createBulletMerchantAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 500)
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.ARMOR_TOUGHNESS, 6)
                .add(Attributes.FOLLOW_RANGE, 64)
                .add(Attributes.MOVEMENT_SPEED, 0.5);
    }

    public void reassessWeaponGoal() {
        // noinspection ConstantValue
        if (this.level() != null && !this.level().isClientSide) {
            int isNewModeRanged = GunfightGoal.isGun(getMainHandItem()) || GunfightGoal.isGun(getOffhandItem()) ? 1 : 0;
            if (isNewModeRanged != isRangedMode) {
                if (isNewModeRanged != 0) {
                    this.goalSelector.removeGoal(this.meleeGoal);
                    this.goalSelector.addGoal(5, this.shootGoal);
                } else {
                    this.goalSelector.removeGoal(this.shootGoal);
                    this.goalSelector.addGoal(5, this.meleeGoal);
                }
                isRangedMode = isNewModeRanged;
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public @Nonnull SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        var result = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        if (this.random.nextFloat() < PRIMER_PROBABILITY) {
            this.setIsPrimerVersion(true);
        }
        this.populateDefaultEquipmentSlots(level().getRandom(), pDifficulty);
        this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
        this.setGuaranteedDrop(EquipmentSlot.OFFHAND);
        this.reassessWeaponGoal();
        return result;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        setItemSlot(EquipmentSlot.OFFHAND, WEAPON_TEMPLATE.get().copy());
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        CompoundUtil.optGetBoolean(compound, "is_primer_version").ifPresent(this::setIsPrimerVersion);
        CompoundUtil.optGetLong(compound, "despawn_protection").ifPresent(this::setDespawnProtection);
        reassessWeaponGoal();
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("is_primer_version", isPrimerVersion());
        compound.putLong("despawn_protection", getDespawnProtection());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        super.setItemSlot(slot, stack);
        if (!level().isClientSide()) {
            tryReassessWeaponGoal();
        }
    }

    // Fix CME

    private static final TaskScheduler SCHEDULER = TaskScheduler.createTickBased(LogicalSide.SERVER);
    private int tickingAi;

    @Override
    public void serverAiStep() {
        try {
            tickingAi++;
            super.serverAiStep();
        } finally {
            tickingAi--;
        }
    }

    public void tryReassessWeaponGoal() {
        if (tickingAi == 0) {
            reassessWeaponGoal();
        } else {
            SCHEDULER.withCondition(this::isAlive).delay(1).thenRun(this::reassessWeaponGoal);
        }
    }

    // Trading

    @Override
    @ParametersAreNonnullByDefault
    public @Nullable AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected @Nonnull InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.isAlive() && !this.isTrading() && !this.isBaby() && !isAggressive()) {
            if (hand == InteractionHand.MAIN_HAND) {
                player.awardStat(Stats.TALKED_TO_VILLAGER);
            }
            if (!this.getOffers().isEmpty() && !this.level().isClientSide()) {
                this.setTradingPlayer(player);
                this.openTradingScreen(player, this.getDisplayName(), 1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    protected void rewardTradeXp(@Nonnull MerchantOffer offer) {
        if (offer.shouldRewardExp()) {
            int amount = 3 + this.random.nextInt(4);
            this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY() + 0.5, this.getZ(), amount));
        }
    }

    @Override
    protected void updateTrades() {
        var random = this.getRandom();
        int count;
        if (isPrimerVersion()) {
            count = 10;
        } else {
            count = 5;
            for (int i = 0; i < 3; i++) {
                if (random.nextFloat() < 0.5) {
                    count += 1;
                }
            }
        }
        var offers = this.getOffers();
        this.addOffersFromItemListings(offers, GSABulletPriceDatabase.listings(), count);
        this.addOffersFromItemListings(offers, EXTRA_TRADES, 1);
    }

    private static final VillagerTrades.ItemListing[] EXTRA_TRADES = {
            GSAVillagerTrades.AutocannonTradingRecipe.INSTANCE,
    };

    // Despawn Control

    public static final long DESPAWN_DELAY = 1200;
    private long despawnProtection = GSACommonConfig.BM_PERSIST_TIME.get();

    private void tickDespawnProtection() {
        if (despawnProtection > 0) {
            despawnProtection--;
        }
        if (despawnProtection <= DESPAWN_DELAY - 400) {
            if (requiresCustomPersistence()) {
                despawnProtection = DESPAWN_DELAY;
            }
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        if (level().isClientSide()) {
            return false;
        }
        return despawnProtection <= 0 && super.removeWhenFarAway(distanceToClosestPlayer);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return isLeashed() || super.requiresCustomPersistence();
    }

    // Sounds

    @Override
    public float getVoicePitch() {
        return isBaby() ? super.getVoicePitch() : Mth.lerp(0.75F, super.getVoicePitch() + 0.2F, 1);
    }

    @Override
    public @Nullable SoundEvent getAmbientSound() {
        return (isTrading() ? GSASoundEvents.BULLET_MERCHANT_TRADE : GSASoundEvents.BULLET_MERCHANT_AMBIENT).get();
    }

    @Override
    public @Nullable SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return GSASoundEvents.BULLET_MERCHANT_HURT.get();
    }

    @Override
    public @Nullable SoundEvent getDeathSound() {
        return GSASoundEvents.BULLET_MERCHANT_DEATH.get();
    }

    @Override
    public @Nonnull SoundEvent getTradeUpdatedSound(boolean yes) {
        return (yes ? GSASoundEvents.BULLET_MERCHANT_YES : GSASoundEvents.BULLET_MERCHANT_NO).get();
    }

    @Override
    public @Nonnull SoundEvent getNotifyTradeSound() {
        return GSASoundEvents.BULLET_MERCHANT_YES.get();
    }

    // Misc

    @Override
    public boolean canBeLeashed(@Nonnull Player player) {
        return true;
    }

    @Override
    public @Nonnull Vec3 getLeashOffset() {
        Vec3 superman = super.getLeashOffset();
        return new Vec3(superman.x(), superman.y() * 0.85, superman.z());
    }
}
