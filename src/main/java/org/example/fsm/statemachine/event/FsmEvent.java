package org.example.fsm.statemachine.event;

public enum FsmEvent {
    CREATE("CREATE"),
    CHECK_READY_TO_PRINT(null),
    CHECK_READY_TO_SIGN(null),
    SIGN("SIGN"),
    ECM_CALLBACK_SUCCESSFUL(null),
    ECM_CALLBACK(null),
    PEGA_CALLBACK_FAIL(null),
    PEGA_STATUS_SUCCESSFUL(null),
    PEGA_STATUS_FAIL(null),
    EXIT_EVENT(null);

    private final String externalCode;

    FsmEvent(String externalCode) {
        this.externalCode = externalCode;
    }

}
