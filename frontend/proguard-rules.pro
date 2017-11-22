-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Remove source file name from stack traces
-renamesourcefileattribute SourceFile
# Preserve the line number information
-keepattributes SourceFile,LineNumberTable

# Jackson annotations
-keep class com.fasterxml.jackson.annotation.** {
    *;
}

# Jackson databind
-dontwarn com.fasterxml.jackson.databind.**

-keepattributes *Annotation*,EnclosingMethod,Signature

-dontwarn io.reactivex.**

-dontwarn okio.**
-dontwarn okhttp3.**
