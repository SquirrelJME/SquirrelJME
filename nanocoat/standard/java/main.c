/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <jni.h>
#include <string.h>

#include "sjme/debug.h"
#include "sjme/dylib.h"

/** Try multiple JNI versions. */
static jint tryJniVersions[] =
{
	JNI_VERSION_1_8,
	JNI_VERSION_1_6,
	JNI_VERSION_1_4,
	JNI_VERSION_1_2,
	JNI_VERSION_1_1,
	0
};

/**
 * Function for creating virtual machines.
 * 
 * @param outVm The resultant VM.
 * @param outEnv The resultant environment.
 * @param initArgs Any arguments.
 * @return @c JNI_OK on success, otherwise another error.
 * @since 2024/04/13
 */
typedef jint JNICALL (*jniCreateJavaVmFunc)(
	sjme_attrInOutNotNull JavaVM** outVm,
	sjme_attrInOutNotNull void** outEnv,
	sjme_attrInNotNull void* initArgs);

static sjme_lpcstr loadJvmConfig(sjme_lpcstr basePath, sjme_lpcstr subPath,
	sjme_lpcstr subFile, int argc, char** argv)
{
#define BUF_SIZE 512
	sjme_cchar fullPath[SJME_CONFIG_PATH_MAX];
	FILE* configFile;
	sjme_lpstr result, command, treatAs, trim;
	sjme_cchar fallback[BUF_SIZE];
	sjme_cchar line[BUF_SIZE];
	int i;
	
	/* Determine full path to config. */
	memset(fullPath, 0, sizeof(fullPath));
	snprintf(fullPath, SJME_CONFIG_PATH_MAX - 1,
		"%s/%s/%s", basePath, subPath, subFile);
	
	/* Debug. */
	sjme_message("Loading config at %s...", fullPath);
	
	/* Open config file. */
	configFile = fopen(fullPath, "rt");
	if (configFile == NULL)
		return NULL;
	
	/* Determine which switch to use. */
	result = NULL;
	memset(fallback, 0, sizeof(fallback));
	
	/* Parsing loop. */
	for (; result == NULL;)
	{
		/* Read in next line. */
		memset(line, 0, sizeof(line));
		if (NULL == fgets(line, BUF_SIZE - 1, configFile))
			break;
		
		/* Must always start with a dash. */
		if (line[0] != '-')
			continue;
		
		/* Command is always here. */
		command = &line[0];
		
		/* Find space which splits how this is treated. */
		treatAs = strchr(line, ' ');
		if (treatAs == NULL)
			treatAs = "KNOWN";
		else
		{
			*treatAs = 0;
			treatAs = treatAs + 1;
			
			/* Remove carriage return. */
			trim = strchr(treatAs, '\r');
			if (trim != NULL)
				*trim = 0;
				
			/* Remove newline. */
			trim = strchr(treatAs, '\n');
			if (trim != NULL)
				*trim = 0;
		}
		
		/* Use this? */
		if (strcmp(treatAs, "KNOWN") == 0)
		{
			/* Fallback not yet set? */
			if (fallback[0] == 0)
				strncpy(fallback, command + 1, BUF_SIZE - 1);
			
			/* Find used switch. */
			for (i = 1; i < argc; i++)
				if (strcmp(command, argv[i]) == 0)
				{
					result = argv[i] + 1;
					break;
				}
		}
	}
	
	/* Close config file. */
	fclose(configFile);
	
	/* Use given result, use fallback with default VM. */
	if (result == NULL && fallback[0] != 0)
		return strdup(fallback);
	return result;
#undef BUF_SIZE
}

/**
 * Attempts to load the JNI library at the given path.
 * 
 * @param basePath The base path.
 * @param subPath The sub-path to use.
 * @param libName The library name to use.
 * @return The resultant library if found.
 * @since 2024/04/13
 */
static sjme_dylib findLibJvmTry(sjme_lpcstr basePath, sjme_lpcstr subPath,
	sjme_lpcstr libName)
{
	sjme_errorCode error;
	sjme_cchar fullPath[SJME_CONFIG_PATH_MAX];
	sjme_dylib result;
	
	/* Determine full path to library. */
	memset(fullPath, 0, sizeof(fullPath));
	snprintf(fullPath, SJME_CONFIG_PATH_MAX - 1,
		"%s/%s/%s", basePath, subPath, libName);
	
	/* Load library. */
	result = NULL;
	if (sjme_error_is(error = sjme_dylib_open(fullPath,
		&result)) || result == NULL)
	{
		sjme_message("Did not find library at %s: %d",
			fullPath, error);
		return NULL;
	}
	
	/* Use this result. */
	return result;
}

/**
 * Locates the Java library.
 * 
 * @param argc Argument count. 
 * @param argv Arguments passed.
 * @return The Java library handle.
 * @since 2024/04/13 
 */
