/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#ifndef __SQUIRRELJME_H__
#define __SQUIRRELJME_H__

#include <jni.h>

#include "lib/scritchui/scritchui.h"
#include "sjme/debug.h"
#include "sjme/charSeq.h"

/** Initializing methods. */
jint JNICALL mleDebugInit(JNIEnv* env, jclass classy);
jint JNICALL mleDylibBaseObjectInit(JNIEnv* env, jclass classy);
jint JNICALL mleJarInit(JNIEnv* env, jclass classy);
jint JNICALL mleMathInit(JNIEnv* env, jclass classy);
jint JNICALL mleMidiInit(JNIEnv* env, jclass classy);
jint JNICALL mleNativeArchiveInit(JNIEnv* env, jclass classy);
jint JNICALL mleNativeScritchDylibInit(JNIEnv* env, jclass classy);
jint JNICALL mleNativeScritchInterfaceInit(JNIEnv* env, jclass classy);
jint JNICALL mleObjectInit(JNIEnv* env, jclass classy);
jint JNICALL mlePencilInit(JNIEnv* env, jclass classy);
jint JNICALL mlePencilFontInit(JNIEnv* env, jclass classy);
jint JNICALL mleReflectionInit(JNIEnv* env, jclass classy);
jint JNICALL mleRuntimeInit(JNIEnv* env, jclass classy);
jint JNICALL mleTaskInit(JNIEnv* env, jclass classy);
jint JNICALL mleTerminalInit(JNIEnv* env, jclass classy);
jint JNICALL mleTypeInit(JNIEnv* env, jclass classy);
jint JNICALL mleThreadInit(JNIEnv* env, jclass classy);

/** Useful macros, structures, and functions for forwarding. */
// Stores forwarded information
typedef struct forwardMethod
{
	jclass xclass;
	jmethodID xmeth;	
} forwardMethod;

// Find forwarded method
forwardMethod JNICALL findForwardMethod(JNIEnv* env,
	const char* const classy, const char* const name, const char* const type);

// Call static methods
void JNICALL forwardCallStaticVoid(JNIEnv* env,
	const char* const classy, const char* const name, const char* const type,
	...);
jint JNICALL forwardCallStaticInteger(JNIEnv* env,
	const char* const classy, const char* const name, const char* const type,
	...);
jlong JNICALL forwardCallStaticLong(JNIEnv* env,
	const char* const classy, const char* const name, const char* const type,
	...);
jobject JNICALL forwardCallStaticObject(JNIEnv* env,
	const char* const classy, const char* const name, const char* const type,
	...);
jboolean JNICALL forwardCallStaticBoolean(JNIEnv* env,
	const char* const classy, const char* const name, const char* const type,
	...);

#define FORWARD_init(funcName, forwardFuncs) \
	jint JNICALL funcName(JNIEnv* env, jclass classy) \
	{ \
		return (*env)->RegisterNatives(env, \
			(*env)->FindClass(env, FORWARD_CLASS), \
			forwardFuncs, sizeof(forwardFuncs) / sizeof(JNINativeMethod)); \
	}
	
#define FORWARD_stringy(x) #x

#define FORWARD_paste(x, y) x ## y

#define FORWARD_from(x) x
	
#define FORWARD_list(className, methodName) \
	{FORWARD_stringy(methodName), \
	FORWARD_from(FORWARD_paste(FORWARD_DESC_, methodName)), \
	(void*)Impl_mle_ ## className ## _ ## methodName}

#define FORWARD_IMPL_none()

#define FORWARD_IMPL_args(...) , __VA_ARGS__

#define FORWARD_IMPL_pass(...) , __VA_ARGS__

#define FORWARD_IMPL_none()

#define FORWARD_FUNC_NAME(className, methodName) \
	Impl_mle_ ## className ## _ ## methodName

#define FORWARD_IMPL_VOID(className, methodName, args, pass) \
	JNIEXPORT void JNICALL Impl_mle_ ## className ## _ ## methodName( \
		JNIEnv* env, jclass classy args) \
	{ \
		forwardCallStaticVoid(env, FORWARD_NATIVE_CLASS, \
			FORWARD_stringy(methodName), \
			FORWARD_from(FORWARD_paste(FORWARD_DESC_, methodName)) \
			pass); \
	}

