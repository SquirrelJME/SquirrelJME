/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// --------------------------------------------------------------------------*/

/**
 * This contians the definitions for the Java native interface.
 *
 * @since 2016/10/19
 */

/** Header guard. */
#ifndef SJME_hGJINH
#define SJME_hGJINH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXJINH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

#include "wintercoat.h"

const char* JNICALL WC_JNI_GetStringUTFChars(JNIEnv* env, jstring str, 
	jboolean* isCopy);

const jchar* JNICALL WC_JNI_GetStringChars(JNIEnv* env, jstring str, 
	jboolean* isCopy);

const jchar*  JNICALL WC_JNI_GetStringCritical(JNIEnv* env, jstring 
	string, jboolean* isCopy);

jbooleanArray JNICALL WC_JNI_NewBooleanArray(JNIEnv* env, jsize len);

jboolean JNICALL WC_JNI_CallBooleanMethodA(JNIEnv* env, jobject obj, 
	jmethodID methodID, const jvalue*  args);

jboolean JNICALL WC_JNI_CallBooleanMethod(JNIEnv* env, jobject obj, 
	jmethodID methodID, ...);

jboolean JNICALL WC_JNI_CallBooleanMethodV(JNIEnv* env, jobject obj, 
	jmethodID methodID, va_list args);

jboolean JNICALL WC_JNI_CallNonvirtualBooleanMethodA(JNIEnv* env, 
	jobject obj, jclass clazz, jmethodID methodID, const jvalue*  args);

jboolean JNICALL WC_JNI_CallNonvirtualBooleanMethod(JNIEnv* env, 
	jobject obj, jclass clazz, jmethodID methodID, ...);

jboolean JNICALL WC_JNI_CallNonvirtualBooleanMethodV(JNIEnv* env, 
	jobject obj, jclass clazz, jmethodID methodID, va_list args);

jboolean JNICALL WC_JNI_CallStaticBooleanMethodA(JNIEnv* env, jclass 
	clazz, jmethodID methodID, const jvalue* args);

jboolean JNICALL WC_JNI_CallStaticBooleanMethod(JNIEnv* env, jclass 
	clazz, jmethodID methodID, ...);

jboolean JNICALL WC_JNI_CallStaticBooleanMethodV(JNIEnv* env, jclass 
	clazz, jmethodID methodID, va_list args);

jboolean JNICALL WC_JNI_ExceptionCheck(JNIEnv* env);

jboolean*  JNICALL WC_JNI_GetBooleanArrayElements(JNIEnv* env, 
	jbooleanArray array, jboolean* isCopy);

jboolean JNICALL WC_JNI_GetBooleanField(JNIEnv* env, jobject obj, 
	jfieldID fieldID);

jboolean JNICALL WC_JNI_GetStaticBooleanField(JNIEnv* env, jclass 
	clazz, jfieldID fieldID);

jboolean JNICALL WC_JNI_IsAssignableFrom(JNIEnv* env, jclass sub, 
	jclass sup);

jboolean JNICALL WC_JNI_IsInstanceOf(JNIEnv* env, jobject obj, jclass 
	clazz);

jboolean JNICALL WC_JNI_IsSameObject(JNIEnv* env, jobject obj1, 
	jobject obj2);

jbyteArray JNICALL WC_JNI_NewByteArray(JNIEnv* env, jsize len);

jbyte JNICALL WC_JNI_CallByteMethodA(JNIEnv* env, jobject obj, 
	jmethodID methodID, const jvalue* args);

jbyte JNICALL WC_JNI_CallByteMethod(JNIEnv* env, jobject obj, 
	jmethodID methodID, ...);

jbyte JNICALL WC_JNI_CallByteMethodV(JNIEnv* env, jobject obj, 
	jmethodID methodID, va_list args);

jbyte JNICALL WC_JNI_CallNonvirtualByteMethodA(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, const jvalue* args);

