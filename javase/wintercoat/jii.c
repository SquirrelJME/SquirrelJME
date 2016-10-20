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
 * Implements the Java invoke interface calls.
 *
 * @since 2016/10/19
 */

#include "jii.h"
#include "jin.h"

/**
 * {@inheritDoc}
 * @since 2016/10/19
 */
jint JNICALL WC_JII_AttachCurrentThread(JavaVM* pvm, void** penv, void* pargs)
{
	WC_JavaVM* jvm;
	WC_JNIEnv* env;
	WC_JNIEnv* rv;
	pthread_t self, *new;
	pthread_mutex_t* emutex;
	
	// {@squirreljme.error WC0d The owning virtual machine is NULL.}
	WC_ASSERT("WC0d", pvm == NULL);
	
	// {@squirreljme.error WC0e No output environment was specified.}
	WC_ASSERT("WC0e", penv == NULL);
	
	// Get current thread
	self = pthread_self();
	
	// Cast
	jvm = (WC_JavaVM*)pvm;
	
	// {@squirreljme.error WC0g Failed to lock the VM mutex when attaching the
	// current thread.}
	WC_ASSERT("WC0g", pthread_mutex_lock(jvm->furry.mutex) != 0);
	
	// Go through all threads to see if the thread was already allocated
	rv = NULL;
	for (env = jvm->furry.envchain; env != NULL; env = env->furry.next)
		if (pthread_equal(self, *env->furry.thread))
		{
			rv = env;
			break;
		}
	
	// If the thread was not bound then create an environment for it
	if (rv == NULL)
	{
		// Allocate
		rv = WC_ForcedMalloc(sizeof(*rv));
		
		// {@squirreljme.error WC0h Could not initialize the thread mutex.}
		emutex = WC_ForcedMalloc(sizeof(*emutex));
		WC_ASSERT("WC0h", pthread_mutex_init(emutex, NULL) != 0);
		rv->furry.mutex = emutex;
		
		// Set
		rv->furry.vm = jvm;
		new = WC_ForcedMalloc(sizeof(*new));
		*new = self;
		rv->furry.thread = new;
		
		// Setup function pointers
		rv->native.GetStringUTFChars = WC_JNI_GetStringUTFChars;
		rv->native.GetStringChars = WC_JNI_GetStringChars;
		rv->native.GetStringCritical = WC_JNI_GetStringCritical;
		rv->native.NewBooleanArray = WC_JNI_NewBooleanArray;
		rv->native.CallBooleanMethodA = WC_JNI_CallBooleanMethodA;
		rv->native.CallBooleanMethod = WC_JNI_CallBooleanMethod;
		rv->native.CallBooleanMethodV = WC_JNI_CallBooleanMethodV;
		rv->native.CallNonvirtualBooleanMethodA = 
			WC_JNI_CallNonvirtualBooleanMethodA;
		rv->native.CallNonvirtualBooleanMethod = 
			WC_JNI_CallNonvirtualBooleanMethod;
		rv->native.CallNonvirtualBooleanMethodV = 
			WC_JNI_CallNonvirtualBooleanMethodV;
		rv->native.CallStaticBooleanMethodA = WC_JNI_CallStaticBooleanMethodA;
		rv->native.CallStaticBooleanMethod = WC_JNI_CallStaticBooleanMethod;
		rv->native.CallStaticBooleanMethodV = WC_JNI_CallStaticBooleanMethodV;
		rv->native.ExceptionCheck = WC_JNI_ExceptionCheck;
		rv->native.GetBooleanArrayElements = WC_JNI_GetBooleanArrayElements;
		rv->native.GetBooleanField = WC_JNI_GetBooleanField;
		rv->native.GetStaticBooleanField = WC_JNI_GetStaticBooleanField;
		rv->native.IsAssignableFrom = WC_JNI_IsAssignableFrom;
		rv->native.IsInstanceOf = WC_JNI_IsInstanceOf;
		rv->native.IsSameObject = WC_JNI_IsSameObject;
		rv->native.NewByteArray = WC_JNI_NewByteArray;
		rv->native.CallByteMethodA = WC_JNI_CallByteMethodA;
		rv->native.CallByteMethod = WC_JNI_CallByteMethod;
		rv->native.CallByteMethodV = WC_JNI_CallByteMethodV;
		rv->native.CallNonvirtualByteMethodA = 
			WC_JNI_CallNonvirtualByteMethodA;
		rv->native.CallNonvirtualByteMethod = WC_JNI_CallNonvirtualByteMethod;
		rv->native.CallNonvirtualByteMethodV = 
			WC_JNI_CallNonvirtualByteMethodV;
		rv->native.CallStaticByteMethodA = WC_JNI_CallStaticByteMethodA;
		rv->native.CallStaticByteMethod = WC_JNI_CallStaticByteMethod;
		rv->native.CallStaticByteMethodV = WC_JNI_CallStaticByteMethodV;
		rv->native.GetByteArrayElements = WC_JNI_GetByteArrayElements;
		rv->native.GetByteField = WC_JNI_GetByteField;
		rv->native.GetStaticByteField = WC_JNI_GetStaticByteField;
		rv->native.NewCharArray = WC_JNI_NewCharArray;
		rv->native.CallCharMethodA = WC_JNI_CallCharMethodA;
		rv->native.CallCharMethod = WC_JNI_CallCharMethod;
		rv->native.CallCharMethodV = WC_JNI_CallCharMethodV;
		rv->native.CallNonvirtualCharMethodA = 
			WC_JNI_CallNonvirtualCharMethodA;
		rv->native.CallNonvirtualCharMethod = WC_JNI_CallNonvirtualCharMethod;
		rv->native.CallNonvirtualCharMethodV = 
			WC_JNI_CallNonvirtualCharMethodV;
		rv->native.CallStaticCharMethodA = WC_JNI_CallStaticCharMethodA;
		rv->native.CallStaticCharMethod = WC_JNI_CallStaticCharMethod;
		rv->native.CallStaticCharMethodV = WC_JNI_CallStaticCharMethodV;
		rv->native.GetCharArrayElements = WC_JNI_GetCharArrayElements;
		rv->native.GetCharField = WC_JNI_GetCharField;
		rv->native.GetStaticCharField = WC_JNI_GetStaticCharField;
		rv->native.DefineClass = WC_JNI_DefineClass;
		rv->native.FindClass = WC_JNI_FindClass;
		rv->native.GetObjectClass = WC_JNI_GetObjectClass;
		rv->native.GetSuperclass = WC_JNI_GetSuperclass;
		rv->native.NewDoubleArray = WC_JNI_NewDoubleArray;
		rv->native.CallDoubleMethodA = WC_JNI_CallDoubleMethodA;
		rv->native.CallDoubleMethod = WC_JNI_CallDoubleMethod;
		rv->native.CallDoubleMethodV = WC_JNI_CallDoubleMethodV;
		rv->native.CallNonvirtualDoubleMethodA = 
			WC_JNI_CallNonvirtualDoubleMethodA;
		rv->native.CallNonvirtualDoubleMethod = 
			WC_JNI_CallNonvirtualDoubleMethod;
		rv->native.CallNonvirtualDoubleMethodV = 
			WC_JNI_CallNonvirtualDoubleMethodV;
		rv->native.CallStaticDoubleMethodA = WC_JNI_CallStaticDoubleMethodA;
		rv->native.CallStaticDoubleMethod = WC_JNI_CallStaticDoubleMethod;
		rv->native.CallStaticDoubleMethodV = WC_JNI_CallStaticDoubleMethodV;
		rv->native.GetDoubleArrayElements = WC_JNI_GetDoubleArrayElements;
		rv->native.GetDoubleField = WC_JNI_GetDoubleField;
		rv->native.GetStaticDoubleField = WC_JNI_GetStaticDoubleField;
		rv->native.FromReflectedField = WC_JNI_FromReflectedField;
		rv->native.GetFieldID = WC_JNI_GetFieldID;
		rv->native.GetStaticFieldID = WC_JNI_GetStaticFieldID;
		rv->native.NewFloatArray = WC_JNI_NewFloatArray;
		rv->native.CallFloatMethodA = WC_JNI_CallFloatMethodA;
		rv->native.CallFloatMethod = WC_JNI_CallFloatMethod;
		rv->native.CallFloatMethodV = WC_JNI_CallFloatMethodV;
		rv->native.CallNonvirtualFloatMethodA = 
			WC_JNI_CallNonvirtualFloatMethodA;
		rv->native.CallNonvirtualFloatMethod = 
			WC_JNI_CallNonvirtualFloatMethod;
		rv->native.CallNonvirtualFloatMethodV = 
			WC_JNI_CallNonvirtualFloatMethodV;
		rv->native.CallStaticFloatMethodA = WC_JNI_CallStaticFloatMethodA;
		rv->native.CallStaticFloatMethod = WC_JNI_CallStaticFloatMethod;
		rv->native.CallStaticFloatMethodV = WC_JNI_CallStaticFloatMethodV;
		rv->native.GetFloatArrayElements = WC_JNI_GetFloatArrayElements;
		rv->native.GetFloatField = WC_JNI_GetFloatField;
		rv->native.GetStaticFloatField = WC_JNI_GetStaticFloatField;
		rv->native.NewIntArray = WC_JNI_NewIntArray;
		rv->native.CallIntMethodA = WC_JNI_CallIntMethodA;
		rv->native.CallIntMethod = WC_JNI_CallIntMethod;
		rv->native.CallIntMethodV = WC_JNI_CallIntMethodV;
		rv->native.CallNonvirtualIntMethodA = WC_JNI_CallNonvirtualIntMethodA;
		rv->native.CallNonvirtualIntMethod = WC_JNI_CallNonvirtualIntMethod;
		rv->native.CallNonvirtualIntMethodV = WC_JNI_CallNonvirtualIntMethodV;
		rv->native.CallStaticIntMethodA = WC_JNI_CallStaticIntMethodA;
		rv->native.CallStaticIntMethod = WC_JNI_CallStaticIntMethod;
		rv->native.CallStaticIntMethodV = WC_JNI_CallStaticIntMethodV;
		rv->native.EnsureLocalCapacity = WC_JNI_EnsureLocalCapacity;
		rv->native.GetIntArrayElements = WC_JNI_GetIntArrayElements;
		rv->native.GetIntField = WC_JNI_GetIntField;
		rv->native.GetJavaVM = WC_JNI_GetJavaVM;
		rv->native.GetStaticIntField = WC_JNI_GetStaticIntField;
		rv->native.GetVersion = WC_JNI_GetVersion;
		rv->native.MonitorEnter = WC_JNI_MonitorEnter;
		rv->native.MonitorExit = WC_JNI_MonitorExit;
		rv->native.PushLocalFrame = WC_JNI_PushLocalFrame;
		rv->native.RegisterNatives = WC_JNI_RegisterNatives;
		rv->native.Throw = WC_JNI_Throw;
		rv->native.ThrowNew = WC_JNI_ThrowNew;
		rv->native.UnregisterNatives = WC_JNI_UnregisterNatives;
		rv->native.NewLongArray = WC_JNI_NewLongArray;
		rv->native.CallLongMethodA = WC_JNI_CallLongMethodA;
		rv->native.CallLongMethod = WC_JNI_CallLongMethod;
		rv->native.CallLongMethodV = WC_JNI_CallLongMethodV;
		rv->native.CallNonvirtualLongMethodA = 
			WC_JNI_CallNonvirtualLongMethodA;
		rv->native.CallNonvirtualLongMethod = WC_JNI_CallNonvirtualLongMethod;
		rv->native.CallNonvirtualLongMethodV = 
			WC_JNI_CallNonvirtualLongMethodV;
		rv->native.CallStaticLongMethodA = WC_JNI_CallStaticLongMethodA;
		rv->native.CallStaticLongMethod = WC_JNI_CallStaticLongMethod;
		rv->native.CallStaticLongMethodV = WC_JNI_CallStaticLongMethodV;
		rv->native.GetDirectBufferCapacity = WC_JNI_GetDirectBufferCapacity;
		rv->native.GetLongArrayElements = WC_JNI_GetLongArrayElements;
		rv->native.GetLongField = WC_JNI_GetLongField;
		rv->native.GetStaticLongField = WC_JNI_GetStaticLongField;
		rv->native.FromReflectedMethod = WC_JNI_FromReflectedMethod;
		rv->native.GetMethodID = WC_JNI_GetMethodID;
		rv->native.GetStaticMethodID = WC_JNI_GetStaticMethodID;
		rv->native.NewObjectArray = WC_JNI_NewObjectArray;
		rv->native.AllocObject = WC_JNI_AllocObject;
		rv->native.CallNonvirtualObjectMethodA = 
			WC_JNI_CallNonvirtualObjectMethodA;
		rv->native.CallNonvirtualObjectMethod = 
			WC_JNI_CallNonvirtualObjectMethod;
		rv->native.CallNonvirtualObjectMethodV = 
			WC_JNI_CallNonvirtualObjectMethodV;
		rv->native.CallObjectMethodA = WC_JNI_CallObjectMethodA;
		rv->native.CallObjectMethod = WC_JNI_CallObjectMethod;
		rv->native.CallObjectMethodV = WC_JNI_CallObjectMethodV;
		rv->native.CallStaticObjectMethodA = WC_JNI_CallStaticObjectMethodA;
		rv->native.CallStaticObjectMethod = WC_JNI_CallStaticObjectMethod;
		rv->native.CallStaticObjectMethodV = WC_JNI_CallStaticObjectMethodV;
		rv->native.GetObjectArrayElement = WC_JNI_GetObjectArrayElement;
		rv->native.GetObjectField = WC_JNI_GetObjectField;
		rv->native.GetStaticObjectField = WC_JNI_GetStaticObjectField;
		rv->native.NewDirectByteBuffer = WC_JNI_NewDirectByteBuffer;
		rv->native.NewGlobalRef = WC_JNI_NewGlobalRef;
		rv->native.NewLocalRef = WC_JNI_NewLocalRef;
		rv->native.NewObjectA = WC_JNI_NewObjectA;
		rv->native.NewObject = WC_JNI_NewObject;
		rv->native.NewObjectV = WC_JNI_NewObjectV;
		rv->native.PopLocalFrame = WC_JNI_PopLocalFrame;
		rv->native.ToReflectedField = WC_JNI_ToReflectedField;
		rv->native.ToReflectedMethod = WC_JNI_ToReflectedMethod;
		rv->native.GetObjectRefType = WC_JNI_GetObjectRefType;
		rv->native.NewShortArray = WC_JNI_NewShortArray;
		rv->native.CallNonvirtualShortMethodA = 
			WC_JNI_CallNonvirtualShortMethodA;
		rv->native.CallNonvirtualShortMethod = 
			WC_JNI_CallNonvirtualShortMethod;
		rv->native.CallNonvirtualShortMethodV = 
			WC_JNI_CallNonvirtualShortMethodV;
		rv->native.CallShortMethodA = WC_JNI_CallShortMethodA;
		rv->native.CallShortMethod = WC_JNI_CallShortMethod;
		rv->native.CallShortMethodV = WC_JNI_CallShortMethodV;
		rv->native.CallStaticShortMethodA = WC_JNI_CallStaticShortMethodA;
		rv->native.CallStaticShortMethod = WC_JNI_CallStaticShortMethod;
		rv->native.CallStaticShortMethodV = WC_JNI_CallStaticShortMethodV;
		rv->native.GetShortArrayElements = WC_JNI_GetShortArrayElements;
		rv->native.GetShortField = WC_JNI_GetShortField;
		rv->native.GetStaticShortField = WC_JNI_GetStaticShortField;
		rv->native.GetArrayLength = WC_JNI_GetArrayLength;
		rv->native.GetStringLength = WC_JNI_GetStringLength;
		rv->native.GetStringUTFLength = WC_JNI_GetStringUTFLength;
		rv->native.NewString = WC_JNI_NewString;
		rv->native.NewStringUTF = WC_JNI_NewStringUTF;
		rv->native.ExceptionOccurred = WC_JNI_ExceptionOccurred;
		rv->native.NewWeakGlobalRef = WC_JNI_NewWeakGlobalRef;
		rv->native.CallNonvirtualVoidMethodA = 
			WC_JNI_CallNonvirtualVoidMethodA;
		rv->native.CallNonvirtualVoidMethod = WC_JNI_CallNonvirtualVoidMethod;
		rv->native.CallNonvirtualVoidMethodV = 
			WC_JNI_CallNonvirtualVoidMethodV;
		rv->native.CallStaticVoidMethodA = WC_JNI_CallStaticVoidMethodA;
		rv->native.CallStaticVoidMethod = WC_JNI_CallStaticVoidMethod;
		rv->native.CallStaticVoidMethodV = WC_JNI_CallStaticVoidMethodV;
		rv->native.CallVoidMethodA = WC_JNI_CallVoidMethodA;
		rv->native.CallVoidMethod = WC_JNI_CallVoidMethod;
		rv->native.CallVoidMethodV = WC_JNI_CallVoidMethodV;
		rv->native.DeleteGlobalRef = WC_JNI_DeleteGlobalRef;
		rv->native.DeleteLocalRef = WC_JNI_DeleteLocalRef;
		rv->native.DeleteWeakGlobalRef = WC_JNI_DeleteWeakGlobalRef;
		rv->native.ExceptionClear = WC_JNI_ExceptionClear;
		rv->native.ExceptionDescribe = WC_JNI_ExceptionDescribe;
		rv->native.FatalError = WC_JNI_FatalError;
		rv->native.GetBooleanArrayRegion = WC_JNI_GetBooleanArrayRegion;
		rv->native.GetByteArrayRegion = WC_JNI_GetByteArrayRegion;
		rv->native.GetCharArrayRegion = WC_JNI_GetCharArrayRegion;
		rv->native.GetDirectBufferAddress = WC_JNI_GetDirectBufferAddress;
		rv->native.GetDoubleArrayRegion = WC_JNI_GetDoubleArrayRegion;
		rv->native.GetFloatArrayRegion = WC_JNI_GetFloatArrayRegion;
		rv->native.GetIntArrayRegion = WC_JNI_GetIntArrayRegion;
		rv->native.GetLongArrayRegion = WC_JNI_GetLongArrayRegion;
		rv->native.GetPrimitiveArrayCritical = 
			WC_JNI_GetPrimitiveArrayCritical;
		rv->native.GetShortArrayRegion = WC_JNI_GetShortArrayRegion;
		rv->native.GetStringRegion = WC_JNI_GetStringRegion;
		rv->native.GetStringUTFRegion = WC_JNI_GetStringUTFRegion;
		rv->native.ReleaseBooleanArrayElements = 
			WC_JNI_ReleaseBooleanArrayElements;
		rv->native.ReleaseByteArrayElements = WC_JNI_ReleaseByteArrayElements;
		rv->native.ReleaseCharArrayElements = WC_JNI_ReleaseCharArrayElements;
		rv->native.ReleaseDoubleArrayElements = 
			WC_JNI_ReleaseDoubleArrayElements;
		rv->native.ReleaseFloatArrayElements = 
			WC_JNI_ReleaseFloatArrayElements;
		rv->native.ReleaseIntArrayElements = WC_JNI_ReleaseIntArrayElements;
		rv->native.ReleaseLongArrayElements = WC_JNI_ReleaseLongArrayElements;
		rv->native.ReleasePrimitiveArrayCritical = 
			WC_JNI_ReleasePrimitiveArrayCritical;
		rv->native.ReleaseShortArrayElements = 
			WC_JNI_ReleaseShortArrayElements;
		rv->native.ReleaseStringChars = WC_JNI_ReleaseStringChars;
		rv->native.ReleaseStringCritical = WC_JNI_ReleaseStringCritical;
		rv->native.ReleaseStringUTFChars = WC_JNI_ReleaseStringUTFChars;
		rv->native.SetBooleanArrayRegion = WC_JNI_SetBooleanArrayRegion;
		rv->native.SetBooleanField = WC_JNI_SetBooleanField;
		rv->native.SetByteArrayRegion = WC_JNI_SetByteArrayRegion;
		rv->native.SetByteField = WC_JNI_SetByteField;
		rv->native.SetCharArrayRegion = WC_JNI_SetCharArrayRegion;
		rv->native.SetCharField = WC_JNI_SetCharField;
		rv->native.SetDoubleArrayRegion = WC_JNI_SetDoubleArrayRegion;
		rv->native.SetDoubleField = WC_JNI_SetDoubleField;
		rv->native.SetFloatArrayRegion = WC_JNI_SetFloatArrayRegion;
		rv->native.SetFloatField = WC_JNI_SetFloatField;
		rv->native.SetIntArrayRegion = WC_JNI_SetIntArrayRegion;
		rv->native.SetIntField = WC_JNI_SetIntField;
		rv->native.SetLongArrayRegion = WC_JNI_SetLongArrayRegion;
		rv->native.SetLongField = WC_JNI_SetLongField;
		rv->native.SetObjectArrayElement = WC_JNI_SetObjectArrayElement;
		rv->native.SetObjectField = WC_JNI_SetObjectField;
		rv->native.SetShortArrayRegion = WC_JNI_SetShortArrayRegion;
		rv->native.SetShortField = WC_JNI_SetShortField;
		rv->native.SetStaticBooleanField = WC_JNI_SetStaticBooleanField;
		rv->native.SetStaticByteField = WC_JNI_SetStaticByteField;
		rv->native.SetStaticCharField = WC_JNI_SetStaticCharField;
		rv->native.SetStaticDoubleField = WC_JNI_SetStaticDoubleField;
		rv->native.SetStaticFloatField = WC_JNI_SetStaticFloatField;
		rv->native.SetStaticIntField = WC_JNI_SetStaticIntField;
		rv->native.SetStaticLongField = WC_JNI_SetStaticLongField;
		rv->native.SetStaticObjectField = WC_JNI_SetStaticObjectField;
		rv->native.SetStaticShortField = WC_JNI_SetStaticShortField;
		
		// Link into the chain
		rv->furry.next = jvm->furry.envchain;
		jvm->furry.envchain = rv;
		
		// Use this one
		(*penv) = rv;
	}
	
	// {@squirreljme.error EC0h Failed to unlock the VM mutex when attaching
	// the current thread.}
	WC_ASSERT("WC0h", pthread_mutex_unlock(jvm->furry.mutex) != 0);
	
	// Ok
	return JNI_OK;
}

jint JNICALL WC_JII_AttachCurrentThreadAsDaemon(JavaVM* pvm, void** penv,
	void* pargs)
{
	WC_TODO();
	return 1;
}

jint JNICALL WC_JII_DestroyJavaVM(JavaVM* pvm)
{
	WC_TODO();
	return 1;
}

jint JNICALL WC_JII_DetachCurrentThread(JavaVM* pvm)
{
	WC_TODO();
	return 1;
}

jint JNICALL WC_JII_GetEnv(JavaVM* pvm, void** penv, jint pversion)
{
	WC_TODO();
	return 1;
}