#define FORWARD_IMPL(className, methodName, rtype, rjava, args, pass) \
	JNIEXPORT rtype JNICALL Impl_mle_ ## className ## _ ## methodName( \
		JNIEnv* env, jclass classy args) \
	{ \
		return FORWARD_paste(forwardCallStatic, rjava)(env, \
			FORWARD_NATIVE_CLASS, \
			FORWARD_stringy(methodName), \
			FORWARD_from(FORWARD_paste(FORWARD_DESC_, methodName)) \
			pass); \
	}

#define DESC_ARRAY(x) "[" x
#define DESC_CLASS(x) "L" x ";"
#define DESC_BOOLEAN "Z"
#define DESC_BYTE "B"
#define DESC_SHORT "S"
#define DESC_CHAR "C"
#define DESC_CHARACTER DESC_CHAR
#define DESC_INT "I"
#define DESC_INTEGER DESC_INT
#define DESC_LONG "J"
#define DESC_FLOAT "F"
#define DESC_DOUBLE "D"
#define DESC_VOID "V"
#define DESC_OBJECT DESC_CLASS("java/lang/Object")
#define DESC_STRING DESC_CLASS("java/lang/String")
#define DESC_BYTE_BUFFER DESC_CLASS("java/nio/ByteBuffer")

#define DESC_JARPACKAGE \
	DESC_CLASS("cc/squirreljme/jvm/mle/brackets/JarPackageBracket")
#define DESC_PENCIL \
	DESC_CLASS("cc/squirreljme/jvm/mle/brackets/PencilBracket")
#define DESC_PENCILFONT \
	DESC_CLASS("cc/squirreljme/jvm/mle/brackets/PencilFontBracket")

#define DESC_DYLIB_COLLECTOR \
	DESC_CLASS("cc/squirreljme/emulator/scritchui/dylib/__Collector__")
#define DESC_DYLIB_BASE \
	DESC_CLASS("cc/squirreljme/emulator/scritchui/dylib/DylibBaseObject")
#define DESC_DYLIB_HAS_OBJECT_POINTER \
	DESC_CLASS("cc/squirreljme/emulator/scritchui/dylib/DylibHasObjectPointer")
#define DESC_DYLIB_PENCIL \
	DESC_CLASS("cc/squirreljme/emulator/scritchui/dylib/DylibPencilObject")
#define DESC_DYLIB_PENCIL_BASIC \
	DESC_CLASS("cc/squirreljme/emulator/scritchui/dylib/DylibPencilBasicObject")
#define DESC_DYLIB_PENCIL_UI \
	DESC_CLASS("cc/squirreljme/emulator/scritchui/dylib/DylibPencilUiObject")
#define DESC_DYLIB_PENCILFONT \
	DESC_CLASS("cc/squirreljme/emulator/scritchui/dylib/DylibPencilFontObject")

#define DESC_SCRITCHUI_ACTIVATE_LISTENER DESC_CLASS( \
	"cc/squirreljme/jvm/mle/scritchui/callbacks/ScritchActivateListener")
#define DESC_SCRITCHUI_CLOSE_LISTENER DESC_CLASS( \
	"cc/squirreljme/jvm/mle/scritchui/callbacks/ScritchCloseListener")
#define DESC_SCRITCHUI_INPUT_LISTENER DESC_CLASS( \
	"cc/squirreljme/jvm/mle/scritchui/callbacks/ScritchInputListener")
#define DESC_SCRITCHUI_PAINT_LISTENER DESC_CLASS( \
	"cc/squirreljme/jvm/mle/scritchui/callbacks/ScritchPaintListener")
#define DESC_SCRITCHUI_MENU_ITEM_ACTIVATE_LISTENER DESC_CLASS( \
	"cc/squirreljme/jvm/mle/scritchui/callbacks/" \
	"ScritchMenuItemActivateListener")
#define DESC_SCRITCHUI_SIZE_SUGGEST_LISTENER DESC_CLASS( \
	"cc/squirreljme/jvm/mle/scritchui/callbacks/ScritchSizeSuggestListener")
