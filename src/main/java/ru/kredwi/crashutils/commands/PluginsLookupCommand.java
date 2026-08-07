package ru.kredwi.crashutils.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import joptsimple.internal.Strings;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class PluginsLookupCommand extends Command {
    public static final Set<String> PLUGIN_BLACKLIST = Set.of(
        "bukkit", "minecraft", "spigot", "paper", "purpur"
    );

    public static final Random random = new Random();
    public static final AtomicInteger REQUEST_ID = new AtomicInteger(random.nextInt(0, Integer.MAX_VALUE));
    public static final Consumer<List<String>> HANDLE_COMMANDS_SUGGESTION = (suggestions) -> {
        REQUEST_ID.set(-1); // reset

        if (suggestions.isEmpty()) {
            ChatUtils.error("Server doest send suggestions");
            return;
        }

        List<String> plugins = suggestions.stream()
            // filtering for a commands with :
            .filter(suggestion -> suggestion.contains(Character.toString(Identifier.NAMESPACE_SEPARATOR)))
            // getting only namespaces
            .map((suggestion) -> suggestion.split(Character.toString(Identifier.NAMESPACE_SEPARATOR))[0])
            // remove non-plugin
            .filter(Predicate.not(PLUGIN_BLACKLIST::contains))
            // remove duplicate
            .distinct()
            .toList();
        String pluginsMessage = Strings.join(plugins, ", ");

        String message = "§5ⓘ §fServer Plugins " + "(" + plugins.size() + "): " +
            "§a" + pluginsMessage;

        ChatUtils.sendMsg(Component.literal(message));
    };

    public PluginsLookupCommand() {
        super("pluginslookup", "Look up hidden plugins");
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.executes((_) -> {
            if (mc.getConnection() == null) {
                error("You cannot be playing in the minecraft server");
                return SINGLE_SUCCESS;
            }

            REQUEST_ID.set(random.nextInt(0, Integer.MAX_VALUE));
            mc.getConnection()
                .send(new ServerboundCommandSuggestionPacket(REQUEST_ID.get(), "/"));
            info("Suggestion packet send");
            return SINGLE_SUCCESS;
        });
    }
}
