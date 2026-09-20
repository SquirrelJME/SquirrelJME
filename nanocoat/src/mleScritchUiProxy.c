/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/instanceProxy.h"
#include "sjme/nvm/task.h"
#include "sjme/nvm/mle.h"
#include "sjme/nvm/mleShelves.h"

SJME_NVM_MLE_SHELF_DECLARE(ScritchUiProxy) =
{
	SJME_NVM_MLE_STOP()
};

sjme_errorCode sjme_nvm_mle_scritchUiProxyHandler(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_jobject proxyInstance,
	sjme_attrInNotNull const sjme_nvm_instance_proxyMethod* proxyMethod,
	sjme_attrInNotNull sjme_jvalueTyped* argR,
	sjme_attrInNotNull sjme_jint argC,
	sjme_attrInNotNull sjme_jvalueTyped* argV)
{
	if (inFrame == NULL || proxyInstance == NULL || proxyMethod == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Forward to minor shelf handling, this already exists and thus we do */
	/* not need to duplicate this functionality. */
	return sjme_mle_mleCallShelfM(inFrame,
		&SJME_NVM_MLE_SHELF_NAME(ScritchUiProxy)[0],
		proxyMethod->id->member.name->seq,
		proxyMethod->id->member.type->seq,
		argR, argC, argV);
}
