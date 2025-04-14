/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/win32/win32.h"
#include "lib/scritchui/win32/win32Intern.h"

sjme_errorCode sjme_scritchui_win32_labelSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInNullable sjme_lpcstr inString)
{
	sjme_errorCode error;
	HWND window;
	HMENU hMenu;
	sjme_scritchui_uiMenuHasParent parent;
	sjme_jint index;
	
	if (inState == NULL || inCommon == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Depends on the type. */
	SetLastError(0);
	switch (inCommon->type)
	{
			/* We need to change the menu we are in. */
		case SJME_SCRITCHUI_TYPE_MENU:
		case SJME_SCRITCHUI_TYPE_MENU_ITEM:
			/* Get parent item. */
			parent = NULL;
			if (sjme_error_is(error = inState->intern->getMenuHasParent(
				inState, (sjme_scritchui_uiMenuKind)inCommon,
				&parent)) || parent == NULL)
				return sjme_error_default(error);
			if (parent->parent != NULL)
			{
				/* This will always be a HMENU. */
				hMenu = parent->parent->common.handle[SJME_SUI_WIN32_H_HMENU];
				
				/* What is the index of our menu? */
				index = ((sjme_scritchui_uiMenuKind)inCommon)->index;
				
				/* Change menu text. */
				ModifyMenu(hMenu,
					index,
					MF_BYPOSITION,
					(UINT_PTR)(inString != NULL ? inString : ""),
					MF_STRING);
			}
			break;
		
			/* This is simple enough, nothing fancy. */
		case SJME_SCRITCHUI_TYPE_WINDOW:
			window = inCommon->handle[SJME_SUI_WIN32_H_HWND];
			SetWindowText(window, (inString != NULL ? inString :
				"SquirrelJME"));
			break;
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}
