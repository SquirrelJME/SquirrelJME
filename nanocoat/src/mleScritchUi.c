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

SJME_NVM_MLE_FUNCTION_DECL(nativeInterface)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_SHELF_DECLARE(NativeScritchUIShelf) =
{
	SJME_NVM_MLE_DEFINE(nativeInterface,
		SJME_MD(SJME_MD_L("cc/squirreljme/jvm/mle/scritchui/ScritchInterface"),
			SJME_MDMP___NO_ARGS__),
		SJME_MP(SJME_MP_L, SJME_MDMP___NO_ARGS__)),
	SJME_NVM_MLE_STOP()
};
