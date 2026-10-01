/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libjvm/commonJniJvm.h"

jclass JNICALL JVM_GetCallerClass(JNIEnv* env, int depth)
{
	sjme_todo("Impl?");
	return NULL;
}

jclass JNICALL JVM_FindPrimitiveClass(JNIEnv* env, const char* utf)
{
	sjme_todo("Impl?");
	return NULL;
}

void JNICALL JVM_ResolveClass(JNIEnv* env, jclass cls)
{
	sjme_todo("Impl?");
}

jclass JNICALL JVM_FindClassFromBootLoader(JNIEnv* env, const char* name)
{
	sjme_todo("Impl?");
	return NULL;
}

jclass JNICALL JVM_FindClassFromCaller(JNIEnv* env,
	const char* name,
	jboolean init,
	jobject loader,
	jclass caller)
{
	sjme_todo("Impl?");
	return NULL;
}

jclass JNICALL JVM_FindClassFromClassLoader(JNIEnv* env,
	const char* name,
	jboolean init,
	jobject loader,
	jboolean throwError)
{
	sjme_todo("Impl?");
	return NULL;
}

jclass JNICALL JVM_FindClassFromClass(JNIEnv* env,
	const char* name,
	jboolean init,
	jclass from)
{
	sjme_todo("Impl?");
	return NULL;
}

jclass JNICALL JVM_FindLoadedClass(JNIEnv* env, jobject loader, jstring name)
{
	sjme_todo("Impl?");
	return NULL;
}

jclass JNICALL JVM_DefineClass(JNIEnv* env,
	const char* name,
	jobject loader,
	const jbyte* buf,
	jsize len,
	jobject pd)
{
	sjme_todo("Impl?");
	return NULL;
}

jclass JNICALL JVM_DefineClassWithSource(JNIEnv* env,
	const char* name,
	jobject loader,
	const jbyte* buf,
	jsize len,
	jobject pd,
	const char* source)
{
	sjme_todo("Impl?");
	return NULL;
}

jstring JNICALL JVM_GetClassName(JNIEnv* env, jclass cls)
{
	sjme_todo("Impl?");
	return NULL;
}

jobjectArray JNICALL JVM_GetClassInterfaces(JNIEnv* env, jclass cls)
{
	sjme_todo("Impl?");
	return NULL;
}

jboolean JNICALL JVM_IsInterface(JNIEnv* env, jclass cls)
{
	sjme_todo("Impl?");
	return JNI_FALSE;
}

jobjectArray JNICALL JVM_GetClassSigners(JNIEnv* env, jclass cls)
{
	sjme_todo("Impl?");
	return NULL;
}

void JNICALL JVM_SetClassSigners(JNIEnv* env, jclass cls, jobjectArray signers)
{
	sjme_todo("Impl?");
}

jobject JNICALL JVM_GetProtectionDomain(JNIEnv* env, jclass cls)
{
	sjme_todo("Impl?");
	return NULL;
}

jboolean JNICALL JVM_IsArrayClass(JNIEnv* env, jclass cls)
{
	sjme_todo("Impl?");
	return JNI_FALSE;
}

jboolean JNICALL JVM_IsPrimitiveClass(JNIEnv* env, jclass cls)
{
	sjme_todo("Impl?");
	return JNI_FALSE;
}

jclass JNICALL JVM_GetComponentType(JNIEnv* env, jclass cls)
{
	sjme_todo("Impl?");
	return NULL;
}

jint JNICALL JVM_GetClassModifiers(JNIEnv* env, jclass cls)
{
	sjme_todo("Impl?");
	return 0;
}

jobjectArray JNICALL JVM_GetDeclaredClasses(JNIEnv* env, jclass ofClass)
{
	sjme_todo("Impl?");
	return NULL;
}

jclass JNICALL JVM_GetDeclaringClass(JNIEnv* env, jclass ofClass)
{
	sjme_todo("Impl?");
	return NULL;
}

jstring JNICALL JVM_GetClassSignature(JNIEnv* env, jclass cls)
{
	sjme_todo("Impl?");
	return NULL;
}

jbyteArray JNICALL JVM_GetClassAnnotations(JNIEnv* env, jclass cls)
{
	sjme_todo("Impl?");
	return NULL;
}

jbyteArray JNICALL JVM_GetClassTypeAnnotations(JNIEnv* env, jclass cls)
{
	sjme_todo("Impl?");
	return NULL;
}

jbyteArray JNICALL JVM_GetFieldTypeAnnotations(JNIEnv* env, jobject field)
{
	sjme_todo("Impl?");
	return NULL;
}

jbyteArray JNICALL JVM_GetMethodTypeAnnotations(JNIEnv* env, jobject method)
{
	sjme_todo("Impl?");
	return NULL;
}

jobjectArray JNICALL JVM_GetClassDeclaredMethods(JNIEnv* env,
	jclass ofClass,
	jboolean publicOnly)
{
	sjme_todo("Impl?");
	return NULL;
}

