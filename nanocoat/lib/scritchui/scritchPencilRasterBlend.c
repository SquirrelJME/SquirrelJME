/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/util.h"
#include "sjme/debug.h"
#include "sjme/fixed.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/core/coreRaster.h"
#include "lib/scritchui/scritchuiStatePencil.h"

#pragma region(blendMacros)

/** Name of the blending function. */
#define SJME_BLEND_NAME(x) \
	SJME_TOKEN_PASTE3_PP(sjme_scritchpen_coreUtil_blendRGBInto, _, x)

/** Declares the blending function. */
#define SJME_BLEND_DECL(x) \
	sjme_errorCode SJME_BLEND_NAME(x)( \
		sjme_attrInNotNull sjme_scritchui_pencil g, \
		sjme_attrInValue sjme_jboolean destAlpha, \
		sjme_attrInValue sjme_jboolean srcAlpha, \
		sjme_attrInValue sjme_jboolean mulAlpha, \
		sjme_attrInRange(0, 255) sjme_jint mulAlphaValue, \
		sjme_attrInNotNullBuf(numPixels) sjme_jint* dest, \
		sjme_attrInNotNullBuf(numPixels) const sjme_jint* src, \
		sjme_attrInPositive sjme_jint numPixels)

/** Local variables for blending. */
#define SJME_BLEND_LOCALS \
	sjme_jint i, z; \
	sjme_fixed Cs[4]; \
	sjme_fixed Cd[4]; \
	sjme_fixed Cr[4]; \
	sjme_fixed iAs, iAd, tff; \
	sjme_fixed invCA; \
	sjme_juint srcMask, destMask

#pragma region(colorAlias)

#define Ad Cd[0]
#define CdR Cd[1]
#define CdG Cd[2]
#define CdB Cd[3]

#define As Cs[0]
#define CsR Cs[1]
#define CsG Cs[2]
#define CsB Cs[3]

#define Ar Cr[0]
#define CrR Cr[1]
#define CrG Cr[2]
#define CrB Cr[3]

#pragma endregion(colorAlias)

/** Function checks. */
#define SJME_BLEND_CHECKS \
	if (g == NULL || dest == NULL || src == NULL) \
		return SJME_ERROR_NULL_ARGUMENTS; \
	\
	if (numPixels < 0) \
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS

/** Pre-loop setup. */
#define SJME_BLEND_PRE_SETUP \
	/* Source and dest mask, if alpha is applicable. */ \
	destMask = (destAlpha ? 0 : UINT32_C(0xFF000000)); \
	srcMask = (srcAlpha ? 0 : UINT32_C(0xFF000000)); \
	tff = sjme_fixed_hi(255);

/** Loop iterator. */
#define SJME_BLEND_ITER \
	for (i = 0; i < numPixels; i++)

/** Beginning of the iteration loop. */
#define SJME_BLEND_ITER_BEGIN \
	/* Extract as integers first. */ \
	Ad = ((dest[i] | destMask) >> 24) & 0xFF; \
	CdR = ((dest[i]) >> 16) & 0xFF; \
	CdG = ((dest[i]) >> 8) & 0xFF; \
	CdB = dest[i] & 0xFF; \
	\
	As = ((src[i] | srcMask) >> 24) & 0xFF; \
	CsR = ((src[i]) >> 16) & 0xFF; \
	CsG = ((src[i]) >> 8) & 0xFF; \
	CsB = src[i] & 0xFF; \
	\
	/* Inverse alpha. */ \
	iAs = sjme_fixed_fraction(255 - As, 255); \
	iAd = sjme_fixed_fraction(255 - Ad, 255); \
	\
	/* Extract destination components. */ \
	Ad = sjme_fixed_fraction(Ad, 255); \
	CdR = sjme_fixed_fraction(CdR, 255); \
	CdG = sjme_fixed_fraction(CdG, 255); \
	CdB = sjme_fixed_fraction(CdB, 255); \
	\
	/* Extract source components, and pre-multiply. */ \
	As = sjme_fixed_fraction(As, 255); \
	CsR = sjme_fixed_mul(sjme_fixed_fraction(CsR, 255), As); \
	CsG = sjme_fixed_mul(sjme_fixed_fraction(CsG, 255), As); \
	CsB = sjme_fixed_mul(sjme_fixed_fraction(CsB, 255), As)

