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
#include "sjme/util.h"

static sjme_errorCode sjme_path_dos_check(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path)
{
	if (path == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_path_dos_finalize(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path)
{
	if (path == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_path_dos_parseRoot(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_jint* outFLimit,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_lpcstr* outFStr,
	sjme_attrInOutNotNull sjme_lpcstr* walkPath)
{
	sjme_jint strLen;
	sjme_cchar c, d, e;
	
	if (path == NULL || outFLimit == NULL || outFStr == NULL ||
		walkPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* If Windows, check if this is a UNC path. */
	strLen = strlen(*walkPath);
	if (path->style->type == SJME_PATH_STYLE_WINDOWS && strLen >= 3)
	{
		/* The first three characters determine if this is valid. */
		c = (*walkPath)[0];
		d = (*walkPath)[1];
		e = (*walkPath)[2];
		
		/* This is a UNC path. */
		if (c == SJME_CONFIG_FILE_SEPARATOR &&
			d == SJME_CONFIG_FILE_SEPARATOR)
		{
			/* The third character cannot be NUL or another separator. */
			if (e == '\0' || e == SJME_CONFIG_FILE_SEPARATOR ||
				e == SJME_CONFIG_FILE_SEPARATOR_ALT)
				return SJME_ERROR_PATH_NOT_VALID;
			
			/* Consume the root. */
			*outFStr = (*walkPath);
			*outFLimit = 2;
			
			/* Move up two since that is the UNC slashes. */
			(*walkPath) += 2;
			
			/* Success! */
			return SJME_ERROR_NONE;
		}
	}
	
	/* Cannot start with any slash. */
	c = (*walkPath)[0];
	if (c == SJME_CONFIG_FILE_SEPARATOR ||
		c == SJME_CONFIG_FILE_SEPARATOR_ALT)
		return SJME_ERROR_PATH_NOT_VALID;
	
	/* The root is limited to this length, maximum. */
	strLen = sjme_min(strLen, 3);
	
	/* The root must start with a letter. */
	if (strLen <= 0 || !(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z'))
		return SJME_ERROR_NO_SUCH_ELEMENT;
	
	/* The second character must be a colon. */
	if (strLen < 2 || (*walkPath)[2] != ':')
		return SJME_ERROR_NO_SUCH_ELEMENT;
	
	/* The third character must be one of the path separators or NUL. */
	c = (*walkPath)[2];
	if (c != SJME_CONFIG_FILE_SEPARATOR &&
		c != SJME_CONFIG_FILE_SEPARATOR_ALT &&
		c != '\0')
		return SJME_ERROR_PATH_NOT_VALID;
	
	/* Consume the root. */
	*outFStr = (*walkPath);
	*outFLimit = strLen;
	
	/* Consume the slash and any redundant slashes. */
	(*walkPath) = &(*walkPath)[strLen];
	while ((*walkPath)[0] == SJME_CONFIG_FILE_SEPARATOR ||
		(*walkPath)[0] == SJME_CONFIG_FILE_SEPARATOR_ALT)
		(*walkPath)++;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_path_generic_check(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path)
{
	if (path == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_path_generic_finalize(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path)
{
	if (path == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_path_generic_parseName(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_jint* outFLimit,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_lpcstr* outFStr,
	sjme_attrInOutNotNull sjme_lpcstr* walkPath)
{
	if (path == NULL || outFLimit == NULL || outFStr == NULL ||
		walkPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_path_posix_parseRoot(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_jint* outFLimit,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_lpcstr* outFStr,
	sjme_attrInOutNotNull sjme_lpcstr* walkPath)
{
	if (path == NULL || outFLimit == NULL || outFStr == NULL ||
		walkPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_path_vfat_check(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path)
{
	if (path == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_path_windows_check(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path)
{
	if (path == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

/** Directory seperator declaration. */
#define sjme_ds(a, b) {a, b}

/** Dot style declaration. */
#define sjme_fs(a, b, c, d) {{a, b}, {c, d}}

const sjme_path_style sjme_path_styles[SJME_NUM_PATH_STYLES] =
{
	/** @link SJME_PATH_STYLE_NONE @endlink . */
	{
		sjme_sm(.type, SJME_PATH_STYLE_NONE),
		sjme_sm(.check, sjme_path_generic_check),
		sjme_sm(.parseRoot, sjme_path_posix_parseRoot),
		sjme_sm(.parseName, sjme_path_generic_parseName),
		sjme_sm(.parseFinalize, sjme_path_generic_finalize),
		sjme_sm(.dirSep, sjme_ds("/", "/")),
		sjme_sm(.pathSep, ":"),
		sjme_sm(.requireAbsolute, SJME_JNI_TRUE),
		sjme_sm(.dot, sjme_fs(-1, "", -1, "")),
		sjme_sm(.dotDot, sjme_fs(-1, "", -1, "")),
	},
	
	/** @link SJME_PATH_STYLE_POSIX @endlink . */
	{
		sjme_sm(.type, SJME_PATH_STYLE_POSIX),
		sjme_sm(.check, sjme_path_generic_check),
		sjme_sm(.parseRoot, sjme_path_posix_parseRoot),
		sjme_sm(.parseName, sjme_path_generic_parseName),
		sjme_sm(.parseFinalize, sjme_path_generic_finalize),
		sjme_sm(.dirSep, sjme_ds("/", "/")),
		sjme_sm(.pathSep, ":"),
		sjme_sm(.requireAbsolute, SJME_JNI_FALSE),
		sjme_sm(.dot, sjme_fs(1, ".", 2, "./")),
		sjme_sm(.dotDot, sjme_fs(2, "..", 3, "../")),
	},
	
	/** @link SJME_PATH_STYLE_DOS @endlink . */
	{
		sjme_sm(.type, SJME_PATH_STYLE_DOS),
		sjme_sm(.check, sjme_path_dos_check),
		sjme_sm(.parseRoot, sjme_path_dos_parseRoot),
		sjme_sm(.parseName, sjme_path_generic_parseName),
		sjme_sm(.parseFinalize, sjme_path_dos_finalize),
		sjme_sm(.dirSep, sjme_ds("\\", "\\")),
		sjme_sm(.pathSep, ";"),
		sjme_sm(.requireAbsolute, SJME_JNI_FALSE),
		sjme_sm(.dot, sjme_fs(1, ".", 2, ".\\")),
		sjme_sm(.dotDot, sjme_fs(2, "..", 3, "..\\")),
	},
	
	/** @link SJME_PATH_STYLE_VFAT @endlink . */
	{
		sjme_sm(.type, SJME_PATH_STYLE_VFAT),
		sjme_sm(.check, sjme_path_vfat_check),
		sjme_sm(.parseRoot, sjme_path_dos_parseRoot),
		sjme_sm(.parseName, sjme_path_generic_parseName),
		sjme_sm(.parseFinalize, sjme_path_generic_finalize),
		sjme_sm(.dirSep, sjme_ds("\\", "/")),
		sjme_sm(.pathSep, ";"),
		sjme_sm(.requireAbsolute, SJME_JNI_FALSE),
		sjme_sm(.dot, sjme_fs(1, ".", 2, ".\\")),
		sjme_sm(.dotDot, sjme_fs(2, "..", 3, "..\\")),
	},
	
	/** @link SJME_PATH_STYLE_WINDOWS @endlink . */
	{
		sjme_sm(.check, sjme_path_windows_check),
		sjme_sm(.parseRoot, sjme_path_dos_parseRoot),
		sjme_sm(.parseName, sjme_path_generic_parseName),
		sjme_sm(.parseFinalize, sjme_path_generic_finalize),
		sjme_sm(.dirSep, sjme_ds("\\", "/")),
		sjme_sm(.pathSep, ";"),
		sjme_sm(.requireAbsolute, SJME_JNI_FALSE),
		sjme_sm(.dot, sjme_fs(1, ".", 2, ".\\")),
		sjme_sm(.dotDot, sjme_fs(2, "..", 3, "..\\")),
	},
};
