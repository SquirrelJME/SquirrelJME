/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "frontend/libjvm/commonJniJvm.h"
#include "frontend/libjvm/internals.h"
#include "frontend/libjvm/vmAll.h"
#include "sjme/alloc.h"
#include "sjme/nvm/boot.h"
#include "sjme/debug.h"
#include "sjme/nvm/task.h"

/** Default amount of memory. */
#define SJME_JVM_INIT_MEMORY (64 * 1048576)

/** The global NVM state for the JNI wrapper. */
static sjme_atomic(sjme_nvm) sjme_jni_nvm_state;

jint JNICALL JNI_CreateJavaVM(
	sjme_attrOutNotNull JavaVM** pvm,
	sjme_attrOutNotNull void** penv,
	sjme_attrInNotNull void* args)
{
	sjme_errorCode error;
	struct JVMNativeInterface* resultJvm;
	struct JNINativeInterface* resultEnv;
	sjme_alloc_pool pool;
	sjme_nvm nvmState;
	JavaVMInitArgs* initArgs;
	sjme_nvm_task initTask;
	sjme_nvm_bootParam bootParam;
	sjme_jint argc, i, o;
	sjme_lpcstr* argv;
	sjme_nvm_task_taskNewConfig taskConfig;
	sjme_list(sjme_nvm_rom_library)* classPath;

	if (pvm == NULL || penv == NULL || args == NULL)
		return JNI_EINVAL;

	/* Aliased. */
	initArgs = args;

	/* Negative number of options?. */
	argc = initArgs->nOptions;
	if (argc < 0)
		return JNI_EINVAL;

	/* Either too old or too new. */
	if (initArgs->version < JNI_VERSION_1_1 ||
		initArgs->version > JNI_VERSION_1_8)
		return JNI_EVERSION;

	/* Setup target argv container. */
	argv = sjme_alloca(sizeof(*argv) * (argc + 2));
	if (argv == NULL)
		return JNI_ENOMEM;
	memset(argv, 0, sizeof(*argv) * (argc + 2));

	/* Recover process name. */
	argv[0] = "squirreljme";

	/* Decompose arguments. */
	/* OpenJDK sends these: */
	/* -Djava.class.path=. */
	/* -Dsun.java.launcher=SUN_STANDARD */
	/* -Dsun.java.launcher.pid=30954 */
	for (i = 0, o = 1; i < argc; i++, o++)
		argv[o] = initArgs->options[i].optionString;

	/* Check to see if an existing state exists to create a new task under. */
	nvmState = sjme_atomic_g(sjme_nvm, &sjme_jni_nvm_state);
	if (nvmState != NULL)
	{
		/* Use the pre-existing pool. */
		pool = nvmState->allocPool;
	}

	/* Creating a fresh VM */
	else
	{
		/* Allocate the memory needed for SquirrelJME. */
		pool = NULL;
		if (sjme_error_is(error = sjme_alloc_poolInitMalloc(&pool,
			SJME_JVM_INIT_MEMORY)) || pool == NULL)
			goto fail_noMemory;
	}

	/* Setup boot parameters. */
	memset(&bootParam, 0, sizeof(bootParam));
	bootParam.nal = &sjme_nal_default;

	/* Allow launcher fallback by default, it is possible that command line */
	/* parsing will set this to false if -jar or a main class is specified. */
	bootParam.launcherFallback = SJME_JNI_TRUE;

	/* Parse the command line. */
	if (sjme_error_is(error = sjme_nvm_parseCommandLine(pool,
		&sjme_nal_default, &bootParam, argc + 1, argv)))
	{
		if (error == SJME_ERROR_EXIT)
			return JNI_OK;
		goto fail_nvmParseArgs;
	}

	/* Always belay main since we are doing this in compatibility with */
	/* the JVM library. */
	bootParam.belay = SJME_NVM_BOOT_BELAY_MAIN;

	/* Allocate resultant function structure. */
	resultJvm = NULL;
	if (sjme_error_is(error = sjme_alloc(pool, sizeof(*resultJvm),
			(void**)&resultJvm)) ||
		resultJvm == NULL)
		goto fail_allocResultVm;

	/* Allocate environment based functions. */
	resultEnv = NULL;
	if (sjme_error_is(error = sjme_alloc(pool, sizeof(*resultEnv),
			(void**)&resultEnv)) ||
		resultEnv == NULL)
		goto fail_allocResultEnv;

	/* Creating a fresh virtual machine? */
	if (nvmState == NULL)
	{
		/* Boot the virtual machine. */
		initTask = NULL;
		if (sjme_error_is(error = sjme_nvm_boot(pool,
			&bootParam, &nvmState, &initTask)) ||
			nvmState == NULL || initTask == NULL)
			goto fail_nvmBoot;

		/* Store global state. */
		sjme_atomic_cs(sjme_nvm, &sjme_jni_nvm_state,
			NULL, nvmState);
	}

	/* Adding a task to an existing one. */
	else
	{
		/* The boot parameters need to be translated to tasks. */
		memset(&taskConfig, 0, sizeof(taskConfig));
		
		/* Search the classpath for the libraries specified. */
		classPath = NULL;
		if (sjme_error_is(error = sjme_nvm_rom_resolveClassPathByName(
			nvmState->suite,
			bootParam.mainClassPathByName, &classPath)))
			goto fail_searchClasspath;
		
		/* Setup class loader for the task. */
		taskConfig.classLoader = NULL;
		if (sjme_error_is(error = sjme_nvm_vmClass_loaderNew(nvmState,
			&taskConfig.classLoader, classPath)) ||
			taskConfig.classLoader == NULL)
			goto fail_initClassLoader;

		/* General. */
		taskConfig.noOptimize = bootParam.noOptimize;

		/* Belay from. */
		taskConfig.belay = bootParam.belay;

		/* Arguments. */
		taskConfig.mainClass = bootParam.mainClass;
		taskConfig.mainArgs = bootParam.mainArgs;
		taskConfig.sysProps = bootParam.sysProps;

		/* Terminal configuration. */
		taskConfig.stdOut = SJME_NVM_TASK_PIPE_REDIRECT_TYPE_TERMINAL;
		taskConfig.stdErr = SJME_NVM_TASK_PIPE_REDIRECT_TYPE_TERMINAL;
		
		/* Start the task. */
		initTask = NULL;
		if (sjme_error_is(error = sjme_nvm_task_taskNew(nvmState,
			&taskConfig, &initTask)) || initTask == NULL)
			goto fail_initTask;
	}
	
	/* Store the environment and VM state into both structures the same. */
	/* Note that the first pointer always points to self so that double */
	/* dereference still works without going sane! */
	SJME_JNI_JVM_JVM(resultJvm) = resultJvm;
	SJME_JNI_JVM_ENV(resultJvm) = resultEnv;
	SJME_JNI_JVM_TASK(resultJvm) = initTask;
	SJME_JNI_ENV_JVM(resultEnv) = resultJvm;
	SJME_JNI_ENV_ENV(resultEnv) = resultEnv;
	SJME_JNI_ENV_TASK(resultEnv) = initTask;

	/* Then link back to both. */
	nvmState->common.frontEnd.wrapper = resultJvm;
	nvmState->common.frontEnd.data = resultEnv;

	/* Bind function pointers. */
#define sjme_jniSetImpl(group, x) \
	SJME_TOKEN_PASTE(result, group)->x = \
		SJME_TOKEN_PASTE4(sjme_jni_, group, x, Impl)

	sjme_jniSetImpl(Jvm, DestroyJavaVM);
	sjme_jniSetImpl(Jvm, AttachCurrentThread);
	sjme_jniSetImpl(Jvm, DetachCurrentThread);
	sjme_jniSetImpl(Jvm, GetEnv);
	sjme_jniSetImpl(Jvm, AttachCurrentThreadAsDaemon);
	sjme_jniSetImpl(Env, GetVersion);
	sjme_jniSetImpl(Env, DefineClass);
	sjme_jniSetImpl(Env, FindClass);
	sjme_jniSetImpl(Env, FromReflectedMethod);
	sjme_jniSetImpl(Env, FromReflectedField);
	sjme_jniSetImpl(Env, ToReflectedMethod);
	sjme_jniSetImpl(Env, GetSuperclass);
	sjme_jniSetImpl(Env, IsAssignableFrom);
	sjme_jniSetImpl(Env, ToReflectedField);
	sjme_jniSetImpl(Env, Throw);
	sjme_jniSetImpl(Env, ThrowNew);
	sjme_jniSetImpl(Env, ExceptionOccurred);
	sjme_jniSetImpl(Env, ExceptionDescribe);
	sjme_jniSetImpl(Env, ExceptionClear);
	sjme_jniSetImpl(Env, FatalError);
	sjme_jniSetImpl(Env, PushLocalFrame);
	sjme_jniSetImpl(Env, PopLocalFrame);
	sjme_jniSetImpl(Env, NewGlobalRef);
	sjme_jniSetImpl(Env, DeleteGlobalRef);
	sjme_jniSetImpl(Env, DeleteLocalRef);
	sjme_jniSetImpl(Env, IsSameObject);
	sjme_jniSetImpl(Env, NewLocalRef);
	sjme_jniSetImpl(Env, EnsureLocalCapacity);
	sjme_jniSetImpl(Env, AllocObject);
	sjme_jniSetImpl(Env, NewObject);
	sjme_jniSetImpl(Env, NewObjectV);
	sjme_jniSetImpl(Env, NewObjectA);
	sjme_jniSetImpl(Env, GetObjectClass);
	sjme_jniSetImpl(Env, IsInstanceOf);
	sjme_jniSetImpl(Env, GetMethodID);
	sjme_jniSetImpl(Env, CallObjectMethod);
	sjme_jniSetImpl(Env, CallObjectMethodV);
	sjme_jniSetImpl(Env, CallObjectMethodA);
	sjme_jniSetImpl(Env, CallBooleanMethod);
	sjme_jniSetImpl(Env, CallBooleanMethodV);
	sjme_jniSetImpl(Env, CallBooleanMethodA);
	sjme_jniSetImpl(Env, CallByteMethod);
	sjme_jniSetImpl(Env, CallByteMethodV);
	sjme_jniSetImpl(Env, CallByteMethodA);
	sjme_jniSetImpl(Env, CallCharMethod);
	sjme_jniSetImpl(Env, CallCharMethodV);
	sjme_jniSetImpl(Env, CallCharMethodA);
	sjme_jniSetImpl(Env, CallShortMethod);
	sjme_jniSetImpl(Env, CallShortMethodV);
	sjme_jniSetImpl(Env, CallShortMethodA);
	sjme_jniSetImpl(Env, CallIntMethod);
	sjme_jniSetImpl(Env, CallIntMethodV);
	sjme_jniSetImpl(Env, CallIntMethodA);
	sjme_jniSetImpl(Env, CallLongMethod);
	sjme_jniSetImpl(Env, CallLongMethodV);
	sjme_jniSetImpl(Env, CallLongMethodA);
	sjme_jniSetImpl(Env, CallFloatMethod);
	sjme_jniSetImpl(Env, CallFloatMethodV);
	sjme_jniSetImpl(Env, CallFloatMethodA);
	sjme_jniSetImpl(Env, CallDoubleMethod);
	sjme_jniSetImpl(Env, CallDoubleMethodV);
	sjme_jniSetImpl(Env, CallDoubleMethodA);
	sjme_jniSetImpl(Env, CallVoidMethod);
	sjme_jniSetImpl(Env, CallVoidMethodV);
	sjme_jniSetImpl(Env, CallVoidMethodA);
	sjme_jniSetImpl(Env, CallNonvirtualObjectMethod);
	sjme_jniSetImpl(Env, CallNonvirtualObjectMethodV);
	sjme_jniSetImpl(Env, CallNonvirtualObjectMethodA);
	sjme_jniSetImpl(Env, CallNonvirtualBooleanMethod);
	sjme_jniSetImpl(Env, CallNonvirtualBooleanMethodV);
	sjme_jniSetImpl(Env, CallNonvirtualBooleanMethodA);
	sjme_jniSetImpl(Env, CallNonvirtualByteMethod);
	sjme_jniSetImpl(Env, CallNonvirtualByteMethodV);
	sjme_jniSetImpl(Env, CallNonvirtualByteMethodA);
	sjme_jniSetImpl(Env, CallNonvirtualCharMethod);
	sjme_jniSetImpl(Env, CallNonvirtualCharMethodV);
	sjme_jniSetImpl(Env, CallNonvirtualCharMethodA);
	sjme_jniSetImpl(Env, CallNonvirtualShortMethod);
	sjme_jniSetImpl(Env, CallNonvirtualShortMethodV);
	sjme_jniSetImpl(Env, CallNonvirtualShortMethodA);
	sjme_jniSetImpl(Env, CallNonvirtualIntMethod);
	sjme_jniSetImpl(Env, CallNonvirtualIntMethodV);
	sjme_jniSetImpl(Env, CallNonvirtualIntMethodA);
	sjme_jniSetImpl(Env, CallNonvirtualLongMethod);
	sjme_jniSetImpl(Env, CallNonvirtualLongMethodV);
	sjme_jniSetImpl(Env, CallNonvirtualLongMethodA);
	sjme_jniSetImpl(Env, CallNonvirtualFloatMethod);
	sjme_jniSetImpl(Env, CallNonvirtualFloatMethodV);
	sjme_jniSetImpl(Env, CallNonvirtualFloatMethodA);
	sjme_jniSetImpl(Env, CallNonvirtualDoubleMethod);
	sjme_jniSetImpl(Env, CallNonvirtualDoubleMethodV);
	sjme_jniSetImpl(Env, CallNonvirtualDoubleMethodA);
	sjme_jniSetImpl(Env, CallNonvirtualVoidMethod);
	sjme_jniSetImpl(Env, CallNonvirtualVoidMethodV);
	sjme_jniSetImpl(Env, CallNonvirtualVoidMethodA);
	sjme_jniSetImpl(Env, GetFieldID);
	sjme_jniSetImpl(Env, GetObjectField);
	sjme_jniSetImpl(Env, GetBooleanField);
	sjme_jniSetImpl(Env, GetByteField);
	sjme_jniSetImpl(Env, GetCharField);
	sjme_jniSetImpl(Env, GetShortField);
	sjme_jniSetImpl(Env, GetIntField);
	sjme_jniSetImpl(Env, GetLongField);
	sjme_jniSetImpl(Env, GetFloatField);
	sjme_jniSetImpl(Env, GetDoubleField);
	sjme_jniSetImpl(Env, SetObjectField);
	sjme_jniSetImpl(Env, SetBooleanField);
	sjme_jniSetImpl(Env, SetByteField);
	sjme_jniSetImpl(Env, SetCharField);
	sjme_jniSetImpl(Env, SetShortField);
	sjme_jniSetImpl(Env, SetIntField);
	sjme_jniSetImpl(Env, SetLongField);
	sjme_jniSetImpl(Env, SetFloatField);
	sjme_jniSetImpl(Env, SetDoubleField);
	sjme_jniSetImpl(Env, GetStaticMethodID);
	sjme_jniSetImpl(Env, CallStaticObjectMethod);
	sjme_jniSetImpl(Env, CallStaticObjectMethodV);
	sjme_jniSetImpl(Env, CallStaticObjectMethodA);
	sjme_jniSetImpl(Env, CallStaticBooleanMethod);
	sjme_jniSetImpl(Env, CallStaticBooleanMethodV);
	sjme_jniSetImpl(Env, CallStaticBooleanMethodA);
	sjme_jniSetImpl(Env, CallStaticByteMethod);
	sjme_jniSetImpl(Env, CallStaticByteMethodV);
	sjme_jniSetImpl(Env, CallStaticByteMethodA);
	sjme_jniSetImpl(Env, CallStaticCharMethod);
	sjme_jniSetImpl(Env, CallStaticCharMethodV);
	sjme_jniSetImpl(Env, CallStaticCharMethodA);
	sjme_jniSetImpl(Env, CallStaticShortMethod);
	sjme_jniSetImpl(Env, CallStaticShortMethodV);
	sjme_jniSetImpl(Env, CallStaticShortMethodA);
	sjme_jniSetImpl(Env, CallStaticIntMethod);
	sjme_jniSetImpl(Env, CallStaticIntMethodV);
	sjme_jniSetImpl(Env, CallStaticIntMethodA);
	sjme_jniSetImpl(Env, CallStaticLongMethod);
	sjme_jniSetImpl(Env, CallStaticLongMethodV);
	sjme_jniSetImpl(Env, CallStaticLongMethodA);
	sjme_jniSetImpl(Env, CallStaticFloatMethod);
	sjme_jniSetImpl(Env, CallStaticFloatMethodV);
	sjme_jniSetImpl(Env, CallStaticFloatMethodA);
	sjme_jniSetImpl(Env, CallStaticDoubleMethod);
	sjme_jniSetImpl(Env, CallStaticDoubleMethodV);
	sjme_jniSetImpl(Env, CallStaticDoubleMethodA);
	sjme_jniSetImpl(Env, CallStaticVoidMethod);
	sjme_jniSetImpl(Env, CallStaticVoidMethodV);
	sjme_jniSetImpl(Env, CallStaticVoidMethodA);
	sjme_jniSetImpl(Env, GetStaticFieldID);
	sjme_jniSetImpl(Env, GetStaticObjectField);
	sjme_jniSetImpl(Env, GetStaticBooleanField);
	sjme_jniSetImpl(Env, GetStaticByteField);
	sjme_jniSetImpl(Env, GetStaticCharField);
	sjme_jniSetImpl(Env, GetStaticShortField);
	sjme_jniSetImpl(Env, GetStaticIntField);
	sjme_jniSetImpl(Env, GetStaticLongField);
	sjme_jniSetImpl(Env, GetStaticFloatField);
	sjme_jniSetImpl(Env, GetStaticDoubleField);
	sjme_jniSetImpl(Env, SetStaticObjectField);
	sjme_jniSetImpl(Env, SetStaticBooleanField);
	sjme_jniSetImpl(Env, SetStaticByteField);
	sjme_jniSetImpl(Env, SetStaticCharField);
	sjme_jniSetImpl(Env, SetStaticShortField);
	sjme_jniSetImpl(Env, SetStaticIntField);
	sjme_jniSetImpl(Env, SetStaticLongField);
	sjme_jniSetImpl(Env, SetStaticFloatField);
	sjme_jniSetImpl(Env, SetStaticDoubleField);
	sjme_jniSetImpl(Env, NewString);
	sjme_jniSetImpl(Env, GetStringLength);
	sjme_jniSetImpl(Env, GetStringChars);
	sjme_jniSetImpl(Env, ReleaseStringChars);
	sjme_jniSetImpl(Env, NewStringUTF);
	sjme_jniSetImpl(Env, GetStringUTFLength);
	sjme_jniSetImpl(Env, GetStringUTFChars);
	sjme_jniSetImpl(Env, ReleaseStringUTFChars);
	sjme_jniSetImpl(Env, GetArrayLength);
	sjme_jniSetImpl(Env, NewObjectArray);
	sjme_jniSetImpl(Env, GetObjectArrayElement);
	sjme_jniSetImpl(Env, SetObjectArrayElement);
	sjme_jniSetImpl(Env, NewBooleanArray);
	sjme_jniSetImpl(Env, NewByteArray);
	sjme_jniSetImpl(Env, NewCharArray);
	sjme_jniSetImpl(Env, NewShortArray);
	sjme_jniSetImpl(Env, NewIntArray);
	sjme_jniSetImpl(Env, NewLongArray);
	sjme_jniSetImpl(Env, NewFloatArray);
	sjme_jniSetImpl(Env, NewDoubleArray);
	sjme_jniSetImpl(Env, GetBooleanArrayElements);
	sjme_jniSetImpl(Env, GetByteArrayElements);
	sjme_jniSetImpl(Env, GetCharArrayElements);
	sjme_jniSetImpl(Env, GetShortArrayElements);
	sjme_jniSetImpl(Env, GetIntArrayElements);
	sjme_jniSetImpl(Env, GetLongArrayElements);
	sjme_jniSetImpl(Env, GetFloatArrayElements);
	sjme_jniSetImpl(Env, GetDoubleArrayElements);
	sjme_jniSetImpl(Env, ReleaseBooleanArrayElements);
	sjme_jniSetImpl(Env, ReleaseByteArrayElements);
	sjme_jniSetImpl(Env, ReleaseCharArrayElements);
	sjme_jniSetImpl(Env, ReleaseShortArrayElements);
	sjme_jniSetImpl(Env, ReleaseIntArrayElements);
	sjme_jniSetImpl(Env, ReleaseLongArrayElements);
	sjme_jniSetImpl(Env, ReleaseFloatArrayElements);
	sjme_jniSetImpl(Env, ReleaseDoubleArrayElements);
	sjme_jniSetImpl(Env, GetBooleanArrayRegion);
	sjme_jniSetImpl(Env, GetByteArrayRegion);
	sjme_jniSetImpl(Env, GetCharArrayRegion);
	sjme_jniSetImpl(Env, GetShortArrayRegion);
	sjme_jniSetImpl(Env, GetIntArrayRegion);
	sjme_jniSetImpl(Env, GetLongArrayRegion);
	sjme_jniSetImpl(Env, GetFloatArrayRegion);
	sjme_jniSetImpl(Env, GetDoubleArrayRegion);
	sjme_jniSetImpl(Env, SetBooleanArrayRegion);
	sjme_jniSetImpl(Env, SetByteArrayRegion);
	sjme_jniSetImpl(Env, SetCharArrayRegion);
	sjme_jniSetImpl(Env, SetShortArrayRegion);
	sjme_jniSetImpl(Env, SetIntArrayRegion);
	sjme_jniSetImpl(Env, SetLongArrayRegion);
	sjme_jniSetImpl(Env, SetFloatArrayRegion);
	sjme_jniSetImpl(Env, SetDoubleArrayRegion);
	sjme_jniSetImpl(Env, RegisterNatives);
	sjme_jniSetImpl(Env, UnregisterNatives);
	sjme_jniSetImpl(Env, MonitorEnter);
	sjme_jniSetImpl(Env, MonitorExit);
	sjme_jniSetImpl(Env, GetJavaVM);
	sjme_jniSetImpl(Env, GetStringRegion);
	sjme_jniSetImpl(Env, GetStringUTFRegion);
	sjme_jniSetImpl(Env, GetPrimitiveArrayCritical);
	sjme_jniSetImpl(Env, ReleasePrimitiveArrayCritical);
	sjme_jniSetImpl(Env, GetStringCritical);
	sjme_jniSetImpl(Env, ReleaseStringCritical);
	sjme_jniSetImpl(Env, NewWeakGlobalRef);
	sjme_jniSetImpl(Env, DeleteWeakGlobalRef);
	sjme_jniSetImpl(Env, ExceptionCheck);
	sjme_jniSetImpl(Env, NewDirectByteBuffer);
	sjme_jniSetImpl(Env, GetDirectBufferAddress);
	sjme_jniSetImpl(Env, GetDirectBufferCapacity);
	sjme_jniSetImpl(Env, GetObjectRefType);
	sjme_jniSetImpl(Env, GetModule);

#undef sjme_jniSetImpl

	/* Success! */
	*pvm = (JavaVM*)resultJvm;
	*penv = resultEnv;
	return JNI_OK;

fail_nvmBoot:
fail_initTask:
fail_initClassLoader:
fail_searchClasspath:
fail_allocResultEnv:
	if (resultEnv != NULL)
		sjme_alloc_free(resultEnv);
fail_allocResultVm:
	if (resultJvm != NULL)
		sjme_alloc_free(resultJvm);
fail_nvmParseArgs:
fail_noMemory:

	/* Notice. */
	sjme_messageB("SquirrelJME Error: %d", error);

	if (error == SJME_ERROR_OUT_OF_MEMORY)
		return JNI_ENOMEM;
	return JNI_ERR;
}

jint JNICALL JNI_GetCreatedJavaVMs(JavaVM** vmBuf, jsize bufLen, jsize* nVMs)
{
	sjme_todo("Impl?");
}

/**
 * Obtains the default virtual machine configuration.
 * 
 * @param args A @c JavaVMInitArgs , the @c version field must be set before
 * this is called.
 * @return Either @c JNI_OK or an error such as if the Java version is not
 * supported.
 * @since 2024/03/18
 */
jint JNICALL JNI_GetDefaultJavaVMInitArgs(
	sjme_attrInOutNotNull void* args)
{
	JavaVMInitArgs* initArgs;

	if (args == NULL)
		return JNI_EINVAL;

	/* This is aliased under void. */
	initArgs = args;

	/* Either too old or too new. */
	if (initArgs->version < JNI_VERSION_1_1 ||
		initArgs->version > JNI_VERSION_1_8)
		return JNI_EVERSION;

	/* Indicate that we support this version. */
	initArgs->version = JNI_VERSION_1_8;

	/* Clear these. */
	initArgs->nOptions = 0;
	initArgs->options = NULL;

	/* Success! */
	return JNI_OK;
}
