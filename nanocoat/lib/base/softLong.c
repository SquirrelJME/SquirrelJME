/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/debug.h"
#include "sjme/softLong.h"
#include "sjme/softInteger.h"
#include "sjme/util.h"

static const sjme_jlong sjme_softLong_zero;

static void sjme_softLong_pack(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInValue sjme_jint vL,
	sjme_attrInValue sjme_jint vH)
{
	if (r == NULL)
		return;
	
	r->part.hi = vH;
	r->part.lo = vL;
}

static void sjme_softLong_negImpl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInValue sjme_jint aL,
	sjme_attrInValue sjme_jint aH)
{
	if (r == NULL)
		return;
	
	/* Negate and check for overflow */
	aH = (~aH);
	aL = ((~aL) + 1);
	if (aL == 0)
		aH++;
	
	/* Return result */
	sjme_softLong_pack(r, aL, aH);
}

static void sjme_softLong_divImpl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_jboolean doRem,
	sjme_attrInValue sjme_jint nL,
	sjme_attrInValue sjme_jint nH,
	sjme_attrInValue sjme_jint dL,
	sjme_attrInValue sjme_jint dH)
{
	sjme_jint ql, qh, rl, rh, dlUnsigned, dhUnsigned;
	sjme_jint i, hMask, lMask, cmp, bh, bl, cl;
	sjme_jboolean negNum, negDem, isNeg;
	
	/* Divide by zero? */
	if ((dL == 0 && dH == 0) || dL == 0)
	{
		sjme_softLong_pack(r, 0, 0);
		return;
	}
	
	/* Wikipedia (http://en.wikipedia.org/wiki/Division_%28digital%29) */
	/* if D == 0 then throw DivisionByZeroException end */
	/* Q := 0 # initialize quotient and remainder to zero */
	/* R := 0 */
	/* for i = n-1...0 do  # " where n is no of bits " */
	/*   R := R << 1       # left-shift R by 1 bit */
	/*   R(0) := N(i)      # set the least-significant bit */
	/*              # of R equal to bit i of the numerator */
	/*   if R >= D then */
	/*     R = R - D */
	/*     Q(i) := 1 */
	/*   end */
	/* end */
	/* long qx = 0, rx = 0; */
	
	/* High and low resultant values */
	ql = 0;
	qh = 0;
	rl = 0;
	rh = 0;
	
	/* Results in a negative? Only results in such if either side is */
	/* a negative value */
	negNum = ((nH & (sjme_jint)INT32_C(0x80000000)) != 0);
	negDem = ((dH & (sjme_jint)INT32_C(0x80000000)) != 0);
	isNeg = (negNum != negDem);
	
	/* Make the numerator positive, if negative */
	if (negNum)
	{
		/* Negate and check for overflow */
		nH = (~nH);
		nL = ((~nL) + 1);
		if (nL == 0)
			nH++;
	}
	
	/* Make the denominator positive, if negative */
	if (negDem)
	{
		/* Negate and check for overflow */
		dH = (~dH);
		dL = ((~dL) + 1);
		if (dL == 0)
			dH++;
	}
	
	/* dL and dH for unsigned compare */
	dlUnsigned = dL + (sjme_jint)INT32_MIN;
	dhUnsigned = dH + (sjme_jint)INT32_MIN;
	
	/* Perform Math */
	for (i = 63, hMask = (sjme_jint)INT32_C(0xFFFFFFFF), lMask = 0;
		i >= 0;
		i--, hMask = sjme_ushr(hMask, 1),
		lMask = (sjme_shr(lMask, 1) | (sjme_jint)INT32_C(0x80000000)))
	{
		/* rx <<= 1; */
		/* rx &= 0xFFFFFFFFFFFFFFFEL; */
		rh = sjme_shl(rh, 1);
		rh |= sjme_ushr(rl, 31);
		rl = sjme_shl(rl, 1);
		
		/* rx |= ((__nx >>> i) & 1L); ... only take the lowest bit! */
		/* branching: */
		/* ! if (i >= 32) */
		/* !     rl |= ((nH >>> (i - 32)) & 0x1); */
		/* ! else */
		/* !     rl |= ((nL >>> i) & 0x1); */
		/* faster using masking: */
		/* ! rl |= (((nH & hMask) >>> i) & 0x1); */
		/* ! rl |= (((nL & lMask) >>> i) & 0x1); */
		rl |= (sjme_ushr(((nH & hMask) | (nL & lMask)), i) & 0x1);
		
		/* Unsigned comparison (shift by 0x8000_0000__0000_0000L) */
		/* if ((rx + Long.MIN_VALUE) >= (__dx + Long.MIN_VALUE)) */
		cmp = (rh + (sjme_jint)INT32_MIN) - dhUnsigned;
		if (cmp >= 0 &&
			(cmp > 0 ||	/* Is just a bigger number overall?  */
			rl + (sjme_jint)INT32_MIN >= dlUnsigned))
		{
			/* rx -= __dx; */
			/* The same as add, but the second operand is negated */
			/* Negate and check for overflow */
			bh = (~dH);
			bl = ((~dL) + 1);
			if (bl == 0)
				bh++;
			
			/* Add the higher/lower parts */
			rh = rh + bh;
			cl = rl + bl;
			
			/* If the low addition carried a bit over, then set that bit in */
			/* the high part */
			if ((cl + (sjme_jint)INT32_C(0x80000000)) < 
				(sjme_jint)(rl + INT32_C(0x80000000)))
				rh++;
			
			/* Use result */
			rl = cl;
			
			/* qx |= (1L << i); */
			if (i >= 32)
				qh |= sjme_shl(1, (i - 32));
			else
				ql |= sjme_shl(1, i);
		}
	}
	
	/* Return the remainder if needed, note the remainder is negative only */
	/* if the numerator is negative */
	if (doRem)
	{
		if (negNum)
			sjme_softLong_negImpl(r, rl, rh);
		else
			sjme_softLong_pack(r, rl, rh);
		
	}
	
	/* Return, normalize negative if needed */
	else
	{
		if (isNeg)
			sjme_softLong_negImpl(r, ql, qh);
		else
			sjme_softLong_pack(r, ql, qh);
	}
}

