/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/dylibExtra.h"
#include "sjme/path.h"
#include "sjme/util.h"
#include "sjme/externalWeak.h"

#if !defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)

/** ScritchAudio library order. */
static const sjme_lpcstr sjme_dylib_extraAudio[] =
{
#if defined(SJME_CONFIG_HAS_OS_POSIX)
	"audio-oss",
#endif
	
#if defined(SJME_CONFIG_HAS_OS_WINDOWS) || \
	defined(SJME_CONFIG_HAS_OS_WINDOWS_CE)
	"audio-winmm",
#endif
	
	NULL,
};

/** ScritchUI library order. */
static const sjme_lpcstr sjme_dylib_extraUi[] =
{
#if defined(SJME_CONFIG_HAS_OS_WINDOWS)
	"ui-win32",
#elif defined(SJME_CONFIG_HAS_OS_MACOS)
	"ui-cocoa",
#elif defined(SJME_CONFIG_HAS_OS_BSD_FAMILY) || \
	defined(SJME_CONFIG_HAS_OS_CYGWIN) || \
	defined(SJME_CONFIG_HAS_OS_LINUX) || \
	defined(SJME_CONFIG_HAS_OS_POSIX)
	"ui-wayland",
	"ui-x11",
#endif

	/* More Modern. */
	"ui-qt6",
	"ui-gtk4",
	"ui-wayland",

	/* Recent enough. */
	"ui-qt5",
	"ui-qt4"
	"ui-gtk3",

	/* Older. */
	"ui-gtk2",
	"ui-motif",
	"ui-tk",
	"ui-x11",

	/* System specific interfaces. */
	"ui-cocoa",
	"ui-palmos",
	"ui-toolbox",
	"ui-win32",

	NULL,
};

/** Possible library directories. */
static sjme_nvm_defaultDirectoryType sjme_dylib_libDirs[] =
{
	SJME_NVM_DEFAULT_DIRECTORY_RUNTIME,
	SJME_NVM_DEFAULT_DIRECTORY_NATIVES,
	SJME_NVM_DEFAULT_DIRECTORY_EXEC,

	SJME_NVM_DEFAULT_DIRECTORY_UNKNOWN,
};

