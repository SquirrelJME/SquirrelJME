/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Reverse engineered prototypes.
 * 
 * @since 2023/12/09
 */

#ifndef SQUIRRELJME_REVPROTO_H
#define SQUIRRELJME_REVPROTO_H

#include "frontend/libjvm/revTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_REVPROTO_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

libjvm_re_undefined8 JNI_GetCreatedJavaVMs(libjvm_re_undefined8* param_1,
	libjvm_re_int param_2, libjvm_re_undefined4* param_3);

libjvm_re_int JNI_GetDefaultJavaVMInitArgs(libjvm_re_int* param_1);

void JVM_Accept(libjvm_re_int param_1, libjvm_re_sockaddr* param_2,
	libjvm_re_socklen_t* param_3);

libjvm_re_undefined4 JVM_ActiveProcessorCount(void);

libjvm_re_undefined8 JVM_AllocateNewArray(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_undefined8* param_3,
	libjvm_re_undefined4 param_4);

libjvm_re_undefined8 JVM_AllocateNewObject(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_undefined8* param_3,
	libjvm_re_undefined8* param_4);

void JVM_ArrayCopy(libjvm_re_long param_1, libjvm_re_undefined8 param_2,
	libjvm_re_long* param_3, libjvm_re_undefined4 param_4,
	libjvm_re_undefined8* param_5, libjvm_re_undefined4 param_6,
	libjvm_re_undefined4 param_7);

libjvm_re_undefined8 JVM_AssertionStatusDirectives(libjvm_re_long param_1);

void JVM_Available(libjvm_re_undefined4 param_1, libjvm_re_undefined8 param_2);

void JVM_Bind(libjvm_re_int param_1, libjvm_re_sockaddr* param_2,
	libjvm_re_socklen_t param_3);

libjvm_re_int JVM_ClassDepth(libjvm_re_long param_1, libjvm_re_long* param_2);

libjvm_re_int JVM_ClassLoaderDepth(libjvm_re_long param_1);

libjvm_re_undefined8 JVM_Clone(libjvm_re_long param_1,
	libjvm_re_long* param_2);

void JVM_Close(libjvm_re_int param_1);

libjvm_re_undefined8 JVM_CompileClass(void);

libjvm_re_undefined8 JVM_CompileClasses(void);

libjvm_re_undefined8 JVM_CompilerCommand(void);

void JVM_Connect(libjvm_re_undefined4 param_1, libjvm_re_undefined8 param_2,
	libjvm_re_undefined4 param_3);

libjvm_re_undefined8 JVM_ConstantPoolGetClassAt(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_long* param_3,
	libjvm_re_int param_4);

libjvm_re_undefined8 JVM_ConstantPoolGetClassAtIfLoaded(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_long* param_3,
	libjvm_re_int param_4);

libjvm_re_undefined8 JVM_ConstantPoolGetDoubleAt(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_long* param_3,
	libjvm_re_int param_4);

libjvm_re_undefined8 JVM_ConstantPoolGetFieldAt(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_long* param_3,
	libjvm_re_int param_4);

libjvm_re_undefined8 JVM_ConstantPoolGetFieldAtIfLoaded(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_long* param_3,
	libjvm_re_int param_4);

libjvm_re_undefined4 JVM_ConstantPoolGetFloatAt(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_long* param_3,
	libjvm_re_int param_4);

libjvm_re_undefined4 JVM_ConstantPoolGetIntAt(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_long* param_3,
	libjvm_re_int param_4);

libjvm_re_undefined8 JVM_ConstantPoolGetLongAt(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_long* param_3,
	libjvm_re_int param_4);

libjvm_re_undefined8 JVM_ConstantPoolGetMemberRefInfoAt(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_long* param_3,
	libjvm_re_int param_4);

libjvm_re_undefined8 JVM_ConstantPoolGetMethodAt(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_long* param_3,
	libjvm_re_int param_4);

libjvm_re_undefined8 JVM_ConstantPoolGetMethodAtIfLoaded(
	libjvm_re_long param_1, libjvm_re_undefined8 param_2,
	libjvm_re_long* param_3, libjvm_re_int param_4);

libjvm_re_undefined4 JVM_ConstantPoolGetSize(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_long* param_3);

libjvm_re_undefined8 JVM_ConstantPoolGetStringAt(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_long* param_3,
	libjvm_re_int param_4);

libjvm_re_undefined8 JVM_ConstantPoolGetUTF8At(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_long* param_3,
	libjvm_re_int param_4);

libjvm_re_int JVM_CountStackFrames(libjvm_re_long param_1,
	libjvm_re_long* param_2);

