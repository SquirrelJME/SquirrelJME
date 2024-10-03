/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/modelessStars.h"

sjme_errorCode sjme_modelessStars(
	sjme_attrInOutNotNull sjme_modelessStarState* state,
	sjme_attrInNotNull uint32_t* buf,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height,
	sjme_attrInPositiveNonZero sjme_jint pitch,
	sjme_attrInValue sjme_jint tick)
{
	sjme_modelessStar* modelessStar;
	uint32_t* bufSlice;
	sjme_modelessStarColor colors[SJME_NUM_MODELESS_STAR_COLOR_IDS];
	sjme_modelessStarColor* startColor;
	sjme_modelessStarColor* endColor;
	sjme_modelessStarColor* atColor;
	sjme_modelessStarColor* sliceColor;
	sjme_jint h, y, x, next;
	sjme_juint rawHex;
	
	/* Make sure input values are fine. */
	if (state == NULL || buf == NULL || width <= 0 || height <= 0 ||
		pitch <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Easier pointers. */
	startColor = &colors[SJME_MODELESS_STAR_COLOR_ID_START];
	endColor = &colors[SJME_MODELESS_STAR_COLOR_ID_END];
	atColor = &colors[SJME_MODELESS_STAR_COLOR_ID_AT];
	sliceColor = &colors[SJME_MODELESS_STAR_COLOR_ID_SLICE];
	
	/* Set base color information. */
	startColor->xrgb[0] = 0x00 << SJME_MODELESS_STAR_COLOR_SHIFT;
	startColor->xrgb[1] = 0x5B << SJME_MODELESS_STAR_COLOR_SHIFT;
	startColor->xrgb[2] = 0xCE << SJME_MODELESS_STAR_COLOR_SHIFT;
	startColor->xrgb[3] = 0xFA << SJME_MODELESS_STAR_COLOR_SHIFT;
	endColor->xrgb[0] = 0x00 << SJME_MODELESS_STAR_COLOR_SHIFT;
	endColor->xrgb[1] = 0xF5 << SJME_MODELESS_STAR_COLOR_SHIFT;
	endColor->xrgb[2] = 0xA9 << SJME_MODELESS_STAR_COLOR_SHIFT;
	endColor->xrgb[3] = 0xB8 << SJME_MODELESS_STAR_COLOR_SHIFT;
	
	/* Set middle color. */
	for (x = 0; x < 4; x++)
	{
		/* Set starting color. */
		atColor->xrgb[x] =
			startColor->xrgb[x];
		
		/* Set slice color. */
		if (endColor->xrgb[x] != startColor->xrgb[x])
			sliceColor->xrgb[x] = (endColor->xrgb[x] -
									  startColor->xrgb[x]) / height;
		else
			sliceColor->xrgb[x] = 0;
	}
	
	/* Draw in each color slice. */
	for (h = 0; h < height; h++)
	{
		/* Determine where to draw in the slice. */
		bufSlice = &buf[pitch * h];
		
		/* Determine the actual color hex. */
		rawHex = 0;
		for (x = 0; x < 4; x++)
		{
			rawHex <<= 8;
			rawHex |= (atColor->xrgb[x] >>
				SJME_MODELESS_STAR_COLOR_SHIFT) & 0xFF;
		}
		
		/* Just set it all to that color. */
		for (x = 0; x < width; x++)
			bufSlice[x] = rawHex;
		
		/* Shift color closer. */
		for (x = 0; x < 4; x++)
		{
			/* If already at this color, do not go there. */
			next = atColor->xrgb[x];
			if (next == endColor->xrgb[x])
				continue;
			
			/* Determine next color. */
			next += sliceColor->xrgb[x];
			
			/* Make sure color stays in bounds. */
			if (next > 0xFF0000)
				next = 0xFF0000;
			else if (next < 0x000000)
				next = 0x000000;
			
			/* Go to this color. */
			atColor->xrgb[x] = next;
		}
	}
	
	/* Draw random star positions. */
	for (h = 0; h < SJME_MODELESS_STAR_COUNT; h++)
	{
		/* Get this star. */
		modelessStar = &state->modelessStars[h];
		
		/* Does this star need to be initialized? */
		if (modelessStar->shining == SJME_JNI_FALSE)
		{
			/* Not creating stars yet, and is not shining. */
			if (state->lockStarCreation > 0)
			{
				/* Count down though... */
				state->lockStarCreation--;
				
				continue;
			}
			
			/* Star is shining now. */
			modelessStar->shining = SJME_JNI_TRUE;
			
			/* Set random position on the screen. */
			if (state->latchedFirstGo)
				modelessStar->x = width - 1;
			else
				modelessStar->x = rand() % width;
			modelessStar->y = rand() % height;
			
			/* Set a random speed for the star. */
			modelessStar->speed = (rand() % 4) + 1;
			
			/* Do not initialize another star just yet. */
			state->lockStarCreation = (tick % 25) + (rand() % 50);
			state->lockStarCreationLast = state->lockStarCreation;
		}
		
		/* Move star to the left. */
		modelessStar->x -= modelessStar->speed;
		
		/* Did the star fall off the screen? */
		if (modelessStar->x < 0)
		{
			modelessStar->shining = SJME_JNI_FALSE;
			
			/* Do not draw this star. */
			continue;
		}
		
		/* Draw star. */
		x = modelessStar->x;
		y = modelessStar->y;
		buf[(pitch * y) + x] = 0x00FFFFFF;
		
		/* Do this step again to tick some more? */
		if (!state->latchedFirstGo)
		{
			/* Reset counter. */
			h = -1;
			
			/* No longer the first go. */
			state->latchedFirstGo = SJME_JNI_TRUE;
		}
	}
	
	/* Handled successfully. */
	return SJME_JNI_TRUE;
}
