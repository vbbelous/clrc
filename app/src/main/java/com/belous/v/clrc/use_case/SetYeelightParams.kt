package com.belous.v.clrc.use_case

import com.belous.v.clrc.data.net.YeelightSource
import java.util.*

class SetYeelightParams {

    operator fun invoke(ip: String, port: Int, args: Queue<String>): Map<String, String> {
        return YeelightSource.setParams(ip, port, args)
    }
}