jbyte JNICALL WC_JNI_CallNonvirtualByteMethod(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, ...);

jbyte JNICALL WC_JNI_CallNonvirtualByteMethodV(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, va_list args);

jbyte JNICALL WC_JNI_CallStaticByteMethodA(JNIEnv* env, jclass clazz, 
	jmethodID methodID, const jvalue* args);

jbyte JNICALL WC_JNI_CallStaticByteMethod(JNIEnv* env, jclass clazz, 
	jmethodID methodID, ...);

jbyte JNICALL WC_JNI_CallStaticByteMethodV(JNIEnv* env, jclass clazz, 
	jmethodID methodID, va_list args);

jbyte*  JNICALL WC_JNI_GetByteArrayElements(JNIEnv* env, jbyteArray 
	array, jboolean* isCopy);

jbyte JNICALL WC_JNI_GetByteField(JNIEnv* env, jobject obj, jfieldID 
	fieldID);

jbyte JNICALL WC_JNI_GetStaticByteField(JNIEnv* env, jclass clazz, 
	jfieldID fieldID);

jcharArray JNICALL WC_JNI_NewCharArray(JNIEnv* env, jsize len);

jchar JNICALL WC_JNI_CallCharMethodA(JNIEnv* env, jobject obj, 
	jmethodID methodID, const jvalue* args);

jchar JNICALL WC_JNI_CallCharMethod(JNIEnv* env, jobject obj, 
	jmethodID methodID, ...);

jchar JNICALL WC_JNI_CallCharMethodV(JNIEnv* env, jobject obj, 
	jmethodID methodID, va_list args);

jchar JNICALL WC_JNI_CallNonvirtualCharMethodA(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, const jvalue* args);

jchar JNICALL WC_JNI_CallNonvirtualCharMethod(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, ...);

jchar JNICALL WC_JNI_CallNonvirtualCharMethodV(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, va_list args);

jchar JNICALL WC_JNI_CallStaticCharMethodA(JNIEnv* env, jclass clazz, 
	jmethodID methodID, const jvalue* args);

jchar JNICALL WC_JNI_CallStaticCharMethod(JNIEnv* env, jclass clazz, 
	jmethodID methodID, ...);

jchar JNICALL WC_JNI_CallStaticCharMethodV(JNIEnv* env, jclass clazz, 
	jmethodID methodID, va_list args);

jchar*  JNICALL WC_JNI_GetCharArrayElements(JNIEnv* env, jcharArray 
	array, jboolean* isCopy);

jchar JNICALL WC_JNI_GetCharField(JNIEnv* env, jobject obj, jfieldID 
	fieldID);

jchar JNICALL WC_JNI_GetStaticCharField(JNIEnv* env, jclass clazz, 
	jfieldID fieldID);

jclass JNICALL WC_JNI_DefineClass(JNIEnv* env, const char* name, 
	jobject loader, const jbyte* buf, jsize len);

jclass JNICALL WC_JNI_FindClass(JNIEnv* env, const char* name);

jclass JNICALL WC_JNI_GetObjectClass(JNIEnv* env, jobject obj);

jclass JNICALL WC_JNI_GetSuperclass(JNIEnv* env, jclass sub);

jdoubleArray JNICALL WC_JNI_NewDoubleArray(JNIEnv* env, jsize len);

jdouble JNICALL WC_JNI_CallDoubleMethodA(JNIEnv* env, jobject obj, 
	jmethodID methodID, const jvalue* args);

jdouble JNICALL WC_JNI_CallDoubleMethod(JNIEnv* env, jobject obj, 
	jmethodID methodID, ...);

jdouble JNICALL WC_JNI_CallDoubleMethodV(JNIEnv* env, jobject obj, 
	jmethodID methodID, va_list args);

jdouble JNICALL WC_JNI_CallNonvirtualDoubleMethodA(JNIEnv* env, 
	jobject obj, jclass clazz, jmethodID methodID, const jvalue* args);

