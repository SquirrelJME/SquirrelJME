/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"

#if defined(SJME_CONFIG_HAS_POSIX)
	#include <locale.h>
#endif

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
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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
	
#if defined(SJME_CONFIG_HAS_WINDOWS) || \
	defined(SJME_CONFIG_HAS_WINDOWS_16) || \
	defined(SJME_CONFIG_HAS_WINDOWS_32) || \
	defined(SJME_CONFIG_HAS_PALMOS)
	argR->value.i = SJME_NVM_MLE_LINE_ENDING_CRLF;
#elif defined(SJME_CONFIG_HAS_MACOS_CLASSIC)
	argR->value.i = SJME_NVM_MLE_LINE_ENDING_CR;
#else
	argR->v.i = SJME_NVM_MLE_LINE_ENDING_LF;
#endif

	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(locale)
{
	static sjme_atomic_sjme_jint cached;
	sjme_nvm_mle_builtInLocaleType locale;
#if defined(SJME_CONFIG_HAS_POSIX)
	sjme_lpcstr set;
#endif

	/* Cached? */
	locale = sjme_atomic_sjme_jint_get(&cached);
	if (locale != SJME_NVM_MLE_LOCALE_UNSPECIFIED)
		goto skip_cached;
	
#if defined(SJME_CONFIG_HAS_WINDOWS)
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#elif defined(SJME_CONFIG_HAS_POSIX)
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

	/* Return the given locale. */
skip_cached:
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = locale;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(memoryProfile)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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

