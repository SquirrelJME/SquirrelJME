/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <sjme/nvm/task.h>

#include "sjme/nvm/bytecode.h"
#include "sjme/nvm/bytecodeSlow.h"

static sjme_errorCode sjme_nvm_byteCode_slowLdcAny(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, 256) sjme_byteCode id,
	sjme_attrInNotNull sjme_byteCode* relRawCode,
	sjme_attrInNotNull sjme_nvm_class_poolEntry* entry)
{
	if (inFrame == NULL || entry == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* What happens, depends on the type. */
	switch (entry->type)
	{
		case SJME_NVM_CLASS_POOL_TYPE_STRING:
			return sjme_nvm_task_frameStackPushStringP(
				inFrame, entry->utf.utf);
		
		default:
			sjme_todo("Impl? %d", entry->type);
			return sjme_error_notImplemented(entry->type);
	}

	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(Ldc)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Read in pool value. */
	poolIndex = relRawCode[1];
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_INTEGER,
		SJME_NVM_CLASS_POOL_TYPE_FLOAT,
		SJME_NVM_CLASS_POOL_TYPE_STRING,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		0)))
		return sjme_error_default(error);

	/* Forward to common handler. */
	return sjme_nvm_byteCode_slowLdcAny(inFrame, id, relRawCode, entry);
}
