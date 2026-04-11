/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include "squirreljme.h"

/* //// MLE /// */
#define mleGroupId NativeScritchInterface
#define mleShelfClass "cc/squirreljme/jvm/mle/scritchui/NativeScritchInterface"
#define mleProxyTarget "cc/squirreljme/emulator/scritchui/EmulatedNativeScritchInterface"
#include "squirreljmeMle.h"
/* //////////// */

#define MLE_DESC_nativeInterface DESC_METHOD( \
	DESC_CLASS("cc/squirreljme/jvm/mle/scritchui/ScritchInterface"), )
MLE_FUNC_PROXY_STATIC(jobject, nativeInterface)

MLE_LIST_BEGIN()
	MLE_LIST_ITEM(nativeInterface),
MLE_LIST_END()
