package org.example.core.statemachine.event;

public enum FsmEvent {
    CREATE("CREATE"),
    CHECK_READY_TO_PRINT(null),
    CHECK_READY_TO_SIGN(null),
    SIGN("SIGN"),
    ECM_CALLBACK(null),
    PEGA_CALLBACK(null),
    PEGA_STATUS(null),
    NEXT_EVENT(null),
    TIMER_EVENT(""),
    SLA_EVENT(null),
    EXIT_EVENT(null);

    private final String externalCode;

    FsmEvent(String externalCode) {
        this.externalCode = externalCode;
    }

}
