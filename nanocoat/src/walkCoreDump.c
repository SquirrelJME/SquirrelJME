/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/*****************************************************************************
 * Core dumps utilize the CBOR format and dump the entirety of the
 * virtual machine state structure @c sjme_nvm to a stream. This is useful
 * for debugging the state of the virtual machine.
 *
 * Such created dumps can be reloaded and restored to a running virtual
 * machine potentially.
 *
 * https://www.rfc-editor.org/rfc/rfc8949
 ****************************************************************************/

#include "sjme/nvm/walk.h"

typedef struct sjme_nvm_walk_coreHandler
{
	/** The handler type ID. */
	sjme_jint typeId;

	/** The function to use for handling. */
	sjme_nvm_walk_stepHandlerFunc function;
} sjme_nvm_walk_coreHandler;

static const sjme_nvm_walk_coreHandler sjme_nvm_walk_coreHandlers[] =
{
	/* End. */
	{
		sjme_sm(.typeId, 0),
		sjme_sm(.function, NULL),
	}
};

static sjme_errorCode sjme_nvm_walk_coreStart(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at)
{
	if (root == NULL || at == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Parent got unbound somehow? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

const sjme_nvm_walk_functions sjme_nvm_walk_coreDumpFunctions =
{
	sjme_sm(.pre, NULL),
	sjme_sm(.step, sjme_nvm_walk_coreStart),
};

sjme_errorCode sjme_nvm_walk_coreDumpFile(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInNotNull sjme_lpcstr filePath)
{
#define MINI_SIZE 8192
	sjme_errorCode error;
	sjme_jubyte mini[MINI_SIZE];
	sjme_alloc_pool miniPool;
	sjme_seekable seekable;
	sjme_stream_output outStream;
	
	if (inState == NULL || filePath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Use the state NAL? */
	if (nal == NULL)
		nal = inState->nal;

	if (nal == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Not supported? */
	if (nal->fileOpen == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;

	/* Allocate mini allocation pool. */
	memset(mini, 0, sizeof(mini));
	miniPool = NULL;
	if (sjme_error_is(error = sjme_alloc_poolInitStatic(&miniPool,
		mini, MINI_SIZE)) || miniPool == NULL)
		return sjme_error_default(error);

	/* Open native file. */
	seekable = NULL;
	if (sjme_error_is(error = nal->fileOpen(miniPool, filePath,
		&seekable, SJME_NAL_OPEN_WRITE_TRUNCATE)) || seekable == NULL)
		return sjme_error_default(error);

	/* Open output stream over the seekable. */
	outStream = NULL;
	if (sjme_error_is(error = sjme_stream_outputOpenSeekable(
		seekable, &outStream, 0, -1, SJME_JNI_TRUE)) ||
		outStream == NULL)
		goto fail_openSub;

	/* Perform the dump. */
	if (sjme_error_is(error = sjme_nvm_walk_coreDumpStream(inState,
		outStream)))
		goto fail_dump;

	/* Close the seekable. */
	if (sjme_error_is(error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(seekable))))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;

fail_dump:
fail_openSub:
	if (seekable != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(seekable));
	
	return sjme_error_default(error);
#undef MINI_SIZE
}

sjme_errorCode sjme_nvm_walk_coreDumpStream(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_stream_output outStream)
{
	if (inState == NULL || outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Perform the core dump. */
	return sjme_nvm_walk_start(inState, SJME_NVM_STRUCT_STATE,
		&sjme_nvm_walk_coreDumpFunctions, outStream);
}
