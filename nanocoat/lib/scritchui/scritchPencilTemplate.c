/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#if !defined(pencilPixelType)
	#pragma clang diagnostic ignored "-Wimplicit-function-declaration"
	#pragma clang diagnostic ignored "-Werror=implicit-function-declaration"

	#pragma clang diagnostic ignored "-Wmissing-variable-declarations"
	#pragma clang diagnostic ignored "-Werror=missing-variable-declarations"
#endif

/** Primitive function name. */
#define pencilFunc_NAME(func) \
	SJME_TOKEN_PASTE5_PP(sjme_scritchui_basic, func, _, \
		pencilPixelType, pencilPixelBits)

static const struct sjme_scritchui_pencilImplFunctions pencilFunc_NAME(_) =
{
	sjme_sm(.driverName, SJME_TOKEN_STRING_PP(pencilFunc_NAME(_))),
	sjme_sm(.asyncSafe, SJME_JNI_FALSE),
	sjme_sm(.close, NULL),
	sjme_sm(.copyArea, NULL),
	sjme_sm(.drawHorizSrc, NULL),
	sjme_sm(.drawHorizSrcOver, NULL),
	sjme_sm(.drawLineSrc, NULL),
	sjme_sm(.drawLineSrcOver, NULL),
	sjme_sm(.drawPixelSrc, NULL),
	sjme_sm(.drawPixelSrcOver, NULL),
	sjme_sm(.mapColor, NULL),
#if defined(pencilRawScanCopy)
	sjme_sm(.rawScanGet, sjme_scritchui_basicRawScanGet),
	sjme_sm(.rawScanPutPure, sjme_scritchui_basicRawScanPutPure),
#else
	sjme_sm(.rawScanGet, pencilFunc_NAME(RawScanGet)),
	sjme_sm(.rawScanPutPure, pencilFunc_NAME(RawScanPutPure)),
#endif
};

/* From this template. */
#undef pencilFunc_NAME

/* Remove definitions for next inclusion. */
#undef pencilPixelType
#undef pencilPixelBits
#undef pencilPixelMask

#if defined(pencilRawScanCopy)
	#undef pencilRawScanCopy
#endif
