package org.example.statemachine.domain.statemachine.listener;

import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.statemachine.transition.TransitionKind;

import java.util.Objects;

public class FsmApplicationListener implements StateMachineListener<FsmState, FsmEvent> {
    @Override
    public void stateChanged(final State<FsmState, FsmEvent> from, final State<FsmState, FsmEvent> to) {
//        System.out.println("...  stateChanged");
        if (from != null && from.getId() != null) {
            System.out.println("   ... stateChanged from=[" + from.getId() + "] to [" + to.getId() + "]");
        }
    }

    @Override
    public void stateEntered(final State<FsmState, FsmEvent> state) {
        System.out.println(String.format("   ... stateEntered = [%s]", state.getId()));
    }

    @Override
    public void stateExited(final State<FsmState, FsmEvent> state) {
        System.out.println(String.format("   ... stateExited = [%s]", state.getId()));
    }

    @Override
    public void eventNotAccepted(final Message<FsmEvent> event) {
        System.out.println("   ...  eventNotAccepted: " + event.getPayload());
    }

    @Override
    public void transition(final Transition<FsmState, FsmEvent> transition) {
        System.out.println(String.format("   ... transition from [%s] to [%s] with trigger [%s]",
                Objects.nonNull(transition.getSource()) ? transition.getSource().getId() : null,
                Objects.nonNull(transition.getTarget()) ? transition.getTarget().getId() : null,
                Objects.nonNull(transition.getTrigger()) ? transition.getTrigger().getEvent().name() : null
        ));

    }

    @Override
    public void transitionStarted(final Transition<FsmState, FsmEvent> transition) {
        if (transition.getKind() == TransitionKind.INITIAL) return;
        System.out.println("   ... transitionStarted " + transition.getSource().getId() + " -> " + transition.getTarget().getId());
    }

    @Override
    public void transitionEnded(final Transition<FsmState, FsmEvent> transition) {
        if (transition.getKind() == TransitionKind.INITIAL) return;
        System.out.println("   ...  transitionEnded " + transition.getSource().getId() + " -> " + transition.getTarget().getId());
    }

    @Override
    public void stateMachineStarted(final StateMachine<FsmState, FsmEvent> stateMachine) {
        System.out.println("   ...  stateMachineStarted " + stateMachine.getUuid());
    }

    @Override
    public void stateMachineStopped(final StateMachine<FsmState, FsmEvent> stateMachine) {
        System.out.println("   ... stateMachineStopped " + stateMachine.getUuid());
    }

    @Override
    public void stateMachineError(final StateMachine<FsmState, FsmEvent> stateMachine, Exception exception) {
        System.out.println("   ... stateMachineError");
    }

    @Override
    public void extendedStateChanged(final Object key, final Object value) {
        System.out.println("   ... extendedStateChanged [" + key + " :" + value + "]");
    }

    @Override
    public void stateContext(final StateContext<FsmState, FsmEvent> stateContext) {
//        Boolean exit = stateContext.getExtendedState().get("EXIT", Boolean.class);
//        if (exit != null && exit) {
//            stateContext.getStateMachine().sendEvent(PurchaseEvent.EXIT_EVENT);
//        }
//        System.out.println("...  stateContext");
    }
}
