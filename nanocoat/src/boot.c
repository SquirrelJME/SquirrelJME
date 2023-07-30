/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
static jboolean sjme_nvm_bootCombineRom(const sjme_nvm_bootConfig* config,
	sjme_static_libraries** outLibraries)
{
	if (config == NULL || outLibraries == NULL)
		return JNI_FALSE;
		
	/* Determine the total library count. */
	if (JNI_TRUE)
		sjme_todo("sjme_nvm_bootVirtualRom()");
		
	/* Allocate output result. */
	if (JNI_TRUE)
		sjme_todo("sjme_nvm_bootVirtualRom()");
	
	/* Bring in all of the libraries into the output. */
	if (JNI_TRUE)
		sjme_todo("sjme_nvm_bootVirtualRom()");

	/* Return the result. */
	sjme_todo("sjme_nvm_bootVirtualRom()");
	return JNI_FALSE;
}

jboolean sjme_nvm_boot(const sjme_nvm_bootConfig* config,
	sjme_nvm_state** outState, int argc, char** argv)
{
	sjme_nvm_state* result;
	
	if (config == NULL || outState == NULL)
		return JNI_FALSE;
		
	/* Allocate new result. */
	result = malloc(sizeof(*result));
	if (result == NULL)
		return JNI_FALSE;
	memset(result, 0, sizeof(*result));
	
	/* Copy the boot config over. */
	memmove(&result->bootConfig, config, sizeof(*config));
	
	/* Combine the input ROMs to a set of libraries. */
	if (!sjme_nvm_bootCombineRom(config, &result->libraries))
	{
		sjme_nvm_destroy(result, NULL);
		return JNI_FALSE;
	}
	
	/* Parse the command line arguments for options on running the VM. */
	if (JNI_TRUE)
		sjme_todo("sjme_nvm_boot()");
	
	/* Spawn initial task which uses the main arguments. */
	if (JNI_TRUE)
		sjme_todo("sjme_nvm_boot()");
	
	/* Return newly created VM. */
	sjme_todo("sjme_nvm_boot()");
	return JNI_FALSE;
}

jboolean sjme_nvm_destroy(sjme_nvm_state* state, jint* exitCode)
{
	if (state == NULL)
		return JNI_FALSE;
	
	/* Free sub-structures. */
	if (JNI_TRUE)
		sjme_todo("sjme_nvm_destroy()");
		
	/* Free main structure. */
	if (JNI_TRUE)
		sjme_todo("sjme_nvm_destroy()");
	
	/* Set exit code, if requested. */
	if (JNI_TRUE)
		sjme_todo("sjme_nvm_destroy()");
	
	/* Finished. */
	sjme_todo("sjme_nvm_destroy()");
	return JNI_FALSE;
}
