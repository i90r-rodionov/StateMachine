package org.example.fsm.statemachine.event;

public enum FsmEvent {
    CHECK_READY_TO_PRINT,
    CHECK_READY_TO_SIGN,
    SIGN,
    ECM_CALLBACK,
    PEGA_CALLBACK,
    EXIT_EVENT,
    SLA_EVENT;

}
