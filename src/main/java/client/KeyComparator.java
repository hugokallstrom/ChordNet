package client;

import java.math.BigInteger;

/**
 * Created by hugo on 12/11/14.
 */
public class KeyComparator {

    private BigInteger lowerBound;
    private BigInteger upperBound;
    private BigInteger maxValue;

    public KeyComparator(BigInteger myKey, BigInteger searchKey) {
        this.lowerBound = myKey;
        this.upperBound = searchKey;
        maxValue = new BigInteger("2").pow(160).subtract(new BigInteger("1"));
    }

    public boolean checkInterval(BigInteger finger) {
        if (regularInterval() && insideInterval(finger)) {
            return true;
        } else if (modInterval() && (betweenLowerAndMaxVal(finger) || betweenZeroAndUpper(finger))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean regularInterval() {
        return upperBound.compareTo(lowerBound) > 0;
    }

    public boolean insideInterval(BigInteger key) {
        return ((key.compareTo(lowerBound) > 0) && (key.compareTo(upperBound) <= 0));
    }

    public boolean modInterval() {
        return upperBound.compareTo(lowerBound) < 0;
    }

    public boolean betweenLowerAndMaxVal(BigInteger key) {
        return ((key.compareTo(lowerBound) > 0) && (key.compareTo(maxValue) <= 0));
    }

    private boolean betweenZeroAndUpper(BigInteger key) {
        return ((key.compareTo(BigInteger.ZERO) >= 0) && (key.compareTo(upperBound) <= 0));
    }
}
