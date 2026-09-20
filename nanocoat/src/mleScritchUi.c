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
	sjme_errorCode error;
	sjme_nvm inState;
	sjme_nvm_task inTask;
	sjme_jobject singleton;
	sjme_jclass proxyClass;

	/* If ScritchUI failed to initialized or this is otherwise headless */
	/* then we can just throw a MLECallError here. */
	if (sjme_atomic_g(sjme_jint, &SJME_F_S(inFrame)->globals.headlessDisplay))
		return sjme_error_mask(SJME_ERROR_HEADLESS_DISPLAY,
			SJME_ERROR_MLE_CALL);

	/* Recover the task we are in. */
	inTask = SJME_F_K(inFrame);
	if (inTask == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Has an instance of the proxy already been initialized? */
	singleton = sjme_atomic_g(sjme_jobject,
		&inTask->globals.singletons[SJME_NVM_TASK_SINGLETON_SCRITCHUI]);
	if (singleton != NULL)
	{
		argR->t = SJME_JAVA_TYPE_ID_OBJECT;
		argR->v.l = singleton;
		return SJME_ERROR_NONE;
	}

	/* Initialize the proxy class. */
	proxyClass = NULL;
	if (sjme_error_is(error = sjme_nvm_task_commonClass(SJME_F_T(inFrame),
		SJME_NVM_COMMON_SCRITCH_UI_PROXY, &proxyClass,
		SJME_JNI_TRUE)) || proxyClass == NULL)
		return sjme_error_default(error);

	/* Create singleton instance of the proxy. */
	singleton = NULL;
	if (sjme_error_is(error = sjme_nvm_instance_objectNew(SJME_F_T(inFrame),
		-1, SJME_NVM_STRUCT_OBJECT_INSTANCE, &singleton, proxyClass)) ||
		singleton == NULL)
		return sjme_error_default(error);

	/* Store singleton for later, it does need to be counted so it does */
	/* not get freed up. */
	sjme_atomic_s(sjme_jobject,
		&inTask->globals.singletons[SJME_NVM_TASK_SINGLETON_SCRITCHUI],
		sjme_weakUp(singleton));

	/* Return the newly created singleton. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = singleton;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_SHELF_DECLARE(NativeScritchUIShelf) =
{
	SJME_NVM_MLE_DEFINE(nativeInterface,
		SJME_MD(SJME_MD_L("cc/squirreljme/jvm/mle/scritchui/ScritchInterface"),
			SJME_MDMP___NO_ARGS__),
		SJME_MP(SJME_MP_L, SJME_MDMP___NO_ARGS__)),
	SJME_NVM_MLE_STOP()
};
