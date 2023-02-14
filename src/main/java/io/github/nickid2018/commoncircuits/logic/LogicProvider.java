package io.github.nickid2018.commoncircuits.logic;

import io.github.nickid2018.commoncircuits.util.Int2IntFunction;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.function.ToIntFunction;

@Getter
public class LogicProvider {

    private final int requiredInputs;
    private final List<LogicType> outputs;
    private final List<LogicType> stores;

    private final TriggerCondition condition;
    private final ToIntFunction<Int2IntFunction>[] logic;

    @SafeVarargs
    public LogicProvider(int requiredInputs,
                         List<LogicType> outputs,
                         List<LogicType> stores,
                         ToIntFunction<Int2IntFunction>... logic) {
        this(requiredInputs, outputs, stores, TriggerCondition.NO_TRIGGER, logic);
    }

    @SafeVarargs
    public LogicProvider(int requiredInputs,
                         List<LogicType> outputs,
                         List<LogicType> stores,
                         TriggerCondition condition,
                         ToIntFunction<Int2IntFunction>... logic) {
        this.requiredInputs = requiredInputs;
        this.outputs = outputs;
        this.stores = stores;
        this.condition = condition;
        this.logic = logic;
    }

    public int getRequiredOutputs() {
        return outputs.size();
    }

    public int runLogic(int outputIndex, Int2IntFunction input) {
        return logic[outputIndex].applyAsInt(input);
    }

    public static final LogicProvider AND = new LogicProvider(
            2, List.of(LogicType.BOOLEAN_REDSTONE_SIGNAL), List.of(),
            input -> input.applyAsInt(0) > 0 && input.applyAsInt(1) > 0 ? 15 : 0
    );

    public static final LogicProvider OR = new LogicProvider(
            2, List.of(LogicType.BOOLEAN_REDSTONE_SIGNAL), List.of(),
            input -> input.applyAsInt(0) > 0 || input.applyAsInt(1) > 0 ? 15 : 0
    );

    public static final LogicProvider NOT = new LogicProvider(
            1, List.of(LogicType.BOOLEAN_REDSTONE_SIGNAL), List.of(),
            input -> input.applyAsInt(0) > 0 ? 0 : 15
    );

    public static final LogicProvider XOR = new LogicProvider(
            2, List.of(LogicType.BOOLEAN_REDSTONE_SIGNAL), List.of(),
            input -> input.applyAsInt(0) > 0 ^ input.applyAsInt(1) > 0 ? 15 : 0
    );

    public static final LogicProvider NAND = new LogicProvider(
            2, List.of(LogicType.BOOLEAN_REDSTONE_SIGNAL), List.of(),
            input -> input.applyAsInt(0) > 0 && input.applyAsInt(1) > 0 ? 0 : 15
    );

    public static final LogicProvider NOR = new LogicProvider(
            2, List.of(LogicType.BOOLEAN_REDSTONE_SIGNAL), List.of(),
            input -> input.applyAsInt(0) > 0 || input.applyAsInt(1) > 0 ? 0 : 15
    );

    public static final LogicProvider XNOR = new LogicProvider(
            2, List.of(LogicType.BOOLEAN_REDSTONE_SIGNAL), List.of(),
            input -> input.applyAsInt(0) > 0 ^ input.applyAsInt(1) > 0 ? 0 : 15
    );

    public static final LogicProvider D_TRIGGER = new LogicProvider(
            1, List.of(LogicType.BOOLEAN_REDSTONE_SIGNAL, LogicType.BOOLEAN_REDSTONE_SIGNAL), List.of(),
            TriggerCondition.RISING_EDGE,
            input -> input.applyAsInt(0) > 0 ? 15 : 0,
            input -> input.applyAsInt(0) > 0 ? 0 : 15
    );
}
