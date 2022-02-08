package org.example.fsm.integration.service.impl;

import org.example.fsm.integration.service.EcmIntegration;
import org.example.fsm.statemachine.guard.Flags;
import org.springframework.stereotype.Service;

@Service
public class EcmIntegrationImpl implements EcmIntegration {

    @Override
    public boolean createFolder() {
        boolean flag = Flags.isCreatedFolderFlag();
        System.out.printf("   *** EcmIntegration.createFolder() return [%s]%n", flag);
        return flag;
    }

    @Override
    public boolean moveFiles() {
        boolean flag = Flags.isMovedFilesFlag();
        System.out.printf("   *** EcmIntegration.moveFiles() return [%s]%n", flag);
        return flag;
    }
}
