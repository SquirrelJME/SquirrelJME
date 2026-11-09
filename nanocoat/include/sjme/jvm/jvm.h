/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * JVM Compatibility header.
 * 
 * @since 2025/10/22
 */

#ifndef SJME_C_SQUIRRELJME_JVM_H
#define SJME_C_SQUIRRELJME_JVM_H

#include "sjme/config.h"
#include "sjme/jvm/jvm_md.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_JVM_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/
#pragma region(incompleteTypes)

/**
 * Simplifies incomplete types.
 *
 * @param x The name of the type.
 * @since 2026/06/28
 */
#define SJME_JVM_INCOMPLETE__(x) \
	typedef struct SJME_TOKEN_PASTE3(sjme_jvm_incomplete_, x, __)* x

/** DTrace Provider? */
SJME_JVM_INCOMPLETE__(JVM_DTraceProvider);

/** JVM_ExceptionTableEntryType? */
SJME_JVM_INCOMPLETE__(JVM_ExceptionTableEntryType);

/** JVM version info? */
SJME_JVM_INCOMPLETE__(jvm_version_info);

struct sockaddr;

/* No longer needed. */
#undef SJME_JVM_INCOMPLETE__

#pragma endregion(incompleteTypes)
#pragma region(jniFunctionsArray)

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetArrayElement(
	JNIEnv* env, 
	jobject array,
	jint index);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetArrayLength(
	JNIEnv* env, 
	jobject array);

jvalue SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetPrimitiveArrayElement(
	JNIEnv* env,
	jobject array,
	jint index,
	jint wCode);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_NewArray(
	JNIEnv* env, 
	jclass elementType, 
	jint length);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_NewMultiArray(
	JNIEnv* env,
	jclass elementType,
	jintArray dim);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_SetArrayElement(
	JNIEnv* env,
	jobject array,
	jint index,
	jobject val);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_SetPrimitiveArrayElement(
	JNIEnv* env,
	jobject array,
	jint index,
	jvalue val,
	unsigned char vCode);

#pragma endregion(jniFunctionsArray)
#pragma region(jniFunctionsCompiler)

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_CompileClass(
	JNIEnv* env, 
	jclass compileClass, 
	jclass classy);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_CompileClasses(
	JNIEnv* env, 
	jclass classy, 
	jstring name);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_CompilerCommand(
	JNIEnv* env,
	jclass compileClass,
	jobject arg);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_DisableCompiler(
	JNIEnv* env, 
	jclass compileClass);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_EnableCompiler(
	JNIEnv* env, 
	jclass compileClass);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_InitializeCompiler(
	JNIEnv* env, 
	jclass compileClass);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_IsSilentCompiler(
	JNIEnv* env, 
	jclass compileClass);

#pragma endregion(jniFunctionsCompiler)
#pragma region(jniFunctionsCore)
	
/**
 * Returns the interface version of the JNI library.
 * 
 * @return The JNI library interface version.
 * @since 2025/10/20
 */
jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetInterfaceVersion(void);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetVersionInfo(
	JNIEnv* env,
	jvm_version_info* info,
	size_t infoSize);
	
#pragma endregion(jniFunctionsCore)
#pragma region(jniFunctionsDebug)

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_AssertionStatusDirectives(
	JNIEnv* env,
	jclass unused);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_DesiredAssertionStatus(
	JNIEnv* env,
	jclass unused,
	jclass classy);

jlong SJME_JNI_IMPORT_EXPORT JNICALL JVM_DTraceActivate(
	JNIEnv* env,
	jint version,
	jstring moduleName,
	jint providersCount,
	JVM_DTraceProvider* providers);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_DTraceDispose(
	JNIEnv* env, 
	jlong activationHandle);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_DTraceGetVersion(
	JNIEnv* env);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_DTraceIsProbeEnabled(
	JNIEnv* env, 
	jmethodID method);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_DTraceIsSupported(
	JNIEnv* env);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_FindSignal(
	const char* name);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_InitAgentProperties(
	JNIEnv* env,
	jobject agentProps);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_RaiseSignal(
	jint signalId);

