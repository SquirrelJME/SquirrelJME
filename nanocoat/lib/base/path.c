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
	{
		SJME_NVM_DEFAULT_DIRECTORY_BUCKET_EXTRA,
		"SQUIRRELJME_BUCKET_EXTRA",
		"",
		SJME_JNI_TRUE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_LIBRARIES,
		"SQUIRRELJME_LIBRARY_PATH",
		"",
		SJME_JNI_TRUE
	},

#if 0
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
#endif

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
		"squirreljme/state",
		SJME_JNI_FALSE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_STATE,
		"APPDATA",
		"squirreljme/state",
		SJME_JNI_FALSE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_STATE,
		"PROGRAMDATA",
		"squirreljme/state",
		SJME_JNI_FALSE
	},

	/* Natives just stay in Program Data. */
	{
		SJME_NVM_DEFAULT_DIRECTORY_NATIVES,
		"PROGRAMDATA",
		"squirreljme/natives",
		SJME_JNI_FALSE
	},

	/* Temporary files. */
	{
		SJME_NVM_DEFAULT_DIRECTORY_TEMPORARY,
		"TMP",
		"",
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

	/* Temporary files. */
	{
		SJME_NVM_DEFAULT_DIRECTORY_TEMPORARY,
		"TEMP",
		"",
		SJME_JNI_FALSE
	},
	{
		SJME_NVM_DEFAULT_DIRECTORY_TEMPORARY,
		NULL,
		"/tmp",
		SJME_JNI_FALSE
	},
#endif

	/* End. */
	{-1, NULL, NULL},
};

