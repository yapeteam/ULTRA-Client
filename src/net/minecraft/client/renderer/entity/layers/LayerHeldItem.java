package net.minecraft.client.renderer.entity.layers;

import cn.timer.ultra.Client;
import cn.timer.ultra.module.modules.player.MoBendsMod;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class LayerHeldItem implements LayerRenderer<EntityLivingBase> {
    private final RendererLivingEntity<?> livingEntityRenderer;

    public LayerHeldItem(RendererLivingEntity<?> livingEntityRendererIn) {
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    public void doRenderLayer(EntityLivingBase entity, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        ItemStack itemstack = entity.getHeldItem();

        if (itemstack != null) {
            GlStateManager.pushMatrix();

            if (this.livingEntityRenderer.getMainModel().isChild) {
                float f = 0.5F;
                GlStateManager.translate(0.0F, 0.625F, 0.0F);
                GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
                GlStateManager.scale(f, f, f);
            }
            boolean moBendsEnabled = Client.instance.getModuleManager().getByClass(MoBendsMod.class).isEnabled();

            if (entity instanceof EntityPlayer) {
                if (((EntityPlayer) entity).isBlocking() && !moBendsEnabled) {
                    float itemScale = 0.0325f;

                    float glScale = 1.05f;
                    float glAngle = 24405.0f;
                    float glAngleX = 137290.0f;
                    float glAngleY = 2009900.0f;
                    float glAngleZ = 2654900.0f;

                    if (entity.isSneaking()) {
                        ((ModelBiped) livingEntityRenderer.getMainModel()).postRenderArm(itemScale);
                        GlStateManager.scale(glScale, glScale, glScale);
                        GlStateManager.translate(-0.58f, 0.32f, -0.07f);
                        GlStateManager.rotate(-glAngle, glAngleX, -glAngleY, -glAngleZ);
                    } else {
                        ((ModelBiped) livingEntityRenderer.getMainModel()).postRenderArm(itemScale);
                        GlStateManager.scale(glScale, glScale, glScale);
                        GlStateManager.translate(-0.45f, 0.25f, -0.07f);
                        GlStateManager.rotate(-glAngle, glAngleX, -glAngleY, -glAngleZ);
                    }
                } else {
                    ((ModelBiped) livingEntityRenderer.getMainModel()).postRenderArm(0.0625f);
                }

                GlStateManager.translate(-0.0625f, 0.4375f, 0.0625f);
            } else {
                ((ModelBiped) livingEntityRenderer.getMainModel()).postRenderArm(0.0625f);
                GlStateManager.translate(-0.0625f, 0.4375f, 0.0625f);
            }

            if (entity instanceof EntityPlayer && ((EntityPlayer) entity).fishEntity != null) {
                itemstack = new ItemStack(Items.fishing_rod, 0);
            }

            Item item = itemstack.getItem();
            Minecraft minecraft = Minecraft.getMinecraft();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item).getRenderType() == 2) {
                GlStateManager.translate(0.0F, 0.1875F, -0.3125F);
                GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                float f1 = 0.375F;
                GlStateManager.scale(-f1, -f1, f1);
            }

            if (entity.isSneaking() && !moBendsEnabled) {
                GlStateManager.translate(0.0F, 0.203125F, 0.0F);
            }

            minecraft.getItemRenderer().renderItem(entity, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON);
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
