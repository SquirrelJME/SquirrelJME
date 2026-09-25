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
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrInNotNull sjme_jobject proxyInstance,
	sjme_attrInNotNull sjme_jmethodID proxyMethod,
	sjme_attrInNotNull sjme_jvalueTyped* argR,
	sjme_attrInNotNull sjme_jint argC,
	sjme_attrInNotNull sjme_jvalueTyped* argV)
{
	sjme_errorCode error;

	if (inFrame == NULL || proxyInstance == NULL || proxyMethod == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("ScritchUI MLE: %s.%s()",
		sjme_charSeq_tempUtf(proxyMethod->member.name->seq),
		sjme_charSeq_tempUtf(proxyMethod->member.type->seq));
#endif

	/* Forward to minor shelf handling, this already exists and thus we do */
	/* not need to duplicate this functionality. */
	if (sjme_error_is(error = sjme_mle_mleCallShelfM(inFrame,
		&SJME_NVM_MLE_SHELF_NAME(ScritchUiProxy)[0],
		proxyMethod->member.name->seq,
		proxyMethod->member.type->seq,
		argR, argC, argV)))
		return sjme_error_vmError(inFrame, error);

	/* Success! */
	return SJME_ERROR_NONE;
}