jdouble JNICALL WC_JNI_CallNonvirtualDoubleMethod(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, ...);

jdouble JNICALL WC_JNI_CallNonvirtualDoubleMethodV(JNIEnv* env, 
	jobject obj, jclass clazz, jmethodID methodID, va_list args);

jdouble JNICALL WC_JNI_CallStaticDoubleMethodA(JNIEnv* env, jclass 
	clazz, jmethodID methodID, const jvalue* args);

jdouble JNICALL WC_JNI_CallStaticDoubleMethod(JNIEnv* env, jclass 
	clazz, jmethodID methodID, ...);

jdouble JNICALL WC_JNI_CallStaticDoubleMethodV(JNIEnv* env, jclass 
	clazz, jmethodID methodID, va_list args);

jdouble*  JNICALL WC_JNI_GetDoubleArrayElements(JNIEnv* env, 
	jdoubleArray array, jboolean* isCopy);

jdouble JNICALL WC_JNI_GetDoubleField(JNIEnv* env, jobject obj, 
	jfieldID fieldID);

jdouble JNICALL WC_JNI_GetStaticDoubleField(JNIEnv* env, jclass clazz, 
	jfieldID fieldID);

jfieldID JNICALL WC_JNI_FromReflectedField(JNIEnv* env, jobject field);

jfieldID JNICALL WC_JNI_GetFieldID(JNIEnv* env, jclass clazz, const 
	char* name, const char* sig);

jfieldID JNICALL WC_JNI_GetStaticFieldID(JNIEnv* env, jclass clazz, 
	const char* name, const char* sig);

jfloatArray JNICALL WC_JNI_NewFloatArray(JNIEnv* env, jsize len);

jfloat JNICALL WC_JNI_CallFloatMethodA(JNIEnv* env, jobject obj, 
	jmethodID methodID, const jvalue* args);

jfloat JNICALL WC_JNI_CallFloatMethod(JNIEnv* env, jobject obj, 
	jmethodID methodID, ...);

jfloat JNICALL WC_JNI_CallFloatMethodV(JNIEnv* env, jobject obj, 
	jmethodID methodID, va_list args);

jfloat JNICALL WC_JNI_CallNonvirtualFloatMethodA(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, const jvalue* args);

jfloat JNICALL WC_JNI_CallNonvirtualFloatMethod(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, ...);

jfloat JNICALL WC_JNI_CallNonvirtualFloatMethodV(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, va_list args);

jfloat JNICALL WC_JNI_CallStaticFloatMethodA(JNIEnv* env, jclass 
	clazz, jmethodID methodID, const jvalue* args);

jfloat JNICALL WC_JNI_CallStaticFloatMethod(JNIEnv* env, jclass clazz, 
	jmethodID methodID, ...);

jfloat JNICALL WC_JNI_CallStaticFloatMethodV(JNIEnv* env, jclass 
	clazz, jmethodID methodID, va_list args);

jfloat*  JNICALL WC_JNI_GetFloatArrayElements(JNIEnv* env, jfloatArray 
	array, jboolean* isCopy);

jfloat JNICALL WC_JNI_GetFloatField(JNIEnv* env, jobject obj, jfieldID 
	fieldID);

jfloat JNICALL WC_JNI_GetStaticFloatField(JNIEnv* env, jclass clazz, 
	jfieldID fieldID);

jintArray JNICALL WC_JNI_NewIntArray(JNIEnv* env, jsize len);

jint JNICALL WC_JNI_CallIntMethodA(JNIEnv* env, jobject obj, jmethodID 
	methodID, const jvalue* args);

jint JNICALL WC_JNI_CallIntMethod(JNIEnv* env, jobject obj, jmethodID 
	methodID, ...);

jint JNICALL WC_JNI_CallIntMethodV(JNIEnv* env, jobject obj, jmethodID 
	methodID, va_list args);

