package cn.timer.ultra.gui.tabgui;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import cn.timer.ultra.Client;
import cn.timer.ultra.module.Category;

public class TabGUI {

    public int currentCategory;
    public int currentModule;
    public boolean viewingCats = true;
    public float seenTrans;
    public boolean showModules;


    public void keyRight() {
        if (viewingCats) {
            if (!showModules) {
                showModules = true;
            } else {
                if (Client.instance.getModuleManager().getByCategory(Category.get(currentCategory)).size() != 0)
                    Client.instance.getModuleManager().getByCategory(Category.get(currentCategory)).get(cats.get(currentCategory).selectedIndex).Enable();
            }
        }
    }

    public void keyLeft() {
        if (viewingCats) {
            if (showModules) {
                showModules = false;
            }
        }
    }

    public void keyDown() {
        if (viewingCats) {
            if (!showModules) {
                if (currentCategory < cats.size() - 1) {
                    currentCategory++;
                } else {
                    currentCategory = 0;
                }
            } else {
                if (cats.get(currentCategory).selectedIndex < Client.instance.getModuleManager().getByCategory(Category.get(currentCategory)).size() - 1) {
                    cats.get(currentCategory).selectedIndex++;
                } else {
                    cats.get(currentCategory).selectedIndex = 0;
                }
            }
        }
    }

    public void keyUp() {
        if (viewingCats) {
            if (!showModules) {
                if (currentCategory > 0) {
                    currentCategory--;
                } else {
                    currentCategory = cats.size() - 1;
                }
            } else {
                if (Objects.requireNonNull(Category.get(currentCategory)).selectedIndex > 0) {
                    Objects.requireNonNull(Category.get(currentCategory)).selectedIndex--;
                } else {
                    Objects.requireNonNull(Category.get(currentCategory)).selectedIndex = Client.instance.getModuleManager().getByCategory(Category.get(currentCategory)).size() - 1;
                }
            }
        }
    }

    public List<Category> cats = Arrays.asList(Category.values());
}
