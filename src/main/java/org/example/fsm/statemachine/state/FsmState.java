package org.example.fsm.statemachine.state;

public enum FsmState {
    CREATE,
    READY_TO_PRINT,
    READY_TO_SIGN,
    CHECK_SIGN,
    SIGNED,
    ECM_FOLDER,
    ECM_SEND,
    ECM_RECEIVE,
    PEGA_SEND,
    PEGA_RECEIVE,
    ST1,
    ST2,
    ST3,
    ERROR,
    SLA_ERROR,
    EXIT,

}