jint JNICALL WC_JNI_CallNonvirtualIntMethodA(JNIEnv* env, jobject obj, 
	jclass clazz, jmethodID methodID, const jvalue* args);

jint JNICALL WC_JNI_CallNonvirtualIntMethod(JNIEnv* env, jobject obj, 
	jclass clazz, jmethodID methodID, ...);

jint JNICALL WC_JNI_CallNonvirtualIntMethodV(JNIEnv* env, jobject obj, 
	jclass clazz, jmethodID methodID, va_list args);

jint JNICALL WC_JNI_CallStaticIntMethodA(JNIEnv* env, jclass clazz, 
	jmethodID methodID, const jvalue* args);

jint JNICALL WC_JNI_CallStaticIntMethod(JNIEnv* env, jclass clazz, 
	jmethodID methodID, ...);

jint JNICALL WC_JNI_CallStaticIntMethodV(JNIEnv* env, jclass clazz, 
	jmethodID methodID, va_list args);

jint JNICALL WC_JNI_EnsureLocalCapacity(JNIEnv* env, jint capacity);

jint*  JNICALL WC_JNI_GetIntArrayElements(JNIEnv* env, jintArray 
	array, jboolean* isCopy);

jint JNICALL WC_JNI_GetIntField(JNIEnv* env, jobject obj, jfieldID 
	fieldID);

jint JNICALL WC_JNI_GetJavaVM(JNIEnv* env, JavaVM** vm);

jint JNICALL WC_JNI_GetStaticIntField(JNIEnv* env, jclass clazz, 
	jfieldID fieldID);

jint JNICALL WC_JNI_GetVersion(JNIEnv* env);

jint JNICALL WC_JNI_MonitorEnter(JNIEnv* env, jobject obj);

jint JNICALL WC_JNI_MonitorExit(JNIEnv* env, jobject obj);

jint JNICALL WC_JNI_PushLocalFrame(JNIEnv* env, jint capacity);

jint JNICALL WC_JNI_RegisterNatives(JNIEnv* env, jclass clazz, const 
	JNINativeMethod* methods, jint nMethods);

jint JNICALL WC_JNI_Throw(JNIEnv* env, jthrowable obj);

jint JNICALL WC_JNI_ThrowNew(JNIEnv* env, jclass clazz, const char* 
	msg);

jint JNICALL WC_JNI_UnregisterNatives(JNIEnv* env, jclass clazz);

jlongArray JNICALL WC_JNI_NewLongArray(JNIEnv* env, jsize len);

jlong JNICALL WC_JNI_CallLongMethodA(JNIEnv* env, jobject obj, 
	jmethodID methodID, const jvalue* args);

jlong JNICALL WC_JNI_CallLongMethod(JNIEnv* env, jobject obj, 
	jmethodID methodID, ...);

jlong JNICALL WC_JNI_CallLongMethodV(JNIEnv* env, jobject obj, 
	jmethodID methodID, va_list args);

jlong JNICALL WC_JNI_CallNonvirtualLongMethodA(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, const jvalue* args);

jlong JNICALL WC_JNI_CallNonvirtualLongMethod(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, ...);

jlong JNICALL WC_JNI_CallNonvirtualLongMethodV(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, va_list args);

jlong JNICALL WC_JNI_CallStaticLongMethodA(JNIEnv* env, jclass clazz, 
	jmethodID methodID, const jvalue* args);

jlong JNICALL WC_JNI_CallStaticLongMethod(JNIEnv* env, jclass clazz, 
	jmethodID methodID, ...);

jlong JNICALL WC_JNI_CallStaticLongMethodV(JNIEnv* env, jclass clazz, 
	jmethodID methodID, va_list args);

jlong JNICALL WC_JNI_GetDirectBufferCapacity(JNIEnv* env, jobject buf);

jlong*  JNICALL WC_JNI_GetLongArrayElements(JNIEnv* env, jlongArray 
	array, jboolean* isCopy);

jlong JNICALL WC_JNI_GetLongField(JNIEnv* env, jobject obj, jfieldID 
	fieldID);

