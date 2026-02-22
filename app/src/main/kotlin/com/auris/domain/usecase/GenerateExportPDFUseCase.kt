package com.auris.domain.usecase

import javax.inject.Inject

/**
 * GenerateExportPDFUseCase — Phase 12 stub.
 * Will generate a PDF report (e.g. iText7) for export from Profile.
 */
class GenerateExportPDFUseCase @Inject constructor() {

    /**
     * @return Path to the generated PDF file, or null if not implemented / error.
     */
    suspend operator fun invoke(): String? = null
}
