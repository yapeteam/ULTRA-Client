package cn.timer.ultra.protection;

public abstract class Data {
    public byte[] blazz;

    public abstract Data load();

    public byte[] hexStringToBytes(String hexString) {
        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(hexString.substring(index, index + 2), 16);
            bytes[i] = (byte) j;
        }
        return bytes;
    }
}
