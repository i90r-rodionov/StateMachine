package org.example.statemachine.domain.statemachine.guard;

public class Flags {

    private Flags() {
        // nothings
    }

    private static boolean noSignFlag;
    private static boolean createFolderFlag;

    public static boolean isNoSignFlag() {
        return noSignFlag;
    }

    public static boolean isCreateFolderFlag() {
        return createFolderFlag;
    }

    public static void setNoSignFlag(boolean noSignFlag) {
        Flags.noSignFlag = noSignFlag;
    }

    public static void setCreateFolderFlag(boolean createFolderFlag) {
        Flags.createFolderFlag = createFolderFlag;
    }
}
