package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin
{
	@Inject(at = @At("RETURN"), method = "getDimensions", cancellable = true)
	private void onGetDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> info)
	{
		info.setReturnValue(info.getReturnValue().scaled(ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this), ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this)));
	}
	
	@ModifyArg(method = "tickMovement", index = 1, at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F"))
	private float onTickMovementMinVelocityProxy(float velocity)
	{
		return velocity * ScaleUtils.getMotionScale((Entity) (Object) this);
	}
	
	// FIXME: FIX THIS
	/*@Inject(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", ordinal = 0, shift = Shift.BEFORE, target = "Lnet/minecraft/entity/LivingEntity;travel(Lnet/minecraft/util/math/Vec3d;)V"))
	private void onTravelModifyFlightSpeed(Vec3d movementInput, CallbackInfo info)
	{
		final float scale = ScaleUtils.getFlightScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			((PlayerEntity) (Object) this).airStrafingSpeed *= scale;
		}
	}*/
	
	@Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;setPickupDelay(I)V"))
	private void onDropItem(ItemStack stack, boolean spread, boolean thrown, CallbackInfoReturnable<ItemEntity> info, double y, ItemEntity entity)
	{
		final float scale = ScaleUtils.setScaleOfDrop(entity, (Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			final Vec3d pos = entity.getPos();
			
			entity.setPosition(pos.x, y + ((1.0F - scale) * 0.3D), pos.z);
		}
	}
	
	@ModifyArg(method = "tickMovement()V", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onTickMovementExpandXProxy(double value)
	{
		return value * ScaleUtils.getMotionScale((Entity) (Object) this);
	}
	
	@ModifyArg(method = "tickMovement()V", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onTickMovementExpandYProxy(double value)
	{
		return value * ScaleUtils.getMotionScale((Entity) (Object) this);
	}
	
	@ModifyArg(method = "tickMovement()V", index = 2, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onTickMovementExpandZProxy(double value)
	{
		return value * ScaleUtils.getMotionScale((Entity) (Object) this);
	}
	
	@ModifyConstant(method = "attack(Lnet/minecraft/entity/Entity;)V", constant = { @Constant(floatValue = 0.5F, ordinal = 1), @Constant(floatValue = 0.5F, ordinal = 2), @Constant(floatValue = 0.5F, ordinal = 3) })
	private float onAttackModifyKnockback(float value)
	{
		final float scale = ScaleUtils.getKnockbackScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@Unique private static final ThreadLocal<Float> pehkui$WIDTH_SCALE = ThreadLocal.withInitial(() -> 1.0F);
	@Unique private static final ThreadLocal<Float> pehkui$HEIGHT_SCALE = ThreadLocal.withInitial(() -> 1.0F);
	
	@Inject(method = "attack", at = @At("HEAD"))
	private void onAttack(Entity target, CallbackInfo info)
	{
		pehkui$WIDTH_SCALE.set(ScaleUtils.getBoundingBoxWidthScale(target));
		pehkui$HEIGHT_SCALE.set(ScaleUtils.getBoundingBoxHeightScale(target));
	}
	
	@ModifyArg(method = "attack(Lnet/minecraft/entity/Entity;)V", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onAttackExpandXProxy(double value)
	{
		return value * pehkui$WIDTH_SCALE.get();
	}
	
	@ModifyArg(method = "attack(Lnet/minecraft/entity/Entity;)V", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onAttackExpandYProxy(double value)
	{
		return value * pehkui$HEIGHT_SCALE.get();
	}
	
	@ModifyArg(method = "attack(Lnet/minecraft/entity/Entity;)V", index = 2, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onAttackExpandZProxy(double value)
	{
		return value * pehkui$WIDTH_SCALE.get();
	}
}
