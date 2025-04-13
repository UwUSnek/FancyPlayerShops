package com.snek.framework.utils;





public final class Easings {
    private Easings(){}

    public static final Easing linear       = new Easing(Easings::_linear      );
    public static final Easing sineIn       = new Easing(Easings::_sineIn      );
    public static final Easing sineOut      = new Easing(Easings::_sineOut     );
    public static final Easing sineInOut    = new Easing(Easings::_sineInOut   );
    public static final Easing cubicIn      = new Easing(Easings::_cubicIn     );
    public static final Easing cubicOut     = new Easing(Easings::_cubicOut    );
    public static final Easing cubicInOut   = new Easing(Easings::_cubicInOut  );
    public static final Easing bounceIn     = new Easing(Easings::_bounceIn    );
    public static final Easing bounceInOut  = new Easing(Easings::_bounceInOut );
    public static final Easing elasticIn    = new Easing(Easings::_elasticIn   );
    public static final Easing elasticOut   = new Easing(Easings::_elasticOut  );
    public static final Easing elasticInOut = new Easing(Easings::_elasticInOut);




    private static double _linear    (final Double x) { return x; }

    private static double _sineIn    (final double x) { return 1 - Math.cos((x * Math.PI) / 2); }
    private static double _sineOut   (final double x) { return     Math.sin((x * Math.PI) / 2); }
    private static double _sineInOut (final double x) { return   -(Math.cos( x * Math.PI) - 1) / 2; }

    private static double _cubicIn    (final double x) { return     Math.pow(    x, 3); }
    private static double _cubicOut   (final double x) { return 1 - Math.pow(1 - x, 3); }
    private static double _cubicInOut (final double x) { return x < 0.5 ? 4 * Math.pow(x, 3) : 1 - Math.pow(-2 * x + 2, 3) / 2; }


    private static double _bounceIn  (final double x) { return 1 - _bounceOut(1 - x); }
    private static double _bounceOut (double x) {
        final double n = 7.5625;
        final double d = 2.75;
        if      (x < 1   / d)                   return n * x * x;
        else if (x < 2   / d) { x -=   1.5 / d; return n * x * x + 0.75;     }
        else if (x < 2.5 / d) { x -=  2.25 / d; return n * x * x + 0.9375;   }
        else                  { x -= 2.625 / d; return n * x * x + 0.984375; }
    }

    private static double _bounceInOut (final double x) {
        return x < 0.5 ? (1 - _bounceOut(1 - 2 * x)) / 2 : (1 + _bounceOut(2 * x - 1)) / 2;
    }




    private static double _elasticIn (final double x) {
        return
            Utils.doubleEquals(x, 0, 0.001) ? 0 : (
            Utils.doubleEquals(x, 1, 0.001) ? 1 :
                -Math.pow(2, 10 * x - 10) * Math.sin((x * 10 - 10.75) * ((2 * Math.PI) / 3))
            )
        ;
    }

    private static double _elasticOut (final double x) {
        return
            Utils.doubleEquals(x, 0, 0.001) ? 0 : (
            Utils.doubleEquals(x, 1, 0.001) ? 1 :
                Math.pow(2, -10 * x) * Math.sin((x * 10 - 0.75) * ((2 * Math.PI) / 3)) + 1
            )
        ;
    }

    private static double _elasticInOut(final double x) {
        final double c = Math.sin(20 * x - 11.125) * (2 * Math.PI) / 4.5;
        return
            Utils.doubleEquals(x, 0, 0.001) ? 0 : (
            Utils.doubleEquals(x, 1, 0.001) ? 1 : (
                x < 0.5 ?
                -(Math.pow(2, +20 * x - 10) * c) / 2 :
                +(Math.pow(2, -20 * x + 10) * c) / 2 + 1
            ))
        ;
    }
}
