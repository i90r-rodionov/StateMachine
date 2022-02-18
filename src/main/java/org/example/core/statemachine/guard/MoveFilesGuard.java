package org.example.core.statemachine.guard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.core.service.mock.CheckService;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.example.core.statemachine.util.FsmHelper;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;


@Component("checkMovedFilesOrSla")
@Slf4j
public class MoveFilesGuard extends BaseGuard {

    public MoveFilesGuard(CheckService checkService) {
        super(checkService);
    }

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {

        boolean flag = getCheckService().checkMovedFiles();

        if (!flag && checkSla(context)) {
            context.getExtendedState().getVariables().put(FsmHelper.SLA_FLAG, true);
            flag = true;
        }
        setFlag(flag);

        return super.evaluate(context);
    }

    @Override
    protected boolean checkSla(StateContext<FsmState, FsmEvent> context) {
        // TODO
        return getCheckService().checkSla();
    }

}
