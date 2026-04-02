/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/* //// MLE /// */
#define mleGroupId ThreadShelf
#define mleShelfClass "cc/squirreljme/jvm/mle/ThreadShelf"
#define mleProxyTarget "cc/squirreljme/emulator/NativeThreadShelf"
#include "squirreljmeMle.h"
/* //////////// */

#include "squirreljme.h"

#define MLE_DESC_aliveThreadCount DESC_METHOD(DESC_INT, \
	DESC_BOOLEAN DESC_BOOLEAN)
MLE_FUNC_PROXY_STATIC(jint, aliveThreadCount)

#define MLE_DESC_currentJavaThread DESC_METHOD(DESC_THREAD, )
MLE_FUNC_PROXY_STATIC(jobject, currentJavaThread)

#define MLE_DESC_javaThreadSetDaemon DESC_METHOD(DESC_VOID, \
	DESC_THREAD)
MLE_FUNC_PROXY_STATIC(void, javaThreadSetDaemon)

#define MLE_DESC_setTrace DESC_METHOD(DESC_VOID, \
	DESC_STRING DESC_ARRAY(DESC_TRACEPOINT))
MLE_FUNC_PROTO(void, setTrace, jobject string, jobject array)
{
	// Has no effect
}

#define MLE_DESC_waitForUpdate DESC_METHOD(DESC_BOOLEAN, DESC_INT)
MLE_FUNC_PROXY_STATIC(jboolean, waitForUpdate)

MLE_LIST_BEGIN()
	MLE_LIST_ITEM(aliveThreadCount),
	MLE_LIST_ITEM(currentJavaThread),
	MLE_LIST_ITEM(javaThreadSetDaemon),
	MLE_LIST_ITEM(setTrace),
	MLE_LIST_ITEM(waitForUpdate),
MLE_LIST_END()