static void sjme_softLong_mulImpl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInValue sjme_jint aL,
	sjme_attrInValue sjme_jint aH,
	sjme_attrInValue sjme_jint bL,
	sjme_attrInValue sjme_jint bH)
{
	sjme_jint over;
	sjme_jint m, p, o, n, a, b, c, d, w, x, y, z;
	
	if (r == NULL)
		return;
	
	/* Multiplies two long values, note that this will fail if both A and B */
	/* are negative. The values must either be both positive or only one is */
	/* negative. */
	
	/* First value */
	a = sjme_ushr(aH, 16);
	b = (aH & INT32_C(0xFFFF));
	c = sjme_ushr(aL, 16);
	d = (aL & INT32_C(0xFFFF));

	/* Second value */
	w = sjme_ushr(bH, 16);
	x = (bH & INT32_C(0xFFFF));
	y = sjme_ushr(bL, 16);
	z = (bL & INT32_C(0xFFFF));

	/* Effectively this is long multiplication */
	/* Multiplication in two's complement form is the same in both */
	/* signed and unsigned, so we need not worry about signs and just treat */
	/* this all as unsigned. Each fragment is 16 of the 64 bits. */
	/*             |  a.  b,  c.  d */
	/* *           |  w.  x,  y.  z */
	/* ============|===.===,===.=== */
	/*             |d_w.d_x,d_y.d_z */
	/*          c_w|c_x.c_y,c_z. */
	/*      b_w b_x|b_y.b_z,   . */
	/*  a_w a_x a_y|a_z.   ,   . */
	/* ============|===.===,===.=== */
	/*  DISCARD       m.  n,  o.  p */
	
	/* Overflow storage, since we want a temporary and we do not want to */
	/* make any allocations or otherwise for 64-bit values. This keeps it */
	/* all in 32-bit and handles the various situations. */
	
	/* Multiply and add all the parts together */
	/* int p = (d * z); */
	p = (d * z);
	over = sjme_ushr(p, 16);
	
	/* int o = (d * y) + (c * z) + (p >> 16); */
	o = over + (d * y);
	over = sjme_ushr(o, 16);
	o = (o & INT32_C(0xFFFF)) + (c * z);
	over += sjme_ushr(o, 16);
	
	/* int n = (d * x) + (c * y) + (b * z) + (o >> 16); */
	n = over + (d * x);
	over = sjme_ushr(n, 16);
	n = (n & INT32_C(0xFFFF)) + (c * y);
	over += sjme_ushr(n, 16);
	n = (n & INT32_C(0xFFFF)) + (b * z);
	over += sjme_ushr(n, 16);
	
	/* int m = (d * w) + (c * x) + (b * y) + (a * z) + (n >> 16); */
	/* We need not care about overflow here, because it will all be */
	/* discarded anyway! */
	m = over + (d * w) + (c * x) + (b * y) + (a * z);

	/* Combine the resultant parts */
	sjme_softLong_pack(r,
		sjme_shl(o, 16) | (p & INT32_C(0xFFFF)),
		sjme_shl(m, 16) | (n & INT32_C(0xFFFF)));
}

