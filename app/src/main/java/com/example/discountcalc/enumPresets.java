package com.example.discountcalc;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        enumPresets.PER3,
        enumPresets.PER5,
        enumPresets.PER10,
        enumPresets.PER15,
        enumPresets.PER20,
        enumPresets.PER25,
        enumPresets.PER30,
        enumPresets.PER40,
        enumPresets.PER50,
        enumPresets.PER80,
        enumPresets.PRESET_MAX
})
@Retention(RetentionPolicy.SOURCE)
public @interface enumPresets {
    int PER3=0;
    int PER5=1;
    int PER10=2;
    int PER15=3;
    int PER20=4;
    int PER25=5;
    int PER30=6;
    int PER40=7;
    int PER50=8;
    int PER80=9;

    int PRESET_MAX=10;
}