void* SJME_JNI_IMPORT_EXPORT JNICALL JVM_RegisterSignal(
	jint signalId, 
	void* handler);

#pragma endregion(jniFunctionsDebug)
#pragma region(jniFunctionsFile) 
	
void SJME_JNI_IMPORT_EXPORT JNICALL JVM_OnExit(
	void (*func)(void));
	
#pragma endregion(jniFunctionsFile)
#pragma region(jniFunctionsFloat)
	
jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_IsNaN(
	jdouble d);
	
#pragma endregion(jniFunctionsFloat)
#pragma region(jniFunctionsGone)
	
void SJME_JNI_IMPORT_EXPORT JNICALL JVM_BeforeHalt(void);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_CopySwapMemory(
	jint unknownA,
	jint unknownB,
	jint unknownC,
	jint unknownD,
	jint unknownE,
	jint unknownF,
	jint unknownG,
	jint unknownH,
	jint unknownI,
	jint unknownJ,
	jint unknownK);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_CX8Field(
	jint unknownA,
	jint unknownB,
	jint unknownC,
	jint unknownD,
	jint unknownE,
	jint unknownF,
	jint unknownG);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_DefineClassWithSourceCond(
	jint unknownA,
	jint unknownB,
	jint unknownC,
	jint unknownD,
	jint unknownE,
	jint unknownF,
	jint unknownG,
	jint unknownH);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_EnqueueOperation(
	jint unknownA,
	jint unknownB,
	jint unknownC,
	jint unknownD,
	jint unknownE);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassLoader(
	jint unknownA,
	jint unknownB);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetFieldAnnotations(
	jint unknownA,
	jint unknownB);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetMethodAnnotations(
	jint unknownA,
	jint unknownB);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetMethodDefaultAnnotationValue(
	jint unknownA,
	jint unknownB);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetMethodParameterAnnotations(
	jint unknownA,
	jint unknownB);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetThreadInterruptEvent(void);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_IsUseContainerSupport(void);

#pragma endregion(jniFunctionsGone)
#pragma region(jniFunctionsIoNet)

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_Accept(
	jint fileNo,
	struct sockaddr* him,
	jint* len);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_Available(
	jint fileNo,
	jlong* numBytes);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_Bind(
	jint fileNo,
	struct sockaddr* addr,
	jint len);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_Close(
	jint fileNo);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_Connect(
	jint fileNo,
	struct sockaddr* addr, 
	jint len);

int SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetHostName(
	char* name, 
	int namelen);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetLastErrorString(
	char* buf, 
	int len);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetSockName(
	jint fileNo,
	struct sockaddr* addr,
	int* len);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetSockOpt(
	jint fileNo,
	int level,
	int optName,
	char* optVal,
	int* optLen);

jstring SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetTemporaryDirectory(
	JNIEnv* env);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_InitializeSocketLibrary(void);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_Listen(
	jint fileNo, 
	jint count);

jlong SJME_JNI_IMPORT_EXPORT JNICALL JVM_Lseek(
	jint fileNo,
	jlong offset, 
	jint whence);

char* SJME_JNI_IMPORT_EXPORT JNICALL JVM_NativePath(
	char* path);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_Open(
	const char* fname,
	jint flags,
	jint mode);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_Read(
	jint fileNo, 
	char* buf,
	jint len);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_Recv(
	jint fileNo,
	char* buf, 
	jint len, 
	jint flags);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_RecvFrom(
	jint fileNo,
	char* buf,
	int len,
	int flags,
	struct sockaddr* fromAddr,
	int* fromAddrLen);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_Send(
	jint fileNo,
	char* buf, 
	jint len,
	jint flags);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_SendTo(
	jint fileNo,
	char* buf,
	int len,
	int flags,
	struct sockaddr* toAddr,
	int toAddrLen);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_SetLength(jint fileNo, 
	jlong length);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_SetSockOpt(
	jint fileNo,
	int level,
	int optName,
	const char* optVal,
	int optLen);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_Socket(
	jint domain, 
	jint type, 
	jint protocol);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_SocketAvailable(
	jint fileNo, 
	jint* result);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_SocketClose(
	jint fileNo);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_SocketShutdown(
	jint fileNo, 
	jint howto);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_Sync(
	jint fileNo);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_Timeout(
	int fd,
	long timeout);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_Write(
	jint fileNo, 
	char* buf, 
	jint len);

