/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Cocoa Definitions.
 * 
 * @since 2024/08/15
 */

#ifndef SQUIRRELJME_COCOA_H
#define SQUIRRELJME_COCOA_H

#import <Foundation/Foundation.h>
#import <Cocoa/Cocoa.h>

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiImpl.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "sjme/config.h"
#include "sjme/debug.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_COCOA_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

sjme_errorCode sjme_scritchui_cocoa_apiInit(
	sjme_attrInNotNull sjme_scritchui inState);

sjme_errorCode sjme_scritchui_cocoa_loopExecuteLater(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_COCOA_H
}
		#undef SJME_CXX_SQUIRRELJME_COCOA_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_COCOA_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_COCOA_H */
