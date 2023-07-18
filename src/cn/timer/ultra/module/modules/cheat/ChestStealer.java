package cn.timer.ultra.module.modules.cheat;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.CombatModule;
import cn.timer.ultra.utils.ultra.TimerUtil;
import cn.timer.ultra.values.Numbers;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class ChestStealer extends CombatModule {
    private final TimerUtil timer = new TimerUtil();
    Numbers<Integer> delay = new Numbers<>("Delay ms", 0, 1000, 50);

    public ChestStealer() {
        super("ChestStealer", Keyboard.KEY_NONE, Category.Cheat);
        addValues(delay);
    }

    @EventTarget
    public void update(EventPreUpdate e) {
        if (mc.theWorld == null || isFullInv()) {
            return;
        }
        if (((mc.thePlayer.openContainer instanceof ContainerChest))) {
            ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
            for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
                if ((chest.getLowerChestInventory().getStackInSlot(i) != null) && (this.timer.delay(delay.getValue()))) {
                    mc.playerController.windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
                    this.timer.reset();
                }
            }
            for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
                if (chest.getLowerChestInventory().getStackInSlot(i) != null) {
                    return;
                }
            }
            mc.thePlayer.closeScreen();
        }
    }

    public boolean isInventoryFull() {
        for (int index = 9; index < 44; ++index) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }

    private boolean isChestEmpty(final GuiChest chest) {
        for (int index = 0; index < chest.inventorySlots.inventorySlots.size(); ++index) {
            final ItemStack stack = chest.inventorySlots.inventoryItemStacks.get(index);
            if (stack != null) {
                return false;
            }
        }
        return true;
    }

    public boolean isFullInv() {
        for (int i = 9; i < 45; ++i) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack == null || itemStack.getUnlocalizedName().contains("air")) {
                return false;
            }
        }
        return true;
    }
}
