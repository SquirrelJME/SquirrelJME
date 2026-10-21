/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/romMeepSwm.h"
#include "sjme/listUtil.h"
#include "sjme/util.h"
#include "sjme/nvm/boot.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/romManifest.h"

#pragma region(swmHandlerDefs)

typedef sjme_errorCode (*sjme_nvm_rom_swmKeyMapFunc)(
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrInOutNotNull sjme_nvm_rom_swmEntry* tempEntry,
	sjme_attrInNotNull const sjme_nvm_rom_manifestStep* step,
	sjme_attrInOutNotNull sjme_lpcwstr* lpwv);

typedef struct sjme_nvm_rom_swmKeyMapHandler
{
	/** The key being handled. */
	sjme_lpcstr key;

	/** The key length to compare, @code -1 @endcode is all characters. */
	sjme_jint keyLen;

	/** The function to actually handle the key. */
	sjme_nvm_rom_swmKeyMapFunc handler;
} sjme_nvm_rom_swmKeyMapHandler;

/** Function prototype for handling keys. */
#define SJME_MEEP_SWM_FUNC(func) \
	static sjme_errorCode SJME_TOKEN_PASTE(sjme_nvm_rom_swmKey_, func)( \
		sjme_attrInNotNull sjme_nvm_rom_suite inSuite, \
		sjme_attrInNotNull sjme_nvm_rom_library inLibrary, \
		sjme_attrInOutNotNull sjme_nvm_rom_swmEntry* tempEntry, \
		sjme_attrInNotNull const sjme_nvm_rom_manifestStep* step, \
		sjme_attrInOutNotNull sjme_lpcwstr* lpwv)

/** Generic input checks. */
#define SJME_MEEP_SWM_FUNC_CHECK() \
	do { \
		if (inSuite == NULL || inLibrary == NULL || tempEntry == NULL || \
			step == NULL || lpwv == NULL) \
			return SJME_ERROR_NULL_ARGUMENTS; \
	} while (0)

#define SJME_MEEP_SWM_SHORT(key, keyLen, func) \
	{ \
		key, keyLen, \
		SJME_TOKEN_PASTE(sjme_nvm_rom_swmKey_, func), \
	}

#pragma endregion(swmHandlerDefs)
#pragma region(swmHandlers)

/** Zero version number. */
static const sjme_nvm_swmVersion sjme_nvm_rom_zeroVersion =
{
	0,
	0,
	0,
	SJME_JNI_FALSE,
};

