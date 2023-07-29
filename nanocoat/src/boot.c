/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

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
	
	sjme_todo("sjme_nvm_boot()");
	return JNI_FALSE;
}

jboolean sjme_nvm_destroy(sjme_nvm_state* state, jint* exitCode)
{
	if (state == NULL)
		return JNI_FALSE;
	
	sjme_todo("sjme_nvm_destroy()");
	return JNI_FALSE;
}
