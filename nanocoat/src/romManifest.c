/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/romManifest.h"
#include "sjme/charSeq.h"
#include "sjme/util.h"

sjme_errorCode sjme_nvm_rom_manifestParseNext(
	sjme_attrInNotNull sjme_stream_input inputStream,
	sjme_attrInOutNotNull sjme_nvm_rom_manifestStep* inOutStep)
{
	sjme_errorCode error;
	sjme_jint c, kI, vI;
	sjme_jboolean wantKey, wantSpace, eatEol, continuationLine, firstChar;
	sjme_jboolean isSection, doInit;

	if (inOutStep == NULL || inputStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* These are initialized here for linter reasons, they are later */
	/* re-initialized accordingly. */
	wantKey = 0;
	wantSpace = 0;
	eatEol = 0;
	continuationLine = 0;
	isSection = 0;
	firstChar = 0;
	kI = 0;
	vI = 0;

	/* Constantly try to parse characters. */
	doInit = SJME_JNI_TRUE;
	for (;;)
	{
		/* Initialize everything? */
		if (doInit)
		{
			/* No longer initialize. */
			doInit = SJME_JNI_FALSE;

			/* Set state flags. */
			wantKey = SJME_JNI_TRUE;
			wantSpace = SJME_JNI_FALSE;
			eatEol = SJME_JNI_FALSE;
			continuationLine = SJME_JNI_FALSE;
			isSection = SJME_JNI_FALSE;
			firstChar = SJME_JNI_TRUE;
			kI = 0;
			vI = 0;

			/* The key and value are wiped because we will be filling into */
			/* them. */
			memset(inOutStep->map.key, 0,
				sizeof(inOutStep->map.key));
			memset(inOutStep->map.value, 0,
				sizeof(inOutStep->map.value));
		}

		/* This is an example manifest to be parsed: */
		/* ---------------------------------------------------------------- */
		/* Manifest-Version: 1.0 */
		/* X-SquirrelJME-InternalProjectName: cldc-compact */
		/* X-SquirrelJME-BuildVersion: w1bd8eb */
		/* X-SquirrelJME-API-Name: Connected Limited Device Configuration ( */
		/*  Compact) */
		/* X-SquirrelJME-API-Vendor: Stephanie Gawroriski */
		/* X-SquirrelJME-API-Version: 1.8.3 */
		/* X-SquirrelJME-PrefixCode: ZZ */
		/* X-SquirrelJME-DefinedConfigurations: CLDC-1.8.0-compact CLDC-1.1 */
		/*  .1 CLDC-1.1.0 CLDC-1.0.0 */
		/* */
		/* Name: Some sub-section */
		/* Whatever: This can be anything. */
		/* ---------------------------------------------------------------- */

		/* Use the lookahead character instead as this is used for checking */
		/* line continuations. */
		if (inOutStep->parse.lookahead > 0)
		{
			c = inOutStep->parse.lookahead;
			inOutStep->parse.lookahead = 0;
		}

		/* Read in the next character, otherwise. */
		else
		{
			/* Read in. */
			c = -1;
			if (sjme_error_is(error = sjme_stream_inputReadUtfChar(
				inputStream, &c)) || c < 0)
			{
				/* If this is EOF and there is something in the value map, */
				/* then this is just a manifest that ends in EOF. */
				if (error == SJME_ERROR_END_OF_FILE &&
					inOutStep->map.value[0] != 0)
					return SJME_ERROR_NONE;

				return sjme_error_default(error);
			}

			/* NUL is never valid for manifests. */
			if (c == 0)
				return SJME_ERROR_INVALID_MANIFEST_FORMAT;
		}

		/* If this happens to be a newline as the first character of a line */
		/* then this is a section. */
		if (firstChar && (c == '\r' || c == '\n'))
			isSection = SJME_JNI_TRUE;

		/* This can never be the case following the above check. */
		firstChar = SJME_JNI_FALSE;

		/* Wanting a space to be ignored? */
		if (wantSpace)
		{
			/* Not valid. */
			if (c != ' ')
				return SJME_ERROR_INVALID_MANIFEST_FORMAT;

			/* Reset and eat the space. */
			wantSpace = SJME_JNI_FALSE;
			continue;
		}

		/* Eating EOL characters? */
		if (eatEol)
		{
			/* Ignore these completely. */
			if (c == '\r' || c == '\n')
				continue;

			/* Stop eating EOL and push the character back in. */
			eatEol = SJME_JNI_FALSE;
			inOutStep->parse.lookahead = c;

			/* This can be a continuation. */
			continuationLine = SJME_JNI_TRUE;
			continue;
		}

		/* Wanting a key? */
		if (wantKey)
		{
			/* Start of a value? */
			if (c == ':')
			{
				/* A space must always follow a colon before the value. */
				wantSpace = SJME_JNI_TRUE;

				/* Transition. */
				wantKey = SJME_JNI_FALSE;
				continue;
			}

			/* Not a valid character for a header? */
			if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
				(c >= '0' && c <= '9') || c == '-' || c == '_'))
				return SJME_ERROR_INVALID_MANIFEST_FORMAT;

			/* Key would be too long? */
			if (kI >= SJME_NVM_ROM_MANIFEST_KEY_LENGTH - 1)
				return SJME_ERROR_INVALID_MANIFEST_FORMAT;

			/* Append to key name. */
			inOutStep->map.key[kI++] = (sjme_jchar)c;
		}

		/* Otherwise, parsing a value. */
		else
		{
			/* If this is a fresh line start, this may be a continuation. */
			if (continuationLine)
			{
				/* No longer fresh. */
				continuationLine = SJME_JNI_FALSE;

				/* It is one! */
				if (c == ' ')
					continue;

				/* It is not, so give the character back. */
				inOutStep->parse.lookahead = c;

				/* If this is not a section declaration, we can stop here. */
				if (!isSection)
					return SJME_ERROR_NONE;

				/* Move the value to the section name. */
				memmove(inOutStep->attr, inOutStep->map.value,
					sizeof(inOutStep->attr));

				/* Reset initialization and parse again. */
				doInit = SJME_JNI_TRUE;
				continue;
			}

			/* If this is a newline, then this may be the end of the value */
			/* a continuation following. */
			if (c == '\r' || c == '\n')
			{
				eatEol = SJME_JNI_TRUE;
				continue;
			}

			/* Value would be too long? */
			if (vI >= SJME_NVM_ROM_MANIFEST_VALUE_LENGTH - 1)
				return SJME_ERROR_INVALID_MANIFEST_FORMAT;

			/* Append to value. */
			inOutStep->map.value[vI++] = (sjme_jchar)c;
		}
	}

	/* Success, otherwise! */
	return SJME_ERROR_NONE;
}
