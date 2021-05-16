package com.cowin.model

import kotlinx.serialization.Serializable

@Serializable
class VaccineFees(
    val vaccine: String,
    val fee: String
)