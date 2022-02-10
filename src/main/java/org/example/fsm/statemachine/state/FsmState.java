package org.example.fsm.statemachine.state;

public enum FsmState {
    /* Основные статусы */
    CREATED,
    READY_TO_PRINT,
    READY_TO_SIGN,
    SIGNED,
    ECM_FOLDER,
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
    // Проверка отправленных в ECM файлов
    CHECK_FILES_ECM_SENT,
    // Проверка возможности повторной отправки файлов
    CHECK_FILES_RESEND,
    CHECK_CREATE_TASK,
    CHECK_COMPLETE,
    CHECK_BANK_SIGN,
    INIT,
    EXIT

}
