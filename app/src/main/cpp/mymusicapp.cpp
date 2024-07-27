#include <jni.h>

// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("mymusicapp");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("mymusicapp")
//      }
//    }

#include <string>
#include "map"
std::wstring jstringToStdWstring(JNIEnv *env, jstring jstr) {
    if (jstr == nullptr) {
        return L"";
    }
    const jchar *chars = env->GetStringChars(jstr, nullptr);
    jsize length = env->GetStringLength(jstr);

    std::wstring str(reinterpret_cast<const wchar_t *>(chars), length);

    env->ReleaseStringChars(jstr, chars);

    return str;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_mymusicapp_MainActivity_stringComparison(JNIEnv *env, jobject thiz, jstring str1,
                                                          jstring str2) {

    std::wstring wstr1 = jstringToStdWstring(env, str1);
    std::wstring wstr2 = jstringToStdWstring(env, str2);

    std::map<wchar_t, wchar_t> m;
    m[L'ă'] = L'a';


    return static_cast<jboolean>(true);
}