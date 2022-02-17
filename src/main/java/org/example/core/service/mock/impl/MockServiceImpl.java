package org.example.core.service.mock.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.core.service.mock.CheckService;
import org.example.core.statemachine.guard.Flags;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Эмулятор интеграционных сервисов
 */
@Service
@Slf4j
public class MockServiceImpl implements CheckService {

    private static int slaCount = 0;

    private final Flags flags = Flags.getInstance();


    @Override
    public boolean checkReadyToPrint() {
        trace();
        return flags.isReadyToPrintFlag();
    }

    @Override
    public boolean checkReadyToSign() {
        trace();
        return flags.isReadyToSignFlag();
    }

    @Override
    public boolean checkSignedFiles() {
        trace();
        return flags.isFilesSignedFlag();
    }

    @Override
    public boolean checkCreatedFolder() {
        trace();
        return flags.isCreatedFolderFlag();
    }


    @Override
    public boolean checkMovedFiles() {
        trace();
        return flags.isMovedFilesFlag();
    }

    @Override
    public boolean checkSla() {
        trace();

        slaCount += 1;
        log.debug( "   ??? slaCount = {}", slaCount);
        if(slaCount % 10 == 0) {
            slaCount = 0;
            return true;
        }
        return false;
    }

    private void trace(){
        StackWalker walker = StackWalker.getInstance();
        //Optional methodName = walker.walk(frames -> frames.findFirst().map(StackWalker.StackFrame::getMethodName));
        List<StackWalker.StackFrame> frameList = walker.walk(frames -> frames.collect(Collectors.toList()));
        log.debug("   --- {}.{}", this.getClass().getSimpleName(), frameList.get(1).getMethodName());
    }

}
