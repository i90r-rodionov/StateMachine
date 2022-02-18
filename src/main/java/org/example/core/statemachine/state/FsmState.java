package org.example.core.statemachine.state;

public enum FsmState {
    /* Основные статусы */
    CREATED,
    READY_TO_PRINT,
    READY_TO_SIGN,
    SIGNED,
    ECM_FOLDER_CREATED,
    ECM_SEND,
    ECM_RECEIVE,
    PEGA_SEND,
    PEGA_RECEIVE,
    WAIT_BANK_SIGN,
    BANK_SIGNED,
    BUSINESS_ERROR,
    SLA_ERROR,

    /* Псевдо статусы, используются для реализации переходов по условию */

    // Проверка приложений на наличие подписи
    CHECK_FILES_SIGNED,
    // Проверка доставки файлов в ECM
    CHECK_FILES_DELIVERED,
    // Проверка возможности повторной отправки файлов
    CHECK_FILES_RESEND,
    CHECK_TASK_CREATED,
    CHECK_PROCESSED,
    CHECK_BANK_SIGN,
    INIT,
    EXIT

}
