import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigRational implements Comparable<BigRational> {
    public static final BigRational ZERO = new BigRational(BigInteger.ZERO, BigInteger.ONE);
    public static final BigRational ONE = new BigRational(BigInteger.ONE, BigInteger.ONE);

    private final BigInteger numerator;
    private final BigInteger denominator;

    public BigRational(BigInteger numerator, BigInteger denominator) {
        if (numerator == null) {
            throw new NullPointerException();
        }
        if (denominator.signum() == 0) {
            throw new IllegalArgumentException();
        }

        if (denominator.signum() < 0) {
            numerator = numerator.negate();
            denominator = denominator.negate();
        }

        this.numerator = numerator;
        this.denominator = denominator;

        assert denominator.signum() > 0;
    }

    public int signum() {
        assert denominator.signum() > 0;

        return numerator.signum();
    }

    public BigRational add(BigRational other) {
        if (denominator.equals(other.denominator)) {
            return new BigRational(numerator.add(other.numerator), denominator);
        }
        else {
            return new BigRational(numerator.multiply(other.denominator).add(other.numerator.multiply(denominator)), denominator.multiply(other.denominator));
        }
    }

    public BigRational multiply(BigRational other) {
        return new BigRational(numerator.multiply(other.numerator), denominator.multiply(other.denominator));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof BigRational)) {
            return false;
        }

        BigRational other = (BigRational)obj;

        if (denominator.equals(other.denominator)) {
            return numerator.equals(other.numerator);
        }
        else if (numerator.equals(other.numerator)) {
            return denominator.equals(other.denominator);
        }
        else {
            return numerator.multiply(other.denominator).equals(other.numerator.multiply(denominator));
        }
    }

    @Override
    public int compareTo(BigRational other) {
        if (denominator.equals(other.denominator)) {
            return numerator.compareTo(other.numerator);
        }
        else if (numerator.equals(other.numerator)) {
            return other.denominator.compareTo(denominator);
        }
        else {
            return numerator.multiply(other.denominator).compareTo(other.numerator.multiply(denominator));
        }
    }

    public BigDecimal toBigDecimal(int scale, RoundingMode roundingMode) {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), scale, roundingMode);
    }

    @Override
    public String toString() {
        return toBigDecimal(3, RoundingMode.HALF_UP).toString();
    }
}
