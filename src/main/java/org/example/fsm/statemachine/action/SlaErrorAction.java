package org.example.fsm.statemachine.action;

import org.example.fsm.statemachine.state.FsmState;
import org.example.fsm.statemachine.event.FsmEvent;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class SlaErrorAction implements Action<FsmState, FsmEvent> {
    @Override
    public void execute(StateContext<FsmState, FsmEvent> stateContext) {
        System.out.println("   ### " + this.getClass().getSimpleName() + " execute");
        System.out.println("   --- context: " + stateContext);
        System.out.println("   --- SM: " + stateContext.getStateMachine());


        System.out.println("   --- SLA_ERROR");
//        Message<FsmEvent> eventMessage = MessageBuilder
//                .withPayload(FsmEvent.SLA_EVENT)
//                .setHeader(StateMachineMessageHeaders.HEADER_DO_ACTION_TIMEOUT, 5000)
//                .build();
//        //stateContext.getStateMachine().sendEvent(eventMessage);
//
//
//        stateContext.getStateMachine().sendEvent(FsmEvent.SLA_EVENT);
    }
}
