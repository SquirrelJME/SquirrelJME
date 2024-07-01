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
	SJME_TOKEN_PASTE4_PP(sjme_scritchui_pencil_, func, _, \
		SJME_PENCIL_NAME)
 
static sjme_errorCode pencilFunc_NAME(DrawPixel)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NULL_ARGUMENTS;
}

static const struct sjme_scritchui_pencilImplFunctions pencilFunc_NAME(_) =
{
	.drawPixel = pencilFunc_NAME(DrawPixel),
};

/* Remove definitions for next inclusion. */
#undef SJME_PENCIL_NAME
#undef SJME_PENCIL_PIXEL_FORMAT
#undef pencilPixelType
#undef pencilPixelBits
#undef pencilPixelMask
#undef pencilFunc_NAME
