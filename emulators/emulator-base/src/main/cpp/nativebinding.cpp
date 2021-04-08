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
#include "squirreljme.h"

JNIEXPORT jlong JNICALL longPack(JNIEnv* env, jclass classy, jint al, jint ah)
{
	return (((jlong)al) & UINT64_C(0xFFFFFFFF)) |
		((((jlong)ah) & UINT64_C(0xFFFFFFFF)) << UINT64_C(32));
}

// Method handler for special functions
JNIEXPORT void JNICALL restrictedFunction(JNIEnv* env, jclass classy);

// Assembly method mappings
static const JNINativeMethod assemblyMethods[] =
{
	{"arrayLength", "(I)I", (void*)restrictedFunction},
	{"arrayLength", "(Ljava/lang/Object;)I", (void*)restrictedFunction},
	{"atomicCompareGetAndSet", "(IIJ)I", (void*)restrictedFunction},
	{"atomicDecrementAndGet", "(J)I", (void*)restrictedFunction},
	{"atomicIncrement", "(J)V", (void*)restrictedFunction},
	{"breakpoint", "()V", (void*)restrictedFunction},
	{"doublePack", "(II)D", (void*)restrictedFunction},
	{"doubleToRawLongBits", "(D)J", (void*)restrictedFunction},
	{"doubleUnpackHigh", "(D)I", (void*)restrictedFunction},
	{"doubleUnpackLow", "(D)I", (void*)restrictedFunction},
	{"exceptionHandle", "()V", (void*)restrictedFunction},
	{"floatToRawIntBits", "(F)I", (void*)restrictedFunction},
	{"intBitsToFloat", "(I)F", (void*)restrictedFunction},
	{"invoke", "(JI)V", (void*)restrictedFunction},
	{"invoke", "(JII)V", (void*)restrictedFunction},
	{"invoke", "(JIII)V", (void*)restrictedFunction},
	{"invoke", "(JIIII)V", (void*)restrictedFunction},
	{"invoke", "(JIIIII)V", (void*)restrictedFunction},
	{"invoke", "(JIIIIII)V", (void*)restrictedFunction},
	{"invoke", "(JIIIIIII)V", (void*)restrictedFunction},
	{"invoke", "(JIIIIIIII)V", (void*)restrictedFunction},
	{"invoke", "(JIIIIIIIII)V", (void*)restrictedFunction},
	{"invokeV", "(JI)I", (void*)restrictedFunction},
	{"invokeV", "(JII)I", (void*)restrictedFunction},
	{"invokeV", "(JIII)I", (void*)restrictedFunction},
	{"invokeV", "(JIIII)I", (void*)restrictedFunction},
	{"invokeV", "(JIIIII)I", (void*)restrictedFunction},
	{"invokeV", "(JIIIIII)I", (void*)restrictedFunction},
	{"invokeV", "(JIIIIIII)I", (void*)restrictedFunction},
	{"invokeV", "(JIIIIIIII)I", (void*)restrictedFunction},
	{"invokeV", "(JIIIIIIIII)I", (void*)restrictedFunction},
	{"invokeVL", "(JI)J", (void*)restrictedFunction},
	{"invokeVL", "(JII)J", (void*)restrictedFunction},
	{"invokeVL", "(JIII)J", (void*)restrictedFunction},
	{"invokeVL", "(JIIII)J", (void*)restrictedFunction},
	{"invokeVL", "(JIIIII)J", (void*)restrictedFunction},
	{"invokeVL", "(JIIIIII)J", (void*)restrictedFunction},
	{"invokeVL", "(JIIIIIII)J", (void*)restrictedFunction},
	{"invokeVL", "(JIIIIIIII)J", (void*)restrictedFunction},
	{"invokeVL", "(JIIIIIIIII)J", (void*)restrictedFunction},
	{"longBitsToDouble", "(J)D", (void*)restrictedFunction},
	{"longPack", "(II)J", (void*)longPack},
	{"longUnpackHigh", "(J)I", (void*)restrictedFunction},
	{"longUnpackLow", "(J)I", (void*)restrictedFunction},
	{"memReadByte", "(JI)I", (void*)restrictedFunction},
	{"memReadInt", "(JI)I", (void*)restrictedFunction},
	{"memReadLong", "(JI)J", (void*)restrictedFunction},
	{"memReadShort", "(JI)I", (void*)restrictedFunction},
	{"memWriteByte", "(JII)V", (void*)restrictedFunction},
	{"memWriteInt", "(JII)V", (void*)restrictedFunction},
	{"memWriteLong", "(JIJ)V", (void*)restrictedFunction},
	{"memWriteShort", "(JII)V", (void*)restrictedFunction},
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
	{"objectToPointer", "(Ljava/lang/Object;)I", (void*)restrictedFunction},
	{"objectToPointerRefQueue", "(Ljava/lang/Object;)I", (void*)restrictedFunction},
	{"pointerToObject", "(I)Ljava/lang/Object;", (void*)restrictedFunction},
	{"poolLoad", "(II)I", (void*)restrictedFunction},
	{"poolLoad", "(Ljava/lang/Object;I)I", (void*)restrictedFunction},
	{"refCount", "(I)V", (void*)restrictedFunction},
	{"refCount", "(Ljava/lang/Object;)V", (void*)restrictedFunction},
	{"refGetCount", "(I)I", (void*)restrictedFunction},
	{"refGetCount", "(Ljava/lang/Object;)I", (void*)restrictedFunction},
	{"refSetCount", "(II)V", (void*)restrictedFunction},
	{"refSetCount", "(Ljava/lang/Object;I)V", (void*)restrictedFunction},
	{"refUncount", "(I)V", (void*)restrictedFunction},
	{"refUncount", "(Ljava/lang/Object;)V", (void*)restrictedFunction},
	{"returnFrame", "()V", (void*)restrictedFunction},
	{"returnFrame", "(I)V", (void*)restrictedFunction},
	{"returnFrame", "(II)V", (void*)restrictedFunction},
	{"returnFrameLong", "(J)V", (void*)restrictedFunction},
	{"specialGetExceptionRegister", "()Ljava/lang/Object;", (void*)restrictedFunction},
	{"specialGetExceptionRegisterThrowable", "()Ljava/lang/Throwable;", (void*)restrictedFunction},
	{"specialGetExceptionRegisterPointer", "()J", (void*)restrictedFunction},
	{"specialGetPoolRegister", "()Ljava/lang/Object;", (void*)restrictedFunction},
	{"specialGetPoolRegisterPointer", "()J", (void*)restrictedFunction},
	{"specialGetReturnRegister", "()I", (void*)restrictedFunction},
	{"specialGetReturnRegisterLong", "()J", (void*)restrictedFunction},
	{"specialGetThreadRegister", "()Ljava/lang/Thread;", (void*)restrictedFunction},
	{"specialGetThreadRegisterPointer", "()J", (void*)restrictedFunction},
	{"specialSetExceptionRegister", "(J)V", (void*)restrictedFunction},
	{"specialSetExceptionRegister", "(Ljava/lang/Object;)V", (void*)restrictedFunction},
	{"specialSetPoolRegister", "(J)V", (void*)restrictedFunction},
	{"specialSetPoolRegister", "(Ljava/lang/Object;)V", (void*)restrictedFunction},
	{"specialSetThreadRegister", "(J)V", (void*)restrictedFunction},
	{"specialSetThreadRegister", "(Ljava/lang/Thread;)V", (void*)restrictedFunction},
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
{
	JNIEnv* env;

	// Used to indicate that something might be happened
	fprintf(stderr, "JNI Sub-Level: Loading Library...\n");

	// Support Java 7!
	return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL restrictedFunction(JNIEnv* env, jclass classy)
{
	// Call the oops method, so we can get a reasonable stack trace
	env->Throw((jthrowable)forwardCallStaticObject(env,
		"cc.squirreljme.runtime.cldc.debug.Debugging",
		"oops", "()java/lang/Error"));
}

JNIEXPORT jint JNICALL Java_cc_squirreljme_emulator_NativeBinding__1_1bindMethods
	(JNIEnv* env, jclass classy)
{
	jint rv = 0;
	
	// It is happening!
	fprintf(stderr, "JNI Sub-Level: Binding Methods...\n");

	rv |= env->RegisterNatives(env->FindClass("cc/squirreljme/jvm/Assembly"),
		assemblyMethods, sizeof(assemblyMethods) / sizeof(JNINativeMethod));
	
	rv |= mleDebugInit(env, classy);
	rv |= mleFormInit(env, classy);
	rv |= mleJarInit(env, classy);
	rv |= mleObjectInit(env, classy);
	rv |= mlePencilInit(env, classy);
	rv |= mleRuntimeInit(env, classy);
	rv |= mleTaskInit(env, classy);
	rv |= mleTerminalInit(env, classy);
	rv |= mleThreadInit(env, classy);
	
	// It happened!
	fprintf(stderr, "JNI Sub-Level: Methods are now bound!\n");

	return rv;
}
