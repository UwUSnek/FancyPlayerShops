// package com.snek.framework.data_types.animations;

// import org.jetbrains.annotations.NotNull;
// import org.joml.Quaternionf;
// import org.joml.Vector3f;
// import org.joml.Vector3i;
// import org.joml.Vector4i;

// import com.snek.framework.utils.Utils;








// /**
//  * A special Transform that also holds text opacity and background values.
//  */
// public class TextTransform extends Transform {
//     protected @NotNull Vector4i _bg;
//     protected @NotNull int      _alpha;

//     /**
//      * Retrieves the background color.
//      * @return The background color.
//      */
//     public Vector4i getBg() {
//         return _bg;
//     }

//     /**
//      * Retrieves the text opacity.
//      * @return The text opacity.
//      */
//     public int getAlpha() {
//         return _alpha;
//     }




//     public TextTransform(@NotNull Vector4i __bg, int __alpha) {
//         super();
//         _bg    = __bg;
//         _alpha = __alpha;
//     }

//     public TextTransform(@NotNull Transform t, @NotNull Vector4i __bg, int __alpha) {
//         super(t._pos, t._lrot, t._scale, t._rrot);
//         System.out.println("Text transform created"); //FIXME remove debug print
//         _bg    = __bg;
//         _alpha = __alpha;
//     }

//     public TextTransform(@NotNull Vector3f __pos, @NotNull Quaternionf __lrot, @NotNull Vector3f __scale, @NotNull Quaternionf __rrot, @NotNull Vector4i __bg, int __alpha) {
//         super(__pos, __lrot, __scale, __rrot);
//         _bg    = __bg;
//         _alpha = __alpha;
//     }

//     @Override
//     public TextTransform clone() {
//         return new TextTransform(
//             new Vector3f(_pos),
//             new Quaternionf(_lrot),
//             new Vector3f(_scale),
//             new Quaternionf(_rrot),
//             new Vector4i(_bg),
//             _alpha
//         );
//     }

//     @Override
//     public TextTransform set(Transform t) {
//         super.set(t);

//         if(t instanceof TextTransform tt) {
//             setBg(tt._bg);
//             setAlpha(tt._alpha);
//         }

//         return this;
//     }




//     /**
//      * Applies a transformation to this.
//      * Background and text opacity values are left unchanged.
//      * @param t The transform to apply
//      * @return this.
//      */
//     @Override
//     public TextTransform apply(Transform t) {
//         System.out.println("Text transform apply"); //FIXME remove debug print
//         super.apply(t);
//         return this;
//     }




//     /**
//      * Applies a linear interpolation to this.
//      * @param target The target transform.
//      * @param factor The factor. Using 0 will return a copy of this, using 1 will return a copy of target
//      * @return this.
//      */
//     @Override
//     public TextTransform interpolate(Transform target, float factor) {
//         System.out.println("Text transform interpolated"); //FIXME remove debug print
//         super.interpolate(target, factor);

//         if(target instanceof TextTransform tt) {
//             setBg   (Utils.interpolateARGB(_bg, tt._bg, factor));
//             setAlpha(Utils.interpolateI   (_alpha, tt._alpha, factor));
//         }

//         return this;
//     }





//     public TextTransform setAlpha(int       a) { _alpha = a;  return this; }
//     public TextTransform setBg   (Vector4i bg) { _bg.set(bg); return this; }
// }
