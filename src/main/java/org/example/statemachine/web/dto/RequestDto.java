package org.example.statemachine.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestDto<T> {
    private String rqId;
    private T body;

    public RequestDto(String rqId, T body) {
        this.rqId = rqId;
        this.body = body;
    }
}
