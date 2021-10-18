package portsim.cargo;

/**
 * Represents the possible types that Bulk Cargo can be.
 * <p>
 * <span style="font-size: 1.2em;">
 * <b>NOTE:</b> You do <b>not</b> need to implement the {@code values()} or
 * ode valueOf(String)} methods as part of the assignment. Their
 * implementations are generated automatically by the compiler. Also, you do
 * <b>not</b> need to implement the {@code Serializable} or {@code Comparable}
 * interfaces, or extend {@code Enum}.
 * </span>
 */
public enum BulkCargoType {
    /**
     * A small hard seed. Harvested for human or animal consumption.
     */
    GRAIN,
    /**
     * A solid compound, usually ores such as iron ore.
     */
    MINERALS,
    /**
     * A combustible black or brownish-black used as a fuel source.
     */
    COAL,
    /**
     * A viscous liquid derived from petroleum, especially for use as a fuel
     * or lubricant.
     */
    OIL,
    /**
     * Another form of bulk cargo that does not fit into other categories.
     */
    OTHER
}
