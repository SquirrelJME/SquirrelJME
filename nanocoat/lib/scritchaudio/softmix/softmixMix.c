/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchaudio/softmix/softmixIntern.h"
#include "sjme/fixed.h"

#define SJME_SOFTMIX(xFrom, xTo, xFromType, xToType, xFromLet, xToLet, \
	xFunc) \
	static sjme_attrOptimize sjme_errorCode \
		sjme_scritchaudio_softmix_ ## xFrom ## _to_ ## xTo( \
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* sourceInfo, \
	sjme_attrInNotNull const sjme_scritchaudio_buffer* sourceBuf, \
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* destInfo, \
	sjme_attrInNotNull sjme_scritchaudio_buffer* destBuf) \
	{ \
		const xFromType* s; \
		xToType* d; \
		sjme_fixed sI, dI, sAdd, dAdd, sT, dT; \
		xFromType a; \
		xToType t, v; \
		 \
		if (sourceInfo == NULL || sourceBuf == NULL || \
			destInfo == NULL || destBuf == NULL) \
			return SJME_ERROR_NULL_ARGUMENTS; \
		 \
		/* Get pointers to relate from. */ \
		s = (xFromType*)sourceBuf->xFromLet; \
		d = (xToType*)destBuf->xToLet; \
		 \
		/* Use the lower set of samples. */ \
		sT = sjme_fixed_hi(sourceInfo->totalSamples); \
		dT = sjme_fixed_hi(destInfo->totalSamples); \
		 \
		/* Copy every sample. */ \
		sAdd = sourceInfo->fromIncr; \
		dAdd = sourceInfo->toIncr; \
		for (sI = 0, dI = 0; dI < dT; sI += sAdd, dI += dAdd) \
		{ \
			/* Read in values to mix. */ \
			a = s[sI >> SJME_FIXED_SHIFT];\
			t = (xToType)(xFunc); \
			v = d[dI >> SJME_FIXED_SHIFT]; \
			 \
			/* This would clip. */ \
			d[dI >> SJME_FIXED_SHIFT] = (v + t); \
		} \
		 \
		/* Success! */ \
		return SJME_ERROR_NONE; \
	}

SJME_SOFTMIX(u8, u8, sjme_jubyte, sjme_jubyte, u, u, a)
SJME_SOFTMIX(u8, s16, sjme_jubyte, sjme_jshort, u, s,
	(sjme_jshort)(((sjme_jbyte)(a - 128))) * 256)
SJME_SOFTMIX(u8, s32, sjme_jubyte, sjme_jint, u, i,
	(sjme_jshort)(((sjme_jbyte)(a - 128))) * 16777216)

SJME_SOFTMIX(s16, u8, sjme_jshort, sjme_jubyte, s, u, 
	((sjme_jubyte)(a / 256)) + 128)
SJME_SOFTMIX(s16, s16, sjme_jshort, sjme_jshort, s, s, a)
SJME_SOFTMIX(s16, s32, sjme_jshort, sjme_jint, s, i, a * 65536)

SJME_SOFTMIX(s32, u8, sjme_jint, sjme_jubyte, i, u, 
	((sjme_jubyte)(a / 16777216)) + 128)
SJME_SOFTMIX(s32, s16, sjme_jint, sjme_jshort, i, s, a / 65536)
SJME_SOFTMIX(s32, s32, sjme_jint, sjme_jint, i, i, a)

#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
SJME_SOFTMIX(u8, f32, sjme_jubyte, float, u, f, a / 128.0F)
SJME_SOFTMIX(s16, f32, sjme_jshort, float, s, f, a / 32768.0F)
SJME_SOFTMIX(s32, f32, sjme_jint, float, i, f, a / 2147483648.0F)

SJME_SOFTMIX(f32, u8, float, sjme_jubyte, f, u, a * 128.0F)
SJME_SOFTMIX(f32, s16, float, sjme_jshort, f, s, a * 32768.0F)
SJME_SOFTMIX(f32, s32, float, sjme_jint, f, i, a * 2147483648.0F)
SJME_SOFTMIX(f32, f32, float, float, f, f, a)
#endif

const sjme_scritchaudio_softmix_mixer
	sjme_scritchaudio_softmix_mixers[SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS]
	[SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS] =
{
	/* 8-bit -> ... */
	{
		sjme_scritchaudio_softmix_u8_to_u8,
		sjme_scritchaudio_softmix_u8_to_s16,
		sjme_scritchaudio_softmix_u8_to_s32,
#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
		sjme_scritchaudio_softmix_u8_to_f32
#else
		NULL,
#endif
	},
	
	/* 16-bit -> ... */
	{
		sjme_scritchaudio_softmix_s16_to_u8,
		sjme_scritchaudio_softmix_s16_to_s16,
		sjme_scritchaudio_softmix_s16_to_s32,
#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
		sjme_scritchaudio_softmix_s16_to_f32
#else
		NULL,
#endif
	},
	
	/* 32-bit -> ... */
	{
		sjme_scritchaudio_softmix_s32_to_u8,
		sjme_scritchaudio_softmix_s32_to_s16,
		sjme_scritchaudio_softmix_s32_to_s32,
#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
		sjme_scritchaudio_softmix_s32_to_f32
#else
		NULL,
#endif
	},
	
	/* float -> ... */
	{
#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
		sjme_scritchaudio_softmix_f32_to_u8,
		sjme_scritchaudio_softmix_f32_to_s16,
		sjme_scritchaudio_softmix_f32_to_s32,
		sjme_scritchaudio_softmix_f32_to_f32,
#else
		NULL,
		NULL,
		NULL,
		NULL,
#endif
	},
};
