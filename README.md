# Crash Utils Addon

A meteor client addon

![Java](https://img.shields.io/badge/Java-25+-orange?style=flat-square&logo=openjdk)
![Fabric](https://img.shields.io/badge/Platform-Fabric-blue?style=flat-square&logo=fabric)
![Minecraft](https://img.shields.io/badge/Minecraft-26.3-brightgreen?style=flat-square&logo=minetest)

[![Maintainability](https://qlty.sh/gh/Kredwi/projects/CrashUtilsAddon/maintainability.png)](https://qlty.sh/gh/Kredwi/projects/CrashUtilsAddon)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/fadc800a852a485ba1846cffb56c2d38)](https://app.codacy.com/gh/Kredwi/CrashUtilsAddon/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

## Requirements

- Java 25
- Minecraft 26.3
- Fabric API
- Meteor Client

## Features

### Exploits from other projects

- Brigadier exploit
- Bundle exploit
- Invalid Position && Negative Infinity position
- Paper Window exploit

### Plugins bugs

- Chat hacks (log4shell, from hacked plugins)
- Plugins exploits (trade system, lpx, wFurnace, WGCalc, Multiverse, fawe, cmi)

### Commands

- `channels` - get all active channels
- `pluginlookup` - get plugins from tab completer

### Other utils

- PasswordBruteforce
- PlaceholdersSpam

## How to build?

```bash
git clone https://github.com/Kredwi/CrashUtilsAddon 
cd ./CrashUtils
./gradlew build 
```

Find the finally mod file in `./build/libs/crash-utils-version.jar`

## F.A.Q

- How to find registered modules?
-
    - here `ru.kredwi.crashutils.CrashUtils#registerModules()`
- How to find registered commands?
-
    - here `ru.kredwi.crashutils.CrashUtils#registerCommands()`
- How to create custom module?
- create class and extends `meteordevelopment.meteorclient.systems.modules.Module` (and register)
- Why channels red and green?
-
    - The status of "The channel is sendable?" true is green false red

## Thanks

- CCBlueX/LiquidBounce
- ZynoxTheGmr/XynisClient-99 && ZynoxTheGmr/XynisClient
- ParadiseDevelopments/ParadiseClient-X
- wrrulosdev/mcpclient
- MeteorDevelopment/meteor-addon-template

(the mod has more bugs)

## License

[Apache 2.0](https://github.com/Kredwi/CrashUtilsAddon/blob/master/LICENSE)
