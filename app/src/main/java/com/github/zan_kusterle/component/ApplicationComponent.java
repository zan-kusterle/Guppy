package com.github.zan_kusterle.component;

import com.github.zan_kusterle.module.AndroidModule;
import com.github.zan_kusterle.module.GuppyModule;
import com.github.zan_kusterle.presenter.ClientPlaylistPresenter;
import com.github.zan_kusterle.presenter.HostPlaylistPresenter;
import com.github.zan_kusterle.presenter.HostSelectorPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by bencz on 2016. 12. 12..
 */
@Singleton
@Component(modules = {AndroidModule.class, GuppyModule.class})
public interface ApplicationComponent {
    HostSelectorPresenter provideHostSelectorPresenter();
    HostPlaylistPresenter provideHostPlaylistPresenter();
    ClientPlaylistPresenter provideClientPlaylistPresenter();
}
