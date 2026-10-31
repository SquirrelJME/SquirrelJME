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
#include "sjme/util.h"

static const sjme_jlong sjme_softLong_zero;

static void sjme_softLong_pack(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInValue sjme_juint vL,
	sjme_attrInValue sjme_jint vH)
{
	if (r == NULL)
		return;
	
	r->part.hi = vH;
	r->part.lo = vL;
}

static void sjme_softLong_negImpl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInValue sjme_juint aL,
	sjme_attrInValue sjme_jint aH)
{
	if (r == NULL)
		return;
	
	/* Negate and check for overflow */
	aH = (~aH);
	aL = (~aL + 1U);
	if (aL == 0U)
		aH++;
	
	/* Return result */
	sjme_softLong_pack(r, aL, aH);
}

static void sjme_softLong_divImpl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_jboolean doRem,
	sjme_attrInValue sjme_juint nL,
	sjme_attrInValue sjme_jint nH,
	sjme_attrInValue sjme_juint dL,
	sjme_attrInValue sjme_jint dH)
{
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
	sjme_todo("Impl?");
	
#if 0
	/* {@squirreljme.error ZZ4z Divide by zero.} */
	if (__dl == 0 && __dh == 0)
		throw new ArithmeticException("ZZ4z");
	
	// Wikipedia (http://en.wikipedia.org/wiki/Division_%28digital%29)
	// if D == 0 then throw DivisionByZeroException end
	// Q := 0 # initialize quotient and remainder to zero
	// R := 0
	// for i = n-1...0 do  # " where n is no of bits "
	//   R := R << 1       # left-shift R by 1 bit
	//   R(0) := N(i)      # set the least-significant bit
	//              # of R equal to bit i of the numerator
	//   if R >= D then
	//     R = R - D
	//     Q(i) := 1
	//   end
	// end
	// long qx = 0, rx = 0;
	
	// High and low resultant values
	int ql = 0;
	int qh = 0;
	int rl = 0;
	int rh = 0;
	
	// Disallow division by zero
	if (__dl == 0)
		return 0;
	
	// Results in a negative? Only results in such if either side is
	// a negative value
	boolean negNum = ((__nh & 0x8000_0000) != 0);
	boolean negDem = ((__dh & 0x8000_0000) != 0);
	boolean isNeg = (negNum != negDem);
	
	// Make the numerator positive, if negative
	if (negNum)
	{
		// Negate and check for overflow
		__nh = (~__nh);
		__nl = (~__nl + 1);
		if (__nl == 0)
			__nh++;
	}
	
	// Make the denominator positive, if negative
	if (negDem)
	{
		// Negate and check for overflow
		__dh = (~__dh);
		__dl = (~__dl + 1);
		if (__dl == 0)
			__dh++;
	}
	
	// __dl and __dh for unsigned compare
	int dlUnsigned = __dl + Integer.MIN_VALUE;
	int dhUnsigned = __dh + Integer.MIN_VALUE;
	
	// Perform Math
	for (int i = 63, hMask = 0xFFFF_FFFF, lMask = 0;
		i >= 0;
		i--, hMask >>>= 1, lMask = ((lMask >> 1) | 0x8000_0000))
	{
		// rx <<= 1;
		// rx &= 0xFFFFFFFFFFFFFFFEL;
		rh <<= 1;
		rh |= (rl >>> 31);
		rl <<= 1;
		
		// rx |= ((__nx >>> i) & 1L); ... only take the lowest bit!
		// branching:
		// ! if (i >= 32)
		// !     rl |= ((__nh >>> (i - 32)) & 0x1);
		// ! else
		// !     rl |= ((__nl >>> i) & 0x1);
		// faster using masking:
		// ! rl |= (((__nh & hMask) >>> i) & 0x1);
		// ! rl |= (((__nl & lMask) >>> i) & 0x1);
		rl |= ((((__nh & hMask) | (__nl & lMask)) >>> i) & 0x1);
		
		// Unsigned comparison (shift by 0x8000_0000__0000_0000L)
		// if ((rx + Long.MIN_VALUE) >= (__dx + Long.MIN_VALUE))
		/*if (SoftLong.cmp(rl, rh + Integer.MIN_VALUE,
			__dl, __dh + Integer.MIN_VALUE) >= 0)*/
		int cmp = (rh + Integer.MIN_VALUE) - dhUnsigned;
		if (cmp >= 0 &&
			(cmp > 0 ||	// Is just a bigger number overall? 
			rl + Integer.MIN_VALUE >= dlUnsigned))
		{
			// rx -= __dx;
			// The same as add, but the second operand is negated
			// Negate and check for overflow
			int bh = (~__dh);
			int bl = (~__dl + 1);
			if (bl == 0)
				bh++;
			
			// Add the higher/lower parts
			rh = rh + bh;
			int cl = rl + bl;
			
			// If the low addition carried a bit over, then set that bit in
			// the high part
			if ((cl + 0x80000000) < (rl + 0x80000000))
				rh++;
			
			// Use result
			rl = cl;
			
			// qx |= (1L << i);
			if (i >= 32)
				qh |= (1 << (i - 32));
			else
				ql |= (1 << i);
		}
	}
	
	// Return the remainder if needed, note the remainder is negative only
	// if the numerator is negative
	if (__doRem)
		return (negNum ? SoftLong.neg(rl, rh) :
			MathShelf.longPack(rl, rh));
	
	// Return, normalize negative if needed
	return (isNeg ? SoftLong.neg(ql, qh) : MathShelf.longPack(ql, qh));
#endif
}

