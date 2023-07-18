package cn.timer.ultra.module.modules.cheat;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.CombatModule;
import cn.timer.ultra.utils.ultra.TimerUtil;
import cn.timer.ultra.values.Booleans;
import cn.timer.ultra.values.Numbers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C16PacketClientStatus;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AutoArmor extends CombatModule {
    private final Numbers<Integer> Delay;
    private final Booleans OpenInv;
    private final Booleans Drop;
    private final TimerUtil timer;
    private final List<String> Slots;
    Minecraft mc;

    public AutoArmor() {
        super("AutoArmor", Keyboard.KEY_NONE, Category.Cheat);
        this.Delay = new Numbers<>("Delay", 0, 5000, 50);
        this.OpenInv = new Booleans("OpenInv", false);
        this.Drop = new Booleans("Drop Useless Armor", true);
        this.timer = new TimerUtil();
        this.Slots = new CopyOnWriteArrayList<>();
        this.mc = Minecraft.getMinecraft();
        this.setKey(48);
        addValues(Delay);
        addValues(OpenInv);
        addValues(Drop);
    }

    @Override
    public void onEnable() {
        this.Slots.clear();
    }

    public boolean isBestArmor(final ItemStack stack, final int type) {
        final float protection = getProtection(stack);
        String strType = "";
        if (type == 1) {
            strType = "Helmet";
        } else if (type == 2) {
            strType = "ChestPlate";
        } else if (type == 3) {
            strType = "Leggings";
        } else if (type == 4) {
            strType = "Boots";
        }
        if (!stack.getUnlocalizedName().contains(strType.toLowerCase())) {
            return false;
        }
        for (int i = 5; i < 45; ++i) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getProtection(is) > protection && is.getUnlocalizedName().contains(strType.toLowerCase())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static float getProtection(final ItemStack stack) {
        float protection = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            final ItemArmor armor = (ItemArmor) stack.getItem();
            protection += (float) (armor.damageReduceAmount + (100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075);
            protection += (float) (EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0);
            protection += (float) (EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0);
            protection += (float) (EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0);
            protection += (float) (EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0);
            protection += (float) (EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, stack) / 100.0);
        }
        return protection;
    }

    @EventTarget
    public void update(EventPreUpdate e) {
        if (this.OpenInv.getValue() && !(this.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if (this.timer.delay(this.Delay.getValue()) && !(this.mc.thePlayer.openContainer instanceof ContainerChest)) {
            this.getBestArmor();
        }
    }

    public void getBestArmor() {
        if (!this.Slots.isEmpty()) {
            for (final String j : this.Slots) {
                if (this.timer.delay(this.Delay.getValue())) {
                    try {
                        this.drop(Integer.parseInt(j));
                        this.Slots.remove(j);
                        this.timer.reset();
                        if (this.Delay.getValue() > 0L) {
                            return;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {
                    }
                }
            }
        }
        for (int type = 1; type < 5; ++type) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
                final ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                if (this.isBestArmor(is, type)) {
                    continue;
                }
                if (!this.OpenInv.getValue()) {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                }
                this.Slots.add(String.valueOf(4 + type));
            }
            for (int i = 9; i < 45; ++i) {
                if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    final ItemStack is2 = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (is2.getItem() instanceof ItemArmor) {
                        if (this.isBestArmor(is2, type) && getProtection(is2) > 0.0f) {
                            this.shiftClick(i);
                            this.timer.reset();
                            if (this.Delay.getValue() > 0L) {
                                return;
                            }
                        } else {
                            this.Slots.add(String.valueOf(i));
                        }
                    }
                }
            }
        }
    }

    public void shiftClick(final int slot) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, this.mc.thePlayer);
    }

    public void drop(final int slot) {
        if (this.Drop.getValue()) {
            this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, this.mc.thePlayer);
        }
    }
}