#define DESC_SCRITCHUI_VIEW_LISTENER DESC_CLASS( \
	"cc/squirreljme/jvm/mle/scritchui/callbacks/ScritchViewListener")
#define DESC_SCRITCHUI_VISIBLE_LISTENER DESC_CLASS( \
	"cc/squirreljme/jvm/mle/scritchui/callbacks/ScritchVisibleListener")

#define DESC_SCRITCHUI_COMPONENT DESC_CLASS( \
	"cc/squirreljme/jvm/mle/scritchui/brackets/ScritchComponentBracket")
#define DESC_SCRITCHUI_LIST DESC_CLASS( \
	"cc/squirreljme/jvm/mle/scritchui/brackets/ScritchListBracket")
#define DESC_SCRITCHUI_MENUKIND DESC_CLASS( \
	"cc/squirreljme/jvm/mle/scritchui/brackets/ScritchMenuKindBracket")
#define DESC_SCRITCHUI_PENCIL DESC_CLASS( \
	"cc/squirreljme/jvm/mle/scritchui/brackets/ScritchPencilBracket")
#define DESC_SCRITCHUI_VIEW DESC_CLASS( \
	"cc/squirreljme/jvm/mle/scritchui/brackets/ScritchViewBracket")
#define DESC_SCRITCHUI_WINDOW DESC_CLASS( \
	"cc/squirreljme/jvm/mle/scritchui/brackets/ScritchWindowBracket")

/** Debug handlers for JNI code. */
extern sjme_debug_handlerFunctions sjme_jni_debugHandlers;

/**
 * Common check and forward call.
 * 
 * @param failRet The failing return value.
 * @param funcMember The function member to check and to call.
 * @param args Arguments to the function.
 * @since 2024/06/13
 */
#define CHECK_AND_FORWARD(failRet, funcMember, args) \
	do { if (funcMember == NULL) \
	{ \
		sjme_jni_throwMLECallError(env, SJME_ERROR_NOT_IMPLEMENTED); \
		return (failRet); \
	} \
	 \
	/* Forward. */ \
	result = (failRet); \
	if (sjme_error_is(error = funcMember args)) \
	{ \
		sjme_jni_throwMLECallError(env, error); \
		return (failRet); \
	} } while(0)

/**
 * Checks to see if a virtual machine call failed.
 *
 * @param env The Java environment.
 * @return If there is an exception.
 * @since 2023/12/29
 */
sjme_jboolean sjme_jni_checkVMException(JNIEnv* env);

/**
 * Directly map integer array.
 * 
 * @param env The Java environment.
 * @param buf The input array to map.
 * @param off The offset into the array.
 * @param len The length of the array.
 * @return The resultant raw object for the array.
 * @since 2024/04/24
 */
jintArray sjme_jni_mappedArrayInt(JNIEnv* env,
	jint* buf, jint off, jint len);

/**
 * Throws a @c MLECallError .
 *
 * @param env The current Java environment.
 * @param code The error code.
 * @since 2024/04/16
 */
void sjme_jni_throwMLECallError(JNIEnv* env, sjme_errorCode code);

/**
 * Throws the given throwable type.
 *
 * @param env The current Java environment.
 * @param code The error code.
 * @param type The type of exception to throw.
 * @since 2024/04/16
 */
void sjme_jni_throwThrowable(JNIEnv* env, sjme_errorCode code,
	sjme_lpcstr type);

/**
 * Throws a @c VMException .
 *
 * @param env The current Java environment.
 * @param code The error code.
 * @since 2023/12/08
 */
void sjme_jni_throwVMException(JNIEnv* env, sjme_errorCode code);

/**
 * Recovers a pointer from a @c DylibBaseObject .
 * 
 * @param env The current Java environment. 
 * @param className The class this must be.
 * @param instance The @c DylibBaseObject instance. 
 * @return The resultant pointer.
 * @since 2024/06/12
 */
void* sjme_jni_recoverPointer(JNIEnv* env, sjme_lpcstr className,
	jobject instance);
	
/**
 * Recovers a pointer from a @c DylibPencilObject .
 * 
 * @param env The current Java environment. 
 * @param g The @c DylibPencilObject instance. 
 * @return The resultant pointer.
 * @since 2024/06/25
 */
