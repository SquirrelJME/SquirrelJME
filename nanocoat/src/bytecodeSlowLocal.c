/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/bytecode.h"
#include "sjme/nvm/bytecodeSlow.h"
#include "sjme/nvm/task.h"

SJME_NVM_BYTECODE_SLOW(ALoadZ)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Always a single byte. */
	pcNew->adjust = 1;

	/* Push copy of the local to the stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameLocalPush(
		inFrame,
		SJME_JAVA_TYPE_ID_OBJECT,
		id - 42)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(ILoadZ)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Always a single byte. */
	pcNew->adjust = 1;

	/* Push copy of the local to the stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameLocalPush(
		inFrame,
		SJME_JAVA_TYPE_ID_INTEGER,
		id - 26)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}
