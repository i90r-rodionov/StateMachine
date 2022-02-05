package org.example.statemachine.domain.statemachine.action;

import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachineMessageHeaders;
import org.springframework.statemachine.action.Action;

public class TimerAction implements Action<FsmState, FsmEvent> {
    @Override
    public void execute(StateContext<FsmState, FsmEvent> stateContext) {
        System.out.println("   ### " + this.getClass().getSimpleName() + " execute");
        System.out.println("   --- context: " + stateContext);
        System.out.println("   --- SM: " + stateContext.getStateMachine());


        System.out.println("   --- EXIT_EVENT");
        Message<FsmEvent> eventMessage = MessageBuilder
                .withPayload(FsmEvent.EXIT_EVENT)
                .setHeader(StateMachineMessageHeaders.HEADER_DO_ACTION_TIMEOUT, 5000)
                .build();
        stateContext.getStateMachine().sendEvent(eventMessage);

        //stateContext.getStateMachine().sendEvent(FsmEvent.EXIT_EVENT);
    }
}
