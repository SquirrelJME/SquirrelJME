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

// Method handler for special functions
JNIEXPORT void JNICALL restrictedFunction(JNIEnv* env, jclass classy);

// Assembly method mappings
static const JNINativeMethod assemblyMethods[] =
{
	{"arrayLength", "(J)I", (void*)restrictedFunction},
	{"arrayLength", "(Ljava/lang/Object;)I", (void*)restrictedFunction},
	{"arrayLengthSet", "(JI)V", (void*)restrictedFunction},
	{"arrayLengthSet", "(Ljava/lang/Object;I)V", (void*)restrictedFunction},
	{"atomicCompareGetAndSet", "(IIJ)I", (void*)restrictedFunction},
	{"atomicDecrementAndGet", "(J)I", (void*)restrictedFunction},
	{"atomicIncrement", "(J)V", (void*)restrictedFunction},
	{"breakpoint", "()V", (void*)restrictedFunction},
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
	{"doublePack", "(II)D", (void*)restrictedFunction},
	{"doubleToRawLongBits", "(D)J", (void*)restrictedFunction},
	{"doubleUnpackHigh", "(D)I", (void*)restrictedFunction},
	{"doubleUnpackLow", "(D)I", (void*)restrictedFunction},
	{"exceptionHandle", "()V", (void*)restrictedFunction},
	{"floatToRawIntBits", "(F)I", (void*)restrictedFunction},
	{"intBitsToFloat", "(I)F", (void*)restrictedFunction},
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
	{"longBitsToDouble", "(J)D", (void*)restrictedFunction},
	{"longPack", "(II)J", (void*)restrictedFunction},
	{"longUnpackHigh", "(J)I", (void*)restrictedFunction},
	{"longUnpackLow", "(J)I", (void*)restrictedFunction},
	{"memReadByte", "(JI)I", (void*)restrictedFunction},
	{"memReadInt", "(JI)I", (void*)restrictedFunction},
	{"memReadJavaInt", "(JI)I", (void*)restrictedFunction},
	{"memReadJavaLong", "(JI)J", (void*)restrictedFunction},
	{"memReadJavaShort", "(JI)I", (void*)restrictedFunction},
	{"memReadPointer", "(JI)J", (void*)restrictedFunction},
	{"memReadShort", "(JI)I", (void*)restrictedFunction},
	{"memWriteByte", "(JII)V", (void*)restrictedFunction},
	{"memWriteInt", "(JII)V", (void*)restrictedFunction},
	{"memWriteJavaInt", "(JII)V", (void*)restrictedFunction},
	{"memWriteJavaLong", "(JIJ)V", (void*)restrictedFunction},
	{"memWriteJavaShort", "(JII)V", (void*)restrictedFunction},
	{"memWritePointer", "(JIJ)V", (void*)restrictedFunction},
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
	{"objectGetClassInfo", "(J)Lcc/squirreljme/jvm/ClassInfo;", (void*)restrictedFunction},
	{"objectGetClassInfo", "(Ljava/lang/Object;)Lcc/squirreljme/jvm/ClassInfo;", (void*)restrictedFunction},
	{"objectGetClassInfoPointer", "(J)J", (void*)restrictedFunction},
	{"objectGetClassInfoPointer", "(Ljava/lang/Object;)J", (void*)restrictedFunction},
	{"objectSetClassInfo", "(JJ)V", (void*)restrictedFunction},
	{"objectSetClassInfo", "(Ljava/lang/Object;J)V", (void*)restrictedFunction},
	{"objectSetClassInfo", "(JLcc/squirreljme/jvm/ClassInfo;)V", (void*)restrictedFunction},
	{"objectSetClassInfo", "(Ljava/lang/Object;Lcc/squirreljme/jvm/ClassInfo;)V", (void*)restrictedFunction},
	{"objectToPointer", "(Ljava/lang/Object;)I", (void*)restrictedFunction},
	{"objectToPointerWide", "(Ljava/lang/Object;)J", (void*)restrictedFunction},
	{"objectToPointerRefQueue", "(Ljava/lang/Object;)I", (void*)restrictedFunction},
	{"objectToPointerRefQueueWide", "(Ljava/lang/Object;)J", (void*)restrictedFunction},
	{"pointerToObject", "(I)Ljava/lang/Object;", (void*)restrictedFunction},
	{"pointerToObject", "(J)Ljava/lang/Object;", (void*)restrictedFunction},
	{"pointerToClassInfo", "(J)Lcc/squirreljme/jvm/ClassInfo;", (void*)restrictedFunction},
	{"poolLoad", "(JI)J", (void*)restrictedFunction},
	{"poolLoad", "(Ljava/lang/Object;I)J", (void*)restrictedFunction},
	{"poolStore", "(JIJ)V", (void*)restrictedFunction},
	{"poolStore", "(Ljava/lang/Object;IJ)V", (void*)restrictedFunction},
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
	{"sizeOfBaseArray", "()I", (void*)restrictedFunction},
	{"sizeOfBaseObject", "()I", (void*)restrictedFunction},
	{"sizeOfPointer", "()I", (void*)restrictedFunction},
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
