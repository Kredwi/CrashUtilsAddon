package ru.kredwi.crashutils;

import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;
import ru.kredwi.crashutils.commands.ChannelsCommand;
import ru.kredwi.crashutils.commands.PluginsLookupCommand;
import ru.kredwi.crashutils.modules.PasswordBruteforce;
import ru.kredwi.crashutils.modules.PlaceholdersSpam;
import ru.kredwi.crashutils.modules.exploit.*;
import ru.kredwi.crashutils.modules.exploit.plugins.AuthmeVelocityExploit;
import ru.kredwi.crashutils.modules.exploit.plugins.GrimACExploit;
import ru.kredwi.crashutils.modules.exploit.plugins.LPXExploit;
import ru.kredwi.crashutils.modules.exploit.plugins.TradeSystemExploit;
import ru.kredwi.crashutils.modules.exploit.plugins.commands.*;

import java.lang.invoke.MethodHandles;

public class CrashUtils extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CRASH_UTILS_CATEGORY = new Category("CrashUtils");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Crash Utils");

        MeteorClient.EVENT_BUS.registerLambdaFactory("ru.kredwi.crashutils", (lookup, clazz) -> {
            try {
                return MethodHandles.privateLookupIn(clazz, MethodHandles.lookup());
            } catch (IllegalAccessError e) {
                return MethodHandles.lookup();
            }
        });

        registerModules();
        registerCommands();
    }

    private void registerModules() {
        // Modules
        Modules.get().add(new PlaceholdersSpam());
        Modules.get().add(new PaperWindowExploit());
        Modules.get().add(new BrigadierExploit());
        Modules.get().add(new NegativeInfinityExploit());
        Modules.get().add(new CMIExploit());
        Modules.get().add(new Log4ShellExploit());
        Modules.get().add(new MultiverseExploit());
        Modules.get().add(new WGCalcExploit());
        Modules.get().add(new WFurnaceExploit());
        Modules.get().add(new PasswordBruteforce());
        Modules.get().add(new BundleExploit());
        Modules.get().add(new InvalidPositionExploit());

        Modules.get().add(new TradeSystemExploit());
        Modules.get().add(new AuthmeVelocityExploit());
        Modules.get().add(new LPXExploit());
        Modules.get().add(new FAWEExploit());

        Modules.get().add(new GrimACExploit());
        Modules.get().add(new ASCIExploit());
    }

    private void registerCommands() {
        // Commands
        Commands.add(new ChannelsCommand());
        Commands.add(new PluginsLookupCommand());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CRASH_UTILS_CATEGORY);
    }

    @Override
    public String getPackage() {
        return "ru.kredwi.crashutils";
    }

    @Override
    public String getWebsite() {
        return "https://kredwi.ru";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("Kredwi", "crash-utils");
    }
}
