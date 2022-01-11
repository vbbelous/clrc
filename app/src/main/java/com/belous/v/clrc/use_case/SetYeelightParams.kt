package com.belous.v.clrc.use_case

import com.belous.v.clrc.data.net.YeelightSource

class SetYeelightParams {

    operator fun invoke(
        ip: String,
        port: Int,
        method: String,
        args: List<String>
    ): Map<String, String> {
        return YeelightSource.setParams(ip, port, method, args)
    }
}