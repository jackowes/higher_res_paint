package xerca.xercapaint.common;


public enum CanvasType {
    SMALL, LARGE, LONG, TALL;

    public static CanvasType fromByte(byte x) {
        return switch (x) {
            case 0 -> SMALL;
            case 1 -> LARGE;
            case 2 -> LONG;
            case 3 -> TALL;
            default -> null;
        };
    }

    public static int getWidth(CanvasType canvasType){
        return switch (canvasType) {
            case SMALL, TALL -> 32;
            case LARGE, LONG -> 64;
        };
    }

    public static int getHeight(CanvasType canvasType){
        return switch (canvasType) {
            case SMALL, LONG -> 32;
            case LARGE, TALL -> 64;
        };
    }
}
