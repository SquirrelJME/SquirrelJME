/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "random.h"

sjme_jboolean sjme_randomSeed(sjme_randomState* random, sjme_jlong seed,
	sjme_error* error)
{
	sjme_todo("Fix this");
	return sjme_false;
#if 0
	/* Check. */
	if (random == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}
	
	/* Wipe the random state. */
	memset(random, 0, sizeof(*random));
	
	/* Calculate the resultant seed. */
	/* this._seed = (__seed ^ 0x5_DEECE66DL) & ((1L << 48) - 1); */
	random->seed.hi = (seed.hi ^ SJME_JINT_C(0x00000005)) & 0xFFFF;
	random->seed.lo = (seed.lo ^ SJME_JINT_C(0xDEECE66D));
		
	return sjme_true;
#endif
}

sjme_jboolean sjme_randomNextBoolean(sjme_randomState* random,
	sjme_jboolean* out, sjme_error* error)
{
	sjme_jint val;
	
	if (out == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}
	
	/* Get next value. */
	if (sjme_randomNextBits(random, &val, 1, error))
		return sjme_keepErrorF(error, SJME_ERROR_COULD_NOT_SEED, 0);
	
	*out = (val != 0 ? sjme_true : sjme_false);
	return sjme_true;
}

sjme_jboolean sjme_randomNextInt(sjme_randomState* random, sjme_jint* out,
	sjme_error* error)
{
	sjme_jint val;
	
	if (out == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}
	
	/* Get next value. */
	if (sjme_randomNextBits(random, &val, 32, error))
		return sjme_keepErrorF(error, SJME_ERROR_COULD_NOT_SEED, 0);
	
	*out = val;
	return sjme_true;
}

sjme_jboolean sjme_randomNextBits(sjme_randomState* random, sjme_jint* out,
	sjme_jint bits, sjme_error* error)
{
	sjme_todo("Fix this");
	return sjme_false;
#if 0
	sjme_jlong seed;
	sjme_jint sh;
	
	/* Cannot be null. */
	if (random == NULL || out == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}
	
	/* Must be in 32-bit range. */
	if (bits < 0 || bits > 32)
	{
		sjme_setError(error, SJME_ERROR_INVALID_ARGUMENT, bits);
		return sjme_false;
	}
	
	/* long seed = (this._seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1); */
	/* aa = this._seed * 0x5DEECE66DL. */
	seed = random->seed;
	seed = sjme_mulLongF(seed,
		SJME_JINT_C(0xDEECE66D), SJME_JINT_C(0x5));
	/* bb = aa + 0xBL */
	seed = sjme_addLongF(seed, SJME_JINT_C(0xB), 0);
	/* cc = & ((1L << 48) - 1) */
	seed.hi = seed.hi & SJME_JINT_C(0xFFFF);
	
	/*this._seed = seed; */
	random->seed.hi = seed.hi;
	random->seed.lo = seed.lo;
	
	/* return (int)(seed >>> (48 - __bits)); */
	sh = 48 - bits;
	if (sh >= 32)
		*out = seed.hi >> (sh - 32);
	else
		*out = (seed.hi << (32 - sh)) | sjme_ushrInt(seed.lo, sh);
	
	return sjme_true;
#endif
}
