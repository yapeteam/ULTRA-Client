package cn.timer.ultra.gui.ClickUI.component.impl;

import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.utils.ultra.color.ColorUtils;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class CustomTextField {

    public String textString;
    public float x;
    public float y;
    public boolean isFocused;
    public boolean isTyping;
    public boolean back;
    public int ticks = 0;
    public int selectedChar;
    public float offset;
    public float newTextWidth;
    public float oldTextWidth;
    public float charWidth;
    public String oldString;
    public StringBuilder stringBuilder;
    private float width;
    private float height;

    public CustomTextField(String text, float width, float height) {
        this.textString = text;
        this.selectedChar = this.textString.length();
        this.width = width;
        this.height = height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    private Color setAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public void draw(float x, float y, int alpha) {
        this.x = x;
        this.y = y;

        if (this.selectedChar > this.textString.length())
            this.selectedChar = this.textString.length();
        else if (this.selectedChar < 0)
            this.selectedChar = 0;

        int selectedChar = this.selectedChar;
        RenderUtil.drawRoundedRect(this.x, this.y + 3f, this.x + width, this.y + height, 1, new Color(241, 241, 241, alpha).getRGB());//115,15
        UniFontLoaders.joystick18.drawString(this.textString, this.x + 1.5f - this.offset, this.y + (height - UniFontLoaders.joystick18.getHeight()) / 2f + 2, setAlpha(Color.GRAY, alpha).getRGB());
        if (this.isFocused) {
            float width = UniFontLoaders.joystick18.getStringWidth(this.textString.substring(0, selectedChar)) + 4;
            float posX = this.x + width - this.offset;
            RenderUtil.drawRect(posX - 2.5f, this.y + 5.5f, posX - 2f, this.y + height - 2.5f, ColorUtils.reAlpha(setAlpha(Color.GRAY, alpha).getRGB(), ticks / 500 % 2 == 0 ? 1f : 0));
        }
        this.tick();
    }

    public void tick() {
        if (isFocused)
            ticks++;
        else
            ticks = 0;
    }

    public void mouseClicked(float mouseX, float mouseY, int mouseID) {
        boolean hovering = RenderUtil.isHovering(this.x, this.y + 3f, this.x + width, this.y + height, mouseX, mouseY);

        if (hovering && mouseID == 0 && !this.isFocused) {
            this.isFocused = true;
            this.selectedChar = this.textString.length();
        } else if (!hovering) {
            this.isFocused = false;
            this.isTyping = false;
        }

    }

    public void keyPressed(int key) {
        if (key == Keyboard.KEY_ESCAPE) {
            this.isFocused = false;
            this.isTyping = false;
        }

        if (this.isFocused) {
            float width;
            float barOffset;
            if (GuiScreen.isKeyComboCtrlV(key)) {
                this.textString = (GuiScreen.getClipboardString());
                return;
            }
            switch (key) {
                case Keyboard.KEY_RETURN:
                    this.isFocused = false;
                    this.isTyping = false;
                    this.ticks = 0;
                    break;
                case Keyboard.KEY_INSERT:
                    Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
                    Transferable clipTf = sysClip.getContents(null);
                    if (clipTf != null) {
                        if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                            try {
                                this.textString = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }

                    this.selectedChar = this.textString.length();
                    width = UniFontLoaders.joystick18.getStringWidth(this.textString.substring(0, this.selectedChar))
                            + 2;
                    barOffset = width - this.offset;
                    if (barOffset > 111F) {
                        this.offset += barOffset - 111F;
                    }

                    break;

                case Keyboard.KEY_BACK:
                    try {
                        if (this.selectedChar <= 0)
                            break;
                        if (this.textString.length() != 0) {
                            oldString = this.textString;
                            stringBuilder = new StringBuilder(oldString);
                            stringBuilder.charAt(this.selectedChar - 1);
                            stringBuilder.deleteCharAt(this.selectedChar - 1);
                            this.textString = ChatAllowedCharacters.filterAllowedCharacters(stringBuilder.toString());
                            --this.selectedChar;
                            if (UniFontLoaders.joystick18.getStringWidth(oldString) + 2 > this.width - 4 && this.offset > 0.0F) {
                                newTextWidth = UniFontLoaders.joystick18.getStringWidth(this.textString) + 2;
                                oldTextWidth = UniFontLoaders.joystick18.getStringWidth(oldString) + 2;
                                charWidth = newTextWidth - oldTextWidth;
                                if (newTextWidth <= this.width - 4 && oldTextWidth - this.width - 4 > charWidth)
                                    charWidth = this.width - 4 - oldTextWidth;

                                this.offset += charWidth;
                            }

                            if (this.selectedChar > this.textString.length()) {
                                this.selectedChar = this.textString.length();
                            }

                            this.ticks = 0;
                        }
                    } catch (Exception ignored) {
                    }
                    break;
                case Keyboard.KEY_HOME:
                    this.selectedChar = 0;
                    this.offset = 0.0F;
                    this.ticks = 0;
                    break;
                case Keyboard.KEY_LEFT:
                    if (this.selectedChar > 0) {
                        --this.selectedChar;
                    }

                    width = UniFontLoaders.joystick18.getStringWidth(this.textString.substring(0, this.selectedChar))
                            + 2;
                    barOffset = width - this.offset;
                    barOffset -= 2.0F;

                    if (barOffset < 0.0F)
                        this.offset += barOffset;
                    this.ticks = 0;
                    break;
                case Keyboard.KEY_RIGHT:
                    if (this.selectedChar < this.textString.length()) {
                        ++this.selectedChar;
                    }

                    width = UniFontLoaders.joystick18.getStringWidth(this.textString.substring(0, this.selectedChar)) + 2;
                    barOffset = width - this.offset;
                    if (barOffset > this.width - 4) {
                        this.offset += barOffset - this.width - 4;
                    }
                    this.ticks = 0;
                    break;
                case Keyboard.KEY_END:
                    this.selectedChar = this.textString.length();
                    width = UniFontLoaders.joystick18.getStringWidth(this.textString.substring(0, this.selectedChar))
                            + 2;
                    barOffset = width - this.offset;
                    if (barOffset > 111F) {
                        this.offset += barOffset - this.width - 4;
                    }
                    this.ticks = 0;
            }
        }
    }

    public void charTyped(char c) {
        if (this.isFocused && ChatAllowedCharacters.isAllowedCharacter(c)) {
            if (!this.isTyping)
                this.isTyping = true;

            oldString = this.textString;
            stringBuilder = new StringBuilder(oldString);
            stringBuilder.insert(this.selectedChar, c);
            this.textString = ChatAllowedCharacters.filterAllowedCharacters(stringBuilder.toString());
            if (this.selectedChar > this.textString.length()) {
                this.selectedChar = this.textString.length();
            } else if (this.selectedChar == oldString.length() && this.textString.startsWith(oldString)) {
                this.selectedChar += this.textString.length() - oldString.length();
            } else {
                ++this.selectedChar;
                float width = UniFontLoaders.joystick18.getStringWidth(this.textString.substring(0, this.selectedChar)) + 2;
                newTextWidth = width - this.offset;
                if (newTextWidth > this.width - 4)
                    this.offset += newTextWidth - this.width - 4;
            }

            newTextWidth = UniFontLoaders.joystick18.getStringWidth(this.textString) + 2;
            oldTextWidth = UniFontLoaders.joystick18.getStringWidth(oldString) + 2;
            if (newTextWidth > this.width - 4) {
                if (oldTextWidth < this.width - 4)
                    oldTextWidth = this.width - 4;

                charWidth = newTextWidth - oldTextWidth;
                if (this.selectedChar == this.textString.length())
                    this.offset += charWidth;
            }
            ticks = 0;
        }
    }
}