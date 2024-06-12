/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * SQF Font Structure.
 * 
 * @since 2024/06/10
 */

#ifndef SQUIRRELJME_SCRITCHUIPENCILFONTSQF_H
#define SQUIRRELJME_SCRITCHUIPENCILFONTSQF_H

#include "sjme/nvm.h"
#include "sjme/alloc.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUIPENCILFONTSQF_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Flags for a specific character.
 * 
 * @since 2024/06/04
 */
typedef enum sjme_scritchui_sqfFlag
{
	/** Character is valid. */
	sjme_scritchui_sqfFlag_VALID = 1,
	
	/** Compressed with RaFoCES w/ huffman table. */
	sjme_scritchui_sqfFlag_RAFOCES = 2,
} sjme_scritchui_sqfFlag;

/**
 * SQF Font information.
 *
 * @since 2019/06/20
 */
typedef struct sjme_scritchui_sqf
{
	/** The name of the font. */
	sjme_lpcstr name;
	
	/** The pixel height of the font. */
	sjme_jint pixelHeight;
	
	/** The ascent of the font. */
	sjme_jint ascent;
	
	/** The descent of the font. */
	sjme_jint descent;
	
	/** Bounding box X offset. */
	sjme_jint bbx;
	
	/** Bounding box Y offset. */
	sjme_jint bby;
	
	/** Bounding box width. */
	sjme_jint bbw;
	
	/** Bounding box height. */
	sjme_jint bbh;
	
	/** The starting codepoint for this font. */
	sjme_jint codepointStart;
	
	/** The number of codepoints which are in this font. */
	sjme_jint codepointCount;
	
	/** The size of the @c huffBits member. */
	sjme_jint huffBitsSize;
	
	/** The size of the @c charBmp member. */
	sjme_jint charBmpSize;
	
	/** Huffman bits for huffman tables. */
	const sjme_jbyte* huffBits;
	
	/** Widths for each character. */
	const sjme_jbyte* charWidths;
	
	/** X offset for character. */
	const sjme_jbyte* charXOffset;
	
	/** Y offset for character. */
	const sjme_jbyte* charYOffset;
	
	/** SQF Font Flags, per character. */
	const sjme_jbyte* charFlags;
	
	/** Offset to the character bitmap for the given character. */
	const sjme_jshort* charBmpOffset;
	
	/** The bytes per scanline for each character. */
	const sjme_jbyte* charBmpScan;
	
	/** Which characters make up the bitmap? */
	const sjme_jbyte* charBmp;
} sjme_scritchui_sqf;

typedef struct sjme_scritchui_sqfCodepage
{
	/** The name of the font. */
	sjme_lpcstr name;
	
	/** The number of codepages in the font. */
	sjme_jint numCodepages;
	
	/** The codepages for the font. */
	const sjme_scritchui_sqf* const* codepages;
} sjme_scritchui_sqfCodepage;

/**
 * Initializes a new pencil font using the given SQF codepage set.
 * 
 * @param inOutFont The resultant font.
 * @param inSqfCodepage The input SQF codepage to use.
 * @return Any resultant error, if any.
 * @since 2024/06/10
 */
sjme_errorCode sjme_scritchui_newPencilFontSqfStatic(
	sjme_scritchui_pencilFont inOutFont,
	const sjme_scritchui_sqfCodepage* inSqfCodepage);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCILFONTSQF_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCILFONTSQF_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCILFONTSQF_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SCRITCHUIPENCILFONTSQF_H */
