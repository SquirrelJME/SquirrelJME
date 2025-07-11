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
#define NAME_SIZE 16
	sjme_jint i, left;
	sjme_cchar buf[BUF_SIZE];
	sjme_lpstr printAt;
	sjme_jvalue* valueP;
	const sjme_nvm_walk_step* inStep;
	
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
	inStep = at->inStep;
	if (at->index < 0 || at->index == INT32_MAX)
		snprintf(printAt, left, "%3s: %-*.*s%s",
			(at->index < 0 ? "STR" : "END"),
			(inStep == NULL ? 0 : NAME_SIZE),
			(inStep == NULL ? 0 : NAME_SIZE),
			(inStep == NULL ? "" : inStep->memberName),
			(inStep == NULL ? "" : "= "));
	else
		snprintf(printAt, left, "%3d: %-*.*s%s",
			at->index,
			(inStep == NULL ? 0 : NAME_SIZE),
			(inStep == NULL ? 0 : NAME_SIZE),
			(inStep == NULL ? "" : inStep->memberName),
			(inStep == NULL ? "" : "= "));
	
	/* Print where? */
	printAt = &buf[strlen(buf)];
	left = BUF_SIZE - strlen(buf);

	/* Printing depends on the type. */
	switch (at->typeId)
	{
		case SJME_NVM_WALK_PSEUDO_ALLOC_POOL:
			snprintf(printAt, left, "Allocation Pool");
			break;
		
		case SJME_NVM_WALK_PSEUDO_ATOMIC_JINT:
			snprintf(printAt, left, "Atomic sjme_jint: 0x%08x",
				sjme_atomic_sjme_jint_get(at->at.atomicInt));
			break;
		
		case SJME_NVM_WALK_PSEUDO_CLOSEABLE:
			snprintf(printAt, left, "Closeable (%d left)",
				sjme_alloc_weakRefLeftR(at->at.closeable));
			break;

		case SJME_NVM_WALK_PSEUDO_CLOSE_HANDLER:
			snprintf(printAt, left, "Close handler (%p)",
				(void*)*at->at.intPointer);
			break;
		
		case SJME_NVM_WALK_PSEUDO_COMMON:
			snprintf(printAt, left, "NVM Common");
			break;

		case SJME_NVM_WALK_PSEUDO_FRONT_END:
			snprintf(printAt, left, "Front End Data");
			break;

		case SJME_NVM_WALK_PSEUDO_NVM_STRUCT_TYPE:
			snprintf(printAt, left, "NVM Structure %d",
				*at->at.nvmStructType);
			break;

		case SJME_NVM_WALK_PSEUDO_PRIMITIVE:
			if (inStep == NULL)
				snprintf(printAt, left, "Lost primitive?");
			else
			{
				/* Determine actual value. */
				valueP = at->at.raw;
				if (inStep->isPointer)
					valueP = *((sjme_jvalue**)valueP);

				/* Print its value. */
				switch (inStep->javaType)
				{
					case SJME_BASIC_TYPE_ID_INTEGER:
						snprintf(printAt, left, "sjme_jint: 0x%08X",
							valueP->i);
						break;

					default:
						snprintf(printAt, left, "(Primitive %d?)",
							inStep->javaType);
						break;
				}
			}
			break;

		case SJME_NVM_WALK_PSEUDO_SPIN_LOCK:
			snprintf(printAt, left, "Mutex/Lock (%d by %p)",
				sjme_atomic_sjme_jint_get(&at->at.spinLock->count),
				(void*)sjme_atomic_sjme_thread_get(&at->at.spinLock->owner));
			break;
		
		case SJME_NVM_STRUCT_STATE:
			snprintf(printAt, left, "NVM State %p",
				at->at.raw);
			break;
		
		default:
			if (inStep != NULL)
				snprintf(printAt, left, "(Type %d)",
					at->typeId);
			else
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
#undef NAME_SIZE
}

const sjme_nvm_walk_functions sjme_nvm_walk_printDump =
{
	sjme_sm(.pre, NULL),
	sjme_sm(.step, sjme_nvm_walk_debugStep),
};