/** End of iteration loop. */
#define SJME_BLEND_ITER_END \
	/* Return the original factor. */ \
	Ar = sjme_fixed_mul(Ar, tff); \
	CrR = sjme_fixed_mul(CrR, tff); \
	CrG = sjme_fixed_mul(CrG, tff); \
	CrB = sjme_fixed_mul(CrB, tff); \
	 \
	/* Undo pre-multiply(???). */ \
	/* It seems this already happens? */ \
	/*invCA = sjme_fixed_div(sjme_fixed_hi(1), Ar);*/ \
	/*CrR = sjme_fixed_mul(CrB, invCA);*/ \
	/*CrG = sjme_fixed_mul(CrG, invCA);*/ \
	/*CrB = sjme_fixed_mul(CrB, invCA);*/ \
	\
	/* Recompose. */ \
	Ar = sjme_fixed_intClip(0, Ar, 0xFF); \
	CrR = sjme_fixed_intClip(0, CrR, 0xFF); \
	CrG = sjme_fixed_intClip(0, CrG, 0xFF); \
	CrB = sjme_fixed_intClip(0, CrB, 0xFF); \
	dest[i] = (Ar << 24) | (CrR << 16) | (CrG << 8) | CrB | destMask

#define SJME_BLEND_FORMULA(alpha, colors) \
	SJME_TOKEN_SINGLE(z = 0; Ar = alpha; \
	z = 1; CrR = (colors); \
	z = 2; CrG = (colors); \
	z = 3; CrB = (colors))

#pragma endregion(blendMacros)
#pragma region(blendFunctions)

