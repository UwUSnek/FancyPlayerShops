package com.snek.fancyplayershops;

import net.minecraft.util.math.AffineTransformation;




public class DisplayAnimation {
    public AffineTransformation in;
    public AffineTransformation loop;
    public AffineTransformation out;

    public DisplayAnimation(AffineTransformation _in, AffineTransformation _loop, AffineTransformation _out) {
        in = _in;
        loop = _loop;
        out = _out;
    }
}
