# Sorus — ProGuard Rules

# Keep Hilt-generated classes
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }

# Keep Room entities and DAOs
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
-keep @androidx.room.Database class * { *; }

# Keep data classes used by Gson / serialization
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Timber
-dontwarn org.jetbrains.annotations.**