jlong JNICALL WC_JNI_GetStaticLongField(JNIEnv* env, jclass clazz, 
	jfieldID fieldID);

jmethodID JNICALL WC_JNI_FromReflectedMethod(JNIEnv* env, jobject 
	method);

jmethodID JNICALL WC_JNI_GetMethodID(JNIEnv* env, jclass clazz, const 
	char* name, const char* sig);

jmethodID JNICALL WC_JNI_GetStaticMethodID(JNIEnv* env, jclass clazz, 
	const char* name, const char* sig);

jobjectArray JNICALL WC_JNI_NewObjectArray(JNIEnv* env, jsize len, 
	jclass clazz, jobject init);

jobject JNICALL WC_JNI_AllocObject(JNIEnv* env, jclass clazz);

jobject JNICALL WC_JNI_CallNonvirtualObjectMethodA(JNIEnv* env, 
	jobject obj, jclass clazz, jmethodID methodID, const jvalue*  args);

jobject JNICALL WC_JNI_CallNonvirtualObjectMethod(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, ...);

jobject JNICALL WC_JNI_CallNonvirtualObjectMethodV(JNIEnv* env, 
	jobject obj, jclass clazz, jmethodID methodID, va_list args);

jobject JNICALL WC_JNI_CallObjectMethodA(JNIEnv* env, jobject obj, 
	jmethodID methodID, const jvalue*  args);

jobject JNICALL WC_JNI_CallObjectMethod(JNIEnv* env, jobject obj, 
	jmethodID methodID, ...);

jobject JNICALL WC_JNI_CallObjectMethodV(JNIEnv* env, jobject obj, 
	jmethodID methodID, va_list args);

jobject JNICALL WC_JNI_CallStaticObjectMethodA(JNIEnv* env, jclass 
	clazz, jmethodID methodID, const jvalue* args);

jobject JNICALL WC_JNI_CallStaticObjectMethod(JNIEnv* env, jclass 
	clazz, jmethodID methodID, ...);

jobject JNICALL WC_JNI_CallStaticObjectMethodV(JNIEnv* env, jclass 
	clazz, jmethodID methodID, va_list args);

jobject JNICALL WC_JNI_GetObjectArrayElement(JNIEnv* env, jobjectArray 
	array, jsize index);

jobject JNICALL WC_JNI_GetObjectField(JNIEnv* env, jobject obj, 
	jfieldID fieldID);

jobject JNICALL WC_JNI_GetStaticObjectField(JNIEnv* env, jclass clazz, 
	jfieldID fieldID);

jobject JNICALL WC_JNI_NewDirectByteBuffer(JNIEnv* env, void* address, 
	jlong capacity);

jobject JNICALL WC_JNI_NewGlobalRef(JNIEnv* env, jobject lobj);

jobject JNICALL WC_JNI_NewLocalRef(JNIEnv* env, jobject ref);

jobject JNICALL WC_JNI_NewObjectA(JNIEnv* env, jclass clazz, jmethodID 
	methodID, const jvalue* args);

jobject JNICALL WC_JNI_NewObject(JNIEnv* env, jclass clazz, jmethodID 
	methodID, ...);

jobject JNICALL WC_JNI_NewObjectV(JNIEnv* env, jclass clazz, jmethodID 
	methodID, va_list args);

jobject JNICALL WC_JNI_PopLocalFrame(JNIEnv* env, jobject result);

jobject JNICALL WC_JNI_ToReflectedField(JNIEnv* env, jclass cls, 
	jfieldID fieldID, jboolean isStatic);

jobject JNICALL WC_JNI_ToReflectedMethod(JNIEnv* env, jclass cls, 
	jmethodID methodID, jboolean isStatic);

jobjectRefType JNICALL WC_JNI_GetObjectRefType(JNIEnv* env, jobject 
	obj);

jshortArray JNICALL WC_JNI_NewShortArray(JNIEnv* env, jsize len);

