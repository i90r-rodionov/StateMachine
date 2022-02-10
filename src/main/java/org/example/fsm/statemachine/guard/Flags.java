package org.example.fsm.statemachine.guard;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.Arrays;

@Getter
@Setter
public class Flags {

    private static Flags instance;

    private boolean readyToPrintFlag = true;
    private boolean readyToSignFlag = true;
    private boolean filesSignedFlag;
    private boolean createdFolderFlag;
    private boolean movedFilesFlag;
    private boolean createdEcmFolderFlag;
    private boolean filesDeliveredFlag;
    private boolean filesResendFlag;

    private Flags() {
        // nothings
    }

    public static synchronized Flags getInstance() {
        if (instance == null) {
            instance = new Flags();
        }
        return instance;
    }

    public String getFields() {
        final StringBuilder result = new StringBuilder();
//        for (Field field : this.getClass().getDeclaredFields()) {
//            field.setAccessible(true);
//            try {
//                result.append(String.format(" %s = [%s]", field.getName(), field.getBoolean(this)));
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
        Arrays.stream(this.getClass().getDeclaredFields())
                .filter(field -> !field.getType().isAssignableFrom(Flags.class))
                .forEach(field -> {
                    try {
                        result.append(String.format(" %s = [%s]", field.getName(), field.getBoolean(this)));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

        return result.toString();
    }

    public void reset() {

        Arrays.stream(this.getClass().getDeclaredFields())
                .filter(field -> field.getType().isAssignableFrom(boolean.class))
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        field.setBoolean(this, false);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

    }

    public void setField(String name, boolean value) {
        try {
            Field field = this.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.setBoolean(this, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getField(String name) {
        try {
            Field field = this.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.getBoolean(this);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
