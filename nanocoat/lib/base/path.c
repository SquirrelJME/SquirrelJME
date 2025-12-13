/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/path.h"
#include "sjme/debug.h"

#if defined(SJME_CONFIG_HAS_OS_PC_DOS)
	/** No concept of user home exists in DOS. */
	#define SJME_PATH_PSEUDO_HOME "C:\\SQUIRREL.JME"
#elif defined(SJME_CONFIG_HAS_OS_PALMOS)
	/** No concept of user home exists in PalmOS. */
	#define SJME_PATH_PSEUDO_HOME "0:/PALM/SquirrelJME/"

	/** Paths cannot be relative in PalmOS. */
	#define SJME_PATH_NO_RELATIVE
#endif

typedef struct sjme_path_defaultPathEnv
{
	/** The type of directory this matches. */
	sjme_nvm_defaultDirectoryType type;
	
	/** Variable to base from. */
	sjme_lpcstr env;

	/** Relative path from that variable. */
	sjme_lpcstr envRel;

	/** Allow starting with tilde to mean the user home directory. */
	sjme_jboolean tildeHome;
} sjme_path_pathEnv;

static const sjme_path_pathEnv sjme_path_pathEnvLookup[] =
{
	/* Overrides which take priority first. */
	{
		SJME_NVM_DEFAULT_DIRECTORY_CACHE,
		"SQUIRRELJME_CACHE_HOME",
		"",
		SJME_JNI_TRUE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_CONFIG,
		"SQUIRRELJME_CONFIG_HOME",
		"",
		SJME_JNI_TRUE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_DATA,
		"SQUIRRELJME_DATA_HOME",
		"",
		SJME_JNI_TRUE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_STATE,
		"SQUIRRELJME_STATE_HOME",
		"",
		SJME_JNI_TRUE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_NATIVES,
		"SQUIRRELJME_LIB_JVM",
		"",
		SJME_JNI_TRUE
	},

	/* Multiple class path location lookup. */
	{
		SJME_NVM_DEFAULT_DIRECTORY_CLASSPATH_1,
		"SQUIRRELJME_CLASSPATH",
		"",
		SJME_JNI_FALSE,
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_CLASSPATH_2,
		"SQUIRRELJME_JAVA_HOME",
		"lib",
		SJME_JNI_FALSE,
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_CLASSPATH_3,
		"SQUIRRELJME_JAVA_HOME",
		"jre/lib",
		SJME_JNI_FALSE,
	},

#if defined(SJME_CONFIG_HAS_OS_WINDOWS) || \
	defined(SJME_CONFIG_HAS_OS_WINDOWS_CE)
	{
		SJME_NVM_DEFAULT_DIRECTORY_CACHE,
		"LOCALAPPDATA",
		"squirreljme/cache",
		SJME_JNI_FALSE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_CACHE,
		"APPDATA",
		"squirreljme/cache",
		SJME_JNI_FALSE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_CACHE,
		"PROGRAMDATA",
		"squirreljme/cache",
		SJME_JNI_FALSE
	},
	
	{
		SJME_NVM_DEFAULT_DIRECTORY_CONFIG,
		"APPDATA",
		"squirreljme/config",
		SJME_JNI_FALSE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_CONFIG,
		"PROGRAMDATA",
		"squirreljme/config",
		SJME_JNI_FALSE
	},
	
	{
		SJME_NVM_DEFAULT_DIRECTORY_DATA,
		"APPDATA",
		"squirreljme/data",
		SJME_JNI_FALSE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_DATA,
		"PROGRAMDATA",
		"squirreljme/data",
		SJME_JNI_FALSE
	},
	
	{
		SJME_NVM_DEFAULT_DIRECTORY_STATE,
		"LOCALAPPDATA",
		"squirreljme/data",
		SJME_JNI_FALSE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_STATE,
		"APPDATA",
		"squirreljme/data",
		SJME_JNI_FALSE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_STATE,
		"PROGRAMDATA",
		"squirreljme/data",
		SJME_JNI_FALSE
	},

	/* Natives just stay in Program Data. */
	{
		SJME_NVM_DEFAULT_DIRECTORY_NATIVES,
		"PROGRAMDATA",
		"squirreljme/natives",
		SJME_JNI_FALSE
	},
#endif
	
#if defined(SJME_CONFIG_HAS_OS_POSIX)
	/* XDG Directories. */
	{
		SJME_NVM_DEFAULT_DIRECTORY_CACHE,
		"XDG_CACHE_HOME",
		"squirreljme",
		SJME_JNI_FALSE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_CONFIG,
		"XDG_CONFIG_HOME",
		"squirreljme",
		SJME_JNI_FALSE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_DATA,
		"XDG_DATA_HOME",
		"squirreljme",
		SJME_JNI_FALSE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_STATE,
		"XDG_STATE_HOME",
		"squirreljme",
		SJME_JNI_FALSE
	},

	/* Directories based off HOME. */
	{
		SJME_NVM_DEFAULT_DIRECTORY_CACHE,
		"HOME",
		".cache/squirreljme",
		SJME_JNI_FALSE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_CONFIG,
		"HOME",
		".config/squirreljme",
		SJME_JNI_FALSE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_DATA,
		"HOME",
		".local/share/squirreljme",
		SJME_JNI_FALSE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_STATE,
		"HOME",
		".local/state/squirreljme",
		SJME_JNI_FALSE
	},

	/* Generic library directory. */
	{
		SJME_NVM_DEFAULT_DIRECTORY_NATIVES,
		NULL,
		"/lib/squirreljme/natives",
		SJME_JNI_FALSE
	},
#endif

	/* End. */
	{-1, NULL, NULL},
};

