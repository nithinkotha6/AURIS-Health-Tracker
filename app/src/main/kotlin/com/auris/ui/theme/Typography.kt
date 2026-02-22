package com.auris.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Inter font family — place TTF files in res/font/ named inter_regular.ttf,
// inter_medium.ttf, inter_semibold.ttf, inter_bold.ttf, then restore the
// Font(R.font.inter_*) calls below.
// TODO: restore once font files are added to res/font/
// private val interFontFamily = FontFamily(
//     Font(R.font.inter_regular,  FontWeight.Normal),
//     Font(R.font.inter_medium,   FontWeight.Medium),
//     Font(R.font.inter_semibold, FontWeight.SemiBold),
//     Font(R.font.inter_bold,     FontWeight.Bold)
// )
private val interFontFamily = FontFamily.SansSerif

val AurisTypography = Typography(
    displayLarge  = TextStyle(fontFamily = interFontFamily, fontWeight = FontWeight.Bold,    fontSize = 34.sp),
    displayMedium = TextStyle(fontFamily = interFontFamily, fontWeight = FontWeight.Bold,    fontSize = 28.sp),
    titleLarge    = TextStyle(fontFamily = interFontFamily, fontWeight = FontWeight.Bold,    fontSize = 22.sp),
    titleMedium   = TextStyle(fontFamily = interFontFamily, fontWeight = FontWeight.SemiBold,fontSize = 16.sp),
    titleSmall    = TextStyle(fontFamily = interFontFamily, fontWeight = FontWeight.Medium,  fontSize = 14.sp),
    bodyLarge     = TextStyle(fontFamily = interFontFamily, fontWeight = FontWeight.Normal,  fontSize = 16.sp),
    bodyMedium    = TextStyle(fontFamily = interFontFamily, fontWeight = FontWeight.Normal,  fontSize = 14.sp),
    bodySmall     = TextStyle(fontFamily = interFontFamily, fontWeight = FontWeight.Normal,  fontSize = 12.sp),
    labelSmall    = TextStyle(fontFamily = interFontFamily, fontWeight = FontWeight.Medium,  fontSize = 10.sp, letterSpacing = 0.5.sp)
)