libjvm_re_undefined8 JVM_CurrentClassLoader(libjvm_re_long param_1);

libjvm_re_undefined8 JVM_CurrentLoadedClass(libjvm_re_long param_1);

libjvm_re_undefined8 JVM_CurrentThread(libjvm_re_long param_1);

void JVM_CurrentTimeMillis(void);

libjvm_re_bool JVM_CX8Field(libjvm_re_long param_1, libjvm_re_long* param_2,
	libjvm_re_ulong param_3, libjvm_re_long param_4, libjvm_re_long param_5);

libjvm_re_undefined8 JVM_DefineClass(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_undefined8 param_3,
	libjvm_re_undefined8 param_4, libjvm_re_undefined4 param_5,
	libjvm_re_undefined8 param_6);

libjvm_re_undefined8 JVM_DefineClassWithSource(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_undefined8 param_3,
	libjvm_re_undefined8 param_4, libjvm_re_undefined4 param_5,
	libjvm_re_undefined8 param_6, libjvm_re_undefined8 param_7);

libjvm_re_undefined8 JVM_DefineClassWithSourceCond(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_undefined8 param_3,
	libjvm_re_undefined8 param_4, libjvm_re_undefined4 param_5,
	libjvm_re_undefined8 param_6, libjvm_re_undefined8 param_7,
	libjvm_re_undefined param_8);

libjvm_re_undefined4 JVM_DesiredAssertionStatus(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_undefined8* param_3);

void JVM_DisableCompiler(void);

libjvm_re_undefined8 JVM_DoPrivileged(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_long* param_3,
	libjvm_re_long* param_4);

libjvm_re_undefined8 JVM_DTraceActivate(libjvm_re_long param_1,
	libjvm_re_undefined4 param_2, libjvm_re_undefined8 param_3,
	libjvm_re_undefined4 param_4, libjvm_re_undefined8 param_5);

void JVM_DTraceDispose(libjvm_re_long param_1, libjvm_re_undefined8 param_2);

libjvm_re_undefined8 JVM_DTraceGetVersion(libjvm_re_long param_1);

libjvm_re_undefined JVM_DTraceIsProbeEnabled(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2);

libjvm_re_undefined JVM_DTraceIsSupported(libjvm_re_long param_1);

void JVM_DumpAllStacks(libjvm_re_long param_1);

libjvm_re_undefined8 JVM_DumpThreads(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_long* param_3);

void JVM_EnableCompiler(void);

void JVM_Exit(libjvm_re_int param_1);

void JVM_FillInStackTrace(libjvm_re_long param_1, libjvm_re_long* param_2);

libjvm_re_undefined8 JVM_FindClassFromBootLoader(libjvm_re_long param_1,
	sjme_lpstr param_2);

libjvm_re_undefined8* JVM_FindClassFromCaller(libjvm_re_long param_1,
	sjme_lpstr param_2, libjvm_re_undefined param_3, libjvm_re_long* param_4,
	libjvm_re_long* param_5);

libjvm_re_undefined8* JVM_FindClassFromClass(libjvm_re_long param_1,
	sjme_lpstr param_2, libjvm_re_undefined param_3, libjvm_re_long* param_4);

libjvm_re_undefined8* JVM_FindClassFromClassLoader(libjvm_re_long param_1,
	sjme_lpstr param_2, libjvm_re_undefined param_3, libjvm_re_undefined8* param_4,
	char param_5);

libjvm_re_undefined8 JVM_FindLoadedClass(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_long* param_3);

libjvm_re_undefined8 JVM_FindPrimitiveClass(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2);

libjvm_re_undefined4 JVM_FindSignal(sjme_lpstr param_1);

libjvm_re_ulong JVM_FreeMemory(void);

void JVM_GC(void);

libjvm_re_undefined8 JVM_GetAllThreads(libjvm_re_long param_1);

libjvm_re_undefined8 JVM_GetArrayElement(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_undefined4 param_3);

libjvm_re_undefined4 JVM_GetArrayLength(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2);

libjvm_re_undefined8 JVM_GetCallerClass(libjvm_re_long param_1,
	libjvm_re_undefined4 param_2);

