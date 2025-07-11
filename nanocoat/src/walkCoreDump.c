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

const sjme_nvm_walk_functions sjme_nvm_walk_coreDump =
{
	sjme_sm(.pre, NULL),
	sjme_sm(.step, sjme_nvm_walk_coreStart),
};
