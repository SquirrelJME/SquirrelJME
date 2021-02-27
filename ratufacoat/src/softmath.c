/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Software Math.
 * 
 * @since 2021/02/27
 */

#include "sjmerc.h"
#include "softmath.h"

sjme_jint_div sjme_div(sjme_jint anum, sjme_jint aden)
{
	/* From Wikipedia (http://en.wikipedia.org/wiki/Division_%28digital%29) */
	/* if D == 0 then throw DivisionByZeroException end*/
	/* Q := 0 # initialize quotient and remainder to Zero  */
	/* R := 0                                              */
	/* for i = n-1...0 do  # " where n is no of bits "     */
	/*   R := R << 1       # left-shift R by 1 bit         */
	/*   R(0) := N(i)      # set the least-significant bit */
	/*              # of R equal to bit i of the numerator */
	/*   if R >= D then                                    */
	/*     R = R - D                                       */
	/*     Q(i) := 1                                       */
	/*   end                                               */
	/* end                                                 */
	sjme_jint_div rv = {0, 0};
	struct
	{
		sjme_juint quot;
		sjme_juint rem;
	} interm = {0, 0};
	sjme_juint i;
	sjme_jbyte isneg;
	
	/* Disallow division by zero */
	if (aden == 0)
		return rv;
	
	/* Negative? */
	isneg = 0;
	if ((anum < 0 && aden >= 0) || (anum >= 0 && aden < 0))
		isneg |= 1;
	
	/* Force Positive */
	anum = (anum < 0 ? -anum : anum);
	aden = (aden < 0 ? -aden : aden);
	
	/* Perform Math */
	for (i = SJME_JUINT_C(31);; i--)
	{
		interm.rem <<= SJME_JUINT_C(1);
		interm.rem &= SJME_JUINT_C(0xFFFFFFFE);
		interm.rem |= (((sjme_juint)anum) >> i) & SJME_JUINT_C(1);
		
		if (interm.rem >= (sjme_juint)aden)
		{
			interm.rem -= (sjme_juint)aden;
			interm.quot |= (SJME_JUINT_C(1) << i);
		}
		
		if (i == 0)
			break;
	}
	
	/* Restore Integers */
	rv.quot = interm.quot;
	rv.rem = interm.rem;
	
	/* Make Negative */
	if (isneg & 1)
		rv.quot = -rv.quot;
	
	/* Return */
	return rv;
}
