/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

#include <stdio.h>

#include "jni.h"
#include "cc_squirreljme_jvm_Assembly.h"

extern "C"
{

JNIEXPORT void JNICALL notImplemented()
{
	fprintf(stderr, "Not implemented.\n");
}

/*****************************************************************************/
/*****************************************************************************/
/*****************************************************************************/

#define CAST_OBJECT_TO_LONG(v) ((jlong)((uintptr_t)(v)))
#define CAST_LONG_TO_OBJECT(v) ((jobject)(uintptr_t)(v))

static jint javaIsInstance(JNIEnv* env, jobject object, const char* name)
{
	jclass classy;

	// See if the class exists before we check
	classy = env->FindClass(name);
	if (classy == NULL)
		return JNI_FALSE;

	return env->IsInstanceOf(object, classy);
}

#define javaTossIllegalArgumentException(env) javaTossIllegalArgumentExceptionReal((env), __func__)
static void javaTossIllegalArgumentExceptionReal(JNIEnv* env, char* why)
{
	jclass classy;

	// See if the class exists before we check
	classy = env->FindClass("java/lang/IllegalArgumentException");
	if (classy == NULL)
		return;

	// Throw it with our method name
	env->ThrowNew(classy, (why == NULL ? "No reason given" : why));
}

/** Called when the library is loaded. */
JNIEXPORT jint JNICALL JNI_OnLoad
	(JavaVM* vm, void* reserved)
{
	// This may be useful
	fprintf(stderr, "SquirrelJME Hook Loaded\n");

	// We could say we support Java 8, however Java ME is at heart a Java 7
	// system so we cannot use the latest features here
	return JNI_VERSION_1_6;
}

JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_arrayLength__J
	(JNIEnv* env, jclass classy, jlong object)
{
	return Java_cc_squirreljme_jvm_Assembly_arrayLength__Ljava_lang_Object_2(env,
		classy, CAST_LONG_TO_OBJECT(object));
}

JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_arrayLength__Ljava_lang_Object_2
	(JNIEnv* env, jclass classy, jobject object)
{
	// Return invalid length if not an array type
	if (object == NULL)
		return -1;

	if (javaIsInstance(env, object, "[Z") ||
		javaIsInstance(env, object, "[B") ||
		javaIsInstance(env, object, "[S") ||
		javaIsInstance(env, object, "[C") ||
		javaIsInstance(env, object, "[I") ||
		javaIsInstance(env, object, "[J") ||
		javaIsInstance(env, object, "[F") ||
		javaIsInstance(env, object, "[D") ||
		javaIsInstance(env, object, "[Ljava/lang/Object;"))
		return env->GetArrayLength((jarray)object);

	return -1;
}

JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_arrayLengthSet__JI
	(JNIEnv* env, jclass classy, jlong object, jint length)
{
	javaTossIllegalArgumentException(env);
}

JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_arrayLengthSet__Ljava_lang_Object_2I
	(JNIEnv* env, jclass classy, jobject object, jint length)
{
	javaTossIllegalArgumentException(env);
}

JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_atomicCompareGetAndSet
	(JNIEnv* env, jclass classy, jint, jint, jlong)
{
	javaTossIllegalArgumentException(env);
	return 0;
}

JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_atomicDecrementAndGet
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
	return 0;
}

JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_atomicIncrement
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
}

JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_breakpoint
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
}

JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_classInfoOfBoolean
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}

JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_classInfoOfBooleanPointer
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return 0;
}

JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_classInfoOfByte
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}

JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_classInfoOfBytePointer
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return 0;
}

JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_classInfoOfCharacter
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}

JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_classInfoOfCharacterPointer
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return 0;
}

JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_classInfoOfDouble
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}

JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_classInfoOfDoublePointer
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_classInfoOfFloat
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_classInfoOfFloatPointer
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_classInfoOfInteger
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_classInfoOfIntegerPointer
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_classInfoOfLong
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_classInfoOfLongPointer
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_classInfoOfShort
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_classInfoOfShortPointer
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jdouble JNICALL Java_cc_squirreljme_jvm_Assembly_doublePack
	(JNIEnv* env, jclass classy, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_doubleToRawLongBits
	(JNIEnv* env, jclass classy, jdouble)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_doubleUnpackHigh
	(JNIEnv* env, jclass classy, jdouble)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_doubleUnpackLow
	(JNIEnv* env, jclass classy, jdouble)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_exceptionHandle
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_floatToRawIntBits
	(JNIEnv* env, jclass classy, jfloat)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jfloat JNICALL Java_cc_squirreljme_jvm_Assembly_intBitsToFloat
	(JNIEnv* env, jclass classy, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}

JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_invoke__JJ
	(JNIEnv* env, jclass classy, jlong, jlong)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_invoke__JJI
	(JNIEnv* env, jclass classy, jlong, jlong, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_invoke__JJII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_invoke__JJIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_invoke__JJIIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_invoke__JJIIIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_invoke__JJIIIIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_invoke__JJIIIIIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}

JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_invoke__JJIIIIIIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_invokeV__JJ
	(JNIEnv* env, jclass classy, jlong, jlong)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_invokeV__JJI
	(JNIEnv* env, jclass classy, jlong, jlong, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_invokeV__JJII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_invokeV__JJIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_invokeV__JJIIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_invokeV__JJIIIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_invokeV__JJIIIIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_invokeV__JJIIIIIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_invokeV__JJIIIIIIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_invokeVL__JJ
	(JNIEnv* env, jclass classy, jlong, jlong)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_invokeVL__JJI
	(JNIEnv* env, jclass classy, jlong, jlong, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_invokeVL__JJII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_invokeVL__JJIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_invokeVL__JJIIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_invokeVL__JJIIIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_invokeVL__JJIIIIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_invokeVL__JJIIIIIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_invokeVL__JJIIIIIIII
	(JNIEnv* env, jclass classy, jlong, jlong, jint, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jdouble JNICALL Java_cc_squirreljme_jvm_Assembly_longBitsToDouble
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_longPack
	(JNIEnv* env, jclass classy, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_longUnpackHigh
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_longUnpackLow
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_memReadByte
	(JNIEnv* env, jclass classy, jlong, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_memReadInt
	(JNIEnv* env, jclass classy, jlong, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_memReadJavaInt
	(JNIEnv* env, jclass classy, jlong, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_memReadJavaLong
	(JNIEnv* env, jclass classy, jlong, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_memReadJavaShort
	(JNIEnv* env, jclass classy, jlong, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_memReadPointer
	(JNIEnv* env, jclass classy, jlong, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_memReadShort
	(JNIEnv* env, jclass classy, jlong, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_memWriteByte
	(JNIEnv* env, jclass classy, jlong, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_memWriteInt
	(JNIEnv* env, jclass classy, jlong, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_memWriteJavaInt
	(JNIEnv* env, jclass classy, jlong, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_memWriteJavaLong
	(JNIEnv* env, jclass classy, jlong, jint, jlong)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_memWriteJavaShort
	(JNIEnv* env, jclass classy, jlong, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_memWritePointer
	(JNIEnv* env, jclass classy, jlong, jint, jlong)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_memWriteShort
	(JNIEnv* env, jclass classy, jlong, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_monitorCountDecrementAndGetAtomic__J
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_monitorCountDecrementAndGetAtomic__Ljava_lang_Object_2
	(JNIEnv* env, jclass classy, jobject)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_monitorCountIncrementAndGetAtomic__J
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_monitorCountIncrementAndGetAtomic__Ljava_lang_Object_2
	(JNIEnv* env, jclass classy, jobject)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_monitorCountGetAtomic__J
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_monitorCountGetAtomic__Ljava_lang_Object_2
	(JNIEnv* env, jclass classy, jobject)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_monitorCountSetAtomic__JI
	(JNIEnv* env, jclass classy, jlong, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_monitorCountSetAtomic__Ljava_lang_Object_2I
	(JNIEnv* env, jclass classy, jobject, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_monitorOwnerCompareGetAndSetAtomic__JJJ
	(JNIEnv* env, jclass classy, jlong, jlong, jlong)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_monitorOwnerCompareGetAndSetAtomic__Ljava_lang_Object_2JJ
	(JNIEnv* env, jclass classy, jobject, jlong, jlong)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_monitorOwnerCompareGetAndSetAtomic__JLjava_lang_Thread_2Ljava_lang_Thread_2
	(JNIEnv* env, jclass classy, jlong, jobject, jobject)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_monitorOwnerCompareGetAndSetAtomic__Ljava_lang_Object_2Ljava_lang_Thread_2Ljava_lang_Thread_2
	(JNIEnv* env, jclass classy, jobject, jobject, jobject)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_monitorOwnerCompareGetAndSetAtomicPointer__JJJ
	(JNIEnv* env, jclass classy, jlong, jlong, jlong)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_monitorOwnerCompareGetAndSetAtomicPointer__Ljava_lang_Object_2JJ
	(JNIEnv* env, jclass classy, jobject, jlong, jlong)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_monitorOwnerCompareGetAndSetAtomicPointer__JLjava_lang_Thread_2Ljava_lang_Thread_2
	(JNIEnv* env, jclass classy, jlong, jobject, jobject)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_monitorOwnerCompareGetAndSetAtomicPointer__Ljava_lang_Object_2Ljava_lang_Thread_2Ljava_lang_Thread_2
	(JNIEnv* env, jclass classy, jobject, jobject, jobject)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_monitorOwnerGetAtomic__J
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_monitorOwnerGetAtomic__Ljava_lang_Object_2
	(JNIEnv* env, jclass classy, jobject)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_monitorOwnerGetPointerAtomic__J
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_monitorOwnerGetPointerAtomic__Ljava_lang_Object_2
	(JNIEnv* env, jclass classy, jobject)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_monitorOwnerSetAtomic__JJ
	(JNIEnv* env, jclass classy, jlong, jlong)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_monitorOwnerSetAtomic__JLjava_lang_Thread_2
	(JNIEnv* env, jclass classy, jlong, jobject)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_monitorOwnerSetAtomic__Ljava_lang_Object_2J
	(JNIEnv* env, jclass classy, jobject, jlong)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_monitorOwnerSetAtomic__Ljava_lang_Object_2Ljava_lang_Thread_2
	(JNIEnv* env, jclass classy, jobject, jobject)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_objectGetClassInfo__J
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_objectGetClassInfo__Ljava_lang_Object_2
	(JNIEnv* env, jclass classy, jobject)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_objectGetClassInfoPointer__J
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_objectGetClassInfoPointer__Ljava_lang_Object_2
	(JNIEnv* env, jclass classy, jobject)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_objectSetClassInfo__JJ
	(JNIEnv* env, jclass classy, jlong, jlong)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_objectSetClassInfo__Ljava_lang_Object_2J
	(JNIEnv* env, jclass classy, jobject, jlong)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_objectSetClassInfo__JLcc_squirreljme_jvm_ClassInfo_2
	(JNIEnv* env, jclass classy, jlong, jobject)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_objectSetClassInfo__Ljava_lang_Object_2Lcc_squirreljme_jvm_ClassInfo_2
	(JNIEnv* env, jclass classy, jobject, jobject)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_objectToPointer
	(JNIEnv* env, jclass classy, jobject)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_objectToPointerRefQueue
	(JNIEnv* env, jclass classy, jobject)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_pointerToObject
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_pointerToClassInfo
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_poolLoad__JI
	(JNIEnv* env, jclass classy, jlong, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_poolLoad__Ljava_lang_Object_2I
	(JNIEnv* env, jclass classy, jobject, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_poolStore__JIJ
	(JNIEnv* env, jclass classy, jlong, jint, jlong)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_poolStore__Ljava_lang_Object_2IJ
	(JNIEnv* env, jclass classy, jobject, jint, jlong)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_refCount__J
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_refCount__Ljava_lang_Object_2
	(JNIEnv* env, jclass classy, jobject)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_refGetCount__J
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_refGetCount__Ljava_lang_Object_2
	(JNIEnv* env, jclass classy, jobject)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_refSetCount__JI
	(JNIEnv* env, jclass classy, jlong, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_refSetCount__Ljava_lang_Object_2I
	(JNIEnv* env, jclass classy, jobject, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_refUncount__J
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_refUncount__Ljava_lang_Object_2
	(JNIEnv* env, jclass classy, jobject)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_returnFrame__
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_returnFrame__I
	(JNIEnv* env, jclass classy, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_returnFrame__II
	(JNIEnv* env, jclass classy, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_returnFrameLong
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sizeOfBaseArray
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sizeOfBaseObject
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sizeOfPointer
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_specialGetExceptionRegister
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jthrowable JNICALL Java_cc_squirreljme_jvm_Assembly_specialGetExceptionRegisterThrowable
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_specialGetExceptionRegisterPointer
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_specialGetPoolRegister
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_specialGetPoolRegisterPointer
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_specialGetReturnRegister
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_specialGetReturnRegisterLong
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_specialGetStaticFieldRegister
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_specialGetThreadRegister
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return NULL;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_specialGetThreadRegisterPointer
	(JNIEnv* env, jclass classy)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_specialSetExceptionRegister__J
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_specialSetExceptionRegister__Ljava_lang_Object_2
	(JNIEnv* env, jclass classy, jobject)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_specialSetPoolRegister__J
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_specialSetPoolRegister__Ljava_lang_Object_2
	(JNIEnv* env, jclass classy, jobject)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_specialSetStaticFieldRegister
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_specialSetThreadRegister__J
	(JNIEnv* env, jclass classy, jlong)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_specialSetThreadRegister__Ljava_lang_Thread_2
	(JNIEnv* env, jclass classy, jobject)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCall__S
	(JNIEnv* env, jclass classy, jshort)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCall__SI
	(JNIEnv* env, jclass classy, jshort, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCall__SII
	(JNIEnv* env, jclass classy, jshort, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCall__SIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCall__SIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCall__SIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCall__SIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCall__SIIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCall__SIIIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallP__S
	(JNIEnv* env, jclass classy, jshort)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallP__SI
	(JNIEnv* env, jclass classy, jshort, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallP__SII
	(JNIEnv* env, jclass classy, jshort, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallP__SIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallP__SIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallP__SIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallP__SIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallP__SIIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallP__SIIIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPV__S
	(JNIEnv* env, jclass classy, jshort)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPV__SI
	(JNIEnv* env, jclass classy, jshort, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPV__SII
	(JNIEnv* env, jclass classy, jshort, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPV__SIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPV__SIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPV__SIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPV__SIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPV__SIIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPV__SIIIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPVL__S
	(JNIEnv* env, jclass classy, jshort)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPVL__SI
	(JNIEnv* env, jclass classy, jshort, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPVL__SII
	(JNIEnv* env, jclass classy, jshort, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPVL__SIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPVL__SIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPVL__SIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPVL__SIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPVL__SIIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPVL__SIIIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallV__S
	(JNIEnv* env, jclass classy, jshort)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallV__SI
	(JNIEnv* env, jclass classy, jshort, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallV__SII
	(JNIEnv* env, jclass classy, jshort, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallV__SIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallV__SIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallV__SIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallV__SIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallV__SIIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallV__SIIIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallVL__S
	(JNIEnv* env, jclass classy, jshort)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallVL__SI
	(JNIEnv* env, jclass classy, jshort, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallVL__SII
	(JNIEnv* env, jclass classy, jshort, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallVL__SIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallVL__SIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallVL__SIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallVL__SIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallVL__SIIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallVL__SIIIIIIII
	(JNIEnv* env, jclass classy, jshort, jint, jint, jint, jint, jint, jint, jint, jint)
{
	javaTossIllegalArgumentException(env);
	return 0;
}

/*****************************************************************************/
/*****************************************************************************/
/*****************************************************************************/

}

