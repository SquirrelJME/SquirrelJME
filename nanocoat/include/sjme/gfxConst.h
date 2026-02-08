/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Graphics constants.
 * 
 * @file
 * @since 2024/04/06
 */

#ifndef SJME_C_GFXCONST_H
#define SJME_C_GFXCONST_H

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_GFXCONST_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The pixel format used for graphics, matches @c UIPixelFormat.
 * 
 * @since 2023/11/25
 */
typedef enum sjme_gfx_pixelFormat
{
	/** 32-bit RGBA (@c uint32_t ) [Java ME Standard]. */
	SJME_GFX_PIXEL_FORMAT_INT_ARGB8888 = 0,
	
	/** 32-bit RGB (@c uint32_t ) [Java ME Standard]. */
	SJME_GFX_PIXEL_FORMAT_INT_RGB888 = 1,
	
	/** 16-bit RGBA4444. (@c uint16_t ) [Java ME Standard]. */
	SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444 = 2,
	
	/** 16-bit RGB565. (@c uint16_t ) [Java ME Standard]. */
	SJME_GFX_PIXEL_FORMAT_SHORT_RGB565 = 3,
	
	/** 16-bit RGB555. (@c uint16_t ). */
	SJME_GFX_PIXEL_FORMAT_SHORT_RGB555 = 4,
	
	/** 16-bit ABGR1555. (@c uint16_t ) [PlayStation 2]. */
	SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555 = 5,
	
	/** 65536 Colors (@c uint16_t ). */
	SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536 = 6,
	
	/** 256 Colors (@c uint8_t ). */
	SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256 = 7,
	
	/** Packed 16 colors (4-bit). (packed @c uint8_t ) */
	SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4 = 8,
	
	/** Packed 4 Colors (2-bit). (packed @c uint8_t ) */
	SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2 = 9,
	
	/** Packed 2 colors (1-bit). (packed @c uint8_t ) */
	SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1 = 10,
	
	/** 32-bit BGRA ({@code int}). */
	SJME_GFX_PIXEL_FORMAT_INT_BGRA8888 = 11,
	
	/** 32-bit BGRX ({@code int}). */
	SJME_GFX_PIXEL_FORMAT_INT_BGRX8888 = 12,
	
	/** 32-bit BGR ({@code int}). */
	SJME_GFX_PIXEL_FORMAT_INT_BGR888 = 13,
	
	/** 32-bit RGBX ({@code int}). */
	SJME_GFX_PIXEL_FORMAT_INT_RGBX8888 = 14,
	
	/** 24-bit RGB consisting of three bytes. */
	SJME_GFX_PIXEL_FORMAT_BYTE3_RGB888 = 15,
	
	/** 24-bit BGR consisting of three bytes. */
	SJME_GFX_PIXEL_FORMAT_BYTE3_BGR888 = 16,
	
	/** 65536 Colors (@c uint16_t ), alpha enabled. */
	SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536A = 17,
	
	/** 256 Colors (@c uint8_t ), alpha enabled. */
	SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256A = 18,
	
	/** Packed 16 colors (4-bit), alpha enabled. (packed @c uint8_t ) */
	SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4A = 19,
	
	/** Packed 4 Colors (2-bit), alpha enabled. (packed @c uint8_t ) */
	SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2A = 20,
	
	/** Packed 2 colors (1-bit), alpha enabled. (packed @c uint8_t ) */
	SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1A = 21,
	
	/** 16-bit RGB444. (@c uint16_t ). */
	SJME_GFX_PIXEL_FORMAT_SHORT_RGB444 = 22,

	/** 16-bit ARGB1555. (@c uint16_t ) [Nokia]. */
	SJME_GFX_PIXEL_FORMAT_SHORT_ARGB1555 = 23,

	/** INDEXED1 but with vertical pixel disposition. (packed @c uint8_t ) */
	SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1_VERTICAL = 24,
	
	/** 8-bit RGB332 @c uint8_t . */
	SJME_GFX_PIXEL_FORMAT_BYTE_RGB332 = 25,
	
	/** Only 8-bit alpha (@c uint8_t ). */
	SJME_GFX_PIXEL_FORMAT_BYTE_A8 = 26,
	
	/** Only 4-bit alpha (packed @c uint8_t ). */
	SJME_GFX_PIXEL_FORMAT_PACKED_A4 = 27,
	
	/** Only 2-bit alpha (packed @c uint8_t ). */
	SJME_GFX_PIXEL_FORMAT_PACKED_A2 = 28,
	
	/** Only 1-bit alpha (packed @c uint8_t ). */
	SJME_GFX_PIXEL_FORMAT_PACKED_A1 = 29,
	
	/** Only 8-bit red (@c uint8_t ). */
	SJME_GFX_PIXEL_FORMAT_BYTE_R8 = 30,
	
	/** Only 8-bit green (@c uint8_t ). */
	SJME_GFX_PIXEL_FORMAT_BYTE_G8 = 31,
	
	/** Only 8-bit blue (@c uint8_t ). */
	SJME_GFX_PIXEL_FORMAT_BYTE_B8 = 32,

	/** The number of pixel formats. */
	SJME_NUM_GFX_PIXEL_FORMATS = 33,
} sjme_gfx_pixelFormat;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_GFXCONST_H
}
		#undef SJME_CXX_SQUIRRELJME_GFXCONST_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_GFXCONST_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_GFXCONST_H */
