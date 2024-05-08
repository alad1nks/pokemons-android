package com.alad1nks.productsandroid.core.designsystem

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.alad1nks.productsandroid.core.designsystem.components.SearchBar
import com.alad1nks.productsandroid.core.designsystem.theme.ProductsAndroidTheme
import org.junit.Rule
import org.junit.Test

class SearchBarTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6
    )

    @Test
    fun snapshotEmptyLight() {
        paparazzi.snapshot {
            ProductsAndroidTheme(
                darkTheme = false
            ) {
                SearchBar(value = "", onValueChange = {  }, onSearchClose = {  })
            }
        }
    }

    @Test
    fun snapshotNotEmptyLight() {
        paparazzi.snapshot {
            ProductsAndroidTheme(
                darkTheme = false
            ) {
                SearchBar(value = "Value", onValueChange = {  }, onSearchClose = {  })
            }
        }
    }

    @Test
    fun snapshotEmptyDark() {
        paparazzi.snapshot {
            ProductsAndroidTheme(
                darkTheme = true
            ) {
                SearchBar(value = "", onValueChange = {  }, onSearchClose = {  })
            }
        }
    }

    @Test
    fun snapshotNotEmptyDark() {
        paparazzi.snapshot {
            ProductsAndroidTheme(
                darkTheme = true
            ) {
                SearchBar(value = "Value", onValueChange = {  }, onSearchClose = {  })
            }
        }
    }
}
