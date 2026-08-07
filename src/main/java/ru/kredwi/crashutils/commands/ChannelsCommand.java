package ru.kredwi.crashutils.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.resources.Identifier;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ChannelsCommand extends Command {
    public ChannelsCommand() {
        super("channels", "show all avaivable channels");
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.executes((_) -> {
            Set<Identifier> allChannels = new HashSet<>(ClientPlayNetworking.getReceived());

            allChannels.addAll(ClientPlayNetworking.getGlobalReceivers());
            allChannels.addAll(ClientPlayNetworking.getSendable());
            info("\n" + allChannels
                .stream()
                .sorted(Comparator.comparing(Identifier::toString))
                .map(id ->
                    "§" + (ClientPlayNetworking.canSend(id) ? "a" : "c") + id
                )
                .collect(Collectors.joining("\n")));
            return SINGLE_SUCCESS;
        });
    }
}
