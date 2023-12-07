/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/boot.h"
#include "sjme/debug.h"
#include "sjme/nvm.h"

/**
 * Combines all of the input ROMs and provides a virtual ROM that contains
 * all of the libraries, this is so everything is within a single library
 * set.
 * 
 * @param config The input configuration.
 * @param outLibraries The output libraries.
 * @return If this was successful.
 * @since 2023/07/28
 */
static sjme_attrCheckReturn sjme_jboolean sjme_nvm_bootCombineRom(
	sjme_attrInNotNull const sjme_nvm_bootConfig* config,
	sjme_attrOutNotNull sjme_static_libraries** outLibraries)
{
	if (config == NULL || outLibraries == NULL)
		return SJME_JNI_FALSE;
		
	/* Determine the total library count. */
	if (SJME_JNI_TRUE)
		sjme_todo("sjme_nvm_bootVirtualRom()");
		
	/* Allocate output result. */
	if (SJME_JNI_TRUE)
		sjme_todo("sjme_nvm_bootVirtualRom()");
	
	/* Bring in all the libraries into the output. */
	if (SJME_JNI_TRUE)
		sjme_todo("sjme_nvm_bootVirtualRom()");

	/* Return the result. */
	sjme_todo("sjme_nvm_bootVirtualRom()");
	return SJME_JNI_FALSE;
}

sjme_jboolean sjme_nvm_boot(const sjme_nvm_bootConfig* config,
	sjme_nvm_state** outState, int argc, char** argv)
{
	sjme_nvm_state* result;
	
	if (config == NULL || outState == NULL)
		return SJME_JNI_FALSE;
		
	/* Allocate new result. */
	result = malloc(sizeof(*result));
	if (result == NULL)
		return SJME_JNI_FALSE;
	memset(result, 0, sizeof(*result));
	
	/* Copy the boot config over. */
	memmove(&result->bootConfig, config, sizeof(*config));
	
	/* Combine the input ROMs to a set of libraries. */
	if (!sjme_nvm_bootCombineRom(config, &result->libraries))
	{
		sjme_nvm_destroy(result, NULL);
		return SJME_JNI_FALSE;
	}
	
	/* Parse the command line arguments for options on running the VM. */
	if (SJME_JNI_TRUE)
		sjme_todo("sjme_nvm_boot()");
	
	/* Spawn initial task which uses the main arguments. */
	if (SJME_JNI_TRUE)
		sjme_todo("sjme_nvm_boot()");
	
	/* Return newly created VM. */
	sjme_todo("sjme_nvm_boot()");
	return SJME_JNI_FALSE;
}

sjme_jboolean sjme_nvm_destroy(sjme_nvm_state* state, sjme_jint* exitCode)
{
	if (state == NULL)
		return SJME_JNI_FALSE;
	
	/* Free sub-structures. */
	if (SJME_JNI_TRUE)
		sjme_todo("sjme_nvm_destroy()");
		
	/* Free main structure. */
	if (SJME_JNI_TRUE)
		sjme_todo("sjme_nvm_destroy()");
	
	/* Set exit code, if requested. */
	if (SJME_JNI_TRUE)
		sjme_todo("sjme_nvm_destroy()");
	
	/* Finished. */
	sjme_todo("sjme_nvm_destroy()");
	return SJME_JNI_FALSE;
}