static void sjme_softLong_addImpl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInValue sjme_jint aL,
	sjme_attrInValue sjme_jint aH,
	sjme_attrInValue sjme_jint bL,
	sjme_attrInValue sjme_jint bH)
{
	sjme_jint cl;
	sjme_jint ch;
	
	if (r == NULL)
		return;
	
	/* Add the higher/lower parts */
	cl = aL + bL;
	ch = aH + bH;

	/* If the low addition carried a bit over, then set that bit in the */
	/* high part */
	if ((sjme_jint)(cl + INT32_C(0x80000000)) < 
		(sjme_jint)(aL + INT32_C(0x80000000)))
		ch++;
	
	/* Return result */
	sjme_softLong_pack(r, cl, ch);
}

static sjme_jboolean sjme_softLong_cmpImpl(
	sjme_attrInValue sjme_jint aL,
	sjme_attrInValue sjme_jint aH,
	sjme_attrInValue sjme_jint bL,
	sjme_attrInValue sjme_jint bH)
{
	/* Compare high values firsts */
	if (aH < bH)
		return -1;
	else if (aH > bH)
		return 1;
	
	/* Compare low values with unsigned comparison */
	aL += INT32_MIN;
	bL += INT32_MIN;
	if (aL < bL)
		return -1;
	else if (aL > bL)
		return 1;
	return 0;
}

static void sjme_softLong_mulImplSub(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInValue sjme_jint aL,
	sjme_attrInValue sjme_jint aH,
	sjme_attrInValue sjme_jint bL,
	sjme_attrInValue sjme_jint bH)
{
	if (r == NULL)
		return;
	
	/* Are both sides negative? */
	if (((aH & bH) & INT32_C(0x80000000)) != 0U)
	{
		/* Negate and check for overflow */
		aH = (~aH);
		aL = ((~aL) + 1);
		if (aL == 0U)
			aH++;
			
		/* Negate and check for overflow */
		bH = (~bH);
		bL = ((~bL) + 1);
		if (bL == 0U)
			bH++;
		
		/* Return result */
		sjme_softLong_mulImpl(r, aL, aH, bL, bH);
		return;
	}
	
	/* Perform the calculation */
	sjme_softLong_mulImpl(r, aL, aH, bL, bH);
}

static void sjme_softLong_shlImpl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInValue sjme_jint aL,
	sjme_attrInValue sjme_jint aH,
	sjme_attrInNotNull sjme_jint s)
{
	if (r == NULL)
		return;
	
	/* Mask the shift amount */
	s &= INT32_C(0x3F);
	
	/* Doing nothing? */
	if (s == 0)
		sjme_softLong_pack(r, aL, aH);
	
	/* Shifting all the low bits to the high bits */
	else if (s >= 32)
		sjme_softLong_pack(r, 0U, sjme_shl(aL, (s - 32)));
	
	/* Merge of bits (shift in range of 1-31) */
	else
		sjme_softLong_pack(r, sjme_shl(aL, s),
			sjme_shl(aH, s) | sjme_ushr(aL, (32 - s)));
}

static void sjme_softLong_shrImpl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInValue sjme_jint aL,
	sjme_attrInValue sjme_jint aH,
	sjme_attrInNotNull sjme_jint s)
{
	if (r == NULL)
		return;
	
	/* Mask the shift amount */
	s &= 0x3F;
	
	/* Doing nothing? */
	if (s == 0)
		sjme_softLong_pack(r, aL, aH);
	
	/* Shifting all the high bits low */
	else if (s >= 32)
		sjme_softLong_pack(r, 
			sjme_shr(aH, (s - 32)),
			sjme_shr((aH & INT32_C(0x80000000)), 31));
	
	/* Merge of bits (shift in range of 1-31) */
	else
		sjme_softLong_pack(r, sjme_shl(aH, (32 - s)) | sjme_ushr(aL, s),
			sjme_shr(aH, s));
}