jshort JNICALL WC_JNI_CallNonvirtualShortMethodA(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, const jvalue* args);

jshort JNICALL WC_JNI_CallNonvirtualShortMethod(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, ...);

jshort JNICALL WC_JNI_CallNonvirtualShortMethodV(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, va_list args);

jshort JNICALL WC_JNI_CallShortMethodA(JNIEnv* env, jobject obj, 
	jmethodID methodID, const jvalue* args);

jshort JNICALL WC_JNI_CallShortMethod(JNIEnv* env, jobject obj, 
	jmethodID methodID, ...);

jshort JNICALL WC_JNI_CallShortMethodV(JNIEnv* env, jobject obj, 
	jmethodID methodID, va_list args);

jshort JNICALL WC_JNI_CallStaticShortMethodA(JNIEnv* env, jclass 
	clazz, jmethodID methodID, const jvalue* args);

jshort JNICALL WC_JNI_CallStaticShortMethod(JNIEnv* env, jclass clazz, 
	jmethodID methodID, ...);

jshort JNICALL WC_JNI_CallStaticShortMethodV(JNIEnv* env, jclass 
	clazz, jmethodID methodID, va_list args);

jshort*  JNICALL WC_JNI_GetShortArrayElements(JNIEnv* env, jshortArray 
	array, jboolean* isCopy);

jshort JNICALL WC_JNI_GetShortField(JNIEnv* env, jobject obj, jfieldID 
	fieldID);

jshort JNICALL WC_JNI_GetStaticShortField(JNIEnv* env, jclass clazz, 
	jfieldID fieldID);

jsize JNICALL WC_JNI_GetArrayLength(JNIEnv* env, jarray array);

jsize JNICALL WC_JNI_GetStringLength(JNIEnv* env, jstring str);

jsize JNICALL WC_JNI_GetStringUTFLength(JNIEnv* env, jstring str);

jstring JNICALL WC_JNI_NewString(JNIEnv* env, const jchar* unicode, 
	jsize len);

jstring JNICALL WC_JNI_NewStringUTF(JNIEnv* env, const char* utf);

jthrowable JNICALL WC_JNI_ExceptionOccurred(JNIEnv* env);

jweak JNICALL WC_JNI_NewWeakGlobalRef(JNIEnv* env, jobject obj);

void JNICALL WC_JNI_CallNonvirtualVoidMethodA(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, const jvalue*  args);

void JNICALL WC_JNI_CallNonvirtualVoidMethod(JNIEnv* env, jobject obj, 
	jclass clazz, jmethodID methodID, ...);

void JNICALL WC_JNI_CallNonvirtualVoidMethodV(JNIEnv* env, jobject 
	obj, jclass clazz, jmethodID methodID, va_list args);

void JNICALL WC_JNI_CallStaticVoidMethodA(JNIEnv* env, jclass cls, 
	jmethodID methodID, const jvalue*  args);

void JNICALL WC_JNI_CallStaticVoidMethod(JNIEnv* env, jclass cls, 
	jmethodID methodID, ...);

void JNICALL WC_JNI_CallStaticVoidMethodV(JNIEnv* env, jclass cls, 
	jmethodID methodID, va_list args);

void JNICALL WC_JNI_CallVoidMethodA(JNIEnv* env, jobject obj, 
	jmethodID methodID, const jvalue*  args);

void JNICALL WC_JNI_CallVoidMethod(JNIEnv* env, jobject obj, jmethodID 
	methodID, ...);

void JNICALL WC_JNI_CallVoidMethodV(JNIEnv* env, jobject obj, 
	jmethodID methodID, va_list args);

void JNICALL WC_JNI_DeleteGlobalRef(JNIEnv* env, jobject gref);

void JNICALL WC_JNI_DeleteLocalRef(JNIEnv* env, jobject obj);

void JNICALL WC_JNI_DeleteWeakGlobalRef(JNIEnv* env, jweak ref);