static sjme_dylib findLibJvm(int argc, char** argv)
{
	sjme_errorCode error;
	sjme_dylib result;
	sjme_lpcstr basePath, dyLibEnv;
	sjme_cchar libName[SJME_CONFIG_NAME_MAX];
	sjme_cchar subName[SJME_CONFIG_NAME_MAX];
	sjme_cchar subPath[SJME_CONFIG_PATH_MAX];
	sjme_lpcstr vmSwitch;
	
	/* Passed via dylib? */
	dyLibEnv = getenv("SQUIRRELJME_JAVA_DYLIB");
	if (dyLibEnv != NULL)
	{
		/* Is it here? */
		result = NULL;
		if (sjme_error_is(error = sjme_dylib_open(dyLibEnv,
			&result)) || result == NULL)
			return result;
	}
	
	/* Possible names are... */
	/* @c lib/libjvm.so . */
	memset(libName, 0, sizeof(libName));
	sjme_dylib_name("jvm",
		libName, SJME_CONFIG_NAME_MAX - 1);
	
	/* Try Java Home directory first. */
	basePath = getenv("SQUIRRELJME_JAVA_HOME");
	if (basePath == NULL)
		basePath = getenv("JAVA_HOME");
	
	/* Look here if it is known. */
	if (basePath != NULL)
	{
		/* Look here first. */
		result = findLibJvmTry(basePath, "lib", libName);
		if (result != NULL)
			return result;
		
		/* Find the VM we want to use. */
		vmSwitch = loadJvmConfig(basePath, "lib", "jvm.cfg",
			argc, argv);
		if (vmSwitch != NULL)
		{
			/* Calculate sub-path to use. */
			memset(subPath, 0, sizeof(subPath));
			snprintf(subPath, SJME_CONFIG_PATH_MAX - 1,
				"lib/%s", vmSwitch);
			
			/* (jvm.cfg) @c $VM/libjvm.so . */
			result = findLibJvmTry(basePath, subPath, libName);
			if (result != NULL)
				return result;
				
			/* Calculate sub-name to use. */
			memset(subName, 0, sizeof(subName));
			if (!sjme_error_is(sjme_dylib_name(vmSwitch,
				subName, SJME_CONFIG_NAME_MAX - 1)))
			{
				/* (jvm.cfg) @c lib$VM.so . */
				result = findLibJvmTry(basePath, "lib",
					subName);
				if (result != NULL)
					return result;
			}
		}
	}
	
	/* Just use standard library lookup. */
	result = NULL;
	if (sjme_error_is(error = sjme_dylib_open(libName,
		&result)) || result == NULL)
		return result;
	
	/* Not found. */
	return NULL;
}

/**
 * Main entry point.
 * 
 * @param argc Argument count. 
 * @param argv Arguments passed.
 * @return A standard exit code.
 * @since 2024/04/13
 */
