/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/path.h"
#include "sjme/debug.h"

sjme_errorCode sjme_path_getName(
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrInNegativeOnePositive sjme_jint inName,
	sjme_attrOutNullable sjme_lpcstr* outBase,
	sjme_attrOutNullable sjme_jint* outBaseDx,
	sjme_attrOutNullable sjme_lpcstr* outEnd,
	sjme_attrOutNullable sjme_jint* outEndDx,
	sjme_attrOutNullable sjme_jint* outLen)
{
	if (inPath == NULL || (outBase == NULL && outBaseDx == NULL &&
		outEnd == NULL && outEndDx == NULL && outLen == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inName < -1)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_path_getNameCount(
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrOutNotNull sjme_attrOutPositive sjme_jint* outCount)
{
	if (inPath == NULL || outCount == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_path_hasRoot(
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrOutNotNull sjme_jboolean* hasRoot)
{
	if (inPath == NULL || hasRoot == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_path_resolveAppend(
	sjme_attrOutNotNull sjme_lpstr outPath,
	sjme_attrInPositiveNonZero sjme_jint outPathLen,
	sjme_attrInNotNull sjme_lpcstr subPath,
	sjme_attrInPositiveNonZero sjme_jint subPathLen)
{
	sjme_errorCode error;
	sjme_jint outLen, subLen;
	sjme_jint subNames, subName;
	sjme_lpstr result;
	sjme_jint resultBytes;
	sjme_lpcstr subBase;
	sjme_jint subBaseLen;
	
	if (outPath == NULL || subPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (outPathLen <= 0 || subBaseLen < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Nothing to do? */
	if (subBaseLen == 0)
		return SJME_ERROR_NONE;
	
	/* Need to check how many characters to potentially add. */
	outLen = strlen(outPath);
	subLen = strlen(subPath);
	
	/* Special? */
	if (subPathLen == INT32_MAX)
		subPathLen = subLen;
	
	/* Otherwise limit accordingly. */
	else if (subLen > subPathLen)
		subLen = subPathLen;
	
	/* Pre-determine if this will overflow. */
	if (outLen < 0 || subLen < 0 || (outLen + subLen) < 0 ||
		(outLen + subLen) + 1 > outPathLen)
		return SJME_ERROR_PATH_TOO_LONG;
	
	/* How many names does the sub-path have? */
	subNames = -1;
	if (sjme_error_is(error = sjme_path_getNameCount(subPath,
		&subNames)) || subNames < 0)
		return error;
	
	/* Pointless? */
	if (subNames == 0)
		return SJME_ERROR_NONE;
	
	/* Setup result for no-overwrite operation. */
	resultBytes = sizeof(*result) * outPathLen;
	result = sjme_alloca(resultBytes);
	if (result == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(result, 0, resultBytes);
	memmove(result, outPath, sizeof(*result) * outLen);
	
	/* Multiple names? */
	if (subNames > 1)
	{
		/* Append individual name components. */
		for (subName = -1; subName < subNames; subName++)
		{
			/* Get subcomponent to add individually. */
			subBase = NULL;
			subBaseLen = -1;
			if (sjme_error_is(error = sjme_path_getName(subPath,
				subName, &subBase, NULL,
				NULL, NULL, &subBaseLen)) ||
				subBase == NULL || subBaseLen < 0)
			{
				/* Ignore if we asked for the root. */
				if (subName == -1 && error == SJME_ERROR_NO_SUCH_ELEMENT)
					continue;
				
				/* Otherwise fail. */
				return sjme_error_default(error);
			}
			
			/* Append individual path. */
			if (sjme_error_is(error = sjme_path_resolveAppend(result,
				outPathLen, subBase, subBaseLen)))
				return sjme_error_default(error);
		}
	}
	
	/* Single path only. */
	else
	{
		return sjme_error_notImplemented(0);
	}
	
	/* Success! Copy resultant path. */
	outLen = strlen(result);
	memmove(outPath, result, sizeof(*result) * outLen);
	return SJME_ERROR_NONE;
}