static sjme_errorCode sjme_nvm_rom_swmParseVersion(
	sjme_attrOutNotNull sjme_nvm_swmVersion* outVer,
	sjme_attrInNotNull sjme_lpcwstr in,
	sjme_attrInPositiveNonZero sjme_jint inLen)
{
	sjme_lpcwstr end, left, right, term;
	sjme_lpcwstr majP, minP, relP;
	sjme_intMax maj, min, rel;

	if (outVer == NULL || in == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inLen <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* This is used for disregarding values. */
	end = &in[inLen];

	/* Find the left dot. */
	left = sjme_lpwcschr(in, '.');
	if (left == NULL || left >= end)
		return SJME_ERROR_INVALID_MANIFEST_VALUE;

	/* Find the right dot, this is optional. */
	right = sjme_lpwcschr(left + 1, '.');
	if (right == NULL || right >= end)
		right = end;

	/* Is there a dash terminator for -compact? */
	term = sjme_lpwcschr(right + 1, '-');
	if (term == NULL || term >= end)
		term = end;

	/* Used to check for extra garbage values. */
	majP = NULL;
	minP = NULL;
	relP = NULL;

	/* Parse the individual values. */
	maj = sjme_lpwcstoimax(in, &majP, 10);
	min = sjme_lpwcstoimax(left + 1, &minP, 10);

	/* The release is an optional value. */
	if (right != end && right + 1 != end)
	{
		rel = sjme_lpwcstoimax(right + 1, &relP, 10);

		/* Did not parse a version? */
		if (relP == NULL || relP != term)
			return SJME_ERROR_INVALID_MANIFEST_VALUE;
	}
	else
		rel = 0;

	/* Are any of these invalid? */
	/* Note that the end pointers must land exactly on their right side. */
	if (maj > 99 || min > 99 || rel > 99 ||
		maj < 0 || min < 0 || rel < 0 ||
		majP == NULL || minP == NULL ||
		majP != left || minP != right)
		return SJME_ERROR_INVALID_MANIFEST_VALUE;

	/* Fill in. */
	outVer->major = (sjme_jubyte)maj;
	outVer->minor = (sjme_jubyte)min;
	outVer->release = (sjme_jubyte)rel;

	/* Is this compact? */
	if (term != end)
		outVer->isCompact = (0 == sjme_string_compareIAWN(
			"-compact", -1,
			term, end - term));
	else
		outVer->isCompact = SJME_JNI_FALSE;

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_rom_swmParseSuite(
	sjme_attrOutNotNull sjme_nvm_swmInfoAll* outSuite,
	sjme_attrInNotNull sjme_lpcwstr in,
	sjme_attrInPositiveNonZero sjme_jint inLen)
{
	sjme_errorCode error;
	sjme_lpcwstr end, semiA, semiB;

	/* bluetooth.api;;1.0.0 */

	if (outSuite == NULL || in == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inLen <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* End is always here. */
	end = &in[inLen];

	/* Find first semicolon, this is required. */
	semiA = sjme_lpwcschr(in, ';');
	if (semiA == NULL || semiA == in || semiA >= end)
		return SJME_ERROR_INVALID_MANIFEST_VALUE;

	/* Find second semicolon, this is optional. */
	semiB = sjme_lpwcschr(semiA + 1, ';');
	if (semiB == NULL || semiB >= end)
		return SJME_ERROR_INVALID_MANIFEST_VALUE;

	/* Parse version, which is optional. */
	if (semiB != end && semiB + 1 !=  end)
	{
		if (sjme_error_is(error = sjme_nvm_rom_swmParseVersion(
			&outSuite->version, semiB + 1, (end - semiB) - 1)))
			return sjme_error_default(error);
	}

	/* Otherwise, set version to zero. */
	else
	{
		outSuite->version.major = 0;
		outSuite->version.minor = 0;
		outSuite->version.release = 0;
		outSuite->version.isCompact = SJME_JNI_FALSE;
	}

	/* Copy name. */
	sjme_lpwcsncat(outSuite->name, in, semiA - in);

	/* Copy vendor, if it is provided. */
	if (semiA != semiB && semiB + 1 != semiA)
		sjme_lpwcsncat(outSuite->name, semiA + 1, (semiB - semiA) - 1);

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_rom_swmParseNamedVersion(
	sjme_attrOutNotNull sjme_nvm_swmInfoNameVersion* outNameVer,
	sjme_attrInNotNull sjme_lpcwstr in,
	sjme_attrInPositiveNonZero sjme_jint inLen)
{
	sjme_errorCode error;
	sjme_lpcwstr end, dash;

	if (outNameVer == NULL || in == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inLen <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* End is always here. */
	end = &in[inLen];

	/* There needs to be a dash, if not then this is not going to be a */
	/* configuration/profile of any kind. */
	dash = sjme_lpwcschr(in, '-');
	if (dash == NULL || dash > end)
		return SJME_ERROR_INVALID_MANIFEST_VALUE;

	/* Copy over the configuration name. */
	sjme_lpwcsncat(outNameVer->name, in,
		sjme_lpwcsnlen(in, (dash - in) + 1));

	/* Parse the version number. */
	if (sjme_error_is(error = sjme_nvm_rom_swmParseVersion(
		&outNameVer->version,
		(dash + 1), (end - dash) - 1)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

SJME_MEEP_SWM_FUNC(definedConfigs)
{
	sjme_errorCode error;
	sjme_lpcwstr end;

	/* X-SquirrelJME-DefinedConfigurations: CLDC-1.8.0-compact CLDC-1.1 */
	/*  .1 CLDC-1.1.0 CLDC-1.0.0 */

	SJME_MEEP_SWM_FUNC_CHECK();

	/* Skip leading whitespace. */
	while (isspace(**lpwv))
		(*lpwv)++;

	/* Find the end of this token, if not the last. */
	end = sjme_lpwcschr(*lpwv, ' ');
	if (end == NULL)
		end = &(*lpwv)[sjme_lpwcslen(*lpwv)];

	/* Parse named version. */
	if (sjme_error_is(error = sjme_nvm_rom_swmParseNamedVersion(
		&tempEntry->configuration,
		(*lpwv), (end - (*lpwv)))))
		return sjme_error_default(error);

	/* Move the pointer over since it is valid. */
	*lpwv = end;

	/* Set details last, as everything is valid now. */
	tempEntry->tag.type = SJME_NVM_SWM_DEPENDENCY_CONFIGURATION;
	tempEntry->tag.use = SJME_NVM_SWM_USE_PROVIDES;

	/* Continue if the end of key has not been reached yet. */
skip_otherwise:
	if (**lpwv != '\0')
		return SJME_ERROR_CONTINUE;
	return SJME_ERROR_NONE;
}

SJME_MEEP_SWM_FUNC(definedProfiles)
{
	sjme_errorCode error;

	/* X-SquirrelJME-DefinedProfiles: MIDP-3.1 MEEP-8 */

	SJME_MEEP_SWM_FUNC_CHECK();

	/* This is effectively the same as this. */
	if (sjme_error_is(error = sjme_nvm_rom_swmKey_definedConfigs(
		inSuite, inLibrary, tempEntry, step, lpwv)))
	{
		/* Note, continue is not an error. */
		if (error != SJME_ERROR_CONTINUE)
			return sjme_error_default(error);
	}

	/* If a configuration entry was created, then change to profile. */
	if (tempEntry->tag.type == SJME_NVM_SWM_DEPENDENCY_CONFIGURATION)
	{
		tempEntry->tag.type = SJME_NVM_SWM_DEPENDENCY_PROFILE;
		tempEntry->tag.use = SJME_NVM_SWM_USE_PROVIDES;
	}

	/* Use the original error. */
	return error;
}

SJME_MEEP_SWM_FUNC(definedStandards)
{
	sjme_errorCode error;
	sjme_lpcwstr end;

	/* X-SquirrelJME-DefinedStandards: bluetooth.api;;1.0.0,bluetooth.api;;1 */
	/* .1.0 */

	SJME_MEEP_SWM_FUNC_CHECK();

	/* Skip leading whitespace. */
	while (isspace(**lpwv))
		(*lpwv)++;

	/* Find the end of this token, if not the last. */
	end = sjme_lpwcschr(*lpwv, ',');
	if (end == NULL)
		end = &(*lpwv)[sjme_lpwcslen(*lpwv)];

	/* Parse named version. */
	if (sjme_error_is(error = sjme_nvm_rom_swmParseSuite(
		&tempEntry->standard,
		(*lpwv), (end - (*lpwv)))))
		return sjme_error_default(error);

	/* Move the pointer over since it is valid, note the command needs */
	/* to be skipped here. */
	*lpwv = end + 1;

	/* Set details last, as everything is valid now. */
	tempEntry->tag.type = SJME_NVM_SWM_DEPENDENCY_STANDARD;
	tempEntry->tag.use = SJME_NVM_SWM_USE_PROVIDES;

	/* Continue if the end of key has not been reached yet. */
skip_otherwise:
	if (**lpwv != '\0')
		return SJME_ERROR_CONTINUE;
	return SJME_ERROR_NONE;
}

SJME_MEEP_SWM_FUNC(internalName)
{
	/* X-SquirrelJME-InternalProjectName: cldc-compact */

	SJME_MEEP_SWM_FUNC_CHECK();

	/* Set details. */
	tempEntry->tag.type = SJME_NVM_SWM_DEPENDENCY_INTERNAL_NAME;
	tempEntry->tag.use = SJME_NVM_SWM_USE_PROVIDES;

	/* Copy over. */
	sjme_lpwcsncat(tempEntry->internalName.name, step->map.value,
		SJME_NVM_SWM_FIELD_LENGTH - 1);

	/* Nothing more needs to be done. */
	return SJME_ERROR_NONE;
}

SJME_MEEP_SWM_FUNC(microConfig)
{
	sjme_errorCode error;
	sjme_lpcwstr end, checkEnd;

	/* Microedition-Configuration: CLDC-1.8-compact */

	SJME_MEEP_SWM_FUNC_CHECK();

	/* Skip leading whitespace. */
	while (isspace(**lpwv))
		(*lpwv)++;

	/* Find the end of this token, if not the last. */
	end = sjme_lpwcschr(*lpwv, ' ');
	if (end == NULL)
		end = &(*lpwv)[sjme_lpwcslen(*lpwv)];

	/* There must only be whitespace following this. */
	for (checkEnd = end; (*checkEnd) != '\0'; checkEnd++)
		if (!isspace(*checkEnd))
			return SJME_ERROR_INVALID_MANIFEST_VALUE;

	/* Parse named version. */
	if (sjme_error_is(error = sjme_nvm_rom_swmParseNamedVersion(
		&tempEntry->configuration,
		(*lpwv), (end - (*lpwv)))))
		return sjme_error_default(error);

	/* This depends on this configuration. */
	tempEntry->tag.type = SJME_NVM_SWM_DEPENDENCY_CONFIGURATION;
	tempEntry->tag.use = SJME_NVM_SWM_USE_DEPENDS;

	/* Nothing more needs to be done. */
	return SJME_ERROR_NONE;
}

SJME_MEEP_SWM_FUNC(microProfile)
{
	sjme_errorCode error;

	/* Microedition-Profile: MEEP-8 */

	SJME_MEEP_SWM_FUNC_CHECK();

	/* This is effectively the same as this. */
	if (sjme_error_is(error = sjme_nvm_rom_swmKey_microConfig(
		inSuite, inLibrary, tempEntry, step, lpwv)))
		return sjme_error_default(error);

	/* If a configuration entry was created, then change to profile. */
	if (tempEntry->tag.type == SJME_NVM_SWM_DEPENDENCY_CONFIGURATION)
	{
		tempEntry->tag.type = SJME_NVM_SWM_DEPENDENCY_PROFILE;
		tempEntry->tag.use = SJME_NVM_SWM_USE_DEPENDS;
	}

	/* Use the original error. */
	return error;
}

SJME_MEEP_SWM_FUNC(liblet)
{
	/* LIBlet-n: ??? */

	SJME_MEEP_SWM_FUNC_CHECK();

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_MEEP_SWM_FUNC(midlet)
{
	/* MIDlet-n: ??? */

	SJME_MEEP_SWM_FUNC_CHECK();

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_MEEP_SWM_FUNC(midxletApi)
{
	/* MIDxlet-api: ??? */

	SJME_MEEP_SWM_FUNC_CHECK();

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_MEEP_SWM_FUNC(midxletOpgl)
{
	/* MIDxlet-opgl: ??? */

	SJME_MEEP_SWM_FUNC_CHECK();

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_MEEP_SWM_FUNC(mainClass)
{
	sjme_errorCode error;
	sjme_charSeqStatic seq;

	/* Main-Class: cc.squirreljme.boop.Main */

	SJME_MEEP_SWM_FUNC_CHECK();

	/* Set details. */
	tempEntry->tag.type = SJME_NVM_SWM_DEPENDENCY_MAIN_CLASS;
	tempEntry->tag.use = SJME_NVM_SWM_USE_ENTRY_POINT;

	/* Copy over, note the name needs converting. */
	memset(&seq, 0, sizeof(seq));
	if (sjme_error_is(error = sjme_charSeq_newWideStatic(
		&seq, step->map.value, 0,
		sjme_lpwcslen(step->map.value))))
		return sjme_error_default(error);

	/* Copy over the class name. */
	strncpy(tempEntry->mainClass.name,
		sjme_charSeq_tempUtf(&seq),
		SJME_NVM_SWM_FIELD_LENGTH - 1);

	/* Success! */
	return SJME_ERROR_NONE;
}

static const sjme_nvm_rom_swmKeyMapHandler sjme_nvm_rom_swmKeyMapHandlers[] =
{
	SJME_MEEP_SWM_SHORT("X-SquirrelJME-DefinedConfigurations", -1,
		definedConfigs),
	SJME_MEEP_SWM_SHORT("X-SquirrelJME-DefinedProfiles", -1,
		definedProfiles),
	SJME_MEEP_SWM_SHORT("X-SquirrelJME-DefinedStandards", -1,
		definedStandards),
	SJME_MEEP_SWM_SHORT("X-SquirrelJME-InternalProjectName", -1,
		internalName),
	SJME_MEEP_SWM_SHORT("Microedition-Configuration", -1,
		microConfig),
	SJME_MEEP_SWM_SHORT("Microedition-Profile", -1,
		microProfile),
	SJME_MEEP_SWM_SHORT("LIBlet-", 7,
		liblet),
	SJME_MEEP_SWM_SHORT("MIDlet-", 7,
		midlet),
	SJME_MEEP_SWM_SHORT("MIDxlet-api", -1,
		midxletApi),
	SJME_MEEP_SWM_SHORT("MIDxlet-opgl", -1,
		midxletOpgl),
	SJME_MEEP_SWM_SHORT("Main-Class", -1,
		mainClass),

	/* End. */
	{NULL, 0, NULL},
};

#pragma endregion(swmHandlers)

static sjme_errorCode sjme_nvm_rom_swmLoadSingleDoJa(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrInNotNull sjme_nvm_rom_swmLibrary* inOutDepend)
{
	sjme_errorCode error;
	sjme_nvm_rom_library jamLib;
	sjme_cchar jamName[SJME_MAX_FILE_NAME];
	sjme_jint n;

	if (allocPool == NULL || inSuite == NULL || inLibrary == NULL ||
		inOutDepend == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Copy base JAM name. */
	memset(&jamName, 0, sizeof(jamName));
	strncpy(jamName, inLibrary->name, SJME_MAX_FILE_NAME - 1);

	/* Determine if this ends in JAR or not. */
	/* If it does, replace. */
	n = (sjme_jint)strlen(jamName);
	if (n > 3 && 0 == strcasecmp(".jar", &jamName[n - 3]))
		jamName[n - 1] = (jamName[n - 1] == 'R' ? 'M' : 'm');

	/* Otherwise, just append. */
	else
		strncat(jamName, ".jam", SJME_MAX_FILE_NAME - 1);

	/* There needs to be an actual partner JAM along with the JAR. */
	jamLib = NULL;
	if (sjme_error_is(error = sjme_nvm_rom_resolveLibraryByName(inSuite,
		jamName, &jamLib)) || jamLib == NULL)
	{
		if (error == SJME_ERROR_LIBRARY_NOT_FOUND)
			return SJME_ERROR_NOT_MATCHED;
		return sjme_error_default(error);
	}

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_nvm_rom_swmLoadSingleManifest(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrInNotNull sjme_nvm_rom_swmLibrary* inOutDepend)
{
#define SJME_ENTRY_GROW_LEN 8
	sjme_errorCode error;
	sjme_jboolean hasManifest, halt;
	sjme_stream_input inputStream;
	sjme_nvm_rom_swmEntry tempEntry;
	sjme_nvm_rom_manifestStep step;
	sjme_lpcwstr lpwv;
	sjme_jint freeSlot, i, n;
	const sjme_nvm_rom_swmKeyMapHandler* handler;
	sjme_list(sjme_nvm_rom_swmEntry)* entries;

	/* TODO FIXME: Optimize/shrink for SDCC. */

	if (allocPool == NULL || inSuite == NULL || inLibrary == NULL ||
		inOutDepend == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Check if a manifest exists. */
	hasManifest = SJME_JNI_FALSE;
	if (sjme_error_is(error = sjme_nvm_rom_libraryResourceExists(
		inLibrary, &hasManifest, "META-INF/MANIFEST.MF")))
		return sjme_error_default(error);

	/* If there is no manifest, there is no point to continue. */
	if (!hasManifest)
		return SJME_ERROR_NOT_MATCHED;

	/* Open resource stream. */
	inputStream = NULL;
	if (sjme_error_is(error = sjme_nvm_rom_libraryResourceAsStream(
		inLibrary, &inputStream, "META-INF/MANIFEST.MF")) ||
		inputStream == NULL)
		goto fail_openRc;

	/* This is used a bunch. */
	entries = inOutDepend->entries;

	/* Parse keys accordingly. */
	memset(&step, 0, sizeof(step));
	for (;;)
	{
		/* Parse the next key. */
		if (sjme_error_is(error = sjme_nvm_rom_manifestParseNext(
			inputStream, &step)))
		{
			/* Stop parsing on EOF. */
			if (error == SJME_ERROR_END_OF_FILE)
				break;

			/* Fail. */
			goto fail_parse;
		}

		/* Ignore if we are in any subsections. */
		if (step.attr[0] != 0)
			continue;

		/* Ignore blank values. */
		if (step.map.value[0] == '\0')
			continue;

		/* Find the handler for the given key. */
		for (handler = &sjme_nvm_rom_swmKeyMapHandlers[0];
			handler->key != NULL && handler->handler != NULL;
			handler++)
		{
			/* This is the one? */
			if (0 == sjme_string_compareIWAN(step.map.key, -1,
				handler->key, handler->keyLen))
				break;
		}

		/* Not valid. */
		if (handler->key == NULL || handler->handler == NULL)
		{
#if defined(SJME_CONFIG_DEBUG_MANIFEST)
			/* Emit a message if debugging, otherwise do not care. */
			sjme_message("Unknown key %"PRSl": %"PRSl"",
				step.map.key, step.map.value);
#endif

			/* Skip. */
			continue;
		}

		/* This is used by the handlers to determine if more needs to be */
		/* done, potentially. */
		lpwv = (sjme_lpcwstr)&step.map.value;

		/* Keep handling the key until it reports nothing is left. */
		halt = SJME_JNI_TRUE;
		do
		{
			/* Something is here, so clear the temporary key. */
			memset(&tempEntry, 0, sizeof(tempEntry));

			/* Perform the handler step. */
			if (sjme_error_is(error = handler->handler(inSuite, inLibrary,
				&tempEntry, &step, &lpwv)))
			{
				/* The only valid error is to continue. */
				if (error != SJME_ERROR_CONTINUE)
					goto fail_parse;

				/* Forget halting. */
				halt = SJME_JNI_FALSE;
			}

			/* Otherwise, halt. */
			else
				halt = SJME_JNI_TRUE;

			/* Entry is valid, so it gets added. */
			if (tempEntry.tag.type != SJME_NVM_SWM_DEPENDENCY_UNKNOWN &&
				tempEntry.tag.use != SJME_NVM_SWM_USE_UNKNOWN)
			{
				/* Find the first free slot in the list. */
				freeSlot = -1;
				if (entries != NULL)
					for (i = 0, n = entries->length; i < n; i++)
						if (entries->elements[i].tag.type == 0 ||
							entries->elements[i].tag.use == 0)
						{
							freeSlot = i;
							break;
						}

				/* Need to grow the list? */
				if (freeSlot < 0)
				{
					/* The free slot is at the end of the list. */
					freeSlot = (entries == NULL ? 0 : entries->length);

					/* Grow the list. */
					if (sjme_error_is(error = sjme_list_replace(
						allocPool,
						freeSlot + SJME_ENTRY_GROW_LEN,
						&entries, sjme_nvm_rom_swmEntry, 0)) ||
						entries == NULL)
						goto fail_growList;

					/* Set new list. */
					inOutDepend->entries = entries;
				}

				/* Copy item details. */
				memmove(&entries->elements[freeSlot], &tempEntry,
					sizeof(entries->elements[freeSlot]));
			}
		} while (!halt);
	}

	/* Close the stream. */
	if (sjme_error_is(error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(inputStream))))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;

fail_growList:
fail_parse:
fail_openRc:
	if (inputStream != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(inputStream));

	return sjme_error_default(error);
#undef SJME_ENTRY_GROW_LEN
}

static sjme_errorCode sjme_nvm_rom_swmLoadSingle(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrInNotNull sjme_nvm_rom_swmLibrary* inOutDepend)
{
	sjme_errorCode error;

	if (allocPool == NULL || inSuite == NULL || inLibrary == NULL ||
		inOutDepend == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Load manifest based information from the Jar directly, or from a */
	/* discovered KJX prepended manifest. */
	if (sjme_error_is(error = sjme_nvm_rom_swmLoadSingleManifest(
		allocPool, inSuite, inLibrary, inOutDepend)))
	{
		if (error == SJME_ERROR_INVALID_MANIFEST_FORMAT ||
			error == SJME_ERROR_INVALID_MANIFEST_VALUE)
			sjme_message("Manifest for %s is bad!",
				inLibrary->name);
		else if (error != SJME_ERROR_NOT_MATCHED)
			return sjme_error_vmError(NULL, sjme_error_default(error));
	}

	/* Load DoJa specific information. */
	if (sjme_error_is(error = sjme_nvm_rom_swmLoadSingleDoJa(
		allocPool, inSuite, inLibrary, inOutDepend)))
	{
		if (error != SJME_ERROR_NOT_MATCHED)
			return sjme_error_vmError(NULL, sjme_error_default(error));
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_rom_injectId(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_nvm_rom_suiteRunTarget* outTarget,
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary)
{
	sjme_errorCode error;
	sjme_jint i, n, libId;

	if (allocPool == NULL || outTarget == NULL || inLibrary == NULL ||
		inSuite == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get the library ID. */
	libId = 0;
	if (sjme_error_is(error = inSuite->functions->libraryId(
		inSuite, inLibrary, &libId)))
		return sjme_error_default(error);

	/* Was this library already included? */
	n = (outTarget->byId == NULL ? 0 : outTarget->byId->length);
	for (i = 0; i < n; i++)
		if (outTarget->byId->elements[i] == libId)
			return SJME_ERROR_CONTINUE;

	/* Grow the list. */
	if (sjme_error_is(error = sjme_list_replace(allocPool,
		n + 1,
		&outTarget->byId, sjme_jint, 0)) ||
		outTarget->byId == NULL)
		return sjme_error_default(error);

	/* Shift everything right so that dependencies added later appear */
	/* earlier. */
	if (n > 0)
	{
		memmove(&outTarget->byId->elements[1],
			&outTarget->byId->elements[0],
			sizeof(outTarget->byId->elements[0]) * n);
	}

	/* Set ID. */
	outTarget->byId->elements[0] = libId;

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_rom_swmResolveSub(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_rom_swmManager swmManager,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrInValue sjme_jboolean isPrimary,
	sjme_attrInNegativeOnePositive sjme_jint entryId,
	sjme_attrOutNotNull sjme_nvm_rom_suiteRunTarget* outTarget)
{
	sjme_errorCode error;
	sjme_list_sjme_nvm_rom_swmLibrary* swmLibraries;
	sjme_list_sjme_nvm_rom_swmEntry* swmEntries;
	sjme_nvm_rom_swmLibrary* swmInfo;
	sjme_nvm_rom_swmEntry* swmEntry;
	sjme_jint i, n, foundEntryId, atEntryId;
	sjme_nvm_rom_library foundLibrary;

	if (swmManager == NULL || inLibrary == NULL || outTarget == NULL ||
		allocPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Inject self to the ID list. */
	if (sjme_error_is(error = sjme_nvm_rom_injectId(allocPool,
		outTarget, swmManager->inSuite, inLibrary)))
	{
		/* Some other error. */
		if (error != SJME_ERROR_CONTINUE)
			return sjme_error_default(error);

		/* We already looked at this, so do not infinite recurse. */
		return SJME_ERROR_NONE;
	}

	/* Find our library first. */
	swmInfo = NULL;
	swmLibraries = swmManager->libraries;
	for (i = 0, n = swmLibraries->length; i < n; i++)
	{
		/* Find our specific library. */
		swmInfo = &swmLibraries->elements[i];
		if (swmInfo->library == inLibrary)
			break;

		/* Forget this info. */
		swmInfo = NULL;
	}

	/* This library is not managed by this manager? */
	if (swmInfo == NULL)
		return SJME_ERROR_LIBRARY_NOT_FOUND;

	/* Used to track entry IDs. */
	foundEntryId = -1;

	/* Look through each entry to find matches. */
	swmEntries = swmInfo->entries;
	for (i = 0, n = swmEntries->length; i < n; i++)
	{
		/* Skip invalid entries. */
		swmEntry = &swmEntries->elements[i];
		if (swmEntry->tag.use <= SJME_NVM_SWM_USE_UNKNOWN ||
			swmEntry->tag.use >= SJME_NVM_SWM_NUM_USE ||
			swmEntry->tag.type <= SJME_NVM_SWM_DEPENDENCY_UNKNOWN ||
			swmEntry->tag.type >= SJME_NVM_SWM_NUM_DEPENDENCY_TYPES)
			continue;

		/* Is this an entry point? */
		if (swmEntry->tag.use == SJME_NVM_SWM_USE_ENTRY_POINT)
		{
			/* We already found an entry point? */
			/* Or this is not a primary dependency. */
			if (foundEntryId == INT32_MAX ||
				foundEntryId == INT32_MIN ||
				!isPrimary)
				continue;

			/* Is this an entry point we care about? */
			atEntryId = ++foundEntryId;
			if (entryId >= 0 && atEntryId != entryId)
				continue;

			/* Since we found this, do not consider any more entries. */
			foundEntryId = INT32_MAX;

			/* Main class entry point? */
			if (swmEntry->tag.type <= SJME_NVM_SWM_DEPENDENCY_MAIN_CLASS)
			{
				outTarget->mainClass = &swmEntry->mainClass.name[0];
			}

			else
			{
				sjme_todo("Impl?");
				return sjme_error_notImplemented(0);
			}
		}

		/* Is this a needed dependency? */
		else if (swmEntry->tag.use == SJME_NVM_SWM_USE_DEPENDS)
		{
			/* Locate the target library based on this dependency. */
			foundLibrary = NULL;
			if (sjme_error_is(error = sjme_nvm_rom_swmResolveDependency(
				swmManager, inLibrary, swmEntry, &foundLibrary)))
				return sjme_error_default(error);

			/* Recurse into self to handle dependencies of this library. */
			/* This also injects self. */
			if (sjme_error_is(error = sjme_nvm_rom_swmResolveSub(
				allocPool, swmManager, foundLibrary,
				SJME_JNI_FALSE, INT32_MAX, outTarget)))
				return sjme_error_default(error);
		}
	}

	/* Nothing more to be done. */
	return SJME_ERROR_NONE;
}

sjme_jint sjme_nvm_rom_compareConfiguration(
	sjme_attrInNullable sjme_lpcwstr a,
	sjme_attrInNullable const sjme_nvm_swmVersion* aV,
	sjme_attrInNullable sjme_lpcwstr b,
	sjme_attrInNullable const sjme_nvm_swmVersion* bV)
{
	sjme_jint rv;

	/* Compare nulls first. */
	rv = sjme_compare_null(a, b);
	if (rv != 0)
		return rv;

	/* Fallback to zero. */
	if (aV == NULL)
		aV = &sjme_nvm_rom_zeroVersion;
	if (bV == NULL)
		bV = &sjme_nvm_rom_zeroVersion;

	/* CLDC before CDC. */
	if (sjme_string_compareIWAN(a, -1, "CLDC", -1) &&
		sjme_string_compareIWAN(b, -1, "CDC", -1))
		return -1;

	/* CDC after CLDC. */
	if (sjme_string_compareIWAN(a, -1, "CDC", -1) &&
		sjme_string_compareIWAN(b, -1, "CLDC", -1))
		return 1;

	/* Otherwise compare based on string. */
	rv = sjme_lpwcscmp(a, b);
	if (rv != 0)
		return 0;

	/* Then the version. */
	return sjme_nvm_rom_compareVersion(aV, bV);
}

sjme_jint sjme_nvm_rom_compareProfile(
	sjme_attrInNullable sjme_lpcwstr a,
	sjme_attrInNullable const sjme_nvm_swmVersion* aV,
	sjme_attrInNullable sjme_lpcwstr b,
	sjme_attrInNullable const sjme_nvm_swmVersion* bV)
{
	sjme_jint rv;
	sjme_jboolean aMeep, aMidp;
	sjme_jboolean bMeep, bMidp;

	/* Compare nulls first. */
	rv = sjme_compare_null(a, b);
	if (rv != 0)
		return rv;

	/* Fallback to zero. */
	if (aV == NULL)
		aV = &sjme_nvm_rom_zeroVersion;
	if (bV == NULL)
		bV = &sjme_nvm_rom_zeroVersion;
	
	/* Are any of these MEEP or MIDP? */
	aMeep = sjme_string_compareIWAN(a, -1, "MEEP", -1);
	aMidp = sjme_string_compareIWAN(a, -1, "MIDP", -1);
	
	/* And for B... */
	bMeep = sjme_string_compareIWAN(b, -1, "MEEP", -1);
	bMidp = sjme_string_compareIWAN(b, -1, "MIDP", -1);
	
	/* MIDP and MEEP after everything else. */
	/* This is so VSCL does not take priority. */
	if ((aMeep || aMidp) && (!bMeep && !bMidp))
		return 1;
	else if ((!aMeep && !aMidp) && (bMeep || bMidp))
		return -1;

	/* MIDP before MEEP. */
	else if (aMidp && bMeep)
		return -1;

	/* MEEP after MIDP. */
	else if (aMeep && bMidp)
		return 1;

	/* Otherwise compare based on string. */
	rv = sjme_lpwcscmp(a, b);
	if (rv != 0)
		return 0;

	/* Then the version. */
	return sjme_nvm_rom_compareVersion(aV, bV);
}

sjme_jint sjme_nvm_rom_compareVersion(
	sjme_attrInNullable const sjme_nvm_swmVersion* a,
	sjme_attrInNullable const sjme_nvm_swmVersion* b)
{
	sjme_jint nulls;

	/* Compare nulls first. */
	nulls = sjme_compare_null(a, b);
	if (nulls != 0)
		return nulls;

	/* Major version. */
	if (a->major < b->major)
		return -1;
	else if (a->major > b->major)
		return 1;

	/* Minor version. */
	if (a->minor < b->minor)
		return -1;
	else if (a->minor > b->minor)
		return 1;

	/* Release version. */
	if (a->release < b->release)
		return -1;
	else if (a->release > b->release)
		return 1;

	/* Compact before not. */
	if (a->isCompact && !b->isCompact)
		return -1;
	else if (!a->isCompact && b->isCompact)
		return 1;

	/* Otherwise these are the same. */
	return 0;
}

sjme_errorCode sjme_nvm_rom_swmLoad(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrOutNotNull sjme_nvm_rom_swmManager* outSwmManager)
{
	sjme_errorCode error;
	sjme_nvm_rom_swmManager result;
	sjme_list_sjme_nvm_rom_library* libraries;
	sjme_nvm_rom_library library;
	sjme_list(sjme_nvm_rom_swmLibrary)* suiteDepends;
	sjme_nvm_rom_swmLibrary* suiteDependency;
	sjme_jint i, n;

	if (allocPool == NULL || inSuite == NULL || outSwmManager == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Allocate the result as it gets added into. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(allocPool,
		sizeof(*result), SJME_NVM_STRUCT_ROM_MEEP_SWM,
		SJME_AS_NVM_COMMONP(&result))) ||
		result == NULL)
		goto fail_allocResult;

	/* Set suite. */
	result->inSuite = sjme_weakUpR(sjme_nvm_rom_suite, inSuite);

	/* We need the library list to process everything. */
	libraries = NULL;
	if (sjme_error_is(error = sjme_nvm_rom_suiteLibraries(inSuite,
		&libraries)) || libraries == NULL)
		goto fail_suiteLibs;

	/* Allocate list of suite information. */
	suiteDepends = NULL;
	if (sjme_error_is(error = sjme_list_alloc(allocPool,
		libraries->length, &suiteDepends, sjme_nvm_rom_swmLibrary, 0)) ||
		suiteDepends == NULL)
		goto fail_allocDepends;
	result->libraries = suiteDepends;

	/* Go through and process each library. */
	for (n = libraries->length, i = 0; i < n; i++)
	{
		/* Ignore blank library slots. */
		library = libraries->elements[i];
		if (library == NULL)
			continue;

		/* Set library here. */
		suiteDependency = &suiteDepends->elements[i];
		suiteDependency->library = library;

		/* Parse this library's dependency information. */
		if (sjme_error_is(error = sjme_nvm_rom_swmLoadSingle(allocPool,
			inSuite, library, suiteDependency)))
		{
			/* The manifest could have errors in it. */
			if (error != SJME_ERROR_INVALID_MANIFEST_VALUE)
				goto fail_parseDepends;

#if defined(SJME_CONFIG_DEBUG)
			sjme_message("Library %s has invalid manifest data.",
				library->name);
#endif
		}
	}

	/* Return the resultant dependency information. */
	*outSwmManager = result;
	return SJME_ERROR_NONE;

fail_parseDepends:
fail_suiteLibs:
fail_allocDepends:
fail_allocResult:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_rom_swmResolve(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_rom_swmManager swmManager,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrInNullable sjme_list(sjme_lpcstr)* inMainArgs,
	sjme_attrInNegativeOnePositive sjme_jint entryId,
	sjme_attrOutNotNull sjme_nvm_rom_suiteRunTarget* outTarget)
{
	sjme_errorCode error;
	sjme_nvm_rom_suiteRunTarget result;

	if (swmManager == NULL || inLibrary == NULL || outTarget == NULL ||
		allocPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (entryId < -1 || entryId == INT32_MAX)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Use recursive resolution. */
	memset(&result, 0, sizeof(result));
	if (sjme_error_is(error = sjme_nvm_rom_swmResolveSub(
		allocPool, swmManager, inLibrary,
		SJME_JNI_TRUE, entryId, &result)))
		return sjme_error_default(error);

	/* Copy main arguments over. */
	result.mainArgs = inMainArgs;

	/* Copy target. */
	if (sjme_error_is(error = sjme_nvm_copyRunTarget(
		allocPool, outTarget, &result,
		SJME_JNI_FALSE, SJME_JNI_FALSE)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_rom_swmResolveDependency(
	sjme_attrInNotNull sjme_nvm_rom_swmManager swmManager,
	sjme_attrInNullable sjme_nvm_rom_library inLibrary,
	sjme_attrInNotNull const sjme_nvm_rom_swmEntry* inMatching,
	sjme_attrOutNotNull sjme_nvm_rom_library* outLibrary)
{
	sjme_errorCode error;
	sjme_list_sjme_nvm_rom_swmLibrary* swmLibraries;
	sjme_list_sjme_nvm_rom_swmEntry* swmEntries;
	sjme_nvm_rom_swmLibrary* swmInfo;
	sjme_nvm_rom_swmEntry* swmEntry;
	sjme_jint i, n, j, m;
	sjme_jboolean matched;

	if (swmManager == NULL || inMatching == NULL || outLibrary == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inMatching->tag.use != SJME_NVM_SWM_USE_DEPENDS &&
		inMatching->tag.use != SJME_NVM_SWM_USE_DEPENDS_OPTIONAL)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Look through every library. */
	swmLibraries = swmManager->libraries;
	for (i = 0, n = swmLibraries->length; i < n; i++)
	{
		/* Find our specific library. */
		swmInfo = &swmLibraries->elements[i];
		if (swmInfo->library == inLibrary)
			break;

		/* Look through each entry to find matches. */
		swmEntries = swmInfo->entries;
		for (j = 0, m = swmEntries->length; j < m; j++)
		{
			/* Skip invalid entries. */
			swmEntry = &swmEntries->elements[j];
			if (swmEntry->tag.use <= SJME_NVM_SWM_USE_UNKNOWN ||
				swmEntry->tag.use >= SJME_NVM_SWM_NUM_USE ||
				swmEntry->tag.type <= SJME_NVM_SWM_DEPENDENCY_UNKNOWN ||
				swmEntry->tag.type >= SJME_NVM_SWM_NUM_DEPENDENCY_TYPES)
				continue;

			/* If this is not a providing type entry then we do not */
			/* care for it. */
			if (swmEntry->tag.use != SJME_NVM_SWM_USE_PROVIDES)
				continue;

			/* This must be a matching request to the type. */
			if (inMatching->tag.type != swmEntry->tag.type)
				continue;

			/* Find the appropriate match. */
			matched = SJME_JNI_FALSE;
			switch (inMatching->tag.type)
			{
					/* sjme_nvm_swmInfoTypedAll */
				case SJME_NVM_SWM_DEPENDENCY_TYPED_SUITE:
					sjme_todo("Impl?");
					return sjme_error_notImplemented(0);

					/* sjme_nvm_swmInfoAll */
				case SJME_NVM_SWM_DEPENDENCY_STANDARD:
				case SJME_NVM_SWM_DEPENDENCY_SUITE:
					sjme_todo("Impl?");
					return sjme_error_notImplemented(0);

				case SJME_NVM_SWM_DEPENDENCY_CONFIGURATION:
					if (sjme_nvm_rom_compareConfiguration(
						inMatching->nameVersion.name,
						&inMatching->nameVersion.version,
						swmEntry->nameVersion.name,
						&swmEntry->nameVersion.version) >= 0)
						matched |= SJME_JNI_TRUE;
					break;

				case SJME_NVM_SWM_DEPENDENCY_PROFILE:
					if (sjme_nvm_rom_compareProfile(
						inMatching->nameVersion.name,
						&inMatching->nameVersion.version,
						swmEntry->nameVersion.name,
						&swmEntry->nameVersion.version) >= 0)
						matched |= SJME_JNI_TRUE;
					break;

				case SJME_NVM_SWM_DEPENDENCY_INTERNAL_NAME:
					if (0 == sjme_lpwcscmp(
						inMatching->nameOnly.name,
						swmEntry->nameOnly.name))
						matched |= SJME_JNI_TRUE;
					break;

					/* Something not known about. */
				default:
					continue;
			}

			/* If this was matched, this is the dependency! */
			if (matched)
			{
				*outLibrary = swmInfo->library;
				return SJME_ERROR_NONE;
			}
		}
	}

	/* No library found. */
	return SJME_ERROR_LIBRARY_NOT_FOUND;
}
