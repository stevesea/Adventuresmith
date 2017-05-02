# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/steve/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontobfuscate
-dontoptimize
-dontpreverify

-printmapping mapping.txt


# from https://github.com/cypressious/KotlinReflectionProguard,
# proguard strips off dto ctors, making jackson deserialization unhappy
-dontwarn kotlin.**
-dontwarn org.w3c.dom.events.*
-dontwarn org.jetbrains.kotlin.di.InjectorForRuntimeDescriptorLoader

-keep class kotlin.** { *; }
-keepclassmembers,allowoptimization enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    **[] $VALUES;
    public *;
}

-keepattributes InnerClasses

-ignorewarnings

-keep class org.stevesea.**
-keepclassmembers class org.stevesea.** { *; }

# make crashlytics reports more meaningful
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

-keep class android.support.v7.widget.SearchView { *; }

# kodein (kotlin dep injection)
-keepattributes Signature