static void sjme_softLong_mulImpl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInValue sjme_juint aL,
	sjme_attrInValue sjme_juint aH,
	sjme_attrInValue sjme_juint bL,
	sjme_attrInValue sjme_juint bH)
{
	uint32_t over;
	uint32_t m, p, o, n, a, b, c, d, w, x, y, z;
	
	if (r == NULL)
		return;
	
	/* Multiplies two long values, note that this will fail if both A and B */
	/* are negative. The values must either be both positive or only one is */
	/* negative. */
	
	/* First value */
	a = (aH >> 16);
	b = (aH & UINT32_C(0xFFFF));
	c = (aL >> 16);
	d = (aL & UINT32_C(0xFFFF));

	/* Second value */
	w = (bH >> 16);
	x = (bH & UINT32_C(0xFFFF));
	y = (bL >> 16);
	z = (bL & UINT32_C(0xFFFF));

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
	over = (p >> 16);
	
	/* int o = (d * y) + (c * z) + (p >> 16); */
	o = over + (d * y);
	over = (o >> 16);
	o = (o & UINT32_C(0xFFFF)) + (c * z);
	over += (o >> 16);
	
	/* int n = (d * x) + (c * y) + (b * z) + (o >> 16); */
	n = over + (d * x);
	over = (n >> 16);
	n = (n & UINT32_C(0xFFFF)) + (c * y);
	over += (n >> 16);
	n = (n & UINT32_C(0xFFFF)) + (b * z);
	over += (n >> 16);
	
	/* int m = (d * w) + (c * x) + (b * y) + (a * z) + (n >> 16); */
	/* We need not care about overflow here, because it will all be */
	/* discarded anyway! */
	m = over + (d * w) + (c * x) + (b * y) + (a * z);

	/* Combine the resultant parts */
	sjme_softLong_pack(r,
		(o << 16) | (p & UINT32_C(0xFFFF)),
		(m << 16) | (n & UINT32_C(0xFFFF)));
}

static void sjme_softLong_addImpl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInValue sjme_juint aL,
	sjme_attrInValue sjme_jint aH,
	sjme_attrInValue sjme_juint bL,
	sjme_attrInValue sjme_jint bH)
{
	uint32_t cl;
	int32_t ch;
	
	if (r == NULL)
		return;
	
	/* Add the higher/lower parts */
	cl = aL + bL;
	ch = aH + bH;

	/* If the low addition carried a bit over, then set that bit in the */
	/* high part */
	if ((cl + UINT32_C(0x80000000)) < (aL + UINT32_C(0x80000000)))
		ch++;
	
	/* Return result */
	sjme_softLong_pack(r, cl, ch);
}

