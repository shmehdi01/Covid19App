package app.shmehdi.covid19app.network.models

import com.google.gson.annotations.SerializedName

data class Countries (

	@SerializedName("Country") val country : String,
	@SerializedName("CountryCode") val countryCode : String,
	@SerializedName("Slug") val slug : String,
	@SerializedName("NewConfirmed") val newConfirmed : Int,
	@SerializedName("TotalConfirmed") val totalConfirmed : Int,
	@SerializedName("NewDeaths") val newDeaths : Int,
	@SerializedName("TotalDeaths") val totalDeaths : Int,
	@SerializedName("NewRecovered") val newRecovered : Int,
	@SerializedName("TotalRecovered") val totalRecovered : Int,
	@SerializedName("Date") val date : String
){

	override fun equals(other: Any?): Boolean {
		if(other is String) {
			return countryCode == other
		}
		if(other is Countries) {
			return countryCode == other.countryCode
		}
		return super.equals(other)
	}
}