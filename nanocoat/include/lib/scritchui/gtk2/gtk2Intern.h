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

typedef sjme_errorCode (*sjme_scritchui_gtk2_intern_accelUpdateFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInNotNull GtkWidget* gtkWidget,
	sjme_attrInValue sjme_jboolean addAccel);

typedef sjme_errorCode (*sjme_scritchui_gtk2_intern_checkErrorFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_errorCode ifOkay);

typedef sjme_errorCode (*sjme_scritchui_gtk2_intern_disconnectSignalFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull GtkWidget* inWidget,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoCore);

typedef sjme_jint (*sjme_scritchui_gtk2_intern_mapGtkToScritchKeyFunc)(
	guint in);

typedef sjme_jint (*sjme_scritchui_gtk2_intern_mapGtkToScritchModFunc)(
	guint in);

typedef guint (*sjme_scritchui_gtk2_intern_mapScritchToGtkKeyFunc)(
	sjme_jint in);

typedef guint (*sjme_scritchui_gtk2_intern_mapScritchToGtkModFunc)(
	sjme_jint in);

typedef sjme_errorCode (*sjme_scritchui_gtk2_intern_reconnectSignalFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull GtkWidget* inWidget,
	sjme_attrInNotNull void* inOnWhat,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoCore,
	sjme_attrInNotNull void* inListener,
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd,
	sjme_attrInNotNull GCallback inGtkCallback,
	sjme_attrInPositiveNonZero sjme_jint numSignals,
	...);

typedef sjme_errorCode (*sjme_scritchui_gtk2_intern_widgetInitFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull GtkWidget* inWidget);

struct sjme_scritchui_implInternFunctions
{
	/** Updates accelerator on a widget. */
	sjme_scritchui_gtk2_intern_accelUpdateFunc accelUpdate;
	
	/** Checks if an error occurred. */
	sjme_scritchui_gtk2_intern_checkErrorFunc checkError;
	
	/** Disconnect a signal. */
	sjme_scritchui_gtk2_intern_disconnectSignalFunc disconnectSignal;
	
	/** Map GTK key to ScritchUI key. */
	sjme_scritchui_gtk2_intern_mapGtkToScritchKeyFunc mapGtkToScritchKey;
	
	/** Map GTK modifier to ScritchUI modifier. */
	sjme_scritchui_gtk2_intern_mapGtkToScritchModFunc mapGtkToScritchMod;
	
	/** Map ScritchUI key to GTK key. */
	sjme_scritchui_gtk2_intern_mapScritchToGtkKeyFunc mapScritchToGtkKey;
	
	/** Map ScritchUI modifier to GTK modifier. */
	sjme_scritchui_gtk2_intern_mapScritchToGtkModFunc mapScritchToGtkMod;
	
	/** Reconnect a signal. */
	sjme_scritchui_gtk2_intern_reconnectSignalFunc reconnectSignal;
	
	/** Widget init func. */
	sjme_scritchui_gtk2_intern_widgetInitFunc widgetInit;
};

sjme_errorCode sjme_scritchui_gtk2_intern_accelUpdate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInNotNull GtkWidget* gtkWidget,
	sjme_attrInValue sjme_jboolean addAccel);

sjme_errorCode sjme_scritchui_gtk2_intern_checkError(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_errorCode ifOkay);

sjme_errorCode sjme_scritchui_gtk2_intern_disconnectSignal(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull GtkWidget* inWidget,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoCore);

sjme_jint sjme_scritchui_gtk2_intern_mapGtkToScritchKey(guint in);

sjme_jint sjme_scritchui_gtk2_intern_mapGtkToScritchMod(guint in);

guint sjme_scritchui_gtk2_intern_mapScritchToGtkKey(sjme_jint in);

guint sjme_scritchui_gtk2_intern_mapScritchToGtkMod(sjme_jint in);

sjme_errorCode sjme_scritchui_gtk2_intern_reconnectSignal(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull GtkWidget* inWidget,
	sjme_attrInNotNull void* inOnWhat,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoCore,
	sjme_attrInNotNull void* inListener,
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd,
	sjme_attrInNotNull GCallback inGtkCallback,
	sjme_attrInPositiveNonZero sjme_jint numSignals,
	...);

sjme_errorCode sjme_scritchui_gtk2_intern_widgetInit(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull GtkWidget* inWidget);

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
