package org.example.statemachine.web.controller;

import org.example.statemachine.domain.service.fsm.FsmService;
import org.example.statemachine.domain.statemachine.persist.StateHolder;
import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.example.statemachine.web.dto.EventRequestDto;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@SuppressWarnings("unused")
public class FsmController {

    private final FsmService fsmService;
    private final StateHolder state;

    public FsmController(
            FsmService fsmServiceervice,
            StateHolder state
    ) {
        this.fsmService = fsmServiceervice;
        this.state = state;
    }

    @PostMapping("/test")
    public String test(@RequestBody EventRequestDto request) {
        System.out.println("### 1");

        fsmService.test(request.getId(), FsmEvent.valueOf(request.getEventName()));
        System.out.println("### 2");

        return String.valueOf(state.getState());
    }

    @GetMapping("/test/{id}/{event}")
    public String test(@PathVariable("id") String id, @PathVariable("event") String event) {
        System.out.println("### 1");

        fsmService.test(id, FsmEvent.valueOf(event.toUpperCase()));
        System.out.println("### 2\n\r");

        return String.valueOf(state.getState());
    }

    @RequestMapping(path = "/restore")
    public String restore() {
        System.out.println("### 91");
        fsmService.restore();
        System.out.println("### 92");

        return String.valueOf(state.getState());
    }

    @RequestMapping(path = "/state")
    public String state() {
        return String.valueOf(state.getState());
    }

    @RequestMapping(path = "/map")
    public String map() {
        Map<UUID, StateMachine<FsmState, FsmEvent>> map = state.getMap();
        map.forEach((k,v) -> System.out.println(k + " " + v.getState().getId() + " {} " + v.isComplete()));
        System.out.println();
        return map.size() + "";
    }
}
