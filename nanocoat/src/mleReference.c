/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/cleanup.h"
#include "sjme/config.h"
#include "sjme/nvm/mle.h"
#include "sjme/nvm/mleShelves.h"

SJME_NVM_MLE_FUNCTION_DECL(weakInit)
{
	sjme_errorCode error;
	sjme_jweak weak;
	sjme_jobject pointer;
	sjme_jobject queue;

	/* Grab arguments. */
	weak = (sjme_jweak)argV[0].v.l;
	pointer = argV[1].v.l;
	queue = argV[2].v.l;

	if (weak == NULL || pointer == NULL)
		return SJME_ERROR_MLE_CALL;

	/* Must be a weak reference. */
	if (!sjme_nvm_isAR(weak, SJME_NVM_STRUCT_WEAK_INSTANCE))
		return SJME_ERROR_MLE_CALL;

	/* We can check if this was initialized before we even grab the lock. */
	if (sjme_atomic_sjme_jint_get(&weak->beenInit))
		return SJME_ERROR_MLE_CALL;

	/* Lock reference. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&weak->object.common.lock)))
		return sjme_error_vmError(inFrame,
			sjme_error_mask(error, SJME_ERROR_MLE_CALL));

	/* Double check initialization, it can only happen once. */
	if (sjme_atomic_sjme_jint_compareSet(&weak->beenInit,
		SJME_JNI_FALSE, SJME_JNI_TRUE))
		goto fail_beenInit;

	/* Set weak data. */
	sjme_atomic_sjme_jobject_compareSet(&weak->pointer,
		NULL, pointer);
	sjme_atomic_sjme_jobject_compareSet(&weak->queue,
		NULL, queue);
	
	/* Unlock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&weak->object.common.lock, NULL)))
		return sjme_error_vmError(inFrame,
			sjme_error_mask(error, SJME_ERROR_MLE_CALL));

	/* Success! */
	return SJME_ERROR_NONE;

fail_beenInit:
	sjme_thread_spinLockRelease(&weak->object.common.lock, NULL);
	return SJME_ERROR_MLE_CALL;
}

SJME_NVM_MLE_SHELF_DECLARE(ReferenceShelf) =
{
	SJME_NVM_MLE_DEFINE(weakInit,
		SJME_MD(SJME_MD_V, SJME_MD_REFERENCE SJME_MD_OBJECT
			SJME_MD_REFERENCE_QUEUE),
		"LLL"),
	SJME_NVM_MLE_STOP()
};