void JNICALL WC_JNI_ExceptionClear(JNIEnv* env);

void JNICALL WC_JNI_ExceptionDescribe(JNIEnv* env);

void JNICALL WC_JNI_FatalError(JNIEnv* env, const char* msg);

void JNICALL WC_JNI_GetBooleanArrayRegion(JNIEnv* env, jbooleanArray 
	array, jsize start, jsize l, jboolean* buf);

void JNICALL WC_JNI_GetByteArrayRegion(JNIEnv* env, jbyteArray array, 
	jsize start, jsize len, jbyte* buf);

void JNICALL WC_JNI_GetCharArrayRegion(JNIEnv* env, jcharArray array, 
	jsize start, jsize len, jchar* buf);

void* JNICALL WC_JNI_GetDirectBufferAddress(JNIEnv* env, jobject buf);

void JNICALL WC_JNI_GetDoubleArrayRegion(JNIEnv* env, jdoubleArray 
	array, jsize start, jsize len, jdouble* buf);

void JNICALL WC_JNI_GetFloatArrayRegion(JNIEnv* env, jfloatArray 
	array, jsize start, jsize len, jfloat* buf);

void JNICALL WC_JNI_GetIntArrayRegion(JNIEnv* env, jintArray array, 
	jsize start, jsize len, jint* buf);

void JNICALL WC_JNI_GetLongArrayRegion(JNIEnv* env, jlongArray array, 
	jsize start, jsize len, jlong* buf);

void* JNICALL WC_JNI_GetPrimitiveArrayCritical(JNIEnv* env, jarray 
	array, jboolean* isCopy);

void JNICALL WC_JNI_GetShortArrayRegion(JNIEnv* env, jshortArray 
	array, jsize start, jsize len, jshort* buf);

void JNICALL WC_JNI_GetStringRegion(JNIEnv* env, jstring str, jsize 
	start, jsize len, jchar* buf);

void JNICALL WC_JNI_GetStringUTFRegion(JNIEnv* env, jstring str, jsize 
	start, jsize len, char* buf);

void JNICALL WC_JNI_ReleaseBooleanArrayElements(JNIEnv* env, 
	jbooleanArray array, jboolean* elems, jint mode);

void JNICALL WC_JNI_ReleaseByteArrayElements(JNIEnv* env, jbyteArray 
	array, jbyte* elems, jint mode);

void JNICALL WC_JNI_ReleaseCharArrayElements(JNIEnv* env, jcharArray 
	array, jchar* elems, jint mode);

void JNICALL WC_JNI_ReleaseDoubleArrayElements(JNIEnv* env, 
	jdoubleArray array, jdouble* elems, jint mode);

void JNICALL WC_JNI_ReleaseFloatArrayElements(JNIEnv* env, jfloatArray 
	array, jfloat* elems, jint mode);

void JNICALL WC_JNI_ReleaseIntArrayElements(JNIEnv* env, jintArray 
	array, jint* elems, jint mode);

void JNICALL WC_JNI_ReleaseLongArrayElements(JNIEnv* env, jlongArray 
	array, jlong* elems, jint mode);

void JNICALL WC_JNI_ReleasePrimitiveArrayCritical(JNIEnv* env, jarray 
	array, void* carray, jint mode);

void JNICALL WC_JNI_ReleaseShortArrayElements(JNIEnv* env, jshortArray 
	array, jshort* elems, jint mode);

void JNICALL WC_JNI_ReleaseStringChars(JNIEnv* env, jstring str, const 
	jchar* chars);

void JNICALL WC_JNI_ReleaseStringCritical(JNIEnv* env, jstring string, 
	const jchar* cstring);

void JNICALL WC_JNI_ReleaseStringUTFChars(JNIEnv* env, jstring str, 
	const char* chars);

void JNICALL WC_JNI_SetBooleanArrayRegion(JNIEnv* env, jbooleanArray 
	array, jsize start, jsize l, const jboolean* buf);

