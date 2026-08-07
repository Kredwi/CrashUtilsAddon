package ru.kredwi.crashutils.modules.pspam;

public interface CommandTick {
    void addTick(Action action);

    boolean hasNext();

    void registerTick();
}
