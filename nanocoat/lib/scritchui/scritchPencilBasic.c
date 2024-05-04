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
#include "sjme/debug.h"

#define pencilPutPixelI_jint(pColor, pIndex)
#define pencilPutPixelI_jshort(pColor, pIndex)
#define pencilPutPixelI_jbyte(pColor, pIndex)

#define pencilPutPixelXY_jint(pColor, pX, pY)
#define pencilPutPixelXY_jshort(pColor, pX, pY)
#define pencilPutPixelXY_jbyte(pColor, pX, pY)

/* 32-bit RGBA (@c uint32_t ) [Java ME Standard]. */
#define SJME_PENCIL_NAME rgba8888
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_INT_RGBA8888
#define pencilPixelType sjme_jint

#include "scritchPencilTemplate.c"

/* 32-bit RGB (@c uint32_t ) [Java ME Standard]. */
#define SJME_PENCIL_NAME rgb888
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_INT_RGB888
#define pencilPixelType sjme_jint

#include "scritchPencilTemplate.c"

/* 16-bit RGBA4444. (@c uint16_t ) [Java ME Standard]. */
#define SJME_PENCIL_NAME rgba4444
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_SHORT_RGBA4444
#define pencilPixelType sjme_jshort

#include "scritchPencilTemplate.c"

/* 16-bit RGB565. (@c uint16_t ) [Java ME Standard]. */
#define SJME_PENCIL_NAME rgb565
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_SHORT_RGB565
#define pencilPixelType sjme_jshort

#include "scritchPencilTemplate.c"

/* 16-bit RGB555. (@c uint16_t ). */
#define SJME_PENCIL_NAME rgb555
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_SHORT_RGB555
#define pencilPixelType sjme_jshort

#include "scritchPencilTemplate.c"

/* 16-bit ABGR1555. (@c uint16_t ) [PlayStation 2]. */
#define SJME_PENCIL_NAME abgr1555
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555
#define pencilPixelType sjme_jshort

#include "scritchPencilTemplate.c"

/* 65536 Colors (@c uint16_t ). */
#define SJME_PENCIL_NAME indexed65536
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536
#define pencilPixelType sjme_jshort

#include "scritchPencilTemplate.c"

/* 256 Colors (@c uint8_t ). */
#define SJME_PENCIL_NAME indexed256
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256
#define pencilPixelType sjme_jbyte

#include "scritchPencilTemplate.c"

/* Packed 16 colors (4-bit). (packed @c uint8_t ) */
#define SJME_PENCIL_NAME indexed4
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4
#define pencilPixelType sjme_jbyte

#include "scritchPencilTemplate.c"

/* Packed 4 Colors (2-bit). (packed @c uint8_t ) */
#define SJME_PENCIL_NAME indexed2
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2
#define pencilPixelType sjme_jbyte

#include "scritchPencilTemplate.c"

/* Packed 2 colors (1-bit). (packed @c uint8_t ) */
#define SJME_PENCIL_NAME indexed1
#define SJME_PENCIL_PIXEL_FORMAT SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1
#define pencilPixelType sjme_jbyte

#include "scritchPencilTemplate.c"

sjme_errorCode sjme_scritchui_pencilInitBuffer(
	sjme_attrInOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositive sjme_jint bw,
	sjme_attrInPositive sjme_jint bh,
	sjme_attrInNotNull void* buf,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInNullable sjme_jint pal,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositive sjme_jint sw,
	sjme_attrInPositive sjme_jint sh)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
