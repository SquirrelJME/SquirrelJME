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

SJME_NVM_MLE_FUNCTION_DECL(vmType)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

const sjme_nvm_mleShelf sjme_nvm_mleRuntimeShelf[] =
{
	SJME_NVM_MLE_DEFINE(vmType, "()I",
		SJME_MI, 0, {}),
	
	{NULL, NULL, -1, -1, NULL, NULL},
};

