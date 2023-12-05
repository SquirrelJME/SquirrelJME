/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Modeless Star Screen.
 * 
 * @since 2023/11/28
 */

#ifndef SQUIRRELJME_MODELESSSTARS_H
#define SQUIRRELJME_MODELESSSTARS_H

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_MODELESSSTARS_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Modeless color info. */
typedef struct sjme_modelessStarColor
{
	/** XRGB. */
	SJME_WRAPPED(jint) xrgb[4];
} sjme_modelessStarColor;

/**
 * Keeps track of modeless stars.
 * 
 * @since 2023/11/22
 */
typedef struct sjme_modelessStar
{
	/** Is this star shining? */
	SJME_WRAPPED(jboolean) shining;
	
	/** The X coordinate. */
	SJME_WRAPPED(jint) x;
	
	/** The Y coordinate. */
	SJME_WRAPPED(jint) y;
	
	/** The speed of the star. */
	SJME_WRAPPED(jint) speed;
} sjme_modelessStar;

/** Color shift. */
#define SJME_MODELESS_STAR_COLOR_SHIFT 16

/** Number of modeless stars. */
#define SJME_MODELESS_STAR_COUNT 128

/** Modeless color options. */
typedef enum sjme_modelessStarColorId
{
	/** Starting color. */
	SJME_MODELESS_STAR_COLOR_ID_START,
	
	/** Ending color. */
	SJME_MODELESS_STAR_COLOR_ID_END,
	
	/** Current color at. */
	SJME_MODELESS_STAR_COLOR_ID_AT,
	
	/** The slice addition. */
	SJME_MODELESS_STAR_COLOR_ID_SLICE,
	
	/** The number of color IDs. */
	SJME_NUM_MODELESS_STAR_COLOR_IDS
} sjme_modelessStarColorId;

/**
 * Contains the state of modeless stars.
 * 
 * @since 2023/11/28
 */
typedef struct sjme_modelessStarState
{
	/** The modeless stars. */
	sjme_modelessStar modelessStars[SJME_MODELESS_STAR_COUNT];
	
	/** Is star creation locked? */
	SJME_WRAPPED(jint) lockStarCreation;
	
	/** The last @c lockStarCreation value. */
	SJME_WRAPPED(jint) lockStarCreationLast;
	
	/** First go latched? */
	SJME_WRAPPED(jboolean) latchedFirstGo;
} sjme_modelessStarState;

/**
 * Draws modeless stars into the buffer.
 * 
 * @param state The modeless star state.
 * @param buf The buffer to draw into. 
 * @param width The width of the buffer.
 * @param height The height of the buffer.
 * @param pitch The pitch of the buffer.
 * @param tick The current tick.
 * @return Returns @c JNI_FALSE if there are issues with inputs.
 * @since 2023/11/22
 */
SJME_WRAPPED(jboolean) sjme_modelessStars(
	sjme_attrInOutNotNull sjme_modelessStarState* state,
	sjme_attrInNotNull uint32_t* buf,
	sjme_attrInPositiveNonZero SJME_WRAPPED(jint) width,
	sjme_attrInPositiveNonZero SJME_WRAPPED(jint) height,
	sjme_attrInPositiveNonZero SJME_WRAPPED(jint) pitch,
	sjme_attrInValue SJME_WRAPPED(jint) tick);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_MODELESSSTARS_H
}
		#undef SJME_CXX_SQUIRRELJME_MODELESSSTARS_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_MODELESSSTARS_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MODELESSSTARS_H */
