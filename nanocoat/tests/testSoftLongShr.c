/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "test.h"
#include "proto.h"
#include "mock.h"
#include "testData.h"
#include "unit.h"
#include "sjme/softLong.h"

/**
 * Tests software long math.
 *  
 * @since 2026/10/31 
 */
SJME_TEST_DECLARE(testSoftLongShr)
{
	sjme_jint i, okay, b;
	sjme_jlong rA, rB, x, a;
	
	/* Run for everything. */
	okay = 0;
	for (i = 0; i < SJME_TEST_NUM_LONGS; i++)
	{
		/* Clear. */
		memset(&rA, 0, sizeof(rA));
		memset(&rB, 0, sizeof(rB));
		memset(&x, 0, sizeof(x));
		memset(&a, 0, sizeof(a));
		
		/* Fill in source values. */
		a.part.lo = sjme_test_longs_in[i].lo;
		a.part.hi = sjme_test_longs_in[i].hi;
		b = sjme_test_longs_in[(i + 1) % SJME_TEST_NUM_LONGS].lo;
		
		/* Fill in expected. */
		x.part.lo = sjme_test_longs_shr[i].lo;
		x.part.hi = sjme_test_longs_shr[i].hi;
		
		/* Calculate. */
		rA = sjme_softLong_shr(&rB, &a, b);
		
		/* Check. */
		if (0 == memcmp(&rA, &x, sizeof(rA)) &&
			0 == memcmp(&rB, &x, sizeof(rB)))
			okay++;
		
		/* Failed! */
		else
		{
			sjme_emitB("> %08x_%08x >> %d (%08x) = ",
				a.part.hi, a.part.lo, b & 0x3F, b);
			sjme_emitB("  %08x_%08x != [%08x_%08x, %08x_%08x]",
				x.part.hi, x.part.lo,
				rA.part.hi, rA.part.lo, rB.part.hi, rB.part.lo);
			sjme_emitB("  %08x_%08x ^^ [%08x_%08x, %08x_%08x]",
				x.part.hi ^ (rA.part.hi | rB.part.hi),
				x.part.lo ^ (rA.part.lo | rB.part.lo),
				rA.part.hi ^ x.part.hi, rA.part.lo ^ x.part.lo,
				rB.part.hi ^ x.part.hi, rB.part.lo ^ x.part.lo);
			sjme_emitB("  %08x_%08x && [%08x_%08x, %08x_%08x]",
				x.part.hi & (rA.part.hi | rB.part.hi),
				x.part.lo & (rA.part.lo | rB.part.lo),
				rA.part.hi & x.part.hi, rA.part.lo & x.part.lo,
				rB.part.hi & x.part.hi, rB.part.lo & x.part.lo);
			sjme_emitB("  %08x_%08x || [%08x_%08x, %08x_%08x]",
				x.part.hi | (rA.part.hi | rB.part.hi),
				x.part.lo | (rA.part.lo | rB.part.lo),
				rA.part.hi | x.part.hi, rA.part.lo | x.part.lo,
				rB.part.hi | x.part.hi, rB.part.lo | x.part.lo);
			sjme_emitB("");
		}
	}
	
	/* Must be the number of longs! */
	if (okay == SJME_TEST_NUM_LONGS)
		return SJME_TEST_RESULT_PASS;
	return SJME_TEST_RESULT_FAIL;
}