#pragma endregion(jniFunctionsIoNet)
#pragma region(jniFunctionsObject)

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_Clone(
	JNIEnv* env, 
	jobject object);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_IHashCode(
	JNIEnv* env,
	jobject object);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_MonitorNotify(
	JNIEnv* env,
	jobject object);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_MonitorNotifyAll(
	JNIEnv* env, 
	jobject object);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_MonitorWait(
	JNIEnv* env,
	jobject object, 
	jlong millis);

void* SJME_JNI_IMPORT_EXPORT JNICALL JVM_RawMonitorCreate(void);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_RawMonitorDestroy(
	void* rawMonitor);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_RawMonitorEnter(
	void* rawMonitor);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_RawMonitorExit(
	void* rawMonitor);
	
#pragma endregion(jniFunctionsObject)
#pragma region(jniFunctionsObjectInput)

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_AllocateNewArray(
	JNIEnv* env,
	jobject object,
	jclass currClass,
	jint length);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_AllocateNewObject(
	JNIEnv* env,
	jobject object,
	jclass currClass,
	jclass initClass);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_LatestUserDefinedLoader(
	JNIEnv* env);

jclass SJME_JNI_IMPORT_EXPORT JNICALL JVM_LoadClass0(
	JNIEnv* env,
	jobject object,
	jclass currClass,
	jstring currClassName);
	
#pragma endregion(jniFunctionsObjectInput)
#pragma region(jniFunctionsOther)

void* SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetManagement(
	jint version);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_SupportsCX8(void);

#pragma endregion(jniFunctionsOther)
#pragma region(jniFunctionsPackage)

jstring SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetSystemPackage(
	JNIEnv* env, 
	jstring name);

jobjectArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetSystemPackages(
	JNIEnv* env);

#pragma endregion(jniFunctionsPackage)
#pragma region(jniFunctionsReflect)

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_ClassDepth(
	JNIEnv* env, 
	jstring name);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_ClassLoaderDepth(
	JNIEnv* env);

jclass SJME_JNI_IMPORT_EXPORT JNICALL JVM_ConstantPoolGetClassAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index);

jclass SJME_JNI_IMPORT_EXPORT JNICALL JVM_ConstantPoolGetClassAtIfLoaded(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index);

jdouble SJME_JNI_IMPORT_EXPORT JNICALL JVM_ConstantPoolGetDoubleAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_ConstantPoolGetFieldAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_ConstantPoolGetFieldAtIfLoaded(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index);

jfloat SJME_JNI_IMPORT_EXPORT JNICALL JVM_ConstantPoolGetFloatAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_ConstantPoolGetIntAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index);

jlong SJME_JNI_IMPORT_EXPORT JNICALL JVM_ConstantPoolGetLongAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index);

jobjectArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_ConstantPoolGetMemberRefInfoAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_ConstantPoolGetMethodAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_ConstantPoolGetMethodAtIfLoaded(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_ConstantPoolGetSize(
	JNIEnv* env,
	jobject unused,
	jobject classPool);

jstring SJME_JNI_IMPORT_EXPORT JNICALL JVM_ConstantPoolGetStringAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index);

