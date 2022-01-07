package com.belous.v.clrc.use_case

import com.belous.v.clrc.data.net.YeelightSource

class GetYeelightParams {

    operator fun invoke(ip: String, port: Int): Map<String, String> {
        return YeelightSource.getParams(ip, port)
    }
}