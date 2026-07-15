# Add project specific ProGuard rules here.
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.vacaciones.app.**$$serializer { *; }
-keepclassmembers class com.vacaciones.app.** {
    *** Companion;
}
-keepclasseswithmembers class com.vacaciones.app.** {
    kotlinx.serialization.KSerializer serializer(...);
}
