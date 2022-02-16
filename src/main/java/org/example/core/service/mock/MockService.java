package org.example.core.service.mock;


public interface MockService {

    default void trace(Object object, boolean flag) {
        String methodName = object
                .getClass()
                .getEnclosingMethod()
                .getName();

        System.out.printf("   *** [%s.%s] return [%s]%n", this.getClass().getSimpleName(), methodName, flag);
    }

    default boolean defaultAction() {
        System.out.printf("   *** [%s] Made somethings in Action.%n", this.getClass().getSimpleName());
        return true;
    }

    default boolean defaultErrorAction() {
        System.out.printf("   *** [%s] Made somethings in ErrorAction.%n", this.getClass().getSimpleName());
        return true;
    }


    boolean createFolder();

    boolean moveFiles();
}
