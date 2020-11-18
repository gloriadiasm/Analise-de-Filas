package com.unitri.edu.queues.domain.user

import com.unitri.edu.queues.domain.bases.BaseDto

data class UserDto (
    var email: String = "",
    var password: String = ""
): BaseDto()