static sjme_jboolean sjme_softLong_cmpImpl(
	sjme_attrInValue sjme_juint aL,
	sjme_attrInValue sjme_juint aH,
	sjme_attrInValue sjme_juint bL,
	sjme_attrInValue sjme_juint bH)
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
	sjme_attrInValue sjme_juint aL,
	sjme_attrInValue sjme_jint aH,
	sjme_attrInValue sjme_juint bL,
	sjme_attrInValue sjme_jint bH)
{
	if (r == NULL)
		return;
	
	/* Are both sides negative? */
	if (((aH & bH) & UINT32_C(0x80000000)) != 0U)
	{
		/* Negate and check for overflow */
		aH = (~aH);
		aL = (~aL + 1U);
		if (aL == 0U)
			aH++;
			
		/* Negate and check for overflow */
		bH = (~bH);
		bL = (~bL + 1U);
		if (bL == 0U)
			bH++;
		
		/* Return result */
		sjme_softLong_mulImplSub(r, aL, aH, bL, bH);
		return;
	}
	
	/* Perform the calculation */
	sjme_softLong_mulImplSub(r, aL, aH, bL, bH);
}

static void sjme_softLong_shlImpl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInValue sjme_juint aL,
	sjme_attrInValue sjme_juint aH,
	sjme_attrInNotNull sjme_jint s)
{
	if (r == NULL)
		return;
	
	/* Mask the shift amount */
	s &= UINT32_C(0x3F);
	
	/* Doing nothing? */
	if (s == 0)
		sjme_softLong_pack(r, aL, aH);
	
	/* Shifting all the low bits to the high bits */
	else if (s >= 32)
		sjme_softLong_pack(r, 0U, aL << (s - 32));
	
	/* Merge of bits (shift in range of 1-31) */
	else
		sjme_softLong_pack(r, (aL << s),
			(aH << s) | (aL >> (32 - s)));
}

static void sjme_softLong_shrImpl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInValue sjme_juint aL,
	sjme_attrInValue sjme_juint aH,
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
		sjme_softLong_pack(r, aH >> (s - 32),
			(aH & UINT32_C(0x80000000)) >> 31);
	
	/* Merge of bits (shift in range of 1-31) */
	else
		sjme_softLong_pack(r, (aH << (32 - s)) | (aL >> s),
			(aH >> s));
}

static void sjme_softLong_subImpl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInValue sjme_juint aL,
	sjme_attrInValue sjme_juint aH,
	sjme_attrInValue sjme_juint bL,
	sjme_attrInValue sjme_juint bH)
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
	if ((cl + UINT32_C(0x80000000)) < (aL + UINT32_C(0x80000000)))
		ch++;
	
	/* Return result */
	sjme_softLong_pack(r, cl, ch);
}

static void sjme_softLong_ushrImpl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInValue sjme_juint aL,
	sjme_attrInValue sjme_juint aH,
	sjme_attrInNotNull sjme_juint s)
{
	if (r == NULL)
		return;
	
	/* Mask the shift amount */
	s &= 0x3FU;
	
	/* Doing nothing? */
	if (s == 0U)
		sjme_softLong_pack(r, aL, aH);
	
	/* Shifting all the high bits low */
	else if (s >= 32U)
		sjme_softLong_pack(r, aH >> (s - 32U), 0U);
	
	/* Merge of bits (shift in range of 1-31) */
	else
		sjme_softLong_pack(r, (aH << (32U - s)) | (aL >> s),
			(aH >> s));
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
	sjme_softLong_addImpl(&v, a->part.lo, a->part.hi,
		b->part.lo, b->part.hi);
	
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
	sjme_softLong_pack(&v, a->part.lo & b->part.lo,
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
	
	return sjme_softLong_cmpImpl(a->part.lo, a->part.hi,
		b->part.lo, b->part.hi);
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
		a->part.lo, a->part.hi,
		b->part.lo, b->part.hi);
	
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
	sjme_softLong_mulImpl(&v, a->part.lo, a->part.hi,
		b->part.lo, b->part.hi);
	
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
	sjme_softLong_negImpl(&v, a->part.lo, a->part.hi);
	
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
	sjme_softLong_pack(&v, a->part.lo | b->part.lo,
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
		a->part.lo, a->part.hi,
		b->part.lo, b->part.hi);
	
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
	sjme_softLong_shlImpl(&v, a->part.lo, a->part.hi,
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
	sjme_softLong_shrImpl(&v, a->part.lo, a->part.hi,
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
	sjme_softLong_subImpl(&v, a->part.lo, a->part.hi,
		b->part.lo, b->part.hi);
	
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
	sjme_softLong_ushrImpl(&v, a->part.lo, a->part.hi,
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
	sjme_softLong_pack(&v, a->part.lo ^ b->part.lo,
		a->part.hi ^ b->part.hi);
	
	if (r != NULL)
		memmove(r, &v, sizeof(*r));
	return v;
}

