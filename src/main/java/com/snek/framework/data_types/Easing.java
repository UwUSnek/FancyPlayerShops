package com.snek.framework.data_types;

import com.snek.framework.utils.Utils;

public final class Easing {
    public Easing() { throw new UnsupportedOperationException("Utility class \"Easing\" cannot be instantiated"); }

    public static double linear (final double x) { return x; }


    public static double sineIn    (final double x) { return 1 - Math.cos((x * Math.PI) / 2); }
    public static double sineOut   (final double x) { return     Math.sin((x * Math.PI) / 2); }
    public static double sineInOut (final double x) { return   -(Math.cos( x * Math.PI) - 1) / 2; }


    public static double cubicIn    (final double x) { return     Math.pow(    x, 3); }
    public static double cubicOut   (final double x) { return 1 - Math.pow(1 - x, 3); }
    public static double cubicInOut (final double x) { return x < 0.5 ? 4 * Math.pow(x, 3) : 1 - Math.pow(-2 * x + 2, 3) / 2; }


    public static double bounceIn  (final double x) { return 1 - bounceOut(1 - x); }
    public static double bounceOut (double x) {
        final double n = 7.5625;
        final double d = 2.75;
        if      (x < 1   / d)                   return n * x * x;
        else if (x < 2   / d) { x -=   1.5 / d; return n * x * x + 0.75;     }
        else if (x < 2.5 / d) { x -=  2.25 / d; return n * x * x + 0.9375;   }
        else                  { x -= 2.625 / d; return n * x * x + 0.984375; }
    }

    public static double bounceInOut (final double x) {
        return x < 0.5 ? (1 - bounceOut(1 - 2 * x)) / 2 : (1 + bounceOut(2 * x - 1)) / 2;
    }



    public static double elasticIn   (final double x) {
        return Utils.doubleEquals(x, 0, 0.001) ? 0 : (Utils.doubleEquals(x, 1, 0.001) ? 1 : -Math.pow(2,  10 * x - 10) * Math.sin((x * 10 - 10.75) * ((2 * Math.PI) / 3)));
    }
    public static double elasticOut  (final double x) {
        return Utils.doubleEquals(x, 0, 0.001) ? 0 : (Utils.doubleEquals(x, 1, 0.001) ? 1 :  Math.pow(2, -10 * x     ) * Math.sin((x * 10 -  0.75) * ((2 * Math.PI) / 3)) + 1);
    }
    public static double elasticInOut(final double x) {
        final double c = Math.sin(20 * x - 11.125) * (2 * Math.PI) / 4.5;
        return Utils.doubleEquals(x, 0, 0.001) ? 0 : (Utils.doubleEquals(x, 1, 0.001) ? 1 : (
            x < 0.5 ?
                -(Math.pow(2,  20 * x - 10) * c) / 2 :
                (Math.pow(2, -20 * x + 10) * c) / 2 + 1
        ));
    }
}
