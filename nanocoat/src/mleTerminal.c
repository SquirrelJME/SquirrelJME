/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/mle.h"
#include "sjme/nvm/mleBrackets.h"
#include "sjme/nvm/mleConst.h"
#include "sjme/nvm/mleShelves.h"

static sjme_jint sjme_nvm_mleFunc_mleTerminal_mapIoException(
	sjme_attrInValue sjme_errorCode error,
	sjme_attrOutNotNull sjme_jvalueTyped* result)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

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
	sjme_errorCode error;
	sjme_nvm_mle_pipe pipe;

	/* Must be an actual pipe. */
	pipe = (sjme_nvm_mle_pipe)argV[0].v.l;
	if (!sjme_nvm_isAR(pipe, SJME_NVM_STRUCT_BRACKET_PIPE))
		return SJME_ERROR_MLE_CALL;

	/* Not an output pipe? */
	if (!pipe->isOutput)
		return SJME_ERROR_MLE_CALL;
	
	/* Write call. */
	if (sjme_error_is(error = sjme_stream_outputFlush(pipe->stream.out)))
		return sjme_nvm_mleFunc_mleTerminal_mapIoException(error, argR);

	/* Success! */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = 0;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(fromStandard)
{
	sjme_errorCode error;
	sjme_nvm_taskGlobals* globals;
	sjme_nvm_mle_standardPipeType type;
	sjme_nvm_mle_pipe pipe;
	const sjme_nal* nal;

	/* Check. */
	type = (sjme_nvm_mle_standardPipeType)argV[0].v.i;
	if (type < 0 || type >= SJME_NVM_MLE_NUM_STD_PIPES)
		return SJME_ERROR_MLE_CALL;
	
	/* Has a pipe already been created? We want single brackets for each */
	/* standard pipe that exists. */
	globals = &inFrame->inTask->globals;
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
		if (sjme_error_is(error = sjme_nvm_instance_objectNewNU(SJME_F_T(inFrame),
			sizeof(*pipe), SJME_NVM_STRUCT_BRACKET_PIPE,
			SJME_AS_JOBJECTP(&pipe), SJME_NVM_BRACKET_NAME_PIPE)) ||
			pipe == NULL)
			goto fail_badAlloc;

		/* Is this an output pipe? */
		pipe->isOutput = (type != SJME_NVM_MLE_STD_PIPE_STDIN);
		
		/* Input pipe. */
		nal = inFrame->inState->nal;
		if (type == SJME_NVM_MLE_STD_PIPE_STDIN)
		{
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);
		}

		/* Output pipe. */
		else
		{
			if (sjme_error_is(error = sjme_stream_outputOpenStdIo(
				inFrame->inState->allocPool, &pipe->stream.out,
				(sjme_pointer)&nal->stdIo[type])) ||
				pipe->stream.out == NULL)
				goto fail_badOpen;
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
		argR->t = SJME_JAVA_TYPE_ID_OBJECT;
		argR->v.l = (sjme_jobject)pipe;
		return SJME_ERROR_NONE;
	}

	/* Not valid. */
	return sjme_error_vmError(inFrame, SJME_ERROR_MLE_CALL);

fail_badOpen:
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
	sjme_errorCode error;
	sjme_nvm_mle_pipe pipe;
	sjme_jbyte single;

	/* Must be an actual pipe. */
	pipe = (sjme_nvm_mle_pipe)argV[0].v.l;
	if (!sjme_nvm_isAR(pipe, SJME_NVM_STRUCT_BRACKET_PIPE))
		return SJME_ERROR_MLE_CALL;

	/* Not an output pipe? */
	if (!pipe->isOutput)
		return SJME_ERROR_MLE_CALL;
	
	/* Write call. */
	single = (sjme_jbyte)argV[1].v.i;
	if (sjme_error_is(error = sjme_stream_outputWrite(pipe->stream.out,
		&single, 1)))
		return sjme_nvm_mleFunc_mleTerminal_mapIoException(error, argR);

	/* Success! */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = 0;
	return SJME_ERROR_NONE;
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
		SJME_MD(SJME_MD_I, SJME_MD_PIPE SJME_MD_I),
		"ILI"),
	SJME_NVM_MLE_DEFINE_ALT(write, multi,
		SJME_MD(SJME_MD_I, SJME_MD_PIPE SJME_MD_A(SJME_MD_B)
			SJME_MD_I SJME_MD_I),
		"ILLII"),
	
	SJME_NVM_MLE_STOP()
};
