/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

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
	{"arrayLength", "(J)I", (void*)Java_cc_squirreljme_jvm_Assembly_arrayLength__J},
	{"arrayLength", "(Ljava/lang/Object;)I", (void*)Java_cc_squirreljme_jvm_Assembly_arrayLength__Ljava_lang_Object_2},
	{"arrayLengthSet", "(JI)V", (void*)restrictedFunction},
	{"arrayLengthSet", "(Ljava/lang/Object;I)V", (void*)restrictedFunction},
	{"atomicCompareGetAndSet", "(IIJ)I", (void*)notImplemented},
	{"atomicDecrementAndGet", "(J)I", (void*)notImplemented},
	{"atomicIncrement", "(J)V", (void*)notImplemented},
	{"atomicTicker", "()I", (void*)notImplemented},
	{"breakpoint", "()V", (void*)doNothing},
	{"classInfoOfBoolean", "()Lcc/squirreljme/jvm/ClassInfo;", (void*)restrictedFunction},
	{"classInfoOfBooleanPointer", "()J", (void*)restrictedFunction},
	{"classInfoOfByte", "()Lcc/squirreljme/jvm/ClassInfo;", (void*)restrictedFunction},
	{"classInfoOfBytePointer", "()J", (void*)restrictedFunction},
	{"classInfoOfCharacter", "()Lcc/squirreljme/jvm/ClassInfo;", (void*)restrictedFunction},
	{"classInfoOfCharacterPointer", "()J", (void*)restrictedFunction},
	{"classInfoOfDouble", "()Lcc/squirreljme/jvm/ClassInfo;", (void*)restrictedFunction},
	{"classInfoOfDoublePointer", "()J", (void*)restrictedFunction},
	{"classInfoOfFloat", "()Lcc/squirreljme/jvm/ClassInfo;", (void*)restrictedFunction},
	{"classInfoOfFloatPointer", "()J", (void*)restrictedFunction},
	{"classInfoOfInteger", "()Lcc/squirreljme/jvm/ClassInfo;", (void*)restrictedFunction},
	{"classInfoOfIntegerPointer", "()J", (void*)restrictedFunction},
	{"classInfoOfLong", "()Lcc/squirreljme/jvm/ClassInfo;", (void*)restrictedFunction},
	{"classInfoOfLongPointer", "()J", (void*)restrictedFunction},
	{"classInfoOfShort", "()Lcc/squirreljme/jvm/ClassInfo;", (void*)restrictedFunction},
	{"classInfoOfShortPointer", "()J", (void*)restrictedFunction},
	{"doublePack", "(II)D", (void*)notImplemented},
	{"doubleToRawLongBits", "(D)J", (void*)notImplemented},
	{"doubleUnpackHigh", "(D)I", (void*)notImplemented},
	{"doubleUnpackLow", "(D)I", (void*)notImplemented},
	{"exceptionHandle", "()V", (void*)doNothing},
	{"floatToRawIntBits", "(F)I", (void*)notImplemented},
	{"gcLock", "(I)Z", (void*)restrictedFunction},
	{"gcUnlock", "(I)V", (void*)restrictedFunction},
	{"intBitsToFloat", "(I)F", (void*)notImplemented},
	{"invoke", "(JJ)V", (void*)restrictedFunction},
	{"invoke", "(JJI)V", (void*)restrictedFunction},
	{"invoke", "(JJII)V", (void*)restrictedFunction},
	{"invoke", "(JJIII)V", (void*)restrictedFunction},
	{"invoke", "(JJIIII)V", (void*)restrictedFunction},
	{"invoke", "(JJIIIII)V", (void*)restrictedFunction},
	{"invoke", "(JJIIIIII)V", (void*)restrictedFunction},
	{"invoke", "(JJIIIIIII)V", (void*)restrictedFunction},
	{"invoke", "(JJIIIIIIII)V", (void*)restrictedFunction},
	{"invokeV", "(JJ)I", (void*)restrictedFunction},
	{"invokeV", "(JJI)I", (void*)restrictedFunction},
	{"invokeV", "(JJII)I", (void*)restrictedFunction},
	{"invokeV", "(JJIII)I", (void*)restrictedFunction},
	{"invokeV", "(JJIIII)I", (void*)restrictedFunction},
	{"invokeV", "(JJIIIII)I", (void*)restrictedFunction},
	{"invokeV", "(JJIIIIII)I", (void*)restrictedFunction},
	{"invokeV", "(JJIIIIIII)I", (void*)restrictedFunction},
	{"invokeV", "(JJIIIIIIII)I", (void*)restrictedFunction},
	{"invokeVL", "(JJ)J", (void*)restrictedFunction},
	{"invokeVL", "(JJI)J", (void*)restrictedFunction},
	{"invokeVL", "(JJII)J", (void*)restrictedFunction},
	{"invokeVL", "(JJIII)J", (void*)restrictedFunction},
	{"invokeVL", "(JJIIII)J", (void*)restrictedFunction},
	{"invokeVL", "(JJIIIII)J", (void*)restrictedFunction},
	{"invokeVL", "(JJIIIIII)J", (void*)restrictedFunction},
	{"invokeVL", "(JJIIIIIII)J", (void*)restrictedFunction},
	{"invokeVL", "(JJIIIIIIII)J", (void*)restrictedFunction},
	{"longBitsToDouble", "(J)D", (void*)notImplemented},
	{"longPack", "(II)J", (void*)Java_cc_squirreljme_jvm_Assembly_longPack},
	{"longUnpackHigh", "(J)I", (void*)Java_cc_squirreljme_jvm_Assembly_longUnpackHigh},
	{"longUnpackLow", "(J)I", (void*)Java_cc_squirreljme_jvm_Assembly_longUnpackLow},
	{"memReadByte", "(JI)I", (void*)notImplemented},
	{"memReadInt", "(JI)I", (void*)notImplemented},
	{"memReadJavaInt", "(JI)I", (void*)notImplemented},
	{"memReadJavaLong", "(JI)J", (void*)notImplemented},
	{"memReadJavaShort", "(JI)I", (void*)notImplemented},
	{"memReadPointer", "(JI)J", (void*)notImplemented},
	{"memReadShort", "(JI)I", (void*)notImplemented},
	{"memWriteByte", "(JII)V", (void*)notImplemented},
	{"memWriteInt", "(JII)V", (void*)notImplemented},
	{"memWriteJavaInt", "(JII)V", (void*)notImplemented},
	{"memWriteJavaLong", "(JIJ)V", (void*)notImplemented},
	{"memWriteJavaShort", "(JII)V", (void*)notImplemented},
	{"memWritePointer", "(JIJ)V", (void*)notImplemented},
	{"memWriteShort", "(JII)V", (void*)notImplemented},
	{"monitorCountDecrementAndGetAtomic", "(J)I", (void*)restrictedFunction},
	{"monitorCountDecrementAndGetAtomic", "(Ljava/lang/Object;)I", (void*)restrictedFunction},
	{"monitorCountIncrementAndGetAtomic", "(J)I", (void*)restrictedFunction},
	{"monitorCountIncrementAndGetAtomic", "(Ljava/lang/Object;)I", (void*)restrictedFunction},
	{"monitorCountGetAtomic", "(J)I", (void*)restrictedFunction},
	{"monitorCountGetAtomic", "(Ljava/lang/Object;)I", (void*)restrictedFunction},
	{"monitorCountSetAtomic", "(JI)V", (void*)restrictedFunction},
	{"monitorCountSetAtomic", "(Ljava/lang/Object;I)V", (void*)restrictedFunction},
	{"monitorOwnerCompareGetAndSetAtomic", "(JJJ)Ljava/lang/Thread;", (void*)restrictedFunction},
	{"monitorOwnerCompareGetAndSetAtomic", "(Ljava/lang/Object;JJ)Ljava/lang/Thread;", (void*)restrictedFunction},
	{"monitorOwnerCompareGetAndSetAtomic", "(JLjava/lang/Thread;Ljava/lang/Thread;)Ljava/lang/Thread;", (void*)restrictedFunction},
	{"monitorOwnerCompareGetAndSetAtomic", "(Ljava/lang/Object;Ljava/lang/Thread;Ljava/lang/Thread;)Ljava/lang/Thread;", (void*)restrictedFunction},
	{"monitorOwnerCompareGetAndSetAtomicPointer", "(JJJ)J", (void*)restrictedFunction},
	{"monitorOwnerCompareGetAndSetAtomicPointer", "(Ljava/lang/Object;JJ)J", (void*)restrictedFunction},
	{"monitorOwnerCompareGetAndSetAtomicPointer", "(JLjava/lang/Thread;Ljava/lang/Thread;)J", (void*)restrictedFunction},
	{"monitorOwnerCompareGetAndSetAtomicPointer", "(Ljava/lang/Object;Ljava/lang/Thread;Ljava/lang/Thread;)J", (void*)restrictedFunction},
	{"monitorOwnerGetAtomic", "(J)Ljava/lang/Thread;", (void*)restrictedFunction},
	{"monitorOwnerGetAtomic", "(Ljava/lang/Object;)Ljava/lang/Thread;", (void*)restrictedFunction},
	{"monitorOwnerGetPointerAtomic", "(J)J", (void*)restrictedFunction},
	{"monitorOwnerGetPointerAtomic", "(Ljava/lang/Object;)J", (void*)restrictedFunction},
	{"monitorOwnerSetAtomic", "(JJ)V", (void*)restrictedFunction},
	{"monitorOwnerSetAtomic", "(JLjava/lang/Thread;)V", (void*)restrictedFunction},
	{"monitorOwnerSetAtomic", "(Ljava/lang/Object;J)V", (void*)restrictedFunction},
	{"monitorOwnerSetAtomic", "(Ljava/lang/Object;Ljava/lang/Thread;)V", (void*)restrictedFunction},
	{"objectGetClassInfo", "(J)Lcc/squirreljme/jvm/ClassInfo;", (void*)restrictedFunction},
	{"objectGetClassInfo", "(Ljava/lang/Object;)Lcc/squirreljme/jvm/ClassInfo;", (void*)restrictedFunction},
	{"objectGetClassInfoPointer", "(J)J", (void*)restrictedFunction},
	{"objectGetClassInfoPointer", "(Ljava/lang/Object;)J", (void*)restrictedFunction},
	{"objectSetClassInfo", "(JJ)V", (void*)restrictedFunction},
	{"objectSetClassInfo", "(Ljava/lang/Object;J)V", (void*)restrictedFunction},
	{"objectSetClassInfo", "(JLcc/squirreljme/jvm/ClassInfo;)V", (void*)restrictedFunction},
	{"objectSetClassInfo", "(Ljava/lang/Object;Lcc/squirreljme/jvm/ClassInfo;)V", (void*)restrictedFunction},
	{"objectToPointer", "(Ljava/lang/Object;)J", (void*)Java_cc_squirreljme_jvm_Assembly_objectToPointer},
	{"objectToPointerRefQueue", "(Ljava/lang/Object;)J", (void*)Java_cc_squirreljme_jvm_Assembly_objectToPointer},
	{"pointerToObject", "(J)Ljava/lang/Object;", (void*)Java_cc_squirreljme_jvm_Assembly_pointerToObject},
	{"pointerToClassInfo", "(J)Lcc/squirreljme/jvm/ClassInfo;", (void*)Java_cc_squirreljme_jvm_Assembly_pointerToObject},
	{"poolLoad", "(JI)J", (void*)restrictedFunction},
	{"poolLoad", "(Ljava/lang/Object;I)J", (void*)restrictedFunction},
	{"poolStore", "(JIJ)V", (void*)restrictedFunction},
	{"poolStore", "(Ljava/lang/Object;IJ)V", (void*)restrictedFunction},
	{"refChainGet", "(J)Lcc/squirreljme/jvm/ReferenceChain;", (void*)restrictedFunction},
	{"refChainGet", "(Ljava/lang/Object;)Lcc/squirreljme/jvm/ReferenceChain;", (void*)restrictedFunction},
	{"refChainSet", "(JJ)V", (void*)restrictedFunction},
	{"refChainSet", "(JLcc/squirreljme/jvm/ReferenceChain;)V", (void*)restrictedFunction},
	{"refChainSet", "(Ljava/lang/Object;J)V", (void*)restrictedFunction},
	{"refChainSet", "(Ljava/lang/Object;Lcc/squirreljme/jvm/ReferenceChain;)V", (void*)restrictedFunction},
	{"refCountGet", "(J)I", (void*)restrictedFunction},
	{"refCountGet", "(Ljava/lang/Object;)I", (void*)restrictedFunction},
	{"refCountSet", "(JI)V", (void*)restrictedFunction},
	{"refCountSet", "(Ljava/lang/Object;I)V", (void*)restrictedFunction},
	{"refCountUp", "(J)V", (void*)doNothing},
	{"refCountUp", "(Ljava/lang/Object;)V", (void*)doNothing},
	{"refUncount", "(J)V", (void*)doNothing},
	{"refUncount", "(Ljava/lang/Object;)V", (void*)doNothing},
	{"returnFrame", "()V", (void*)notImplemented},
	{"returnFrame", "(I)V", (void*)notImplemented},
	{"returnFrame", "(II)V", (void*)notImplemented},
	{"returnFrameLong", "(J)V", (void*)notImplemented},
	{"sizeOfBaseArray", "()I", (void*)notImplemented},
	{"sizeOfBaseObject", "()I", (void*)notImplemented},
	{"sizeOfPointer", "()I", (void*)notImplemented},
	{"specialGetExceptionRegister", "()Ljava/lang/Object;", (void*)restrictedFunction},
	{"specialGetExceptionRegisterThrowable", "()Ljava/lang/Throwable;", (void*)restrictedFunction},
	{"specialGetExceptionRegisterPointer", "()J", (void*)restrictedFunction},
	{"specialGetPoolRegister", "()Ljava/lang/Object;", (void*)restrictedFunction},
	{"specialGetPoolRegisterPointer", "()J", (void*)restrictedFunction},
	{"specialGetReturnRegister", "()I", (void*)restrictedFunction},
	{"specialGetReturnRegisterLong", "()J", (void*)restrictedFunction},
	{"specialGetStaticFieldRegister", "()J", (void*)restrictedFunction},
	{"specialGetThreadRegister", "()Ljava/lang/Thread;", (void*)restrictedFunction},
	{"specialGetThreadRegisterPointer", "()J", (void*)restrictedFunction},
	{"specialSetExceptionRegister", "(J)V", (void*)restrictedFunction},
	{"specialSetExceptionRegister", "(Ljava/lang/Object;)V", (void*)restrictedFunction},
	{"specialSetPoolRegister", "(J)V", (void*)restrictedFunction},
	{"specialSetPoolRegister", "(Ljava/lang/Object;)V", (void*)restrictedFunction},
	{"specialSetStaticFieldRegister", "(J)V", (void*)restrictedFunction},
	{"specialSetThreadRegister", "(J)V", (void*)restrictedFunction},
	{"specialSetThreadRegister", "(Ljava/lang/Thread;)V", (void*)restrictedFunction},
	{"spinLockBurn", "(I)V", (void*)doNothing},

	// Un-Pure System Calls
	{"sysCall", "(S)V", (void*)GLUE_NAME(__S)},
	{"sysCall", "(SI)V", (void*)GLUE_NAME(__SI)},
	{"sysCall", "(SII)V", (void*)GLUE_NAME(__SII)},
	{"sysCall", "(SIII)V", (void*)GLUE_NAME(__SIII)},
	{"sysCall", "(SIIII)V", (void*)GLUE_NAME(__SIIII)},
	{"sysCall", "(SIIIII)V", (void*)GLUE_NAME(__SIIIII)},
	{"sysCall", "(SIIIIII)V", (void*)GLUE_NAME(__SIIIIII)},
	{"sysCall", "(SIIIIIII)V", (void*)GLUE_NAME(__SIIIIIII)},
	{"sysCall", "(SIIIIIIII)V", (void*)GLUE_NAME(__SIIIIIIII)},
	{"sysCallV", "(S)I", (void*)GLUE_NAME(V__S)},
	{"sysCallV", "(SI)I", (void*)GLUE_NAME(V__SI)},
	{"sysCallV", "(SII)I", (void*)GLUE_NAME(V__SII)},
	{"sysCallV", "(SIII)I", (void*)GLUE_NAME(V__SIII)},
	{"sysCallV", "(SIIII)I", (void*)GLUE_NAME(V__SIIII)},
	{"sysCallV", "(SIIIII)I", (void*)GLUE_NAME(V__SIIIII)},
	{"sysCallV", "(SIIIIII)I", (void*)GLUE_NAME(V__SIIIIII)},
	{"sysCallV", "(SIIIIIII)I", (void*)GLUE_NAME(V__SIIIIIII)},
	{"sysCallV", "(SIIIIIIII)I", (void*)GLUE_NAME(V__SIIIIIIII)},
	{"sysCallVL", "(S)J", (void*)GLUE_NAME(VL__S)},
	{"sysCallVL", "(SI)J", (void*)GLUE_NAME(VL__SI)},
	{"sysCallVL", "(SII)J", (void*)GLUE_NAME(VL__SII)},
	{"sysCallVL", "(SIII)J", (void*)GLUE_NAME(VL__SIII)},
	{"sysCallVL", "(SIIII)J", (void*)GLUE_NAME(VL__SIIII)},
	{"sysCallVL", "(SIIIII)J", (void*)GLUE_NAME(VL__SIIIII)},
	{"sysCallVL", "(SIIIIII)J", (void*)GLUE_NAME(VL__SIIIIII)},
	{"sysCallVL", "(SIIIIIII)J", (void*)GLUE_NAME(VL__SIIIIIII)},
	{"sysCallVL", "(SIIIIIIII)J", (void*)GLUE_NAME(VL__SIIIIIIII)},

	// Pure System Calls	{"sysCallP", "(S)V", (void*)GLUE_NAME(P__S)},
	{"sysCallP", "(SI)V", (void*)GLUE_NAME(P__SI)},
	{"sysCallP", "(SII)V", (void*)GLUE_NAME(P__SII)},
	{"sysCallP", "(SIII)V", (void*)GLUE_NAME(P__SIII)},
	{"sysCallP", "(SIIII)V", (void*)GLUE_NAME(P__SIIII)},
	{"sysCallP", "(SIIIII)V", (void*)GLUE_NAME(P__SIIIII)},
	{"sysCallP", "(SIIIIII)V", (void*)GLUE_NAME(P__SIIIIII)},
	{"sysCallP", "(SIIIIIII)V", (void*)GLUE_NAME(P__SIIIIIII)},
	{"sysCallP", "(SIIIIIIII)V", (void*)GLUE_NAME(P__SIIIIIIII)},
	{"sysCallPV", "(S)I", (void*)GLUE_NAME(PV__S)},
	{"sysCallPV", "(SI)I", (void*)GLUE_NAME(PV__SI)},
	{"sysCallPV", "(SII)I", (void*)GLUE_NAME(PV__SII)},
	{"sysCallPV", "(SIII)I", (void*)GLUE_NAME(PV__SIII)},
	{"sysCallPV", "(SIIII)I", (void*)GLUE_NAME(PV__SIIII)},
	{"sysCallPV", "(SIIIII)I", (void*)GLUE_NAME(PV__SIIIII)},
	{"sysCallPV", "(SIIIIII)I", (void*)GLUE_NAME(PV__SIIIIII)},
	{"sysCallPV", "(SIIIIIII)I", (void*)GLUE_NAME(PV__SIIIIIII)},
	{"sysCallPV", "(SIIIIIIII)I", (void*)GLUE_NAME(PV__SIIIIIIII)},
	{"sysCallPVL", "(S)J", (void*)GLUE_NAME(PVL__S)},
	{"sysCallPVL", "(SI)J", (void*)GLUE_NAME(PVL__SI)},
	{"sysCallPVL", "(SII)J", (void*)GLUE_NAME(PVL__SII)},
	{"sysCallPVL", "(SIII)J", (void*)GLUE_NAME(PVL__SIII)},
	{"sysCallPVL", "(SIIII)J", (void*)GLUE_NAME(PVL__SIIII)},
	{"sysCallPVL", "(SIIIII)J", (void*)GLUE_NAME(PVL__SIIIII)},
	{"sysCallPVL", "(SIIIIII)J", (void*)GLUE_NAME(PVL__SIIIIII)},
	{"sysCallPVL", "(SIIIIIII)J", (void*)GLUE_NAME(PVL__SIIIIIII)},
	{"sysCallPVL", "(SIIIIIIII)J", (void*)GLUE_NAME(PVL__SIIIIIIII)}
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
