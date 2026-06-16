/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Basic ScritchUI types.
 * 
 * @file
 * @since 2026/01/21
 */

#ifndef SJME_C_SQUIRRELJME_SCRITCHUIBASIC_H
#define SJME_C_SQUIRRELJME_SCRITCHUIBASIC_H

#include "sjme/stdTypes.h"
#include "sjme/frontEnd.h"
#include "lib/scritchui/scritchuiConst.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SCRITCHUIBASIC_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#pragma region(scritchui_basicTypes)

/**
 * ScritchUI state.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_stateBase* sjme_scritchui;
	
/**
 * An opaque native handle.
 * 
 * @since 2024/04/02
 */
typedef sjme_pointer sjme_scritchui_handle;

/**
 * ScritchUI Pencil.
 * 
 * @since 2026/01/21
 */
typedef struct sjme_scritchui_pencilBase sjme_scritchui_pencilBase;

/**
 * Represents a point.
 * 
 * @since 2024/07/12
 */
typedef struct sjme_scritchui_point
{
	/** X coordinate. */
	sjme_jint x;
	
	/** Y coordinate. */
	sjme_jint y;
} sjme_scritchui_point;

/**
 * Represents a line.
 * 
 * @since 2024/07/12
 */
typedef struct sjme_scritchui_line
{
	/** Starting point. */
	sjme_scritchui_point s;
	
	/** End point. */
	sjme_scritchui_point e;
} sjme_scritchui_line;

/**
 * Represents a dimension.
 * 
 * @since 2024/07/12
 */
typedef struct sjme_scritchui_dim
{
	/** The width. */
	sjme_jint width;
	
	/** The height. */
	sjme_jint height;
} sjme_scritchui_dim;

/**
 * Represents a rectangle.
 * 
 * @since 2024/04/26
 */
typedef struct sjme_scritchui_rect
{
	/** The starting point of the rectangle. */
	sjme_scritchui_point s;
	
	/** The dimension of the rect. */
	sjme_scritchui_dim d;
} sjme_scritchui_rect;
	
/**
 * Pencil drawing sub-translation matrix.
 * 
 * @since 2024/07/09
 */
typedef struct sjme_scritchui_matrixSub
{
	/** Step for source X coordinate. */
	sjme_fixed wx;
	
	/** Step for source Y coordinate. */
	sjme_fixed zy;
} sjme_scritchui_matrixSub;

/**
 * Pencil drawing matrix, for any translations, rotations, and mirroring.
 * 
 * @since 2024/07/09
 */
typedef struct sjme_scritchui_matrix
{
	/** Translation for input X coordinates. */
	sjme_scritchui_matrixSub x;
	
	/** Translation for input Y coordinates. */
	sjme_scritchui_matrixSub y;
	
	/** Target width after transformations. */
	sjme_jint tw;
	
	/** Target width after transformations. */
	sjme_jint th;
} sjme_scritchui_matrix;

/**
 * Represents the color of a pixel.
 * 
 * @since 2024/07/09
 */
typedef struct sjme_scritchui_color
{
	/** The raw pencil color, which is placed in the buffer. */
	sjme_jint v;
	
	/** The RGBA color. */
	sjme_jint argb;
	
	/** Red. */
	sjme_jubyte r;
	
	/** Green. */
	sjme_jubyte g;
	
	/** Blue. */
	sjme_jubyte b;
	
	/** Alpha. */
	sjme_jubyte a;
	
	/** Indexed color. */
	sjme_jchar i;
} sjme_scritchui_color;
	
struct sjme_scritchui_uiCommonBase
{
	/** The type of what this is. */
	sjme_scritchui_uiType type;
	
	/** Magic number for ScritchUI objects. */
	sjme_jint magic;
	
	/** The state which owns this. */
	sjme_scritchui state;
	
	/**
	 * Front-end data for this, note that ScritchUI implementations must not
	 * use this for information as this is only to be used by front-ends.
	 *
	 * Bindings may be used as needed.
	 */
	sjme_frontEndBindable frontEnd;
	
	/** Opaque native handles for this, as needed. */
	sjme_scritchui_handle handle[SJME_SCRITCHUI_NUM_COMMON_HANDLES];
	
	/** Other value storage, as needed. */
	sjme_jint intVals[SJME_SCRITCHUI_NUM_COMMON_VALUES];
};

/**
 * Adjustable parameters for pencil font rendering.
 *
 * This reduces the need to have multiples of pseudo fonts for different sizes
 * and styles, and additionally keeps everything down to a smaller set of
 * primary fonts.
 *
 * @since 2026/01/20
 */
typedef struct sjme_scritchui_pencilFontParam
{
	/** The style of font to render. */
	sjme_scritchui_pencilFontStyle style;

	/** The pixel size to render at. */
	sjme_jint pixelSize;
} sjme_scritchui_pencilFontParam;
	
/**
 * Contains the identifying information for a font.
 * 
 * @since 2026/01/19
 */
typedef struct sjme_scritchui_pencilFontId
{
	/** The name of the font. */
	sjme_cchar name[SJME_MAX_FONT_NAME];

	/** The face of the font. */
	sjme_scritchui_pencilFontFace face;

	/** The natural intrinsic parameters of the font. */
	sjme_scritchui_pencilFontParam param;
} sjme_scritchui_pencilFontId;
	
#pragma endregion(scritchui_basicTypes)
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIBASIC_H
}
#undef SJME_CXX_SQUIRRELJME_SCRITCHUIBASIC_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIBASIC_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SCRITCHUIBASIC_H */
