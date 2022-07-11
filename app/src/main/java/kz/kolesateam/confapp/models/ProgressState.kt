package kz.kolesateam.confapp.models

sealed class ProgressState {

    object Loading : ProgressState()
    object Done : ProgressState()
}
