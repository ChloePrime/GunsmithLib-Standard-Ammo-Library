package cn.chloeprime.gunsmithlib_std_ammo.common.entity;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MarkerPetBase extends TamableAnimal {
    protected MarkerPetBase(EntityType<? extends MarkerPetBase> type, Level level) {
        super(type, level);
        moveControl = new MoveControl(this) {
            @Override
            public void tick() {
            }
        };
        lookControl = new LookControl(this) {
            @Override
            public void tick() {
            }
        };
        jumpControl = new JumpControl(this) {
            @Override
            public void tick() {
                super.tick();
            }
        };
    }

    // TamableAnimal

    @Override
    protected void spawnTamingParticles(boolean tamed) {
    }

    @Override
    public boolean isInSittingPose() {
        return false;
    }

    @Override
    public void setInSittingPose(boolean sitting) {
    }

    @Override
    public void tame(Player owner) {
        this.setTame(true);
        this.setOwnerUUID(owner.getUUID());
    }

    @Override
    public boolean isOrderedToSit() {
        return false;
    }

    @Override
    public void setOrderedToSit(boolean pOrderedToSit) {
    }

    // Animal

    @Override
    public double getMyRidingOffset() {
        return getEyeHeight();
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return false;
    }

    @Override
    public boolean canFallInLove() {
        return false;
    }

    @Override
    public boolean isInLove() {
        return false;
    }

    @Override
    public void setInLove(@Nullable Player pPlayer) {
    }

    @Override
    public boolean canMate(Animal pOtherAnimal) {
        return false;
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel pLevel, Animal pMate) {
        super.spawnChildFromBreeding(pLevel, pMate);
    }

    // AgeableMob

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return null;
    }

    // PathfinderMob

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType type) {
        return true;
    }

    @Override
    public boolean isPathFinding() {
        return false;
    }

    @Override
    protected void tickLeash() {
    }

    @Override
    protected boolean shouldStayCloseToLeashHolder() {
        return false;
    }

    // Mob

    @Override
    protected BodyRotationControl createBodyControl() {
        return new BodyRotationControl(this) {
            @Override
            public void clientTick() {
            }
        };
    }

    @Override
    public int getExperienceReward() {
        return 0;
    }

    @Override
    protected void updateControlFlags() {
    }

    @Override
    protected Vec3i getPickupReach() {
        return Vec3i.ZERO;
    }

    @Override
    public ItemStack equipItemIfPossible(ItemStack stack) {
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean canReplaceCurrentItem(ItemStack pCandidate, ItemStack pExisting) {
        return false;
    }

    @Override
    public boolean canReplaceEqualItem(ItemStack pCandidate, ItemStack pExisting) {
        return false;
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        return false;
    }

    @Override
    public void checkDespawn() {
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader pLevel) {
        return true;
    }

    @Override
    public Iterable<ItemStack> getHandSlots() {
        return Collections.emptyList();
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
    }

    @Override
    public boolean canTakeItem(ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player user, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    public boolean canBeLeashed(Player s) {
        return false;
    }

    @Override
    public boolean isWithinRestriction() {
        return false;
    }

    @Override
    public void dropLeash(boolean pBroadcastPacket, boolean pDropLeash) {
    }

    @Override
    public @Nullable Entity getLeashHolder() {
        return null;
    }

    @Override
    public boolean isEffectiveAi() {
        return false;
    }

    // LivingEntity

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void kill() {
        remove(Entity.RemovalReason.KILLED);
    }

    @Override
    protected void doPush(Entity ignored) {
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public void die(DamageSource cause) {
        remove(RemovalReason.KILLED);
    }

    // Entity

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public Vec3 getDeltaMovement() {
        return Vec3.ZERO;
    }

    @Override
    public void setDeltaMovement(Vec3 velocity) {
    }

    // Marker

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected boolean couldAcceptPassenger() {
        return false;
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void push(Entity target) {
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }
}
