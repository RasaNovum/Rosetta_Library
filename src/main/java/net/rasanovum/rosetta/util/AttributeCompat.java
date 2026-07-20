package net.rasanovum.rosetta.util;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

/** Attribute modifier helpers. */
public final class AttributeCompat {
    private AttributeCompat() {}

    //? if >=1.21 {
    public static AttributeModifier movementSpeedMultiplier(String modifierKey, double amount) {
        return new AttributeModifier(RegistryCompat.getLocation(modifierKey), amount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
    //?} else {
    /*public static AttributeModifier movementSpeedMultiplier(String modifierKey, double amount) {
        return new AttributeModifier(attributeModifierUuid(modifierKey), modifierKey, amount, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
    *///?}

    public static boolean hasModifier(AttributeInstance attribute, String modifierKey) {
        //? if >=1.21 {
        return attribute.getModifier(RegistryCompat.getLocation(modifierKey)) != null;
        //?} else {
        /*return attribute.getModifier(attributeModifierUuid(modifierKey)) != null;
        *///?}
    }

    public static void removeModifier(AttributeInstance attribute, String modifierKey) {
        //? if >=1.21 {
        attribute.removeModifier(RegistryCompat.getLocation(modifierKey));
        //?} else {
        /*attribute.removeModifier(attributeModifierUuid(modifierKey));
        *///?}
    }

    public static String canonicalModifierKey(String modifierKey) {
        //? if >=1.21 {
        return RegistryCompat.getLocation(modifierKey).toString();
        //?} else {
        /*return attributeModifierUuid(modifierKey).toString();
        *///?}
    }

    public static String modifierKey(AttributeModifier modifier) {
        //? if >=1.21 {
        return modifier.id().toString();
        //?} else {
        /*return modifier.getId().toString();
        *///?}
    }

    public static double calculateAttributeValueSkipping(AttributeInstance attribute, Predicate<AttributeModifier> skipModifier) {
        double baseValue = attribute.getBaseValue();
        Map<AttributeModifier.Operation, Set<AttributeModifier>> operationToModifiers = new EnumMap<>(AttributeModifier.Operation.class);
        for (AttributeModifier.Operation operation : AttributeModifier.Operation.values()) {
            operationToModifiers.put(operation, new HashSet<>());
        }

        //? if >=1.21 {
        for (AttributeModifier modifier : attribute.getModifiers()) {
            if (!skipModifier.test(modifier)) operationToModifiers.get(modifier.operation()).add(modifier);
        }
        for (AttributeModifier modifier : operationToModifiers.get(AttributeModifier.Operation.ADD_VALUE)) baseValue += modifier.amount();
        double value = baseValue;
        for (AttributeModifier modifier : operationToModifiers.get(AttributeModifier.Operation.ADD_MULTIPLIED_BASE)) value += baseValue * modifier.amount();
        for (AttributeModifier modifier : operationToModifiers.get(AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)) value *= 1.0D + modifier.amount();
        return attribute.getAttribute().value().sanitizeValue(value);
        //?} else {
        /*for (AttributeModifier modifier : attribute.getModifiers()) {
            if (!skipModifier.test(modifier)) operationToModifiers.get(modifier.getOperation()).add(modifier);
        }
        for (AttributeModifier modifier : operationToModifiers.get(AttributeModifier.Operation.ADDITION)) baseValue += modifier.getAmount();
        double value = baseValue;
        for (AttributeModifier modifier : operationToModifiers.get(AttributeModifier.Operation.MULTIPLY_BASE)) value += baseValue * modifier.getAmount();
        for (AttributeModifier modifier : operationToModifiers.get(AttributeModifier.Operation.MULTIPLY_TOTAL)) value *= 1.0D + modifier.getAmount();
        return attribute.getAttribute().sanitizeValue(value);
        *///?}
    }

    //? if <1.21 {
    /*private static UUID attributeModifierUuid(String modifierKey) {
        return UUID.nameUUIDFromBytes(modifierKey.getBytes(StandardCharsets.UTF_8));
    }
    *///?}
}
