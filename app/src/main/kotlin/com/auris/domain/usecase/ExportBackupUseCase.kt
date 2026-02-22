package com.auris.domain.usecase

import java.io.OutputStream
import javax.inject.Inject

/**
 * ExportBackupUseCase — Phase 12 stub.
 * Will write a backup (e.g. JSON or encrypted bundle) to the given stream.
 */
class ExportBackupUseCase @Inject constructor() {

    suspend operator fun invoke(output: OutputStream): Result<Unit> = Result.success(Unit)
}
