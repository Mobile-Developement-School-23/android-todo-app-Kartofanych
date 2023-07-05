package com.example.todoapp.di;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(dependencies = {}, modules = {ApplicationComponent.class, DatabaseModule.class,})
interface ApplicationComponent {

}