sjme_errorCode sjme_path_check(
	sjme_attrInNotNull const sjme_path* path)
{
	sjme_errorCode error;
	sjme_jint length, nameCount, i, beginDx, endDx, lastDx;
	
	if (path == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Must be zero! */
	if (path->zero != 0)
		return SJME_ERROR_MEMORY_CORRUPTION;

	/* A style must be specified. */
	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Invalid flags set? */
	if ((path->flags & (~SJME_PATH_ALL_FLAGS)) != 0)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Path cannot have a root and be relative. */
	if ((path->flags & (SJME_PATH_HAS_ROOT | SJME_PATH_IS_RELATIVE)) ==
		(SJME_PATH_HAS_ROOT | SJME_PATH_IS_RELATIVE))
		return SJME_ERROR_ILLEGAL_STATE;

	/* Length not valid? */
	length = path->length;
	if (length < 0 || length > SJME_MAX_PATH)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Final character of the path is not NUL? */
	if (length < SJME_MAX_PATH && path->chars[length] != '\0')
		return SJME_ERROR_ILLEGAL_STATE;

	/* Name count out of range? */
	nameCount = path->nameCount;
	if (nameCount < 0 || nameCount > SJME_MAX_PATH_DEPTH)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Check that all name indexes are valid. */
	lastDx = 0;
	for (i = 0; i < nameCount; i++)
	{
		/* Get the start and the end of the name. */
		beginDx = path->names[i];
		endDx = path->names[i + 1];

		/* Make sure the bounds are valid. */
		if (beginDx < 0 || endDx < 0 ||
			beginDx >= SJME_MAX_PATH || endDx > SJME_MAX_PATH ||
			((beginDx != 0 || endDx != 0) && beginDx >= endDx) ||
			beginDx != lastDx)
			return SJME_ERROR_ILLEGAL_STATE;

		/* The next name must start at the end of this one, or the length */
		/* of this path must meet the same condition. */
		lastDx = endDx;
	}

	/* Last name index must match the length and the first name must */
	/* always start at zero. */
	if (lastDx != length || path->names[0] != 0)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Special case, if an operating system does not support any form */
	/* of relative paths then make sure denormal paths are never used. */
	if (path->style->requireAbsolute)
		if (sjme_error_is(error = sjme_path_checkDenormal(path,
			SJME_JNI_TRUE)))
			return sjme_error_default(error);

	/* Valid path! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_path_checkDenormal(
	sjme_attrInNotNull const sjme_path* path,
	sjme_attrInValue sjme_jboolean requireAbsolute)
{
	sjme_errorCode error;
	sjme_jint i, n, len;
	sjme_lpcstr str;
	sjme_jboolean dotDotOkay;
	const sjme_path_styleDot(*dot)[2];
	const sjme_path_styleDot(*dotDot)[2];

	if (path == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (sjme_error_is(error = sjme_path_check(path)))
		return sjme_error_default(error);

	/* If requiring absolute paths, then this must have a root and not be */
	/* a relative path. */
	if (requireAbsolute && (path->flags & (SJME_PATH_IS_RELATIVE |
		SJME_PATH_HAS_ROOT)) != SJME_PATH_HAS_ROOT)
		return SJME_ERROR_PATH_NOT_ABSOLUTE;

	/* Get dot and dot dot styles. */
	dot = &path->style->dot;
	dotDot = &path->style->dotDot;

	/* Check each name for relative components. */
	dotDotOkay = SJME_JNI_TRUE;
	for (n = path->nameCount, i = 0; i < n; i++)
	{
		/* Get the name at this index. */
		len = -1;
		str = NULL;
		if (sjme_error_is(error = sjme_path_getNameF(&len, &str,
			path, i)) || len < 0 || str == NULL)
			return sjme_error_default(error);

		/* Dot stays in the current directory. */
		if ((len == (*dot)[0].len &&
				!strncmp((*dot)[0].str, str, len)) ||
			(len == (*dot)[1].len &&
				!strncmp((*dot)[1].str, str, len)))
			return SJME_ERROR_PATH_NOT_ABSOLUTE;

		/* Dot-dot goes up a directory. */
		else if ((len == (*dotDot)[0].len &&
				!strncmp((*dotDot)[0].str, str, len)) ||
			(len == (*dotDot)[1].len &&
				!strncmp((*dotDot)[1].str, str, len)))
		{
			/* A dot-dot should not occur here as it is not at the very */
			/* start of a relative path! */
			/* If a path starts with dot-dot and we want an absolute path */
			/* then we already know this is bad. */
			if (!dotDotOkay || requireAbsolute)
				return SJME_ERROR_PATH_NOT_ABSOLUTE;
		}

		/* Any other path case means dot-dot is no longer valid. This is the */
		/* case when normalized relative paths are passed which are in */
		/* the form of ../cute/squirrels and denormalized are in */
		/* the form of ../cute/../squirrels . */
		else
			dotDotOkay = SJME_JNI_FALSE;
	}

	/* Path is okay! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_path_default(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInValue sjme_nvm_defaultDirectoryType type,
	sjme_attrInNegativeOnePositive sjme_jint index)
{
	sjme_errorCode error;
	sjme_jint i, vi;
	const sjme_path_pathEnv* lookup;
	sjme_lpcstr lastEnv;
	sjme_jboolean lastTilde;
	sjme_path envPath, buildPath;
	
	if (outPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (type <= SJME_NVM_DEFAULT_DIRECTORY_UNKNOWN ||
		type >= SJME_NVM_NUM_DEFAULT_DIRECTORY_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (index < -1)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Use a default NAL? */
	if (nal == NULL)
		nal = &sjme_nal_default;

	/* Clear temporary paths. */
	memset(&envPath, 0, sizeof(envPath));
	memset(&buildPath, 0, sizeof(buildPath));
	
	/* Go through each default to locate paths accordingly. */
	lastEnv = NULL;
	lastTilde = SJME_JNI_FALSE;
	error = SJME_ERROR_NONE;
	for (i = 0, vi = 0;; i++)
	{
		/* Go through the lookup set. */
		lookup = &sjme_path_pathEnvLookup[i];
		if (lookup->type <= 0 ||
			(lookup->env == NULL && lookup->envRel == NULL))
			break;

		/* Is this the wrong type? */
		if (lookup->type != type)
			continue;

		/* Wanting a specific index? */
		if (index >= 0)
			if (index != (vi++))
				continue;

		/* Lookup uses no defined environment variable. */
		if (lookup->env == NULL)
		{
			/* Clear these, as they are both not valid. */
			lastEnv = NULL;
			lastTilde = SJME_JNI_FALSE;
			memset(&envPath, 0, sizeof(envPath));
		}
		
		/* Lookup the environment variable, if it has changed. */
		else if (lastEnv == NULL || lastTilde != lookup->tildeHome ||
			0 != strcmp(lastEnv, lookup->env))
		{
			/* Get it from the system. */
			memset(&buildPath, 0, sizeof(buildPath));
			if (nal->getEnv == NULL ||
				sjme_error_is(error = nal->getEnv(
					buildPath.chars, SJME_MAX_PATH - 1, lookup->env)))
			{
				/* No env set, so ignore this lookup. */
				if (error == SJME_ERROR_NO_SUCH_ELEMENT)
				{
					/* If looking for a specific index? Stop. */
					if (index >= 0)
						break;

					/* Skip looking at this path. */
					continue;
				}

				/* Fail. */
				return sjme_error_default(error);
			}

			/* Replace with the user home directory? */
			memset(&envPath, 0, sizeof(envPath));
			if (buildPath.chars[0] == '~' && (buildPath.chars[1] == '\0' ||
				buildPath.chars[1] == '/'))
			{
				/* Grab the home directory. */
				if (sjme_error_is(error = sjme_path_userHome(nal, &envPath)))
					return sjme_error_default(error);

				/* Append the resolved path, if not NUL. */
				if (buildPath.chars[1] == '/')
					if (sjme_error_is(error = sjme_path_resolveS(&envPath,
						&buildPath.chars[2])))
						return sjme_error_default(error);
			}

			/* Normal parse. */
			else
			{
				/* Parse the path. */
				if (sjme_error_is(error = sjme_path_parse(&envPath,
					buildPath.chars)))
					return sjme_error_default(error);
			}

			/* The path must be absolute and normalized. */
			if (sjme_error_is(error = sjme_path_checkDenormal(&envPath,
				SJME_JNI_TRUE)))
				return sjme_error_default(error);
			
			/* Cached for later. */
			lastEnv = lookup->env;
			lastTilde = lookup->tildeHome;
		}

		/* Need to rebuild the path, so start by clearing it and using */
		/* the new base path. */
		memset(&buildPath, 0, sizeof(buildPath));
		if (lastEnv != NULL)
			if (sjme_error_is(error = sjme_path_resolveP(
				&buildPath, &envPath)))
				return sjme_error_default(error);
		
		/* Resolve the adjacent path onto this. */
		if (lookup->envRel != NULL)
			if (sjme_error_is(error = sjme_path_resolveS(
				&buildPath, lookup->envRel)))
				return sjme_error_default(error);
		
		/* The path must be absolute and normalized. */
		if (sjme_error_is(error = sjme_path_checkDenormal(&buildPath,
			SJME_JNI_TRUE)))
			return sjme_error_default(error);
		
		/* Success! */
		memmove(outPath, &buildPath, sizeof(buildPath));
		return SJME_ERROR_NONE;
	}

	/* The path is not defined at all. */
	return SJME_ERROR_PATH_NOT_DEFINED;
}

sjme_errorCode sjme_path_getName(
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull const sjme_path* inPath,
	sjme_attrInPositive sjme_jint nameDx)
{
	sjme_errorCode error;
	
	if (outPath == NULL || inPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (nameDx < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	if (sjme_error_is(error = sjme_path_check(inPath)))
		return sjme_error_default(error);

	if (nameDx >= inPath->nameCount)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_path_getNameF(
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_jint* outFLimit,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_lpcstr* outFStr,
	sjme_attrInNotNull const sjme_path* inPath,
	sjme_attrInPositive sjme_jint nameDx)
{
	sjme_errorCode error;

	if (outFLimit == NULL || outFStr == NULL || inPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (nameDx < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	if (sjme_error_is(error = sjme_path_check(inPath)))
		return sjme_error_default(error);

	if (nameDx >= inPath->nameCount)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* This is just grabbing directly from the name data. */
	*outFStr = &inPath->chars[inPath->names[nameDx]];
	*outFLimit = inPath->names[nameDx + 1] - inPath->names[nameDx];
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_path_getParent(
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull const sjme_path* inPath)
{
	sjme_errorCode error;

	if (outPath == NULL || inPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (sjme_error_is(error = sjme_path_check(inPath)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_path_getRoot(
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull const sjme_path* inPath)
{
	sjme_errorCode error;

	if (outPath == NULL || inPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (sjme_error_is(error = sjme_path_check(inPath)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_path_normalize(
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull const sjme_path* inPath)
{
	sjme_errorCode error;

	if (outPath == NULL || inPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (sjme_error_is(error = sjme_path_check(inPath)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_path_parse(
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull sjme_lpcstr strPath)
{
	sjme_errorCode error;
	const sjme_path_style* style;

	if (outPath == NULL || strPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get the style as determined by NAL. */
	style = NULL;
	if (sjme_nal_default.pathStyle != NULL &&
		sjme_error_is(error = sjme_nal_default.pathStyle(&style)))
		return sjme_error_default(error);

	/* Forward parse, fallback to none. */
	return sjme_path_parseYP((style != NULL ? style :
		&sjme_path_styles[SJME_PATH_STYLE_NONE]), outPath, strPath);
}

sjme_errorCode sjme_path_parseF(
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull sjme_attrFormatArg sjme_lpcstr format,
	...)
{
	sjme_errorCode error;

	if (outPath == NULL || format == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_path_resolveP(
	sjme_attrOutNotNull sjme_attrOutModify sjme_path* outPath,
	sjme_attrInNotNull const sjme_path* inPath)
{
	sjme_errorCode error;
	sjme_path result;
	sjme_jint newLength, newCount;

	if (outPath == NULL || inPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (sjme_error_is(error = sjme_path_check(inPath)))
		return sjme_error_default(error);

	/* If the input path is a blank path, this does nothing. */
	if (inPath->length == 0)
		return SJME_ERROR_NONE;

	/* If the path is absolute then just use the absolute path. */
	/* Or if the output path is a blank path. */
	if ((inPath->flags & SJME_PATH_HAS_ROOT) != 0 || outPath->length == 0)
	{
		memmove(outPath, inPath, sizeof(*outPath));
		return SJME_ERROR_NONE;
	}

	/* Make a defensive copy of the output path. */
	memmove(&result, outPath, sizeof(*outPath));
	if (sjme_error_is(error = sjme_path_check(&result)))
		return sjme_error_default(error);

	/* If there is no separator at the end, one must be added. */
	if (result.chars[result.length - 1] != SJME_CONFIG_FILE_SEPARATOR)
	{
		/* Make sure this does not overflow the path. */
		if (result.length >= SJME_MAX_PATH)
			return SJME_ERROR_PATH_TOO_LONG;
		
		/* Add in the new character. */
		result.chars[result.length++] = SJME_CONFIG_FILE_SEPARATOR;
		result.names[result.nameCount] = result.length;
	}

	/* Calculate size of the new path. */
	newLength = result.length + inPath->length;
	newCount = result.nameCount + inPath->nameCount;
	if (newLength < 0 || newLength > SJME_MAX_PATH)
		return SJME_ERROR_PATH_TOO_LONG;
	else if (newCount < 0 || newCount > SJME_MAX_PATH_DEPTH)
		return SJME_ERROR_PATH_TOO_DEEP;

	/* Copy entire path segment over. */
	memmove(&result.chars[result.length], &inPath->chars[0],
		sizeof(result.chars[0]) * inPath->length);

	/* Copy names over. */
	memmove(&result.names[result.nameCount], &inPath->names[0],
		sizeof(result.names[0]) * (inPath->nameCount + 1));

	/* Offset names accordingly. */
	while (result.nameCount <= newCount)
		result.names[result.nameCount++] += result.length;
	result.nameCount = newCount;
	result.length = newLength;

	/* Make sure the final path is valid. */
	if (sjme_error_is(error = sjme_path_check(&result)))
		return sjme_error_default(error);

	/* Success! */
	memmove(outPath, &result, sizeof(*outPath));
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_path_parseY(
	sjme_attrInValue sjme_path_styleType style,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull sjme_lpcstr strPath)
{
	if (outPath == NULL || strPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (style < 0 || style >= SJME_NUM_PATH_STYLES)
		return SJME_ERROR_INVALID_ARGUMENT;

	return sjme_path_parseYP(&sjme_path_styles[style],
		outPath, strPath);
}

sjme_errorCode sjme_path_parseYP(
	sjme_attrInValue const sjme_path_style* style,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull sjme_lpcstr strPath)
{
	sjme_errorCode error;
	sjme_path working;
	sjme_cchar c;
	sjme_jint strLen, bp, ep, nameAt, i, n;
	
	if (style == NULL || outPath == NULL || strPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Clear working path. */
	memset(&working, 0, sizeof(working));
	
#if SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_UNIX
	/* At the very start, if we have three or more slashes then we just use */
	/* the last one. It is simplest to do it here. */
	strLen = strlen(strPath);
	if (strLen >= 3 && strPath[0] == '/' &&
		strPath[1] == '/' && strPath[2] == '/')
		do
		{
			strPath = &strPath[1];
		} while (strPath[0] == '/' && strPath[1] == '/');
#endif

	/* Make sure the path is not too long. */
	strLen = strlen(strPath);
	if (strLen > SJME_MAX_PATH)
		return SJME_ERROR_PATH_TOO_LONG;

	/* Copy it over to act as the base path. */
	memmove(&working.chars[0], strPath, sizeof(*strPath) * strLen);

#if SJME_CONFIG_PATH_STYLE_IS_DOS_OR_WINDOWS || \
	defined(SJME_CONFIG_PATH_STYLE_IS_UPPERCASE)
	/* Check for invalid characters in the path, on some platforms anyway. */
	/* Or perform case normalization. */
	for (i = 0; i < strLen; i++)
	{
		c = working.chars[i];

#if SJME_CONFIG_PATH_STYLE_IS_DOS_OR_WINDOWS
		/* These are always invalid on DOS/Windows. */
		if (c == ':' || c == '*' || c == '?' || c == '?' ||
			c == '<' || c == '>' || c == '|')
			return SJME_ERROR_PATH_NOT_VALID;
#endif

#if SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_DOS
		/* These are invalid strictly in DOS. */
		if (c == '+' || c == ',' || c == ';' || c == '[' || c == ']' ||
			c == '/')
			return SJME_ERROR_PATH_NOT_VALID;
#endif

#if defined(SJME_CONFIG_PATH_STYLE_IS_UPPERCASE)
		/* Force uppercase? */
		if (c >= 'a' && c <= 'z')
			working.chars[i] = 'A' + (c - 'a');
#endif
	}
#endif

	/* Build out the set of names, which are effectively each and every */
	/* directory. */
	nameAt = 0;
	for (bp = 0, ep = 0; ep < strLen + 1; ep++)
	{
		/* Too many names? */
		if (nameAt == SJME_MAX_PATH_DEPTH)
			return SJME_ERROR_PATH_TOO_DEEP;
		
		/* Is this a directory separator? Or NUL? */
		c = working.chars[ep];
		if (c == SJME_CONFIG_FILE_SEPARATOR ||
			c == SJME_CONFIG_FILE_SEPARATOR_ALT ||
			c == '\0')
		{
			working.names[nameAt++] = bp;
			working.names[nameAt] = ep + 1;
			bp = ep + 1;
		}
	}
	
	/* Too many names? */
	if (nameAt > SJME_MAX_PATH_DEPTH)
		return SJME_ERROR_PATH_TOO_DEEP;

	/* The "final" name is the string length. */
	working.names[nameAt] = strLen;
	working.length = strLen;
	working.nameCount = nameAt;

	/* Is this path a directory? */
	c = working.chars[strLen - 1];
	if (c == SJME_CONFIG_FILE_SEPARATOR ||
		c == SJME_CONFIG_FILE_SEPARATOR_ALT)
		working.flags |= SJME_PATH_IS_DIRECTORY;

#if SJME_CONFIG_PATH_STYLE_IS_DOS_OR_WINDOWS
	/* Cannot start with a UNIX root. */
	if (working.chars[0] == '/')
		return SJME_ERROR_PATH_NOT_VALID;
#endif

#if SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_WINDOWS
	/* There cannot be awkward UNC paths in Windows */
	if ((working.chars[0] == '\\' && working.chars[1] == '/') ||
		(working.chars[0] == '/' && working.chars[1] == '\\'))
		return SJME_ERROR_PATH_NOT_VALID;
#endif

	/* Determine if this has a root component. */
#if SJME_CONFIG_PATH_STYLE_IS_DOS_OR_WINDOWS
	c = working.chars[0];
	if (strLen >= 2 && (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') &&
		working.chars[1] == ':')
	{
		/* Slash or NUL must follow the root. */
		c = working.chars[2];
		if (c != SJME_CONFIG_FILE_SEPARATOR &&
			c != SJME_CONFIG_FILE_SEPARATOR_ALT &&
			c != '\0')
			return SJME_ERROR_PATH_NOT_VALID;
		
		/* Valid! */
		working.flags |= SJME_PATH_HAS_ROOT;
	}
#if SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_WINDOWS
	/* Handle UNC paths on Windows as an alternative. */
	else if (strLen >= 2 && (working.chars[0] == SJME_CONFIG_FILE_SEPARATOR
		&& working.chars[1] == SJME_CONFIG_FILE_SEPARATOR)
	{
		/* UNC paths cannot be \\\ or \\/. */
		if (working.chars[2] == SJME_CONFIG_FILE_SEPARATOR ||
			working.chars[2] == SJME_CONFIG_FILE_SEPARATOR_ALT)
			return SJME_ERROR_PATH_NOT_VALID;
		
		/* Shift down. */
		memmove(&working.names[1], &working.names[2],
			sizeof(working.names[0]) *
			(SJME_MAX_PATH_DEPTH - working.nameCount));
		working.nameCount--;

		/* Make sure the last name is cleared. */
		working.names[SJME_MAX_PATH_DEPTH] = 0;
		
		/* Valid! */
		working.flags |= SJME_PATH_HAS_ROOT;
	}
#endif
#elif SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_UNIX
	if ((strLen >= 2 && working.chars[0] == '/' && working.chars[1] == '/') ||
		(strLen >= 1 && working.chars[0] == '/'))
	{
		/* Double slash characters must always be considered a single */
		/* component to be POSIX compatible. */
		if (working.chars[1] == '/')
		{
			/* Shift down. */
			memmove(&working.names[1], &working.names[2],
				sizeof(working.names[0]) *
				(SJME_MAX_PATH_DEPTH - working.nameCount));
			working.nameCount--;

			/* Make sure the last name is cleared. */
			working.names[SJME_MAX_PATH_DEPTH] = 0;
		}
		
		/* Valid! */
		working.flags |= SJME_PATH_HAS_ROOT;
	}
#else
	#error Unknown native path style.
#endif

	/* Remove empty names so that /cute//squirrels becomes /cute/squirrels. */
	for (nameAt = working.nameCount - 1; nameAt >= 0; nameAt--)
	{
		/* Get both sides of the name. */
		bp = working.names[nameAt];
		ep = working.names[nameAt + 1];

		/* If the name is a single character and only consists of the path */
		/* component, strip it. */
		c = working.chars[bp];
		if (ep == bp + 1 && (c == SJME_CONFIG_FILE_SEPARATOR ||
			c == SJME_CONFIG_FILE_SEPARATOR_ALT))
		{
#if (SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_UNIX) || \
	(SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_WINDOWS)
			/* Do not actually strip the root component on UNIX. */
			/* On Windows, do not strip out the UNC root. */
			if (nameAt == 0)
				break;
#endif
			
			/* Shift entire path down. */
			memmove(&working.chars[bp], &working.chars[bp + 1],
				SJME_MAX_PATH - bp);
			
			/* Shift names down. */
			memmove(&working.names[nameAt], &working.names[nameAt + 1],
				sizeof(working.names[0]) *
				(SJME_MAX_PATH_DEPTH - working.nameCount));
			working.nameCount--;

			/* Make sure the last name is cleared. */
			working.names[SJME_MAX_PATH_DEPTH] = 0;

			/* Make sure all indexes post shift get decremented. */
			for (i = nameAt; i <= working.nameCount; i++)
				working.names[i]--;

			/* Length gets cut down by one as well. */
			working.length--;
		}
	}
	
#if SJME_CONFIG_FILE_SEPARATOR != SJME_CONFIG_FILE_SEPARATOR_ALT 
	/* If the alternative path character is actually different, then */
	/* force everything to be the primary path character. */
	for (n = working.length, i = 0; i < n; i++)
		if (working.chars[i] == SJME_CONFIG_FILE_SEPARATOR_ALT)
			working.chars[i] = SJME_CONFIG_FILE_SEPARATOR;
#endif

	/* Make sure resultant working path is valid. */
	if (sjme_error_is(error = sjme_path_check(&working)))
		return sjme_error_default(error);

	/* Copy out! */
	memmove(outPath, &working, sizeof(*outPath));
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_path_resolveS(
	sjme_attrOutNotNull sjme_attrOutModify sjme_path* outPath,
	sjme_attrInNotNull sjme_lpcstr inPath)
{
	sjme_errorCode error;
	sjme_path parsed;

	if (outPath == NULL || inPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (sjme_error_is(error = sjme_path_check(outPath)))
		return sjme_error_default(error);

	/* Parse the input path first. */
	memset(&parsed, 0, sizeof(parsed));
	if (sjme_error_is(error = sjme_path_parse(&parsed, inPath)))
		return sjme_error_default(error);

	/* Then forward to the normal path resolution. */
	return sjme_path_resolveP(outPath, &parsed);
}

sjme_errorCode sjme_path_resolveV(
	sjme_attrOutNotNull sjme_attrOutModify sjme_path* outPath,
	...)
{
	sjme_errorCode error;

	if (outPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (sjme_error_is(error = sjme_path_check(outPath)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_path_resolveSibling(
	sjme_attrOutNotNull sjme_attrOutModify sjme_path* outPath,
	sjme_attrInNotNull const sjme_path* inPath)
{
	sjme_errorCode error;

	if (outPath == NULL || inPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (sjme_error_is(error = sjme_path_check(outPath)))
		return sjme_error_default(error);

	if (sjme_error_is(error = sjme_path_check(inPath)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_path_subPath(
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull const sjme_path* inPath,
	sjme_attrInPositive sjme_jint beginDx,
	sjme_attrInPositive sjme_jint endDx)
{
	sjme_errorCode error;

	if (outPath == NULL || inPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (beginDx < 0 || endDx < 0 || endDx < beginDx)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	if (sjme_error_is(error = sjme_path_check(inPath)))
		return sjme_error_default(error);

	if (beginDx >= inPath->nameCount || endDx >= inPath->nameCount)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_path_userHome(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath)
{
	if (outPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_PATH_PSEUDO_HOME)
	/* The operating system has no concept of a home directory, so choose an */
	/* arbitrary one. */
	return sjme_path_resolveS(outPath, SJME_PATH_PSEUDO_HOME);
#else
	/* Use a default NAL? */
	if (nal == NULL)
		nal = &sjme_nal_default;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
}
