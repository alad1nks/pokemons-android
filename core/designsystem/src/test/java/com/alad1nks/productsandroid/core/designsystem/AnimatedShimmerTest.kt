package com.alad1nks.productsandroid.core.designsystem

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.alad1nks.productsandroid.core.designsystem.components.AnimatedShimmerListItem
import com.alad1nks.productsandroid.core.designsystem.theme.ProductsAndroidTheme
import org.junit.Rule
import org.junit.Test

class AnimatedShimmerTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6
    )

    @Test
    fun snapshotLight() {
        paparazzi.snapshot {
            ProductsAndroidTheme(
                darkTheme = false
            ) {
                AnimatedShimmerListItem()
            }
        }
    }

    @Test
    fun snapshotDark() {
        paparazzi.snapshot {
            ProductsAndroidTheme(
                darkTheme = true
            ) {
                AnimatedShimmerListItem()
            }
        }
    }
}
