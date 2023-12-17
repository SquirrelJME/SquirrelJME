/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/allocSizeOf.h"
#include "sjme/boot.h"
#include "sjme/debug.h"
#include "sjme/except.h"
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
	reservedSize = -1;
	if (SJME_IS_ERROR(error = sjme_alloc_sizeOf(
		SJME_ALLOC_SIZEOF_RESERVED_POOL, 0, &reservedSize)))
		return error;
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
	sjme_nvm_state** outState)
{
#define FIXED_SUITE_COUNT 16
	SJME_EXCEPT_VDEF;
	sjme_errorCode error;
	sjme_exceptTrace* trace;
	sjme_nvm_state* volatile result;
	sjme_rom_suite* volatile mergeSuites[FIXED_SUITE_COUNT];
	volatile sjme_jint numMergeSuites;
	
	if (param == NULL || outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Initialize trace. */
	trace = NULL;

SJME_EXCEPT_WITH(trace):
	/* Set up a reserved pool where all the data structures for the VM go... */
	/* But only if one does not exist. */
	if (reservedPool == NULL)
		if (SJME_IS_ERROR(error = sjme_nvm_allocReservedPool(mainPool,
			&reservedPool)))
			SJME_EXCEPT_TOSS(error);

	/* Allocate resultant state. */
	result = NULL;
	if (SJME_IS_ERROR(error = sjme_alloc(reservedPool,
		sizeof(*result), (void**)&result)) || result == NULL)
		SJME_EXCEPT_TOSS(error);

	/* Make a defensive copy of the boot parameters. */
	if (SJME_IS_ERROR(error = sjme_alloc_copy(reservedPool,
		sizeof(sjme_nvm_bootParam),
		(void**)&result->bootParamCopy, param)) ||
		result == NULL)
		SJME_EXCEPT_TOSS(error);

	/* Set parameters accordingly. */
	result->allocPool = mainPool;
	result->reservedPool = reservedPool;

	/* Initialize base for suite merging. */
	memset(mergeSuites, 0, sizeof(mergeSuites));
	numMergeSuites = 0;

	/* Process payload suites. */
	if (result->bootParamCopy->payload != NULL)
	{
		/* Scan accordingly. */
		if (SJME_IS_ERROR(error = sjme_rom_fromPayload(reservedPool,
			&mergeSuites[numMergeSuites],
			result->bootParamCopy->payload)))
			SJME_EXCEPT_TOSS(error);

		/* Was a suite generated? */
		if (mergeSuites[numMergeSuites] != NULL)
			numMergeSuites++;
	}

	/* If there is a virtual suite, move it in. */
	if (result->bootParamCopy->virtualSuite != NULL)
	{
		/* Make a virtual suite for this. */
		if (SJME_IS_ERROR(error = sjme_rom_newSuite(reservedPool,
			&mergeSuites[numMergeSuites],
			result->bootParamCopy->virtualSuite)))
			SJME_EXCEPT_TOSS(error);

		/* Was a suite generated? */
		if (mergeSuites[numMergeSuites] != NULL)
			numMergeSuites++;
	}

	/* No suites at all? Running with absolutely nothing??? */
	if (numMergeSuites <= 0)
		SJME_EXCEPT_TOSS(SJME_ERROR_NO_SUITES);

	/* Use the single suite only. */
	else if (numMergeSuites == 1)
		result->suite = mergeSuites[0];

	/* Merge everything into one. */
	else
	{
		/* Merge all the suites together into one. */
		if (SJME_IS_ERROR(error = sjme_rom_fromMerge(reservedPool,
			&result->suite, mergeSuites,
			numMergeSuites)) || result->suite == NULL)
			SJME_EXCEPT_TOSS(error);
	}

	/* Debug. */
	sjme_message("Main class: %s", result->bootParamCopy->mainClass);

	/* Spawn initial task which uses the main arguments. */
	if (SJME_JNI_TRUE)
		sjme_todo("sjme_nvm_boot()");
	
	/* Return newly created VM. */
	sjme_todo("sjme_nvm_boot()");
	return SJME_ERROR_NOT_IMPLEMENTED;

SJME_EXCEPT_FAIL:
	sjme_todo("Cleanup after failure.");
	return SJME_ERROR_NOT_IMPLEMENTED;

#undef FIXED_SUITE_COUNT
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
