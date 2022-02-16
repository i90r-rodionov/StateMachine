package org.example.core.statemachine.guard;

import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;


@Component("checkCreatedFolder")
public class CreateFolderGuard extends BaseGuard {

    private Flags flags = Flags.getInstance();

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {

        boolean flag = false; //flags.isCreatedFolderFlag();

        setFlag(flag);

        return super.evaluate(context);
    }
}
