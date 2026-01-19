/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/framebuffer/fb.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiTypes.h"

sjme_errorCode sjme_scritchui_fb_hardwareGraphics(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrOutNullable sjme_alloc_weak* outWeakPencil,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInPositiveNonZero sjme_jint bh,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEndBindable* inLockFrontEndCopy,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInNullable const sjme_frontEndBindable* pencilFrontEndCopy)
{
	sjme_scritchui wrappedState;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If there is a system implementation, use it. */
	wrappedState = inState->wrappedState;
	if (wrappedState->impl->hardwareGraphics != NULL)
		return wrappedState->impl->hardwareGraphics(wrappedState,
			outPencil, outWeakPencil, pf, bw, bh, inLockFuncs,
			inLockFrontEndCopy, sx, sy, sw, sh, pencilFrontEndCopy);
	
	/* Otherwise, use general ScritchUI implementation */
	return wrappedState->apiInThread->hardwareGraphics(wrappedState,
		outPencil, outWeakPencil, pf, bw, bh, inLockFuncs,
		inLockFrontEndCopy, sx, sy, sw, sh, pencilFrontEndCopy);
}