static sjme_errorCode sjme_path_append(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path,
	sjme_attrInPositiveNonZero sjme_jint len,
	sjme_attrInNotNull sjme_lpcstr str)
{
	sjme_jint newLen;
	
	if (path == NULL || str == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (len < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Path would end up being too long? */
	newLen = path->length + len;
	if (newLen > SJME_MAX_PATH)
		return SJME_ERROR_PATH_TOO_LONG;
	
	/* Copy all bytes directly. */
	memmove(&path->chars[path->length], str,
		sizeof(path->chars[0]) * len);
	
	/* Shift up lengths. */
	path->length = newLen;
	path->names[path->nameCount] = newLen;

#if defined(SJME_CONFIG_DEBUG_PATH) && defined(SJME_CONFIG_DEBUG_VERBOSE)
	sjme_message("[%d/%d]: %s <- %.*s",
		path->length, path->nameCount, path->chars,
		len, str);
#endif
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_path_appendName(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path,
	sjme_attrInPositiveNonZero sjme_jint len,
	sjme_attrInNotNull sjme_lpcstr str)
{
	if (path == NULL || str == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (len < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Path would exceed depth limit? */
	if (path->nameCount >= SJME_MAX_PATH_DEPTH)
		return SJME_ERROR_PATH_TOO_DEEP;
	
	/* The name after the last name is really just the length of the path, */
	/* so add that at the end. */
	path->names[++path->nameCount] = path->length;
	
	/* Continue to append, since it appends to the last name. */
	return sjme_path_append(path, len, str);
}

static sjme_errorCode sjme_path_defaultLookup(
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
		type >= SJME_NVM_NUM_DEFAULT_DIRECTORY_TYPES)
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
		if (buildPath.length > 0)
		{
			memmove(outPath, &buildPath, sizeof(buildPath));
			return SJME_ERROR_NONE;
		}
	}

	/* The path is not defined at all. */
	return SJME_ERROR_PATH_NOT_DEFINED;
}

static sjme_errorCode sjme_path_defaultStatic(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInValue sjme_nvm_defaultDirectoryType type,
	sjme_attrInNegativeOnePositive sjme_jint index)
{
	sjme_errorCode error;
	sjme_path envPath, buildPath;

	if (outPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (type <= SJME_NVM_DEFAULT_DIRECTORY_UNKNOWN ||
		type >= SJME_NVM_NUM_DEFAULT_DIRECTORY_TYPES)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (index < -1)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Use a default NAL? */
	if (nal == NULL)
		nal = &sjme_nal_default;

	/* Clear temporary paths. */
	memset(&envPath, 0, sizeof(envPath));
	memset(&buildPath, 0, sizeof(buildPath));

	/* Executable directory path? */
	if (type == SJME_NVM_DEFAULT_DIRECTORY_EXEC)
	{
		/* Get from the environment. */
		if (nal->execPath != NULL &&
			sjme_error_is(error = nal->execPath(envPath.chars,
				SJME_MAX_PATH - 1)))
			return sjme_error_default(error);

		/* Nothing here? */
		if (envPath.chars[0] == '\0')
			return SJME_ERROR_PATH_NOT_DEFINED;

		/* Parse path. */
		if (sjme_error_is(error = sjme_path_parseYN(nal,
			&buildPath, envPath.chars)))
			return sjme_error_default(error);

		/* Since this is the executable path, look at the parent directory. */
		if (sjme_error_is(error = sjme_path_getParent(&buildPath, &buildPath)))
			return sjme_error_default(error);
	}

	/* Default library directory. */
	else if (type == SJME_NVM_DEFAULT_DIRECTORY_LIBRARIES)
	{
		/* This starts from the data directory. */
		if (sjme_error_is(error = sjme_path_defaultLookup(nal, &buildPath,
			SJME_NVM_DEFAULT_DIRECTORY_DATA, -1)))
			return sjme_error_default(error);

		/* Then is just the "lib" subdirectory. */
		if (sjme_error_is(error = sjme_path_resolveS(
			&buildPath, "lib")))
			return sjme_error_default(error);
	}

	/* Non-volatile data bucket. */
	else if (type == SJME_NVM_DEFAULT_DIRECTORY_BUCKET_DATA)
	{
		/* This starts from the state directory. */
		if (sjme_error_is(error = sjme_path_defaultLookup(nal, &buildPath,
			SJME_NVM_DEFAULT_DIRECTORY_STATE, -1)))
			return sjme_error_default(error);

		/* Then is just the "data" subdirectory. */
		if (sjme_error_is(error = sjme_path_resolveS(
			&buildPath, "data")))
			return sjme_error_default(error);
	}

	/* The temporary directory should always exist, so use a fallback */
	/* just in case. */
	else if (type == SJME_NVM_DEFAULT_DIRECTORY_TEMPORARY)
	{
		/* We can place temporary files in the cache directory. */
		if (sjme_error_is(error = sjme_path_defaultLookup(nal, &buildPath,
			SJME_NVM_DEFAULT_DIRECTORY_CACHE, -1)))
			return sjme_error_default(error);

		/* Everything should be placed in a directory. */
		if (sjme_error_is(error = sjme_path_resolveS(
			&buildPath, "sjme.tmp")))
			return sjme_error_default(error);
	}

	/* As long as this is not a blank path, use it. */
	if (buildPath.length > 0)
	{
		memmove(outPath, &buildPath, sizeof(buildPath));
		return SJME_ERROR_NONE;
	}

	/* Otherwise, fail. */
	return SJME_ERROR_PATH_NOT_DEFINED;
}

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

	/* A style must be specified if non-blank. */
	if (path->length > 0 && path->style == NULL)
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
	/* The style could be NULL if this is a blank path. */
	if (path->style != NULL && path->style->requireAbsolute)
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
	const sjme_path_styleSub(*dot)[2];
	const sjme_path_styleSub(*dotDot)[2];

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
				0 == strncmp((*dot)[0].str, str, len)) ||
			(len == (*dot)[1].len &&
				0 == strncmp((*dot)[1].str, str, len)))
			return SJME_ERROR_PATH_NOT_ABSOLUTE;

		/* Dot-dot goes up a directory. */
		else if ((len == (*dotDot)[0].len &&
				0 == strncmp((*dotDot)[0].str, str, len)) ||
			(len == (*dotDot)[1].len &&
				0 == strncmp((*dotDot)[1].str, str, len)))
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

sjme_errorCode sjme_path_checkDirSep(
	sjme_attrInNotNull const sjme_path* path,
	sjme_attrInNotNull sjme_lpcstr string,
	sjme_attrInValue sjme_jboolean noAlt)
{
	const sjme_path_style* style;
	sjme_jint strLen;
	
	if (path == NULL || string == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* There must be a valid style. */
	style = path->style;
	if (style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Matches the first separator? */
	strLen = strlen(string);
	if (style->dirSep[0].len > 0 && strLen >= style->dirSep[0].len &&
		0 == strncmp(style->dirSep[0].str, string,
			style->dirSep[0].len))
		return SJME_ERROR_NONE;
	
	/* Matches the second separator? */
	if (!noAlt && style->dirSep[1].len > 0 && strLen >= style->dirSep[1].len &&
		0 == strncmp(style->dirSep[1].str, string,
			style->dirSep[1].len))
		return SJME_ERROR_NONE;
	
	/* Not found. */
	return SJME_ERROR_NO_SUCH_ELEMENT;
}

sjme_errorCode sjme_path_default(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInValue sjme_nvm_defaultDirectoryType type,
	sjme_attrInNegativeOnePositive sjme_jint index)
{
	sjme_errorCode error;
	
	if (outPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (type <= SJME_NVM_DEFAULT_DIRECTORY_UNKNOWN ||
		type >= SJME_NVM_NUM_DEFAULT_DIRECTORY_TYPES)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (index < -1)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Use a default NAL? */
	if (nal == NULL)
		nal = &sjme_nal_default;

	/* Static paths? */
	if (type == SJME_NVM_DEFAULT_DIRECTORY_EXEC ||
		type == SJME_NVM_DEFAULT_DIRECTORY_LIBRARIES ||
		type == SJME_NVM_DEFAULT_DIRECTORY_BUCKET_DATA)
		return sjme_path_defaultStatic(nal, outPath, type, -1);

	/* Use default lookup and resolution for paths. */
	if (sjme_error_is(error = sjme_path_defaultLookup(nal, outPath, type,
		index)))
	{
		/* Second chance path? */
		if (type == SJME_NVM_DEFAULT_DIRECTORY_TEMPORARY)
			return sjme_path_defaultStatic(nal, outPath, type, -1);

		/* Fail. */
		return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
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
	
	/* This is just a sub-path, but a single element. */
	return sjme_path_subPath(outPath, inPath,
		nameDx, nameDx + 1);
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
	
	/* If this is the root component, this will always be that root. */
	if (inPath->nameCount == 1 && (inPath->flags & SJME_PATH_HAS_ROOT) != 0)
	{
		memmove(outPath, inPath, sizeof(*outPath));
		return SJME_ERROR_NONE;
	}
	
	/* If this is the blank path or otherwise only has a single name left, */
	/* then it has no parent. */
	if (inPath->nameCount <= 1)
		return SJME_ERROR_NO_SUCH_ELEMENT;
	
	/* Otherwise, strip the last name. */
	return sjme_path_subPath(outPath, inPath,
		0, inPath->nameCount - 1);
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
	
	/* If there is no root, then there is none. */
	if ((inPath->flags & SJME_PATH_HAS_ROOT) == 0)
		return SJME_ERROR_NO_SUCH_ELEMENT;
	
	/* Otherwise, return only the first name. */
	return sjme_path_subPath(outPath, inPath, 0, 1);
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

sjme_errorCode sjme_path_parseYN(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull sjme_lpcstr strPath)
{
	sjme_errorCode error;
	const sjme_path_style* style;
	
	if (outPath == NULL || strPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Use default NAL? */
	if (nal == NULL)
		nal = &sjme_nal_default;
	
	/* Get the native path style. */
	style = NULL;
	error = SJME_ERROR_UNKNOWN;
	if (nal->pathStyle != NULL &&
		sjme_error_is(error = nal->pathStyle(&style)) ||
		style == NULL)
		return sjme_error_default(error);
	
	/* Parse the given path. */
	return sjme_path_parseYP(style, outPath, strPath);
}

sjme_errorCode sjme_path_parseYP(
	sjme_attrInValue const sjme_path_style* style,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull sjme_lpcstr strPath)
{
	sjme_errorCode error;
	sjme_path result;
	sjme_jint strLen, len;
	sjme_lpcstr walkAt, str, lastChar;
	sjme_cchar walkBuf[SJME_MAX_PATH + 1];
	
	if (style == NULL || outPath == NULL || strPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Sanity check on the various lengths. */
	if ((style->dirSep[0].len != -1 && style->dirSep[0].len != 1) ||
		(style->dirSep[1].len != -1 && style->dirSep[1].len != 1) ||
		(style->pathSep.len != 1) ||
		(style->dot[0].len != -1 && style->dot[0].len < 1) ||
		(style->dot[1].len != -1 && style->dot[1].len < 1) ||
		(style->dotDot[0].len != -1 && style->dotDot[0].len < 1) ||
		(style->dotDot[1].len != -1 && style->dotDot[1].len < 1))
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Make sure the path is not too long to start with. */
	strLen = strlen(strPath);
	if (strLen > SJME_MAX_PATH)
		return SJME_ERROR_PATH_TOO_LONG;
	
	/* Make a defensive copy of the input path. */
	memset(walkBuf, 0, sizeof(walkBuf));
	memmove(walkBuf, strPath, sizeof(walkBuf[0]) * strLen);
	walkAt = &walkBuf[0];
	
	/* Setup initial base path. */
	memset(&result, 0, sizeof(result));
	result.style = style;
	
	/* Determine if this path has a root component. */
	len = -1;
	str = NULL;
	if (sjme_error_is(error = result.style->parseRoot(&result,
		&len, &str, &walkAt)))
		if (error != SJME_ERROR_NO_SUCH_ELEMENT)
			return sjme_error_default(error);
	
	/* Add the root component, if there is one. */
	if (len > 0 && str != NULL)
	{
		/* Has a root. */
		result.flags |= SJME_PATH_HAS_ROOT;
		
		/* Append the name. */
		if (sjme_error_is(error = sjme_path_appendName(&result,
			len, str)))
			return sjme_error_default(error);
	}
	
	/* Keep processing names, provided there are names to parse. */
	while ((*walkAt) != '\0')
	{
		/* Parse the next name. */
		len = -1;
		str = NULL;
		if (sjme_error_is(error = result.style->parseName(&result,
			&len, &str, &walkAt)))
		{
			if (error != SJME_ERROR_NO_SUCH_ELEMENT)
				return sjme_error_default(error);
			
			/* Possible that there is some garbage to disregard. */
			break;
		}
		
		/* If there is no separator at the end, one must be added, if this */
		/* is not the first name. */
		if (result.nameCount > 0)
			if (sjme_error_is(error = sjme_path_checkDirSep(&result,
				&result.chars[result.length - 1], SJME_JNI_TRUE)))
			{
				if (error != SJME_ERROR_NO_SUCH_ELEMENT)
					return sjme_error_default(error);
				
				/* Add in the primary path separator. */
				if (sjme_error_is(error = sjme_path_append(&result,
					result.style->dirSep[0].len,
					result.style->dirSep[0].str)))
					return sjme_error_default(error);
			}
		
		/* Append the name. */
		if (sjme_error_is(error = sjme_path_appendName(&result,
			len, str)))
			return sjme_error_default(error);
	}
	
	/* If the path has no root, then it is relative. */
	if ((result.flags & SJME_PATH_HAS_ROOT) == 0)
		result.flags |= SJME_PATH_IS_RELATIVE;
	
	/* If this has a root and only has the root component, consider this a */
	/* directory. */
	if ((result.nameCount <= 1 &&
		(result.flags & SJME_PATH_HAS_ROOT) != 0))
		result.flags |= SJME_PATH_IS_DIRECTORY;
	
	/* If the last name contains a slash then consider this a directory. */
	if (result.nameCount >= 1)
	{
		/* Look at the last character. */
		lastChar = &result.chars[result.names[result.nameCount] - 1];
		if (!sjme_error_is(error = sjme_path_checkDirSep(
			&result, lastChar, SJME_JNI_FALSE)))
			result.flags |= SJME_PATH_IS_DIRECTORY;
		
		/* Some other error? */
		else if (error != SJME_ERROR_NO_SUCH_ELEMENT)
			return sjme_error_default(error);
	}
	
	/* Finalize the path. */
	if (sjme_error_is(error = result.style->parseFinalize(&result)))
		return sjme_error_default(error);
	
	/* The resultant path should be valid. */
	if (sjme_error_is(error = sjme_path_check(&result)))
		return sjme_error_default(error);
	
	/* Success! */
	memmove(outPath, &result, sizeof(*outPath));
	return SJME_ERROR_NONE;
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
	/* Note that blank paths, just get the name. */
	if (result.length > 0)
		if (sjme_error_is(error = sjme_path_checkDirSep(&result,
			&result.chars[result.length - 1], SJME_JNI_TRUE)))
		{
			if (error != SJME_ERROR_NO_SUCH_ELEMENT)
				return sjme_error_default(error);
			
			/* Add in the primary path separator. */
			if (sjme_error_is(error = sjme_path_append(&result,
				result.style->dirSep[0].len,
				result.style->dirSep[0].str)))
				return sjme_error_default(error);
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

sjme_errorCode sjme_path_resolveS(
	sjme_attrOutNotNull sjme_attrOutModify sjme_path* outPath,
	sjme_attrInNotNull sjme_lpcstr inPath)
{
	sjme_errorCode error;
	sjme_path parsed;
	const sjme_path_style* style;

	if (outPath == NULL || inPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (sjme_error_is(error = sjme_path_check(outPath)))
		return sjme_error_default(error);
	
	/* This could be a default blank path. */
	style = outPath->style;
	if (style == NULL && outPath->length == 0)
		if (sjme_error_is(error = sjme_nal_default.pathStyle(&style)))
			return sjme_error_default(error);
	
	/* Parse the input path first. */
	memset(&parsed, 0, sizeof(parsed));
	if (sjme_error_is(error = sjme_path_parseYP(style,
		&parsed, inPath)))
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
	sjme_jint charBase, i;
	sjme_path result;

	if (outPath == NULL || inPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (beginDx < 0 || endDx < 0 || endDx <= beginDx)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	if (sjme_error_is(error = sjme_path_check(inPath)))
		return sjme_error_default(error);

	if (beginDx >= inPath->nameCount || endDx > inPath->nameCount)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* The character base is where to start copying from. */
	charBase = inPath->names[beginDx];
	
	/* Determine the actual new name count. */
	memset(&result, 0, sizeof(result));
	result.style = inPath->style;
	result.nameCount = endDx - beginDx;
	result.length = inPath->names[beginDx + (result.nameCount)] - charBase;
	
	/* Copy names over, include length end name. */
	memmove(&result.names[0], &inPath->names[beginDx],
		sizeof(inPath->names[0]) * (result.nameCount + 1));
	
	/* Copy characters over. */
	memmove(&result.chars[0], &inPath->chars[charBase],
		sizeof(result.chars[0]) * result.length);
	
	/* Correct lengths and offsets. */
	for (i = 0; i <= result.nameCount; i++)
		result.names[i] -= charBase;
	
	/* If the input path has a root, and we did begin from the root, then */
	/* the target also gets the root flag. */
	if (beginDx == 0 && (inPath->flags & SJME_PATH_HAS_ROOT) != 0)
		result.flags |= SJME_PATH_HAS_ROOT;
	
	/* Otherwise, this becomes a relative path. */
	else
		result.flags |= SJME_PATH_IS_RELATIVE;
	
	/* Make sure the resultant path is valid. */
	if (sjme_error_is(error = sjme_path_check(&result)))
		return sjme_error_default(error);
	
	/* Success! */
	memmove(outPath, &result, sizeof(*outPath));
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_path_userHome(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath)
{
	sjme_errorCode error;
	sjme_cchar env[SJME_MAX_PATH];
	const sjme_path_style* style;

	if (outPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Use a default NAL? */
	if (nal == NULL)
		nal = &sjme_nal_default;

	/* No NAL implementation? */
	if (nal->userHome == NULL)
		return SJME_ERROR_PATH_NOT_DEFINED;

	/* Request from NAL. */
	memset(env, 0, sizeof(env));
	if (sjme_error_is(error = nal->userHome(env, SJME_MAX_PATH - 1)))
		return sjme_error_default(error);
	env[SJME_MAX_PATH - 1] = '\0';

	/* Get the native path style. */
	style = NULL;
	if (sjme_error_is(error = nal->pathStyle(&style)) || style == NULL)
		return sjme_error_default(error);

	/* Parse the path. */
	return sjme_path_parseYP(style, outPath, env);
}
