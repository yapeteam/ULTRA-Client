package net.optifine.player;

import cn.timer.ultra.Client;
import cn.timer.ultra.module.modules.render.Cape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CapeUtils {
    private static final Pattern PATTERN_USERNAME = Pattern.compile("[a-zA-Z0-9_]+");
    private static final Map<String, ResourceLocation> capes = new HashMap<>();

    public static void downloadCape(AbstractClientPlayer player) {
        String s = player.getNameClear();
        Cape cape = Client.instance.getModuleManager().getByClass(Cape.class);

        if (s != null && !s.isEmpty() && !s.contains("\u0000") && PATTERN_USERNAME.matcher(s).matches()) {
            String s1 = "https://s.optifine.net/capes/" + s + ".png";
            ResourceLocation resourcelocation = new ResourceLocation("capeof/" + s);
            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            ITextureObject itextureobject = texturemanager.getTexture(resourcelocation);
            if (itextureobject == null) {
                String capeloc = cape.mode.getValue();
                resourcelocation = new ResourceLocation("client/capes/" +
                        capeloc +
                        ".png");
                player.setLocationOfCape(resourcelocation);
                return;
            }

            if (itextureobject instanceof ThreadDownloadImageData) {
                ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData) itextureobject;

                if (threaddownloadimagedata.imageFound != null) {
                    if (threaddownloadimagedata.imageFound) {
                        player.setLocationOfCape(resourcelocation);

                        if (threaddownloadimagedata.getImageBuffer() instanceof CapeImageBuffer) {
                            CapeImageBuffer capeimagebuffer = (CapeImageBuffer) threaddownloadimagedata.getImageBuffer();
                            player.setElytraOfCape(capeimagebuffer.isElytraOfCape());
                        }
                    }

                    return;
                }
            }

            CapeImageBuffer capeimagebuffer = new CapeImageBuffer(player, resourcelocation);
            ThreadDownloadImageData threaddownloadimagedata = new ThreadDownloadImageData(null, s1, null, capeimagebuffer);
            threaddownloadimagedata.pipeline = true;
            texturemanager.loadTexture(resourcelocation, threaddownloadimagedata);
        }

    }

    public static BufferedImage parseCape(BufferedImage img) {
        int i = 64;
        int j = 32;
        int k = img.getWidth();

        for (int l = img.getHeight(); i < k || j < l; j *= 2) {
            i *= 2;
        }

        BufferedImage bufferedimage = new BufferedImage(i, j, 2);
        Graphics graphics = bufferedimage.getGraphics();
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();
        return bufferedimage;
    }

    public static boolean isElytraCape(BufferedImage imageRaw, BufferedImage imageFixed) {
        return imageRaw.getWidth() > imageFixed.getHeight();
    }

    public static void ReloadCapesFromLoc() {
        if (Minecraft.getMinecraft().thePlayer == null) return;
        Cape cape = Client.instance.getModuleManager().getByClass(Cape.class);
        capes.clear();
        for (String mode : cape.mode.getModes()) {
            capes.put(mode, new ResourceLocation("client/capes/" + mode + ".png"));
        }
        if (cape.isEnabled())
            Minecraft.getMinecraft().thePlayer.setLocationOfCape(capes.get(cape.mode.getValue()));
    }

    public static void reloadCape(AbstractClientPlayer player) {
        String s = player.getNameClear();
        ResourceLocation resourcelocation = new ResourceLocation("capeof/" + s);
        TextureManager texturemanager = Config.getTextureManager();
        ITextureObject itextureobject = texturemanager.getTexture(resourcelocation);
        Cape cape = Client.instance.getModuleManager().getByClass(Cape.class);
        if (itextureobject == null && player == Minecraft.getMinecraft().thePlayer) {
            String capeloc = cape.mode.getValue();
            resourcelocation = capes.get(capeloc);
            player.setLocationOfCape(resourcelocation);
            return;
        }

        if (itextureobject instanceof SimpleTexture) {
            SimpleTexture simpletexture = (SimpleTexture) itextureobject;
            simpletexture.deleteGlTexture();
            texturemanager.deleteTexture(resourcelocation);
        }

        player.setLocationOfCape(null);
        player.setElytraOfCape(false);

        downloadCape(player);
    }
}
