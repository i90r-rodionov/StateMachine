package org.example.core.statemachine.guard;

import lombok.extern.slf4j.Slf4j;
import org.example.core.service.mock.CheckService;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.example.core.statemachine.util.FsmHelper;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;


@Component("checkCreatedFolderOrSla")
@Slf4j
public class CreateFolderGuard extends BaseGuard {

    public CreateFolderGuard(CheckService checkService) {
        super(checkService);
    }

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {
        log.debug("   ??? Guard evalute [{}]", this.getClass().getSimpleName());

        boolean flag = getCheckService().checkCreatedFolder();

        if (!flag && checkSlaForState(context)) {
            log.debug("   ??? Set SLA_FLAG");
            context.getExtendedState().getVariables().put(FsmHelper.SLA_FLAG, true);
            flag = true;
        }
        setFlag(flag);

        return super.evaluate(context);
    }

    @Override
    protected boolean checkSlaForState(StateContext<FsmState, FsmEvent> context) {
        // TODO
        return getCheckService().checkSla();
    }
}
