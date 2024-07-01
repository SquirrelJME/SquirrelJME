/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/debug.h"

/* 32-bit RGBA (@c uint32_t ) [Java ME Standard]. */
#define SJME_PENCIL_NAME argb8888
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_INT_ARGB8888
#define pencilPixelType sjme_jint
#define pencilPixelBits 32
#define pencilPixelMask 0xFFFFFFFF

#include "scritchPencilTemplate.c"

/* 32-bit RGB (@c uint32_t ) [Java ME Standard]. */
#define SJME_PENCIL_NAME rgb888
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_INT_RGB888
#define pencilPixelType sjme_jint
#define pencilPixelBits 32
#define pencilPixelMask 0xFFFFFFFF

#include "scritchPencilTemplate.c"

/* 16-bit RGBA4444. (@c uint16_t ) [Java ME Standard]. */
#define SJME_PENCIL_NAME argb4444
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444
#define pencilPixelType sjme_jshort
#define pencilPixelBits 16
#define pencilPixelMask 0xFFFF

#include "scritchPencilTemplate.c"

/* 16-bit RGB565. (@c uint16_t ) [Java ME Standard]. */
#define SJME_PENCIL_NAME rgb565
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_SHORT_RGB565
#define pencilPixelType sjme_jshort
#define pencilPixelBits 16
#define pencilPixelMask 0xFFFF

#include "scritchPencilTemplate.c"

/* 16-bit RGB555. (@c uint16_t ). */
#define SJME_PENCIL_NAME rgb555
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_SHORT_RGB555
#define pencilPixelType sjme_jshort
#define pencilPixelBits 16
#define pencilPixelMask 0xFFFF

#include "scritchPencilTemplate.c"

/* 16-bit ABGR1555. (@c uint16_t ) [PlayStation 2]. */
#define SJME_PENCIL_NAME abgr1555
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555
#define pencilPixelType sjme_jshort
#define pencilPixelBits 16
#define pencilPixelMask 0xFFFF

#include "scritchPencilTemplate.c"

/* 65536 Colors (@c uint16_t ). */
#define SJME_PENCIL_NAME indexed65536
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536
#define pencilPixelType sjme_jshort
#define pencilPixelBits 8
#define pencilPixelMask 0xFFFF

#include "scritchPencilTemplate.c"

/* 256 Colors (@c uint8_t ). */
#define SJME_PENCIL_NAME indexed256
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256
#define pencilPixelType sjme_jbyte
#define pencilPixelBits 8
#define pencilPixelMask 0xFF

#include "scritchPencilTemplate.c"

/* Packed 16 colors (4-bit). (packed @c uint8_t ) */
#define SJME_PENCIL_NAME indexed4
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4
#define pencilPixelType sjme_jbyte
#define pencilPixelBits 4
#define pencilPixelMask 0xF

#include "scritchPencilTemplate.c"

/* Packed 4 Colors (2-bit). (packed @c uint8_t ) */
#define SJME_PENCIL_NAME indexed2
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2
#define pencilPixelType sjme_jbyte
#define pencilPixelBits 2
#define pencilPixelMask 0x3

#include "scritchPencilTemplate.c"

/* Packed 2 colors (1-bit). (packed @c uint8_t ) */
#define SJME_PENCIL_NAME indexed1
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1
#define pencilPixelType sjme_jbyte
#define pencilPixelBits 1
#define pencilPixelMask 0x1

#include "scritchPencilTemplate.c"

sjme_errorCode sjme_scritchui_pencilInitBufferStatic(
	sjme_attrInOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositive sjme_jint bw,
	sjme_attrInPositive sjme_jint bh,
	sjme_attrInNotNull void* buf,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInNullable const sjme_jint* pal,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositive sjme_jint sw,
	sjme_attrInPositive sjme_jint sh)
{
	const sjme_scritchui_pencilImplFunctions* chosen;
	
	if (outPencil == NULL || buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bw <= 0 || bh <= 0 || sx < 0 || sy < 0 || sw <= 0 || sh <= 0 ||
		sx >= sw || sy >= sh || sx >= bw || sy >= bh ||
		sw > bw || sh > bh || (sx + sw) > bw || (sy + sh) > bh ||
		(sx + sw) < 0 || (sy + sh) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (offset < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Which drawing operations to use? */
	switch (pf)
	{
		case SJME_GFX_PIXEL_FORMAT_INT_ARGB8888:
			chosen = &sjme_scritchui_pencil___argb8888;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_INT_RGB888:
			chosen = &sjme_scritchui_pencil___rgb888;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444:
			chosen = &sjme_scritchui_pencil___argb4444;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB565:
			chosen = &sjme_scritchui_pencil___rgb565;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB555:
			chosen = &sjme_scritchui_pencil___rgb555;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555:
			chosen = &sjme_scritchui_pencil___abgr1555;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536:
			chosen = &sjme_scritchui_pencil___indexed65536;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256:
			chosen = &sjme_scritchui_pencil___indexed256;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4:
			chosen = &sjme_scritchui_pencil___indexed4;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2:
			chosen = &sjme_scritchui_pencil___indexed2;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1:
			chosen = &sjme_scritchui_pencil___indexed1;
			break;
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
