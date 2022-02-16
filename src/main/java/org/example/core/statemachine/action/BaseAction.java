package org.example.core.statemachine.action;

import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.example.core.statemachine.persist.StateHolder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;


abstract public class BaseAction implements Action<FsmState, FsmEvent> {

    private final StateHolder stateHolder;

    protected BaseAction(StateHolder stateHolder) {
        this.stateHolder = stateHolder;
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {

        System.out.printf("   ### execute [%s] sourceState=[%s] targetState [%s]%n",
                this.getClass().getSimpleName(), context.getSource().getId().name(), context.getTarget().getId().name());

        stateHolder.setState(context.getTarget().getId());
        //context.getStateMachine().stop();
        //context.getStateMachine().start();
    }

    protected StateHolder getStateHolder() {
        return stateHolder;
    }

    protected void generateError() {
        System.out.printf("   ### Error in [%s]%n", this.getClass().getSimpleName());
        throw new RuntimeException(this.getClass().getSimpleName() + " Error");
    }
}
