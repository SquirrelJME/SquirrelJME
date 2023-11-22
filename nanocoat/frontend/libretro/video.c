/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm.h"
#include "sjme/debug.h"
#include "3rdparty/libretro/libretro.h"
#include "frontend/libretro/shared.h"

/** Modeless color info. */
typedef struct sjme_modelessColor
{
	/** XRGB. */
	jint xrgb[4];
} sjme_modelessColor;

/**
 * Keeps track of modeless stars.
 * 
 * @since 2023/11/22
 */
typedef struct sjme_modelessStar
{
	/** Is this star shining? */
	jboolean shining;
	
	/** The X coordinate. */
	jint x;
	
	/** The Y coordinate. */
	jint y;
	
	/** The speed of the star. */
	jint speed;
} sjme_modelessStar;

/** Color shift. */
#define SJME_MODELESS_COLOR_SHIFT 16

/** Number of modeless stars. */
#define SJME_MODELESS_NUM_STARS 128

/** Modeless color options. */
typedef enum sjme_modelessColorId
{
	/** Starting color. */
	SJME_MODELESS_COLOR_ID_START,
	
	/** Ending color. */
	SJME_MODELESS_COLOR_ID_END,
	
	/** Current color at. */
	SJME_MODELESS_COLOR_ID_AT,
	
	/** The slice addition. */
	SJME_MODELESS_COLOR_ID_SLICE,
	
	/** The number of color IDs. */
	SJME_NUM_MODELESS_COLOR_IDS
} sjme_modelessColorId;

sjme_attrUnused RETRO_API void retro_get_system_av_info(
	struct retro_system_av_info* info)
{
	/* Base size. */
	info->geometry.base_width = 240;
	info->geometry.base_height = 320;
	
	/* Maximum permitted size. */
	info->geometry.max_width = 240;
	info->geometry.max_height = 320;
	
	/* Always square pixels. */
	info->geometry.aspect_ratio = 1.0F;
	
	/* Always target 60 FPS. */
	info->timing.fps = 60;
	
	/* Use a standard audio format here. */
	info->timing.sample_rate = 22050;
}

sjme_attrUnused RETRO_API void retro_set_video_refresh(
	retro_video_refresh_t refresh)
{
	sjme_libretro_videoRefreshCallback = refresh;
}

void sjme_libretro_modelessStars(
	sjme_attrInNotNull uint32_t* buf,
	sjme_attrInPositiveNonZero jint width,
	sjme_attrInPositiveNonZero jint height,
	sjme_attrInPositiveNonZero jint pitch,
	sjme_attrInValue jint tick)
{
	static sjme_modelessStar modelessStars[SJME_MODELESS_NUM_STARS];
	static jint lockStarCreation;
	static jint lockStarCreationLast;
	static jboolean latchedFirstGo;
	sjme_modelessStar* modelessStar;
	uint32_t* bufSlice;
	sjme_modelessColor colors[SJME_NUM_MODELESS_COLOR_IDS];
	sjme_modelessColor* startColor;
	sjme_modelessColor* endColor;
	sjme_modelessColor* atColor;
	sjme_modelessColor* sliceColor;
	jint h, y, x, next;
	juint rawHex;
	
	/* Easier pointers. */
	startColor = &colors[SJME_MODELESS_COLOR_ID_START];
	endColor = &colors[SJME_MODELESS_COLOR_ID_END];
	atColor = &colors[SJME_MODELESS_COLOR_ID_AT];
	sliceColor = &colors[SJME_MODELESS_COLOR_ID_SLICE];
	
	/* Set base color information. */
	startColor->xrgb[0] = 0x00 << SJME_MODELESS_COLOR_SHIFT;
	startColor->xrgb[1] = 0x5B << SJME_MODELESS_COLOR_SHIFT;
	startColor->xrgb[2] = 0xCE << SJME_MODELESS_COLOR_SHIFT;
	startColor->xrgb[3] = 0xFA << SJME_MODELESS_COLOR_SHIFT;
	endColor->xrgb[0] = 0x00 << SJME_MODELESS_COLOR_SHIFT;
	endColor->xrgb[1] = 0xF5 << SJME_MODELESS_COLOR_SHIFT;
	endColor->xrgb[2] = 0xA9 << SJME_MODELESS_COLOR_SHIFT;
	endColor->xrgb[3] = 0xB8 << SJME_MODELESS_COLOR_SHIFT;
	
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
			rawHex |= (atColor->xrgb[x] >> SJME_MODELESS_COLOR_SHIFT) & 0xFF;
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
	for (h = 0; h < SJME_MODELESS_NUM_STARS; h++)
	{
		/* Get this star. */
		modelessStar = &modelessStars[h];
		
		/* Does this star need to be initialized? */
		if (modelessStar->shining == JNI_FALSE)
		{
			/* Not creating stars yet, and is not shining. */
			if (lockStarCreation > 0)
			{
				/* Count down though... */
				lockStarCreation--;
				
				continue;
			}
			
			/* Star is shining now. */
			modelessStar->shining = JNI_TRUE;
			
			/* Set random position on the screen. */
			if (latchedFirstGo)
				modelessStar->x = width - 1;
			else
				modelessStar->x = rand() % width;
			modelessStar->y = rand() % height;
			
			/* Set a random speed for the star. */
			modelessStar->speed = (rand() % 4) + 1;
			
			/* Do not initialize another star just yet. */
			lockStarCreation = (tick % 25) + (rand() % 50);
			lockStarCreationLast = lockStarCreation;
		}
		
		/* Move star to the left. */
		modelessStar->x -= modelessStar->speed;
		
		/* Did the star fall of the screen? */
		if (modelessStar->x < 0)
		{
			modelessStar->shining = false;
			
			/* Do not draw this star. */
			continue;
		}
		
		/* Draw star. */
		x = modelessStar->x;
		y = modelessStar->y;
		buf[(pitch * y) + x] = 0x00FFFFFF;
		
		/* Do this step again to tick some more? */
		if (!latchedFirstGo)
		{
			/* Reset counter. */
			h = -1;
			
			/* No longer the first go. */
			latchedFirstGo = JNI_TRUE;
		}
	}
}
