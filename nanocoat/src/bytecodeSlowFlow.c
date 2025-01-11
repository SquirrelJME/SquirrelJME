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

SJME_NVM_BYTECODE_SLOW(NoOp)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Does nothing! */
	
	SJME_NVM_BYTECODE_SLOW_EXIT;
}
