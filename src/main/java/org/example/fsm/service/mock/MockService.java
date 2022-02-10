package org.example.fsm.service.mock;


public interface MockService {

    default void trace(Object object, boolean flag) {
        String methodName = object
                .getClass()
                .getEnclosingMethod()
                .getName();

        System.out.printf("   *** [%s.%s] return [%s]%n", this.getClass().getSimpleName(), methodName, flag);
    }

    default boolean defaultAction() {
        return true;
    }

    boolean createFolder();

    boolean moveFiles();
}
