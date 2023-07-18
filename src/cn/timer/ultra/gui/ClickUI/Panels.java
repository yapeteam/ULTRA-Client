package cn.timer.ultra.gui.ClickUI;

public enum Panels {
    Modules(0, "Feature"),
    MusicPlayer(1, "MusicPlayer");
    public final String name;
    final int index;

    Panels(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public static Panels get(int index) {
        for (Panels value : Panels.values()) {
            if (value.getIndex() == index)
                return value;
        }
        return null;
    }
}
