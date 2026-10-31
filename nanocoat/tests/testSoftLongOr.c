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
SJME_TEST_DECLARE(testSoftLongOr)
{
	sjme_jint i, okay;
	sjme_jlong rA, rB, x, a, b;
	
	/* Run for everything. */
	okay = 0;
	for (i = 0; i < SJME_TEST_NUM_LONGS; i++)
	{
		/* Clear. */
		memset(&rA, 0, sizeof(rA));
		memset(&rB, 0, sizeof(rB));
		memset(&x, 0, sizeof(x));
		memset(&a, 0, sizeof(a));
		memset(&b, 0, sizeof(b));
		
		/* Fill in source values. */
		a.part.lo = sjme_test_longs_in[i].lo;
		a.part.hi = sjme_test_longs_in[i].hi;
		b.part.lo = sjme_test_longs_in[(i + 1) % SJME_TEST_NUM_LONGS].lo;
		b.part.hi = sjme_test_longs_in[(i + 1) % SJME_TEST_NUM_LONGS].hi;
		
		/* Fill in expected. */
		x.part.lo = sjme_test_longs_or[i].lo;
		x.part.hi = sjme_test_longs_or[i].hi;
		
		/* Calculate. */
		rA = sjme_softLong_or(&rB, &a, &b);
		
		/* Check. */
		if (0 == memcmp(&rA, &x, sizeof(rA)) &&
			0 == memcmp(&rB, &x, sizeof(rB)))
			okay++;
		
		/* Failed! */
		else
		{
			sjme_emitB("> %08x_%08x | %08x_%08x = ",
				a.part.hi, a.part.lo, b.part.hi, b.part.lo);
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