static void sjme_softLong_subImpl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInValue sjme_jint aL,
	sjme_attrInValue sjme_jint aH,
	sjme_attrInValue sjme_jint bL,
	sjme_attrInValue sjme_jint bH)
{
	uint32_t ch;
	uint32_t cl;
	
	if (r == NULL)
		return;
	
	/* The same as add, but the second operand is negated */
	/* Negate and check for overflow */
	bH = (~bH);
	bL = (~bL + 1U);
	if (bL == 0U)
		bH++;
	
	/* Add the higher/lower parts */
	ch = aH + bH;
	cl = aL + bL;

	/* If the low addition carried a bit over, then set that bit in the */
	/* high part */
	if ((sjme_jint)(cl + INT32_C(0x80000000)) < 
		(sjme_jint)(aL + INT32_C(0x80000000)))
		ch++;
	
	/* Return result */
	sjme_softLong_pack(r, cl, ch);
}

static void sjme_softLong_ushrImpl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInValue sjme_jint aL,
	sjme_attrInValue sjme_jint aH,
	sjme_attrInNotNull sjme_jint s)
{
	if (r == NULL)
		return;
	
	/* Mask the shift amount */
	s &= 0x3F;
	
	/* Doing nothing? */
	if (s == 0)
		sjme_softLong_pack(r, aL, aH);
	
	/* Shifting all the high bits low */
	else if (s >= 32U)
		sjme_softLong_pack(r, sjme_ushr(aH, (s - 32U)), 0U);
	
	/* Merge of bits (shift in range of 1-31) */
	else
		sjme_softLong_pack(r, sjme_shl(aH, (32U - s)) | sjme_ushr(aL, s),
			sjme_ushr(aH, s));
}

sjme_jlong sjme_softLong_add(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b)
{
	sjme_jlong v;
	
	if (a == NULL)
		a = &sjme_softLong_zero;
	if (b == NULL)
		b = &sjme_softLong_zero;
	
	memset(&v, 0, sizeof(v));
	sjme_softLong_addImpl(&v, (sjme_jint)a->part.lo, a->part.hi,
		(sjme_jint)b->part.lo, b->part.hi);
	
	if (r != NULL)
		memmove(r, &v, sizeof(*r));
	return v;
}
	
sjme_jlong sjme_softLong_and(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b)
{
	sjme_jlong v;
	
	if (a == NULL)
		a = &sjme_softLong_zero;
	if (b == NULL)
		b = &sjme_softLong_zero;
	
	memset(&v, 0, sizeof(v));
	sjme_softLong_pack(&v, (sjme_jint)(a->part.lo & b->part.lo),
		a->part.hi & b->part.hi);
	
	if (r != NULL)
		memmove(r, &v, sizeof(*r));
	return v;
}
	
sjme_jboolean sjme_softLong_cmp(
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b)
{
	if (a == NULL)
		a = &sjme_softLong_zero;
	if (b == NULL)
		b = &sjme_softLong_zero;
	
	return sjme_softLong_cmpImpl((sjme_jint)a->part.lo, a->part.hi,
		(sjme_jint)b->part.lo, b->part.hi);
}
	
sjme_jlong sjme_softLong_div(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b)
{
	sjme_jlong v;
	
	if (a == NULL)
		a = &sjme_softLong_zero;
	if (b == NULL)
		b = &sjme_softLong_zero;
	
	memset(&v, 0, sizeof(v));
	sjme_softLong_divImpl(&v, SJME_JNI_FALSE,
		(sjme_jint)a->part.lo, a->part.hi,
		(sjme_jint)b->part.lo, b->part.hi);
	
	if (r != NULL)
		memmove(r, &v, sizeof(*r));
	return v;
}
	
sjme_jlong sjme_softLong_mul(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b)
{
	sjme_jlong v;
	
	if (a == NULL)
		a = &sjme_softLong_zero;
	if (b == NULL)
		b = &sjme_softLong_zero;
	
	memset(&v, 0, sizeof(v));
	sjme_softLong_mulImpl(&v, (sjme_jint)a->part.lo, a->part.hi,
		(sjme_jint)b->part.lo, b->part.hi);
	
	if (r != NULL)
		memmove(r, &v, sizeof(*r));
	return v;
}
	
sjme_jlong sjme_softLong_neg(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a)
{
	sjme_jlong v;
	
	if (a == NULL)
		a = &sjme_softLong_zero;
	
	memset(&v, 0, sizeof(v));
	sjme_softLong_negImpl(&v, (sjme_jint)a->part.lo, a->part.hi);
	
	if (r != NULL)
		memmove(r, &v, sizeof(*r));
	return v;
}
	