int main(int argc, char** argv)
{
	sjme_errorCode error;
	sjme_dylib libJvm;
	jniCreateJavaVmFunc createFunc;
	JavaVM* vm;
	JNIEnv* env;
	jint eval;
	JavaVMInitArgs initArgs;
	JavaVMOption* vmOptions;
	sjme_lpcstr optArg, mainClass;
	jclass foundMainClass, foundStringClass;
	jmethodID mainMethod;
	jobjectArray mainArgs;
	jstring str;
	sjme_cchar classpath[SJME_CONFIG_PATH_MAX];
	sjme_lpstr dotSlash;
	int tryJniVersion, optionsN, i, argBase, argN;
	
	/* Setup options for forwarded arguments. */
	vmOptions = sjme_alloca(sizeof(*vmOptions) * (argc + 7));
	if (vmOptions == NULL)
	{
		sjme_die("Out of memory.");
		return EXIT_FAILURE;
	}
	
	/* Clear before using. */
	memset(vmOptions, 0, sizeof(*vmOptions) * (argc + 7));
	memset(classpath, 0, sizeof(classpath));
	
	/* The main class to use. */
	mainClass = NULL;
	
	/* Base argument parsing for classpath and otherwise. */
	argBase = argc;
	for (i = 1; i < argc; i++)
	{
		/* Argument to parse. */
		optArg = argv[i];
		
		/* Dump version info? */
		if (strcmp(optArg, "-version") == 0)
		{
			fprintf(stdout, "squirreljme version \"1.8.0\"\n");
			return EXIT_SUCCESS;
		}
		
		/* Load classpath. */
		else if (strcmp(optArg, "-classpath") == 0)
		{
			/* Get following. */
			if ((++i) >= argc)
			{
				sjme_die("Expected argument after -jar.");
				return EXIT_FAILURE;
			}
			
			/* Load in. */
			optArg = argv[i];
			
			/* Copy classpath over. */
			snprintf(classpath, SJME_CONFIG_PATH_MAX - 1,
				"-Djava.class.path=%s", optArg);
		}
		
		/* Load Jar and its main manifest. */
		else if (strcmp(optArg, "-jar") == 0)
		{
			/* Get following. */
			if ((++i) >= argc)
			{
				sjme_die("Expected argument after -jar.");
				return EXIT_FAILURE;
			}
			
			/* Load in. */
			optArg = argv[i];
			
			/* Copy classpath over. */
			snprintf(classpath, SJME_CONFIG_PATH_MAX - 1,
				"-Djava.class.path=%s", optArg);
			
			sjme_todo("-jar?");
		}
		
		/* Forward other options to the VM. */
		else if (optArg[0] == '-')
		{
			vmOptions[optionsN].optionString = optArg;
			optionsN++;
		}
		
		/* Stop processing, these would be main class and/or arguments. */
		else
		{
			/* This is the main class used. */
			mainClass = optArg;
			
			/* Argument base is after this one. */
			argBase = i + 1;
			break;
		}
	}
	
	/* No main class specified? */
	if (mainClass == NULL)
	{
		sjme_die("No main class specified.");
		return EXIT_FAILURE;
	}
	
	/* No classpath specified? */
	if (classpath[0] == 0)
	{
		sjme_die("No classpath specified.");
		return EXIT_FAILURE;
	}
	
	/* Find and load libjvm. */
	libJvm = findLibJvm(optionsN, argv);
	if (libJvm == NULL)
	{
		sjme_die("Could not find libjvm!");
		return EXIT_FAILURE;
	}

	/* Find function for creating the VM. */
	createFunc = NULL;
	if (sjme_error_is(error = sjme_dylib_lookup(libJvm,
		"JNI_CreateJavaVM", &createFunc) || createFunc == NULL))
	{
		sjme_die("Could not find JNI_CreateJavaVM(): %d", error);
		return EXIT_FAILURE;
	}
	
	/* Set classpath. */
	vmOptions[optionsN].optionString = classpath;
	optionsN++;
	
	/* Setup initial arguments. */
	memset(&initArgs, 0, sizeof(initArgs));
	initArgs.ignoreUnrecognized = JNI_FALSE;
	initArgs.options = vmOptions;
	initArgs.nOptions = optionsN;
	
	/* Try multiple versions. */
	eval = JNI_ERR;
	for (tryJniVersion = 0;; tryJniVersion++)
	{
		/* Failed. */
		if (tryJniVersions[tryJniVersion] == 0)
		{
			sjme_die("JNI_CreateJavaVM() failed: %d", eval);
			return EXIT_FAILURE;
		}
		
		/* Set this version. */
		initArgs.version = tryJniVersions[tryJniVersion];
		
		/* Create resultant VM. */
		vm = NULL;
		env = NULL;
		eval = createFunc(&vm, &env, &initArgs);
		
		/* Version not supported, try older version next. */
		if (eval == JNI_EVERSION)
			continue;
		
		/* General failure. */
		if (eval != JNI_OK || vm == NULL || env == NULL)
		{
			sjme_die("JNI_CreateJavaVM() failed: %d", eval);
			return EXIT_FAILURE;
		}
		
		/* Debug. */
		sjme_message("VM Created!");
		
		/* Success! Create no more! */
		break;
	}
	
	/* Find String class. */
	foundStringClass = (*env)->FindClass(env, "java/lang/String");
	if (foundStringClass == NULL)
	{
		sjme_die("Could not find String class.");
		return EXIT_FAILURE;
	}
	
	/* The main class needs to be translated. */
	while (NULL != (dotSlash = strchr(mainClass, '.')))
		*dotSlash = '/';
	
	/* Find main class. */
	foundMainClass = (*env)->FindClass(env, mainClass);
	if (foundMainClass == NULL)
	{
		sjme_die("Could not find main class: %s", mainClass);
		return EXIT_FAILURE;
	}
	
	/* Find main method. */
	mainMethod = (*env)->GetStaticMethodID(env, foundMainClass,
		"main", "([Ljava/lang/String;)V");
	if (mainMethod == NULL)
	{
		sjme_die("Could not find main method in %s.", mainClass);
		return EXIT_FAILURE;
	}
	
	/* Setup main arguments. */
	argN = argc - argBase;
	if (argN < 0)
		argN = 0;
	mainArgs = (*env)->NewObjectArray(env, argN, foundStringClass, NULL);
	if (mainArgs == NULL)
	{
		sjme_die("Could not allocate string array.");
		return EXIT_FAILURE;
	}
	
	/* Load in string values. */
	for (i = 0; i < argN; i++)
	{
		/* Create string for argument. */
		str = (*env)->NewStringUTF(env, argv[argBase + i]);
		if (str == NULL)
		{
			sjme_die("Could not create argument: %s", argv[argBase + i]);
			return EXIT_FAILURE;
		}
		
		/* Place it in. */
		(*env)->SetObjectArrayElement(env, mainArgs, i, str);
	}
	
	/* Call main. */
	(*env)->CallStaticVoidMethod(env, foundMainClass, mainMethod, mainArgs);
	
	/* Did an exception occur? */
	if ((*env)->ExceptionCheck(env))
	{
		/* Describe it. */
		(*env)->ExceptionDescribe(env);
		
		/* Fail now. */
		sjme_die("Exception thrown.");
		return EXIT_FAILURE;
	}
	
	/* Destroy the VM, this will block until all non-daemon threads exit. */
	return (*vm)->DestroyJavaVM(vm);
}