sjme_scritchui_pencil sjme_jni_recoverPencil(JNIEnv* env, jobject g);	

/**
 * Recovers a pointer from a @c DylibPencilFontObject .
 * 
 * @param env The current Java environment. 
 * @param fontInstance The @c DylibPencilFontObject instance. 
 * @return The resultant pointer.
 * @since 2024/06/25
 */
sjme_scritchui_pencilFont sjme_jni_recoverFont(JNIEnv* env,
	jobject fontInstance);

/**
 * Fills in the front end information.
 * 
 * @param env The environment used. 
 * @param into What is being written to.
 * @param ref The object reference, if any.
 * @return Any resultant error, if any.
 * @since 2024/06/27
 */
sjme_errorCode sjme_jni_fillFrontEnd(JNIEnv* env, sjme_frontEnd* into,
	jobject ref);

/**
 * Recovers the Java environment pointer.
 * 
 * @param outEnv The resultant environment. 
 * @param inVm The input virtual machine.
 * @return Any resultant error, if any.
 * @since 2024/06/27
 */
sjme_errorCode sjme_jni_recoverEnv(
	sjme_attrInOutNotNull JNIEnv** outEnv,
	sjme_attrInNotNull JavaVM* inVm);

/**
 * Recovers the Java environment pointer.
 * 
 * @param outEnv The resultant environment. 
 * @param inFrontEnd The input front end.
 * @return Any resultant error, if any.
 * @since 2024/06/27
 */
sjme_errorCode sjme_jni_recoverEnvFrontEnd(
	sjme_attrInOutNotNull JNIEnv** outEnv,
	sjme_attrInNotNull const sjme_frontEnd* inFrontEnd);

/**
 * Initializes a character sequence from a Java String.
 * 
 * @param env The Java environment.
 * @param inOutSeq The input/output character sequence.
 * @param inString The input string to wrap.
 * @return Any resultant error, if any.
 * @since 2024/06/26
 */
sjme_errorCode sjme_jni_jstringCharSeqStatic(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrInNotNull sjme_charSeq* inOutSeq,
	sjme_attrInNotNull jstring inString);

/**
 * Maps input @c sjme_jlong to @c jlong .
 * 
 * @param value The input value. 
 * @return The resultant value.
 * @since 2024/06/30
 */
jlong sjme_jni_jlong(sjme_jlong value);

/**
 * Pushes a weak link bound to an object.
 * 
 * @param env The Java environment.
 * @param javaObject The object to bind.
 * @param nativeWeak The native weak to bind from.
 * @return Any resultant error, if any.
 * @since 2024/07/11
 */
sjme_errorCode sjme_jni_pushWeakLink(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrInNotNull jobject javaObject,
	sjme_attrInNotNull sjme_alloc_weak nativeWeak);

/**
 * Returns the Java type of the given array.
 * 
 * @param env The Java environment. 
 * @param array The array to get the type of.
 * @param outJavaType The resultant type.
 * @return Any resultant error.
 * @since 2024/07/11
 */
sjme_errorCode sjme_jni_arrayType(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrInNotNull jobject array,
	sjme_attrOutNotNull sjme_basicTypeId* outType);

/**
 * Gets the elements of an array.
 * 
 * @param env The environment. 
 * @param array The array object.
 * @param rawBuf The raw output buffer.
 * @param isCopy Is the array a copy?
 * @param typeSize The size of the element type.
 * @return Any resultant error.
 * @since 2024/07/11
 */
sjme_errorCode sjme_jni_arrayGetElements(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrInNotNull jobject array,
	sjme_attrOutNotNull sjme_pointer* rawBuf,
	sjme_attrOutNotNull jboolean* isCopy,
	sjme_attrOutNullable sjme_jint* typeSize);

/**
 * Gets the elements of an array.
 * 
 * @param env The environment. 
 * @param array The array object.
 * @param rawBuf The raw output buffer.
 * @return Any resultant error.
 * @since 2024/07/11
 */
sjme_errorCode sjme_jni_arrayReleaseElements(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrInNotNull jarray array,
	sjme_attrInNotNull sjme_pointer rawBuf);

#endif /* __SQUIRRELJME_H__ */

