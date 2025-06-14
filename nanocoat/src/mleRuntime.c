/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
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
	argR->type = SJME_JAVA_TYPE_ID_INTEGER;
	
#if defined(SJME_CONFIG_HAS_WINDOWS) || \
	defined(SJME_CONFIG_HAS_WINDOWS_16) || \
	defined(SJME_CONFIG_HAS_WINDOWS_32) || \
	defined(SJME_CONFIG_HAS_PALMOS)
	argR->value.i = SJME_NVM_MLE_LINE_ENDING_CRLF;
#elif defined(SJME_CONFIG_HAS_MACOS_CLASSIC)
	argR->value.i = SJME_NVM_MLE_LINE_ENDING_CR;
#else
	argR->value.i = SJME_NVM_MLE_LINE_ENDING_LF;
#endif

	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(locale)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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
	argR->type = SJME_JAVA_TYPE_ID_INTEGER;
	argR->value.i = SJME_NVM_MLE_VM_TYPE_NANOCOAT;

	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_SHELF_DECLARE(RuntimeShelf) =
{
	SJME_NVM_MLE_DEFINE(byteOrder, "()I",
		"I"),
	SJME_NVM_MLE_DEFINE(currentTimeMillis, "()J",
		"J"),
	SJME_NVM_MLE_DEFINE(encoding, "()I",
		"I"),
	SJME_NVM_MLE_DEFINE(exit, "(I)V",
		"VI"),
	SJME_NVM_MLE_DEFINE(garbageCollect, "()V",
		"V"),
	SJME_NVM_MLE_DEFINE(lineEnding, "()I",
		"I"),
	SJME_NVM_MLE_DEFINE(locale, "()I",
		"I"),
	SJME_NVM_MLE_DEFINE(memoryProfile, "()I",
		"I"),
	SJME_NVM_MLE_DEFINE(nanoTime, "()J",
		"J"),
	SJME_NVM_MLE_DEFINE(phoneModel, "()J",
		"J"),
	SJME_NVM_MLE_DEFINE(systemEnv,
		"(Ljava/lang/String;)Ljava/lang/String;",
		"LL"),
	SJME_NVM_MLE_DEFINE(systemProperty,
		"(Ljava/lang/String;)Ljava/lang/String;",
		"LL"),
	SJME_NVM_MLE_DEFINE(vmDescription,
		"(I)Ljava/lang/String;",
		"LI"),
	SJME_NVM_MLE_DEFINE(vmStatistic, "(I)J",
		"JI"),
	SJME_NVM_MLE_DEFINE(vmType, "()I",
		"I"),
	
	SJME_NVM_MLE_STOP(),
};

