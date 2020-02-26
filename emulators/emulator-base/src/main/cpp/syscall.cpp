/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

#include <stdio.h>

#include "jni.h"
#include "cc_squirreljme_jvm_Assembly.h"

/***************************** ACTUAL SYSTEM CALLS ***************************/

JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallVL__SIIIIIIII
	(JNIEnv* env, jclass classy, jshort si, jint a, jint b, jint c, jint d,
	jint e, jint f, jint g, jint h)
{
	jclass jvmFunctionClassy;
	jmethodID jvmSystemCallMethod;

	// Locate class
	jvmFunctionClassy = env->FindClass("cc/squirreljme/jvm/JVMFunction");

	if (jvmFunctionClassy == NULL)
	{
		env->ThrowNew(env->FindClass("java/lang/RuntimeException"),
			"Could not find JVMFunction class.");
		return 0;
	}

	// Locate method
	jvmSystemCallMethod = env->GetStaticMethodID(
		jvmFunctionClassy, "jvmSystemCall", "(SIIIIIIII)J");

	if (jvmSystemCallMethod == NULL)
	{
		env->ThrowNew(env->FindClass("java/lang/RuntimeException"),
			"Could not find JVMFunction.jvmSystemCall method.");
		return 0;
	}

	// These are un-pure system calls and as such will call jvmSystemCall
	// to handle the system call
	return env->CallStaticLongMethod(jvmFunctionClassy, jvmSystemCallMethod,
		si, a, b, c, d, e, f, g, h);
}


JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPVL__SIIIIIIII
	(JNIEnv* env, jclass classy, jshort si, jint a, jint b, jint c, jint d,
	jint e, jint f, jint g, jint h)
{
	jclass emulatorAssemblyClassy;
	jmethodID emulatorSystemCallClassy;

	// Locate class
	emulatorAssemblyClassy = env->FindClass(
		"cc/squirreljme/emulator/EmulatorAssembly");

	if (emulatorAssemblyClassy == NULL)
	{
		env->ThrowNew(env->FindClass("java/lang/RuntimeException"),
			"Could not find EmulatorAssembly class.");
		return 0;
	}

	// Locate method
	emulatorSystemCallClassy = env->GetStaticMethodID(
		emulatorAssemblyClassy, "systemCall", "(SIIIIIIII)J");

	if (emulatorSystemCallClassy == NULL)
	{
		env->ThrowNew(env->FindClass("java/lang/RuntimeException"),
			"Could not find EmulatorAssembly.systemCall method.");
		return 0;
	}

	// Handle system call
	switch (si)
	{
			// System call not implemented in native code, forward to Java
			// based handler.
		default:
			return env->CallStaticLongMethod(emulatorAssemblyClassy,
				emulatorSystemCallClassy, si, a, b, c, d, e, f, g, h);
	}
}

/*************************** FINAL LEVEL FORWARDED ***************************/

JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCall__SIIIIIIII
	(JNIEnv* env, jclass classy, jshort si, jint a, jint b, jint c, jint d,
	jint e, jint f, jint g, jint h)
{
	Java_cc_squirreljme_jvm_Assembly_sysCallVL__SIIIIIIII(
		env, classy, si, a, b, c, d, e, f, g, h);
}

JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallV__SIIIIIIII
	(JNIEnv* env, jclass classy, jshort si, jint a, jint b, jint c, jint d,
	jint e, jint f, jint g, jint h)
{
	return (jint)Java_cc_squirreljme_jvm_Assembly_sysCallVL__SIIIIIIII(
		env, classy, si, a, b, c, d, e, f, g, h);
}

JNIEXPORT void JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallP__SIIIIIIII
	(JNIEnv* env, jclass classy, jshort si, jint a, jint b, jint c, jint d,
	jint e, jint f, jint g, jint h)
{
	Java_cc_squirreljme_jvm_Assembly_sysCallPVL__SIIIIIIII(
		env, classy, si, a, b, c, d, e, f, g, h);
}

JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_sysCallPV__SIIIIIIII
	(JNIEnv* env, jclass classy, jshort si, jint a, jint b, jint c, jint d,
	jint e, jint f, jint g, jint h)
{
	return (jint)Java_cc_squirreljme_jvm_Assembly_sysCallPVL__SIIIIIIII(
		env, classy, si, a, b, c, d, e, f, g, h);
}

/*************************** FORWARDED SYSTEM CALLS **************************/

