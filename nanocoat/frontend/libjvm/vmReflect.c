/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libjvm/commonJniJvm.h"
#include "frontend/libjvm/internals.h"

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_ClassDepth(
	JNIEnv* env, 
	jstring name)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_ClassLoaderDepth(
	JNIEnv* env)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jclass SJME_JNI_LIB_FUNCTION JNICALL JVM_ConstantPoolGetClassAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jclass SJME_JNI_LIB_FUNCTION JNICALL JVM_ConstantPoolGetClassAtIfLoaded(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jdouble SJME_JNI_LIB_FUNCTION JNICALL JVM_ConstantPoolGetDoubleAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_JDOUBLE;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_ConstantPoolGetFieldAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_ConstantPoolGetFieldAtIfLoaded(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jfloat SJME_JNI_LIB_FUNCTION JNICALL JVM_ConstantPoolGetFloatAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_JFLOAT;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_ConstantPoolGetIntAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jlong SJME_JNI_LIB_FUNCTION JNICALL JVM_ConstantPoolGetLongAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_JLONG;
}

jobjectArray SJME_JNI_LIB_FUNCTION JNICALL JVM_ConstantPoolGetMemberRefInfoAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_ConstantPoolGetMethodAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_ConstantPoolGetMethodAtIfLoaded(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_ConstantPoolGetSize(
	JNIEnv* env,
	jobject unused,
	jobject classPool)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jstring SJME_JNI_LIB_FUNCTION JNICALL JVM_ConstantPoolGetStringAt(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jstring SJME_JNI_LIB_FUNCTION JNICALL JVM_ConstantPoolGetUTF8At(
	JNIEnv* env,
	jobject unused,
	jobject classPool,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_CurrentClassLoader(
	JNIEnv* env)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jclass SJME_JNI_LIB_FUNCTION JNICALL JVM_CurrentLoadedClass(
	JNIEnv* env)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jclass SJME_JNI_LIB_FUNCTION JNICALL JVM_DefineClass(
	JNIEnv* env,
	const char* name,
	jobject classLoader,
	const jbyte* buf,
	jsize len,
	jobject pd)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jclass SJME_JNI_LIB_FUNCTION JNICALL JVM_DefineClassWithSource(
	JNIEnv* env,
	const char* name,
	jobject classLoader,
	const jbyte* buf,
	jsize len,
	jobject pd,
	const char* source)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jclass SJME_JNI_LIB_FUNCTION JNICALL JVM_FindClassFromBootLoader(
	JNIEnv* env,
	const char* name)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jclass SJME_JNI_LIB_FUNCTION JNICALL JVM_FindClassFromCaller(
	JNIEnv* env,
	const char* name,
	jboolean init,
	jobject classLoader,
	jclass caller)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jclass SJME_JNI_LIB_FUNCTION JNICALL JVM_FindClassFromClass(
	JNIEnv* env,
	const char* name,
	jboolean init,
	jclass from)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jclass SJME_JNI_LIB_FUNCTION JNICALL JVM_FindClassFromClassLoader(
	JNIEnv* env,
	const char* name,
	jboolean doInit,
	jobject classLoader,
	jboolean throwError)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jclass SJME_JNI_LIB_FUNCTION JNICALL JVM_FindLoadedClass(
	JNIEnv* env,
	jobject classLoader,
	jstring name)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jclass SJME_JNI_LIB_FUNCTION JNICALL JVM_FindPrimitiveClass(
	JNIEnv* env, 
	const char* utf)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jclass SJME_JNI_LIB_FUNCTION JNICALL JVM_GetCallerClass(
	JNIEnv* env,
	int depth)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassAccessFlags(
	JNIEnv* env,
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jbyteArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassAnnotations(
	JNIEnv* env, 
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassConstantPool(
	JNIEnv* env,
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobjectArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassContext(
	JNIEnv* env)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassCPEntriesCount(
	JNIEnv* env, 
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassCPTypes(
	JNIEnv* env, 
	jclass classy, 
	unsigned char* types)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

jobjectArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassDeclaredConstructors(
	JNIEnv* env,
	jclass ofClass,
	jboolean publicOnly)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobjectArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassDeclaredFields(
	JNIEnv* env,
	jclass ofClass,
	jboolean publicOnly)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobjectArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassDeclaredMethods(
	JNIEnv* env,
	jclass ofClass,
	jboolean publicOnly)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassFieldsCount(
	JNIEnv* env, 
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jobjectArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassInterfaces(
	JNIEnv* env, 
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassMethodsCount(
	JNIEnv* env, 
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassModifiers(
	JNIEnv* env,
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jstring SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassName(
	JNIEnv* env, 
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

const char* SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassNameUTF(
	JNIEnv* env, 
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jstring SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassSignature(
	JNIEnv* env,
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobjectArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassSigners(
	JNIEnv* env, 
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jbyteArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassTypeAnnotations(
	JNIEnv* env, 
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jclass SJME_JNI_LIB_FUNCTION JNICALL JVM_GetComponentType(
	JNIEnv* env, 
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

const char* SJME_JNI_LIB_FUNCTION JNICALL JVM_GetCPClassNameUTF(
	JNIEnv* env, 
	jclass classy, 
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

const char* SJME_JNI_LIB_FUNCTION JNICALL JVM_GetCPFieldClassNameUTF(
	JNIEnv* env,
	jclass classy,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetCPFieldModifiers(
	JNIEnv* env,
	jclass classy,
	int index,
	jclass calledClass)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

const char* SJME_JNI_LIB_FUNCTION JNICALL JVM_GetCPFieldNameUTF(
	JNIEnv* env,
	jclass classy,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

const char* SJME_JNI_LIB_FUNCTION JNICALL JVM_GetCPFieldSignatureUTF(
	JNIEnv* env,
	jclass classy,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

const char* SJME_JNI_LIB_FUNCTION JNICALL JVM_GetCPMethodClassNameUTF(
	JNIEnv* env,
	jclass classy,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetCPMethodModifiers(
	JNIEnv* env,
	jclass classy,
	int index,
	jclass calledClass)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

const char* SJME_JNI_LIB_FUNCTION JNICALL JVM_GetCPMethodNameUTF(
	JNIEnv* env, 
	jclass classy, 
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

const char* SJME_JNI_LIB_FUNCTION JNICALL JVM_GetCPMethodSignatureUTF(
	JNIEnv* env,
	jclass classy,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobjectArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetDeclaredClasses(
	JNIEnv* env, 
	jclass ofClass)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jclass SJME_JNI_LIB_FUNCTION JNICALL JVM_GetDeclaringClass(
	JNIEnv* env, 
	jclass ofClass)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobjectArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetEnclosingMethodInfo(
	JNIEnv* env, 
	jclass ofClass)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetFieldIxModifiers(
	JNIEnv* env, 
	jclass classy, 
	int index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jbyteArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetFieldTypeAnnotations(
	JNIEnv* env, 
	jobject field)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetMethodIxArgsSize(
	JNIEnv* env,
	jclass classy,
	int index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_GetMethodIxByteCode(
	JNIEnv* env,
	jclass classy,
	jint methodIndex,
	unsigned char* code)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetMethodIxByteCodeLength(
	JNIEnv* env,
	jclass classy,
	jint methodIndex)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_GetMethodIxExceptionIndexes(
	JNIEnv* env,
	jclass classy,
	jint methodIndex,
	unsigned short* exceptions)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetMethodIxExceptionsCount(
	JNIEnv* env,
	jclass classy,
	jint methodIndex)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_GetMethodIxExceptionTableEntry(
	JNIEnv* env,
	jclass classy,
	jint methodIndex,
	jint entry_index,
	JVM_ExceptionTableEntryType* entry)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetMethodIxExceptionTableLength(
	JNIEnv* env,
	jclass classy,
	int index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetMethodIxLocalsCount(
	JNIEnv* env,
	jclass classy,
	int index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetMethodIxMaxStack(
	JNIEnv* env,
	jclass classy, 
	int index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetMethodIxModifiers(
	JNIEnv* env,
	jclass classy,
	int index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

const char* SJME_JNI_LIB_FUNCTION JNICALL JVM_GetMethodIxNameUTF(
	JNIEnv* env, 
	jclass classy,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

const char* SJME_JNI_LIB_FUNCTION JNICALL JVM_GetMethodIxSignatureUTF(
	JNIEnv* env,
	jclass classy,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobjectArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetMethodParameters(
	JNIEnv* env, 
	jobject method)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jbyteArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetMethodTypeAnnotations(
	JNIEnv* env, 
	jobject method)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_GetProtectionDomain(
	JNIEnv* env, 
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jintArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetResourceLookupCache(
	JNIEnv* env,
	jobject classLoader,
	const char* resourceName)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobjectArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetResourceLookupCacheURLs(
	JNIEnv* env,
	jobject classLoader)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_InvokeMethod(
	JNIEnv* env,
	jobject method,
	jobject object,
	jobjectArray args0)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_IsArrayClass(
	JNIEnv* env, 
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_IsConstructorIx(
	JNIEnv* env,
	jclass classy, 
	int index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_IsInterface(
	JNIEnv* env,
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_IsPrimitiveClass(
	JNIEnv* env, 
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_IsSameClassPackage(
	JNIEnv* env,
	jclass class1,
	jclass class2)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_IsVMGeneratedMethodIx(
	JNIEnv* env, 
	jclass classy, 
	int index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_KnownToNotExist(
	JNIEnv* env,
	jobject classLoader,
	const char* classname)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_NewInstanceFromConstructor(
	JNIEnv* env,
	jobject c,
	jobjectArray args0)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_ReleaseUTF(
	const char* utf)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_ResolveClass(
	JNIEnv* env,
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_SetClassSigners(
	JNIEnv* env, 
	jclass classy, 
	jobjectArray signers)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}
