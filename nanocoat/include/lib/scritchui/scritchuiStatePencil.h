/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI Pencil State.
 * 
 * @file
 * @since 2026/01/21
 */

#ifndef SJME_C_SQUIRRELJME_SCRITCHUISTATEPENCIL_H
#define SJME_C_SQUIRRELJME_SCRITCHUISTATEPENCIL_H

#include "lib/scritchui/scritchuiBasic.h"
#include "lib/scritchui/scritchuiApiStruct.h"
#include "lib/scritchui/scritchuiApiStructImpl.h"
#include "lib/scritchui/scritchuiTypeDefs.h"
#include "lib/scritchui/scritchuiTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SCRITCHUISTATEPENCIL_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#pragma region(scritchui_font)
	
struct sjme_scritchui_pencilFontBase
{
	/** Common data. */
	sjme_scritchui_uiCommonBase common;
	
	/** The ID of the font. */
	sjme_scritchui_pencilFontId id;
	
	/**
	 * The priority of the font, this determines whether it is used as
	 * a main font or as a source of backup glyphs.
	 */
	sjme_jint priority;
	
	/** Internal handle pointer for implementation needs. */
	sjme_pointer handle;
	
	/** External API. */
	const sjme_scritchui_pencilFontFunctions* api;

	/** External API, in UI thread. */
	const sjme_scritchui_pencilFontFunctions* apiInThread;
	
	/** Internal implementation. */
	const sjme_scritchui_pencilFontImplFunctions* impl;
	
	/** Font cache details. */
	struct
	{
		/** The baseline of the font. */
		sjme_jint baseline;
		
		/** The leading of the font. */
		sjme_jint leading;
		
		/** The ascent of the font. */
		sjme_jint ascent[2];
		
		/** The descent of the font. */
		sjme_jint descent[2];
	} cache;
};
	
#pragma endregion(scritchui_font)
#pragma region(scritchui_pencil)
	
/**
 * The state of the pencil lock.
 * 
 * @since 2024/07/08
 */
typedef struct sjme_scritchui_pencilLockState
{
	/** Spin lock for access to the buffer. */
	sjme_alignPointer sjme_thread_spinLock spinLock;
	
	/** The times this was opened. */
	sjme_alignPointer sjme_atomic(sjme_jint) count;
	
	/** The front end source for drawing. */
	sjme_frontEndBindable source;
	
	/** The base address where drawing should occur. */
	sjme_pointer base;
	
	/** The buffer limit of the base, in bytes. */
	sjme_jint baseLimitBytes;
	
	/** Is this a copy? */
	sjme_jboolean isCopy;
} sjme_scritchui_pencilLockState;

typedef struct sjme_scritchui_pencilBase
{
	/** Common data. */
	sjme_scritchui_uiCommonBase common;
	
	/** The current state of the pencil. */
	sjme_scritchui_pencilState state;
	
	/** External API. */
	const sjme_scritchui_pencilFunctions* api;
	
	/** External API, in thread of execution. */
	const sjme_scritchui_pencilFunctions* apiInThread;
	
	/** Implementation API. */
	const sjme_scritchui_pencilImplFunctions* impl;
	
	/** Utility functions. */
	const sjme_scritchui_pencilUtilFunctions* util;
	
	/** Optional locking functions, for buffer access as required. */
	const sjme_scritchui_pencilLockFunctions* lock;
	
	/** The lock state. */
	sjme_scritchui_pencilLockState lockState;
	
	/** Lowest level primitive pencil functions. */
	sjme_scritchui_pencilPrimFunctions prim;
	
	/** Front end information for paint. */
	sjme_frontEndBindable frontEnd;
	
	/** The pixel format used. */
	sjme_gfx_pixelFormat pixelFormat;
	
	/** Is there an alpha channel? */
	sjme_jboolean hasAlpha;
	
	/** The default font to use. */
	sjme_scritchui_pencilFont defaultFont;
	
	/** The width of the surface. */
	sjme_jint width;
	
	/** The height of the surface. */
	sjme_jint height;
	
	/** The scanline length, in pixels. */
	sjme_jint scanLenPixels;
	
	/** The scan line length, in bits. */
	sjme_jint scanLenBits;
	
	/** The scan line length, in bytes. */
	sjme_jint scanLenBytes;
	
	/** Bits per pixel. */
	sjme_jint bitsPerPixel;
	
	/** The bytes per pixel. */
	sjme_jint bytesPerPixel;
	
	/** Forced X/Y translate. */
	sjme_scritchui_point forceTranslate;
	
	/** Color palette. */
	struct
	{
		/** The colors available. */
		const sjme_jint* colors;
		
		/** The number of colors used. */
		sjme_jint numColors;
	} palette;
} sjme_scritchui_pencilBase;
	
#pragma endregion(scritchui_pencil)
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUISTATEPENCIL_H
}
#undef SJME_CXX_SQUIRRELJME_SCRITCHUISTATEPENCIL_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUISTATEPENCIL_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SCRITCHUISTATEPENCIL_H */
