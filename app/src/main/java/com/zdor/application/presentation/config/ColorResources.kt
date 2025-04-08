package com.zdor.application.presentation.config

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.zdor.application.R

object ColorResources {
    @Composable
    fun purpleBackground() = colorResource(id = R.color.purple_background)

    @Composable
    fun white() = colorResource(id = R.color.white)

    @Composable
    fun darkBlue() = colorResource(id = R.color.dark_blue)

    @Composable
    fun lightPurple() = colorResource(id = R.color.light_blue)

    @Composable
    fun black() = colorResource(id = R.color.black)

    @Composable
    fun red() = colorResource(id = R.color.red)

    @Composable
    fun transparent() = colorResource(id = R.color.transparent)

    @Composable
    fun gray() = colorResource(id = R.color.gray)
}