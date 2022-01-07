package com.belous.v.clrc.use_case

import com.belous.v.clrc.data.net.YeelightSource

class FindNewDevices {

    operator fun invoke(): List<Map<String, String>> {
        return YeelightSource.searchDevices()
    }
}