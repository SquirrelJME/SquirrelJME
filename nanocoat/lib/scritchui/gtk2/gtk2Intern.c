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
	guint32 unicode;
	
	/* Map to hard keys first for specific keys such as number pad keys */
	/* as they technically map to unicode, theoretically. */
	switch (in)
	{
		case GDK_KEY_Alt_L:
		case GDK_KEY_Alt_R:
			return SJME_SCRITCHINPUT_KEY_ALT;
		case GDK_KEY_Caps_Lock:
			return SJME_SCRITCHINPUT_KEY_CAPSLOCK;
		case GDK_KEY_Menu:
		case GDK_KEY_MenuKB:
		case GDK_KEY_MenuPB:
		case GDK_KEY_TopMenu:
			return SJME_SCRITCHINPUT_KEY_CONTEXT_MENU;
		case GDK_KEY_Control_L:
		case GDK_KEY_Control_R:
			return SJME_SCRITCHINPUT_KEY_CONTROL;
		case GDK_KEY_Down:
		case GDK_KEY_KP_Down:
			return SJME_SCRITCHINPUT_KEY_DOWN;
		case GDK_KEY_End:
		case GDK_KEY_KP_End:
			return SJME_SCRITCHINPUT_KEY_END;
		case GDK_KEY_F1:
			return SJME_SCRITCHINPUT_KEY_F1;
		case GDK_KEY_F2:
			return SJME_SCRITCHINPUT_KEY_F2;
		case GDK_KEY_F3:
			return SJME_SCRITCHINPUT_KEY_F3;
		case GDK_KEY_F4:
			return SJME_SCRITCHINPUT_KEY_F4;
		case GDK_KEY_F5:
			return SJME_SCRITCHINPUT_KEY_F5;
		case GDK_KEY_F6:
			return SJME_SCRITCHINPUT_KEY_F6;
		case GDK_KEY_F7:
			return SJME_SCRITCHINPUT_KEY_F7;
		case GDK_KEY_F8:
			return SJME_SCRITCHINPUT_KEY_F8;
		case GDK_KEY_F9:
			return SJME_SCRITCHINPUT_KEY_F9;
		case GDK_KEY_F10:
			return SJME_SCRITCHINPUT_KEY_F10;
		case GDK_KEY_F11:
			return SJME_SCRITCHINPUT_KEY_F11;
		case GDK_KEY_F12:
			return SJME_SCRITCHINPUT_KEY_F12;
		case GDK_KEY_F13:
			return SJME_SCRITCHINPUT_KEY_F13;
		case GDK_KEY_F14:
			return SJME_SCRITCHINPUT_KEY_F14;
		case GDK_KEY_F15:
			return SJME_SCRITCHINPUT_KEY_F15;
		case GDK_KEY_F16:
			return SJME_SCRITCHINPUT_KEY_F16;
		case GDK_KEY_F17:
			return SJME_SCRITCHINPUT_KEY_F17;
		case GDK_KEY_F18:
			return SJME_SCRITCHINPUT_KEY_F18;
		case GDK_KEY_F19:
			return SJME_SCRITCHINPUT_KEY_F19;
		case GDK_KEY_F20:
			return SJME_SCRITCHINPUT_KEY_F20;
		case GDK_KEY_F21:
			return SJME_SCRITCHINPUT_KEY_F21;
		case GDK_KEY_F22:
			return SJME_SCRITCHINPUT_KEY_F22;
		case GDK_KEY_F23:
			return SJME_SCRITCHINPUT_KEY_F23;
		case GDK_KEY_F24:
			return SJME_SCRITCHINPUT_KEY_F24;
		case GDK_KEY_Home:
		case GDK_KEY_KP_Home:
			return SJME_SCRITCHINPUT_KEY_HOME;
		case GDK_KEY_Insert:
		case GDK_KEY_KP_Insert:
			return SJME_SCRITCHINPUT_KEY_INSERT;
		case GDK_KEY_Left:
		case GDK_KEY_KP_Left:
			return SJME_SCRITCHINPUT_KEY_LEFT;
		case GDK_KEY_Meta_L:
		case GDK_KEY_Meta_R:
			return SJME_SCRITCHINPUT_KEY_META;
		case GDK_KEY_Num_Lock:
			return SJME_SCRITCHINPUT_KEY_NUMLOCK;
		case GDK_KEY_KP_0:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_0;
		case GDK_KEY_KP_1:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_1;
		case GDK_KEY_KP_2:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_2;
		case GDK_KEY_KP_3:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_3;
		case GDK_KEY_KP_4:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_4;
		case GDK_KEY_KP_5:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_5;
		case GDK_KEY_KP_6:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_6;
		case GDK_KEY_KP_7:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_7;
		case GDK_KEY_KP_8:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_8;
		case GDK_KEY_KP_9:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_9;
		case GDK_KEY_KP_Decimal:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_DECIMAL;
		case GDK_KEY_KP_Divide:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_DIVIDE;
		case GDK_KEY_KP_Enter:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_ENTER;
		case GDK_KEY_KP_Subtract:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_MINUS;
		case GDK_KEY_KP_Multiply:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_MULTIPLY;
		case GDK_KEY_KP_Add:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_PLUS;
		case GDK_KEY_Page_Down:
		case GDK_KEY_KP_Page_Down:
			return SJME_SCRITCHINPUT_KEY_PAGE_DOWN;
		case GDK_KEY_Page_Up:
		case GDK_KEY_KP_Page_Up:
			return SJME_SCRITCHINPUT_KEY_PAGE_UP;
		case GDK_KEY_Pause:
		case GDK_KEY_AudioPause:
			return SJME_SCRITCHINPUT_KEY_PAUSE;
		case GDK_KEY_Print:
		case GDK_KEY_3270_PrintScreen:
			return SJME_SCRITCHINPUT_KEY_PRINTSCREEN;
		case GDK_KEY_Right:
		case GDK_KEY_KP_Right:
			return SJME_SCRITCHINPUT_KEY_RIGHT;
		case GDK_KEY_Scroll_Lock:
			return SJME_SCRITCHINPUT_KEY_SCROLLLOCK;
		case GDK_KEY_Shift_L:
		case GDK_KEY_Shift_R:
			return SJME_SCRITCHINPUT_KEY_SHIFT;
		case GDK_KEY_Up:
		case GDK_KEY_KP_Up:
			return SJME_SCRITCHINPUT_KEY_UP;
		
		case GDK_KEY_Return:
		case GDK_KEY_ISO_Enter:
		case GDK_KEY_3270_Enter:
			return SJME_SCRITCHINPUT_KEY_ENTER;
		
		case GDK_KEY_BackSpace:
			return SJME_SCRITCHINPUT_KEY_BACKSPACE;
		
		case GDK_KEY_Delete:
			return SJME_SCRITCHINPUT_KEY_DELETE;
		
		case GDK_KEY_Tab:
			return SJME_SCRITCHINPUT_KEY_TAB;
		
		case GDK_KEY_Escape:
			return SJME_SCRITCHINPUT_KEY_ESCAPE;
	}
	
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
}

guint sjme_scritchui_gtk2_intern_mapScritchToGtkMod(sjme_jint in)
{
}

sjme_errorCode sjme_scritchui_gtk2_intern_reconnectSignal(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull GtkWidget* inWidget,
	sjme_attrInNotNull sjme_pointer inOnWhat,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoCore,
	sjme_attrInNotNull sjme_pointer inListener,
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd,
	sjme_attrInNotNull GCallback inGtkCallback,
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
		infoCore->callback = inListener;
		if (copyFrontEnd != NULL)
			memmove(&infoCore->frontEnd, copyFrontEnd,
				sizeof(*copyFrontEnd));
		
		/* Start argument handling. */
		va_start(argList, numSignals);
		
		/* Connect all signals accordingly. */
		for (i = 0; i < numSignals; i++)
		{
			/* Get next signal item. */
			inSignal = va_arg(argList, sjme_lpcstr);
			
			/* Connect accordingly. */
			if (inSignal != NULL)
				infoCore->extra = g_signal_connect(inWidget, inSignal,
					inGtkCallback, inOnWhat);
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
