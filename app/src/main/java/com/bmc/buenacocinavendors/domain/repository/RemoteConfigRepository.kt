package com.bmc.buenacocinavendors.domain.repository

import com.bmc.buenacocinavendors.core.CUCEI_AREA_BOUNDS_ON_GMAPS_NAME
import com.bmc.buenacocinavendors.core.CUCEI_CENTER_ON_GMAPS_NAME
import com.bmc.buenacocinavendors.core.DEFAULT_CUCEI_CENTER_ON_GMAPS_NAME
import com.bmc.buenacocinavendors.core.DEFAULT_LATITUDE_CUCEI_CENTER_ON_GMAPS
import com.bmc.buenacocinavendors.core.DEFAULT_LONGITUDE_CUCEI_CENTER_ON_GMAPS
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

class RemoteConfigRepository @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) {
    private val _cuceiCenterOnMap = MutableStateFlow<Pair<String, LatLng>?>(null)
    val cuceiCenterOnMap: Flow<Pair<String, LatLng>?> = _cuceiCenterOnMap
    private val _cuceiAreaBoundsOnMap = MutableStateFlow<List<Pair<String, LatLng>>?>(null)
    val cuceiAreaBoundsOnMap: Flow<List<Pair<String, LatLng>>?> = _cuceiAreaBoundsOnMap

    init {
        _cuceiCenterOnMap.value = fetchCuceiCenter()
        _cuceiAreaBoundsOnMap.value = fetchCuceiAreaBounds()
        fetchCuceiLocationData()
    }

    private fun fetchCuceiLocationData() {
        firebaseRemoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                firebaseRemoteConfig.activate().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (configUpdate.updatedKeys.contains(CUCEI_CENTER_ON_GMAPS_NAME)) {
                            _cuceiCenterOnMap.value = fetchCuceiCenter()
                        }
                        if (configUpdate.updatedKeys.contains(CUCEI_AREA_BOUNDS_ON_GMAPS_NAME)) {
                            _cuceiAreaBoundsOnMap.value = fetchCuceiAreaBounds()
                        }
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                println("Remote config error: ${error.message}")
            }
        })
    }

    private fun fetchCuceiCenter(): Pair<String, LatLng> {
        val jsonStr = firebaseRemoteConfig.getString(CUCEI_CENTER_ON_GMAPS_NAME)
        val centerObj = Json.parseToJsonElement(jsonStr).jsonObject
        val name = centerObj["name"]?.jsonPrimitive?.content ?: DEFAULT_CUCEI_CENTER_ON_GMAPS_NAME
        val lat = centerObj["lat"]?.jsonPrimitive?.double ?: DEFAULT_LATITUDE_CUCEI_CENTER_ON_GMAPS
        val lng = centerObj["lng"]?.jsonPrimitive?.double ?: DEFAULT_LONGITUDE_CUCEI_CENTER_ON_GMAPS
        return name to LatLng(lat, lng)
    }

    private fun fetchCuceiAreaBounds(): List<Pair<String, LatLng>> {
        val jsonStr = firebaseRemoteConfig.getString(CUCEI_AREA_BOUNDS_ON_GMAPS_NAME)
        val bounds = mutableListOf<Pair<String, LatLng>>()
        val jsonArray = Json.parseToJsonElement(jsonStr).jsonArray
        for (point in jsonArray) {
            val pointObj = point.jsonObject
            val name = pointObj["name"]?.jsonPrimitive?.content ?: "Unknown"
            val lat = pointObj["lat"]?.jsonPrimitive?.double ?: 0.0
            val lng = pointObj["lng"]?.jsonPrimitive?.double ?: 0.0
            bounds.add(name to LatLng(lat, lng))
        }
        return bounds
    }
}