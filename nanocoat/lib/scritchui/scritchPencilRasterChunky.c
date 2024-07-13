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

sjme_errorCode sjme_scritchpen_core_copyArea(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint dx,
	sjme_attrInValue sjme_jint dy,
	sjme_attrInValue sjme_jint anchor)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Need to lock? */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	return SJME_ERROR_NOT_IMPLEMENTED;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_core_drawXRGB32Region(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_jint* data,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositive sjme_jint dataLen,
	sjme_attrInPositive sjme_jint scanLen,
	sjme_attrInValue sjme_jboolean alpha,
	sjme_attrInValue sjme_jint xSrc,
	sjme_attrInValue sjme_jint ySrc,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInValue sjme_jint trans,
	sjme_attrInValue sjme_jint xDest,
	sjme_attrInValue sjme_jint yDest,
	sjme_attrInValue sjme_jint anchor,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest,
	sjme_attrInPositive sjme_jint origImgWidth,
	sjme_attrInPositive sjme_jint origImgHeight)
{
	sjme_errorCode error;
	sjme_scritchui_pencilMatrix m;
	sjme_fixed wx, zy, wxBase, zyMajor;
	sjme_jint dx, dy, iwx, izy, at, q;
	sjme_jint* flatRgb;
	sjme_jint* srcRgb;
	void* rawScan;
	void* srcRaw;
	sjme_jint rawScanBytes, flatRgbBytes;
	sjme_jboolean destAlpha;
	
	if (g == NULL || data == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The source rectangle must always be in bounds. */
	if (xSrc < 0 || ySrc < 0 || wSrc <= 0 || hSrc <= 0 ||
		(xSrc + wSrc) > origImgWidth || (ySrc + hSrc) > origImgHeight ||
		(xSrc + wSrc) < 0 || (ySrc + hSrc) < 0 ||
		origImgWidth < 0 || origImgHeight < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (off < 0 || dataLen < 0 || (off + dataLen) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Drawing nothing? */
	if (wDest <= 0 || hDest <= 0)
		return SJME_ERROR_NONE;
	
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &xDest, &yDest);
	
	/* We are now doing the transforming and drawing ourselves. */
	/* Calculate transformation matrix. */
	memset(&m, 0, sizeof(m));
	if (sjme_error_is(error = sjme_scritchpen_coreUtil_applyRotateScale(
		&m, trans, wSrc, hSrc, wDest, hDest)))
		return sjme_error_default(error);
	
	/* Anchor to target coordinates after scaling, because we need */
	/* to know what our target scale is. */
	if (sjme_error_is(error = sjme_scritchpen_coreUtil_applyAnchor(anchor,
		xDest, yDest, m.tw, m.th, 0,
		&xDest, &yDest)))
		return sjme_error_default(error);
	
	/* Determine how large our raw scan buffer actually is. */
	rawScanBytes = -1;
	if (sjme_error_is(error = g->util->rawScanBytes(g,
		m.tw, 0,
		&rawScanBytes, NULL)) ||
		rawScanBytes < 0)
		return sjme_error_default(error);
	
	/* RGB buffer is this many bytes. */
	flatRgbBytes = m.tw * sizeof(*flatRgb);
	
	/* Setup input and output RGB buffers. */
	rawScan = sjme_alloca(rawScanBytes);
	flatRgb = sjme_alloca(flatRgbBytes);
	if (rawScan == NULL || flatRgb == NULL)
		return sjme_error_defaultOr(error,
			SJME_ERROR_OUT_OF_MEMORY);
	
	/* Clear buffers. */
	memset(rawScan, 0, rawScanBytes);
	memset(flatRgb, 0, flatRgbBytes);
	
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Figure out the position of our base pointer. */
	/* Matrix multiplication? Squeak? */
	wxBase = sjme_fixed_mul(sjme_fixed_hi(xSrc), m.x.wx) +
		sjme_fixed_mul(sjme_fixed_hi(ySrc), m.x.zy);
	wx = wxBase;
	zyMajor = sjme_fixed_mul(sjme_fixed_hi(xSrc), m.y.wx) +
		sjme_fixed_mul(sjme_fixed_hi(ySrc), m.y.zy);
	zy = zyMajor;
	
	/* Scan copy, rotate, and stretch by destination scans. */
	for (dy = 0; dy < m.th; dy++, wxBase += m.y.wx, zyMajor += m.y.zy)
	{
		/* Reset wx to base for start of scan. */
		wx = wxBase;
		zy = zyMajor;
		
		/* Scan in RGB line. */
		for (dx = 0; dx < m.tw; dx++, wx += m.x.wx, zy += m.x.zy)
		{
			/* Get pixel from source buffer. */
			iwx = sjme_fixed_int(sjme_fixed_round(wx)) % wSrc;
			izy = sjme_fixed_int(sjme_fixed_round(zy)) % hSrc;
			
			/* Keep in bounds. */
			if (iwx < 0)
				iwx += (wSrc - 1);
			if (izy < 0)
				izy += (hSrc - 1);
			
			/* Copy pixel from source? */
			at = off + ((izy * scanLen) + iwx);
			flatRgb[dx] = data[at];
		}
		
		/* Map RGB to Native. */
		if (sjme_error_is(error = g->util->rgbToRawScan(
			g, rawScan, 0, rawScanBytes,
			flatRgb, 0, m.tw)))
			goto fail_any;
		
		/* Render RGB line at destination. */
		sjme_todo("Impl?");
#if 0
		if (sjme_error_is(error = g->prim.rawScanPut(g,
			xDest, yDest + dy,
			rawScan, rawScanBytes, m.tw,
			alpha)))
			goto fail_any;
#endif
	}
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
		
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}
