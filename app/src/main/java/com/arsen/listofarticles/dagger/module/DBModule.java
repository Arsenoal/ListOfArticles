package com.arsen.listofarticles.dagger.module;

import android.content.Context;

import com.arsen.listofarticles.database.DbHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DBModule {
    private Context context;

    public DBModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    DbHelper provideDBHelper() {
        return new DbHelper(context);
    }
}
