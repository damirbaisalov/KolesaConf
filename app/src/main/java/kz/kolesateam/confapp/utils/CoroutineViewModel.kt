package kz.kolesateam.confapp.utils

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class CoroutineViewModel(
    private val dispatcherContext: CoroutineContext
) : ViewModel(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext by lazy {
        dispatcherContext + job
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}