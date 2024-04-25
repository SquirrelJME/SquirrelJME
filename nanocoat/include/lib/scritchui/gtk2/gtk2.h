/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * GTK2 ScritchUI Header.
 * 
 * @since 2024/04/02
 */

#ifndef SQUIRRELJME_GTK2_H
#define SQUIRRELJME_GTK2_H

#include <gtk-2.0/gtk/gtk.h>

#include "sjme/config.h"
#include "sjme/debug.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiImpl.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_GTK2_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Maps a @c sjme_jboolean to @c gboolean . */
#define SJME_JBOOLEAN_TO_GBOOLEAN(b) \
	((b) == SJME_JNI_FALSE ? FALSE : TRUE)

/** Maps a @c gboolean to @c sjme_jboolean . */
#define GBOOLEAN_TO_SJME_JBOOLEAN(b) \
	((b) == FALSE ? SJME_JNI_FALSE : SJME_JNI_TRUE)

sjme_errorCode sjme_scritchui_gtk2_apiInit(
	sjme_attrInNotNull sjme_scritchui inState);

sjme_errorCode sjme_scritchui_gtk2_componentRepaint(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);
	
sjme_errorCode sjme_scritchui_gtk2_componentRevalidate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);

sjme_errorCode sjme_scritchui_gtk2_componentSetPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNullable sjme_scritchui_paintListenerFunc inListener,
	sjme_attrInNotNull sjme_scritchui_uiPaintable inPaint,
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd);

sjme_errorCode sjme_scritchui_gtk2_containerAdd(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiContainer inContainerData,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);
	
sjme_errorCode sjme_scritchui_gtk2_containerSetBounds(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);

sjme_errorCode sjme_scritchui_gtk2_loopExecuteLater(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything);

sjme_errorCode sjme_scritchui_gtk2_panelEnableFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus);

sjme_errorCode sjme_scritchui_gtk2_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel);

sjme_errorCode sjme_scritchui_gtk2_screens(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScreen* outScreens,
	sjme_attrInOutNotNull sjme_jint* inOutNumScreens);

sjme_errorCode sjme_scritchui_gtk2_windowContentMinimumSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);

sjme_errorCode sjme_scritchui_gtk2_windowNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow);
	
sjme_errorCode sjme_scritchui_gtk2_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible);
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_GTK2_H
}
		#undef SJME_CXX_SQUIRRELJME_GTK2_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_GTK2_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_GTK2_H */
