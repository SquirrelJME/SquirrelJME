/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Environment setup.
 * 
 * @since 2022/01/02
 */

#ifndef SQUIRRELJME_LRENV_H
#define SQUIRRELJME_LRENV_H

#include "sjmerc.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_LRENV_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The frame rate of the core. */
#define SJME_LIBRETRO_FRAME_RATE 60

/** Threading model key. */
#define SJME_LIBRETRO_CONFIG_THREAD_MODEL "squirreljme_thread_model"

/** Cooperative threading model. */
#define SJME_LIBRETRO_CONFIG_THREAD_MODEL_COOP "cooperative"

/** Simultaneous threading model. */
#define SJME_LIBRETRO_CONFIG_THREAD_MODEL_SMT "simultaneous"

/** CPU Cycles key. */
#define SJME_LIBRETRO_CONFIG_COOP_CYCLES "squirreljme_coop_cycles"

/** Default CPU cycles. */
#define SJME_LIBRETRO_CONFIG_DEFAULT_CYCLES \
	SJME_STRINGIFY(SJME_LIBRETRO_FRAME_RATE)

/** ROM Order Key. */
#define SJME_LIBRETRO_CONFIG_ROM_ORDER "squirreljme_rom_order"

/** External/Internal ROM order. */
#define SJME_LIBRETRO_CONFIG_ROM_ORDER_EXT_INT "external+internal"

/** Internal/External ROM order. */
#define SJME_LIBRETRO_CONFIG_ROM_ORDER_INT_EXT "internal+external"

/** External ROM order. */
#define SJME_LIBRETRO_CONFIG_ROM_ORDER_EXT "external"

/** Internal ROM order. */
#define SJME_LIBRETRO_CONFIG_ROM_ORDER_INT "internal"

/** Display size configuration key. */
#define SJME_LIBRETRO_CONFIG_DISPLAY_SIZE "squirreljme_display_size"

/** Pixel format configuration key. */
#define SJME_LIBRETRO_CONFIG_PIXEL_FORMAT "squirreljme_pixel_format"

/** Pixel format 24-bit RGBA8888. */
#define SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_RGBA8888 "24-bit"

/** Pixel format 16-bit RGBA4444. */
#define SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_RGBA4444 "16-bit (RGB444)"

/** Pixel format 16-bit RGB565. */
#define SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_RGB565 "16-bit (RGB565)"

/** Pixel format 15-bit RGB555. */
#define SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_RGB555 "15-bit (RGB555)"

/** Pixel format 16-bit ABGR1555 (PlayStation 2). */
#define SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_ABGR1555 "15-bit (BGR555)"

/** Pixel format indexed 16-bit color. */
#define SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_65536I "65536 Indexed Colors"

/** Pixel format indexed 8-bit color. */
#define SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_256I "256 Indexed Colors"

/** Pixel format indexed 4-bit color. */
#define SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_4I "16 Indexed Colors"

/** Pixel format indexed 2-bit color. */
#define SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_2I "4 Indexed Colors"

/** Pixel format indexed 1-bit color. */
#define SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_1I "Monochrome"

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_LRENV_H
}
#undef SJME_CXX_SQUIRRELJME_LRENV_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_LRENV_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_LRENV_H */