jobjectArray JNICALL JVM_GetClassDeclaredFields(JNIEnv* env,
	jclass ofClass,
	jboolean publicOnly)
{
	sjme_todo("Impl?");
	return NULL;
}

jobjectArray JNICALL JVM_GetClassDeclaredConstructors(JNIEnv* env,
	jclass ofClass,
	jboolean publicOnly)
{
	sjme_todo("Impl?");
	return NULL;
}

jint JNICALL JVM_GetClassAccessFlags(JNIEnv* env, jclass cls)
{
	sjme_todo("Impl?");
	return 0;
}

jobject JNICALL JVM_InvokeMethod(JNIEnv* env,
	jobject method,
	jobject obj,
	jobjectArray args0)
{
	sjme_todo("Impl?");
	return NULL;
}

jobject JNICALL JVM_NewInstanceFromConstructor(JNIEnv* env,
	jobject c,
	jobjectArray args0)
{
	sjme_todo("Impl?");
	return NULL;
}

jobject JNICALL JVM_GetClassConstantPool(JNIEnv* env, jclass cls)
{
	sjme_todo("Impl?");
	return NULL;
}

jint JNICALL JVM_ConstantPoolGetSize(JNIEnv* env,
	jobject unused,
	jobject jcpool)
{
	sjme_todo("Impl?");
	return 0;
}

