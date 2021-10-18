package portsim.port;

import portsim.ship.Ship;
import portsim.util.BadEncodingException;
import portsim.util.NoSuchShipException;

/**
 * A Bulk Quay is a type of quay specifically designed for the unloading of
 * Bulk Carrier vessels.
 */
public class BulkQuay extends Quay {
    /**
     * The maximum weight in tonnes that the quay can handle
     */
    private int maxTonnage;

    /**
     * Creates a new Bulk Quay with the given ID and max tonnage.
     *
     * @param id         quay ID
     * @param maxTonnage maximum tonnage the quay can handle
     *
     * @throws IllegalArgumentException if ID or maxTonnage &lt; 0
     */
    public BulkQuay(int id, int maxTonnage) throws IllegalArgumentException {
        super(id);
        if (maxTonnage < 0) {
            throw new IllegalArgumentException("maxTonnage must be greater "
                + "than or equal to 0: " + maxTonnage);
        }
        this.maxTonnage = maxTonnage;
    }

    /**
     * Returns the maximum number of tonnes of cargo this quay can handle at
     * one time.
     *
     * @return maxTonnage
     */
    public int getMaxTonnage() {
        return maxTonnage;
    }

    /**
     * Returns true if and only if this BulkQuay is equal to the other given
     * BulkQuay.
     * <p>
     * For two BulkQuays to be equal, they must have the same ID, ship status
     * (must either both be empty or both be occupied) and same tonnage
     * capacity.
     *
     * @param o other object to check quality
     *
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BulkQuay)) {
            return false;
        }
        BulkQuay bulkQuay = (BulkQuay) o;
        return super.equals(bulkQuay)
                && maxTonnage == bulkQuay.getMaxTonnage();
    }

    /**
     * Returns the hash code of this BulkQuay.
     * <p>
     * Two BulkQuays that are equal according to {@link #equals(Object)} should
     * have the same hash code.
     *
     * @return hash code of this quay.
     */
    @Override
    public int hashCode() {
        return super.hashCode() % maxTonnage;
    }

    /**
     * Returns the human-readable string representation of this BulkQuay.
     * <p>
     * The format of the string to return is:
     * <pre>BulkQuay id [Ship: imoNumber] - maxTonnage</pre>
     * <p>
     * Where:
     * <ul>
     *     <li><pre>id</pre> is the ID of this quay</li>
     *     <li><pre>imoNumber</pre> is the IMO number of the ship docked at this
     *     quay, or <pre>None</pre> if the quay is unoccupied</li>
     *     <li><pre>maxTonnage</pre> is the maximum weight in tonnes of this
     *     quay</li>
     * </ul>
     * <p>
     * For example: <pre>BulkQuay 2 [Ship: 2372721] - 120</pre>
     *
     * @return string representation of this quay
     */
    @Override
    public String toString() {
        return String.format("%s - %d",
                super.toString(),
                this.maxTonnage);
    }

    /**
     * Returns the machine-readable string representation of this BulkQuay.
     * <p>
     * The format of the string to return is:
     * <pre>BulkQuay:id:imoNumber:maxTonnage</pre>
     * <p>
     * For example: <pre>BulkQuay:3:1258691:120</pre> OR
     * <pre>BulkQuay:3:None:120</pre>
     *
     * @return encoded string representation of this quay
     */
    @Override
    public String encode() {
        return String.format("%s:%d",
                super.encode(),
                maxTonnage);
    }
}
