package net.bloody.utils;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;

public class TimeFormatter implements Externalizable, Comparable<TimeFormatter>, Cloneable {

    private static final long SECOND = 1000;

    private static final long MINUTE = SECOND * 60;

    private static final long HOUR = MINUTE * 60;
    private static final long DAY = HOUR * 24;

    private static final long YEAR = DAY * 365;

    private static final int CEILING_PERCENTAGE = 15;

    /**
     * Parsing state.
     */
    public static enum State {

        NUMBER, IGNORED, UNIT

    }

    public static State getState(char c) {
        State out;
        switch (c) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                out = State.NUMBER;
                break;
            case 's':
            case 'm':
            case 'h':
            case 'd':
            case 'y':
            case 'S':
            case 'M':
            case 'H':
            case 'D':
            case 'Y':
                out = State.UNIT;
                break;
            default:
                out = State.IGNORED;
        }
        return out;
    }

    public static TimeFormatter eval(final CharSequence s) {
        TimeFormatter out = new TimeFormatter(0L);

        int num = 0;

        int start = 0;
        int end = 0;

        State oldState = State.IGNORED;

        for (char c : new Iterable<Character>() {

            /**
             * @see java.lang.Iterable#iterator()
             */
            public Iterator<Character> iterator() {
                return new Iterator<Character>() {

                    private int p = 0;

                    /**
                     * @see java.util.Iterator#hasNext()
                     */
                    public boolean hasNext() {
                        return p < s.length();
                    }

                    /**
                     * @see java.util.Iterator#next()
                     */
                    public Character next() {
                        return s.charAt(p++);
                    }

                    /**
                     * @see java.util.Iterator#remove()
                     */
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                };
            }

        }) {
            State newState = getState(c);
            if (oldState != newState) {
                if (oldState == State.NUMBER && (newState == State.IGNORED || newState == State.UNIT)) {
                    num = Integer.parseInt(s.subSequence(start, end).toString());
                }
                else if (oldState == State.UNIT && (newState == State.IGNORED || newState == State.NUMBER)) {
                    out.nTimes(s.subSequence(start, end).toString(), num);
                    num = 0;
                }
                start = end;
            }
            ++end;
            oldState = newState;
        }
        if (oldState == State.UNIT) {
            out.nTimes(s.subSequence(start, end).toString(), num);
        }

        return out;
    }

    public static String exactly(CharSequence in) {
        return eval(in).getExactly();
    }

    public static String exactly(long l) {
        return new TimeFormatter(l).getExactly();
    }

    public static String approximately(CharSequence in) {
        return eval(in).getApproximately();
    }

    public static String approximately(long l) {
        return new TimeFormatter(l).getApproximately();
    }

    private long delta;

    public TimeFormatter() {
        this(0L);
    }

    public TimeFormatter(long delta) {
        super();
        this.delta = Math.abs(delta);
    }

    private void nTimes(String unit, int n) {
        if ("ms".equalsIgnoreCase(unit)) {
            ms(n);
        }
        else if ("s".equalsIgnoreCase(unit)) {
            s(n);
        }
        else if ("m".equalsIgnoreCase(unit)) {
            m(n);
        }
        else if ("h".equalsIgnoreCase(unit)) {
            h(n);
        }
        else if ("d".equalsIgnoreCase(unit)) {
            d(n);
        }
        else if ("y".equalsIgnoreCase(unit)) {
            y(n);
        }
    }

    private long upperCeiling(long x) {
        return (x / 100) * (100 - CEILING_PERCENTAGE);
    }

    private long lowerCeiling(long x) {
        return (x / 100) * CEILING_PERCENTAGE;
    }

    private String ceil(long d, long n) {
        return Integer.toString((int) Math.ceil((double) d / n));
    }

    private String floor(long d, long n) {
        return Integer.toString((int) Math.floor((double) d / n));
    }

    public TimeFormatter y() {
        return y(1);
    }

    public TimeFormatter y(int n) {
        delta += YEAR * Math.abs(n);
        return this;
    }

    public TimeFormatter d() {
        return d(1);
    }

    public TimeFormatter d(int n) {
        delta += DAY * Math.abs(n);
        return this;
    }

    public TimeFormatter h() {
        return h(1);
    }

    public TimeFormatter h(int n) {
        delta += HOUR * Math.abs(n);
        return this;
    }

    public TimeFormatter m() {
        return m(1);
    }

    public TimeFormatter m(int n) {
        delta += MINUTE * Math.abs(n);
        return this;
    }

    public TimeFormatter s() {
        return s(1);
    }

    public TimeFormatter s(int n) {
        delta += SECOND * Math.abs(n);
        return this;
    }

    public TimeFormatter ms() {
        return ms(1);
    }

    public TimeFormatter ms(int n) {
        delta += Math.abs(n);
        return this;
    }

    public String getExactly() {
        return getExactly(new StringBuilder()).toString();
    }

    public <T extends Appendable> T getExactly(T a) {
        try {
            boolean prependBlank = false;
            long d = delta;
            if (d >= YEAR) {
                a.append(floor(d, YEAR));
                a.append('y');
                prependBlank = true;
            }
            d %= YEAR;
            if (d >= DAY) {
                if (prependBlank) {
                    a.append(' ');
                }
                a.append(floor(d, DAY));
                a.append('d');
                prependBlank = true;
            }
            d %= DAY;
            if (d >= HOUR) {
                if (prependBlank) {
                    a.append(' ');
                }
                a.append(floor(d, HOUR));
                a.append('h');
                prependBlank = true;
            }
            d %= HOUR;
            if (d >= MINUTE) {
                if (prependBlank) {
                    a.append(' ');
                }
                a.append(floor(d, MINUTE));
                a.append('m');
                prependBlank = true;
            }
            d %= MINUTE;
            if (d >= SECOND) {
                if (prependBlank) {
                    a.append(' ');
                }
                a.append(floor(d, SECOND));
                a.append('s');
                prependBlank = true;
            }
            d %= SECOND;
            if (d > 0) {
                if (prependBlank) {
                    a.append(' ');
                }
                a.append(Integer.toString((int) d));
                a.append('m');
                a.append('s');
            }
        }
        catch (IOException ex) {
            // What were they thinking...
        }
        return a;
    }

    public String getApproximately() {
        return getApproximately(new StringBuilder()).toString();
    }

    public <T extends Appendable> T getApproximately(T a) {

        try {
            int parts = 0;
            boolean rounded = false;
            boolean prependBlank = false;
            long d = delta;
            long mod = d % YEAR;

            if (mod >= upperCeiling(YEAR)) {
                a.append(ceil(d, YEAR));
                a.append('y');
                ++parts;
                rounded = true;
                prependBlank = true;
            }
            else if (d >= YEAR) {
                a.append(floor(d, YEAR));
                a.append('y');
                ++parts;
                rounded = mod <= lowerCeiling(YEAR);
                prependBlank = true;
            }

            if (!rounded) {
                d %= YEAR;
                mod = d % DAY;

                if (mod >= upperCeiling(DAY)) {
                    if (prependBlank) {
                        a.append(' ');
                    }
                    a.append(ceil(d, DAY));
                    a.append('d');
                    ++parts;
                    rounded = true;
                    prependBlank = true;
                }
                else if (d >= DAY) {
                    if (prependBlank) {
                        a.append(' ');
                    }
                    a.append(floor(d, DAY));
                    a.append('d');
                    ++parts;
                    rounded = mod <= lowerCeiling(DAY);
                    prependBlank = true;
                }

                if (parts < 2) {
                    d %= DAY;
                    mod = d % HOUR;

                    if (mod >= upperCeiling(HOUR)) {
                        if (prependBlank) {
                            a.append(' ');
                        }
                        a.append(ceil(d, HOUR));
                        a.append('h');
                        ++parts;
                        rounded = true;
                        prependBlank = true;
                    }
                    else if (d >= HOUR && !rounded) {
                        if (prependBlank) {
                            a.append(' ');
                        }
                        a.append(floor(d, HOUR));
                        a.append('h');
                        ++parts;
                        rounded = mod <= lowerCeiling(HOUR);
                        prependBlank = true;
                    }

                    if (parts < 2) {
                        d %= HOUR;
                        mod = d % MINUTE;

                        if (mod >= upperCeiling(MINUTE)) {
                            if (prependBlank) {
                                a.append(' ');
                            }
                            a.append(ceil(d, MINUTE));
                            a.append('m');
                            ++parts;
                            rounded = true;
                            prependBlank = true;
                        }
                        else if (d >= MINUTE && !rounded) {
                            if (prependBlank) {
                                a.append(' ');
                            }
                            a.append(floor(d, MINUTE));
                            a.append('m');
                            ++parts;
                            rounded = mod <= lowerCeiling(MINUTE);
                            prependBlank = true;
                        }

                        if (parts < 2) {
                            d %= MINUTE;
                            mod = d % SECOND;

                            if (mod >= upperCeiling(SECOND)) {
                                if (prependBlank) {
                                    a.append(' ');
                                }
                                a.append(ceil(d, SECOND));
                                a.append('s');
                                ++parts;
                                rounded = true;
                                prependBlank = true;
                            }
                            else if (d >= SECOND && !rounded) {
                                if (prependBlank) {
                                    a.append(' ');
                                }
                                a.append(floor(d, SECOND));
                                a.append('s');
                                ++parts;
                                rounded = mod <= lowerCeiling(SECOND);
                                prependBlank = true;
                            }

                            if (parts < 2) {
                                d %= SECOND;

                                if (d > 0 && !rounded) {
                                    if (prependBlank) {
                                        a.append(' ');
                                    }
                                    a.append(Integer.toString((int) d));
                                    a.append('m');
                                    a.append('s');
                                }
                            }

                        }

                    }

                }
            }
        }
        catch (IOException ex) {
            // What were they thinking...
        }

        return a;
    }

    public long getDelta() {
        return delta;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TimeFormatter)) {
            return false;
        }
        return delta == ((TimeFormatter) obj).delta;
    }

    @Override
    public int hashCode() {
        return (int) (delta ^ (delta >> 32));
    }

    @Override
    public String toString() {
        return getExactly();
    }

    @Override
    public int compareTo(TimeFormatter t) {
        return delta == t.delta ? 0 : (delta < t.delta ? -1 : 1);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        delta = in.readLong();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(delta);
    }

}
