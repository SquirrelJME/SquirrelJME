/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internal GTK functions.
 * 
 * @since 2024/05/14
 */

#ifndef SQUIRRELJME_GTK2INTERN_H
#define SQUIRRELJME_GTK2INTERN_H

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_GTK2INTERN_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

typedef sjme_errorCode (*sjme_scritchui_gtk2_reconnectSignalFunc)(
	sjme_attrInNotNull GtkWidget* inWidget,
	sjme_attrInNotNull void* inOnWhat,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoCore,
	sjme_attrInNotNull void* inListener,
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd,
	sjme_attrInNotNull sjme_lpcstr inSignal,
	sjme_attrInNotNull GCallback inGtkCallback);

/** Internal GTK implementation functions. */	
struct sjme_scritchui_implInternFunctions
{
	/** Reconnect a signal. */
	sjme_scritchui_gtk2_reconnectSignalFunc reconnectSignal;
};

sjme_errorCode sjme_scritchui_gtk2_intern_reconnectSignal(
	sjme_attrInNotNull GtkWidget* inWidget,
	sjme_attrInNotNull void* inOnWhat,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoCore,
	sjme_attrInNotNull void* inListener,
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd,
	sjme_attrInNotNull sjme_lpcstr inSignal,
	sjme_attrInNotNull GCallback inGtkCallback);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_GTK2INTERN_H
}
		#undef SJME_CXX_SQUIRRELJME_GTK2INTERN_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_GTK2INTERN_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_GTK2INTERN_H */
