/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI Pencil Drawing.
 * 
 * @file
 * @since 2024/05/01
 */

#ifndef SJME_C_SCRITCHUIPENCIL_H
#define SJME_C_SCRITCHUIPENCIL_H

#include "sjme/charSeq.h"
#include "sjme/alloc.h"
#include "lib/scritchui/scritchuiBasic.h"
#include "lib/scritchui/scritchuiTypeDefs.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUIPENCIL_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Returns whether the given pixel format uses an alpha channel. 
 * 
 * @param pf The pixel format to check.
 * @return If the pixel format uses an alpha channel.
 * @since 2025/11/27
 */
sjme_jboolean sjme_scritchpen_hasAlpha(
	sjme_attrInValue sjme_gfx_pixelFormat pf);

/**
 * Creates a hardware reference bracket to the native hardware graphics.
 * 
 * @param inOutPencil The input and output pencil.
 * @param inState The input ScritchUI state.
 * @param pf The @link sjme_gfx_pixelFormat @endlink used for the draw.
 * @param bw The buffer width, this is the scanline width of the buffer.
 * @param bh The buffer height.
 * @param inLockFuncs The locking functions to use for buffer access.
 * @param inLockFrontEndCopy Front end copy data for locks.
 * @param tx Forced X translate.
 * @param ty Forced Y translate.
 * @param sx Starting surface X coordinate.
 * @param sy Starting surface Y coordinate.
 * @param sw Surface width.
 * @param sh Surface height.
 * @param defaultFont The default font to use.
 * @param copyFrontEnd The front end copy.
 * @return The bracket capable of drawing hardware accelerated graphics.
 * @return An error if the requested graphics are not valid.
 * @since 2024/05/01
 */
sjme_errorCode sjme_scritchpen_initBufferStatic(
	sjme_attrInOutNotNull sjme_scritchui_pencil inOutPencil,
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInPositiveNonZero sjme_jint bh,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEndBindable* inLockFrontEndCopy,
	sjme_attrInValue sjme_jint tx,
	sjme_attrInValue sjme_jint ty,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInNotNull sjme_scritchui_pencilFont defaultFont,
	sjme_attrInNullable const sjme_frontEndBindable* copyFrontEnd);

/**
 * Static pencil function initialization.
 * 
 * @param inState The input ScritchUI state.
 * @param inPencil The pencil to be initialized.
 * @param inFunctions The functions to set.
 * @param inLockFuncs Functions for native locking.
 * @param inLockFrontEndCopy Front end copy data for locks.
 * @param pf The pixel format used.
 * @param tx Forced X translate.
 * @param ty Forced Y translate.
 * @param sw The surface width.
 * @param sh The surface height.
 * @param bw The buffer width, the scanline length.
 * @param defaultFont The default font to use.
 * @param copyFrontEnd Optional front end data to copy.
 * @return Any error code if applicable.
 * @since 2024/05/04
 */
sjme_errorCode sjme_scritchpen_initStatic(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_pencil inPencil,
	sjme_attrInNotNull const sjme_scritchui_pencilImplFunctions* inFunctions,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEndBindable* inLockFrontEndCopy,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInValue sjme_jint tx,
	sjme_attrInValue sjme_jint ty,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInNotNull sjme_scritchui_pencilFont defaultFont,
	sjme_attrInNullable const sjme_frontEndBindable* copyFrontEnd);

/**
 * Returns whether the given pixel format is indexed with a palette. 
 * 
 * @param pf The pixel format to check.
 * @return If the pixel format is indexed.
 * @since 2025/12/06
 */
sjme_jboolean sjme_scritchpen_isIndexed(
	sjme_attrInValue sjme_gfx_pixelFormat pf);

/**
 * Allocates a new pencil instance.
 * 
 * @param inState The input ScritchUI state.
 * @param outPencil The resultant pencil.
 * @param inFunctions The functions to set.
 * @param inLockFuncs Functions for native locking.
 * @param inLockFrontEndCopy Front end copy data for locks.
 * @param pf The pixel format used.
 * @param tx Forced X translate.
 * @param ty Forced Y translate.
 * @param sw The surface width.
 * @param sh The surface height.
 * @param bw The buffer width, the scanline length.
 * @param defaultFont The default font to use.
 * @param copyFrontEnd Optional front end data to copy.
 * @return Any error code if applicable.
 * @since 2024/05/04
 */
sjme_errorCode sjme_scritchpen_new(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrInNotNull const sjme_scritchui_pencilImplFunctions* inFunctions,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEndBindable* inLockFrontEndCopy,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInValue sjme_jint tx,
	sjme_attrInValue sjme_jint ty,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInNotNull sjme_scritchui_pencilFont defaultFont,
	sjme_attrInNullable const sjme_frontEndBindable* copyFrontEnd);
	
/**
 * Creates a hardware reference bracket to the native hardware graphics.
 * 
 * @param inState The state this is under.
 * @param outPencil The resultant pencil.
 * @param outWeakPencil The output weak reference to the pencil.
 * @param pf The @link sjme_gfx_pixelFormat @endlink used for the draw.
 * @param bw The buffer width, this is the scanline width of the buffer.
 * @param bh The buffer height.
 * @param inLockFuncs The locking functions to use for buffer access.
 * @param inLockFrontEndCopy Front end copy data for locks.
 * @param tx Forced X translate.
 * @param ty Forced Y translate.
 * @param sx Starting surface X coordinate.
 * @param sy Starting surface Y coordinate.
 * @param sw Surface width.
 * @param sh Surface height.
 * @param defaultFont The default font to use.
 * @param copyFrontEnd The front-end copy.
 * @return The bracket capable of drawing hardware accelerated graphics.
 * @return An error if the requested graphics are not valid.
 * @since 2024/05/01
 */
sjme_errorCode sjme_scritchpen_newBuffer(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrOutNullable sjme_alloc_weak* outWeakPencil,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInPositiveNonZero sjme_jint bh,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEndBindable* inLockFrontEndCopy,
	sjme_attrInValue sjme_jint tx,
	sjme_attrInValue sjme_jint ty,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInNotNull sjme_scritchui_pencilFont defaultFont,
	sjme_attrInNullable const sjme_frontEndBindable* copyFrontEnd);
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCIL_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCIL_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCIL_H */
#endif     /* #ifdef __cplusplus */

#endif /* SJME_C_SCRITCHUIPENCIL_H */
