package net.rasanovum.rosetta.util;

import net.rasanovum.rosetta.util.RegistryCompat;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

import java.util.function.Function;
import java.util.function.Supplier;

//? if >=26.1 {
/*import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import net.minecraft.world.level.gamerules.GameRules;
*///?} else {
import net.minecraft.world.level.GameRules;
//? if fabric {
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
//?}
//?}

//? if >=26.1 && fabric {
/*import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
*///?}

//? if >=26.1 && neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
*///?}

public final class GameRuleCompat {
    private final String namespace;

    public GameRuleCompat(String namespace) {
        this.namespace = namespace;
        //? if >=26.1 && neoforge {
        /*this.gameRules = DeferredRegister.create(BuiltInRegistries.GAME_RULE, namespace);
        *///?}
    }

    public enum Category { PLAYER, MISC }

    public static final class Key<T> {
        private final Function<Level, T> levelReader;
        private final Function<MinecraftServer, T> serverReader;

        private Key(Function<Level, T> levelReader, Function<MinecraftServer, T> serverReader) {
            this.levelReader = levelReader;
            this.serverReader = serverReader;
        }
    }

    //? if >=26.1 && neoforge {
    /*private final DeferredRegister<GameRule<?>> gameRules;
    *///?}

    public Key<Integer> registerInt(String name, Category category, int defaultValue) {
        //? if >=26.1 {
        /*return registerModernInt(name, category, defaultValue);
        *///?} else {
        return registerLegacyInt(namespace + "." + name, category, defaultValue);
        //?}
    }

    public Key<Boolean> registerBool(String name, Category category, boolean defaultValue) {
        //? if >=26.1 {
        /*return registerModernBool(name, category, defaultValue);
        *///?} else {
        return registerLegacyBool(namespace + "." + name, category, defaultValue);
        //?}
    }

    public void register(Object eventBus) {
        if (eventBus == null) return;
        //? if >=26.1 && neoforge {
        /*gameRules.register((IEventBus) eventBus);
        *///?}
    }

    public static <T> T get(Level level, Key<T> rule) {
        return rule.levelReader.apply(level);
    }

    public static <T> T get(MinecraftServer server, Key<T> rule) {
        return rule.serverReader.apply(server);
    }

    //? if >=26.1 && neoforge {
    /*private Key<Integer> registerModernInt(String name, Category category, int defaultValue) {
        DeferredHolder<GameRule<?>, GameRule<Integer>> holder = gameRules.register(name, () -> new GameRule<>(
                modernCategory(category), GameRuleType.INT, IntegerArgumentType.integer(),
                GameRuleTypeVisitor::visitInteger, Codec.INT, value -> value, defaultValue, FeatureFlagSet.of()));
        return wrapModern(holder::get);
    }

    private Key<Boolean> registerModernBool(String name, Category category, boolean defaultValue) {
        DeferredHolder<GameRule<?>, GameRule<Boolean>> holder = gameRules.register(name, () -> new GameRule<>(
                modernCategory(category), GameRuleType.BOOL, BoolArgumentType.bool(),
                GameRuleTypeVisitor::visitBoolean, Codec.BOOL, value -> value ? 1 : 0, defaultValue, FeatureFlagSet.of()));
        return wrapModern(holder::get);
    }
    *///?}

    //? if >=26.1 && fabric {
    /*private Key<Integer> registerModernInt(String name, Category category, int defaultValue) {
        GameRule<Integer> rule = GameRuleBuilder.forInteger(defaultValue).category(modernCategory(category))
                .buildAndRegister(RegistryCompat.getLocation(namespace, name));
        return wrapModern(() -> rule);
    }

    private Key<Boolean> registerModernBool(String name, Category category, boolean defaultValue) {
        GameRule<Boolean> rule = GameRuleBuilder.forBoolean(defaultValue).category(modernCategory(category))
                .buildAndRegister(RegistryCompat.getLocation(namespace, name));
        return wrapModern(() -> rule);
    }
    *///?}

    //? if >=26.1 {
    /*
    private static <T> Key<T> wrapModern(Supplier<GameRule<T>> ruleSupplier) {
        return new Key<>(
                level -> {
                    GameRule<T> rule = ruleSupplier.get();
                    MinecraftServer server = level.getServer();
                    return server != null ? server.getGameRules().get(rule) : rule.defaultValue();
                },
                server -> server.getGameRules().get(ruleSupplier.get())
        );
    }

    private static GameRuleCategory modernCategory(Category category) {
        return category == Category.PLAYER ? GameRuleCategory.PLAYER : GameRuleCategory.MISC;
    }
    *///?} else {
    private static Key<Integer> registerLegacyInt(String name, Category category, int defaultValue) {
        //? if fabric {
        GameRules.Key<GameRules.IntegerValue> rule = GameRuleRegistry.register(name, legacyCategory(category), GameRuleFactory.createIntRule(defaultValue));
        //?} else if neoforge || forge {
        /*GameRules.Key<GameRules.IntegerValue> rule = GameRules.register(name, legacyCategory(category), GameRules.IntegerValue.create(defaultValue));
        *///?}
        return new Key<>(level -> level.getGameRules().getInt(rule), server -> server.getGameRules().getInt(rule));
    }

    private static Key<Boolean> registerLegacyBool(String name, Category category, boolean defaultValue) {
        //? if fabric {
        GameRules.Key<GameRules.BooleanValue> rule = GameRuleRegistry.register(name, legacyCategory(category), GameRuleFactory.createBooleanRule(defaultValue));
        //?} else if neoforge || forge {
        /*GameRules.Key<GameRules.BooleanValue> rule = GameRules.register(name, legacyCategory(category), GameRules.BooleanValue.create(defaultValue));
        *///?}
        return new Key<>(level -> level.getGameRules().getBoolean(rule), server -> server.getGameRules().getBoolean(rule));
    }

    private static GameRules.Category legacyCategory(Category category) {
        return category == Category.PLAYER ? GameRules.Category.PLAYER : GameRules.Category.MISC;
    }
    //?}
}