jstring SJME_JNI_IMPORT_EXPORT JNICALL JVM_ConstantPoolGetUTF8At(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_CurrentClassLoader(
	JNIEnv* env);

jclass SJME_JNI_IMPORT_EXPORT JNICALL JVM_CurrentLoadedClass(
	JNIEnv* env);

jclass SJME_JNI_IMPORT_EXPORT JNICALL JVM_DefineClass(
	JNIEnv* env,
	const char* name,
	jobject classLoader,
	const jbyte* buf,
	jsize len,
	jobject pd);

jclass SJME_JNI_IMPORT_EXPORT JNICALL JVM_DefineClassWithSource(
	JNIEnv* env,
	const char* name,
	jobject classLoader,
	const jbyte* buf,
	jsize len,
	jobject pd,
	const char* source);

jclass SJME_JNI_IMPORT_EXPORT JNICALL JVM_FindClassFromBootLoader(
	JNIEnv* env,
	const char* name);

jclass SJME_JNI_IMPORT_EXPORT JNICALL JVM_FindClassFromCaller(
	JNIEnv* env,
	const char* name,
	jboolean init,
	jobject classLoader,
	jclass caller);

jclass SJME_JNI_IMPORT_EXPORT JNICALL JVM_FindClassFromClass(
	JNIEnv* env,
	const char* name,
	jboolean init,
	jclass from);

jclass SJME_JNI_IMPORT_EXPORT JNICALL JVM_FindClassFromClassLoader(
	JNIEnv* env,
	const char* name,
	jboolean doInit,
	jobject classLoader,
	jboolean throwError);

jclass SJME_JNI_IMPORT_EXPORT JNICALL JVM_FindLoadedClass(
	JNIEnv* env,
	jobject classLoader,
	jstring name);

jclass SJME_JNI_IMPORT_EXPORT JNICALL JVM_FindPrimitiveClass(
	JNIEnv* env, 
	const char* utf);

jclass SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetCallerClass(
	JNIEnv* env,
	int depth);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassAccessFlags(
	JNIEnv* env,
	jclass classy);

jbyteArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassAnnotations(
	JNIEnv* env, 
	jclass classy);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassConstantPool(
	JNIEnv* env,
	jclass classy);

jobjectArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassContext(
	JNIEnv* env);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassCPEntriesCount(
	JNIEnv* env, 
	jclass classy);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassCPTypes(
	JNIEnv* env, 
	jclass classy, 
	unsigned char* types);

jobjectArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassDeclaredConstructors(
	JNIEnv* env,
	jclass ofClass,
	jboolean publicOnly);

jobjectArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassDeclaredFields(
	JNIEnv* env,
	jclass ofClass,
	jboolean publicOnly);

jobjectArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassDeclaredMethods(
	JNIEnv* env,
	jclass ofClass,
	jboolean publicOnly);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassFieldsCount(
	JNIEnv* env, 
	jclass classy);

jobjectArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassInterfaces(
	JNIEnv* env, 
	jclass classy);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassMethodsCount(
	JNIEnv* env, 
	jclass classy);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassModifiers(
	JNIEnv* env,
	jclass classy);

jstring SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassName(
	JNIEnv* env, 
	jclass classy);

const char* SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassNameUTF(
	JNIEnv* env, 
	jclass classy);

jstring SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassSignature(
	JNIEnv* env,
	jclass classy);

jobjectArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassSigners(
	JNIEnv* env, 
	jclass classy);

jbyteArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetClassTypeAnnotations(
	JNIEnv* env, 
	jclass classy);

jclass SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetComponentType(
	JNIEnv* env, 
	jclass classy);

const char* SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetCPClassNameUTF(
	JNIEnv* env, 
	jclass classy, 
	jint index);

const char* SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetCPFieldClassNameUTF(
	JNIEnv* env,
	jclass classy,
	jint index);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetCPFieldModifiers(
	JNIEnv* env,
	jclass classy,
	int index,
	jclass calledClass);

const char* SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetCPFieldNameUTF(
	JNIEnv* env,
	jclass classy,
	jint index);

const char* SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetCPFieldSignatureUTF(
	JNIEnv* env,
	jclass classy,
	jint index);

const char* SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetCPMethodClassNameUTF(
	JNIEnv* env,
	jclass classy,
	jint index);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetCPMethodModifiers(
	JNIEnv* env,
	jclass classy,
	int index,
	jclass calledClass);

const char* SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetCPMethodNameUTF(
	JNIEnv* env, 
	jclass classy, 
	jint index);

const char* SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetCPMethodSignatureUTF(
	JNIEnv* env,
	jclass classy,
	jint index);

jobjectArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetDeclaredClasses(
	JNIEnv* env, 
	jclass ofClass);

jclass SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetDeclaringClass(
	JNIEnv* env, 
	jclass ofClass);

jobjectArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetEnclosingMethodInfo(
	JNIEnv* env, 
	jclass ofClass);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetFieldIxModifiers(
	JNIEnv* env, 
	jclass classy, 
	int index);

jbyteArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetFieldTypeAnnotations(
	JNIEnv* env, 
	jobject field);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetMethodIxArgsSize(
	JNIEnv* env,
	jclass classy,
	int index);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetMethodIxByteCode(
	JNIEnv* env,
	jclass classy,
	jint methodIndex,
	unsigned char* code);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetMethodIxByteCodeLength(
	JNIEnv* env,
	jclass classy,
	jint methodIndex);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetMethodIxExceptionIndexes(
	JNIEnv* env,
	jclass classy,
	jint methodIndex,
	unsigned short* exceptions);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetMethodIxExceptionsCount(
	JNIEnv* env,
	jclass classy,
	jint methodIndex);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetMethodIxExceptionTableEntry(
	JNIEnv* env,
	jclass classy,
	jint methodIndex,
	jint entry_index,
	JVM_ExceptionTableEntryType* entry);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetMethodIxExceptionTableLength(
	JNIEnv* env,
	jclass classy,
	int index);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetMethodIxLocalsCount(
	JNIEnv* env,
	jclass classy,
	int index);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetMethodIxMaxStack(
	JNIEnv* env,
	jclass classy, 
	int index);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetMethodIxModifiers(
	JNIEnv* env,
	jclass classy,
	int index);

const char* SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetMethodIxNameUTF(
	JNIEnv* env, 
	jclass classy,
	jint index);

const char* SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetMethodIxSignatureUTF(
	JNIEnv* env,
	jclass classy,
	jint index);

jobjectArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetMethodParameters(
	JNIEnv* env, 
	jobject method);

jbyteArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetMethodTypeAnnotations(
	JNIEnv* env, 
	jobject method);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetProtectionDomain(
	JNIEnv* env, 
	jclass classy);

jintArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetResourceLookupCache(
	JNIEnv* env,
	jobject classLoader,
	const char* resourceName);

jobjectArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetResourceLookupCacheURLs(
	JNIEnv* env,
	jobject classLoader);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_InvokeMethod(
	JNIEnv* env,
	jobject method,
	jobject object,
	jobjectArray args0);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_IsArrayClass(
	JNIEnv* env, 
	jclass classy);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_IsConstructorIx(
	JNIEnv* env,
	jclass classy, 
	int index);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_IsInterface(
	JNIEnv* env,
	jclass classy);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_IsPrimitiveClass(
	JNIEnv* env, 
	jclass classy);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_IsSameClassPackage(
	JNIEnv* env,
	jclass class1,
	jclass class2);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_IsVMGeneratedMethodIx(
	JNIEnv* env, 
	jclass classy, 
	int index);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_KnownToNotExist(
	JNIEnv* env,
	jobject classLoader,
	const char* classname);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_NewInstanceFromConstructor(
	JNIEnv* env,
	jobject c,
	jobjectArray args0);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_ReleaseUTF(
	const char* utf);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_ResolveClass(
	JNIEnv* env,
	jclass classy);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_SetClassSigners(
	JNIEnv* env, 
	jclass classy, 
	jobjectArray signers);

#pragma endregion(jniFunctionsReflect)
#pragma region(jniFunctionsRuntime)

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_ActiveProcessorCount(void);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_Exit(
	jint code);

void* SJME_JNI_IMPORT_EXPORT JNICALL JVM_FindLibraryEntry(
	void* handle, 
	const char* name);

jlong SJME_JNI_IMPORT_EXPORT JNICALL JVM_FreeMemory(void);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_GC(void);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_Halt(
	jint code);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_IsSupportedJNIVersion(
	jint version);

void* SJME_JNI_IMPORT_EXPORT JNICALL JVM_LoadLibrary(
	const char* name);

jlong SJME_JNI_IMPORT_EXPORT JNICALL JVM_MaxMemory(void);

jlong SJME_JNI_IMPORT_EXPORT JNICALL JVM_MaxObjectInspectionAge(void);

jlong SJME_JNI_IMPORT_EXPORT JNICALL JVM_TotalMemory(void);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_TraceInstructions(
	jboolean on);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_TraceMethodCalls(
	jboolean on);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_UnloadLibrary(
	void* handle);

#pragma endregion(jniFunctionsRuntime)
#pragma region(jniFunctionsSecurity)
	
jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_DoPrivileged(
	JNIEnv* env,
	jclass classy,
	jobject action,
	jobject context,
	jboolean wrapException);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetInheritedAccessControlContext(
	JNIEnv* env,
	jclass classy);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetStackAccessControlContext(
	JNIEnv* env,
	jclass classy);

#pragma endregion(jniFunctionsSecurity)
#pragma region(jniFunctionsString)
	
jstring SJME_JNI_IMPORT_EXPORT JNICALL JVM_InternString(
	JNIEnv* env,
	jstring str);
	
#pragma endregion(jniFunctionsString)
#pragma region(jniFunctionsSystem)

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_ArrayCopy(
	JNIEnv* env,
	jclass ignored,
	jobject src,
	jint srcOff,
	jobject dest,
	jint destOff,
	jint length);

jlong SJME_JNI_IMPORT_EXPORT JNICALL JVM_CurrentTimeMillis(
	JNIEnv* env,
	jclass ignored);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_InitProperties(
	JNIEnv* env,
	jobject p);

jlong SJME_JNI_IMPORT_EXPORT JNICALL JVM_NanoTime(
	JNIEnv* env,
	jclass ignored);

#pragma endregion(jniFunctionsSystem)
#pragma region(jniFunctionsThread)

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_CountStackFrames(
	JNIEnv* env,
	jobject thread);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_CurrentThread(
	JNIEnv* env,
	jclass threadClass);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_DumpAllStacks(
	JNIEnv* env,
	jclass unused);

jobjectArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_DumpThreads(
	JNIEnv* env,
	jclass threadClass,
	jobjectArray threads);

jobjectArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetAllThreads(
	JNIEnv* env,
	jclass dummy);

jobjectArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetThreadStateNames(
	JNIEnv* env,
	jint javaThreadState,
	jintArray values);

jintArray SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetThreadStateValues(
	JNIEnv* env,
	jint javaThreadState);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_HoldsLock(
	JNIEnv* env,
	jclass threadClass,
	jobject object);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_Interrupt(
	JNIEnv* env,
	jobject thread);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_IsInterrupted(
	JNIEnv* env,
	jobject thread,
	jboolean clearInterrupted);

jboolean SJME_JNI_IMPORT_EXPORT JNICALL JVM_IsThreadAlive(
	JNIEnv* env,
	jobject thread);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_ResumeThread(
	JNIEnv* env,
	jobject thread);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_SetNativeThreadName(
	JNIEnv* env,
	jobject thread,
	jstring name);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_SetThreadPriority(
	JNIEnv* env,
	jobject thread,
	jint priority);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_Sleep(
	JNIEnv* env,
	jclass threadClass,
	jlong millis);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_StartThread(
	JNIEnv* env,
	jobject thread);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_StopThread(
	JNIEnv* env,
	jobject thread,
	jobject exception);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_SuspendThread(
	JNIEnv* env,
	jobject thread);

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_Yield(
	JNIEnv* env,
	jclass threadClass);

#pragma endregion(jniFunctionsThread)
#pragma region(jniFunctionsThrowable)

void SJME_JNI_IMPORT_EXPORT JNICALL JVM_FillInStackTrace(
	JNIEnv* env,
	jobject throwable);

jint SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetStackTraceDepth(
	JNIEnv* env,
	jobject throwable);

jobject SJME_JNI_IMPORT_EXPORT JNICALL JVM_GetStackTraceElement(
	JNIEnv* env,
	jobject throwable,
	jint index);
	
#pragma endregion(jniFunctionsThrowable)

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_JVM_H
}
#undef SJME_CXX_SQUIRRELJME_JVM_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_JVM_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_JVM_H */
