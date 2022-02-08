package org.example.fsm.statemachine.guard;

public class Flags {

    private Flags() {
        // nothings
    }

    private static boolean noSignFlag;
    private static boolean createdFolderFlag;
    private static boolean movedFilesFlag;


    public static boolean isNoSignFlag() {
        return noSignFlag;
    }

    public static void setNoSignFlag(boolean noSignFlag) {
        Flags.noSignFlag = noSignFlag;
    }

    public static boolean isCreatedFolderFlag() {
        return createdFolderFlag;
    }

    public static void setCreatedFolderFlag(boolean createdFolderFlag) {
        Flags.createdFolderFlag = createdFolderFlag;
    }

    public static boolean isMovedFilesFlag() {
        return movedFilesFlag;
    }

    public static void setMovedFilesFlag(boolean movedFilesFlag) {
        Flags.movedFilesFlag = movedFilesFlag;
    }
}
