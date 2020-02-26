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

// Method handler for when functions are not implemented at all
JNIEXPORT void JNICALL notImplemented(JNIEnv* env, jclass classy);

// Glue for putting names together
#define GLUE_NAME(name) Java_cc_squirreljme_jvm_Assembly_sysCall##name

// Assembly method mappings
static const JNINativeMethod assemblyMethods[] =
{
	{"arrayLength", "(J)I", Java_cc_squirreljme_jvm_Assembly_arrayLength__J},
	{"arrayLength", "(Ljava/lang/Object;)I", Java_cc_squirreljme_jvm_Assembly_arrayLength__Ljava_lang_Object_2},
	{"arrayLengthSet", "(JI)V", notImplemented},
	{"arrayLengthSet", "(Ljava/lang/Object;I)V", notImplemented},
	{"atomicCompareGetAndSet", "(IIJ)I", notImplemented},
	{"atomicDecrementAndGet", "(J)I", notImplemented},
	{"atomicIncrement", "(J)V", notImplemented},
	{"breakpoint", "()V", notImplemented},
	{"classInfoOfBoolean", "()Lcc/squirreljme/jvm/ClassInfo;", notImplemented},
	{"classInfoOfBooleanPointer", "()J", notImplemented},
	{"classInfoOfByte", "()Lcc/squirreljme/jvm/ClassInfo;", notImplemented},
	{"classInfoOfBytePointer", "()J", notImplemented},
	{"classInfoOfCharacter", "()Lcc/squirreljme/jvm/ClassInfo;", notImplemented},
	{"classInfoOfCharacterPointer", "()J", notImplemented},
	{"classInfoOfDouble", "()Lcc/squirreljme/jvm/ClassInfo;", notImplemented},
	{"classInfoOfDoublePointer", "()J", notImplemented},
	{"classInfoOfFloat", "()Lcc/squirreljme/jvm/ClassInfo;", notImplemented},
	{"classInfoOfFloatPointer", "()J", notImplemented},
	{"classInfoOfInteger", "()Lcc/squirreljme/jvm/ClassInfo;", notImplemented},
	{"classInfoOfIntegerPointer", "()J", notImplemented},
	{"classInfoOfLong", "()Lcc/squirreljme/jvm/ClassInfo;", notImplemented},
	{"classInfoOfLongPointer", "()J", notImplemented},
	{"classInfoOfShort", "()Lcc/squirreljme/jvm/ClassInfo;", notImplemented},
	{"classInfoOfShortPointer", "()J", notImplemented},
	{"doublePack", "(II)D", notImplemented},
	{"doubleToRawLongBits", "(D)J", notImplemented},
	{"doubleUnpackHigh", "(D)I", notImplemented},
	{"doubleUnpackLow", "(D)I", notImplemented},
	{"exceptionHandle", "()V", notImplemented},
	{"floatToRawIntBits", "(F)I", notImplemented},
	{"intBitsToFloat", "(I)F", notImplemented},
	{"invoke", "(JJ)V", notImplemented},
	{"invoke", "(JJI)V", notImplemented},
	{"invoke", "(JJII)V", notImplemented},
	{"invoke", "(JJIII)V", notImplemented},
	{"invoke", "(JJIIII)V", notImplemented},
	{"invoke", "(JJIIIII)V", notImplemented},
	{"invoke", "(JJIIIIII)V", notImplemented},
	{"invoke", "(JJIIIIIII)V", notImplemented},
	{"invoke", "(JJIIIIIIII)V", notImplemented},
	{"invokeV", "(JJ)I", notImplemented},
	{"invokeV", "(JJI)I", notImplemented},
	{"invokeV", "(JJII)I", notImplemented},
	{"invokeV", "(JJIII)I", notImplemented},
	{"invokeV", "(JJIIII)I", notImplemented},
	{"invokeV", "(JJIIIII)I", notImplemented},
	{"invokeV", "(JJIIIIII)I", notImplemented},
	{"invokeV", "(JJIIIIIII)I", notImplemented},
	{"invokeV", "(JJIIIIIIII)I", notImplemented},
	{"invokeVL", "(JJ)J", notImplemented},
	{"invokeVL", "(JJI)J", notImplemented},
	{"invokeVL", "(JJII)J", notImplemented},
	{"invokeVL", "(JJIII)J", notImplemented},
	{"invokeVL", "(JJIIII)J", notImplemented},
	{"invokeVL", "(JJIIIII)J", notImplemented},
	{"invokeVL", "(JJIIIIII)J", notImplemented},
	{"invokeVL", "(JJIIIIIII)J", notImplemented},
	{"invokeVL", "(JJIIIIIIII)J", notImplemented},
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
	{"monitorCountDecrementAndGetAtomic", "(J)I", notImplemented},
	{"monitorCountDecrementAndGetAtomic", "(Ljava/lang/Object;)I", notImplemented},
	{"monitorCountIncrementAndGetAtomic", "(J)I", notImplemented},
	{"monitorCountIncrementAndGetAtomic", "(Ljava/lang/Object;)I", notImplemented},
	{"monitorCountGetAtomic", "(J)I", notImplemented},
	{"monitorCountGetAtomic", "(Ljava/lang/Object;)I", notImplemented},
	{"monitorCountSetAtomic", "(JI)V", notImplemented},
	{"monitorCountSetAtomic", "(Ljava/lang/Object;I)V", notImplemented},
	{"monitorOwnerCompareGetAndSetAtomic", "(JJJ)Ljava/lang/Thread;", notImplemented},
	{"monitorOwnerCompareGetAndSetAtomic", "(Ljava/lang/Object;JJ)Ljava/lang/Thread;", notImplemented},
	{"monitorOwnerCompareGetAndSetAtomic", "(JLjava/lang/Thread;Ljava/lang/Thread;)Ljava/lang/Thread;", notImplemented},
	{"monitorOwnerCompareGetAndSetAtomic", "(Ljava/lang/Object;Ljava/lang/Thread;Ljava/lang/Thread;)Ljava/lang/Thread;", notImplemented},
	{"monitorOwnerCompareGetAndSetAtomicPointer", "(JJJ)J", notImplemented},
	{"monitorOwnerCompareGetAndSetAtomicPointer", "(Ljava/lang/Object;JJ)J", notImplemented},
	{"monitorOwnerCompareGetAndSetAtomicPointer", "(JLjava/lang/Thread;Ljava/lang/Thread;)J", notImplemented},
	{"monitorOwnerCompareGetAndSetAtomicPointer", "(Ljava/lang/Object;Ljava/lang/Thread;Ljava/lang/Thread;)J", notImplemented},
	{"monitorOwnerGetAtomic", "(J)Ljava/lang/Thread;", notImplemented},
	{"monitorOwnerGetAtomic", "(Ljava/lang/Object;)Ljava/lang/Thread;", notImplemented},
	{"monitorOwnerGetPointerAtomic", "(J)J", notImplemented},
	{"monitorOwnerGetPointerAtomic", "(Ljava/lang/Object;)J", notImplemented},
	{"monitorOwnerSetAtomic", "(JJ)V", notImplemented},
	{"monitorOwnerSetAtomic", "(JLjava/lang/Thread;)V", notImplemented},
	{"monitorOwnerSetAtomic", "(Ljava/lang/Object;J)V", notImplemented},
	{"monitorOwnerSetAtomic", "(Ljava/lang/Object;Ljava/lang/Thread;)V", notImplemented},
	{"objectGetClassInfo", "(J)Lcc/squirreljme/jvm/ClassInfo;", notImplemented},
	{"objectGetClassInfo", "(Ljava/lang/Object;)Lcc/squirreljme/jvm/ClassInfo;", notImplemented},
	{"objectGetClassInfoPointer", "(J)J", notImplemented},
	{"objectGetClassInfoPointer", "(Ljava/lang/Object;)J", notImplemented},
	{"objectSetClassInfo", "(JJ)V", notImplemented},
	{"objectSetClassInfo", "(Ljava/lang/Object;J)V", notImplemented},
	{"objectSetClassInfo", "(JLcc/squirreljme/jvm/ClassInfo;)V", notImplemented},
	{"objectSetClassInfo", "(Ljava/lang/Object;Lcc/squirreljme/jvm/ClassInfo;)V", notImplemented},
	{"objectToPointer", "(Ljava/lang/Object;)J", Java_cc_squirreljme_jvm_Assembly_objectToPointer},
	{"objectToPointerRefQueue", "(Ljava/lang/Object;)J", Java_cc_squirreljme_jvm_Assembly_objectToPointer},
	{"pointerToObject", "(J)Ljava/lang/Object;", Java_cc_squirreljme_jvm_Assembly_pointerToObject},
	{"pointerToClassInfo", "(J)Lcc/squirreljme/jvm/ClassInfo;", Java_cc_squirreljme_jvm_Assembly_pointerToObject},
	{"poolLoad", "(JI)J", notImplemented},
	{"poolLoad", "(Ljava/lang/Object;I)J", notImplemented},
	{"poolStore", "(JIJ)V", notImplemented},
	{"poolStore", "(Ljava/lang/Object;IJ)V", notImplemented},
	{"refCount", "(J)V", notImplemented},
	{"refCount", "(Ljava/lang/Object;)V", notImplemented},
	{"refGetCount", "(J)I", notImplemented},
	{"refGetCount", "(Ljava/lang/Object;)I", notImplemented},
	{"refSetCount", "(JI)V", notImplemented},
	{"refSetCount", "(Ljava/lang/Object;I)V", notImplemented},
	{"refUncount", "(J)V", notImplemented},
	{"refUncount", "(Ljava/lang/Object;)V", notImplemented},
	{"returnFrame", "()V", notImplemented},
	{"returnFrame", "(I)V", notImplemented},
	{"returnFrame", "(II)V", notImplemented},
	{"returnFrameLong", "(J)V", notImplemented},
	{"sizeOfBaseArray", "()I", notImplemented},
	{"sizeOfBaseObject", "()I", notImplemented},
	{"sizeOfPointer", "()I", notImplemented},
	{"specialGetExceptionRegister", "()Ljava/lang/Object;", notImplemented},
	{"specialGetExceptionRegisterThrowable", "()Ljava/lang/Throwable;", notImplemented},
	{"specialGetExceptionRegisterPointer", "()J", notImplemented},
	{"specialGetPoolRegister", "()Ljava/lang/Object;", notImplemented},
	{"specialGetPoolRegisterPointer", "()J", notImplemented},
	{"specialGetReturnRegister", "()I", notImplemented},
	{"specialGetReturnRegisterLong", "()J", notImplemented},
	{"specialGetStaticFieldRegister", "()J", notImplemented},
	{"specialGetThreadRegister", "()Ljava/lang/Thread;", notImplemented},
	{"specialGetThreadRegisterPointer", "()J", notImplemented},
	{"specialSetExceptionRegister", "(J)V", notImplemented},
	{"specialSetExceptionRegister", "(Ljava/lang/Object;)V", notImplemented},
	{"specialSetPoolRegister", "(J)V", notImplemented},
	{"specialSetPoolRegister", "(Ljava/lang/Object;)V", notImplemented},
	{"specialSetStaticFieldRegister", "(J)V", notImplemented},
	{"specialSetThreadRegister", "(J)V", notImplemented},
	{"specialSetThreadRegister", "(Ljava/lang/Thread;)V", notImplemented},

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

JNIEXPORT void JNICALL notImplemented(JNIEnv* env, jclass classy)
{
	fprintf(stderr, "Not implemented %s.\n", __func__);
	env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"),
		__func__);
}

JNIEXPORT jint JNICALL Java_cc_squirreljme_emulator_NativeBinding__1_1bindMethods
	(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(env->FindClass("cc/squirreljme/jvm/Assembly"),
		assemblyMethods, sizeof(assemblyMethods) / sizeof(JNINativeMethod));
}
