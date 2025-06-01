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

#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
static sjme_errorCode sjme_scritchaudio_softmix_f32_to_s16(
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* sourceInfo,
	sjme_attrInNotNull sjme_scritchaudio_buffer* sourceBuf,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* destInfo,
	sjme_attrInNotNull sjme_scritchaudio_buffer* destBuf)
{
	float* s;
	sjme_jshort* d;
	sjme_fixed sI, dI, sAdd, dAdd, sT, dT, lT;
	sjme_jshort t, v;
	sjme_jint f, c;
	
	if (sourceInfo == NULL || sourceBuf == NULL ||
		destInfo == NULL || destBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get pointers to relate from. */
	s = (float*)sourceBuf->f;
	d = destBuf->s;
	
	/* Use the lower set of samples. */
	sT = sourceInfo->totalSamples;
	dT = destInfo->totalSamples;
	lT = sjme_fixed_hi(sT < dT ? sT : dT);
	
	/* Copy every sample. */
	sAdd = sourceInfo->fromIncr;
	dAdd = sourceInfo->toIncr;
	for (sI = 0, dI = 0; sI < lT && dI < lT; sI += sAdd, dI += dAdd)
	{
		/* Read in values to mix. */
		t = (sjme_jshort)(s[sI >> SJME_FIXED_SHIFT] * 32768);
		v = d[dI >> SJME_FIXED_SHIFT];

		/* Soft clipping? */
		d[dI >> SJME_FIXED_SHIFT] = (v + t) - (v * t);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}
#endif

const sjme_scritchaudio_softmix_mixer
	sjme_scritchaudio_softmix_mixers[SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS]
	[SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS] =
{
	/* 8-bit -> ... */
	{
		NULL,
		NULL,
		NULL,
		NULL,
	},
	
	/* 16-bit -> ... */
	{
		NULL,
		NULL,
		NULL,
		NULL,
	},
	
	/* 32-bit -> ... */
	{
		NULL,
		NULL,
		NULL,
		NULL,
	},
	
	/* float -> ... */
	{
#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
		NULL,
		sjme_scritchaudio_softmix_f32_to_s16,
		NULL,
		NULL,
#else
		NULL,
		NULL,
		NULL,
		NULL,
#endif
	},
};