jclass JNICALL JVM_ConstantPoolGetClassAt(JNIEnv* env,
	jobject unused,
	jobject jcpool,
	jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

jclass JNICALL JVM_ConstantPoolGetClassAtIfLoaded(JNIEnv* env,
	jobject unused,
	jobject jcpool,
	jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

jobject JNICALL JVM_ConstantPoolGetMethodAt(JNIEnv* env,
	jobject unused,
	jobject jcpool,
	jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

jobject JNICALL JVM_ConstantPoolGetMethodAtIfLoaded(JNIEnv* env,
	jobject unused,
	jobject jcpool,
	jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

jobject JNICALL JVM_ConstantPoolGetFieldAt(JNIEnv* env,
	jobject unused,
	jobject jcpool,
	jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

jobject JNICALL JVM_ConstantPoolGetFieldAtIfLoaded(JNIEnv* env,
	jobject unused,
	jobject jcpool,
	jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

jobjectArray JNICALL JVM_ConstantPoolGetMemberRefInfoAt(JNIEnv* env,
	jobject unused,
	jobject jcpool,
	jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

jint JNICALL JVM_ConstantPoolGetIntAt(JNIEnv* env,
	jobject unused,
	jobject jcpool,
	jint index)
{
	sjme_todo("Impl?");
	return 0;
}

jlong JNICALL JVM_ConstantPoolGetLongAt(JNIEnv* env,
	jobject unused,
	jobject jcpool,
	jint index)
{
	sjme_todo("Impl?");
	return 0;
}

jfloat JNICALL JVM_ConstantPoolGetFloatAt(JNIEnv* env,
	jobject unused,
	jobject jcpool,
	jint index)
{
	sjme_todo("Impl?");
	return 0;
}

jdouble JNICALL JVM_ConstantPoolGetDoubleAt(JNIEnv* env,
	jobject unused,
	jobject jcpool,
	jint index)
{
	sjme_todo("Impl?");
	return 0;
}

jstring JNICALL JVM_ConstantPoolGetStringAt(JNIEnv* env,
	jobject unused,
	jobject jcpool,
	jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

jstring JNICALL JVM_ConstantPoolGetUTF8At(JNIEnv* env,
	jobject unused,
	jobject jcpool,
	jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

jobjectArray JNICALL JVM_GetMethodParameters(JNIEnv* env, jobject method)
{
	sjme_todo("Impl?");
	return NULL;
}

jclass JNICALL JVM_CurrentLoadedClass(JNIEnv* env)
{
	sjme_todo("Impl?");
	return NULL;
}

jobject JNICALL JVM_CurrentClassLoader(JNIEnv* env)
{
	sjme_todo("Impl?");
	return NULL;
}

jobjectArray JNICALL JVM_GetClassContext(JNIEnv* env)
{
	sjme_todo("Impl?");
	return NULL;
}

jint JNICALL JVM_ClassDepth(JNIEnv* env, jstring name)
{
	sjme_todo("Impl?");
	return 0;
}

jint JNICALL JVM_ClassLoaderDepth(JNIEnv* env)
{
	sjme_todo("Impl?");
	return 0;
}

const char* JNICALL JVM_GetClassNameUTF(JNIEnv* env, jclass cb)
{
	sjme_todo("Impl?");
	return NULL;
}

void JNICALL JVM_GetClassCPTypes(JNIEnv* env, jclass cb, unsigned char* types)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_GetClassCPEntriesCount(JNIEnv* env, jclass cb)
{
	sjme_todo("Impl?");
	return 0;
}

jint JNICALL JVM_GetClassFieldsCount(JNIEnv* env, jclass cb)
{
	sjme_todo("Impl?");
	return 0;
}

jint JNICALL JVM_GetClassMethodsCount(JNIEnv* env, jclass cb)
{
	sjme_todo("Impl?");
	return 0;
}

void JNICALL JVM_GetMethodIxExceptionIndexes(JNIEnv* env,
	jclass cb,
	jint method_index,
	unsigned short* exceptions)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_GetMethodIxExceptionsCount(JNIEnv* env,
	jclass cb,
	jint method_index)
{
	sjme_todo("Impl?");
	return 0;
}

void JNICALL JVM_GetMethodIxByteCode(JNIEnv* env,
	jclass cb,
	jint method_index,
	unsigned char* code)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_GetMethodIxByteCodeLength(JNIEnv* env,
	jclass cb,
	jint method_index)
{
	sjme_todo("Impl?");
	return 0;
}

void JNICALL JVM_GetMethodIxExceptionTableEntry(JNIEnv* env,
	jclass cb,
	jint method_index,
	jint entry_index,
	JVM_ExceptionTableEntryType* entry)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_GetMethodIxExceptionTableLength(JNIEnv* env,
	jclass cb,
	int index)
{
	sjme_todo("Impl?");
	return 0;
}

jint JNICALL JVM_GetFieldIxModifiers(JNIEnv* env, jclass cb, int index)
{
	sjme_todo("Impl?");
	return 0;
}

jint JNICALL JVM_GetMethodIxModifiers(JNIEnv* env, jclass cb, int index)
{
	sjme_todo("Impl?");
	return 0;
}

jint JNICALL JVM_GetMethodIxLocalsCount(JNIEnv* env, jclass cb, int index)
{
	sjme_todo("Impl?");
	return 0;
}

jint JNICALL JVM_GetMethodIxArgsSize(JNIEnv* env, jclass cb, int index)
{
	sjme_todo("Impl?");
	return 0;
}

jint JNICALL JVM_GetMethodIxMaxStack(JNIEnv* env, jclass cb, int index)
{
	sjme_todo("Impl?");
	return 0;
}

jboolean JNICALL JVM_IsConstructorIx(JNIEnv* env, jclass cb, int index)
{
	sjme_todo("Impl?");
	return JNI_FALSE;
}

jboolean JNICALL JVM_IsVMGeneratedMethodIx(JNIEnv* env, jclass cb, int index)
{
	sjme_todo("Impl?");
	return JNI_FALSE;
}

const char* JNICALL JVM_GetMethodIxNameUTF(JNIEnv* env, jclass cb, jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

const char* JNICALL JVM_GetMethodIxSignatureUTF(JNIEnv* env,
	jclass cb,
	jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

const char* JNICALL JVM_GetCPFieldNameUTF(JNIEnv* env, jclass cb, jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

const char* JNICALL JVM_GetCPMethodNameUTF(JNIEnv* env, jclass cb, jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

const char* JNICALL JVM_GetCPMethodSignatureUTF(JNIEnv* env,
	jclass cb,
	jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

const char* JNICALL JVM_GetCPFieldSignatureUTF(JNIEnv* env,
	jclass cb,
	jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

const char* JNICALL JVM_GetCPClassNameUTF(JNIEnv* env, jclass cb, jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

const char* JNICALL JVM_GetCPFieldClassNameUTF(JNIEnv* env,
	jclass cb,
	jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

const char* JNICALL JVM_GetCPMethodClassNameUTF(JNIEnv* env,
	jclass cb,
	jint index)
{
	sjme_todo("Impl?");
	return NULL;
}

jint JNICALL JVM_GetCPFieldModifiers(JNIEnv* env,
	jclass cb,
	int index,
	jclass calledClass)
{
	sjme_todo("Impl?");
	return 0;
}

jint JNICALL JVM_GetCPMethodModifiers(JNIEnv* env,
	jclass cb,
	int index,
	jclass calledClass)
{
	sjme_todo("Impl?");
	return 0;
}

void JNICALL JVM_ReleaseUTF(const char* utf)
{
	sjme_todo("Impl?");
}

jboolean JNICALL JVM_IsSameClassPackage(JNIEnv* env,
	jclass class1,
	jclass class2)
{
	sjme_todo("Impl?");
	return JNI_FALSE;
}

jobjectArray JNICALL JVM_GetEnclosingMethodInfo(JNIEnv* env, jclass ofClass)
{
	sjme_todo("Impl?");
	return NULL;
}

jboolean JNICALL JVM_KnownToNotExist(JNIEnv* env,
	jobject loader,
	const char* classname)
{
	sjme_todo("Impl?");
	return JNI_FALSE;
}

jobjectArray JNICALL JVM_GetResourceLookupCacheURLs(JNIEnv* env,
	jobject loader)
{
	sjme_todo("Impl?");
	return NULL;
}

jintArray JNICALL JVM_GetResourceLookupCache(JNIEnv* env,
	jobject loader,
	const char* resource_name)
{
	sjme_todo("Impl?");
	return NULL;
}
