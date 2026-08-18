/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdio.h>

/* //// MLE /// */
#define mleGroupId TerminalShelf
#define mleShelfClass "cc/squirreljme/jvm/mle/TerminalShelf"
#define mleProxyTarget "cc/squirreljme/emulator/EmulatedTerminalShelf"
#include "squirreljmeMle.h"
/* //////////// */

#include "squirreljme.h"

#define MLE_DESC_available DESC_METHOD(DESC_INT, \
	DESC_PIPE)
MLE_FUNC_PROXY_STATIC(jint, available)

#define MLE_DESC_close DESC_METHOD(DESC_INT, \
	DESC_PIPE)
MLE_FUNC_PROXY_STATIC(jint, close)

#define MLE_DESC_flush DESC_METHOD(DESC_INT, \
	DESC_PIPE)
MLE_FUNC_PROXY_STATIC(jint, flush)

#define MLE_DESC_fromStandard DESC_METHOD(DESC_PIPE, \
	DESC_INT)
MLE_FUNC_PROXY_STATIC(jobject, fromStandard)

#define MLE_DESC_read_single DESC_METHOD(DESC_INT, \
	DESC_PIPE)
MLE_FUNC_PROXY_STATIC_ALT(jint, read, single)

#define MLE_DESC_read_multi DESC_METHOD(DESC_INT, \
	DESC_PIPE DESC_ARRAY(DESC_BYTE) DESC_INT DESC_INT)
MLE_FUNC_PROXY_STATIC_ALT(jint, read, multi)

#define MLE_DESC_write_single DESC_METHOD(DESC_INT, \
	DESC_PIPE DESC_INT)
MLE_FUNC_PROXY_STATIC_ALT(jint, write, single)

#define MLE_DESC_write_multi DESC_METHOD(DESC_INT, \
	DESC_PIPE DESC_ARRAY(DESC_BYTE) DESC_INT DESC_INT)
MLE_FUNC_PROXY_STATIC_ALT(jint, write, multi)

MLE_LIST_BEGIN()
	MLE_LIST_ITEM(available),
	MLE_LIST_ITEM(close),
	MLE_LIST_ITEM(flush),
	MLE_LIST_ITEM(fromStandard),
	MLE_LIST_ITEM_ALT(read, single),
	MLE_LIST_ITEM_ALT(read, multi),
	MLE_LIST_ITEM_ALT(write, single),
	MLE_LIST_ITEM_ALT(write, multi),
MLE_LIST_END()
