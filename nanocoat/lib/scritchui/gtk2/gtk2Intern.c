/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdarg.h>
#include <gdk/gdk.h>
#include <gdk/gdkkeysyms.h>

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/gtk2/gtk2Intern.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/alloc.h"

/**
 * Internal key mappings.
 * 
 * @since 2024/07/24
 */
typedef struct sjme_scritchui_gtk2_intern_keyMap
{
	/** The GTK key. */
	guint gtk;
	
	/** The ScritchUI key. */
	sjme_jint scritch;
} sjme_scritchui_gtk2_intern_keyMap;

/** GTK Mapping table. */
static const sjme_scritchui_gtk2_intern_keyMap sjme_gtkKeyMap[] =
{
	{GDK_KEY_Alt_L, SJME_SCRITCHINPUT_KEY_ALT},
	{GDK_KEY_Alt_R, SJME_SCRITCHINPUT_KEY_ALT},
	{GDK_KEY_Caps_Lock, SJME_SCRITCHINPUT_KEY_CAPSLOCK},
	{GDK_KEY_Menu, SJME_SCRITCHINPUT_KEY_CONTEXT_MENU},
	{GDK_KEY_MenuKB, SJME_SCRITCHINPUT_KEY_CONTEXT_MENU},
	{GDK_KEY_MenuPB, SJME_SCRITCHINPUT_KEY_CONTEXT_MENU},
	{GDK_KEY_TopMenu, SJME_SCRITCHINPUT_KEY_CONTEXT_MENU},
	{GDK_KEY_Control_L, SJME_SCRITCHINPUT_KEY_CONTROL},
	{GDK_KEY_Control_R, SJME_SCRITCHINPUT_KEY_CONTROL},
	{GDK_KEY_Down, SJME_SCRITCHINPUT_KEY_DOWN},
	{GDK_KEY_KP_Down, SJME_SCRITCHINPUT_KEY_DOWN},
	{GDK_KEY_End, SJME_SCRITCHINPUT_KEY_END},
	{GDK_KEY_KP_End, SJME_SCRITCHINPUT_KEY_END},
	{GDK_KEY_F1, SJME_SCRITCHINPUT_KEY_F1},
	{GDK_KEY_F2, SJME_SCRITCHINPUT_KEY_F2},
	{GDK_KEY_F3, SJME_SCRITCHINPUT_KEY_F3},
	{GDK_KEY_F4, SJME_SCRITCHINPUT_KEY_F4},
	{GDK_KEY_F5, SJME_SCRITCHINPUT_KEY_F5},
	{GDK_KEY_F6, SJME_SCRITCHINPUT_KEY_F6},
	{GDK_KEY_F7, SJME_SCRITCHINPUT_KEY_F7},
	{GDK_KEY_F8, SJME_SCRITCHINPUT_KEY_F8},
	{GDK_KEY_F9, SJME_SCRITCHINPUT_KEY_F9},
	{GDK_KEY_F10, SJME_SCRITCHINPUT_KEY_F10},
	{GDK_KEY_F11, SJME_SCRITCHINPUT_KEY_F11},
	{GDK_KEY_F12, SJME_SCRITCHINPUT_KEY_F12},
	{GDK_KEY_F13, SJME_SCRITCHINPUT_KEY_F13},
	{GDK_KEY_F14, SJME_SCRITCHINPUT_KEY_F14},
	{GDK_KEY_F15, SJME_SCRITCHINPUT_KEY_F15},
	{GDK_KEY_F16, SJME_SCRITCHINPUT_KEY_F16},
	{GDK_KEY_F17, SJME_SCRITCHINPUT_KEY_F17},
	{GDK_KEY_F18, SJME_SCRITCHINPUT_KEY_F18},
	{GDK_KEY_F19, SJME_SCRITCHINPUT_KEY_F19},
	{GDK_KEY_F20, SJME_SCRITCHINPUT_KEY_F20},
	{GDK_KEY_F21, SJME_SCRITCHINPUT_KEY_F21},
	{GDK_KEY_F22, SJME_SCRITCHINPUT_KEY_F22},
	{GDK_KEY_F23, SJME_SCRITCHINPUT_KEY_F23},
	{GDK_KEY_F24, SJME_SCRITCHINPUT_KEY_F24},
	{GDK_KEY_Home, SJME_SCRITCHINPUT_KEY_HOME},
	{GDK_KEY_KP_Home, SJME_SCRITCHINPUT_KEY_HOME},
	{GDK_KEY_Insert, SJME_SCRITCHINPUT_KEY_INSERT},
	{GDK_KEY_KP_Insert, SJME_SCRITCHINPUT_KEY_INSERT},
	{GDK_KEY_Left, SJME_SCRITCHINPUT_KEY_LEFT},
	{GDK_KEY_KP_Left, SJME_SCRITCHINPUT_KEY_LEFT},
	{GDK_KEY_Meta_L, SJME_SCRITCHINPUT_KEY_META},
	{GDK_KEY_Meta_R, SJME_SCRITCHINPUT_KEY_META},
	{GDK_KEY_Num_Lock, SJME_SCRITCHINPUT_KEY_NUMLOCK},
	{GDK_KEY_KP_0, SJME_SCRITCHINPUT_KEY_NUMPAD_0},
	{GDK_KEY_KP_1, SJME_SCRITCHINPUT_KEY_NUMPAD_1},
	{GDK_KEY_KP_2, SJME_SCRITCHINPUT_KEY_NUMPAD_2},
	{GDK_KEY_KP_3, SJME_SCRITCHINPUT_KEY_NUMPAD_3},
	{GDK_KEY_KP_4, SJME_SCRITCHINPUT_KEY_NUMPAD_4},
	{GDK_KEY_KP_5, SJME_SCRITCHINPUT_KEY_NUMPAD_5},
	{GDK_KEY_KP_6, SJME_SCRITCHINPUT_KEY_NUMPAD_6},
	{GDK_KEY_KP_7, SJME_SCRITCHINPUT_KEY_NUMPAD_7},
	{GDK_KEY_KP_8, SJME_SCRITCHINPUT_KEY_NUMPAD_8},
	{GDK_KEY_KP_9, SJME_SCRITCHINPUT_KEY_NUMPAD_9},
	{GDK_KEY_KP_Decimal, SJME_SCRITCHINPUT_KEY_NUMPAD_DECIMAL},
	{GDK_KEY_KP_Divide, SJME_SCRITCHINPUT_KEY_NUMPAD_DIVIDE},
	{GDK_KEY_KP_Enter, SJME_SCRITCHINPUT_KEY_NUMPAD_ENTER},
	{GDK_KEY_KP_Subtract, SJME_SCRITCHINPUT_KEY_NUMPAD_MINUS},
	{GDK_KEY_KP_Multiply, SJME_SCRITCHINPUT_KEY_NUMPAD_MULTIPLY},
	{GDK_KEY_KP_Add, SJME_SCRITCHINPUT_KEY_NUMPAD_PLUS},
	{GDK_KEY_Page_Down, SJME_SCRITCHINPUT_KEY_PAGE_DOWN},
	{GDK_KEY_KP_Page_Down, SJME_SCRITCHINPUT_KEY_PAGE_DOWN},
	{GDK_KEY_Page_Up, SJME_SCRITCHINPUT_KEY_PAGE_UP},
	{GDK_KEY_KP_Page_Up, SJME_SCRITCHINPUT_KEY_PAGE_UP},
	{GDK_KEY_Pause, SJME_SCRITCHINPUT_KEY_PAUSE},
	{GDK_KEY_AudioPause, SJME_SCRITCHINPUT_KEY_PAUSE},
	{GDK_KEY_Print, SJME_SCRITCHINPUT_KEY_PRINTSCREEN},
	{GDK_KEY_3270_PrintScreen, SJME_SCRITCHINPUT_KEY_PRINTSCREEN},
	{GDK_KEY_Right, SJME_SCRITCHINPUT_KEY_RIGHT},
	{GDK_KEY_KP_Right, SJME_SCRITCHINPUT_KEY_RIGHT},
	{GDK_KEY_Scroll_Lock, SJME_SCRITCHINPUT_KEY_SCROLLLOCK},
	{GDK_KEY_Shift_L, SJME_SCRITCHINPUT_KEY_SHIFT},
	{GDK_KEY_Shift_R, SJME_SCRITCHINPUT_KEY_SHIFT},
	{GDK_KEY_Up, SJME_SCRITCHINPUT_KEY_UP},
	{GDK_KEY_KP_Up, SJME_SCRITCHINPUT_KEY_UP},
	{GDK_KEY_Return, SJME_SCRITCHINPUT_KEY_ENTER},
	{GDK_KEY_ISO_Enter, SJME_SCRITCHINPUT_KEY_ENTER},
	{GDK_KEY_3270_Enter, SJME_SCRITCHINPUT_KEY_ENTER},
	{GDK_KEY_BackSpace, SJME_SCRITCHINPUT_KEY_BACKSPACE},
	{GDK_KEY_Delete, SJME_SCRITCHINPUT_KEY_DELETE},
	{GDK_KEY_Tab, SJME_SCRITCHINPUT_KEY_TAB},
	{GDK_KEY_Escape, SJME_SCRITCHINPUT_KEY_ESCAPE},

	/* End. */
		{0, 0}
};

