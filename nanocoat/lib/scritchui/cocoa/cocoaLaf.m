/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/cocoa/cocoa.h"
#include "lib/scritchui/cocoa/cocoaIntern.h"

sjme_errorCode sjme_scritchui_cocoa_lafDpiProject(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrInValue sjme_jboolean toBase,
	sjme_attrInNullable sjme_jint* inOutX,
	sjme_attrInNullable sjme_jint* inOutY)
{
	NSView* view;
	NSPoint point;

	if (inState == NULL || (inOutX == NULL && inOutY == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;

#if (SJME_CONFIG_COCOA_VERSION_LEAST(MAC_OS_X_VERSION_10_5) && \
	SJME_CONFIG_COCOA_VERSION_BEFORE(MAC_OS_X_VERSION_10_7)) || \
	SJME_CONFIG_GNUSTEP_VERSION_LEAST(SJME_GNUSTEP_GUI_0_20_0)
	/* Without a context, we cannot perform any translation. */
	if (inContext == NULL)
		return SJME_ERROR_NONE;

	/* Recover view, if there is none then ignore. */
	view = inContext->common.handle[SJME_SUI_COCOA_H_NSVIEW];
	if (view == NULL)
		return SJME_ERROR_NONE;

	/* Perform point conversion. */
	if (inOutX != NULL)
		point.x = *inOutX;
	if (inOutY != NULL)
		point.y = *inOutY;

	if (toBase)
		point = [view convertPointToBase:point];
	else
		point = [view convertPointFromBase:point];

	/* Return the result of the translation. */
	if (inOutX != NULL)
		*inOutX = point.x;
	if (inOutY != NULL)
		*inOutY = point.y;
	return SJME_ERROR_NONE;

#elif SJME_CONFIG_COCOA_VERSION_LEAST(MAC_OS_X_VERSION_10_7)
	sjme_todo();
	return sjme_error_notImplemented(0);

#else
	/* This does not exist before 10.5, so do nothing. */
	return SJME_ERROR_NONE;
#endif
}
