package com.mahmoud.musemap.presentation.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoud.musemap.presentation.home.HomeViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNoteClick: (Long) -> Unit
) {
    val context = LocalContext.current

    val notesState = viewModel.notes.collectAsState(initial = emptyList()).value

    remember {
        Configuration.getInstance().userAgentValue = context.packageName
    }

    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(9.5)

                controller.setCenter(GeoPoint(51.899, -2.078))
            }
        },
        update = { mapView ->
            mapView.overlays.clear()

            notesState.forEach { note ->
                if (note.latitude != null && note.longitude != null) {
                    val marker = Marker(mapView)
                    marker.position = GeoPoint(note.latitude, note.longitude)
                    marker.title = note.title
                    marker.snippet = note.content
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                    marker.setOnMarkerClickListener { _, _ ->
                        onNoteClick(note.id)
                        true
                    }

                    mapView.overlays.add(marker)
                }
            }

            mapView.invalidate()
        },
        modifier = Modifier.fillMaxSize()
    )
}