sjme_errorCode sjme_scritchui_gtk2_intern_accelUpdate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInNotNull GtkWidget* gtkWidget,
	sjme_attrInValue sjme_jboolean addAccel)
{
	GtkAccelGroup* gtkAccel;
	guint gtkKey, gtkMod;
	sjme_jint* key;
	sjme_jint* mod;
	sjme_scritchui_uiMenuItem menuItem;
	
	if (inState == NULL || inCommon == NULL || gtkWidget == NULL)
		return SJME_ERROR_NONE;
	
	/* Only work with menu items. */
	if (inCommon->type != SJME_SCRITCHUI_TYPE_MENU_ITEM)
		return SJME_ERROR_NONE;
	menuItem = (sjme_scritchui_uiMenuItem)inCommon;
	
	/* The accelerator group we are using is just the global state one. */
	gtkAccel = GTK_ACCEL_GROUP(inState->common.handle[SJME_SUI_GTK2_H_ACCELG]);
		
	/* Get value mappings. */
	key = &inCommon->intVals[SJME_SUI_GTK2_V_ACCELKEY];
	mod = &inCommon->intVals[SJME_SUI_GTK2_V_ACCELMOD];
	
	/* Add accelerator. */
	if (addAccel)
	{
		/* Are we not adding anything? */
		if (menuItem->accelKey == 0)
			return SJME_ERROR_NONE;
		
		/* Determine key set to use. */
		gtkKey = inState->implIntern->mapScritchToGtkKey(
			menuItem->accelKey);
		gtkMod = inState->implIntern->mapScritchToGtkMod(
			menuItem->accelMod);
		
		/* Associate. */
		gtk_widget_add_accelerator(gtkWidget,
			"activate",
			gtkAccel,
			gtkKey, gtkMod,
			GTK_ACCEL_VISIBLE);
		
		/* Store for later potential removal. */
		*key = gtkKey;
		*mod = gtkMod;
	}
	
	/* Remove accelerator. */
	else if (*key != 0 || *mod != 0)
	{
		/* Perform the remove. */
		gtk_widget_remove_accelerator(gtkWidget,
			gtkAccel,
			gtkKey, gtkMod);
		
		/* Clear old values. */
		*key = 0;
		*mod = 0;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_gtk2_intern_checkError(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_errorCode ifOkay)
{
	GError* err;
	
	if (inState == NULL)
		return SJME_ERROR_NONE;
	
	/* Get error state. */
	err = NULL;
	g_clear_error(&err);
	
	/* Was an error set? Try translating it... */
	if (err != NULL)
		return SJME_ERROR_NATIVE_WIDGET_FAILURE;
	
	/* All fine, use error code we desired. */
	return ifOkay;
}

sjme_errorCode sjme_scritchui_gtk2_intern_disconnectSignal(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull GtkWidget* inWidget,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoCore)
{
	sjme_jint i, n;
	sjme_list_sjme_intPointer* idList;
	
	if (inState == NULL || inWidget == NULL || infoCore == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Disconnect old signal? */
	if (infoCore->extra != 0)
	{
		/* Recover ID list. */
		idList = (sjme_list_sjme_intPointer*)infoCore->extra;
		
		/* Disconnect each signal. */
		for (i = 0, n = idList->length; i < n; i++)
			if (idList->elements[i] != 0)
			{
				gtk_signal_disconnect(inWidget, (gulong)idList->elements[i]);
				idList->elements[i] = 0;
			}
		
		/* Clear out and cleanup. */
		infoCore->extra = 0;
		infoCore->callback = NULL;
		memset(&infoCore->frontEnd, 0, sizeof(infoCore->frontEnd));
		
		/* Free ID list. */
		sjme_alloc_free(idList);
	}
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_jint sjme_scritchui_gtk2_intern_mapGtkToScritchKey(guint in)
{
	const sjme_scritchui_gtk2_intern_keyMap* map;
	guint32 unicode;
	
	/* Map to hard keys first for specific keys such as number pad keys */
	/* as they technically map to unicode, theoretically. */
	for (map = &sjme_gtkKeyMap[0]; map->scritch != 0; map++)
		if (in == map->gtk)
			return map->scritch;
	
	/* Is this a unicode key? Map to char if so... */
	unicode = gdk_keyval_to_unicode(in);
	if (unicode >= ' ' && unicode <= 0xFFFF)
		return unicode;
	
	/* Unknown key. */
	return SJME_SCRITCHINPUT_KEY_UNKNOWN;
}

sjme_jint sjme_scritchui_gtk2_intern_mapGtkToScritchMod(guint in)
{
	sjme_jint result;
	
	/* Map all modifiers. */
	result = 0;
	if (in & GDK_SHIFT_MASK)
		result |= SJME_SCRITCHINPUT_MODIFIER_SHIFT;
	if (in & GDK_MOD1_MASK)
		result |= SJME_SCRITCHINPUT_MODIFIER_ALT;
	if (in & GDK_MOD2_MASK)
		result |= SJME_SCRITCHINPUT_MODIFIER_CHR;
	if (in & GDK_CONTROL_MASK)
		result |= SJME_SCRITCHINPUT_MODIFIER_CTRL;
	if (in & (GDK_SUPER_MASK | GDK_HYPER_MASK | GDK_META_MASK))
		result |= SJME_SCRITCHINPUT_MODIFIER_COMMAND;
	
	return result;
}

guint sjme_scritchui_gtk2_intern_mapScritchToGtkKey(sjme_jint in)
{
	const sjme_scritchui_gtk2_intern_keyMap* map;
	guint unicode;
	
	/* Map to hard keys first for specific keys such as number pad keys */
	/* as they technically map to unicode, theoretically. */
	for (map = &sjme_gtkKeyMap[0]; map->scritch != 0; map++)
		if (in == map->scritch)
			return map->gtk;
	
	/* Is this a unicode key? Map to char if so... */
	unicode = gdk_unicode_to_keyval(in);
	if (unicode != 0x01000000 && unicode != (in | 0x01000000))
		return unicode;
	
	/* Unknown key. */
	return 0;
}

guint sjme_scritchui_gtk2_intern_mapScritchToGtkMod(sjme_jint in)
{
	guint result;
	
	/* Map all modifiers. */
	result = 0;
	if (in & SJME_SCRITCHINPUT_MODIFIER_SHIFT)
		result |= GDK_SHIFT_MASK;
	if (in & SJME_SCRITCHINPUT_MODIFIER_ALT)
		result |= GDK_MOD1_MASK;
	if (in & SJME_SCRITCHINPUT_MODIFIER_CHR)
		result |= GDK_MOD2_MASK;
	if (in & SJME_SCRITCHINPUT_MODIFIER_CTRL)
		result |= GDK_CONTROL_MASK;
	if (in & SJME_SCRITCHINPUT_MODIFIER_COMMAND)
		result |= GDK_META_MASK;
	
	return result;
}

sjme_errorCode sjme_scritchui_gtk2_intern_reconnectSignal(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull GtkWidget* inWidget,
	sjme_attrInNotNull sjme_pointer inOnWhat,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoCore,
	sjme_attrInNotNull sjme_pointer inListener,
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd,
	sjme_attrInNotNull GCallback inGtkCallback,
	sjme_attrInValue sjme_jboolean isAfter,
	sjme_attrInPositiveNonZero sjme_jint numSignals,
	...)
{
	sjme_errorCode error;
	sjme_jint i;
	sjme_list_sjme_intPointer* idList;
	va_list argList;
	sjme_lpcstr inSignal;
	
	if (inState == NULL || inWidget == NULL || inOnWhat == NULL ||
		infoCore == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Disconnect old signal? */
	if (infoCore->extra != 0)
		inState->implIntern->disconnectSignal(inState, inWidget, infoCore);
	
	/* Connect signal. */
	if (inListener != NULL)
	{
		/* Setup new list. */
		idList = NULL;
		if (sjme_error_is(error = sjme_list_alloc(inState->pool,
			numSignals, &idList, sjme_intPointer, 0)) || idList == NULL)
			return sjme_error_default(error);
		
		/* Fill in. */
		infoCore->extra = (sjme_intPointer)idList;
		if (sjme_error_is(error = inState->intern->setSimpleListener(inState,
			infoCore, inListener, copyFrontEnd)))
			return sjme_error_default(error);
		
		/* Start argument handling. */
		va_start(argList, numSignals);
		
		/* Connect all signals accordingly. */
		for (i = 0; i < numSignals; i++)
		{
			/* Get next signal item. */
			inSignal = va_arg(argList, sjme_lpcstr);
			
			/* Connect accordingly. */
			if (inSignal != NULL)
			{
				if (isAfter)
					infoCore->extra = g_signal_connect_after(inWidget,
						inSignal, inGtkCallback, inOnWhat);
				else
					infoCore->extra = g_signal_connect(inWidget, inSignal,
						inGtkCallback, inOnWhat);
			}
		}
			
		/* End argument handling. */
		va_end(argList);
	}
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_intern_widgetInit(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull GtkWidget* inWidget)
{
	if (inWidget == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Setup visibility events. */
	gtk_widget_add_events(inWidget,
		GDK_VISIBILITY_NOTIFY_MASK |
		GDK_STRUCTURE_MASK);
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
