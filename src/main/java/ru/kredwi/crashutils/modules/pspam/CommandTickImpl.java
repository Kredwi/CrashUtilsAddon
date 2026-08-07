package ru.kredwi.crashutils.modules.pspam;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.commands.Commands;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import ru.kredwi.crashutils.PlayerChatMessages;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class CommandTickImpl implements CommandTick {
    private final LinkedList<Action> actions = new LinkedList<>();

    private final PlayerChatMessages pcm;
    private final ArrayList<Runnable> tasks;

    public CommandTickImpl(PlayerChatMessages pcm, ArrayList<Runnable> tasks) {
        this.pcm = pcm;
        this.tasks = tasks;
    }

    @Override
    public void addTick(Action action) {
        this.actions.add(action);
    }

    @Override
    public boolean hasNext() {
        boolean next = false;
        for (Action action : actions) {
            if (!action.isComplete())
                next = true;
        }
        return next;
    }

    @Override
    public void registerTick() {
        String command = actions.stream().map(Action::tick)
            .collect(Collectors.joining(" "));
        tasks.add(() ->
            Minecraft.getInstance().execute(() -> {
                try {
                    ClientPacketListener connection = Minecraft
                        .getInstance()
                        .getConnection();
                    if (connection != null) {
                        connection.sendCommand(command);
                    } else
                        Commands.dispatch(command);
                } catch (CommandSyntaxException e) {
                    MeteorClient.LOG.error("Error of syntax command", e);
                    pcm.error.accept("Error of syntax command");
                }
            })
        );
    }
}
