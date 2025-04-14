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
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/alloc.h"

sjme_errorCode sjme_scritchui_gtk2_lafElementColor(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrOutNotNull sjme_jint* outRGB,
	sjme_attrInValue sjme_scritchui_lafElementColorType elementColor)
{
	sjme_lpcstr styleName;
	GtkWidget* gtkWidget;
	GtkStyle* gtkStyle;
	GdkColor gdkColor;
	
	if (inState == NULL || outRGB == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Get context widget, if applicable. */
	gtkWidget = NULL;
	if (inContext != NULL)
		gtkWidget = inContext->common.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* Determine the color to use. */
	styleName = NULL;
	switch (elementColor)
	{
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_BACKGROUND:
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_BORDER:
			styleName = "bg_color";
			break;
			
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_FOREGROUND:
			styleName = "fg_color";
			break;
			
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_HIGHLIGHTED_BACKGROUND:
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_HIGHLIGHTED_BORDER:
			styleName = "selected_bg_color";
			break;
			
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_HIGHLIGHTED_FOREGROUND:
			styleName = "selected_fg_color";
			break;
		
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_FOCUS_BORDER:
			styleName = "button_active";
			break;
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Get the style to use. */
	if (gtkWidget != NULL)
		gtkStyle = gtk_widget_get_style(GTK_WIDGET(gtkWidget));
	else
		gtkStyle = gtk_widget_get_default_style();
	
	/* Obtain color. */
	memset(&gdkColor, 0, sizeof(gdkColor));
	if (gtk_style_lookup_color(gtkStyle,
		styleName, &gdkColor))
		*outRGB = ((gdkColor.red >> 8) << 16) |
			((gdkColor.green >> 8) << 8) |
			(gdkColor.blue >> 8);
	
	/* Not a valid color. */
	else
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
