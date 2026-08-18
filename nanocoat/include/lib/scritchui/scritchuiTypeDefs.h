/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Typedefs for ScritchUI.
 * 
 * @file
 * @since 2026/01/20
 */

#ifndef SJME_C_SQUIRRELJME_SCRITCHUITYPEDEFS_H
#define SJME_C_SQUIRRELJME_SCRITCHUITYPEDEFS_H

#include "sjme/stdTypes.h"
#include "lib/scritchui/scritchuiBasic.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SCRITCHUITYPEDEFS_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#pragma region(scritchui)

/**
 * ScritchUI API functions, implemented by a native library accordingly.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_apiFunctions sjme_scritchui_apiFunctions;

/**
 * ScritchUI implementation functions.
 * 
 * @since 2024/04/06
 */
typedef struct sjme_scritchui_implFunctions sjme_scritchui_implFunctions;

/**
 * Internal ScritchUI API functions.
 * 
 * @since 2024/04/15
 */
typedef struct sjme_scritchui_internFunctions sjme_scritchui_internFunctions;

/**
 * A chunk of loop queue slots.
 * 
 * @since 2024/12/31
 */
typedef struct sjme_scritchui_loopQueueChunk sjme_scritchui_loopQueueChunk;
	
/**
 * Common data structure shared by everything in ScritchUI.
 * 
 * @since 2024/04/02
 */
typedef struct sjme_scritchui_uiCommonBase sjme_scritchui_uiCommonBase;

/**
 * Common data pointer, which is shared by everything in ScritchUI.
 * 
 * @since 2024/04/02
 */
typedef sjme_scritchui_uiCommonBase* sjme_scritchui_uiCommon;

/**
 * Represents a choice of options such as those in a list.
 * 
 * @since 2024/12/25
 */
typedef struct sjme_scritchui_uiChoiceBase sjme_scritchui_uiChoiceBase;

/**
 * Represents a choice of options such as those in a list.
 * 
 * @since 2024/04/20
 */
typedef sjme_scritchui_uiChoiceBase* sjme_scritchui_uiChoice;

/**
 * Represents a single choice item.
 * 
 * @since 2024/04/25
 */
typedef struct sjme_scritchui_uiChoiceItemBase sjme_scritchui_uiChoiceItemBase;

/**
 * Represents a single choice item.
 * 
 * @since 2024/04/20
 */
typedef sjme_scritchui_uiChoiceItemBase* sjme_scritchui_uiChoiceItem;

/**
 * Base component structure within ScritchUI.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_uiComponentBase sjme_scritchui_uiComponentBase;

/**
 * Component within ScritchUI.
 * 
 * @since 2024/03/27
 */
typedef sjme_scritchui_uiComponentBase* sjme_scritchui_uiComponent;

/**
 * Represents a container which can contain other components.
 * 
 * @since 2024/12/25
 */
typedef struct sjme_scritchui_uiContainerBase sjme_scritchui_uiContainerBase;

/**
 * Represents a container which can contain other components.
 * 
 * @since 2024/04/20
 */
typedef sjme_scritchui_uiContainerBase* sjme_scritchui_uiContainer;

/**
 * Represents a component which can have a label.
 * 
 * @since 2024/07/22
 */
typedef struct sjme_scritchui_uiLabeledBase sjme_scritchui_uiLabeledBase;

/**
 * Represents a component which can have a label.
 * 
 * @since 2024/07/22
 */
typedef sjme_scritchui_uiLabeledBase* sjme_scritchui_uiLabeled;

/**
 * Base paintable for ScritchUI.
 * 
 * @since 2024/12/25
 */
typedef struct sjme_scritchui_uiPaintableBase sjme_scritchui_uiPaintableBase;

/**
 * Base paintable for ScritchUI.
 * 
 * @since 2024/04/06
 */
typedef sjme_scritchui_uiPaintableBase* sjme_scritchui_uiPaintable;

/**
 * A panel within ScritchUI.
 * 
 * @since 2024/07/28
 */
typedef struct sjme_scritchui_uiPanelBase sjme_scritchui_uiPanelBase;

/**
 * A panel within ScritchUI.
 * 
 * @since 2024/03/27
 */
typedef sjme_scritchui_uiPanelBase* sjme_scritchui_uiPanel;

/**
 * A list within ScritchUI.
 * 
 * @since 2024/07/16
 */
typedef struct sjme_scritchui_uiListBase* sjme_scritchui_uiList;

/**
 * A menu that has children.
 * 
 * @since 2024/07/23
 */
typedef struct sjme_scritchui_uiMenuHasChildrenBase
	sjme_scritchui_uiMenuHasChildrenBase;

/**
 * A ScritchUI menu kind
 * 
 * @since 2024/07/21
 */
typedef struct sjme_scritchui_uiMenuKindBase sjme_scritchui_uiMenuKindBase;

/**
 * A ScritchUI menu kind
 * 
 * @since 2024/07/21
 */
typedef sjme_scritchui_uiMenuKindBase* sjme_scritchui_uiMenuKind;

/**
 * A menu that has children.
 * 
 * @since 2024/07/23
 */
typedef sjme_scritchui_uiMenuHasChildrenBase* sjme_scritchui_uiMenuHasChildren;

/**
 * A menu that has a parent.
 * 
 * @since 2024/07/23
 */
typedef struct sjme_scritchui_uiMenuHasParentBase
	sjme_scritchui_uiMenuHasParentBase;

/**
 * A menu that has a parent.
 * 
 * @since 2024/07/23
 */
typedef sjme_scritchui_uiMenuHasParentBase* sjme_scritchui_uiMenuHasParent;

/**
 * A menu within ScritchUI.
 * 
 * @since 2024/07/21
 */
