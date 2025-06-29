/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/nvm/mle.h"
#include "sjme/nvm/mleShelves.h"

SJME_NVM_MLE_FUNCTION_DECL(traceStack)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_SHELF_DECLARE(DebugShelf) =
{
	SJME_NVM_MLE_DEFINE(traceStack,
		SJME_MD(SJME_MD_A(SJME_MD_TRACE), ),
		"L", ),
	SJME_NVM_MLE_STOP()
};
