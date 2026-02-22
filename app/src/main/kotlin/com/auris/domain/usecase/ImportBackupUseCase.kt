package com.auris.domain.usecase

import java.io.InputStream
import javax.inject.Inject

/**
 * ImportBackupUseCase — Phase 12 stub.
 * Will read a backup from the given stream and restore DB/state.
 */
class ImportBackupUseCase @Inject constructor() {

    suspend operator fun invoke(input: InputStream): Result<Unit> = Result.success(Unit)
}
