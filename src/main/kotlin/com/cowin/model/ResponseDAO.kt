package com.cowin.model

import kotlinx.serialization.Serializable
import com.cowin.model.Centers

@Serializable
data class ResponseDAO (

	var centers : MutableList<Centers>
)