/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/** Primitive function name. */
#define pencilFunc_NAME(func) \
	SJME_TOKEN_PASTE5_PP(sjme_scritchui_basic, func, _, \
		pencilPixelType, pencilPixelBits)

static const struct sjme_scritchui_pencilImplFunctions pencilFunc_NAME(_) =
{
#if defined(pencilRawScanCopy)
	.rawScanGet = sjme_scritchui_basicRawScanGet,
	.rawScanPutPure = sjme_scritchui_basicRawScanPutPure,
#else
	.rawScanGet = pencilFunc_NAME(RawScanGet),
	.rawScanPutPure = pencilFunc_NAME(RawScanPutPure),
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