libjvm_re_uint JVM_GetClassAccessFlags(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined8 JVM_GetClassAnnotations(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined8 JVM_GetClassConstantPool(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined8 JVM_GetClassContext(libjvm_re_long param_1);

libjvm_re_undefined4 JVM_GetClassCPEntriesCount(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

void JVM_GetClassCPTypes(libjvm_re_long param_1, libjvm_re_undefined8* param_2,
	libjvm_re_long param_3);

libjvm_re_undefined8 JVM_GetClassDeclaredConstructors(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, char param_3);

libjvm_re_undefined8 JVM_GetClassDeclaredFields(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, char param_3);

libjvm_re_undefined8 JVM_GetClassDeclaredMethods(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, char param_3);

libjvm_re_undefined2 JVM_GetClassFieldsCount(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined8 JVM_GetClassInterfaces(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined8 JVM_GetClassLoader(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined4 JVM_GetClassMethodsCount(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined4 JVM_GetClassModifiers(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined8 JVM_GetClassName(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined8 JVM_GetClassNameUTF(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined8 JVM_GetClassSignature(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined8 JVM_GetClassSigners(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined8 JVM_GetComponentType(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined8 JVM_GetCPClassNameUTF(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined4 param_3);

libjvm_re_undefined8 JVM_GetCPFieldClassNameUTF(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_int param_3);

libjvm_re_uint JVM_GetCPFieldModifiers(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_int param_3,
	libjvm_re_undefined8* param_4);

libjvm_re_undefined8 JVM_GetCPFieldNameUTF(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_int param_3);

libjvm_re_undefined8 JVM_GetCPFieldSignatureUTF(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_int param_3);

libjvm_re_undefined8 JVM_GetCPMethodClassNameUTF(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_int param_3);

libjvm_re_uint JVM_GetCPMethodModifiers(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_int param_3,
	libjvm_re_undefined8* param_4);

libjvm_re_undefined8 JVM_GetCPMethodNameUTF(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_int param_3);

libjvm_re_undefined8 JVM_GetCPMethodSignatureUTF(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_int param_3);

libjvm_re_undefined8 JVM_GetDeclaredClasses(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined8 JVM_GetDeclaringClass(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined8 JVM_GetEnclosingMethodInfo(libjvm_re_long param_1,
	libjvm_re_long* param_2);

libjvm_re_undefined8 JVM_GetFieldAnnotations(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_ushort JVM_GetFieldIxModifiers(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_int param_3);

void JVM_GetHostName(sjme_lpstr param_1, libjvm_re_int param_2);

libjvm_re_undefined8 JVM_GetInheritedAccessControlContext(
	libjvm_re_long param_1);

libjvm_re_undefined8 JVM_GetInterfaceVersion(void);

void JVM_GetLastErrorString(libjvm_re_undefined8 param_1,
	libjvm_re_int param_2);

libjvm_re_undefined8 JVM_GetManagement(libjvm_re_undefined4 param_1);

libjvm_re_undefined8 JVM_GetMethodAnnotations(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2);

libjvm_re_undefined8 JVM_GetMethodDefaultAnnotationValue(
	libjvm_re_long param_1, libjvm_re_undefined8 param_2);

libjvm_re_undefined2 JVM_GetMethodIxArgsSize(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined4 param_3);

void JVM_GetMethodIxByteCode(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined4 param_3,
	void* param_4);

libjvm_re_undefined2 JVM_GetMethodIxByteCodeLength(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined4 param_3);

void JVM_GetMethodIxExceptionIndexes(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined4 param_3,
	libjvm_re_long param_4);

libjvm_re_undefined4 JVM_GetMethodIxExceptionsCount(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined4 param_3);

void JVM_GetMethodIxExceptionTableEntry(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined4 param_3,
	libjvm_re_int param_4, libjvm_re_uint* param_5);

libjvm_re_ulong JVM_GetMethodIxExceptionTableLength(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined4 param_3);

libjvm_re_undefined2 JVM_GetMethodIxLocalsCount(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined4 param_3);

libjvm_re_undefined2 JVM_GetMethodIxMaxStack(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined4 param_3);

libjvm_re_uint JVM_GetMethodIxModifiers(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined4 param_3);

libjvm_re_undefined8 JVM_GetMethodIxNameUTF(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined4 param_3);

libjvm_re_undefined8 JVM_GetMethodIxSignatureUTF(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined4 param_3);

libjvm_re_undefined8 JVM_GetMethodParameterAnnotations(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2);

libjvm_re_undefined8 JVM_GetPrimitiveArrayElement(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_undefined4 param_3,
	libjvm_re_int param_4);

libjvm_re_undefined8 JVM_GetProtectionDomain(libjvm_re_long param_1,
	libjvm_re_long* param_2);

void JVM_GetSockName(libjvm_re_int param_1, libjvm_re_sockaddr* param_2,
	libjvm_re_socklen_t* param_3);

void JVM_GetSockOpt(libjvm_re_int param_1, libjvm_re_int param_2,
	libjvm_re_int param_3, void* param_4, libjvm_re_socklen_t* param_5);

libjvm_re_undefined8 JVM_GetStackAccessControlContext(libjvm_re_long param_1);

libjvm_re_undefined4 JVM_GetStackTraceDepth(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined8 JVM_GetStackTraceElement(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined4 param_3);

libjvm_re_undefined8 JVM_GetSystemPackage(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined8 JVM_GetSystemPackages(libjvm_re_long param_1);

libjvm_re_undefined8 JVM_GetTemporaryDirectory(libjvm_re_long param_1);

libjvm_re_undefined8 JVM_GetThreadStateNames(libjvm_re_long param_1,
	libjvm_re_undefined4 param_2, libjvm_re_long* param_3);

libjvm_re_undefined8 JVM_GetThreadStateValues(libjvm_re_long param_1,
	libjvm_re_undefined4 param_2);

void JVM_GetVersionInfo(libjvm_re_long param_1, libjvm_re_undefined4* param_2,
	size_t param_3);

void JVM_Halt(libjvm_re_undefined4 param_1);

libjvm_re_undefined8 JVM_handle_linux_signal(libjvm_re_int param_1,
	libjvm_re_long param_2, libjvm_re_undefined8 param_3,
	libjvm_re_int param_4);

libjvm_re_undefined JVM_HoldsLock(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_long* param_3);

libjvm_re_undefined4 JVM_IHashCode(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_long* JVM_InitAgentProperties(libjvm_re_long param_1,
	libjvm_re_long* param_2);

void JVM_InitializeCompiler(void);

libjvm_re_undefined8 JVM_InitializeSocketLibrary(void);

libjvm_re_long* JVM_InitProperties(libjvm_re_long param_1,
	libjvm_re_long* param_2);

libjvm_re_undefined8 JVM_InternString(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

void JVM_Interrupt(libjvm_re_long param_1, libjvm_re_long* param_2);

libjvm_re_undefined8 JVM_InvokeMethod(libjvm_re_long param_1,
	libjvm_re_long* param_2, libjvm_re_long* param_3, libjvm_re_long* param_4);

libjvm_re_uint JVM_IsArrayClass(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_ulong JVM_IsConstructorIx(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_ulong param_3);

libjvm_re_uint JVM_IsInterface(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined4 JVM_IsInterrupted(libjvm_re_long param_1,
	libjvm_re_long* param_2, char param_3);

void JVM_IsNaN(double param_1);

libjvm_re_undefined JVM_IsPrimitiveClass(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined JVM_IsSameClassPackage(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined8* param_3);

libjvm_re_undefined8 JVM_IsSilentCompiler(void);

void JVM_IsSupportedJNIVersion(libjvm_re_undefined4 param_1);

libjvm_re_undefined JVM_IsThreadAlive(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2);

libjvm_re_undefined8 JVM_LatestUserDefinedLoader(libjvm_re_long param_1);

void JVM_Listen(libjvm_re_int param_1, libjvm_re_int param_2);

libjvm_re_undefined8* JVM_LoadClass0(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_undefined8* param_3,
	libjvm_re_long* param_4);

void JVM_Lseek(libjvm_re_int param_1, libjvm_re_off64_t param_2,
	libjvm_re_int param_3);

libjvm_re_ulong JVM_MaxMemory(void);

void JVM_MaxObjectInspectionAge(void);

void JVM_MonitorNotify(libjvm_re_long param_1, libjvm_re_long* param_2);

void JVM_MonitorNotifyAll(libjvm_re_long param_1, libjvm_re_long* param_2);

void JVM_MonitorWait(libjvm_re_long param_1, libjvm_re_long* param_2,
	libjvm_re_long param_3);

void JVM_NanoTime(void);

libjvm_re_undefined8 JVM_NativePath(libjvm_re_undefined8 param_1);

libjvm_re_undefined8 JVM_NewArray(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined4 param_3);

libjvm_re_undefined8 JVM_NewInstanceFromConstructor(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_long* param_3);

libjvm_re_undefined8 JVM_NewMultiArray(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined8 param_3);

void JVM_OnExit(libjvm_re_undefined8 param_1);

libjvm_re_undefined8 JVM_Open(libjvm_re_undefined8 param_1,
	libjvm_re_ulong param_2, libjvm_re_ulong param_3);

void JVM_PrintStackTrace(libjvm_re_long param_1, libjvm_re_undefined8* param_2,
	libjvm_re_undefined8* param_3);

libjvm_re_undefined8 JVM_RaiseSignal(libjvm_re_int param_1);

libjvm_re_long JVM_RawMonitorCreate(void);

void JVM_RawMonitorDestroy(libjvm_re_long param_1);

libjvm_re_undefined8 JVM_RawMonitorEnter(libjvm_re_undefined8 param_1);

void JVM_RawMonitorExit(libjvm_re_undefined8 param_1);

void JVM_Read(libjvm_re_ulong param_1, libjvm_re_undefined8 param_2,
	libjvm_re_ulong param_3);

void JVM_Recv(libjvm_re_ulong param_1, libjvm_re_undefined8 param_2,
	libjvm_re_int param_3, libjvm_re_ulong param_4);

void JVM_RecvFrom(libjvm_re_ulong param_1, libjvm_re_undefined8 param_2,
	libjvm_re_int param_3, libjvm_re_ulong param_4,
	libjvm_re_undefined8 param_5, libjvm_re_undefined4* param_6);

libjvm_re_long JVM_RegisterSignal(libjvm_re_uint param_1,
	libjvm_re_long param_2);

void JVM_ReleaseUTF(void);

void JVM_ResolveClass(libjvm_re_long param_1);

void JVM_ResumeThread(libjvm_re_long param_1, libjvm_re_undefined8* param_2);

void JVM_Send(libjvm_re_ulong param_1, libjvm_re_undefined8 param_2,
	libjvm_re_int param_3, libjvm_re_ulong param_4);

void JVM_SendTo(libjvm_re_ulong param_1, libjvm_re_undefined8 param_2,
	libjvm_re_int param_3, libjvm_re_ulong param_4,
	libjvm_re_undefined8 param_5, libjvm_re_ulong param_6);

void JVM_SetArrayElement(libjvm_re_long param_1, libjvm_re_undefined8 param_2,
	libjvm_re_undefined4 param_3, libjvm_re_undefined8* param_4);

void JVM_SetClassSigners(libjvm_re_long param_1, libjvm_re_undefined8* param_2,
	libjvm_re_undefined8* param_3);

void JVM_SetLength(libjvm_re_int param_1, libjvm_re_off64_t param_2);

void JVM_SetNativeThreadName(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined8* param_3);

void JVM_SetPrimitiveArrayElement(libjvm_re_long param_1,
	libjvm_re_undefined8 param_2, libjvm_re_undefined4 param_3,
	libjvm_re_undefined8 param_4, libjvm_re_undefined param_5);

void JVM_SetProtectionDomain(libjvm_re_long param_1, libjvm_re_long* param_2,
	libjvm_re_undefined8* param_3);

void JVM_SetSockOpt(libjvm_re_int param_1, libjvm_re_int param_2,
	libjvm_re_int param_3, void* param_4, libjvm_re_socklen_t param_5);

void JVM_SetThreadPriority(libjvm_re_long param_1,
	libjvm_re_undefined8* param_2, libjvm_re_undefined4 param_3);

void JVM_Sleep(libjvm_re_long param_1, libjvm_re_undefined8 param_2,
	libjvm_re_long param_3);

void JVM_Socket(libjvm_re_int param_1, libjvm_re_int param_2,
	libjvm_re_int param_3);

void JVM_SocketAvailable(libjvm_re_undefined4 param_1,
	libjvm_re_undefined8 param_2);

void JVM_SocketClose(libjvm_re_int param_1);

void JVM_SocketShutdown(libjvm_re_int param_1, libjvm_re_int param_2);

void JVM_StartThread(libjvm_re_long param_1, libjvm_re_undefined8* param_2);

void JVM_StopThread(libjvm_re_long param_1, libjvm_re_long* param_2,
	libjvm_re_long* param_3);

libjvm_re_undefined JVM_SupportsCX8(void);

void JVM_SuspendThread(libjvm_re_long param_1, libjvm_re_undefined8* param_2);

void JVM_Sync(libjvm_re_int param_1);

void JVM_Timeout(libjvm_re_undefined4 param_1, libjvm_re_undefined8 param_2);

libjvm_re_ulong JVM_TotalMemory(void);

void JVM_TraceInstructions(void);

void JVM_TraceMethodCalls(void);

void JVM_Write(libjvm_re_ulong param_1, libjvm_re_undefined8 param_2,
	libjvm_re_ulong param_3);

void JVM_Yield(libjvm_re_long param_1);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_REVPROTO_H
}
		#undef SJME_CXX_SQUIRRELJME_REVPROTO_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_REVPROTO_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_REVPROTO_H */
