package com.cowin.model

import kotlinx.serialization.Serializable
import org.jetbrains.annotations.Nullable

@Serializable
data class Sessions (

	val session_id : String,
	val date : String,
	val available_capacity : Float,
	val min_age_limit : Int,
	val vaccine : String,
	val slots : List<String>,
	val available_capacity_dose1: String,
	val available_capacity_dose2: String,


)