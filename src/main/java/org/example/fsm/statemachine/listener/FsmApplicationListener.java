package org.example.fsm.statemachine.listener;

import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.state.FsmState;
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
            System.out.printf("   ... stateChanged from=[%s] to [%s]%n", from.getId(), to.getId());
        }
    }

    @Override
    public void stateEntered(final State<FsmState, FsmEvent> state) {
        System.out.printf("   ... stateEntered = [%s]%n", state.getId());
    }

    @Override
    public void stateExited(final State<FsmState, FsmEvent> state) {
        System.out.printf("   ... stateExited = [%s]%n", state.getId());
    }

    @Override
    public void eventNotAccepted(final Message<FsmEvent> event) {
        System.out.println("   ...  eventNotAccepted: " + event.getPayload());
    }

    @Override
    public void transition(final Transition<FsmState, FsmEvent> transition) {
        if (transition.getKind() == TransitionKind.INITIAL) return;
        System.out.printf("   ... transition from [%s] to [%s] with trigger [%s]%n",
                Objects.nonNull(transition.getSource()) ? transition.getSource().getId() : null,
                Objects.nonNull(transition.getTarget()) ? transition.getTarget().getId() : null,
                Objects.nonNull(transition.getTrigger().getEvent()) ? transition.getTrigger().getEvent().name() : null
        );

    }

    @Override
    public void transitionStarted(final Transition<FsmState, FsmEvent> transition) {
        if (transition.getKind() == TransitionKind.INITIAL) return;
        System.out.printf("   ... transitionStarted [%s] -> [%s] [%s]%n",
                transition.getSource().getId(), transition.getTarget().getId(),
                transition.getTrigger());
    }

    @Override
    public void transitionEnded(final Transition<FsmState, FsmEvent> transition) {
        if (transition.getKind() == TransitionKind.INITIAL) return;
        String eventName = Objects.nonNull(transition.getTrigger().getEvent()) ? transition.getTrigger().getEvent().name() : "";
        System.out.printf("   ... transitionEnded [%s] -> [%s] [%s]%n",
                transition.getSource().getId(), transition.getTarget().getId(),
                eventName);
    }

    @Override
    public void stateMachineStarted(final StateMachine<FsmState, FsmEvent> stateMachine) {
        System.out.println("   ... stateMachineStarted " + stateMachine.getUuid());
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
//        System.out.println(String.format("   ... stateContext=[%s]", stateContext));
    }
}
