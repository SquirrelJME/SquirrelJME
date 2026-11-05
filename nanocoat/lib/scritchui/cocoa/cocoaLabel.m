/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/cocoa/cocoa.h"
#include "lib/scritchui/cocoa/cocoaIntern.h"

sjme_errorCode sjme_scritchui_cocoa_labelSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInNullable sjme_lpcstr inString)
{
	SJMEMenu* cocoaMenu;
	SJMEMenuItem* cocoaMenuItem;
	SJMEWindow* cocoaWindow;

	if (inState == NULL || inCommon == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Depends on the type. */
	switch (inCommon->type)
	{
		case SJME_SCRITCHUI_TYPE_MENU:
			cocoaMenu = inCommon->handle[SJME_SUI_COCOA_H_NSMENU];

			[cocoaMenu setTitle:
				[NSString stringWithUTF8String:inString]];
			/* Fall through to set the menu item as well. */

		case SJME_SCRITCHUI_TYPE_MENU_ITEM:
			cocoaMenuItem = inCommon->handle[SJME_SUI_COCOA_H_NSMENUITEM];

			[cocoaMenuItem setTitle:
				[NSString stringWithUTF8String:inString]];
			break;

		case SJME_SCRITCHUI_TYPE_WINDOW:
			cocoaWindow = inCommon->handle[SJME_SUI_COCOA_H_NSVIEW];

			[cocoaWindow setTitle:
				[NSString stringWithUTF8String:inString]];
			[cocoaWindow setMiniwindowTitle:
				[NSString stringWithUTF8String:inString]];
			break;

		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
