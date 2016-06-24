package com.devil07.divyang.okhttplib;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

        public class GlideConfiguration implements GlideModule {

                @Override
        public void applyOptions(Context context, GlideBuilder builder) {
                // Apply options to the builder here.
                        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);  //glide processes image into rgb so to convert the format to argb
                                                                                 //this class is provided as metadata in the manifest
            }

                @Override
        public void registerComponents(Context context, Glide glide) {
                // register ModelLoaders here.
                    }
    }