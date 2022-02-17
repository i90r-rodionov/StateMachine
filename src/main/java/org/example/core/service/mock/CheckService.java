package org.example.core.service.mock;


/**
 * Проверки выполнения условий для изменения статуса заявки
 */
public interface CheckService {

    boolean checkReadyToPrint();
    boolean checkReadyToSign();
    boolean checkSignedFiles();
    boolean checkCreatedFolder();
    boolean checkMovedFiles();
    boolean checkDeliveredFiles();
    boolean checkResendFiles();
    boolean checkCreatedTask();


    default boolean defaultAction() {
        System.out.printf("   *** [%s] Made somethings in Action.%n", this.getClass().getSimpleName());
        return true;
    }

    default boolean defaultErrorAction() {
        System.out.printf("   *** [%s] Made somethings in ErrorAction.%n", this.getClass().getSimpleName());
        return true;
    }



    boolean checkSla();

}
