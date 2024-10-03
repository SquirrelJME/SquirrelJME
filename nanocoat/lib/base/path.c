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
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrInNegativeOnePositive sjme_jint inName,
	sjme_attrOutNullable sjme_lpcstr* outBase,
	sjme_attrOutNullable sjme_jint* outLen)
{
	if (inPath == NULL || (outBase == NULL && outLen == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward to full form. */
	return sjme_path_getNameF(inPath,
		inPathLen, inName,
		outBase, NULL,
		NULL, NULL,
		outLen, NULL);
}

sjme_errorCode sjme_path_getNameF(
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrInNegativeOnePositive sjme_jint inName,
	sjme_attrOutNullable sjme_lpcstr* outBase,
	sjme_attrOutNullable sjme_jint* outBaseDx,
	sjme_attrOutNullable sjme_lpcstr* outEnd,
	sjme_attrOutNullable sjme_jint* outEndDx,
	sjme_attrOutNullable sjme_jint* outLen,
	sjme_attrOutNullable sjme_jint* outCount)
{
	sjme_lpcstr at, end, base;
	sjme_lpcstr stop;
	sjme_jint len, rem, totalCount, frag;
	sjme_lpcstr rootBase, rootEnd;
	sjme_lpcstr nameBase, nameEnd;
	sjme_jboolean hit;
	
	if (inPath == NULL || (outBase == NULL && outBaseDx == NULL &&
		outEnd == NULL && outEndDx == NULL && outLen == NULL &&
		outCount == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if ((outCount == NULL) != (outBase != NULL || outBaseDx != NULL ||
		outEnd != NULL || outEndDx != NULL || outLen != NULL))
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (inPathLen < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (inName < -1)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (outCount != NULL && inName != INT32_MAX)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Stopping point is always here. */
	len = strlen(inPath);
	if (len < inPathLen)
		inPathLen = len;
	stop = &inPath[inPathLen];
	
	/* Initially clear the total count. */
	totalCount = 0;
	
	/* Check for the root component at the start of the path. */
	rootBase = NULL;
	rootEnd = NULL;
	for (at = &inPath[0], end = at; end <= stop;)
	{
		/* How long is the current path? What is left of it? */
		len = end - at;
		rem = stop - end;

#if 0 && defined(SJME_CONFIG_DEBUG)
		/* Debug. */
		sjme_message("Root look: %c", at[0]);
#endif
		
#if SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_DOS
		/* Does this look like a drive letter? */
		if (len >= 2 && (((at[0]) >= 'a' && (at[0]) <= 'z') ||
			((at[0]) >= 'A' && (at[0]) <= 'Z')) && at[1] == ':')
		{
			sjme_todo("Impl?");
		}
		
		/* Not possible, stop early. */
		if (len >= 3)
			break;
		
#elif SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_UNIX
		/* Does this start with slash? */
		if (len >= 1 && at[0] == '/')
		{
			/* Double slash? */
			if (rem > 0 && at[1] == '/')
			{
				rootBase = &at[0];
				rootEnd = &at[2];
				break;
			}
			
			/* Single slash. */
			else
			{
				rootBase = &at[0];
				rootEnd = &at[1];
				break;
			}
		}
		
		/* Not possible, stop early. */
		if (len >= 3)
			break;
#else
		return sjme_error_notImplemented(0);
#endif
		
		/* Did not find, increment up. */
		end++;
	}

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("Root path found: `%.*s` <- `%s`",
		(int)(rootEnd - rootBase), rootBase, inPath);
#endif
	
	/* Did we want the root component? */
	if (inName == -1)
	{
		/* There was none. */
		if (rootBase == NULL || rootEnd == NULL)
			return SJME_ERROR_NO_SUCH_ELEMENT;
		
		/* Calculate outputs. */
		if (outBase != NULL)
			*outBase = rootBase;
		if (outBaseDx != NULL)
			*outBaseDx = rootBase - inPath;
		if (outEnd != NULL)
			*outEnd = rootEnd;
		if (outEndDx != NULL)
			*outEndDx = rootEnd - inPath;
		if (outLen != NULL)
			*outLen = rootEnd - rootBase;
		
		/* Success! */
		return SJME_ERROR_NONE;
	}
	
	/* Start from the path unless a root was specified. */
	at = (rootEnd != NULL ? rootEnd : &inPath[0]);
	
	/* Scan through remaining components. */
	hit = SJME_JNI_FALSE;
	nameBase = NULL;
	nameEnd = NULL;
	for (base = at, end = at; end <= stop;)
	{
		/* How long is the current path? What is left of it? */
		len = end - at;
		rem = stop - end;
		frag = -1;
		
#if 0 && defined(SJME_CONFIG_DEBUG)
		/* Debug. */
		sjme_message("Name look: %c", end[0]);
#endif
		
		/* Force hit on NUL? */
		if (end[0] == '\0')
		{
			hit = SJME_JNI_TRUE;
			frag = 1;
		}
		
		/* Otherwise check directory character. */
		else
		{
#if SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_DOS
			/* Directory specifier? */
			hit = (end[0] == '/' || end[0] == '\\');
			frag = 1;
		
#elif SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_UNIX
		/* Directory specifier? */
			hit = (end[0] == '/');
			frag = 1;
#else
			return sjme_error_notImplemented(0);
#endif
		}
		
		/* Did we hit a directory split? */
		if (hit)
		{
			/* Should be set. */
			if (frag <= 0)
				return SJME_ERROR_ILLEGAL_STATE;
			
			/* Determine name locations. */
			nameBase = base;
			nameEnd = end;
			
#if defined(SJME_CONFIG_DEBUG)
			/* Debug. */
			sjme_message("Name found %d: `%.*s` <- `%s`",
				totalCount, (int)(nameEnd - nameBase), nameBase,
				inPath);
#endif
			
			/* Is this the one we want? */
			if (inName == totalCount)
			{
				/* Calculate outputs. */
				if (outBase != NULL)
					*outBase = nameBase;
				if (outBaseDx != NULL)
					*outBaseDx = nameBase - inPath;
				if (outEnd != NULL)
					*outEnd = nameEnd;
				if (outEndDx != NULL)
					*outEndDx = nameEnd - inPath;
				if (outLen != NULL)
					*outLen = nameEnd - nameBase;
				
				/* Success! */
				return SJME_ERROR_NONE;
			}
			
			/* Count up and set new pointer regions. */
			totalCount++;
			base = &end[frag];
			at = base;
			end = base;
			
			/* Do not let normal followup run. */
			continue;
		}
		
		/* Did not find, increment up. */
		end++;
	}
	
	/* Success! */
	if (outCount != NULL)
		*outCount = totalCount;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_path_getNameCount(
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrOutNotNull sjme_attrOutPositive sjme_jint* outCount)
{
	sjme_errorCode error;
	sjme_jint result;
	
	if (inPath == NULL || outCount == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inPathLen < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Use the pathname get to determine the root count. */
	result = -1;
	if (sjme_error_is(error = sjme_path_getNameF(inPath, inPathLen,
		INT32_MAX, NULL, NULL,
		NULL, NULL, NULL, &result)) ||
		result < 0)
		return sjme_error_default(error);
	
	/* Give the count. */
	*outCount = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_path_hasRoot(
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrOutNotNull sjme_jboolean* hasRoot)
{
	sjme_errorCode error;
	sjme_jint len;
	
	if (inPath == NULL || hasRoot == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inPathLen < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Try to get the root. */
	len = -1;
	if (sjme_error_is(error = sjme_path_getNameF(inPath, inPathLen,
		-1,
		NULL, NULL, NULL, NULL,
		&len, NULL)) || len <= 0)
	{
		/* Does not have one? */
		if (error == SJME_ERROR_NO_SUCH_ELEMENT)
		{
			*hasRoot = SJME_JNI_TRUE;
			return SJME_ERROR_NONE;
		}
		
		/* Other failure. */
		return sjme_error_default(error);
	}
	
	/* If this was reached, there is none. */
	*hasRoot = SJME_JNI_FALSE;
	return SJME_ERROR_NONE;
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
	sjme_jint subBaseLen, sepLen;
	
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
	
	/* Nothing to do? */
	if (subLen == 0)
		return SJME_ERROR_NONE;
	
	/* Pre-determine if this will overflow. */
	if (outLen < 0 || subLen < 0 || (outLen + subLen) < 0 ||
		(outLen + subLen) + 1 > outPathLen)
		return SJME_ERROR_PATH_TOO_LONG;
	
	/* How many names does the sub-path have? */
	subNames = -1;
	if (sjme_error_is(error = sjme_path_getNameCount(subPath,
		subPathLen, &subNames)) || subNames < 0)
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
			if (sjme_error_is(error = sjme_path_getName(
				subPath, subPathLen,
				subName, &subBase, &subBaseLen)) ||
				subBase == NULL || subBaseLen < 0)
			{
				/* Ignore missing root. */
				if (subName == -1 && error == SJME_ERROR_NO_SUCH_ELEMENT)
					continue;
				
				/* Otherwise fail. */
				return sjme_error_default(error);
			}
			
			/* This has a root component, so make it absolute. */
			if (subName == -1)
			{
#if defined(SJME_CONFIG_DEBUG)
				/* Debug. */
				sjme_message("Set root: `%.*s`",
					subBaseLen, subBase);
#endif
				
				/* Copy over. */
				memset(result, 0, sizeof(*result) * outPathLen);
				memmove(result, subBase,
					sizeof(*result) * subBaseLen);
				
				continue;
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
		/* Gets single subcomponent details. */
		subBase = NULL;
		subBaseLen = -1;
		if (sjme_error_is(error = sjme_path_getName(
			subPath, subPathLen,
			0, &subBase, &subBaseLen)) ||
			subBase == NULL || subBaseLen < 0)
			return sjme_error_default(error);
		
#if defined(SJME_CONFIG_DEBUG)
		/* Debug. */
		sjme_message("Append single: `%.*s`",
			subBaseLen, subBase);
#endif
		
		/* Recalculate output length. */
		outLen = strlen(result);
		
		/* Add directory separator. */
		sepLen = strlen(SJME_CONFIG_FILE_SEPARATOR) + 1; 
		memmove(&result[outLen], SJME_CONFIG_FILE_SEPARATOR,
			sizeof(*result) * sepLen);
			
		/* Recalculate output length. */
		outLen = strlen(result);
		
		/* Append it. */
		memset(&result[outLen], 0, sizeof(*subBase) * subBaseLen); 
		memmove(&result[outLen],
			subBase, sizeof(*subBase) * subBaseLen);
	}
	
	/* Debug. */
#if defined(SJME_CONFIG_DEBUG)
	sjme_message("resolve(%.*s, %.*s) -> %.*s",
		outPathLen, outPath, subPathLen, subPath,
			(int)strlen(result), result);
#endif
	
	/* Success! Copy resultant path. */
	outLen = strlen(result) + 1;
	memmove(outPath, result, sizeof(*result) * outLen);
	return SJME_ERROR_NONE;
}
