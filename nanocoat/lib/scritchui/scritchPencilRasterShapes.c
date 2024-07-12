/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/util.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/core/coreRaster.h"
#include "sjme/debug.h"
#include "sjme/fixed.h"

sjme_errorCode sjme_scritchui_core_pencilDrawRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	sjme_errorCode error;
	sjme_jint xw, yh;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
		return sjme_error_default(error);
		
	/* Transform. */
	sjme_scritchui_core_transform(g, &x, &y);
	
	/* Pre-calculate coordinates. */
	xw = x + w;
	yh = y + h;
	
	/* Clear error state. */
	error = SJME_ERROR_NONE;
	
	/* Draw horizontal spans first. */
	error |= g->prim.drawHoriz(g, x, y, w);
	error |= g->prim.drawHoriz(g, x, yh, w);
	
	/* Draw vertical spans. */
	error |= g->prim.drawLine(g, x, y, x, yh);
	error |= g->prim.drawLine(g, xw, y, xw, yh);
	
	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchui_core_pencilDrawTriangle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
		return sjme_error_default(error);
		
	/* Transform. */
	sjme_scritchui_core_transform(g, &x1, &y1);
	sjme_scritchui_core_transform(g, &x2, &y2);
	sjme_scritchui_core_transform(g, &x3, &y3);
	
	/* Clear error state. */
	error = SJME_ERROR_NONE;
	
	/* Draw lines via primitives. */
	error |= g->prim.drawLine(g, x1, y1, x2, y2);
	error |= g->prim.drawLine(g, x2, y2, x3, y3);
	error |= g->prim.drawLine(g, x3, y3, x1, y1);
	
	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchui_core_pencilFillRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	sjme_errorCode error;
	sjme_scritchui_pencilDrawHorizFunc drawHoriz;
	sjme_jint yz, yze;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
		return sjme_error_default(error);
	
	/* Transform. */
	sjme_scritchui_core_transform(g, &x, &y);
	
	/* Cap width and height to 1 always. */
	if (w <= 0)
		w = 1;
	if (h <= 0)
		h = 1;
	
	/* Use primitives otherwise. */
	error = SJME_ERROR_NONE;
	drawHoriz = g->prim.drawHoriz;
	for (yz = y, yze = y + h; yz < yze; yz++)
		error |= drawHoriz(g, x, yz, w);
	
	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success? */
	return error;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchui_core_pencilFillTriangle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
		
	return SJME_ERROR_NOT_IMPLEMENTED;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}
