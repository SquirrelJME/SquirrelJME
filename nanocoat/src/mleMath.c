/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/nvm/mle.h"
#include "sjme/nvm/mleShelves.h"

SJME_NVM_MLE_FUNCTION_DECL(doublePack)
{
	/* Combine to double. */
	argR->t = SJME_JAVA_TYPE_ID_DOUBLE;
	argR->v.d.bits.lo = argV[0].v.i;
	argR->v.d.bits.hi = argV[1].v.i;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(doubleUnpackHigh)
{
	/* Only take the high part. */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = argV[0].v.d.bits.hi;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(doubleUnpackLow)
{
	/* Only take the low part. */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = argV[0].v.d.bits.lo;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(longPack)
{
	/* Combine to long. */
	argR->t = SJME_JAVA_TYPE_ID_LONG;
	argR->v.j.part.lo = argV[0].v.i;
	argR->v.j.part.hi = argV[1].v.i;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(longUnpackHigh)
{
	/* Only take the high part. */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = argV[0].v.j.part.hi;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(longUnpackLow)
{
	/* Only take the low part. */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = argV[0].v.j.part.lo;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(rawDoubleToLong)
{
	/* Directly remap bits. */
	argR->t = SJME_JAVA_TYPE_ID_LONG;
	argR->v.j.full = argV[0].v.d.longBits;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(rawFloatToInt)
{
	/* Directly remap bits. */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = argV[0].v.f.bits;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(rawIntToFloat)
{
	/* Directly remap bits. */
	argR->t = SJME_JAVA_TYPE_ID_FLOAT;
	argR->v.f.bits = argV[0].v.i;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(rawLongToDouble)
{
	/* Directly remap bits. */
	argR->t = SJME_JAVA_TYPE_ID_DOUBLE;
	argR->v.d.longBits = argV[0].v.j.full;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_SHELF_DECLARE(MathShelf) =
{
	SJME_NVM_MLE_DEFINE(doublePack,
		SJME_MD(SJME_MD_D, SJME_MD_I SJME_MD_I),
		"D", "II"),
	SJME_NVM_MLE_DEFINE(doubleUnpackHigh,
		SJME_MD(SJME_MD_I, SJME_MD_D),
		"I", "D"),
	SJME_NVM_MLE_DEFINE(doubleUnpackLow,
		SJME_MD(SJME_MD_I, SJME_MD_D),
		"I", "D"),
	SJME_NVM_MLE_DEFINE(longPack,
		SJME_MD(SJME_MD_J, SJME_MD_I SJME_MD_I),
		"J", "II"),
	SJME_NVM_MLE_DEFINE(longUnpackHigh,
		SJME_MD(SJME_MD_I, SJME_MD_J),
		"I", "J"),
	SJME_NVM_MLE_DEFINE(longUnpackLow,
		SJME_MD(SJME_MD_I, SJME_MD_J),
		"I", "J"),
	SJME_NVM_MLE_DEFINE(rawDoubleToLong,
		SJME_MD(SJME_MD_J, SJME_MD_D),
		"J", "D"),
	SJME_NVM_MLE_DEFINE(rawFloatToInt,
		SJME_MD(SJME_MD_I, SJME_MD_F),
		"I", "F"),
	SJME_NVM_MLE_DEFINE(rawIntToFloat,
		SJME_MD(SJME_MD_F, SJME_MD_I),
		"F", "I"),
	SJME_NVM_MLE_DEFINE(rawLongToDouble,
		SJME_MD(SJME_MD_D, SJME_MD_J),
		"D", "J"),
	SJME_NVM_MLE_STOP()
};
