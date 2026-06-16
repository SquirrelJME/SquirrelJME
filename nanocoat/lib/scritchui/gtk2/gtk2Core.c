/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/gtk2/gtk2Intern.h"
#include "lib/scritchui/core/core.h"
#include "sjme/dylib.h"

/** GTK Function set for Scritch UI. */
static const sjme_scritchui_implFunctions sjme_scritchui_gtkFunctions =
{
	sjme_sm(.driverName, "gtk2"),
	sjme_sm(.apiInit, sjme_scritchui_gtk2_apiInit),
	sjme_sm(.choiceItemInsert, NULL),
	sjme_sm(.choiceItemRemove, NULL),
	sjme_sm(.choiceItemSetEnabled, NULL),
	sjme_sm(.choiceItemSetImage, NULL),
	sjme_sm(.choiceItemSetSelected, NULL),
	sjme_sm(.choiceItemSetString, NULL),
	sjme_sm(.componentFocusGrab, sjme_scritchui_gtk2_componentFocusGrab),
	sjme_sm(.componentFocusHas, sjme_scritchui_gtk2_componentFocusHas),
	sjme_sm(.componentPosition, NULL),
	sjme_sm(.componentRepaint, sjme_scritchui_gtk2_componentRepaint),
	sjme_sm(.componentRevalidate, sjme_scritchui_gtk2_componentRevalidate),
	sjme_sm(.componentSetActivateListener, NULL),
	sjme_sm(.componentSetInputListener, sjme_scritchui_gtk2_componentSetInputListener),
	sjme_sm(.componentSetPaintListener, sjme_scritchui_gtk2_componentSetPaintListener),
	sjme_sm(.componentSetSizeListener, sjme_scritchui_gtk2_componentSetSizeListener),
	sjme_sm(.componentSetVisibleListener, NULL),
	sjme_sm(.componentSize, sjme_scritchui_gtk2_componentSize),
	sjme_sm(.containerAdd, sjme_scritchui_gtk2_containerAdd),
	sjme_sm(.containerRemove, sjme_scritchui_gtk2_containerRemove),
	sjme_sm(.containerSetBounds, sjme_scritchui_gtk2_containerSetBounds),
	sjme_sm(.labelSetString, sjme_scritchui_gtk2_labelSetString),
	sjme_sm(.lafDpiProject, NULL),
	sjme_sm(.lafElementColor, sjme_scritchui_gtk2_lafElementColor),
	sjme_sm(.lafMetric, NULL),
	sjme_sm(.listNew, NULL),
	sjme_sm(.loopExecute, NULL),
	sjme_sm(.loopExecuteLater, sjme_scritchui_gtk2_loopExecuteLater),
	sjme_sm(.loopExecuteWait, NULL),
	sjme_sm(.loopIterate, NULL),
	sjme_sm(.menuBarNew, sjme_scritchui_gtk2_menuBarNew),
	sjme_sm(.menuInsert, sjme_scritchui_gtk2_menuInsert),
	sjme_sm(.menuItemNew, sjme_scritchui_gtk2_menuItemNew),
	sjme_sm(.menuNew, sjme_scritchui_gtk2_menuNew),
	sjme_sm(.menuRemove, sjme_scritchui_gtk2_menuRemove),
	sjme_sm(.panelEnableFocus, sjme_scritchui_gtk2_panelEnableFocus),
	sjme_sm(.panelNew, sjme_scritchui_gtk2_panelNew),
	sjme_sm(.screenGetBounds, sjme_scritchui_gtk2_screenGetBounds),
	sjme_sm(.screens, sjme_scritchui_gtk2_screens),
	sjme_sm(.scrollPanelNew, sjme_scritchui_gtk2_scrollPanelNew),
	sjme_sm(.viewGetView, sjme_scritchui_gtk2_viewGetView),
	sjme_sm(.viewSetArea, sjme_scritchui_gtk2_viewSetArea),
	sjme_sm(.viewSetView, sjme_scritchui_gtk2_viewSetView),
	sjme_sm(.viewSetViewListener, sjme_scritchui_gtk2_viewSetViewListener),
	sjme_sm(.windowContentMinimumSize, 
		sjme_scritchui_gtk2_windowContentMinimumSize),
	sjme_sm(.windowGetFrame, sjme_scritchui_gtk2_windowGetFrame),
	sjme_sm(.windowNew, sjme_scritchui_gtk2_windowNew),
	sjme_sm(.windowSetCloseListener, 
		sjme_scritchui_gtk2_windowSetCloseListenerFunc),
	sjme_sm(.windowSetMenuBar, sjme_scritchui_gtk2_windowSetMenuBar),
	sjme_sm(.windowSetVisible, sjme_scritchui_gtk2_windowSetVisible),
};

