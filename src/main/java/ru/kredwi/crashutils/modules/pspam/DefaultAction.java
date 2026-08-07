package ru.kredwi.crashutils.modules.pspam;

import ru.kredwi.crashutils.PlayerChatMessages;

public class DefaultAction implements Action {

    private final String name;
    private boolean complete = false;

    public DefaultAction(PlayerChatMessages __, String name) {
        this.name = name;
    }

    @Override
    public String tick() {
        this.complete = true;
        return this.name;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }
}
