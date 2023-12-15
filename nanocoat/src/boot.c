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

sjme_errorCode sjme_nvm_allocReservedPool(
	sjme_attrInNotNull sjme_alloc_pool* mainPool,
	sjme_attrOutNotNull sjme_alloc_pool** outReservedPool)
{
	sjme_errorCode error;
	void* reservedBase;
	sjme_alloc_pool* reservedPool;
	sjme_jint reservedSize;

	if (mainPool == NULL || outReservedPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Determine how big the reserved pool should be... */
	reservedBase = NULL;
	reservedSize = 64 * 1024;
	if (SJME_IS_ERROR(error = sjme_alloc(mainPool,
		reservedSize, (void**)&reservedBase) ||
		reservedBase == NULL))
		return error;

	/* Initialize a reserved pool where all of our own data structures go. */
	reservedPool = NULL;
	if (SJME_IS_ERROR(error = sjme_alloc_poolInitStatic(
		&reservedPool, reservedBase, reservedSize)) ||
		reservedPool == NULL)
		return error;

	/* Use the resultant pool. */
	*outReservedPool = reservedPool;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_boot(sjme_alloc_pool* mainPool,
	sjme_alloc_pool* reservedPool, const sjme_nvm_bootParam* param,
	sjme_nvm_state** outState, int argc, char** argv)
{
	sjme_nvm_state* result;
	sjme_errorCode error;
	
	if (param == NULL || outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Set up a reserved pool where all the data structures for the VM go... */
	/* But only if one does not exist. */
	if (reservedPool == NULL)
		if (SJME_IS_ERROR(error = sjme_nvm_allocReservedPool(mainPool,
			&reservedPool)))
			return error;

	/* Allocate resultant state. */
	result = NULL;
	if (SJME_IS_ERROR(error = sjme_alloc(reservedPool,
		sizeof(*result), (void**)&result)) || result == NULL)
		return error;

	/* Make a defensive copy of the boot parameters. */
	if (SJME_IS_ERROR(error = sjme_alloc_copy(reservedPool,
		sizeof(sjme_nvm_bootParam),
		(void**)&result->bootParamCopy, param)) ||
		result == NULL)
		return error;

	/* Set parameters accordingly. */
	result->allocPool = mainPool;
	result->reservedPool = reservedPool;

	/* Determine the full set of suites that are available for merging. */
	if (SJME_JNI_TRUE)
		sjme_todo("sjme_nvm_boot()");
	
	/* Parse the command line arguments for options on running the VM. */
	if (SJME_JNI_TRUE)
		sjme_todo("sjme_nvm_boot()");
	
	/* Spawn initial task which uses the main arguments. */
	if (SJME_JNI_TRUE)
		sjme_todo("sjme_nvm_boot()");
	
	/* Return newly created VM. */
	sjme_todo("sjme_nvm_boot()");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_nvm_destroy(sjme_nvm_state* state, sjme_jint* exitCode)
{
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
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
	return SJME_ERROR_NOT_IMPLEMENTED;
}
