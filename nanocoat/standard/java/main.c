/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <jni.h>

#include "sjme/debug.h"
#include "sjme/dylib.h"

/**
 * Locates the Java library.
 * 
 * @return The Java library path.
 * @since 2024/04/13 
 */
static sjme_lpcstr findLibJvm()
{
	sjme_lpcstr javaHome;
	
	/* Try Java Home directory first. */
	javaHome = getenv("SQUIRRELJME_JAVA_HOME");
	if (javaHome == NULL)
		javaHome = getenv("JAVA_HOME");
		
	/* Possible names are... */
	/* @c lib/libjvm.so . */
	/* (jvm.cfg) @c $VM/libjvm.so . */
	/* (jvm.cfg) @c lib$VM.so . */
	
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
	sjme_lpcstr libJvmPath;
	sjme_dylib libJvm;
	
	/* Find the location of libjvm. */
	libJvmPath = findLibJvm();
	if (libJvmPath == NULL)
	{
		sjme_die("Could not find libjvm!");
		return EXIT_FAILURE;
	}
	
	/* Load library. */
	libJvm = NULL;
	if (sjme_error_is(error = sjme_dylib_open(libJvmPath,
		&libJvm)) || libJvm == NULL)
	{
		sjme_die("Unable to load Java library!");
		return EXIT_FAILURE;
	}
	
	sjme_todo("Impl?");
}
