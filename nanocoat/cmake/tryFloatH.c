/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <float.h>

static void check(int ignored, ...) {}

int main(int argc, char** argv)
{
	check(FLT_ROUNDS, FLT_EVAL_METHOD, FLT_RADIX,
		FLT_MANT_DIG, DBL_MANT_DIG);
	
#if defined(FLT_HAS_SUBNORM)
	check(FLT_HAS_SUBNORM);
#endif
	
#if defined(__FLT_HAS_DENORM__)
	check(__FLT_HAS_DENORM__);
#endif
	
#if defined(DBL_HAS_SUBNORM)
	check(DBL_HAS_SUBNORM);
#endif
	
#if defined(__DBL_HAS_DENORM__)
	check(__DBL_HAS_DENORM__);
#endif
	
	return 0;
}
