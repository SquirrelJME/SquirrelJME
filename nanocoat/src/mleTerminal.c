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
#include "sjme/nvm/mleBrackets.h"
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
	sjme_errorCode error;
	sjme_nvm_task_globals* globals;
	sjme_nvm_mle_standardPipeType type;
	sjme_nvm_mle_pipe pipe;
	const sjme_nal* nal;

	/* Check. */
	type = (sjme_nvm_mle_standardPipeType)argV[0].value.i;
	if (type < 0 || type >= SJME_NVM_MLE_NUM_STD_PIPES)
		return SJME_ERROR_MLE_CALL;

	/* Check if the system even supports this. */
	nal = inFrame->inState->nal;
	if ((type == SJME_NVM_MLE_STD_PIPE_STDIN && nal->stdInF == NULL) ||
		(type == SJME_NVM_MLE_STD_PIPE_STDOUT && nal->stdOutF == NULL) ||
		(type == SJME_NVM_MLE_STD_PIPE_STDERR && nal->stdErrF == NULL))
		return SJME_ERROR_MLE_CALL;

	/* Has a pipe already been created? We want single brackets for each */
	/* standard pipe that exists. */
	globals = &inFrame->inThread->inTask->globals;
	pipe = globals->stdPipes[type];
	if (pipe != NULL)
		goto skip_validPipe;

	/* Lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&globals->lock)))
		return sjme_error_default(error);

	/* Check again, and only initialize the pipe if it is still NULL. */
	pipe = globals->stdPipes[type];
	if (pipe == NULL)
	{
		/* Allocate pipe object. */
		if (sjme_error_is(error = sjme_nvm_task_objectNewN(inFrame->inThread,
			sizeof(*pipe), SJME_NVM_STRUCT_BRACKET_PIPE,
			SJME_AS_JOBJECTP(&pipe), SJME_NVM_BRACKET_NAME_PIPE)) ||
			pipe == NULL)
			goto fail_badAlloc;
		
		/* Input pipe. */
		if (type == SJME_NVM_MLE_STD_PIPE_STDIN)
		{
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);
		}

		/* Output pipe. */
		else
		{
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);
		}

		/* Cache for later usage. */
		globals->stdPipes[type] = pipe;
	}

	/* Unlock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(&globals->lock,
		NULL)))
		return sjme_error_default(error);

skip_validPipe:
	/* Is the pipe valid? */
	if (pipe != NULL)
	{
		argR->type = SJME_JAVA_TYPE_ID_OBJECT;
		argR->value.l = (sjme_jobject)pipe;
		return SJME_ERROR_NONE;
	}

	/* Not valid. */
	return sjme_error_vmError(inFrame, SJME_ERROR_MLE_CALL);

fail_badAlloc:
	sjme_thread_spinLockRelease(&globals->lock, NULL);
	return sjme_error_vmError(inFrame, error);
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
