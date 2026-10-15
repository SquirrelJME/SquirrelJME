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

	/* Find the right dot. */
	right = sjme_lpwcschr(left + 1, '.');
	if (right == NULL || right >= end)
		return SJME_ERROR_INVALID_MANIFEST_VALUE;

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
	rel = sjme_lpwcstoimax(right + 1, &relP, 10);

	/* Are any of these invalid? */
	/* Note that the end pointers must land exactly on their right side. */
	if (maj > 99 || min > 99 || rel > 99 ||
		maj < 0 || min < 0 || rel < 0 ||
		majP == NULL || minP == NULL || relP == NULL ||
		majP != left || minP != right || relP != term)
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
	/* X-SquirrelJME-DefinedStandards: ??? */

	SJME_MEEP_SWM_FUNC_CHECK();

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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
	/* Microedition-Configuration: CLDC-1.8-compact */

	SJME_MEEP_SWM_FUNC_CHECK();

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_MEEP_SWM_FUNC(microProfile)
{
	/* Microedition-Profile: MEEP-8 */

	SJME_MEEP_SWM_FUNC_CHECK();

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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
	sjme_jint freeSlot;
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
#if defined(SJME_CONFIG_DEBUG)
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
					if (sjme_error_is(error = sjme_listUtil_findFree(
						SJME_AS_LIST_POINTER(&entries), &freeSlot)))
						return sjme_error_default(error);

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
		if (error == SJME_ERROR_INVALID_MANIFEST_FORMAT)
			sjme_message("Manifest for %s was not valid.",
				inLibrary->name);
		else if (error != SJME_ERROR_NOT_MATCHED)
			return sjme_error_default(error);
	}

	/* Load DoJa specific information. */
	if (sjme_error_is(error = sjme_nvm_rom_swmLoadSingleDoJa(
		allocPool, inSuite, inLibrary, inOutDepend)))
	{
		if (error != SJME_ERROR_NOT_MATCHED)
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
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
			goto fail_parseDepends;
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
	sjme_attrInNotNull sjme_nvm_rom_swmManager swmManager,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrOutNotNull sjme_lpstr* outMainClass,
	sjme_attrOutNotNull sjme_list(sjme_lpstr)** outMainArgs,
	sjme_attrOutNotNull sjme_list(sjme_jint)** outById,
	sjme_attrOutNotNull sjme_list(sjme_lpstr)** outByName)
{
	if (swmManager == NULL || inLibrary == NULL || outMainClass == NULL ||
		outMainArgs == NULL || outById == NULL || outByName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
