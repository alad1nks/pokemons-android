package com.alad1nks.productsandroid.core.designsystem

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.alad1nks.productsandroid.core.designsystem.components.ErrorScreen
import com.alad1nks.productsandroid.core.designsystem.theme.ProductsAndroidTheme
import org.junit.Rule
import org.junit.Test

class ErrorScreenTest {
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
                ErrorScreen(onTryAgainClick = {  })
            }
        }
    }

    @Test
    fun snapshotDark() {
        paparazzi.snapshot {
            ProductsAndroidTheme(
                darkTheme = true
            ) {
                ErrorScreen(onTryAgainClick = {  })
            }
        }
    }
}
