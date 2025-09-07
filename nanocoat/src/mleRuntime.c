/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"

#if defined(SJME_CONFIG_HAS_OS_POSIX)
	#include <locale.h>

	/* Android does not have langinfo. */
	#if !defined(SJME_CONFIG_HAS_OS_ANDROID)
		#include <langinfo.h>
	#endif
#endif

#include <sjme/nvm/cleanup.h>

#include "sjme/nvm/mle.h"
#include "sjme/nvm/mleConst.h"
#include "sjme/nvm/mleShelves.h"

SJME_NVM_MLE_FUNCTION_DECL(byteOrder)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(currentTimeMillis)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(encoding)
{
	static sjme_atomic_sjme_jint cached;
	sjme_nvm_mle_builtInEncodingType encoding;
	const char* codeType;
#if defined(SJME_CONFIG_HAS_OS_POSIX)
	sjme_lpcstr set;
#endif

	/* Cached? */
	encoding = sjme_atomic_sjme_jint_get(&cached);
	if (encoding != SJME_NVM_MLE_ENCODING_UNSPECIFIED)
		goto skip_cached;

	/* Reset codetype based string. */
	codeType = NULL;
	
#if defined(SJME_CONFIG_HAS_OS_WINDOWS)
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#elif defined(SJME_CONFIG_HAS_OS_ANDROID)
	/* Android only does UTF-8. */
	encoding = SJME_NVM_MLE_ENCODING_UTF8;
#elif defined(SJME_CONFIG_HAS_OS_POSIX)
	/* Get the base global locale. */
	set = setlocale(LC_ALL, "");
	if (set != NULL)
		codeType = nl_langinfo(CODESET);
#endif

	/* Base on the code type string? */
	if (encoding == SJME_NVM_MLE_ENCODING_UNSPECIFIED && codeType != NULL)
	{
		if (strcasecmp(codeType, "UTF-8") == 0)
			encoding = SJME_NVM_MLE_ENCODING_UTF8;
		else if (strcasecmp(codeType, "ASCII") == 0 ||
			strcasecmp(codeType, "ANSI_X3.4-1968") == 0)
			encoding = SJME_NVM_MLE_ENCODING_ASCII;
		else if (strcasecmp(codeType, "IBM037") == 0 ||
			strcasecmp(codeType, "EBCDIC-US") == 0)
			encoding = SJME_NVM_MLE_ENCODING_IBM037;
		else if (strcasecmp(codeType, "ISO-8859-1") == 0)
			encoding = SJME_NVM_MLE_ENCODING_ISO_8859_1;
		else if (strcasecmp(codeType, "ISO-8859-15") == 0)
			encoding = SJME_NVM_MLE_ENCODING_ISO_8859_15;
		else if (strcasecmp(codeType, "SHIFT_JIS") == 0 ||
			strcasecmp(codeType, "SHIFT_JISX0213") == 0)
			encoding = SJME_NVM_MLE_ENCODING_SHIFT_JIS;
		else if (strcasecmp(codeType, "IBM437") == 0)
			encoding = SJME_NVM_MLE_ENCODING_IBM437;
	}
	
	/* Fallback to UTF-8 if unspecified. */
	if (encoding == SJME_NVM_MLE_ENCODING_UNSPECIFIED)
		encoding = SJME_NVM_MLE_ENCODING_UTF8;
	sjme_atomic_sjme_jint_set(&cached, encoding);

	/* Return the given encoding. */
skip_cached:
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = encoding;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(exit)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(garbageCollect)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(lineEnding)
{
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	
#if defined(SJME_CONFIG_HAS_OS_WINDOWS) || \
	defined(SJME_CONFIG_HAS_OS_WINDOWS_16) || \
	defined(SJME_CONFIG_HAS_OS_WINDOWS_32) || \
	defined(SJME_CONFIG_HAS_OS_PALMOS) || \
	defined(SJME_CONFIG_HAS_OS_PC_DOS)
	argR->v.i = SJME_NVM_MLE_LINE_ENDING_CRLF;
#elif defined(SJME_CONFIG_HAS_OS_MACOS_CLASSIC)
	argR->v.i = SJME_NVM_MLE_LINE_ENDING_CR;
#else
	argR->v.i = SJME_NVM_MLE_LINE_ENDING_LF;
#endif

	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(locale)
{
	static sjme_atomic_sjme_jint cached;
	sjme_nvm_mle_builtInLocaleType locale;
#if defined(SJME_CONFIG_HAS_OS_POSIX)
	sjme_lpcstr set;
#endif

	/* Cached? */
	locale = sjme_atomic_sjme_jint_get(&cached);
	if (locale != SJME_NVM_MLE_LOCALE_UNSPECIFIED)
		goto skip_cached;
	
#if defined(SJME_CONFIG_HAS_OS_WINDOWS)
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#elif defined(SJME_CONFIG_HAS_OS_POSIX)
	/* Get the base global locale. */
	set = setlocale(LC_ALL, "");
	if (set != NULL)
	{
		/* US English. */
		if (strncasecmp(set, "en_US", 5))
			locale = SJME_NVM_MLE_LOCALE_US_ENGLISH;

		/* Default English (US). */
		else if (strncasecmp(set, "en", 2))
			locale = SJME_NVM_MLE_LOCALE_US_ENGLISH;
	}
#endif
	
	/* Fallback to US English if unspecified. */
	if (locale == SJME_NVM_MLE_LOCALE_UNSPECIFIED)
		locale = SJME_NVM_MLE_LOCALE_US_ENGLISH;
	sjme_atomic_sjme_jint_set(&cached, locale);

	/* Return the given locale. */
skip_cached:
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = locale;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(memoryProfile)
{
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
#if defined(SJME_CONFIG_HAS_LOW_MEMORY)
	argR->v.i = SJME_NVM_MLE_MEMORY_PROFILE_MINIMAL;
#else
	argR->v.i = SJME_NVM_MLE_MEMORY_PROFILE_NORMAL;
#endif
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(nanoTime)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(phoneModel)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(systemEnv)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(systemProperty)
{
	sjme_errorCode error;
	sjme_jstring key, loaded;
	sjme_jint ik, iv, n;
	const sjme_list_sjme_lpcstr* sysProps;
	sjme_charSeq keySeq;
	sjme_lpcstr keyString, k, v;

	/* Read in key value, must be a valid string! */
	key = (sjme_jstring)argV[0].v.l;
	if (!sjme_nvm_isAR(key, SJME_NVM_STRUCT_STRING_INSTANCE))
		return SJME_ERROR_MLE_CALL;
	
	/* Has the sequence ever been initialized? */
	keySeq = sjme_atomic_sjme_charSeq_get(&key->seq);
	if (keySeq == NULL)
		return SJME_ERROR_MLE_CALL;

	/* Determine the key to use. */
	keyString = sjme_charSeq_tempUtf(keySeq);

	/* Look within the init config, if it is valid. */
	sysProps = (SJME_F_K(inFrame)->initConfig != NULL &&
		SJME_F_K(inFrame)->initConfig->sysProps != NULL ?
		SJME_F_K(inFrame)->initConfig->sysProps : NULL);
	if (sysProps != NULL)
		for (ik = 0, iv = ik + 1, n = sysProps->length; iv < n; ik++, iv++)
		{
			/* Load in key and value. */
			k = sysProps->elements[ik];
			v = sysProps->elements[iv];

			/* Skip if missing. */
			if (k == NULL || v == NULL)
				continue;

			/* Is this a match? */
			if (strcasecmp(k, v) == 0)
			{
				/* Load in string value. */
				loaded = NULL;
				if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfUtf(
					SJME_F_T(inFrame), &loaded, SJME_JNI_TRUE, v)) ||
					loaded == NULL)
					return sjme_error_mask(error, SJME_ERROR_MLE_CALL);

				/* Use the loaded string value. */
				argR->t = SJME_JAVA_TYPE_ID_OBJECT;
				argR->v.l = SJME_AS_JOBJECT(loaded);
				return SJME_ERROR_NONE;
			}
		}

	/* Not found. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = NULL;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(vmDescription)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(vmStatistic)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(vmType)
{
	/* Always returns this constant value of NanoCoat. */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = SJME_NVM_MLE_VM_TYPE_NANOCOAT;

	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_SHELF_DECLARE(RuntimeShelf) =
{
	SJME_NVM_MLE_DEFINE(byteOrder, "()I",
		"I", ),
	SJME_NVM_MLE_DEFINE(currentTimeMillis, "()J",
		"J", ),
	SJME_NVM_MLE_DEFINE(encoding, "()I",
		"I", ),
	SJME_NVM_MLE_DEFINE(exit, "(I)V",
		"V", "I"),
	SJME_NVM_MLE_DEFINE(garbageCollect, "()V",
		"V", ),
	SJME_NVM_MLE_DEFINE(lineEnding, "()I",
		"I", ),
	SJME_NVM_MLE_DEFINE(locale, "()I",
		"I", ),
	SJME_NVM_MLE_DEFINE(memoryProfile, "()I",
		"I", ),
	SJME_NVM_MLE_DEFINE(nanoTime, "()J",
		"J", ),
	SJME_NVM_MLE_DEFINE(phoneModel, "()J",
		"J", ),
	SJME_NVM_MLE_DEFINE(systemEnv,
		"(Ljava/lang/String;)Ljava/lang/String;",
		"L", "L"),
	SJME_NVM_MLE_DEFINE(systemProperty,
		"(Ljava/lang/String;)Ljava/lang/String;",
		"L", "L"),
	SJME_NVM_MLE_DEFINE(vmDescription,
		"(I)Ljava/lang/String;",
		"L", "I"),
	SJME_NVM_MLE_DEFINE(vmStatistic, "(I)J",
		"J", "I"),
	SJME_NVM_MLE_DEFINE(vmType, "()I",
		"I", ),
	
	SJME_NVM_MLE_STOP(),
};

