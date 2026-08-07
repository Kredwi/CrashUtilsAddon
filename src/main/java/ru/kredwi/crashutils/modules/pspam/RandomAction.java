package ru.kredwi.crashutils.modules.pspam;

import joptsimple.internal.Strings;
import ru.kredwi.crashutils.PlayerChatMessages;

import java.util.UUID;

public class RandomAction implements Action {
    private final int countOfAction;

    private int summOfAction = 0;

    public RandomAction(PlayerChatMessages __, int countOfAction) {
        this.countOfAction = countOfAction;
    }

    @Override
    public boolean isComplete() {
        return summOfAction >= countOfAction;
    }

    // random uuid generator
    @Override
    public String tick() {
        summOfAction++;
        return String.join(Strings.EMPTY, UUID.randomUUID()
                .toString()
                .split("-"))
            .substring(1, 10);
    }
}
