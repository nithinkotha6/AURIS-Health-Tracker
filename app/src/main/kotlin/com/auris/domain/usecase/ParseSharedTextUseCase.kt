package com.auris.domain.usecase

import javax.inject.Inject

/**
 * ParseSharedTextUseCase — regex fallback for plain text shared from AI apps.
 *
 * Phase 8: when AI app shares text (via ACTION_SEND) instead of an auris:// link,
 * this use case searches for the link in the text body.
 *
 * Fallback chain:
 * 1. Look for auris:// URL in text
 * 2. Return raw text for manual parsing if no link found
 */
class ParseSharedTextUseCase @Inject constructor(
    private val parseDeepLink: ParseDeepLinkUseCase
) {

    sealed class SharedTextResult {
        /** Found and successfully parsed an auris:// link */
        data class DeepLinkFound(val result: ParseDeepLinkUseCase.ParseResult.Success) : SharedTextResult()

        /** No auris:// link found; raw text available for display */
        data class NoLinkFound(val rawText: String) : SharedTextResult()

        /** Link found but failed to parse */
        data class ParseError(val reason: String) : SharedTextResult()
    }

    operator fun invoke(sharedText: String): SharedTextResult {
        if (sharedText.isBlank()) return SharedTextResult.NoLinkFound("")

        // Search for auris:// URL in the text
        val linkRegex = Regex("""auris://log\?[^\s]+""")
        val match = linkRegex.find(sharedText)

        if (match != null) {
            val uri = match.value
            return when (val result = parseDeepLink(uri)) {
                is ParseDeepLinkUseCase.ParseResult.Success ->
                    SharedTextResult.DeepLinkFound(result)
                is ParseDeepLinkUseCase.ParseResult.Failure ->
                    SharedTextResult.ParseError(result.reason)
            }
        }

        return SharedTextResult.NoLinkFound(sharedText)
    }
}
