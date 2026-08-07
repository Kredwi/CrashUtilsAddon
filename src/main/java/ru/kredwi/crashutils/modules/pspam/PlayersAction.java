package ru.kredwi.crashutils.modules.pspam;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.players.NameAndId;
import ru.kredwi.crashutils.PlayerChatMessages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayersAction implements Action {

    private final List<String> playerNames;

    public PlayersAction(PlayerChatMessages module, int countOfAction) {
        ServerData server = Minecraft.getInstance()
            .getCurrentServer();
        if (server == null) {
            module.info.accept("You cannot playing in server");
            this.playerNames = Collections.emptyList();
            return;
        }

        ServerStatus.Players players = server
            .players;

        if (players == null) {
            module.info.accept("Players not found in the server");
            this.playerNames = Collections.emptyList();
            return;
        }

        this.playerNames = new ArrayList<>(players
            .sample()
            .stream()
            .map(NameAndId::name)
            .toList());
    }


    @Override
    public String tick() {
        String playerName = playerNames.getFirst();
        playerNames.removeFirst();
        return playerName;
    }

    @Override
    public boolean isComplete() {
        return playerNames != null && playerNames.isEmpty();
    }

}