sjme_jlong sjme_softLong_or(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b)
{
	sjme_jlong v;
	
	if (a == NULL)
		a = &sjme_softLong_zero;
	if (b == NULL)
		b = &sjme_softLong_zero;
	
	memset(&v, 0, sizeof(v));
	sjme_softLong_pack(&v, (sjme_jint)a->part.lo | b->part.lo,
		a->part.hi | b->part.hi);
	
	if (r != NULL)
		memmove(r, &v, sizeof(*r));
	return v;
}
	
sjme_jlong sjme_softLong_rem(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b)
{
	sjme_jlong v;
	
	if (a == NULL)
		a = &sjme_softLong_zero;
	if (b == NULL)
		b = &sjme_softLong_zero;
	
	memset(&v, 0, sizeof(v));
	sjme_softLong_divImpl(&v, SJME_JNI_TRUE,
		(sjme_jint)a->part.lo, a->part.hi,
		(sjme_jint)b->part.lo, b->part.hi);
	
	if (r != NULL)
		memmove(r, &v, sizeof(*r));
	return v;
}
	
sjme_jlong sjme_softLong_shl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull sjme_jint s)
{
	sjme_jlong v;
	
	if (a == NULL)
		a = &sjme_softLong_zero;
	
	memset(&v, 0, sizeof(v));
	sjme_softLong_shlImpl(&v, (sjme_jint)a->part.lo, a->part.hi,
		s);
	
	if (r != NULL)
		memmove(r, &v, sizeof(*r));
	return v;
}
	
sjme_jlong sjme_softLong_shr(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull sjme_jint s)
{
	sjme_jlong v;
	
	if (a == NULL)
		a = &sjme_softLong_zero;
	
	memset(&v, 0, sizeof(v));
	sjme_softLong_shrImpl(&v, (sjme_jint)a->part.lo, a->part.hi,
		s);
	
	if (r != NULL)
		memmove(r, &v, sizeof(*r));
	return v;
}
	
sjme_jlong sjme_softLong_sub(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b)
{
	sjme_jlong v;
	
	if (a == NULL)
		a = &sjme_softLong_zero;
	if (b == NULL)
		b = &sjme_softLong_zero;
	
	memset(&v, 0, sizeof(v));
	sjme_softLong_subImpl(&v, (sjme_jint)a->part.lo, a->part.hi,
		(sjme_jint)b->part.lo, b->part.hi);
	
	if (r != NULL)
		memmove(r, &v, sizeof(*r));
	return v;
}

void sjme_softLong_toDouble(
	sjme_attrOutNotNull sjme_jdouble* r,
	sjme_attrInNotNull const sjme_jlong* a)
{
	sjme_jlong v;
	
	if (a == NULL)
		a = &sjme_softLong_zero;
	
	sjme_todo("Impl?");
}

void sjme_softLong_toFloat(
	sjme_attrOutNotNull sjme_jfloat* r,
	sjme_attrInNotNull const sjme_jlong* a)
{
	sjme_jlong v;
	
	if (a == NULL)
		a = &sjme_softLong_zero;
	
	sjme_todo("Impl?");
}

sjme_jint sjme_softLong_toInteger(
	sjme_attrOutNotNull sjme_jint* r,
	sjme_attrInNotNull const sjme_jlong* a)
{
	sjme_jlong v;
	
	if (a == NULL)
		a = &sjme_softLong_zero;
	
	/* Just return the low order bits */
	*r = (sjme_jint)a->part.lo;
	return (sjme_jint)a->part.lo;
}
	
sjme_jlong sjme_softLong_ushr(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull sjme_jint s)
{
	sjme_jlong v;
	
	if (a == NULL)
		a = &sjme_softLong_zero;
	
	memset(&v, 0, sizeof(v));
	sjme_softLong_ushrImpl(&v, (sjme_jint)a->part.lo, a->part.hi,
		s);
	
	if (r != NULL)
		memmove(r, &v, sizeof(*r));
	return v;
}
	
sjme_jlong sjme_softLong_xor(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b)
{
	sjme_jlong v;
	
	if (a == NULL)
		a = &sjme_softLong_zero;
	if (b == NULL)
		b = &sjme_softLong_zero;
	
	memset(&v, 0, sizeof(v));
	sjme_softLong_pack(&v, (sjme_jint)(a->part.lo ^ b->part.lo),
		a->part.hi ^ b->part.hi);
	
	if (r != NULL)
		memmove(r, &v, sizeof(*r));
	return v;
}

