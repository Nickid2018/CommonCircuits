package io.github.nickid2018.commoncircuits.logic;

import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.function.Function;
import java.util.function.ToIntBiFunction;

@Getter
@AllArgsConstructor
public class LogicType {

    public static final LogicType SIMPLE_REDSTONE_SIGNAL = new LogicType(
            name -> IntegerProperty.create(name, 0, 15),
            (property, state) -> (Integer) state.getValue(property),
            value -> value
    );
    public static final LogicType REGULAR_BOOLEAN_REDSTONE_SIGNAL = new LogicType(
            BooleanProperty::create,
            (property, state) -> (Boolean) state.getValue(property) ? 15 : 0,
            value -> value > 0
    );

    private final Function<String, Property<?>> property;
    private final ToIntBiFunction<Property<?>, BlockState> valueGetter;
    private final Int2ObjectFunction<? extends Comparable<?>> toValue;

    public Property<?> createProperty(String name) {
        return property.apply(name);
    }

    public int getValue(Property<?> property, BlockState state) {
        return valueGetter.applyAsInt(property, state);
    }

    @SuppressWarnings("rawtypes")
    public Comparable toValue(int value) {
        return toValue.get(value);
    }
}
