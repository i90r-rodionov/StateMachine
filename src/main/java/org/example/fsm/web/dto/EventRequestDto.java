package org.example.fsm.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fsm.statemachine.event.FsmEvent;

@Getter
@NoArgsConstructor
public class EventRequestDto extends RequestDto<FsmEvent> {
    private String id;
    private String eventName;

    public EventRequestDto(String rqId, String id, String nameEvent) {
        super(rqId, null);
        this.id = id;
        this.eventName = nameEvent;
    }

    public EventRequestDto(String rqId, FsmEvent body, String id) {
        super(rqId, body);
        this.id = id;
    }
}
