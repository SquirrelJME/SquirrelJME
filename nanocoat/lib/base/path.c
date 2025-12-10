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
	sjme_jint length, nameCount, i, beginDx, endDx, lastDx;
	
	if (path == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Must be zero! */
	if (path->zero != 0)
		return SJME_ERROR_MEMORY_CORRUPTION;

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
			beginDx >= endDx || beginDx != lastDx)
			return SJME_ERROR_ILLEGAL_STATE;

		/* The next name must start at the end of this one, or the length */
		/* of this path must meet the same condition. */
		lastDx = endDx;
	}

	/* Last name index must match the length. */
	if (lastDx != length)
		return SJME_ERROR_ILLEGAL_STATE;

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

	if (path == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (sjme_error_is(error = sjme_path_check(path)))
		return sjme_error_default(error);

	/* If requiring absolute paths, then this must have a root and not be */
	/* a relative path. */
	if (requireAbsolute && (path->flags & (SJME_PATH_IS_RELATIVE |
		SJME_PATH_HAS_ROOT)) != SJME_PATH_HAS_ROOT)
		return SJME_ERROR_PATH_NOT_ABSOLUTE;

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
		if ((len == 1 && !strcmp(".", str)) ||
			(len == 2 && !strcmp("./", str)))
			return SJME_ERROR_PATH_NOT_ABSOLUTE;

		/* Dot-dot goes up a directory. */
		else if ((len == 2 && !strcmp("..", str)) ||
			(len == 3 && !strcmp("../", str)))
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
				if (sjme_error_is(error = sjme_path_userHome(&envPath)))
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

		/* Need to rebuild the path, so start by clearing it. */
		memset(&buildPath, 0, sizeof(buildPath));
		
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
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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

	if (outPath == NULL || strPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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

	if (outPath == NULL || inPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (sjme_error_is(error = sjme_path_check(outPath)))
		return sjme_error_default(error);

	if (sjme_error_is(error = sjme_path_check(inPath)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath)
{
	if (outPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

#if 0
sjme_errorCode sjme_path_baseName(
	sjme_attrInOutNotNullBuf(inOutPathLen) sjme_lpstr inOutPath,
	sjme_attrInPositive sjme_jint inOutPathLen)
{
	sjme_errorCode error;
	sjme_jint nameCount;
	sjme_jboolean isRoot;
	sjme_cchar result[SJME_MAX_PATH];
	sjme_jint resultLen;
	
	if (inOutPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inOutPathLen < 0 || SJME_POINTER_OVERFLOW(inOutPath, inOutPathLen))
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* How many names does this path actually have? */
	nameCount = -1;
	if (sjme_error_is(error = sjme_path_getNameCount(inOutPath, inOutPathLen,
		&nameCount)) || nameCount < 0)
		return sjme_error_default(error);

	/* If there are no names, then the base name is just blank. */
	if (nameCount == 0)
	{
		memset(inOutPath, 0, sizeof(*inOutPath) * inOutPathLen);
		return SJME_ERROR_NONE;
	}
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_path_default(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInNegativeOnePositive sjme_jint index,
	sjme_attrInValue sjme_nvm_defaultDirectoryType type,
	sjme_attrOutNotNullBuf(outPathLen) sjme_lpstr outPath,
	sjme_attrInPositive sjme_jint outPathOff,
	sjme_attrInPositiveNonZero sjme_jint outPathLen)
{
	sjme_errorCode error;
	sjme_jint i, vi, outLen;
	const sjme_path_pathEnv* lookup;
	sjme_lpcstr lastEnv, subResolve;
	sjme_jboolean isDirSep;
	sjme_cchar envValue[SJME_MAX_PATH];
	sjme_cchar buildPath[SJME_MAX_PATH];
	
	if (outPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (type <= SJME_NVM_DEFAULT_DIRECTORY_UNKNOWN ||
		type >= SJME_NVM_NUM_DEFAULT_DIRECTORY_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (index < -1)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (outPathLen < 0 || outPathOff < 0 ||
		SJME_POINTER_OVERFLOW(outPath, outPathOff + outPathLen))
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Use a default NAL instead? */
	if (nal == NULL)
		nal = &sjme_nal_default;

	/* Go through each default to locate paths accordingly. */
	lastEnv = NULL;
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
			memset(envValue, 0, sizeof(envValue));
		}
		
		/* Lookup the environment variable, if it has changed. */
		else if (lastEnv == NULL || 0 != strcmp(lastEnv, lookup->env))
		{
			/* Get it from the system. */
			memset(envValue, 0, sizeof(envValue));
			if (nal->getEnv == NULL ||
				sjme_error_is(error = nal->getEnv(
					envValue, SJME_MAX_PATH - 1, lookup->env)))
			{
				/* No env set, so ignore this lookup. */
				if (error == SJME_ERROR_NO_SUCH_ELEMENT)
				{
					/* If looking for a specific index? Stop. */
					if (index >= 0)
						break;
					
					continue;
				}

				/* Fail. */
				return sjme_error_default(error);
			}

			/* Normalize the path so that it is not wonky in any way. */
			if (sjme_error_is(error = sjme_path_normalize(
				envValue, 0, SJME_MAX_PATH - 1,
				SJME_JNI_TRUE)))
				return sjme_error_default(error);
			
			/* Cached for later. */
			lastEnv = lookup->env;
		}

		/* Need to rebuild the path, so start by clearing it. */
		memset(buildPath, 0, sizeof(buildPath));

		/* Replace start with the user home directory? */
		subResolve = envValue;
		if (lookup->env != NULL && lookup->tildeHome && envValue[0] == '~')
		{
			/* Is there a directory seperator following the home tilde? */
			isDirSep = SJME_JNI_FALSE;
			if (sjme_error_is(error = sjme_path_isDirectorySep(
				envValue, 0, SJME_MAX_PATH - 1,
				&isDirSep, &subResolve)))
				return sjme_error_default(error);

			/* Only if there is a directory seperator, or if the path points */
			/* to the actual user home directory is it considered valid. */
			if (isDirSep || envValue[1] == '\0')
				if (sjme_error_is(error = sjme_path_userHome(buildPath,
					SJME_MAX_PATH - 1)))
					return sjme_error_default(error);
		}

		/* Resolve the adjacent path onto this. */
		if (lookup->envRel != NULL)
		{
			/* Perform path resolution. */
			if (sjme_error_is(error = sjme_path_resolveAppend(
				buildPath, SJME_MAX_PATH - 1,
				lookup->envRel, strlen(lookup->envRel))))
				return sjme_error_default(error);
		}
		
		/* Ensure the path is normalized, it must also be absolute. */
		if (sjme_error_is(error = sjme_path_normalize(
			buildPath, 0, SJME_MAX_PATH - 1,
			SJME_JNI_TRUE)))
			return sjme_error_default(error);

		/* Make sure the target path can actually fit. */
		outLen = strlen(buildPath);
		if (outLen > outPathLen ||
			SJME_POINTER_OVERFLOW(outPath, outPathOff + outLen))
			return SJME_ERROR_PATH_TOO_LONG;

		/* Copy it over. */
		memmove(SJME_POINTER_OFFSET(outPath, outPathOff),
			buildPath, sizeof(*buildPath) * outLen);

		/* Make sure it ends in NUL. */
		if (outPathOff + (outLen - 1) < outPathLen)
			outPath[outPathOff + (outLen - 1)] = '\0';

		/* Success! */
		return SJME_ERROR_NONE;
	}

	/* The path is not defined at all. */
	return SJME_ERROR_PATH_NOT_DEFINED;
}

sjme_errorCode sjme_path_getName(
	sjme_attrInNotNullBuf(inPathLen) sjme_lpcstr inPath,
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrInNegativeOnePositive sjme_jint inName,
	sjme_attrOutNullable sjme_lpcstr* outBase,
	sjme_attrOutNullable sjme_jint* outLen,
	sjme_attrOutNullable sjme_jboolean* outIsRoot)
{
	if (inPath == NULL || (outBase == NULL && outLen == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward to full form. */
	return sjme_path_getNameF(inPath,
		inPathLen, inName,
		outBase, NULL,
		NULL, NULL,
		outLen, NULL, outIsRoot);
}

sjme_errorCode sjme_path_getNameF(
	sjme_attrInNotNullBuf(inPathLen) sjme_lpcstr inPath,
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrInNegativeOnePositive sjme_jint inName,
	sjme_attrOutNullable sjme_lpcstr* outBase,
	sjme_attrOutNullable sjme_jint* outBaseDx,
	sjme_attrOutNullable sjme_lpcstr* outEnd,
	sjme_attrOutNullable sjme_jint* outEndDx,
	sjme_attrOutNullable sjme_jint* outLen,
	sjme_attrOutNullable sjme_jint* outCount,
	sjme_attrOutNullable sjme_jboolean* outIsRoot)
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

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
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
	if (rootBase != NULL && rootEnd != NULL)
		sjme_message("Root path found: `%.*s` <- `%s`",
			(int)(rootEnd - rootBase), rootBase, inPath);
#endif
	
	/* Did we want the root component? */
	if (inName == -1)
	{
		/* Root element? */
		if (outIsRoot != NULL)
			*outIsRoot = SJME_JNI_TRUE;
		
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
	
	/* Going forward, nothing is the root */
	if (outIsRoot != NULL)
		*outIsRoot = SJME_JNI_FALSE;
	
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
		
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
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
	sjme_attrInNotNullBuf(inPathLen) sjme_lpcstr inPath,
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
			NULL, NULL, NULL, &result, NULL)) ||
		result < 0)
		return sjme_error_default(error);
	
	/* Give the count. */
	*outCount = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_path_isDirectory(
	sjme_attrInNotNullBuf(inPathLen) sjme_lpcstr inPath,
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrOutNotNull sjme_jboolean* isDirectory)
{
	sjme_errorCode error;
	sjme_jint subLen, nameCount;
	sjme_jboolean hasDirSep, hasRoot;
	
	if (inPath == NULL || isDirectory == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inPathLen < 0 || SJME_POINTER_OVERFLOW(inPath, inPathLen))
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Perform simple check to see if the path ends with a directory */
	/* separator. */
	subLen = strlen(inPath);
	if (subLen > inPathLen)
		subLen = inPathLen;

	/* Is this specific character a directory separator? */
	hasDirSep = SJME_JNI_FALSE;
	if (sjme_error_is(error = sjme_path_isDirectorySep(inPath,
		subLen - 1, inPathLen, &hasDirSep, NULL)))
		return sjme_error_default(error);
	
	/* If the last character is a directory separator, then one is */
	/* indicated. */
	if (hasDirSep)
	{
		*isDirectory = SJME_JNI_TRUE;
		return SJME_ERROR_NONE;
	}

	/* Otherwise, the final check that needs to be done is to make sure */
	/* the given path does not have a root component as that may not */
	/* always end in a directory separator. */
	nameCount = -1;
	hasRoot = SJME_JNI_FALSE;
	if (sjme_error_is(error = sjme_path_getNameCount(inPath, inPathLen,
		&nameCount)) || nameCount < 0)
		return sjme_error_default(error);
	if (sjme_error_is(error = sjme_path_hasRoot(inPath, inPathLen,
		&hasRoot)))
		return sjme_error_default(error);

	/* If there are one or no names and there is a root, then this must */
	/* be a directory. */
	if (hasRoot && nameCount <= 1)
		*isDirectory = SJME_JNI_FALSE;

	/* Otherwise, not considered to be a directory. */
	else
		*isDirectory = SJME_JNI_FALSE;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_path_isDirectorySep(
	sjme_attrInNotNullBuf(inPathLen) sjme_lpcstr inPath,
	sjme_attrInPositive sjme_jint inPathOff,
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrOutNotNull sjme_jboolean* isDirectory,
	sjme_attrOutNullable sjme_lpcstr* outFollowing)
{
	sjme_lpcstr readBase;
	sjme_jboolean hasDirSep;
	sjme_jint followCount;
	
	if (inPath == NULL || isDirectory == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inPathOff < 0 || inPathLen < 0 || (inPathOff + inPathLen) < 0 ||
		SJME_POINTER_OVERFLOW(inPath, inPathOff + inPathLen))
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Read in the character at the offset. */
	readBase = SJME_POINTER_OFFSET(inPath, inPathOff);

#if SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_UNIX
	/* Unix slash? */
	hasDirSep = (*readBase == '/');
	followCount = 1;
#elif SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_DOS
	/* DOS allows multiple slashes. */
	hasDirSep = (*readBase == '/' || *readBase == '\\');
	followCount = 1;
#elif SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_MACOS_CLASSIC
	/* macOS classic uses colons. */
	hasDirSep = (*readBase == ':');
	followCount = 1;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif

	/* Returning the following character? */
	if (outFollowing != NULL)
		*outFollowing = SJME_POINTER_OFFSET(readBase,
			(hasDirSep ? followCount : 0));

	/* Is a directory seperator used? */
	*isDirectory = hasDirSep;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_path_hasRoot(
	sjme_attrInNotNullBuf(inPathLen) sjme_lpcstr inPath,
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
		&len, NULL, NULL)) || len <= 0)
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

sjme_errorCode sjme_path_normalize(
	sjme_attrInOutNotNullBuf(inOutPathLen) sjme_lpstr inOutPath,
	sjme_attrInPositive sjme_jint inOutPathOff,
	sjme_attrInPositive sjme_jint inOutPathLen,
	sjme_attrInValue sjme_jboolean requireAbsolute)
{
	if (inOutPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inOutPathLen < 0 || inOutPathOff < 0 ||
		SJME_POINTER_OVERFLOW(inOutPath, inOutPathOff + inOutPathLen))
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_path_resolveAppend(
	sjme_attrOutNotNullBuf(outPathLen) sjme_lpstr inOutPath,
	sjme_attrInPositiveNonZero sjme_jint inOutPathLen,
	sjme_attrInNotNullBuf(subPathLen) sjme_lpcstr subPath,
	sjme_attrInPositiveNonZero sjme_jint subPathLen)
{
	sjme_errorCode error;
	sjme_jint outLen, subLen;
	sjme_jint subNames, subName;
	sjme_lpstr result;
	sjme_jint resultBytes;
	sjme_lpcstr subBase;
	sjme_jint subBaseLen, sepLen;
	
	if (inOutPath == NULL || subPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inOutPathLen <= 0 || subPathLen < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Nothing to do? */
	if (subPathLen == 0)
		return SJME_ERROR_NONE;
	
	/* Need to check how many characters to potentially add. */
	outLen = strlen(inOutPath);
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
		(outLen + subLen) + 1 > inOutPathLen)
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
	resultBytes = sizeof(*result) * (inOutPathLen + 2);
	result = sjme_alloca(resultBytes);
	if (result == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(result, 0, resultBytes);
	memmove(result, inOutPath, sizeof(*result) * outLen);
	
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
					subName, &subBase, &subBaseLen, NULL)) ||
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
				memset(result, 0, sizeof(*result) * inOutPathLen);
				memmove(result, subBase,
					sizeof(*result) * subBaseLen);
				
				continue;
			}
			
			/* Append individual path. */
			if (sjme_error_is(error = sjme_path_resolveAppend(result,
				inOutPathLen, subBase, subBaseLen)))
				return sjme_error_default(error);
			
			/* Recalculate output length. */
			outLen = strlen(result);
			
			/* Add directory separator, if needed. */
			if (outLen > 0 && subName < subNames - 1)
			{
				sepLen = strlen(SJME_CONFIG_FILE_SEPARATOR) + 1; 
				memmove(&result[outLen], SJME_CONFIG_FILE_SEPARATOR,
					sizeof(*result) * sepLen);
			}
		}
	}
	
	/* Single path only, cannot be blank. */
	else
	{
		/* Gets single subcomponent details. */
		subBase = NULL;
		subBaseLen = -1;
		if (sjme_error_is(error = sjme_path_getName(
				subPath, subPathLen,
				0, &subBase, &subBaseLen, NULL)) ||
			subBase == NULL || subBaseLen < 0)
			return sjme_error_default(error);
		
#if defined(SJME_CONFIG_DEBUG)
		/* Debug. */
		sjme_message("Append single: `%.*s`",
			subBaseLen, subBase);
#endif
		/* Recalculate output length. */
		outLen = strlen(result);
		
		/* Add directory separator if the path is not empty and it does */
		/* not end a separator. */
		if (outLen > 0 &&
			strcmp(&result[outLen - 1], SJME_CONFIG_FILE_SEPARATOR)
#if SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_DOS
			|| strcmp(&result[outLen - 1], "/")
#endif
			)
		{
			sepLen = strlen(SJME_CONFIG_FILE_SEPARATOR) + 1; 
			memmove(&result[outLen], SJME_CONFIG_FILE_SEPARATOR,
				sizeof(*result) * sepLen);
		}
		
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
		inOutPathLen, inOutPath, subPathLen, subPath,
			(int)strlen(result), result);
#endif
	
	/* Success! Copy resultant path. */
	outLen = strlen(result) + 1;
	memmove(inOutPath, result, sizeof(*result) * outLen);
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_path_userHome(
	sjme_attrOutNotNullBuf(outPathLen) sjme_lpstr outPath,
	sjme_attrInPositiveNonZero sjme_jint outPathLen)
{
	if (outPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (outPathLen < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
#endif
