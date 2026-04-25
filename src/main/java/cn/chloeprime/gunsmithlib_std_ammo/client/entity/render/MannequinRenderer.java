package cn.chloeprime.gunsmithlib_std_ammo.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Score;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * 适用于非玩家实体的玩家模型渲染器。<br/>
 * 不支持的功能：<br/>
 * - 披风<br/>
 * - 肩部实体（鹦鹉）<br/>
 * - 玩家渲染事件（{@link RenderPlayerEvent}）
 * @param <E> 实体类型
 */
public class MannequinRenderer<E extends Mob> extends MobRenderer<E, PlayerModel<E>> {
    public static
    <E extends Mob>
    EntityRendererProvider<E> factory(@Nonnull ResourceLocation skin, boolean slim) {
        Objects.requireNonNull(skin);
        return context -> new MannequinRenderer<>(skin, context, slim);
    }

    public MannequinRenderer(ResourceLocation skin, EntityRendererProvider.Context context, boolean slim) {
        super(context, new PlayerModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim), 0.5F);
        this.skin = Objects.requireNonNull(skin);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidArmorModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidArmorModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new ArrowLayer<>(context, this));
        // Unsupported: CapeLayer;
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new ElytraLayer<>(this, context.getModelSet()));
        // Unsupported: ParrotOnShoulderLayer
        this.addLayer(new SpinAttackEffectLayer<>(this, context.getModelSet()));
        this.addLayer(new BeeStingerLayer<>(this));
    }

    private final @Nonnull ResourceLocation skin;

    @Override
    public @Nonnull ResourceLocation getTextureLocation(@Nonnull E pEntity) {
        return skin;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(E entity, float yaw, float partialTicks, PoseStack pose, MultiBufferSource buffer, int lightCoords) {
        this.setModelProperties(entity);
        super.render(entity, yaw, partialTicks, pose, buffer, lightCoords);
    }

    @Override
    public @Nonnull Vec3 getRenderOffset(E entity, float partialTicks) {
        return entity.isCrouching() ? new Vec3(0, -0.125, 0) : super.getRenderOffset(entity, partialTicks);
    }

    private void setModelProperties(E entity) {
        PlayerModel<E> playermodel = this.getModel();
        if (entity.isSpectator()) {
            playermodel.setAllVisible(false);
            playermodel.head.visible = true;
            playermodel.hat.visible = true;
        } else {
            playermodel.setAllVisible(true);
            playermodel.hat.visible = true;
            playermodel.jacket.visible = true;
            playermodel.leftPants.visible = true;
            playermodel.rightPants.visible = true;
            playermodel.leftSleeve.visible = true;
            playermodel.rightSleeve.visible = true;
            playermodel.crouching = entity.isCrouching();
            HumanoidModel.ArmPose poseMainhand = getArmPose(entity, InteractionHand.MAIN_HAND);
            HumanoidModel.ArmPose poseOffhand = getArmPose(entity, InteractionHand.OFF_HAND);
            if (poseMainhand.isTwoHanded()) {
                poseOffhand = entity.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
            }

            if (entity.getMainArm() == HumanoidArm.RIGHT) {
                playermodel.rightArmPose = poseMainhand;
                playermodel.leftArmPose = poseOffhand;
            } else {
                playermodel.rightArmPose = poseOffhand;
                playermodel.leftArmPose = poseMainhand;
            }
        }
    }

    private static HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand) {
        ItemStack itemstack = entity.getItemInHand(hand);
        if (itemstack.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else {
            if (entity.getUsedItemHand() == hand && entity.getUseItemRemainingTicks() > 0) {
                UseAnim useanim = itemstack.getUseAnimation();
                if (useanim == UseAnim.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }

                if (useanim == UseAnim.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }

                if (useanim == UseAnim.SPEAR) {
                    return HumanoidModel.ArmPose.THROW_SPEAR;
                }

                if (useanim == UseAnim.CROSSBOW && hand == entity.getUsedItemHand()) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }

                if (useanim == UseAnim.SPYGLASS) {
                    return HumanoidModel.ArmPose.SPYGLASS;
                }

                if (useanim == UseAnim.TOOT_HORN) {
                    return HumanoidModel.ArmPose.TOOT_HORN;
                }

                if (useanim == UseAnim.BRUSH) {
                    return HumanoidModel.ArmPose.BRUSH;
                }
            } else if (!entity.swinging && itemstack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(itemstack)) {
                return HumanoidModel.ArmPose.CROSSBOW_HOLD;
            }

            var forgeArmPose = IClientItemExtensions.of(itemstack).getArmPose(entity, hand, itemstack);
            return Objects.requireNonNullElse(forgeArmPose, HumanoidModel.ArmPose.ITEM);
        }
    }

    @Override
    protected void scale(@Nonnull E entity, PoseStack poseStack, float partialTicks) {
        final float scale = 0.9375F;
        poseStack.scale(scale, scale, scale);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void renderNameTag(E entity, Component displayName, PoseStack pose, MultiBufferSource buffer, int lightCoords) {
        double distanceSqr = this.entityRenderDispatcher.distanceToSqr(entity);
        pose.pushPose();
        if (distanceSqr < 100) {
            var scoreboard = entity.level().getScoreboard();
            var objective = scoreboard.getDisplayObjective(2);
            if (objective != null) {
                Score score = scoreboard.getOrCreatePlayerScore(entity.getScoreboardName(), objective);
                super.renderNameTag(entity, Component.literal(Integer.toString(score.getScore())).append(CommonComponents.SPACE).append(objective.getDisplayName()), pose, buffer, lightCoords);
                pose.translate(0, 9 * 1.15F * 0.025F, 0);
            }
        }
        super.renderNameTag(entity, displayName, pose, buffer, lightCoords);
        pose.popPose();
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void setupRotations(E entity, PoseStack pose, float ageInTicks, float yaw, float partialTicks) {
        float swimAmount = entity.getSwimAmount(partialTicks);
        if (entity.isFallFlying()) {
            super.setupRotations(entity, pose, ageInTicks, yaw, partialTicks);
            float fallFlyTicks = (float)entity.getFallFlyingTicks() + partialTicks;
            float fallFlyRot = Mth.clamp(fallFlyTicks * fallFlyTicks / 100, 0, 1);
            if (!entity.isAutoSpinAttack()) {
                pose.mulPose(Axis.XP.rotationDegrees(fallFlyRot * (-90 - entity.getXRot())));
            }

            Vec3 front = entity.getViewVector(partialTicks);
            Vec3 velocity = entity.getDeltaMovement();
            double velX2Z2 = velocity.horizontalDistanceSqr();
            double frontX2Z2 = front.horizontalDistanceSqr();
            if (velX2Z2 > 0 && frontX2Z2 > 0) {
                double cos = (velocity.x * front.x + velocity.z * front.z) / Math.sqrt(velX2Z2 * frontX2Z2);
                double sign = velocity.x * front.z - velocity.z * front.x;
                pose.mulPose(Axis.YP.rotation((float)(Math.signum(sign) * Math.acos(cos))));
            }
        } else if (swimAmount > 0) {
            super.setupRotations(entity, pose, ageInTicks, yaw, partialTicks);
            float swimMaxRot = entity.isInWater() || entity.isInFluidType((fluidType, height) -> entity.canSwimInFluidType(fluidType)) ? -90 - entity.getXRot() : -90;
            float swimRot = Mth.lerp(swimAmount, 0, swimMaxRot);
            pose.mulPose(Axis.XP.rotationDegrees(swimRot));
            if (entity.isVisuallySwimming()) {
                pose.translate(0, -1, 0.3F);
            }
        } else {
            super.setupRotations(entity, pose, ageInTicks, yaw, partialTicks);
        }
    }
}
