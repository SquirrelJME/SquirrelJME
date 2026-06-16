/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Sub-structured ScritchUI types.
 * 
 * @file
 * @since 2026/01/21
 */

#ifndef SJME_C_SQUIRRELJME_SCRITCHUITYPESSUB_H
#define SJME_C_SQUIRRELJME_SCRITCHUITYPESSUB_H

#include "sjme/atomic.h"
#include "sjme/list.h"
#include "lib/scritchui/scritchuiBasic.h"
#include "lib/scritchui/scritchuiTypeDefs.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SCRITCHUITYPESSUB_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#pragma region(scritchui_other)
	
/** @link sjme_scritchui_uiComponent @endlink type. */
#define SJME_TYPEOF_BASIC_sjme_scritchui_uiComponent \
	SJME_TYPEOF_BASIC_sjme_pointer
	
/** @link sjme_scritchui_pencil @endlink type. */
#define SJME_TYPEOF_BASIC_sjme_scritchui_pencil \
	SJME_TYPEOF_BASIC_sjme_pointer
	
/** @link sjme_scritchui_pencil @endlink is a pointer. */
#define SJME_TYPEOF_IS_POINTER_sjme_scritchui_pencil 1
	
#pragma endregion(scritchui_other)
#pragma region(scritchui_lists)
	
/** Atomic pencil reference. */
SJME_ATOMIC_DECLARE(sjme_scritchui_pencil, 0);
	
/** List of component. */
SJME_LIST_DECLARE(sjme_scritchui_uiComponent, 0);

/** A list of choice items. */
SJME_LIST_DECLARE(sjme_scritchui_uiChoiceItem, 0);

/** Menu item list. */
SJME_LIST_DECLARE(sjme_scritchui_uiMenuKind, 0);
	
/** A list of pencil fonts. */
SJME_LIST_DECLARE(sjme_scritchui_pencilFont, 0);
	
/** A list of screens. */
SJME_LIST_DECLARE(sjme_scritchui_uiScreen, 0);

#pragma endregion(scritchui_lists)
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUITYPESSUB_H
}
#undef SJME_CXX_SQUIRRELJME_SCRITCHUITYPESSUB_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUITYPESSUB_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SCRITCHUITYPESSUB_H */
