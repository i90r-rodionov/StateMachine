package org.example.fsm.service.mock.impl;

import org.example.fsm.service.mock.MockService;
import org.example.fsm.statemachine.guard.Flags;
import org.springframework.stereotype.Service;

@Service
public class MockServiceImpl implements MockService {

    private final Flags flags = Flags.getInstance();

    @Override
    public boolean createFolder() {
        boolean flag = flags.isCreatedFolderFlag();

        trace(new Object(), flag);

        return flag;
    }

    @Override
    public boolean moveFiles() {
        boolean flag = flags.isMovedFilesFlag();

        trace(new Object(), flag);

        return flag;
    }
}