static SJME_BLEND_DECL(SrcOver)
{
	SJME_BLEND_LOCALS;
	SJME_BLEND_CHECKS;
	SJME_BLEND_PRE_SETUP;
	
	/* Blend each pixel individually. */
	SJME_BLEND_ITER
	{
		SJME_BLEND_ITER_BEGIN;
		
		/* Ar = As + Ad * (1 - As) */
		/* Cr = Cs + Cd * (1 - As) */
		SJME_BLEND_FORMULA(
			As + sjme_fixed_mul(Ad, iAs),
			Cs[z] + sjme_fixed_mul(Cd[z], iAs));
		
		SJME_BLEND_ITER_END;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static SJME_BLEND_DECL(Src)
{
	SJME_BLEND_LOCALS;
	SJME_BLEND_CHECKS;
	SJME_BLEND_PRE_SETUP;
	
	/* Blend each pixel individually. */
	SJME_BLEND_ITER
	{
		SJME_BLEND_ITER_BEGIN;
		
		/* Ar = As */
		/* Cr = Cs */
		SJME_BLEND_FORMULA(
			As,
			Cs[z]);
		
		SJME_BLEND_ITER_END;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static SJME_BLEND_DECL(SrcAtop)
{
	SJME_BLEND_LOCALS;
	SJME_BLEND_CHECKS;
	SJME_BLEND_PRE_SETUP;
	
	/* Blend each pixel individually. */
	SJME_BLEND_ITER
	{
		SJME_BLEND_ITER_BEGIN;
		
		/* Ar = (As * Ad) + (Ad * (1 - As)) = Ad */
		/* Cr = (Cs * Ad) + (Cd * (1 - As)) */
		SJME_BLEND_FORMULA(
			sjme_fixed_mul(As, Ad) + sjme_fixed_mul(Ad, iAs),
			sjme_fixed_mul(Cs[z], Ar) + sjme_fixed_mul(Cd[z], iAs));
		
		SJME_BLEND_ITER_END;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static SJME_BLEND_DECL(SrcIn)
{
	SJME_BLEND_LOCALS;
	SJME_BLEND_CHECKS;
	SJME_BLEND_PRE_SETUP;
	
	/* Blend each pixel individually. */
	SJME_BLEND_ITER
	{
		SJME_BLEND_ITER_BEGIN;
		
		/* Ar = As * Ad */
		/* Cr = Cs * Ad */
		SJME_BLEND_FORMULA(
			sjme_fixed_mul(As, Ad),
			sjme_fixed_mul(Cs[z], Ad));
		
		SJME_BLEND_ITER_END;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static SJME_BLEND_DECL(SrcOut)
{
	SJME_BLEND_LOCALS;
	SJME_BLEND_CHECKS;
	SJME_BLEND_PRE_SETUP;
	
	/* Blend each pixel individually. */
	SJME_BLEND_ITER
	{
		SJME_BLEND_ITER_BEGIN;
		
		/* Ar = As * (1 - Ad) */
		/* Cr = Cs * (1 - Ad) */
		SJME_BLEND_FORMULA(
			sjme_fixed_mul(As, iAd),
			sjme_fixed_mul(Cs[z], iAd));
		
		SJME_BLEND_ITER_END;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static SJME_BLEND_DECL(DestOver)
{
	SJME_BLEND_LOCALS;
	SJME_BLEND_CHECKS;
	SJME_BLEND_PRE_SETUP;
	
	/* Blend each pixel individually. */
	SJME_BLEND_ITER
	{
		SJME_BLEND_ITER_BEGIN;
		
		/* Ar = (As * (1 - Ad)) + Ad */
		/* Cr = (Cs * (1 - Ad)) + Cd */
		SJME_BLEND_FORMULA(
			sjme_fixed_mul(As, iAs) + Ad,
			sjme_fixed_mul(Cs[z], iAs) + Cd[z]);
		
		SJME_BLEND_ITER_END;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static SJME_BLEND_DECL(Dest)
{
	SJME_BLEND_LOCALS;
	SJME_BLEND_CHECKS;
	SJME_BLEND_PRE_SETUP;
	
	/* Blend each pixel individually. */
	SJME_BLEND_ITER
	{
		SJME_BLEND_ITER_BEGIN;
		
		/* Ar = Ad */
		/* Cr = Cd */
		SJME_BLEND_FORMULA(
			Ad,
			Cd[z]);
		
		SJME_BLEND_ITER_END;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static SJME_BLEND_DECL(DestAtop)
{
	SJME_BLEND_LOCALS;
	SJME_BLEND_CHECKS;
	SJME_BLEND_PRE_SETUP;
	
	/* Blend each pixel individually. */
	SJME_BLEND_ITER
	{
		SJME_BLEND_ITER_BEGIN;
		
		/* Ar = (As * (1 - Ad)) + (Ad * As) = As */
		/* Cr = (Cs * (1 - Ad)) + (Cd * As) */
		SJME_BLEND_FORMULA(
			sjme_fixed_mul(As, iAd) + sjme_fixed_mul(Ad, As),
			sjme_fixed_mul(Cs[z], iAd) + sjme_fixed_mul(Cd[z], Ar));
		
		SJME_BLEND_ITER_END;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static SJME_BLEND_DECL(DestIn)
{
	SJME_BLEND_LOCALS;
	SJME_BLEND_CHECKS;
	SJME_BLEND_PRE_SETUP;
	
	/* Blend each pixel individually. */
	SJME_BLEND_ITER
	{
		SJME_BLEND_ITER_BEGIN;
		
		/* Ar = Ad * As */
		/* Cr = Cd * As */
		SJME_BLEND_FORMULA(
			sjme_fixed_mul(Ad, As),
			sjme_fixed_mul(Cd[z], As));
		
		SJME_BLEND_ITER_END;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static SJME_BLEND_DECL(DestOut)
{
	SJME_BLEND_LOCALS;
	SJME_BLEND_CHECKS;
	SJME_BLEND_PRE_SETUP;
	
	/* Blend each pixel individually. */
	SJME_BLEND_ITER
	{
		SJME_BLEND_ITER_BEGIN;
		
		/* Ar = Ad * (1 - As) */
		/* Cr = Cd * (1 - As) */
		SJME_BLEND_FORMULA(
			sjme_fixed_mul(Ad, iAs),
			sjme_fixed_mul(Cd[z], iAs));
		
		SJME_BLEND_ITER_END;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static SJME_BLEND_DECL(Clear)
{
	SJME_BLEND_LOCALS;
	SJME_BLEND_CHECKS;
	SJME_BLEND_PRE_SETUP;
	
	/* Blend each pixel individually. */
	SJME_BLEND_ITER
	{
		SJME_BLEND_ITER_BEGIN;
		
		/* Ar = 0 */
		/* Cr = 0 */
		SJME_BLEND_FORMULA(
			0,
			0);
		
		SJME_BLEND_ITER_END;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static SJME_BLEND_DECL(XOr)
{
	SJME_BLEND_LOCALS;
	SJME_BLEND_CHECKS;
	SJME_BLEND_PRE_SETUP;
	
	/* Blend each pixel individually. */
	SJME_BLEND_ITER
	{
		SJME_BLEND_ITER_BEGIN;
		
		/* Ar = (As * (1 - Ad)) + (Ad * (1 - As)) */
		/* Cr = (Cs * (1 - Ad)) + (Cd * (1 - As)) */
		SJME_BLEND_FORMULA(
			sjme_fixed_mul(As, iAd) + sjme_fixed_mul(Ad, iAs),
			sjme_fixed_mul(Cs[z], iAd) + sjme_fixed_mul(Cd[z], iAs));
		
		SJME_BLEND_ITER_END;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

#pragma endregion(blendFunctions)

static const sjme_scritchui_pencilBlendRGBIntoFunc
	sjme_scritchui_blendFuncs[SJME_NUM_SCRITCHUI_PENCIL_BLENDS] = 
{
	SJME_BLEND_NAME(SrcOver),
	SJME_BLEND_NAME(Src),
	SJME_BLEND_NAME(SrcAtop),
	SJME_BLEND_NAME(SrcIn),
	SJME_BLEND_NAME(SrcOut),
	SJME_BLEND_NAME(DestOver),
	SJME_BLEND_NAME(Dest),
	SJME_BLEND_NAME(DestAtop),
	SJME_BLEND_NAME(DestIn),
	SJME_BLEND_NAME(DestOut),
	SJME_BLEND_NAME(Clear),
	SJME_BLEND_NAME(XOr),
};

sjme_errorCode sjme_scritchpen_coreUtil_blendRGBInto(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean destAlpha,
	sjme_attrInValue sjme_jboolean srcAlpha,
	sjme_attrInValue sjme_jboolean mulAlpha,
	sjme_attrInRange(0, 255) sjme_jint mulAlphaValue,
	sjme_attrInNotNullBuf(numPixels) sjme_jint* dest,
	sjme_attrInNotNullBuf(numPixels) const sjme_jint* src,
	sjme_attrInPositive sjme_jint numPixels)
{
	sjme_scritchui_pencilBlendingMode blending;
	SJME_BLEND_CHECKS;
	
	/* Make sure the blending mode is actually valid. */
	blending = g->state.blending;
	if (blending < 0 || blending >= SJME_NUM_SCRITCHUI_PENCIL_BLENDS)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Forward to the correct blending function. */
	return sjme_scritchui_blendFuncs[blending](g, destAlpha,
		srcAlpha, mulAlpha, mulAlphaValue, dest, src, numPixels);
}
