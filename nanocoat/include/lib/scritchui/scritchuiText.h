/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Text rendering support, equivalent
 * to @code{.java} javax.microedition.lcdui.Text @endcode .
 * 
 * @file
 * @since 2024/06/25
 */

#ifndef SJME_C_SCRITCHUITEXT_H
#define SJME_C_SCRITCHUITEXT_H

#include "lib/scritchui/scritchuiBasic.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUITEXT_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Contains the state of the text renderer.
 * 
 * @since 2024/06/26
 */
typedef struct sjme_scritchui_textBase* sjme_scritchui_text;

/**
 * Destroys a static text instance.
 * 
 * @param inOutText The input and output text to destroy. 
 * @return Any resultant error, if any.
 * @since 2024/06/26
 */
sjme_errorCode sjme_scritchui_textDeleteStatic(
	sjme_attrInOutNotNull sjme_scritchui_text inOutText);

/**
 * Initializes a new static text instance.
 * 
 * @param inOutText The input and output text to initialize. 
 * @return Any resultant error, if any.
 * @since 2024/06/26
 */
sjme_errorCode sjme_scritchui_textNewStatic(
	sjme_attrInOutNotNull sjme_scritchui_text inOutText);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUITEXT_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHUITEXT_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUITEXT_H */
#endif     /* #ifdef __cplusplus */

#endif /* SJME_C_SCRITCHUITEXT_H */
