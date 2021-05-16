package com.cowin.model

import kotlinx.serialization.Serializable

@Serializable
data class Centers (

	val center_id : Int,
	val name : String,
	val address : String,
	val state_name : String,
	val district_name : String,
	val block_name : String,
	val pincode : Int,
	val lat : Int,
	val long : Int,
	val from : String,
	val to : String,
	val fee_type : String,
	var sessions : MutableList<Sessions>,
	val vaccine_fees : List<VaccineFees>? = null
)