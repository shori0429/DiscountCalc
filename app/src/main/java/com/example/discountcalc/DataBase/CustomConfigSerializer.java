package com.example.discountcalc.DataBase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.core.Serializer;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.datastore.rxjava3.RxDataStoreBuilder;


import com.example.discountcalc.CustomConfigData;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kotlin.Unit;
import kotlin.coroutines.Continuation;

public class CustomConfigSerializer implements Serializer<CustomConfigData> {

    private int price;
    private int perLabel;
    private int per;
    CustomConfigData customConfigData=CustomConfigData.getDefaultInstance();

    @Override
    public CustomConfigData getDefaultValue() {
        return CustomConfigData.getDefaultInstance();
    }

    @Nullable
    @Override
    public CustomConfigData readFrom(@NonNull InputStream input, @NonNull Continuation<? super CustomConfigData> continuation) {
        try{
            return CustomConfigData.parseFrom(input);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read proto.",e);
        }
    }

    @Nullable
    @Override
    public Object writeTo(CustomConfigData customConfigData, @NonNull OutputStream outputStream, @NonNull Continuation<? super Unit> continuation) {
        try {
            customConfigData.writeTo(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



}
