package com.snek.framework.utils;

import org.joml.Vector3i;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3i;




/**
 * A simpler but more readable minecraft.text.MutableText.
 * Supports .clone().
 * Use .get() to create a MutableText from this object's data.
 */
public class Txt {
    private MutableText rawText;
    private Style style;


    public Txt() {
        rawText = Text.empty();
        style = Style.EMPTY;
    }
    public Txt(String s) {
        rawText = Text.literal(s);
        style = Style.EMPTY;
    }
    public Txt(MutableText s) {
        rawText = s.copy();
        style = rawText.getStyle();
    }
    public Txt(Text s) {
        rawText = s.copy();
        style = rawText.getStyle();
    }
    @Override
    public Txt clone(){
        rawText.setStyle(style);
        return new Txt(rawText.copy());
    }


    public Txt black    () { return color(       0); }
    public Txt blue     () { return color(     170); }
    public Txt green    () { return color(   43520); }
    public Txt aqua     () { return color(   43690); }
    public Txt darkRed  () { return color(11141120); }
    public Txt purple   () { return color(11141290); }
    public Txt gold     () { return color(16755200); }
    public Txt lightGray() { return color(11184810); }
    public Txt gray     () { return color( 5592405); }
    public Txt lightBlue() { return color( 5592575); }
    public Txt lime     () { return color( 5635925); }
    public Txt cyan     () { return color( 5636095); }
    public Txt red      () { return color(16733525); }
    public Txt magenta  () { return color(16733695); }
    public Txt yellow   () { return color(16777045); }
    public Txt white    () { return color(16777215); }


    public Txt color(int r, int g, int b) {
        style = style.withColor((r << 16) | (g << 8) | b);
        return this;
    }
    public Txt color(Vector3i rgb) {
        style = style.withColor((rgb.x << 16) | (rgb.y << 8) | rgb.z);
        return this;
    }
    public Txt color     (int rgb) { style = style.withColor        (rgb ); return this; }
    public Txt bold             () { style = style.withBold         (true); return this; }
    public Txt italic           () { style = style.withItalic       (true); return this; }
    public Txt withObfuscated   () { style = style.withObfuscated   (true); return this; }
    public Txt withStrikethrough() { style = style.withStrikethrough(true); return this; }


    public Txt cat(Text s) {
        rawText.append(s);
        return this;
    }

    public Txt cat(Txt s) {
        rawText.append(s.get());
        return this;
    }

    public Txt cat(String s) {
        return this.cat(Text.literal(s));
    }

    public Text get(){
        rawText.setStyle(style);
        return rawText;
    }
}
