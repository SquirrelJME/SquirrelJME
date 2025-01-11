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
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);

	SJME_NVM_BYTECODE_SLOW_EXIT;
}
