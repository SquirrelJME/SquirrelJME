/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

#include <stdio.h>
#include <stdlib.h>

#include "jni.h"
#include "cc_squirreljme_emulator_NativeBinding.h"
#include "cc_squirreljme_jvm_Assembly.h"

// Method handler for special functions
JNIEXPORT void JNICALL doNothing(JNIEnv* env, jclass classy);
JNIEXPORT jint JNICALL doNothingZeroI(JNIEnv* env, jclass classy);
JNIEXPORT void JNICALL notImplemented(JNIEnv* env, jclass classy);
JNIEXPORT void JNICALL restrictedFunction(JNIEnv* env, jclass classy);

// Glue for putting names together
#define GLUE_NAME(name) Java_cc_squirreljme_jvm_Assembly_sysCall##name

// Assembly method mappings
static const JNINativeMethod assemblyMethods[] =
{
	{"arrayLength", "(J)I", Java_cc_squirreljme_jvm_Assembly_arrayLength__J},
	{"arrayLength", "(Ljava/lang/Object;)I", Java_cc_squirreljme_jvm_Assembly_arrayLength__Ljava_lang_Object_2},
	{"arrayLengthSet", "(JI)V", restrictedFunction},
	{"arrayLengthSet", "(Ljava/lang/Object;I)V", restrictedFunction},
	{"atomicCompareGetAndSet", "(IIJ)I", notImplemented},
	{"atomicDecrementAndGet", "(J)I", notImplemented},
	{"atomicIncrement", "(J)V", notImplemented},
	{"breakpoint", "()V", doNothing},
	{"classInfoOfBoolean", "()Lcc/squirreljme/jvm/ClassInfo;", restrictedFunction},
	{"classInfoOfBooleanPointer", "()J", restrictedFunction},
	{"classInfoOfByte", "()Lcc/squirreljme/jvm/ClassInfo;", restrictedFunction},
	{"classInfoOfBytePointer", "()J", restrictedFunction},
	{"classInfoOfCharacter", "()Lcc/squirreljme/jvm/ClassInfo;", restrictedFunction},
	{"classInfoOfCharacterPointer", "()J", restrictedFunction},
	{"classInfoOfDouble", "()Lcc/squirreljme/jvm/ClassInfo;", restrictedFunction},
	{"classInfoOfDoublePointer", "()J", restrictedFunction},
	{"classInfoOfFloat", "()Lcc/squirreljme/jvm/ClassInfo;", restrictedFunction},
	{"classInfoOfFloatPointer", "()J", restrictedFunction},
	{"classInfoOfInteger", "()Lcc/squirreljme/jvm/ClassInfo;", restrictedFunction},
	{"classInfoOfIntegerPointer", "()J", restrictedFunction},
	{"classInfoOfLong", "()Lcc/squirreljme/jvm/ClassInfo;", restrictedFunction},
	{"classInfoOfLongPointer", "()J", restrictedFunction},
	{"classInfoOfShort", "()Lcc/squirreljme/jvm/ClassInfo;", restrictedFunction},
	{"classInfoOfShortPointer", "()J", restrictedFunction},
	{"doublePack", "(II)D", notImplemented},
	{"doubleToRawLongBits", "(D)J", notImplemented},
	{"doubleUnpackHigh", "(D)I", notImplemented},
	{"doubleUnpackLow", "(D)I", notImplemented},
	{"exceptionHandle", "()V", doNothing},
	{"floatToRawIntBits", "(F)I", notImplemented},
	{"intBitsToFloat", "(I)F", notImplemented},
	{"invoke", "(JJ)V", restrictedFunction},
	{"invoke", "(JJI)V", restrictedFunction},
	{"invoke", "(JJII)V", restrictedFunction},
	{"invoke", "(JJIII)V", restrictedFunction},
	{"invoke", "(JJIIII)V", restrictedFunction},
	{"invoke", "(JJIIIII)V", restrictedFunction},
	{"invoke", "(JJIIIIII)V", restrictedFunction},
	{"invoke", "(JJIIIIIII)V", restrictedFunction},
	{"invoke", "(JJIIIIIIII)V", restrictedFunction},
	{"invokeV", "(JJ)I", restrictedFunction},
	{"invokeV", "(JJI)I", restrictedFunction},
	{"invokeV", "(JJII)I", restrictedFunction},
	{"invokeV", "(JJIII)I", restrictedFunction},
	{"invokeV", "(JJIIII)I", restrictedFunction},
	{"invokeV", "(JJIIIII)I", restrictedFunction},
	{"invokeV", "(JJIIIIII)I", restrictedFunction},
	{"invokeV", "(JJIIIIIII)I", restrictedFunction},
	{"invokeV", "(JJIIIIIIII)I", restrictedFunction},
	{"invokeVL", "(JJ)J", restrictedFunction},
	{"invokeVL", "(JJI)J", restrictedFunction},
	{"invokeVL", "(JJII)J", restrictedFunction},
	{"invokeVL", "(JJIII)J", restrictedFunction},
	{"invokeVL", "(JJIIII)J", restrictedFunction},
	{"invokeVL", "(JJIIIII)J", restrictedFunction},
	{"invokeVL", "(JJIIIIII)J", restrictedFunction},
	{"invokeVL", "(JJIIIIIII)J", restrictedFunction},
	{"invokeVL", "(JJIIIIIIII)J", restrictedFunction},
	{"longBitsToDouble", "(J)D", notImplemented},
	{"longPack", "(II)J", notImplemented},
	{"longUnpackHigh", "(J)I", notImplemented},
	{"longUnpackLow", "(J)I", notImplemented},
	{"memReadByte", "(JI)I", notImplemented},
	{"memReadInt", "(JI)I", notImplemented},
	{"memReadJavaInt", "(JI)I", notImplemented},
	{"memReadJavaLong", "(JI)J", notImplemented},
	{"memReadJavaShort", "(JI)I", notImplemented},
	{"memReadPointer", "(JI)J", notImplemented},
	{"memReadShort", "(JI)I", notImplemented},
	{"memWriteByte", "(JII)V", notImplemented},
	{"memWriteInt", "(JII)V", notImplemented},
	{"memWriteJavaInt", "(JII)V", notImplemented},
	{"memWriteJavaLong", "(JIJ)V", notImplemented},
	{"memWriteJavaShort", "(JII)V", notImplemented},
	{"memWritePointer", "(JIJ)V", notImplemented},
	{"memWriteShort", "(JII)V", notImplemented},
	{"monitorCountDecrementAndGetAtomic", "(J)I", restrictedFunction},
	{"monitorCountDecrementAndGetAtomic", "(Ljava/lang/Object;)I", restrictedFunction},
	{"monitorCountIncrementAndGetAtomic", "(J)I", restrictedFunction},
	{"monitorCountIncrementAndGetAtomic", "(Ljava/lang/Object;)I", restrictedFunction},
	{"monitorCountGetAtomic", "(J)I", restrictedFunction},
	{"monitorCountGetAtomic", "(Ljava/lang/Object;)I", restrictedFunction},
	{"monitorCountSetAtomic", "(JI)V", restrictedFunction},
	{"monitorCountSetAtomic", "(Ljava/lang/Object;I)V", restrictedFunction},
	{"monitorOwnerCompareGetAndSetAtomic", "(JJJ)Ljava/lang/Thread;", restrictedFunction},
	{"monitorOwnerCompareGetAndSetAtomic", "(Ljava/lang/Object;JJ)Ljava/lang/Thread;", restrictedFunction},
	{"monitorOwnerCompareGetAndSetAtomic", "(JLjava/lang/Thread;Ljava/lang/Thread;)Ljava/lang/Thread;", restrictedFunction},
	{"monitorOwnerCompareGetAndSetAtomic", "(Ljava/lang/Object;Ljava/lang/Thread;Ljava/lang/Thread;)Ljava/lang/Thread;", restrictedFunction},
	{"monitorOwnerCompareGetAndSetAtomicPointer", "(JJJ)J", restrictedFunction},
	{"monitorOwnerCompareGetAndSetAtomicPointer", "(Ljava/lang/Object;JJ)J", restrictedFunction},
	{"monitorOwnerCompareGetAndSetAtomicPointer", "(JLjava/lang/Thread;Ljava/lang/Thread;)J", restrictedFunction},
	{"monitorOwnerCompareGetAndSetAtomicPointer", "(Ljava/lang/Object;Ljava/lang/Thread;Ljava/lang/Thread;)J", restrictedFunction},
	{"monitorOwnerGetAtomic", "(J)Ljava/lang/Thread;", restrictedFunction},
	{"monitorOwnerGetAtomic", "(Ljava/lang/Object;)Ljava/lang/Thread;", restrictedFunction},
	{"monitorOwnerGetPointerAtomic", "(J)J", restrictedFunction},
	{"monitorOwnerGetPointerAtomic", "(Ljava/lang/Object;)J", restrictedFunction},
	{"monitorOwnerSetAtomic", "(JJ)V", restrictedFunction},
	{"monitorOwnerSetAtomic", "(JLjava/lang/Thread;)V", restrictedFunction},
	{"monitorOwnerSetAtomic", "(Ljava/lang/Object;J)V", restrictedFunction},
	{"monitorOwnerSetAtomic", "(Ljava/lang/Object;Ljava/lang/Thread;)V", restrictedFunction},
	{"objectGetClassInfo", "(J)Lcc/squirreljme/jvm/ClassInfo;", restrictedFunction},
	{"objectGetClassInfo", "(Ljava/lang/Object;)Lcc/squirreljme/jvm/ClassInfo;", restrictedFunction},
	{"objectGetClassInfoPointer", "(J)J", restrictedFunction},
	{"objectGetClassInfoPointer", "(Ljava/lang/Object;)J", restrictedFunction},
	{"objectSetClassInfo", "(JJ)V", restrictedFunction},
	{"objectSetClassInfo", "(Ljava/lang/Object;J)V", restrictedFunction},
	{"objectSetClassInfo", "(JLcc/squirreljme/jvm/ClassInfo;)V", restrictedFunction},
	{"objectSetClassInfo", "(Ljava/lang/Object;Lcc/squirreljme/jvm/ClassInfo;)V", restrictedFunction},
	{"objectToPointer", "(Ljava/lang/Object;)J", Java_cc_squirreljme_jvm_Assembly_objectToPointer},
	{"objectToPointerRefQueue", "(Ljava/lang/Object;)J", Java_cc_squirreljme_jvm_Assembly_objectToPointer},
	{"pointerToObject", "(J)Ljava/lang/Object;", Java_cc_squirreljme_jvm_Assembly_pointerToObject},
	{"pointerToClassInfo", "(J)Lcc/squirreljme/jvm/ClassInfo;", Java_cc_squirreljme_jvm_Assembly_pointerToObject},
	{"poolLoad", "(JI)J", restrictedFunction},
	{"poolLoad", "(Ljava/lang/Object;I)J", restrictedFunction},
	{"poolStore", "(JIJ)V", restrictedFunction},
	{"poolStore", "(Ljava/lang/Object;IJ)V", restrictedFunction},
	{"refCount", "(J)V", doNothing},
	{"refCount", "(Ljava/lang/Object;)V", doNothing},
	{"refGetCount", "(J)I", restrictedFunction},
	{"refGetCount", "(Ljava/lang/Object;)I", restrictedFunction},
	{"refSetCount", "(JI)V", restrictedFunction},
	{"refSetCount", "(Ljava/lang/Object;I)V", restrictedFunction},
	{"refUncount", "(J)V", doNothing},
	{"refUncount", "(Ljava/lang/Object;)V", doNothing},
	{"returnFrame", "()V", notImplemented},
	{"returnFrame", "(I)V", notImplemented},
	{"returnFrame", "(II)V", notImplemented},
	{"returnFrameLong", "(J)V", notImplemented},
	{"sizeOfBaseArray", "()I", notImplemented},
	{"sizeOfBaseObject", "()I", notImplemented},
	{"sizeOfPointer", "()I", notImplemented},
	{"specialGetExceptionRegister", "()Ljava/lang/Object;", restrictedFunction},
	{"specialGetExceptionRegisterThrowable", "()Ljava/lang/Throwable;", restrictedFunction},
	{"specialGetExceptionRegisterPointer", "()J", restrictedFunction},
	{"specialGetPoolRegister", "()Ljava/lang/Object;", restrictedFunction},
	{"specialGetPoolRegisterPointer", "()J", restrictedFunction},
	{"specialGetReturnRegister", "()I", restrictedFunction},
	{"specialGetReturnRegisterLong", "()J", restrictedFunction},
	{"specialGetStaticFieldRegister", "()J", restrictedFunction},
	{"specialGetThreadRegister", "()Ljava/lang/Thread;", restrictedFunction},
	{"specialGetThreadRegisterPointer", "()J", restrictedFunction},
	{"specialSetExceptionRegister", "(J)V", restrictedFunction},
	{"specialSetExceptionRegister", "(Ljava/lang/Object;)V", restrictedFunction},
	{"specialSetPoolRegister", "(J)V", restrictedFunction},
	{"specialSetPoolRegister", "(Ljava/lang/Object;)V", restrictedFunction},
	{"specialSetStaticFieldRegister", "(J)V", restrictedFunction},
	{"specialSetThreadRegister", "(J)V", restrictedFunction},
	{"specialSetThreadRegister", "(Ljava/lang/Thread;)V", restrictedFunction},

	// Un-Pure System Calls
	{"sysCall", "(S)V", GLUE_NAME(__S)},
	{"sysCall", "(SI)V", GLUE_NAME(__SI)},
	{"sysCall", "(SII)V", GLUE_NAME(__SII)},
	{"sysCall", "(SIII)V", GLUE_NAME(__SIII)},
	{"sysCall", "(SIIII)V", GLUE_NAME(__SIIII)},
	{"sysCall", "(SIIIII)V", GLUE_NAME(__SIIIII)},
	{"sysCall", "(SIIIIII)V", GLUE_NAME(__SIIIIII)},
	{"sysCall", "(SIIIIIII)V", GLUE_NAME(__SIIIIIII)},
	{"sysCall", "(SIIIIIIII)V", GLUE_NAME(__SIIIIIIII)},
	{"sysCallV", "(S)I", GLUE_NAME(V__S)},
	{"sysCallV", "(SI)I", GLUE_NAME(V__SI)},
	{"sysCallV", "(SII)I", GLUE_NAME(V__SII)},
	{"sysCallV", "(SIII)I", GLUE_NAME(V__SIII)},
	{"sysCallV", "(SIIII)I", GLUE_NAME(V__SIIII)},
	{"sysCallV", "(SIIIII)I", GLUE_NAME(V__SIIIII)},
	{"sysCallV", "(SIIIIII)I", GLUE_NAME(V__SIIIIII)},
	{"sysCallV", "(SIIIIIII)I", GLUE_NAME(V__SIIIIIII)},
	{"sysCallV", "(SIIIIIIII)I", GLUE_NAME(V__SIIIIIIII)},
	{"sysCallVL", "(S)J", GLUE_NAME(VL__S)},
	{"sysCallVL", "(SI)J", GLUE_NAME(VL__SI)},
	{"sysCallVL", "(SII)J", GLUE_NAME(VL__SII)},
	{"sysCallVL", "(SIII)J", GLUE_NAME(VL__SIII)},
	{"sysCallVL", "(SIIII)J", GLUE_NAME(VL__SIIII)},
	{"sysCallVL", "(SIIIII)J", GLUE_NAME(VL__SIIIII)},
	{"sysCallVL", "(SIIIIII)J", GLUE_NAME(VL__SIIIIII)},
	{"sysCallVL", "(SIIIIIII)J", GLUE_NAME(VL__SIIIIIII)},
	{"sysCallVL", "(SIIIIIIII)J", GLUE_NAME(VL__SIIIIIIII)},

	// Pure System Calls	{"sysCallP", "(S)V", GLUE_NAME(P__S)},
	{"sysCallP", "(SI)V", GLUE_NAME(P__SI)},
	{"sysCallP", "(SII)V", GLUE_NAME(P__SII)},
	{"sysCallP", "(SIII)V", GLUE_NAME(P__SIII)},
	{"sysCallP", "(SIIII)V", GLUE_NAME(P__SIIII)},
	{"sysCallP", "(SIIIII)V", GLUE_NAME(P__SIIIII)},
	{"sysCallP", "(SIIIIII)V", GLUE_NAME(P__SIIIIII)},
	{"sysCallP", "(SIIIIIII)V", GLUE_NAME(P__SIIIIIII)},
	{"sysCallP", "(SIIIIIIII)V", GLUE_NAME(P__SIIIIIIII)},
	{"sysCallPV", "(S)I", GLUE_NAME(PV__S)},
	{"sysCallPV", "(SI)I", GLUE_NAME(PV__SI)},
	{"sysCallPV", "(SII)I", GLUE_NAME(PV__SII)},
	{"sysCallPV", "(SIII)I", GLUE_NAME(PV__SIII)},
	{"sysCallPV", "(SIIII)I", GLUE_NAME(PV__SIIII)},
	{"sysCallPV", "(SIIIII)I", GLUE_NAME(PV__SIIIII)},
	{"sysCallPV", "(SIIIIII)I", GLUE_NAME(PV__SIIIIII)},
	{"sysCallPV", "(SIIIIIII)I", GLUE_NAME(PV__SIIIIIII)},
	{"sysCallPV", "(SIIIIIIII)I", GLUE_NAME(PV__SIIIIIIII)},
	{"sysCallPVL", "(S)J", GLUE_NAME(PVL__S)},
	{"sysCallPVL", "(SI)J", GLUE_NAME(PVL__SI)},
	{"sysCallPVL", "(SII)J", GLUE_NAME(PVL__SII)},
	{"sysCallPVL", "(SIII)J", GLUE_NAME(PVL__SIII)},
	{"sysCallPVL", "(SIIII)J", GLUE_NAME(PVL__SIIII)},
	{"sysCallPVL", "(SIIIII)J", GLUE_NAME(PVL__SIIIII)},
	{"sysCallPVL", "(SIIIIII)J", GLUE_NAME(PVL__SIIIIII)},
	{"sysCallPVL", "(SIIIIIII)J", GLUE_NAME(PVL__SIIIIIII)},
	{"sysCallPVL", "(SIIIIIIII)J", GLUE_NAME(PVL__SIIIIIIII)}
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
{
	JNIEnv* env;

	// Used to indicate that something might be happened
	fprintf(stderr, "SquirrelJME Native Bindings Initializing...\n");

	// Support Java 7!
	return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL doNothing(JNIEnv* env, jclass classy)
{
}

JNIEXPORT jint JNICALL doNothingZeroI(JNIEnv* env, jclass classy)
{
	return 0;
}

JNIEXPORT void JNICALL notImplemented(JNIEnv* env, jclass classy)
{
	fprintf(stderr, "Not implemented: %s.\n", __func__);
	env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"),
		__func__);
}

JNIEXPORT void JNICALL restrictedFunction(JNIEnv* env, jclass classy)
{
	fprintf(stderr, "Restricted function: %s.\n", __func__);
	env->ThrowNew(env->FindClass("java/lang/Error"),
		__func__);
}

JNIEXPORT jint JNICALL Java_cc_squirreljme_emulator_NativeBinding__1_1bindMethods
	(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(env->FindClass("cc/squirreljme/jvm/Assembly"),
		assemblyMethods, sizeof(assemblyMethods) / sizeof(JNINativeMethod));
}
