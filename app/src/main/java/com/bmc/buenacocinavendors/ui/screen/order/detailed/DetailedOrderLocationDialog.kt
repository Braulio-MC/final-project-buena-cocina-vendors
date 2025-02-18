package com.bmc.buenacocinavendors.ui.screen.order.detailed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bmc.buenacocinavendors.domain.model.InsightTopLocationDomain
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.TileOverlay
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.heatmaps.HeatmapTileProvider

@Composable
fun DetailedOrderLocationDialog(
    isDialogOpen: Boolean,
    isLoadingUserLocation: Boolean,
    isTopLocationsOnMapLoading: Boolean,
    cuceiCenter: Pair<String, LatLng>,
    cuceiBounds: List<Pair<String, LatLng>>,
    cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cuceiCenter.second, 16.5f)
    },
    orderLocation: LatLng?,
    currentUserLocation: LatLng?,
    topLocationsOnMap: List<InsightTopLocationDomain>,
    onDismiss: () -> Unit
) {
    if (isDialogOpen) {
        var isMapLoaded by remember { mutableStateOf(false) }
        val cBounds = cuceiBounds.map { it.second }
        val weightedData = topLocationsOnMap.map { it.geopoint }
        var heatMapTileProvider: HeatmapTileProvider? = null
        if (!isTopLocationsOnMapLoading && weightedData.isNotEmpty()) {
            heatMapTileProvider = HeatmapTileProvider.Builder()
                .weightedData(weightedData)
                .radius(50)
                .build()
        }

        Dialog(
            onDismissRequest = onDismiss
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp)
            ) {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapLoaded = { isMapLoaded = true }
                ) {
                    if (isMapLoaded) {
                        Polygon(
                            points = cBounds,
                            strokeColor = Color.DarkGray,
                            strokeWidth = 5f,
                            fillColor = Color.DarkGray.copy(alpha = 0.1f)
                        )
                        heatMapTileProvider?.let {
                            TileOverlay(
                                tileProvider = heatMapTileProvider,
                                transparency = 0.5f,
                                onClick = { tile ->
                                    if (tile.transparency == 1f) {
                                        tile.transparency = 0.5f
                                    } else {
                                        tile.transparency = 1f
                                    }
                                }
                            )
                        }
                        orderLocation?.let { loc ->
                            Marker(
                                state = MarkerState(position = loc),
                                title = "Ubicacion del pedido",
                                snippet = "Aqui tienes que entregar el pedido"
                            )
                        }
                        currentUserLocation?.let { location ->
                            Marker(
                                state = MarkerState(position = location),
                                title = "Ubicacion actual",
                                snippet = "Estas aqui",
                                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                            )
                        }
                    }
                }
                if (isLoadingUserLocation) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .size(40.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize(),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(25.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}