static sjme_errorCode sjme_dylib_openExtraScritchAny(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInNullable sjme_lpcstr subComponent,
	sjme_attrOutNotNull sjme_dylib* outLib,
	sjme_attrOutNullable sjme_lpcstr* outFoundComponent,
	sjme_attrInNotNull const sjme_lpcstr* order)
{
#if !defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	sjme_errorCode error;
	sjme_jint i, dir;
	sjme_lpcstr orderComponent;
	sjme_path tryPath;
	sjme_cchar tempName[SJME_MAX_FILE_NAME];
	sjme_dylib result;
	sjme_lpcstr actualSubComponent;
#endif
	
	if (nal == NULL || outLib == NULL || order == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if !defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	/* Go through all components, use the passed component, if any. */
	result = NULL;
	actualSubComponent = NULL;
	for (i = (subComponent != NULL ? -1 : 0);; i++)
	{
		/* Stop when all possible components have been checked. */
		/* Unless only a specific component was requested. */
		orderComponent = (i < 0 ? subComponent : order[i]);
		if (orderComponent == NULL ||
			(subComponent != NULL && i == 0))
			break;

		/* This is the subcomponent that would be used, if this was */
		/* successful */
		actualSubComponent = subComponent;

		/* Determine dynamic library name. */
		memset(tempName, 0, sizeof(tempName));
		if (sjme_error_is(error = sjme_dylib_name(
			"squirreljme-scritch", orderComponent,
			tempName, SJME_MAX_FILE_NAME - 1)))
			return sjme_error_default(error);
		tempName[SJME_MAX_FILE_NAME - 1] = '\0';

		/* Search each directory in order. */
		for (dir = 0; sjme_dylib_libDirs[dir] > 0; dir++)
		{
			/* Determine base path to use. */
			memset(&tryPath, 0, sizeof(tryPath));
			if (sjme_error_is(error = sjme_path_default(nal,
				&tryPath, sjme_dylib_libDirs[dir], -1)))
			{
				/* Path is defined, however checks failed for it. */
				if (error == SJME_ERROR_PATH_NOT_ABSOLUTE ||
					error == SJME_ERROR_PATH_TOO_DEEP ||
					error == SJME_ERROR_PATH_TOO_LONG ||
					error == SJME_ERROR_PATH_NOT_VALID ||
					error == SJME_ERROR_PATH_NOT_DEFINED)
					continue;

				/* Failed with something else. */
				return sjme_error_default(error);
			}

			/* Resolve from this path. */
			if (sjme_error_is(error = sjme_path_resolveS(&tryPath, tempName)))
				return sjme_error_default(error);

			/* Attempt loading the library. */
			if (sjme_error_is(error = sjme_dylib_open(tryPath.chars,
				&result)) || result == NULL)
			{
				if (error != SJME_ERROR_COULD_NOT_LOAD_LIBRARY)
					return sjme_error_default(error);

				/* Try again. */
				continue;
			}

			/* A library was found, so stop. */
			break;
		}
	}

	/* Was a library found? */
	if (result != NULL)
	{
		if (outFoundComponent != NULL)
			*outFoundComponent = actualSubComponent;
		*outLib = result;
		return SJME_ERROR_NONE;
	}
#endif

	/* No library was found. */
	return SJME_ERROR_LIBRARY_NOT_FOUND;
}

#endif

sjme_errorCode sjme_dylib_preferredExtra(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInRange(0, SJME_DYLIB_NUM_EXTRA_FAMILY)
		sjme_dylib_extraFamily family,
	sjme_attrOutNotNull sjme_lpcstr* outSubComponent)
{
#define MAX_XDG_NAME 32
	sjme_errorCode error;
	sjme_lpcstr prefer;
#if defined(SJME_CONFIG_HAS_OS_BSD_FAMILY) || \
	defined(SJME_CONFIG_HAS_OS_CYGWIN) || \
	defined(SJME_CONFIG_HAS_OS_LINUX) || \
	defined(SJME_CONFIG_HAS_OS_POSIX)
	sjme_cchar xdgName[MAX_XDG_NAME];
#endif

	if (outSubComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (family <= SJME_DYLIB_EXTRA_FAMILY_UNKNOWN ||
		family >= SJME_DYLIB_NUM_EXTRA_FAMILY)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Default NAL? */
	if (nal == NULL)
		nal = &sjme_nal_default;

	/* Default to nothing. */
	prefer = NULL;

#if defined(SJME_CONFIG_HAS_OS_BSD_FAMILY) || \
	defined(SJME_CONFIG_HAS_OS_CYGWIN) || \
	defined(SJME_CONFIG_HAS_OS_LINUX) || \
	defined(SJME_CONFIG_HAS_OS_POSIX)
	/* Determine a default UI based on the XDG standard. */
	memset(xdgName, 0, sizeof(xdgName));
	if (nal->getEnv != NULL)
		if (sjme_error_is(error = nal->getEnv(
			&xdgName[0], MAX_XDG_NAME - 1,
			"XDG_CURRENT_DESKTOP")))
		{
			/* These specific errors are okay and should not cause */
			/* this to fail. */
			if (error != SJME_ERROR_NO_SUCH_ELEMENT &&
				error != SJME_ERROR_INDEX_OUT_OF_BOUNDS &&
				error != SJME_ERROR_NOT_IMPLEMENTED)
				return sjme_error_default(error);

			/* Wipe so that it is invalidated. */
			memset(xdgName, 0, sizeof(xdgName));
		}

	/* Defaults which seem to make sense for ScritchUI. */
	if (family == SJME_DYLIB_EXTRA_FAMILY_SCRITCHUI &&
		prefer == NULL)
	{
		if (0 == strncasecmp(xdgName, "KDE", MAX_XDG_NAME) ||
			0 == strncasecmp(xdgName, "LXQt", MAX_XDG_NAME))
			prefer = "qt5";
		else if (0 == strncasecmp(xdgName, "Cinnamon", MAX_XDG_NAME) ||
			0 == strncasecmp(xdgName, "GNOME", MAX_XDG_NAME))
			prefer = "gtk3";
		else if (0 == strncasecmp(xdgName, "LXDE", MAX_XDG_NAME) ||
			0 == strncasecmp(xdgName, "MATE", MAX_XDG_NAME))
			prefer = "gtk2";
		else if (0 == strncasecmp(xdgName, "wmaker", MAX_XDG_NAME) ||
			0 == strncasecmp(xdgName, "windowmaker", MAX_XDG_NAME))
			prefer = "cocoa";
	}
#endif

	/* Was there a preference? */
	*outSubComponent = prefer;
	return SJME_ERROR_NONE;
#undef MAX_XDG_NAME
}

sjme_errorCode sjme_dylib_openExtra(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInRange(0, SJME_DYLIB_NUM_EXTRA_FAMILY)
		sjme_dylib_extraFamily family,
	sjme_attrInNullable sjme_lpcstr subComponent,
	sjme_attrOutNotNull sjme_dylib* outLib,
	sjme_attrOutNullable sjme_lpcstr* outFoundComponent)
{
#define NUM_COMPONENTS 4
#if !defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	sjme_errorCode error;
	sjme_jint i, n, tryCount;
	sjme_cchar at;
	sjme_lpcstr tryComponent[NUM_COMPONENTS];
	sjme_lpcstr extraDefault, byFamily;
	sjme_dylib result;
	sjme_lpcstr actualSubComponent;
#endif
	
	if (outLib == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (family <= 0 || family >= SJME_DYLIB_NUM_EXTRA_FAMILY)
		return SJME_ERROR_INVALID_ARGUMENT;

#if !defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	/* Fallback to default NAL? */
	if (nal == NULL)
		nal = &sjme_nal_default;

	/* Multiple fallbacks of these may be used. */
	tryCount = 0;
	memset(tryComponent, 0, sizeof(tryComponent));

	/* If a subcomponent is passed, make sure it does not have a wonky */
	/* set of characters. */
	if (subComponent != NULL)
	{
		/* Check for validity. */
		for (n = sjme_util_sizeToInt(strlen(subComponent)), i = 0;
			i < n; i++)
		{
			at = subComponent[i];
			if (at == '\\' || at == '/' || at == ':' || at <= ' ')
				return SJME_ERROR_SECURITY_EXCEPTION;
		}

		/* This is valid so use it. */
		tryComponent[tryCount++] = subComponent;
	}

	/* What is the external default interface. */
	extraDefault = NULL;
	if (sjme_error_is(error = sjme_extern_extraFamilyDefault(family,
		&extraDefault)))
		return sjme_error_default(error);

	/* Was there one? */
	if (extraDefault != NULL)
		tryComponent[tryCount++] = extraDefault;

	/* Check for one, depending on the family and system. */
	byFamily = NULL;
	if (sjme_error_is(error = sjme_dylib_preferredExtra(
		nal, family, &byFamily)))
		return sjme_error_default(error);

	/* Was there one? */
	if (byFamily != NULL)
		tryComponent[tryCount++] = byFamily;

	/* Try everything. */
	/* Note <= because the last tryComponent is NULL. */
	for (i = 0; i <= tryCount; i++)
	{
		/* Which family type to load? */
		result = NULL;
		actualSubComponent = NULL;
		switch (family)
		{
			case SJME_DYLIB_EXTRA_FAMILY_SCRITCHUI:
				error = sjme_dylib_openExtraScritchAny(nal,
					tryComponent[i], &result, &actualSubComponent,
					&sjme_dylib_extraUi[0]);
				break;

			case SJME_DYLIB_EXTRA_FAMILY_SCRITCHAUDIO:
				error = sjme_dylib_openExtraScritchAny(nal,
					tryComponent[i], &result, &actualSubComponent,
					&sjme_dylib_extraAudio[0]);
				break;

			default:
				sjme_todo("Impl?");
				return sjme_error_notImplemented(0);
		}

		/* Did any of these fail? */
		if (sjme_error_is(error))
		{
			/* This error is fine. */
			if (error == SJME_ERROR_COULD_NOT_LOAD_LIBRARY ||
				error == SJME_ERROR_LIBRARY_NOT_FOUND)
				continue;

			/* Otherwise, any other error is bad. */
			return sjme_error_default(error);
		}

		/* Was an actual library opened? */
		if (result != NULL)
		{
			/* The found component may be important for debugging or loading */
			/* a library. */
			if (outFoundComponent != NULL)
				*outFoundComponent = actualSubComponent;

			*outLib = result;
			return SJME_ERROR_NONE;
		}
	}
#endif

	/* Not found. */
	return SJME_ERROR_LIBRARY_NOT_FOUND;
#undef NUM_COMPONENTS
}
