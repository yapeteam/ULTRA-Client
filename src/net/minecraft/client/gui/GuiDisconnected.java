package net.minecraft.client.gui;

import cn.timer.ultra.utils.ultra.render.RenderUtil;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

public class GuiDisconnected extends GuiScreen {
    private final String reason;
    private final IChatComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;
    private final boolean ban;

    public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, IChatComponent chatComp) {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey);
        this.message = chatComp;
        this.ban = chatComp.getFormattedText().toLowerCase().contains("ban") || chatComp.getFormattedText().toLowerCase().contains("kick") || chatComp.getFormattedText().contains("封禁") || chatComp.getFormattedText().contains("踢出") || chatComp.getFormattedText().contains("处罚");
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    Player player = null;

    public void initGui() {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu")));
        if (player != null) {
            player.close();
            player = null;
        }
        if (ban) {
            try {
                player = new Player(new BufferedInputStream(mc.mcDefaultResourcePack.getInputStream(new ResourceLocation("client/sounds/bgm.mp3"))));
            } catch (JavaLayerException | IOException e) {
                throw new RuntimeException(e);
            }
            new Thread(() -> {
                try {
                    player.play();
                } catch (JavaLayerException ignored) {
                }
            }).start();
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (ban)
            RenderUtil.drawImage(new ResourceLocation("client/fun/BackgroundImage.png"), 0, 0, width, height, 1);
        else this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
        int i = this.height / 2 - this.field_175353_i / 2;

        if (this.multilineMessage != null) {
            for (String text : this.multilineMessage) {
                this.drawCenteredString(this.fontRendererObj, text, this.width / 2, i, 16777215);
                i += this.fontRendererObj.FONT_HEIGHT;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        if (player != null)
            player.close();
        player = null;
    }
}
