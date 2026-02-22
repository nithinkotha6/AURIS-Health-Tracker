package com.auris.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auris.domain.usecase.ExportBackupUseCase
import com.auris.domain.usecase.GenerateExportPDFUseCase
import com.auris.domain.usecase.ImportBackupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

/**
 * ProfileViewModel — Phase 12: Export report and Backup/Restore actions.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val generateExportPDFUseCase: GenerateExportPDFUseCase,
    private val exportBackupUseCase: ExportBackupUseCase,
    private val importBackupUseCase: ImportBackupUseCase
) : ViewModel() {

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events: SharedFlow<String> = _events.asSharedFlow()

    fun exportReport() {
        viewModelScope.launch {
            val path = generateExportPDFUseCase()
            _events.emit(
                if (path != null) "Report saved to $path" else "Export report (Phase 12 – coming soon)"
            )
        }
    }

    fun backup(output: OutputStream) {
        viewModelScope.launch {
            exportBackupUseCase(output).fold(
                onSuccess = { _events.emit("Backup saved (Phase 12 – stub)") },
                onFailure = { _events.emit("Backup failed: ${it.message}") }
            )
        }
    }

    fun backupStub() {
        viewModelScope.launch {
            backup(java.io.ByteArrayOutputStream())
        }
    }

    fun restore(input: InputStream) {
        viewModelScope.launch {
            importBackupUseCase(input).fold(
                onSuccess = { _events.emit("Restore complete (Phase 12 – stub)") },
                onFailure = { _events.emit("Restore failed: ${it.message}") }
            )
        }
    }

    fun restoreStub() {
        viewModelScope.launch {
            _events.emit("Restore: choose backup file (Phase 12 – coming soon)")
        }
    }
}
