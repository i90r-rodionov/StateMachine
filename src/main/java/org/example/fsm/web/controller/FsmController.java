package org.example.fsm.web.controller;

import org.example.fsm.service.fsm.FsmService;
import org.example.fsm.service.fsm.dto.FlagsDto;
import org.example.fsm.statemachine.guard.Flags;
import org.example.fsm.statemachine.persist.StateHolder;
import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.state.FsmState;
import org.example.fsm.web.dto.EventRequestDto;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@SuppressWarnings("unused")
public class FsmController {

    private final FsmService fsmService;
    private final StateHolder stateHolder;
    private final Flags flags = Flags.getInstance();

    public FsmController(
            FsmService fsmServiceervice,
            StateHolder state
    ) {
        this.fsmService = fsmServiceervice;
        this.stateHolder = state;
    }

    @PostMapping("/test")
    public String test(@RequestBody EventRequestDto request) {
        System.out.println("### 1: " + request.getEventName());

        fsmService.test(request.getId(), FsmEvent.valueOf(request.getEventName()));
        System.out.println("### 2");

        return String.valueOf(stateHolder.getState());
    }

    @GetMapping("/test/{id}/{event}")
    public String test(@PathVariable("id") String id, @PathVariable("event") String event) {
        System.out.println(">>> 1: " + event);

        fsmService.test(id, FsmEvent.valueOf(event.toUpperCase()));
        System.out.println(">>> 2\n\r");

        return String.valueOf(stateHolder.getState());
    }

    @RequestMapping(path = "/restore")
    public String restore() {
        System.out.println("### 91");
        fsmService.restore();
        System.out.println("### 92");

        return String.valueOf(stateHolder.getState());
    }

    @GetMapping(path = "/reset/{state}")
    public String reset(@PathVariable("state") String state) {
        System.out.println("### 101");
        fsmService.reset(state);

        String response = String.format("State=[%s] %s", stateHolder.getState(), flags.getFields());
        System.out.println("### 102 = " + response);
        return response;

    }

    @GetMapping("/flag/{name}/{value}")
    public String flag(@PathVariable("name") String name,
                        @PathVariable("value") String value) {
        System.out.println(">>> Flags");
        fsmService.flag(name, value);

        String response = String.format("State=[%s] %s", stateHolder.getState(), flags.getFields());
        System.out.println(response);
        return response;
    }



    @RequestMapping(path = "/state")
    public String state() {

        Flags flags = Flags.getInstance();

        String response = String.format("State=[%s] %s", stateHolder.getState(), flags.getFields());

        System.out.println(response);
        return response;
    }

    @RequestMapping(path = "/map")
    public String map() {
        Map<UUID, StateMachine<FsmState, FsmEvent>> map = stateHolder.getMap();
        map.forEach((k, v) -> System.out.println(k + " " + v.getState().getId() + " {} " + v.isComplete()));
        System.out.println();
        return map.size() + "";
    }
}
