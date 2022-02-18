package org.example.core.statemachine.action;

import lombok.extern.slf4j.Slf4j;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.persist.StateHolder;
import org.example.core.statemachine.state.FsmState;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import reactor.core.publisher.Mono;

@Slf4j
abstract public class BaseAction implements Action<FsmState, FsmEvent> {

    private final StateHolder stateHolder;

    protected BaseAction(StateHolder stateHolder) {
        this.stateHolder = stateHolder;
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {

        log.debug("   ### execute [{}] sourceState=[{}] targetState [{}]",
                this.getClass().getSimpleName(), context.getSource().getId().name(), context.getTarget().getId().name());

        stateHolder.setState(context.getTarget().getId());
    }

    protected StateHolder getStateHolder() {
        return stateHolder;
    }

    protected void generateError() {
        log.debug("   ### Error in [{}]", this.getClass().getSimpleName());
        throw new RuntimeException(this.getClass().getSimpleName() + " Error");
    }

    protected void sendEvent(final StateContext<FsmState, FsmEvent> context, FsmEvent event) {

        Message<FsmEvent> fsmEventMessage =
                MessageBuilder
                        .withPayload(event)
                        .build();

        context.getStateMachine().sendEvent(Mono.just(fsmEventMessage)).subscribe();

        log.debug("   ### Action sendEvent=[{}]", event.name());
    }

    protected boolean isSetSla(final StateContext<FsmState, FsmEvent> context) {
        return false;
    }
}
