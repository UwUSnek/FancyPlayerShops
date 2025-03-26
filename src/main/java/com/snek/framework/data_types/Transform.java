package com.snek.framework.data_types;

import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import net.minecraft.util.math.AffineTransformation;








/**
 * A better net.minecraft.util.math.AffineTransformation.
 * Supports .clone().
 * Use .get() to create an AffineTransformation from this Transform's data.
 */
public class Transform {
    private @NotNull Vector3f    _pos;
    private @NotNull Quaternionf _lrot;
    private @NotNull Vector3f    _scale;
    private @NotNull Quaternionf _rrot;

    /**
     * Creates a new AffineTransformation using the current translation, left rotation, scale and right rotation values.
     * @return The transformation.
     */
    public AffineTransformation get() {
        return new AffineTransformation(_pos, _lrot, _scale, _rrot);
    }

    public Transform() {
        _pos   = new Vector3f(0.0f);
        _lrot  = new Quaternionf();
        _scale = new Vector3f(1.0f);
        _rrot  = new Quaternionf();
    }

    public Transform(@NotNull Vector3f __pos, @NotNull Quaternionf __lrot, @NotNull Vector3f __scale, @NotNull Quaternionf __rrot) {
        _pos   = new Vector3f(__pos);
        _lrot  = new Quaternionf(__lrot);
        _scale = new Vector3f(__scale);
        _rrot  = new Quaternionf(__rrot);
    }

    @Override
    public Transform clone() {
        return new Transform(
            new Vector3f(_pos),
            new Quaternionf(_lrot),
            new Vector3f(_scale),
            new Quaternionf(_rrot)
        );
    }




    /**
     * Applies a transformation to this transform.
     * @param t The transform to apply
     */
    public void apply(Transform t) {
        move(t._pos);
        rot(t._lrot);
        scale(t._scale);
        rrot(t._rrot);
    }




    public void interpolate(Transform target, float factor) {
        _pos  .lerp (target._pos,   factor);
        _lrot .slerp(target._lrot,  factor);
        _scale.lerp (target._scale, factor);
        _rrot .slerp(target._rrot,  factor);
    }





    // Left rotation
    public Transform rotX        (float x                  ) { _lrot.rotateX(x);                                  return this; }
    public Transform rotY        (float y                  ) { _lrot.rotateY(y);                                  return this; }
    public Transform rotZ        (float z                  ) { _lrot.rotateZ(z);                                  return this; }
    public Transform rot         (float x, float y, float z) { rotX(x); rotY(y); rotZ(z);                         return this; }
    public Transform rot         (Quaternionf r            ) { _lrot.mul(r);                                      return this; }

    //TODO
    // public Transform setRotX  (float x                  ) { _lrot.rotateX(x);                                  return this; }
    // public Transform setRotY  (float y                  ) { _lrot.rotateY(y);                                  return this; }
    // public Transform setRotZ  (float z                  ) { _lrot.rotateZ(z);                                  return this; }
    // public Transform setRot   (float x, float y, float z) { rotX(x); rotY(y); rotZ(z);                         return this; }


    // Translation
    public Transform moveX       (float x                  ) { _pos.x += x;                                       return this; }
    public Transform moveY       (float y                  ) { _pos.y += y;                                       return this; }
    public Transform moveZ       (float z                  ) { _pos.z += z;                                       return this; }
    public Transform move        (float x, float y, float z) { moveX(x); moveY(y); moveZ(z);                      return this; }
    public Transform move        (Vector3f s               ) { _pos.add(s);                                       return this; }

    public Transform setPosX     (float x                  ) { _pos.x = x;                                        return this; }
    public Transform setPosY     (float y                  ) { _pos.y = y;                                        return this; }
    public Transform setPosZ     (float z                  ) { _pos.z = z;                                        return this; }
    public Transform setPos      (float x, float y, float z) { setPosX(x); setPosY(y); setPosZ(z);                return this; }


    // Scale
    public Transform scaleX      (float x                  ) { _scale.x *= x;                                     return this; }
    public Transform scaleY      (float y                  ) { _scale.y *= y;                                     return this; }
    public Transform scaleZ      (float z                  ) { _scale.z *= z;                                     return this; }
    public Transform scale       (float x, float y, float z) { scaleX(x); scaleY(y); scaleZ(z);                   return this; }
    public Transform scale       (float n                  ) { scale(n, n, n);                                    return this; }
    public Transform scale       (Vector3f s               ) { _scale.mul(s);                                     return this; }

    public Transform setScaleX   (float x                  ) { _scale.x = x;                                      return this; }
    public Transform setScaleY   (float y                  ) { _scale.y = y;                                      return this; }
    public Transform setScaleZ   (float z                  ) { _scale.z = z;                                      return this; }
    public Transform setScale    (float x, float y, float z) { setScaleX(x); setScaleY(y); setScaleZ(z);          return this; }
    public Transform setScale    (float n                  ) { setScale(n, n, n);                                 return this; }


    // Right rotation
    public Transform rrotX       (float x                  ) { _rrot.rotateX(x);                                  return this; }
    public Transform rrotY       (float y                  ) { _rrot.rotateY(y);                                  return this; }
    public Transform rrotZ       (float z                  ) { _rrot.rotateZ(z);                                  return this; }
    public Transform rrot        (float x, float y, float z) { rrotX(x); rrotY(y); rrotZ(z);                      return this; }
    public Transform rrot        (Quaternionf r            ) { _rrot.mul(r);                                      return this; }

    //TODO
    // public Transform setRrotX (float x                  ) { _rrot.setAngleAxis(x);                             return this; }
    // public Transform setRrotY (float y                  ) { _rrot.setAngleAxis(y);                             return this; }
    // public Transform setRrotZ (float z                  ) { _rrot.setAngleAxis(z);                             return this; }
    // public Transform setRrot  (float x, float y, float z) { rrotX(x); rrotY(y); rrotZ(z);                      return this; }
}
