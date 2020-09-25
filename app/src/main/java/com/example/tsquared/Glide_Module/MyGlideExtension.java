package com.example.tsquared.Glide_Module;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.BaseRequestOptions;

@GlideExtension
public class MyGlideExtension {

    private MyGlideExtension() {}

    @NonNull
    @GlideOption
    public static BaseRequestOptions<?> roundedCorners(BaseRequestOptions<?> options, @NonNull Context context, int cornerRadius) {
        int px = Math.round(cornerRadius * (context.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return options.transforms(new RoundedCorners(px));
    }
}
