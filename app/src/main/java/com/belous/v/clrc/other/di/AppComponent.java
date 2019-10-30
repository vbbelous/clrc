package com.belous.v.clrc.other.di;


import com.belous.v.clrc.view.dialog.BaseDialog;
import com.belous.v.clrc.view.dialog.FoundDialog;
import com.belous.v.clrc.view.fragment.InfoFragment;
import com.belous.v.clrc.view.fragment.ListFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(FoundDialog foundDialog);

    void inject(BaseDialog baseDialog);

    void inject(ListFragment listFragment);

    void inject(InfoFragment infoFragment);
}
