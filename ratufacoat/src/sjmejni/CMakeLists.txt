# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: SquirrelJME Java Native Interface

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Declare base library
add_library(SquirrelJMEJNI OBJECT
	init.c
	memtag.c
	shadow.c

	# All of the various function implementations, all sources must be
	# specified here without a wildcard so that the object targets in CMake
	# are able to collect every source/object file for inclusion into certain
	# targets such as RetroArch or JNI library
	tables/function/defAllocObject.c
	tables/function/defCallBooleanMethod.c
	tables/function/defCallBooleanMethodA.c
	tables/function/defCallBooleanMethodV.c
	tables/function/defCallByteMethod.c
	tables/function/defCallByteMethodA.c
	tables/function/defCallByteMethodV.c
	tables/function/defCallCharMethod.c
	tables/function/defCallCharMethodA.c
	tables/function/defCallCharMethodV.c
	tables/function/defCallDoubleMethod.c
	tables/function/defCallDoubleMethodA.c
	tables/function/defCallDoubleMethodV.c
	tables/function/defCallFloatMethod.c
	tables/function/defCallFloatMethodA.c
	tables/function/defCallFloatMethodV.c
	tables/function/defCallIntMethod.c
	tables/function/defCallIntMethodA.c
	tables/function/defCallIntMethodV.c
	tables/function/defCallLongMethod.c
	tables/function/defCallLongMethodA.c
	tables/function/defCallLongMethodV.c
	tables/function/defCallNonvirtualBooleanMethod.c
	tables/function/defCallNonvirtualBooleanMethodA.c
	tables/function/defCallNonvirtualBooleanMethodV.c
	tables/function/defCallNonvirtualByteMethod.c
	tables/function/defCallNonvirtualByteMethodA.c
	tables/function/defCallNonvirtualByteMethodV.c
	tables/function/defCallNonvirtualCharMethod.c
	tables/function/defCallNonvirtualCharMethodA.c
	tables/function/defCallNonvirtualCharMethodV.c
	tables/function/defCallNonvirtualDoubleMethod.c
	tables/function/defCallNonvirtualDoubleMethodA.c
	tables/function/defCallNonvirtualDoubleMethodV.c
	tables/function/defCallNonvirtualFloatMethod.c
	tables/function/defCallNonvirtualFloatMethodA.c
	tables/function/defCallNonvirtualFloatMethodV.c
	tables/function/defCallNonvirtualIntMethod.c
	tables/function/defCallNonvirtualIntMethodA.c
	tables/function/defCallNonvirtualIntMethodV.c
	tables/function/defCallNonvirtualLongMethod.c
	tables/function/defCallNonvirtualLongMethodA.c
	tables/function/defCallNonvirtualLongMethodV.c
	tables/function/defCallNonvirtualObjectMethod.c
	tables/function/defCallNonvirtualObjectMethodA.c
	tables/function/defCallNonvirtualObjectMethodV.c
	tables/function/defCallNonvirtualShortMethod.c
	tables/function/defCallNonvirtualShortMethodA.c
	tables/function/defCallNonvirtualShortMethodV.c
	tables/function/defCallNonvirtualVoidMethod.c
	tables/function/defCallNonvirtualVoidMethodA.c
	tables/function/defCallNonvirtualVoidMethodV.c
	tables/function/defCallObjectMethod.c
	tables/function/defCallObjectMethodA.c
	tables/function/defCallObjectMethodV.c
	tables/function/defCallShortMethod.c
	tables/function/defCallShortMethodA.c
	tables/function/defCallShortMethodV.c
	tables/function/defCallStaticBooleanMethod.c
	tables/function/defCallStaticBooleanMethodA.c
	tables/function/defCallStaticBooleanMethodV.c
	tables/function/defCallStaticByteMethod.c
	tables/function/defCallStaticByteMethodA.c
	tables/function/defCallStaticByteMethodV.c
	tables/function/defCallStaticCharMethod.c
	tables/function/defCallStaticCharMethodA.c
	tables/function/defCallStaticCharMethodV.c
	tables/function/defCallStaticDoubleMethod.c
	tables/function/defCallStaticDoubleMethodA.c
	tables/function/defCallStaticDoubleMethodV.c
	tables/function/defCallStaticFloatMethod.c
	tables/function/defCallStaticFloatMethodA.c
	tables/function/defCallStaticFloatMethodV.c
	tables/function/defCallStaticIntMethod.c
	tables/function/defCallStaticIntMethodA.c
	tables/function/defCallStaticIntMethodV.c
	tables/function/defCallStaticLongMethod.c
	tables/function/defCallStaticLongMethodA.c
	tables/function/defCallStaticLongMethodV.c
	tables/function/defCallStaticObjectMethod.c
	tables/function/defCallStaticObjectMethodA.c
	tables/function/defCallStaticObjectMethodV.c
	tables/function/defCallStaticShortMethod.c
	tables/function/defCallStaticShortMethodA.c
	tables/function/defCallStaticShortMethodV.c
	tables/function/defCallStaticVoidMethod.c
	tables/function/defCallStaticVoidMethodA.c
	tables/function/defCallStaticVoidMethodV.c
	tables/function/defCallVoidMethod.c
	tables/function/defCallVoidMethodA.c
	tables/function/defCallVoidMethodV.c
	tables/function/defDefineClass.c
	tables/function/defDeleteGlobalRef.c
	tables/function/defDeleteLocalRef.c
	tables/function/defDeleteWeakGlobalRef.c
	tables/function/defEnsureLocalCapacity.c
	tables/function/defExceptionCheck.c
	tables/function/defExceptionClear.c
	tables/function/defExceptionDescribe.c
	tables/function/defExceptionOccurred.c
	tables/function/defFatalError.c
	tables/function/defFindClass.c
	tables/function/defFromReflectedField.c
	tables/function/defFromReflectedMethod.c
	tables/function/defGetArrayLength.c
	tables/function/defGetBooleanArrayElements.c
	tables/function/defGetBooleanArrayRegion.c
	tables/function/defGetBooleanField.c
	tables/function/defGetByteArrayElements.c
	tables/function/defGetByteArrayRegion.c
	tables/function/defGetByteField.c
	tables/function/defGetCharArrayElements.c
	tables/function/defGetCharArrayRegion.c
	tables/function/defGetCharField.c
	tables/function/defGetDirectBufferAddress.c
	tables/function/defGetDirectBufferCapacity.c
	tables/function/defGetDoubleArrayElements.c
	tables/function/defGetDoubleArrayRegion.c
	tables/function/defGetDoubleField.c
	tables/function/defGetFieldID.c
	tables/function/defGetFloatArrayElements.c
	tables/function/defGetFloatArrayRegion.c
	tables/function/defGetFloatField.c
	tables/function/defGetIntArrayElements.c
	tables/function/defGetIntArrayRegion.c
	tables/function/defGetIntField.c
	tables/function/defGetJavaVM.c
	tables/function/defGetLongArrayElements.c
	tables/function/defGetLongArrayRegion.c
	tables/function/defGetLongField.c
	tables/function/defGetMethodID.c
	tables/function/defGetObjectArrayElement.c
	tables/function/defGetObjectClass.c
	tables/function/defGetObjectField.c
	tables/function/defGetObjectRefType.c
	tables/function/defGetPrimitiveArrayCritical.c
	tables/function/defGetShortArrayElements.c
	tables/function/defGetShortArrayRegion.c
	tables/function/defGetShortField.c
	tables/function/defGetStaticBooleanField.c
	tables/function/defGetStaticByteField.c
	tables/function/defGetStaticCharField.c
	tables/function/defGetStaticDoubleField.c
	tables/function/defGetStaticFieldID.c
	tables/function/defGetStaticFloatField.c
	tables/function/defGetStaticIntField.c
	tables/function/defGetStaticLongField.c
	tables/function/defGetStaticMethodID.c
	tables/function/defGetStaticObjectField.c
	tables/function/defGetStaticShortField.c
	tables/function/defGetStringChars.c
	tables/function/defGetStringCritical.c
	tables/function/defGetStringLength.c
	tables/function/defGetStringRegion.c
	tables/function/defGetStringUTFChars.c
	tables/function/defGetStringUTFLength.c
	tables/function/defGetStringUTFRegion.c
	tables/function/defGetSuperclass.c
	tables/function/defGetVersion.c
	tables/function/defIsAssignableFrom.c
	tables/function/defIsInstanceOf.c
	tables/function/defIsSameObject.c
	tables/function/defMonitorEnter.c
	tables/function/defMonitorExit.c
	tables/function/defNewBooleanArray.c
	tables/function/defNewByteArray.c
	tables/function/defNewCharArray.c
	tables/function/defNewDirectByteBuffer.c
	tables/function/defNewDoubleArray.c
	tables/function/defNewFloatArray.c
	tables/function/defNewGlobalRef.c
	tables/function/defNewIntArray.c
	tables/function/defNewLocalRef.c
	tables/function/defNewLongArray.c
	tables/function/defNewObject.c
	tables/function/defNewObjectA.c
	tables/function/defNewObjectArray.c
	tables/function/defNewObjectV.c
	tables/function/defNewShortArray.c
	tables/function/defNewString.c
	tables/function/defNewStringUTF.c
	tables/function/defNewWeakGlobalRef.c
	tables/function/defPopLocalFrame.c
	tables/function/defPushLocalFrame.c
	tables/function/defRegisterNatives.c
	tables/function/defReleaseBooleanArrayElements.c
	tables/function/defReleaseByteArrayElements.c
	tables/function/defReleaseCharArrayElements.c
	tables/function/defReleaseDoubleArrayElements.c
	tables/function/defReleaseFloatArrayElements.c
	tables/function/defReleaseIntArrayElements.c
	tables/function/defReleaseLongArrayElements.c
	tables/function/defReleasePrimitiveArrayCritical.c
	tables/function/defReleaseShortArrayElements.c
	tables/function/defReleaseStringChars.c
	tables/function/defReleaseStringCritical.c
	tables/function/defReleaseStringUTFChars.c
	tables/function/defSetBooleanArrayRegion.c
	tables/function/defSetBooleanField.c
	tables/function/defSetByteArrayRegion.c
	tables/function/defSetByteField.c
	tables/function/defSetCharArrayRegion.c
	tables/function/defSetCharField.c
	tables/function/defSetDoubleArrayRegion.c
	tables/function/defSetDoubleField.c
	tables/function/defSetFloatArrayRegion.c
	tables/function/defSetFloatField.c
	tables/function/defSetIntArrayRegion.c
	tables/function/defSetIntField.c
	tables/function/defSetLongArrayRegion.c
	tables/function/defSetLongField.c
	tables/function/defSetObjectArrayElement.c
	tables/function/defSetObjectField.c
	tables/function/defSetShortArrayRegion.c
	tables/function/defSetShortField.c
	tables/function/defSetStaticBooleanField.c
	tables/function/defSetStaticByteField.c
	tables/function/defSetStaticCharField.c
	tables/function/defSetStaticDoubleField.c
	tables/function/defSetStaticFloatField.c
	tables/function/defSetStaticIntField.c
	tables/function/defSetStaticLongField.c
	tables/function/defSetStaticObjectField.c
	tables/function/defSetStaticShortField.c
	tables/function/defThrow.c
	tables/function/defThrowNew.c
	tables/function/defToReflectedField.c
	tables/function/defToReflectedMethod.c
	tables/function/defUnregisterNatives.c
	tables/interface/defAttachCurrentThread.c
	tables/interface/defAttachCurrentThreadAsDaemon.c
	tables/interface/defDestroyJavaVM.c
	tables/interface/defDetachCurrentThread.c
	tables/interface/defGetEnv.c)

# This is written only in C!
set_target_properties(SquirrelJMEJNI PROPERTIES LINKER_LANGUAGE C)

# Dynamically generated and standard includes
target_include_directories(SquirrelJMEJNI PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# Make this position independent so it can be linked into shared libraries
set_property(TARGET SquirrelJMEJNI
	PROPERTY POSITION_INDEPENDENT_CODE ON)
