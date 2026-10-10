/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * All VM functions.
 * 
 * @since 2026/06/28
 */

#ifndef SJME_C_SQUIRRELJME_VMALL_H
#define SJME_C_SQUIRRELJME_VMALL_H

#include "sjme/config.h"
#include "frontend/libjvm/commonJniJvm.h"
#include "frontend/libjvm/internals.h"
#include "frontend/libjvm/vmArray.h"
#include "frontend/libjvm/vmCompiler.h"
#include "frontend/libjvm/vmCore.h"
#include "frontend/libjvm/vmDebug.h"
#include "frontend/libjvm/vmFile.h"
#include "frontend/libjvm/vmFloat.h"
#include "frontend/libjvm/vmIoNet.h"
#include "frontend/libjvm/vmObject.h"
#include "frontend/libjvm/vmObjectInput.h"
#include "frontend/libjvm/vmOther.h"
#include "frontend/libjvm/vmPackage.h"
#include "frontend/libjvm/vmReflect.h"
#include "frontend/libjvm/vmRuntime.h"
#include "frontend/libjvm/vmSecurity.h"
#include "frontend/libjvm/vmString.h"
#include "frontend/libjvm/vmSystem.h"
#include "frontend/libjvm/vmThread.h"
#include "frontend/libjvm/vmThrowable.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_VMALL_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#define sjme_jni_JvmDestroyJavaVMImpl sjme_jni_JvmTodoImpl
#define sjme_jni_JvmAttachCurrentThreadImpl sjme_jni_JvmTodoImpl
#define sjme_jni_JvmDetachCurrentThreadImpl sjme_jni_JvmTodoImpl
#define sjme_jni_JvmGetEnvImpl sjme_jni_JvmTodoImpl
#define sjme_jni_JvmAttachCurrentThreadAsDaemonImpl sjme_jni_JvmTodoImpl
#define sjme_jni_EnvGetVersionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvDefineClassImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvFindClassImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvFromReflectedMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvFromReflectedFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvToReflectedMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetSuperclassImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvIsAssignableFromImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvToReflectedFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvThrowImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvThrowNewImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvExceptionOccurredImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvExceptionDescribeImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvExceptionClearImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvFatalErrorImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvPushLocalFrameImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvPopLocalFrameImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewGlobalRefImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvDeleteGlobalRefImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvDeleteLocalRefImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvIsSameObjectImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewLocalRefImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvEnsureLocalCapacityImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvAllocObjectImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewObjectImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewObjectVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewObjectAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetObjectClassImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvIsInstanceOfImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetMethodIDImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallObjectMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallObjectMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallObjectMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallBooleanMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallBooleanMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallBooleanMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallByteMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallByteMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallByteMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallCharMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallCharMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallCharMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallShortMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallShortMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallShortMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallIntMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallIntMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallIntMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallLongMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallLongMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallLongMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallFloatMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallFloatMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallFloatMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallDoubleMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallDoubleMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallDoubleMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallVoidMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallVoidMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallVoidMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualObjectMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualObjectMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualObjectMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualBooleanMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualBooleanMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualBooleanMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualByteMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualByteMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualByteMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualCharMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualCharMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualCharMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualShortMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualShortMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualShortMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualIntMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualIntMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualIntMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualLongMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualLongMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualLongMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualFloatMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualFloatMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualFloatMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualDoubleMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualDoubleMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualDoubleMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualVoidMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualVoidMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallNonvirtualVoidMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetFieldIDImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetObjectFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetBooleanFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetByteFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetCharFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetShortFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetIntFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetLongFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetFloatFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetDoubleFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetObjectFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetBooleanFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetByteFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetCharFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetShortFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetIntFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetLongFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetFloatFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetDoubleFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStaticMethodIDImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticObjectMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticObjectMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticObjectMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticBooleanMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticBooleanMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticBooleanMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticByteMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticByteMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticByteMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticCharMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticCharMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticCharMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticShortMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticShortMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticShortMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticIntMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticIntMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticIntMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticLongMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticLongMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticLongMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticFloatMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticFloatMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticFloatMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticDoubleMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticDoubleMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticDoubleMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticVoidMethodImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticVoidMethodVImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvCallStaticVoidMethodAImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStaticFieldIDImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStaticObjectFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStaticBooleanFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStaticByteFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStaticCharFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStaticShortFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStaticIntFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStaticLongFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStaticFloatFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStaticDoubleFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetStaticObjectFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetStaticBooleanFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetStaticByteFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetStaticCharFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetStaticShortFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetStaticIntFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetStaticLongFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetStaticFloatFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetStaticDoubleFieldImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewStringImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStringLengthImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStringCharsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvReleaseStringCharsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewStringUTFImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStringUTFLengthImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStringUTFCharsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvReleaseStringUTFCharsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetArrayLengthImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewObjectArrayImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetObjectArrayElementImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetObjectArrayElementImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewBooleanArrayImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewByteArrayImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewCharArrayImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewShortArrayImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewIntArrayImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewLongArrayImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewFloatArrayImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewDoubleArrayImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetBooleanArrayElementsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetByteArrayElementsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetCharArrayElementsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetShortArrayElementsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetIntArrayElementsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetLongArrayElementsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetFloatArrayElementsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetDoubleArrayElementsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvReleaseBooleanArrayElementsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvReleaseByteArrayElementsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvReleaseCharArrayElementsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvReleaseShortArrayElementsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvReleaseIntArrayElementsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvReleaseLongArrayElementsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvReleaseFloatArrayElementsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvReleaseDoubleArrayElementsImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetBooleanArrayRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetByteArrayRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetCharArrayRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetShortArrayRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetIntArrayRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetLongArrayRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetFloatArrayRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetDoubleArrayRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetBooleanArrayRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetByteArrayRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetCharArrayRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetShortArrayRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetIntArrayRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetLongArrayRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetFloatArrayRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvSetDoubleArrayRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvRegisterNativesImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvUnregisterNativesImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvMonitorEnterImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvMonitorExitImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetJavaVMImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStringRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStringUTFRegionImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetPrimitiveArrayCriticalImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvReleasePrimitiveArrayCriticalImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetStringCriticalImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvReleaseStringCriticalImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewWeakGlobalRefImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvDeleteWeakGlobalRefImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvExceptionCheckImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvNewDirectByteBufferImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetDirectBufferAddressImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetDirectBufferCapacityImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetObjectRefTypeImpl sjme_jni_EnvTodoImpl
#define sjme_jni_EnvGetModuleImpl sjme_jni_EnvTodoImpl

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_VMALL_H
}
#undef SJME_CXX_SQUIRRELJME_VMALL_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_VMALL_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_VMALL_H */