void JNICALL WC_JNI_SetBooleanField(JNIEnv* env, jobject obj, jfieldID 
	fieldID, jboolean val);

void JNICALL WC_JNI_SetByteArrayRegion(JNIEnv* env, jbyteArray array, 
	jsize start, jsize len, const jbyte* buf);

void JNICALL WC_JNI_SetByteField(JNIEnv* env, jobject obj, jfieldID 
	fieldID, jbyte val);

void JNICALL WC_JNI_SetCharArrayRegion(JNIEnv* env, jcharArray array, 
	jsize start, jsize len, const jchar* buf);

void JNICALL WC_JNI_SetCharField(JNIEnv* env, jobject obj, jfieldID 
	fieldID, jchar val);

void JNICALL WC_JNI_SetDoubleArrayRegion(JNIEnv* env, jdoubleArray 
	array, jsize start, jsize len, const jdouble* buf);

void JNICALL WC_JNI_SetDoubleField(JNIEnv* env, jobject obj, jfieldID 
	fieldID, jdouble val);

void JNICALL WC_JNI_SetFloatArrayRegion(JNIEnv* env, jfloatArray 
	array, jsize start, jsize len, const jfloat* buf);

void JNICALL WC_JNI_SetFloatField(JNIEnv* env, jobject obj, jfieldID 
	fieldID, jfloat val);

void JNICALL WC_JNI_SetIntArrayRegion(JNIEnv* env, jintArray array, 
	jsize start, jsize len, const jint* buf);

void JNICALL WC_JNI_SetIntField(JNIEnv* env, jobject obj, jfieldID 
	fieldID, jint val);

void JNICALL WC_JNI_SetLongArrayRegion(JNIEnv* env, jlongArray array, 
	jsize start, jsize len, const jlong* buf);

void JNICALL WC_JNI_SetLongField(JNIEnv* env, jobject obj, jfieldID 
	fieldID, jlong val);

void JNICALL WC_JNI_SetObjectArrayElement(JNIEnv* env, jobjectArray 
	array, jsize index, jobject val);

void JNICALL WC_JNI_SetObjectField(JNIEnv* env, jobject obj, jfieldID 
	fieldID, jobject val);

void JNICALL WC_JNI_SetShortArrayRegion(JNIEnv* env, jshortArray 
	array, jsize start, jsize len, const jshort* buf);

void JNICALL WC_JNI_SetShortField(JNIEnv* env, jobject obj, jfieldID 
	fieldID, jshort val);

void JNICALL WC_JNI_SetStaticBooleanField(JNIEnv* env, jclass clazz, 
	jfieldID fieldID, jboolean value);

void JNICALL WC_JNI_SetStaticByteField(JNIEnv* env, jclass clazz, 
	jfieldID fieldID, jbyte value);

void JNICALL WC_JNI_SetStaticCharField(JNIEnv* env, jclass clazz, 
	jfieldID fieldID, jchar value);

void JNICALL WC_JNI_SetStaticDoubleField(JNIEnv* env, jclass clazz, 
	jfieldID fieldID, jdouble value);

void JNICALL WC_JNI_SetStaticFloatField(JNIEnv* env, jclass clazz, 
	jfieldID fieldID, jfloat value);

void JNICALL WC_JNI_SetStaticIntField(JNIEnv* env, jclass clazz, 
	jfieldID fieldID, jint value);

void JNICALL WC_JNI_SetStaticLongField(JNIEnv* env, jclass clazz, 
	jfieldID fieldID, jlong value);

void JNICALL WC_JNI_SetStaticObjectField(JNIEnv* env, jclass clazz, 
	jfieldID fieldID, jobject value);

void JNICALL WC_JNI_SetStaticShortField(JNIEnv* env, jclass clazz, 
	jfieldID fieldID, jshort value);

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXJINH
}
#undef SJME_cXJINH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXJINH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGJINH */

