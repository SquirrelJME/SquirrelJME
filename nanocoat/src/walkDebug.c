/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/walk.h"

static sjme_errorCode sjme_nvm_walk_debugStep(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at)
{
#define BUF_SIZE 256
	sjme_jint i, left;
	sjme_cchar buf[BUF_SIZE];
	sjme_lpstr printAt;
	
	if (at == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Indent the buffer. */
	memset(buf, 0, sizeof(BUF_SIZE));
	for (i = 0; i < at->depth; i++)
		strncat(buf, "|", BUF_SIZE - 1);
	
	/* Print where? */
	printAt = &buf[strlen(buf)];
	left = BUF_SIZE - strlen(buf);
	if (left <= 0)
	{
		printAt = &buf[BUF_SIZE - 1];
		left = 0;
	}

	/* Print out information. */
	if (at->index < 0 || at->index == INT32_MAX)
		snprintf(printAt, left, "%3s: ",
			(at->index < 0 ? "STR" : "END"));
	else
		snprintf(printAt, left, "%3d: ",
			at->index);
	
	/* Print where? */
	printAt = &buf[strlen(buf)];
	left = BUF_SIZE - strlen(buf);

	/* Printing depends on the type. */
	switch (at->typeId)
	{
		case SJME_NVM_STRUCT_STATE:
			snprintf(printAt, left, "NanoCoat State %p",
				at->at.raw);
			break;
		
		default:
			snprintf(printAt, left, "Type %d @ %p?",
				at->typeId, at->at.raw);
			break;
	}

	/* Print out the buffer. */
	buf[BUF_SIZE - 1] = 0;
	sjme_messageB("%s", buf);

	/* Success! */
	return SJME_ERROR_NONE;
#undef BUF_SIZE
}

const sjme_nvm_walk_functions sjme_nvm_walk_printDump =
{
	sjme_sm(.pre, NULL),
	sjme_sm(.step, sjme_nvm_walk_debugStep),
};
