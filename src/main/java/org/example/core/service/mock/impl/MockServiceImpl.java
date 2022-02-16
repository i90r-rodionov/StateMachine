package org.example.core.service.mock.impl;

import org.example.core.statemachine.guard.Flags;
import org.example.core.service.mock.MockService;
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
