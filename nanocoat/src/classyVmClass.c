/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/classyVmClass.h"
#include "sjme/util.h"
#include "sjme/nvm/instance.h"

sjme_errorCode sjme_nvm_vmClass_poolNamesLFromClassesA(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInValue sjme_jboolean countUp,
	sjme_attrOutNotNull sjme_list(sjme_nvm_stringPool_string)** outList,
	sjme_attrInPositive sjme_jint numClasses,
	sjme_attrInNotNull sjme_jclass* classes)
{
	sjme_errorCode error;
	sjme_jint i;
	sjme_jclass classy;
	sjme_list(sjme_nvm_stringPool_string)* result;

	if (inState == NULL || outList == NULL || classes == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (numClasses < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Doing nothing? */
	if (numClasses == 0)
	{
		*outList = NULL;
		return SJME_ERROR_NONE;
	}

	/* Allocate new list. */
	result = NULL;
	if (sjme_error_is(error = sjme_list_alloc(inState->allocPool,
		numClasses, &result, sjme_nvm_stringPool_string, 0)) ||
		result == NULL)
		return sjme_error_default(error);

	/* Copy over the input names. */
	for (i = 0; i < numClasses; i++)
	{
		/* Skip slot if class is missing. */
		classy = classes[i];
		if (classy == NULL)
			continue;

		/* No actual name to set? */
		if (classy->info->name == NULL)
			return SJME_ERROR_ILLEGAL_STATE;

		/* There may be a usage where we want to count, or not to count. */
		/* This depends on if this is used for debugging or not. */
		if (countUp)
			result->elements[i] = sjme_weakUp(classy->info->name);
		else
			result->elements[i] = classy->info->name;
	}

	/* Success! */
	*outList = result;
	return SJME_ERROR_NONE;
}
