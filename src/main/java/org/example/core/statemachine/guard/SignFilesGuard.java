package org.example.core.statemachine.guard;

import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;


@Component("checkSignedFiles")
public class SignFilesGuard extends BaseGuard {

    private final Flags flags = Flags.getInstance();

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {
        boolean flag = flags.isFilesSignedFlag();

        setFlag(flag);

        return super.evaluate(context);
    }
}
