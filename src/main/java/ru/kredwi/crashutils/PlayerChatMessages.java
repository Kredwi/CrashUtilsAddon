package ru.kredwi.crashutils;

import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;

import java.util.function.Consumer;

public class PlayerChatMessages {

    public final Consumer<String> info;
    public final Consumer<String> error;
    public final Consumer<String> warning;

    public PlayerChatMessages(Module module) {
        this.info = module::info;
        this.error = module::error;
        this.warning = module::warning;
    }

    public PlayerChatMessages(String title, Class<?> clazz) {
        this.info = (msg) -> {
            ChatUtils.forceNextPrefixClass(clazz);
            ChatUtils.infoPrefix(title, msg);
        };
        this.error = (msg) -> {
            ChatUtils.forceNextPrefixClass(clazz);
            ChatUtils.error(title, msg);
        };
        this.warning = (msg) -> {
            ChatUtils.forceNextPrefixClass(clazz);
            ChatUtils.warningPrefix(title, msg);
        };
    }

}
