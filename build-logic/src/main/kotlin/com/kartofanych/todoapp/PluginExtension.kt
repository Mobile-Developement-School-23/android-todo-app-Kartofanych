package com.kartofanych.todoapp

import org.gradle.api.provider.Property

interface PluginExtension {

    val fileSizeLimitInMb: Property<Int>

    val enableSizeCheck: Property<Boolean>

}