package ru.kredwi.crashutils.modules;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.kredwi.crashutils.CrashUtils;
import ru.kredwi.crashutils.PlayerChatMessages;
import ru.kredwi.crashutils.modules.pspam.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class PlaceholdersSpam extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<String>> messages = sgGeneral.add(new StringListSetting.Builder()
        .name("messages")
        .description("Messages to use for spam.")
        .defaultValue(List.of("Meteor on Crack!"))
        .build()
    );
    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("The delay between specified messages in ticks.")
        .defaultValue(20)
        .min(0)
        .sliderMax(200)
        .build()
    );
    private final Setting<Boolean> disableOnLeave = sgGeneral.add(new BoolSetting.Builder()
        .name("disable-on-leave")
        .description("Disables spam when you leave a server.")
        .defaultValue(true)
        .build()
    );
    private final Setting<Boolean> disableOnDisconnect = sgGeneral.add(new BoolSetting.Builder()
        .name("disable-on-disconnect")
        .description("Disables spam when you are disconnected from a server.")
        .defaultValue(true)
        .build()
    );

    private final Map<String, BiFunction<@NotNull PlayerChatMessages, @Nullable Integer, @NotNull Action>> placeholders = new HashMap<>();
    private final PlayerChatMessages pcm = new PlayerChatMessages(this);
    private final ArrayList<Runnable> tasks = new ArrayList<>();

    private int index, nes;

    public PlaceholdersSpam() {
        super(CrashUtils.CRASH_UTILS_CATEGORY, "pspam", "Placeholders spams specified messages in chat.");
    }

    @Override
    public void onDeactivate() {
        placeholders.clear();
        tasks.clear();
    }

    @Override
    public void onActivate() {
        this.placeholders.put("random", RandomAction::new);
        this.placeholders.put("self", SelfAction::new);
        this.placeholders.put("pall", PlayersAction::new);
        CompletableFuture.runAsync(() ->
            messages.get()
                .forEach((unparsedCommand) -> {
                    if (unparsedCommand != null) {
                        CommandTick cmdTick = parseCommand(unparsedCommand);
                        while (isActive() && cmdTick.hasNext()) {
                            cmdTick.registerTick();
                        }
                    }
                }));
    }

    @EventHandler
    private void onScreenOpen(OpenScreenEvent event) {
        if (disableOnDisconnect.get() && event.screen instanceof DisconnectedScreen) {
            toggle();
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (disableOnLeave.get()) toggle();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        index++;
        if (index >= delay.get()) {
            index = 0;
            if (isActive() && !tasks.isEmpty()) {
                Runnable task = tasks.get(nes);
                if (task == null)
                    return;
                task.run();
                nes++;
                if (nes >= tasks.size()) {
                    nes = 0;
                }
            }
        }
    }

    private CommandTick parseCommand(@NotNull String unparsedCommand) {
        CommandTick cmdTick = new CommandTickImpl(pcm, tasks);
        String[] commandParts = unparsedCommand.trim().split(" ");
        for (String unparsedParam : commandParts) {
            cmdTick.addTick(getParsedAction(unparsedParam));
        }
        return cmdTick;
    }

    private Action getParsedAction(String commandPart) {
        if (!commandPart.startsWith("$"))
            return new DefaultAction(new PlayerChatMessages(this), commandPart);

        Optional<String> parsed = placeholders.keySet()
            .stream()
            .filter(e -> commandPart.substring(1)
                .startsWith(e))
            .findFirst();

        if (parsed.isPresent()) {
            String actionName = parsed.get().substring(0, parsed.get().length());
            var action = this.placeholders.get(actionName);
            if (actionName.length() != commandPart.length()) {
                String parsedNumberArgment = commandPart
                    .substring(1)
                    .substring(parsed.get().length(), commandPart.length() - 1);
                int formatedNumber = 1;
                try {
                    if (!parsedNumberArgment.trim().isEmpty()) {
                        formatedNumber = Integer.parseInt(parsedNumberArgment);
                    }
                } catch (NumberFormatException e) {
                    error("Error of formatting number: ".concat(e.getMessage()));
                    error("parsedNumberArgument " + parsedNumberArgment);
                }

                return action.apply(pcm, formatedNumber);
            }
            return action.apply(pcm, 1);
        }
        return new DefaultAction(pcm, commandPart);
    }
}
