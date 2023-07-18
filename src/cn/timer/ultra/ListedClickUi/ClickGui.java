package cn.timer.ultra.ListedClickUi;

import cn.timer.ultra.Client;
import cn.timer.ultra.ListedClickUi.component.Component;
import cn.timer.ultra.ListedClickUi.component.Frame;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.modules.render.ClickGUI;
import cn.timer.ultra.utils.ultra.color.ColorUtils;
import cn.timer.ultra.utils.ultra.render.BlurUtil;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;

//Skid from https://github.com/Pandus1337/ClickGUI
public class ClickGui extends GuiScreen {

    public static ArrayList<Frame> frames;
    public static int color = -1;

    public ClickGui() {
        frames = new ArrayList<>();
    }

    public boolean background = false;

    @Override
    public void initGui() {
        if (frames.isEmpty()) {
            int frameX = 50;
            int frameY = 12;
            for (Category category : Category.values()) {
                Frame frame = new Frame(category);
                frame.setX(frameX);
                frame.setY(frameY);
                frames.add(frame);
                frameX += frame.getWidth() + 30;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (Mouse.hasWheel()) {
            int y = Mouse.getDWheel() / 15;
            for (Frame frame : frames) {
                frame.setY(frame.getY() + y);
            }
        }
        boolean isDragging = false;
        for (Frame frame : frames) {
            frame.updatePosition(mouseX, mouseY);
            if (frame.isDragging) {
                isDragging = true;
            }
        }
        if (!isDragging) {
            for (Frame frame : frames) {
                if (frame.isWithinHeader(mouseX, mouseY) && !frame.isDragging && Mouse.isButtonDown(0)) {
                    if (frames.get(frames.size() - 1) != frame) {
                        ArrayList<Frame> list = new ArrayList<>();
                        for (Frame value : frames) {
                            if (value == frame)
                                continue;
                            list.add(value);
                        }
                        frames = list;
                        frames.add(frame);
                    }
                    frame.isDragging = true;
                    frame.dragX = mouseX - frame.getX();
                    frame.dragY = mouseY - frame.getY();
                }
            }
        }

        if (background) {
            GL11.glClear(16384);
            for (int i = 0; i < this.width / 20 + 5; ++i) {
                for (int j = 0; j < this.height / 20 + 5; ++j) {
                    final int color = ColorUtils.rainbow(-(i + j) * 100000000L).getRGB();
                    RenderUtil.drawRect2((float) (i * 20), (float) (j * 20), 20.0f, 20.0f, color);
                }
            }
            BlurUtil.blur(0.0f, 0.0f, (float) this.width, (float) this.height, 3);
        }
        for (Frame frame : frames) {
            frame.renderFrame();
            for (Component comp : frame.getComponents()) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (Frame frame : frames) {
            if (frame.isOpen()) {
                if (!frame.getComponents().isEmpty()) {
                    for (Component component : frame.getComponents()) {
                        component.mouseClicked(mouseX, mouseY, mouseButton);
                    }
                }
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        for (Frame frame : frames) {
            if (frame.isOpen() && keyCode != 1) {
                if (!frame.getComponents().isEmpty()) {
                    for (Component component : frame.getComponents()) {
                        component.keyTyped(typedChar, keyCode);
                    }
                }
            }
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (Frame frame : frames) {
            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
                frame.setOpen(!frame.isOpen());
            }
            frame.setDrag(false);
        }
        for (Frame frame : frames) {
            if (frame.isOpen()) {
                if (!frame.getComponents().isEmpty()) {
                    for (Component component : frame.getComponents()) {
                        component.mouseReleased(mouseX, mouseY, mouseButton);
                    }
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        Client.instance.getModuleManager().getByClass(ClickGUI.class).setEnabled(false);
    }
}
