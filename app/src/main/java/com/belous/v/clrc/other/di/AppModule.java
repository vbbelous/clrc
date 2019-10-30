package com.belous.v.clrc.other.di;

import android.content.Context;

import androidx.room.Room;

import com.belous.v.clrc.model.db.AppDatabase;
import com.belous.v.clrc.model.db.ItemDao;
import com.belous.v.clrc.presenter.InfoPresenter;
import com.belous.v.clrc.presenter.InfoPresenterImpl;
import com.belous.v.clrc.presenter.ListPresenter;
import com.belous.v.clrc.presenter.ListPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class AppModule {

    private Context context;

    AppModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    Context getContext() {
        return context;
    }

    @Singleton
    @Provides
    AppDatabase provideAppDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
    }

    @Singleton
    @Provides
    ItemDao provideItemDao(AppDatabase appDatabase) {
        return appDatabase.itemDao();
    }

    @Singleton
    @Provides
    ListPresenter provideListPresenter(ItemDao itemDao) {
        return new ListPresenterImpl(itemDao);
    }

    @Singleton
    @Provides
    InfoPresenter provideInfoPresenter(ItemDao itemDao) {
        return new InfoPresenterImpl(itemDao);
    }
}