// Declaration of arguments
#define ARGDECL_ZERO JNIEnv* env, jclass classy, jshort si
#define ARGDECL_ONE ARGDECL_ZERO, jint a
#define ARGDECL_TWO ARGDECL_ONE, jint b
#define ARGDECL_THREE ARGDECL_TWO, jint c
#define ARGDECL_FOUR ARGDECL_THREE, jint d
#define ARGDECL_FIVE ARGDECL_FOUR, jint e
#define ARGDECL_SIX ARGDECL_FIVE, jint f
#define ARGDECL_SEVEN ARGDECL_SIX, jint g
#define ARGDECL_EIGHT ARGDECL_SEVEN, jint h

// Arguments to forward to the other function
#define ARGBASE_BASE	env, classy, si
#define ARGPASS_ZERO	ARGBASE_BASE, 0, 0, 0, 0, 0, 0, 0, 0
#define ARGPASS_ONE		ARGBASE_BASE, a, 0, 0, 0, 0, 0, 0, 0
#define ARGPASS_TWO		ARGBASE_BASE, a, b, 0, 0, 0, 0, 0, 0
#define ARGPASS_THREE	ARGBASE_BASE, a, b, c, 0, 0, 0, 0, 0
#define ARGPASS_FOUR	ARGBASE_BASE, a, b, c, d, 0, 0, 0, 0
#define ARGPASS_FIVE	ARGBASE_BASE, a, b, c, d, e, 0, 0, 0
#define ARGPASS_SIX		ARGBASE_BASE, a, b, c, d, e, f, 0, 0
#define ARGPASS_SEVEN	ARGBASE_BASE, a, b, c, d, e, f, g, 0
#define ARGPASS_EIGHT	ARGBASE_BASE, a, b, c, d, e, f, g, h

// Returning a value or not?
#define RETURN return
#define NO_RETURN

// Glues name together
#define GLUE_NAME(name) Java_cc_squirreljme_jvm_Assembly_sysCall##name

// Builds the entire declaration
#define BUILD(returnToken, returnType, name, toName, argDecl, argPass) \
	JNIEXPORT returnType JNICALL GLUE_NAME(name) \
		(argDecl) \
	{ \
		returnToken GLUE_NAME(toName) (argPass); \
	}

BUILD(NO_RETURN, void, __S, __SIIIIIIII, ARGDECL_ZERO, ARGPASS_ZERO)
BUILD(NO_RETURN, void, __SI, __SIIIIIIII, ARGDECL_ONE, ARGPASS_ONE)
BUILD(NO_RETURN, void, __SII, __SIIIIIIII, ARGDECL_TWO, ARGPASS_TWO)
BUILD(NO_RETURN, void, __SIII, __SIIIIIIII, ARGDECL_THREE, ARGPASS_THREE)
BUILD(NO_RETURN, void, __SIIII, __SIIIIIIII, ARGDECL_FOUR, ARGPASS_FOUR)
BUILD(NO_RETURN, void, __SIIIII, __SIIIIIIII, ARGDECL_FIVE, ARGPASS_FIVE)
BUILD(NO_RETURN, void, __SIIIIII, __SIIIIIIII, ARGDECL_SIX, ARGPASS_SIX)
BUILD(NO_RETURN, void, __SIIIIIII, __SIIIIIIII, ARGDECL_SEVEN, ARGPASS_SEVEN)

BUILD(RETURN, jint, V__S, V__SIIIIIIII, ARGDECL_ZERO, ARGPASS_ZERO)
BUILD(RETURN, jint, V__SI, V__SIIIIIIII, ARGDECL_ONE, ARGPASS_ONE)
BUILD(RETURN, jint, V__SII, V__SIIIIIIII, ARGDECL_TWO, ARGPASS_TWO)
BUILD(RETURN, jint, V__SIII, V__SIIIIIIII, ARGDECL_THREE, ARGPASS_THREE)
BUILD(RETURN, jint, V__SIIII, V__SIIIIIIII, ARGDECL_FOUR, ARGPASS_FOUR)
BUILD(RETURN, jint, V__SIIIII, V__SIIIIIIII, ARGDECL_FIVE, ARGPASS_FIVE)
BUILD(RETURN, jint, V__SIIIIII, V__SIIIIIIII, ARGDECL_SIX, ARGPASS_SIX)
BUILD(RETURN, jint, V__SIIIIIII, V__SIIIIIIII, ARGDECL_SEVEN, ARGPASS_SEVEN)

