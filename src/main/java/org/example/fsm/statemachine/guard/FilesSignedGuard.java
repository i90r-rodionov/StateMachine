package org.example.fsm.statemachine.guard;

import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;


@Service("checkFilesSigned")
public class FilesSignedGuard extends BaseGuard {

    private final Flags flags = Flags.getInstance();

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {
        boolean flag = flags.isFilesSignedFlag();

        setFlag(flag);

        return super.evaluate(context);
    }
}
