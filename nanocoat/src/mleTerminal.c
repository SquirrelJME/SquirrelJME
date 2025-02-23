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

SJME_NVM_MLE_FUNCTION_DECL(available)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(close)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(flush)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(fromStandard)
{
	sjme_nvm_task_globals* globals;
	sjme_nvm_mle_standardPipeType type;
	sjme_jobject pipe;

	/* Check. */
	type = (sjme_nvm_mle_standardPipeType)argV[0].value.i;
	if (type < 0 || type >= SJME_NVM_MLE_NUM_STD_PIPES)
		return SJME_ERROR_MLE_CALL;

	/* Has a pipe already been created? We want single brackets for each */
	/* standard pipe that exists. */
	globals = &inFrame->inThread->inTask->globals;
	pipe = globals->stdPipes[type];
	if (pipe != NULL)
	{
		argR->type = SJME_JAVA_TYPE_ID_OBJECT;
		argR->value.l = pipe;
		return SJME_ERROR_NONE;
	}
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(read)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL_ALT(write, single)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL_ALT(write, multi)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_SHELF_DECLARE(TerminalShelf) =
{
	SJME_NVM_MLE_DEFINE(available,
		SJME_MD(SJME_MD_I, SJME_MD_PIPE),
		"IL"),
	SJME_NVM_MLE_DEFINE(close,
		SJME_MD(SJME_MD_I, SJME_MD_PIPE),
		"IL"),
	SJME_NVM_MLE_DEFINE(flush,
		SJME_MD(SJME_MD_I, SJME_MD_PIPE),
		"IL"),
	SJME_NVM_MLE_DEFINE(fromStandard,
		SJME_MD(SJME_MD_PIPE, SJME_MD_I),
		"LI"),
	SJME_NVM_MLE_DEFINE(read,
		SJME_MD(SJME_MD_I, SJME_MD_PIPE SJME_MD_A(SJME_MD_B) SJME_MD_I
			SJME_MD_I),
		"ILLII"),
	SJME_NVM_MLE_DEFINE_ALT(write, single,
		SJME_MD(SJME_MD_I, SJME_MD_PIPE SJME_MD_A(SJME_MD_B)
			SJME_MD_I),
		"ILI"),
	SJME_NVM_MLE_DEFINE_ALT(write, multi,
		SJME_MD(SJME_MD_I, SJME_MD_PIPE SJME_MD_A(SJME_MD_B)
			SJME_MD_I SJME_MD_I),
		"ILLII"),
	
	SJME_NVM_MLE_STOP()
};