BUILD(RETURN, jlong, VL__S, VL__SIIIIIIII, ARGDECL_ZERO, ARGPASS_ZERO)
BUILD(RETURN, jlong, VL__SI, VL__SIIIIIIII, ARGDECL_ONE, ARGPASS_ONE)
BUILD(RETURN, jlong, VL__SII, VL__SIIIIIIII, ARGDECL_TWO, ARGPASS_TWO)
BUILD(RETURN, jlong, VL__SIII, VL__SIIIIIIII, ARGDECL_THREE, ARGPASS_THREE)
BUILD(RETURN, jlong, VL__SIIII, VL__SIIIIIIII, ARGDECL_FOUR, ARGPASS_FOUR)
BUILD(RETURN, jlong, VL__SIIIII, VL__SIIIIIIII, ARGDECL_FIVE, ARGPASS_FIVE)
BUILD(RETURN, jlong, VL__SIIIIII, VL__SIIIIIIII, ARGDECL_SIX, ARGPASS_SIX)
BUILD(RETURN, jlong, VL__SIIIIIII, VL__SIIIIIIII, ARGDECL_SEVEN, ARGPASS_SEVEN)

BUILD(NO_RETURN, void, P__S, P__SIIIIIIII, ARGDECL_ZERO, ARGPASS_ZERO)
BUILD(NO_RETURN, void, P__SI, P__SIIIIIIII, ARGDECL_ONE, ARGPASS_ONE)
BUILD(NO_RETURN, void, P__SII, P__SIIIIIIII, ARGDECL_TWO, ARGPASS_TWO)
BUILD(NO_RETURN, void, P__SIII, P__SIIIIIIII, ARGDECL_THREE, ARGPASS_THREE)
BUILD(NO_RETURN, void, P__SIIII, P__SIIIIIIII, ARGDECL_FOUR, ARGPASS_FOUR)
BUILD(NO_RETURN, void, P__SIIIII, P__SIIIIIIII, ARGDECL_FIVE, ARGPASS_FIVE)
BUILD(NO_RETURN, void, P__SIIIIII, P__SIIIIIIII, ARGDECL_SIX, ARGPASS_SIX)
BUILD(NO_RETURN, void, P__SIIIIIII, P__SIIIIIIII, ARGDECL_SEVEN, ARGPASS_SEVEN)

BUILD(RETURN, jint, PV__S, PV__SIIIIIIII, ARGDECL_ZERO, ARGPASS_ZERO)
BUILD(RETURN, jint, PV__SI, PV__SIIIIIIII, ARGDECL_ONE, ARGPASS_ONE)
BUILD(RETURN, jint, PV__SII, PV__SIIIIIIII, ARGDECL_TWO, ARGPASS_TWO)
BUILD(RETURN, jint, PV__SIII, PV__SIIIIIIII, ARGDECL_THREE, ARGPASS_THREE)
BUILD(RETURN, jint, PV__SIIII, PV__SIIIIIIII, ARGDECL_FOUR, ARGPASS_FOUR)
BUILD(RETURN, jint, PV__SIIIII, PV__SIIIIIIII, ARGDECL_FIVE, ARGPASS_FIVE)
BUILD(RETURN, jint, PV__SIIIIII, PV__SIIIIIIII, ARGDECL_SIX, ARGPASS_SIX)
BUILD(RETURN, jint, PV__SIIIIIII, PV__SIIIIIIII, ARGDECL_SEVEN, ARGPASS_SEVEN)

BUILD(RETURN, jlong, PVL__S, PVL__SIIIIIIII, ARGDECL_ZERO, ARGPASS_ZERO)
BUILD(RETURN, jlong, PVL__SI, PVL__SIIIIIIII, ARGDECL_ONE, ARGPASS_ONE)
BUILD(RETURN, jlong, PVL__SII, PVL__SIIIIIIII, ARGDECL_TWO, ARGPASS_TWO)
BUILD(RETURN, jlong, PVL__SIII, PVL__SIIIIIIII, ARGDECL_THREE, ARGPASS_THREE)
BUILD(RETURN, jlong, PVL__SIIII, PVL__SIIIIIIII, ARGDECL_FOUR, ARGPASS_FOUR)
BUILD(RETURN, jlong, PVL__SIIIII, PVL__SIIIIIIII, ARGDECL_FIVE, ARGPASS_FIVE)
BUILD(RETURN, jlong, PVL__SIIIIII, PVL__SIIIIIIII, ARGDECL_SIX, ARGPASS_SIX)
BUILD(RETURN, jlong, PVL__SIIIIIII, PVL__SIIIIIIII,
	ARGDECL_SEVEN, ARGPASS_SEVEN)