/** Internal implementation functions. */
static const sjme_scritchui_implInternFunctions
	sjme_scritchui_gtk2InternFunctions =
{
	sjme_sm(.accelUpdate, sjme_scritchui_gtk2_intern_accelUpdate),
	sjme_sm(.checkError, sjme_scritchui_gtk2_intern_checkError),
	sjme_sm(.disconnectSignal, sjme_scritchui_gtk2_intern_disconnectSignal),
	sjme_sm(.reconnectSignal, sjme_scritchui_gtk2_intern_reconnectSignal),
	sjme_sm(.mapGtkToScritchKey, sjme_scritchui_gtk2_intern_mapGtkToScritchKey),
	sjme_sm(.mapGtkToScritchMod, sjme_scritchui_gtk2_intern_mapGtkToScritchMod),
	sjme_sm(.mapScritchToGtkKey, sjme_scritchui_gtk2_intern_mapScritchToGtkKey),
	sjme_sm(.mapScritchToGtkMod, sjme_scritchui_gtk2_intern_mapScritchToGtkMod),
	sjme_sm(.widgetInit, sjme_scritchui_gtk2_intern_widgetInit),
};

static sjme_thread_result sjme_scritchui_gtk2_loopMain(
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_scritchui state;
	GtkAccelGroup* accelGroup;
	int argc;
	char** argv;
	
	if (anything == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);
	
	/* Restore state. */
	state = (sjme_scritchui)anything;
	
	/* Enable debug options. */
	argc = 5;
	argv = sjme_alloca(argc * sizeof(*argv));
	argv[0] = "squirreljme";
	argv[1] = "--gtk-debug";
	argv[2] = "misc,modules,geometry";
	argv[3] = "--gdk-debug";
	argv[4] = "misc,events,draw,eventloop";
	
	/* Initialize, we do not care for the arguments. */
	gtk_init(&argc, &argv);
	
	/* Accelerator group, needed for menus. */
	accelGroup = gtk_accel_group_new();
	state->common.handle[SJME_SUI_GTK2_H_ACCELG] = accelGroup;
	
	/* Make sure the accelerator group does not just disappear. */
	g_object_ref(accelGroup);
	
	/* Need to call thread specific initializer? */
	/* Usually this is for binding a thread to a JavaVM. */
	if (state->loopThreadInit != NULL)
		state->loopThreadInit(state);
	
	/* Debug. */
	sjme_message("GTK Main Loop...");
	
	/* Before we go into the main loop, signal it is ready. */
	sjme_atomic_s(sjme_jint, &state->loopThreadReady, 1);
	
	/* Run main loop. */
	gtk_main();
	
	/* Success! */
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

sjme_errorCode SJME_SCRITCHUI_DYLIB_SYMBOL_DECLARE(gtk2)(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrInOutNotNull sjme_scritchui* outState,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable const sjme_scritchui_externalFunctions* externals,
	sjme_attrInNullable sjme_frontEndBindable* initFrontEnd)
{
	sjme_errorCode error;
	sjme_scritchui state;

	if (outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward to core call. */
	state = NULL;
	if (sjme_error_is(error = sjme_scritchui_core_apiInit(inPool,
		&state, &sjme_scritchui_gtkFunctions, loopExecute, externals,
		initFrontEnd)) || state == NULL)
		return sjme_error_default(error);
	
	/* Success! */
	*outState = state;
	return SJME_ERROR_NONE;
}

SJME_SCRITCHUI_DYLIB_API_EXPORT_SET(gtk2)

sjme_errorCode sjme_scritchui_gtk2_apiInit(
	sjme_attrInNotNull sjme_scritchui inState)
{
	sjme_errorCode error;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We set the minimum size in the expose event of the window. */
	inState->bugs.noContentSizeWhenVisible = SJME_JNI_TRUE;
	
	/* Internal functions to use specifically for GTK. */
	inState->implIntern = &sjme_scritchui_gtk2InternFunctions;
	
	/* This is a standard desktop. */
	inState->wmType = SJME_SCRITCHUI_WM_TYPE_STANDARD_DESKTOP;

	/* The number pad uses the calculator layout. */
	inState->platformFlags |= SJME_SCRITCHUI_LAF_PLATFORM_NUMPAD_CALC_LAYOUT;
		
	/* Debug. */
	sjme_message("GTK thread setup...");
	
	/* Start main GTK thread. */
	if (sjme_error_is(error = sjme_thread_new(
		&inState->loopThread,
		&inState->loopThreadId,
		sjme_scritchui_gtk2_loopMain, inState)) ||
		inState->loopThread == SJME_THREAD_NULL)
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
