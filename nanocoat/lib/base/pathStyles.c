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

/** Directory seperator declaration. */
#define sjme_ds(a, b) {a, b}

/** Dot style declaration. */
#define sjme_fs(a, b, c, d) {{a, b}, {c, d}}

static sjme_errorCode sjme_path_dos_check(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path)
{
	sjme_jint i, n;
	sjme_lpcstr chars;
	sjme_cchar c;

	if (path == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Ensure no invalid characters are used. */
	chars = path->chars;
	for (n = path->length, i = 0; i < n; i++)
	{
		c = chars[i];
		if (c < 31 || c == 127 || c == '"' || c == '*' || c == '/' ||
			c == ':' || c == '<' || c == '>' || c == '?' || c == '|' ||
			c == '+' || c == ',' || c == '.' || c == ';' || c == '=' ||
			c == '[' || c == ']')
			return SJME_ERROR_PATH_NOT_VALID;
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_path_dos_finalize(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path)
{
	sjme_jint i, n;
	sjme_lpstr chars;
	sjme_cchar c;

	if (path == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Capitalize all lowercase characters. */
	chars = path->chars;
	for (n = path->length, i = 0; i < n; i++)
	{
		c = chars[i];
		if (c >= 'a' && c <= 'z')
			chars[i] = 'A' + (c - 'a');
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_path_dos_parseRoot(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_jint* outFLimit,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_lpcstr* outFStr,
	sjme_attrInOutNotNull sjme_lpcstr* walkPath)
{
	sjme_errorCode error;
	sjme_jint strLen;
	sjme_cchar c, d, e;
	
	if (path == NULL || outFLimit == NULL || outFStr == NULL ||
		walkPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* If Windows, check if this is a UNC path. */
	strLen = sjme_util_sizeToInt(strlen(*walkPath));
	if (path->style->type == SJME_PATH_STYLE_WINDOWS && strLen >= 3)
	{
		/* The first three characters determine if this is valid. */
		c = (*walkPath)[0];
		d = (*walkPath)[1];
		e = (*walkPath)[2];
		
		/* This is a UNC path. */
		if (c == '\\' && d == '\\')
		{
			/* The third character cannot be NUL or another separator. */
			if (e == '\0' || e == '\\' || e == '/')
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
	error = sjme_path_checkDirSep(path, (*walkPath), SJME_JNI_FALSE);
	if (error != SJME_ERROR_NO_SUCH_ELEMENT)
		return sjme_error_defaultOr(error, SJME_ERROR_PATH_NOT_VALID);
	
	/* The root is limited to this length, maximum. */
	strLen = sjme_min(strLen, 3);
	
	/* The root must start with a letter. */
	c = (*walkPath)[0];
	if (strLen <= 0 || !(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z'))
		return SJME_ERROR_NO_SUCH_ELEMENT;
	
	/* The second character must be a colon. */
	if (strLen < 2 || (*walkPath)[1] != ':')
		return SJME_ERROR_NO_SUCH_ELEMENT;
	
	/* The third character must be one of the path separators or NUL. */
	if ((*walkPath)[2] != '\0')
		if (sjme_error_is(error = sjme_path_checkDirSep(path,
			&(*walkPath)[2], SJME_JNI_FALSE)))
		{
			if (error == SJME_ERROR_NO_SUCH_ELEMENT)
				return SJME_ERROR_PATH_NOT_VALID;
			return sjme_error_default(error);
		}
	
	/* Consume the root. */
	*outFStr = (*walkPath);
	*outFLimit = strLen;
	
	/* Consume the slash and any redundant slashes. */
	(*walkPath) = &(*walkPath)[strLen];
	for (;; (*walkPath)++)
		if (sjme_error_is(error = sjme_path_checkDirSep(path, (*walkPath),
			SJME_JNI_FALSE)))
		{
			if (error != SJME_ERROR_NO_SUCH_ELEMENT)
				return sjme_error_default(error);
			break;
		}
	
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
	
	/* Nothing needs to be done here. */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_path_generic_finalize(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path)
{
	const sjme_path_styleSub* sep;
	const sjme_path_styleSub* alt;
	sjme_jint i, n;
	
	if (path == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Is separator conversion even needed? */
	sep = &path->style->dirSep[0];
	alt = &path->style->dirSep[1];
	if (sep->str == NULL || alt->str == NULL ||
		(sep->len == alt->len && sep->str[0] == alt->str[0]))
		return SJME_ERROR_NONE;
	
	/* Turn all alternative separators into the primary separator. */
	for (n = path->length, i = 0; i < n; i++)
		if (path->chars[i] == alt->str[0])
			path->chars[i] = sep->str[0];
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_path_generic_parseName(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_jint* outFLimit,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_lpcstr* outFStr,
	sjme_attrInOutNotNull sjme_lpcstr* walkPath)
{
	sjme_errorCode error;
	sjme_lpcstr begin, end;
	
	if (path == NULL || outFLimit == NULL || outFStr == NULL ||
		walkPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Start at the current base. */
	begin = (*walkPath);
	end = NULL;
	
	/* Find the next directory separator. */
	for (;;)
	{
		/* This is not a directory separator. */
		if (sjme_error_is(error = sjme_path_checkDirSep(path,
			(*walkPath), SJME_JNI_FALSE)))
		{
			/* Some other error. */
			if (error != SJME_ERROR_NO_SUCH_ELEMENT)
				return sjme_error_default(error);
			
			/* If the end was reached, stop, as this is the first character */
			/* that follows the separator. */
			if (end != NULL)
				break;
			
			/* Always explicitly stop at NUL. */
			if ((*walkPath)[0] == '\0')
			{
				end = (*walkPath);
				break;
			}
		}
		
		/* This is a directory separator. */
		else
		{
			/* If the end was not set yet, set it to the first separator. */
			if (end == NULL)
				end = (*walkPath);
		}
			
		/* Move up to the next character. */
		(*walkPath)++;
	}
	
	/* If end was never set, it is implicitly the end. */
	if (end == NULL)
		end = (*walkPath);
	
	/* There is no actual name? */
	if (end == begin)
		return SJME_ERROR_NO_SUCH_ELEMENT;
	
	/* Give the determined name, with the separator (if any). */
	*outFLimit = (sjme_jint)((sjme_intPointer)end - (sjme_intPointer)begin);
	*outFStr = begin;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_path_posix_parseRoot(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_jint* outFLimit,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_lpcstr* outFStr,
	sjme_attrInOutNotNull sjme_lpcstr* walkPath)
{
	sjme_jint strLen;
	sjme_cchar a, b, c;
	
	if (path == NULL || outFLimit == NULL || outFStr == NULL ||
		walkPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Read in the first three characters. */
	strLen = sjme_util_sizeToInt(strlen((*walkPath)));
	a = (strLen >= 1 ? (*walkPath)[0] : '\0');
	b = (strLen >= 2 ? (*walkPath)[1] : '\0');
	c = (strLen >= 3 ? (*walkPath)[2] : '\0');
	
	/* Double slash root? This is only valid with only two slashes. */
	/* Ignore with the generic none path style. */
	if (path->style->type != SJME_PATH_STYLE_NONE &&
		a == '/' && b == '/' && c != '/')
	{
		/* Use the base. */
		*outFLimit = 2;
		*outFStr = (*walkPath);
		
		/* Bump up by two. */
		(*walkPath) += 2;
		
		/* Success! */
		return SJME_ERROR_NONE;
	}
	
	/* Otherwise, if this does start with the root component then consume */
	/* all possible slashes. */
	if (a == '/')
	{
		/* Use the base. */
		*outFLimit = 1;
		*outFStr = (*walkPath);
		
		/* Eat every slash. */
		while ((*walkPath)[0] == '/')
			(*walkPath)++;
		
		/* Success! */
		return SJME_ERROR_NONE;
	}
	
	/* No root. */
	return SJME_ERROR_NO_SUCH_ELEMENT;
}

static sjme_errorCode sjme_path_vfat_check(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path)
{
	sjme_jint i, n;
	sjme_lpcstr chars;
	sjme_cchar c;

	if (path == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Ensure no invalid characters are used. */
	chars = path->chars;
	for (n = path->length, i = 0; i < n; i++)
	{
		c = chars[i];

		if (c < 31 || c == 127 || c == '"' || c == '*' || c == '/' ||
			c == ':' || c == '<' || c == '>' || c == '?' || c == '|')
			return SJME_ERROR_PATH_NOT_VALID;
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_path_windows_check(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path)
{
	if (path == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (path->style == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Forward to VFAT check. */
	return sjme_path_vfat_check(path);
}

const sjme_path_style sjme_path_styles[SJME_NUM_PATH_STYLES] =
{
	/** @link SJME_PATH_STYLE_NONE @endlink . */
	{
		sjme_sm(.type, SJME_PATH_STYLE_NONE),
		sjme_sm(.check, sjme_path_generic_check),
		sjme_sm(.parseRoot, sjme_path_posix_parseRoot),
		sjme_sm(.parseName, sjme_path_generic_parseName),
		sjme_sm(.parseFinalize, sjme_path_generic_finalize),
		sjme_sm(.dirSep, sjme_fs(1, "/", 1, "/")),
		sjme_sm(.pathSep, sjme_ds(1, ":")),
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
		sjme_sm(.dirSep, sjme_fs(1, "/", 1, "/")),
		sjme_sm(.pathSep, sjme_ds(1, ":")),
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
		sjme_sm(.dirSep, sjme_fs(1, "\\", 1, "\\")),
		sjme_sm(.pathSep, sjme_ds(1, ";")),
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
		sjme_sm(.dirSep, sjme_fs(1, "\\", 1, "/")),
		sjme_sm(.pathSep, sjme_ds(1, ";")),
		sjme_sm(.requireAbsolute, SJME_JNI_FALSE),
		sjme_sm(.dot, sjme_fs(1, ".", 2, ".\\")),
		sjme_sm(.dotDot, sjme_fs(2, "..", 3, "..\\")),
	},
	
	/** @link SJME_PATH_STYLE_WINDOWS @endlink . */
	{
		sjme_sm(.type, SJME_PATH_STYLE_WINDOWS),
		sjme_sm(.check, sjme_path_windows_check),
		sjme_sm(.parseRoot, sjme_path_dos_parseRoot),
		sjme_sm(.parseName, sjme_path_generic_parseName),
		sjme_sm(.parseFinalize, sjme_path_generic_finalize),
		sjme_sm(.dirSep, sjme_fs(1, "\\", 1, "/")),
		sjme_sm(.pathSep, sjme_ds(1, ";")),
		sjme_sm(.requireAbsolute, SJME_JNI_FALSE),
		sjme_sm(.dot, sjme_fs(1, ".", 2, ".\\")),
		sjme_sm(.dotDot, sjme_fs(2, "..", 3, "..\\")),
	},
};