typedef struct sjme_scritchui_uiMenuBase* sjme_scritchui_uiMenu;

/**
 * A menu bar within ScritchUI.
 * 
 * @since 2024/07/21
 */
typedef struct sjme_scritchui_uiMenuBarBase* sjme_scritchui_uiMenuBar;

/**
 * A menu item within ScritchUI.
 * 
 * @since 2024/07/21
 */
typedef struct sjme_scritchui_uiMenuItemBase* sjme_scritchui_uiMenuItem;

/**
 * A single monitor screen on the display for ScritchUI.
 * 
 * @since 2024/12/25
 */
typedef struct sjme_scritchui_uiScreenBase sjme_scritchui_uiScreenBase;

/**
 * A single monitor screen on the display for ScritchUI.
 * 
 * @since 2024/04/06
 */
typedef sjme_scritchui_uiScreenBase* sjme_scritchui_uiScreen;

/**
 * A panel which can also be scrolled.
 * 
 * @since 2024/07/28
 */
typedef struct sjme_scritchui_uiScrollPanelBase* sjme_scritchui_uiScrollPanel;

/**
 * Viewport manager information for any widget that is a viewport.
 * 
 * @since 2024/07/29
 */
typedef struct sjme_scritchui_uiViewBase sjme_scritchui_uiViewBase;

/**
 * Viewport manager information for any widget that is a viewport.
 * 
 * @since 2024/07/29
 */
typedef sjme_scritchui_uiViewBase* sjme_scritchui_uiView;

/**
 * A window within ScritchUI.
 * 
 * @since 2024/12/25
 */
typedef struct sjme_scritchui_uiWindowBase sjme_scritchui_uiWindowBase;

/**
 * A window within ScritchUI.
 * 
 * @since 2024/03/27
 */
typedef sjme_scritchui_uiWindowBase* sjme_scritchui_uiWindow;

/**
 * Opaque internal implementation functions.
 * 
 * @since 2024/05/14
 */
typedef struct sjme_scritchui_implInternFunctions
	sjme_scritchui_implInternFunctions;
	
/**
 * Stores information for a single loop queue item.
 * 
 * @since 2024/12/31
 */
typedef struct sjme_scritchui_loopQueueItem sjme_scritchui_loopQueueItem;
	
#pragma endregion(scritchui)
#pragma region(scritchui_pencil)
	
/**
 * ScritchUI Pencil state.
 * 
 * @since 2024/05/01
 */
typedef struct sjme_scritchui_pencilBase sjme_scritchui_pencilBase; 
	
/**
 * ScritchUI Pencil state.
 * 
 * @since 2024/05/01
 */
typedef struct sjme_scritchui_pencilBase* sjme_scritchui_pencil;

/**
 * Font structure for ScritchUI Pencil.
 * 
 * @since 2024/05/12
 */
typedef struct sjme_scritchui_pencilFontBase* sjme_scritchui_pencilFont;

/**
 * A single link within a loaded/known font chain.
 * 
 * @since 2024/06/10
 */
typedef struct sjme_scritchui_pencilFontLink sjme_scritchui_pencilFontLink;

/**
 * Font iteration state.
 *
 * @since 2026/04/11
 */
typedef struct sjme_scritchui_fontIterateStep sjme_scritchui_fontIterateStep;

/**
 * Utility functions to help in implementations or otherwise perform some
 * common logic.
 * 
 * @since 2024/07/12
 */
typedef struct sjme_scritchui_pencilUtilFunctions
	sjme_scritchui_pencilUtilFunctions;

/**
 * Functions which are used to lock and unlock access to the backing pencil
 * buffer, if applicable.
 * 
 * @since 2024/07/08
 */
typedef struct sjme_scritchui_pencilLockFunctions
	sjme_scritchui_pencilLockFunctions;

/**
 * Pencil font with parameters.
 * 
 * @since 2026/01/20
 */
typedef struct sjme_scritchui_pencilFontWithParam 
	sjme_scritchui_pencilFontWithParam;

#pragma endregion(scritchui_pencil)
#pragma region(scritchui_listener)
	
/** Declares a ScritchUI listener set. */
#define SJME_SCRITCHUI_LISTENER_TYPEDEF(what) \
	typedef struct SJME_TOKEN_PASTE(sjme_scritchui_listener_, what) \
		SJME_TOKEN_PASTE(sjme_scritchui_listener_, what)

/** Void listener. */
SJME_SCRITCHUI_LISTENER_TYPEDEF(void);

/** Activate choice item. */
SJME_SCRITCHUI_LISTENER_TYPEDEF(activate);

/** Choice items updated, before or after. */
SJME_SCRITCHUI_LISTENER_TYPEDEF(valueUpdate);

/** Close listener. */
SJME_SCRITCHUI_LISTENER_TYPEDEF(close);

/** Input listener. */
SJME_SCRITCHUI_LISTENER_TYPEDEF(input);

/** Menu item is activated. */
SJME_SCRITCHUI_LISTENER_TYPEDEF(menuItemActivate);

/** Paint listener. */
SJME_SCRITCHUI_LISTENER_TYPEDEF(paint);

/** Size listener. */
SJME_SCRITCHUI_LISTENER_TYPEDEF(size);

/** Suggest size listener. */
SJME_SCRITCHUI_LISTENER_TYPEDEF(sizeSuggest);

/** View listener. */
SJME_SCRITCHUI_LISTENER_TYPEDEF(view);

/** Visible listener. */
SJME_SCRITCHUI_LISTENER_TYPEDEF(visible);
	
#pragma endregion(scritchui_listener)
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUITYPEDEFS_H
}
#undef SJME_CXX_SQUIRRELJME_SCRITCHUITYPEDEFS_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUITYPEDEFS_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SCRITCHUITYPEDEFS_H */
