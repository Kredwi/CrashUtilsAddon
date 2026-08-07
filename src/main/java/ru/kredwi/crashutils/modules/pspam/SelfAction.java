package ru.kredwi.crashutils.modules.pspam;

import net.minecraft.client.Minecraft;
import ru.kredwi.crashutils.PlayerChatMessages;

public class SelfAction implements Action {

    private boolean complete;

    public SelfAction(PlayerChatMessages __, int countOfAction) {
    }

    @Override
    public String tick() {
        this.complete = true;
        return Minecraft.getInstance()
            .player
            .getName()
            .getString();
    }

    @Override
    public boolean isComplete() {
        return complete;
    }
}
