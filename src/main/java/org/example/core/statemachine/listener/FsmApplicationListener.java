package org.example.core.statemachine.listener;

import lombok.extern.slf4j.Slf4j;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.statemachine.transition.TransitionKind;

import java.util.Objects;

@Slf4j
public class FsmApplicationListener implements StateMachineListener<FsmState, FsmEvent> {
    @Override
    public void stateChanged(final State<FsmState, FsmEvent> from, final State<FsmState, FsmEvent> to) {
//        log.debug("...  stateChanged");
        if (from != null && from.getId() != null) {
            log.debug("   ... stateChanged from=[{}] to [{}]", from.getId(), to.getId());
        }
    }

    @Override
    public void stateEntered(final State<FsmState, FsmEvent> state) {
        log.debug("   ... stateEntered = [{}]", state.getId());
    }

    @Override
    public void stateExited(final State<FsmState, FsmEvent> state) {
        log.debug("   ... stateExited = [{}]", state.getId());
    }

    @Override
    public void eventNotAccepted(final Message<FsmEvent> event) {
        log.debug("   ...  eventNotAccepted: " + event.getPayload());
    }

    @Override
    public void transition(final Transition<FsmState, FsmEvent> transition) {
        if (transition.getKind() == TransitionKind.INITIAL) return;
        log.debug("   ... transition from [{}] to [{}] with trigger [{}]",
                Objects.nonNull(transition.getSource()) ? transition.getSource().getId() : null,
                Objects.nonNull(transition.getTarget()) ? transition.getTarget().getId() : null,
                Objects.nonNull(transition.getTrigger().getEvent()) ? transition.getTrigger().getEvent().name() : null
        );

    }

    @Override
    public void transitionStarted(final Transition<FsmState, FsmEvent> transition) {
        if (transition.getKind() == TransitionKind.INITIAL) return;
        log.debug("   ... transitionStarted [{}] -> [{}] [{}]",
                transition.getSource().getId(), transition.getTarget().getId(),
                transition.getTrigger());
    }

    @Override
    public void transitionEnded(final Transition<FsmState, FsmEvent> transition) {
        if (transition.getKind() == TransitionKind.INITIAL) return;
        String eventName = Objects.nonNull(transition.getTrigger().getEvent()) ? transition.getTrigger().getEvent().name() : "";
        log.debug("   ... transitionEnded [{}] -> [{}] event=[{}]",
                transition.getSource().getId(), transition.getTarget().getId(),
                eventName);
    }

    @Override
    public void stateMachineStarted(final StateMachine<FsmState, FsmEvent> stateMachine) {
        log.debug("   ... stateMachineStarted {}", stateMachine.getUuid());
    }

    @Override
    public void stateMachineStopped(final StateMachine<FsmState, FsmEvent> stateMachine) {
        log.debug("   ... stateMachineStopped {}", stateMachine.getUuid());
    }

    @Override
    public void stateMachineError(final StateMachine<FsmState, FsmEvent> stateMachine, Exception exception) {
        log.debug("   ... stateMachineError");
    }

    @Override
    public void extendedStateChanged(final Object key, final Object value) {
        log.debug("   ... extendedStateChanged [{}]:[{}]", key, value);
    }

    @Override
    public void stateContext(final StateContext<FsmState, FsmEvent> stateContext) {
//        Boolean exit = stateContext.getExtendedState().get("EXIT", Boolean.class);
//        if (exit != null && exit) {
//            stateContext.getStateMachine().sendEvent(PurchaseEvent.EXIT_EVENT);
//        }
//        log.debug(String.format("   ... stateContext=[{}]", stateContext));
    }
}
