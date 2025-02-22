/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/util.h"
#include "sjme/nvm/bytecode.h"
#include "sjme/nvm/bytecodeSlow.h"
#include "sjme/nvm/task.h"

SJME_NVM_BYTECODE_SLOW(IfEq)
{
	sjme_jint offset;
	sjme_jvalueTyped value;
	SJME_NVM_BYTECODE_SLOW_ENTRY;
	
	/* Read the branch value. */
	offset = sjme_big_short(*sjme_util_memUnaligned16(&relRawCode[1]));
	
	/* Pop single integer value. */
	memset(&value, 0, sizeof(value));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Successful branch? */
	if (value.value.i == 0)
		pcNew->adjust = offset;

	/* Failed branch. */
	else
		pcNew->adjust = 3;

	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(NoOp)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Does nothing except skip the instruction. */
	pcNew->adjust = 1;
	
	SJME_NVM_BYTECODE_SLOW_EXIT;
}
