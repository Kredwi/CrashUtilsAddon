package ru.kredwi.crashutils.mixin;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kredwi.crashutils.commands.PluginsLookupCommand;

import java.util.Set;

@Mixin(ClientSuggestionProvider.class)
public class ClientSuggestionProviderMixin {

    @Shadow
    private Set<String> customCompletionSuggestions;

    @Inject(method = "completeCustomSuggestions", at = @At("RETURN"))
    private void completeCustomSuggestions(final int id, final Suggestions result, CallbackInfo ci) {
        if (PluginsLookupCommand.REQUEST_ID.get() == id)
            PluginsLookupCommand.HANDLE_COMMANDS_SUGGESTION.accept(result.getList()
                .stream()
                .map(Suggestion::getText)
                .toList());
    }

}
