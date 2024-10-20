/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI Library Header.
 * 
 * @since 2024/03/27
 */

#ifndef SQUIRRELJME_SCRITCHUI_H
#define SQUIRRELJME_SCRITCHUI_H

#include "sjme/config.h"
#include "sjme/multithread.h"
#include "sjme/tokenUtils.h"
#include "sjme/gfxConst.h"
#include "sjme/stdTypes.h"
#include "sjme/list.h"
#include "sjme/native.h"
#include "lib/scritchinput/scritchinput.h"
#include "sjme/alloc.h"
#include "sjme/dylib.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUI_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Represents the type that this is.
 * 
 * @since 2024/04/02
 */
typedef enum sjme_scritchui_uiType
{
	/** Reserved. */
	SJME_SCRITCHUI_TYPE_RESERVED,
	
	/** Font. */
	SJME_SCRITCHUI_TYPE_FONT,
	
	/** List. */
	SJME_SCRITCHUI_TYPE_LIST,
	
	/** Menu. */
	SJME_SCRITCHUI_TYPE_MENU,
	
	/** Menu bar. */
	SJME_SCRITCHUI_TYPE_MENU_BAR,
	
	/** Menu item. */
	SJME_SCRITCHUI_TYPE_MENU_ITEM,
	
	/** Panel. */
	SJME_SCRITCHUI_TYPE_PANEL,
	
	/** Pencil. */
	SJME_SCRITCHUI_TYPE_PENCIL,
	
	/** Root state. */
	SJME_SCRITCHUI_TYPE_ROOT_STATE,
	
	/** Screen. */
	SJME_SCRITCHUI_TYPE_SCREEN,
	
	/** Scroll Panel. */
	SJME_SCRITCHUI_TYPE_SCROLL_PANEL,
	
	/** Window. */
	SJME_SCRITCHUI_TYPE_WINDOW,
	
	/** The number of possible types. */
	SJME_NUM_SCRITCHUI_UI_TYPES
} sjme_scritchui_uiType;

/** Generic cast check. */
#define SJME_SUI_CAST(uiType, type, v) \
	((type)sjme_scritchui_checkCast((type), (v)))

/** Common type. */
#define SJME_SUI_CAST_COMMON(v) \
	((sjme_scritchui_uiCommon)(v))

/** Check cast to menu kind. */
#define SJME_SUI_CAST_MENU_KIND(v) \
	((sjme_scritchui_uiMenuKind)sjme_scritchui_checkCast_menuKind((v)))

/** Check cast to component kind. */
#define SJME_SUI_CAST_COMPONENT(v) \
	((sjme_scritchui_uiComponent)sjme_scritchui_checkCast_component((v)))

/** Check cast to panel. */
#define SJME_SUI_CAST_PANEL(v) \
	SJME_SUI_CAST(SJME_SCRITCHUI_TYPE_PANEL, \
	sjme_scritchui_uiPanel, (v))

/** Check cast to menu. */
#define SJME_SUI_CAST_MENU(v) \
	SJME_SUI_CAST(SJME_SCRITCHUI_TYPE_MENU, \
	sjme_scritchui_uiMenu, (v))

/** Check cast to menu bar. */
#define SJME_SUI_CAST_MENU_BAR(v) \
	SJME_SUI_CAST(SJME_SCRITCHUI_TYPE_MENU_BAR, \
	sjme_scritchui_uiMenuBar, (v))

/** Check cast to menu item. */
#define SJME_SUI_CAST_MENU_ITEM(v) \
	SJME_SUI_CAST(SJME_SCRITCHUI_TYPE_MENU_Item, \
	sjme_scritchui_uiMenuItem, (v))

/**
 * An opaque native handle.
 * 
 * @since 2024/04/02
 */
typedef sjme_pointer sjme_scritchui_handle;

/**
 * API Flags for ScritchUI.
 * 
 * @since 2024/03/29
 */
typedef enum sjme_scritchui_apiFlag
{
	/** Only panels are supported for this interface. */
	SJME_SCRITCHUI_API_FLAG_PANEL_ONLY = 1,
} sjme_scritchui_apiFlag;

/**
 * Which type of screen update has occurred?
 * 
 * @since 2024/04/09
 */
typedef enum sjme_scritchui_screenUpdateType
{
	/** Unknown. */
	SJME_SCRITCHUI_SCREEN_UPDATE_UNKNOWN,
	
	/** New screen. */
	SJME_SCRITCHUI_SCREEN_UPDATE_NEW,
	
	/** Deleted screen. */
	SJME_SCRITCHUI_SCREEN_UPDATE_DELETED,
	
	/** Updated screen (resolution, color, etc.) */
	SJME_SCRITCHUI_SCREEN_UPDATE_CHANGED,
	
	/** The number of update types. */
	SJME_SCRITCHUI_NUM_SCREEN_UPDATE
} sjme_scritchui_screenUpdateType;

/**
 * The type of window manager that is used.
 * 
 * @since 2024/04/15
 */
typedef enum sjme_scritchui_windowManagerType
{
	/** One frame per screen. */
	SJME_SCRITCHUI_WM_TYPE_ONE_FRAME_PER_SCREEN = 0,
	
	/** Standard desktop interface. */
	SJME_SCRITCHUI_WM_TYPE_STANDARD_DESKTOP = 1,
	
	/** The number of window manager types. */
	SJME_SCRITCHUI_NUM_WM_TYPES
} sjme_scritchui_windowManagerType;

/**
 * Font style for pencil fonts.
 * 
 * @since 2024/06/13
 */
typedef enum sjme_scritchui_pencilFontStyle
{
	/** Bold text. */
	SJME_SCRITCHUI_PENCIL_FONT_STYLE_BOLD = 1,
	
	/** Italic (slanted) text. */
	SJME_SCRITCHUI_PENCIL_FONT_STYLE_ITALIC = 2,
	
	/** Underlined text. */
	SJME_SCRITCHUI_PENCIL_FONT_STYLE_UNDERLINED = 4,
	
	/** All styles. */
	SJME_SCRITCHUI_PENCIL_FONT_STYLE_ALL =
		SJME_SCRITCHUI_PENCIL_FONT_STYLE_BOLD |
		SJME_SCRITCHUI_PENCIL_FONT_STYLE_ITALIC |
		SJME_SCRITCHUI_PENCIL_FONT_STYLE_UNDERLINED,
} sjme_scritchui_pencilFontStyle;

/**
 * Represents the type of choice that a choice selection may be.
 * 
 * @since 2024/07/17
 */
typedef enum sjme_scritchui_choiceType
{
	/** Only one element may be selected at a time. */
	SJME_SCRITCHUI_CHOICE_TYPE_EXCLUSIVE = 0,
	
	/**
	 * The item that is focused is always the only one selected, pressing an
	 * action key (like enter/space) will activate the item.
	 */
	SJME_SCRITCHUI_CHOICE_TYPE_IMPLICIT = 1,
	
	/** Any number of items may be selected. */
	SJME_SCRITCHUI_CHOICE_TYPE_MULTIPLE = 2,
	
	/** The number of choice types. */
	SJME_SCRITCHUI_NUM_CHOICE_TYPES = 3,
} sjme_scritchui_choiceType;

/**
 * The element color type for look and feel.
 * 
 * @since 2024/07/27 
 */
typedef enum sjme_scritchui_lafElementColorType
{
	/** Background color. */
	SJME_SCRITCHUI_LAF_ELEMENT_COLOR_BACKGROUND = 0,
	
	/** Border color. */
	SJME_SCRITCHUI_LAF_ELEMENT_COLOR_BORDER = 1,
	
	/** Foreground color. */
	SJME_SCRITCHUI_LAF_ELEMENT_COLOR_FOREGROUND = 2,
	
	/** Highlighted background color. */
	SJME_SCRITCHUI_LAF_ELEMENT_COLOR_HIGHLIGHTED_BACKGROUND = 3,
	
	/** Highlighted border color. */
	SJME_SCRITCHUI_LAF_ELEMENT_COLOR_HIGHLIGHTED_BORDER = 4,
	
	/** Highlighted foreground color. */
	SJME_SCRITCHUI_LAF_ELEMENT_COLOR_HIGHLIGHTED_FOREGROUND = 5,
	
	/** Focus border. */
	SJME_SCRITCHUI_LAF_ELEMENT_COLOR_FOCUS_BORDER = 6,
	
	/** Panel background color. */
	SJME_SCRITCHUI_LAF_ELEMENT_COLOR_PANEL_BACKGROUND = 7,
	
	/** Panel foreground color. */
	SJME_SCRITCHUI_LAF_ELEMENT_COLOR_PANEL_FOREGROUND = 8,
	
	/** The number of element colors. */
	SJME_SCRITCHUI_NUM_LAF_ELEMENT_COLOR = 9,
} sjme_scritchui_lafElementColorType;

/**
 * Represents a point.
 * 
 * @since 2024/07/12
 */
typedef struct sjme_scritchui_point
{
	/** X coordinate. */
	sjme_jint x;
	
	/** Y coordinate. */
	sjme_jint y;
} sjme_scritchui_point;

/**
 * Represents a line.
 * 
 * @since 2024/07/12
 */
typedef struct sjme_scritchui_line
{
	/** Starting point. */
	sjme_scritchui_point s;
	
	/** End point. */
	sjme_scritchui_point e;
} sjme_scritchui_line;

/**
 * Represents a dimension.
 * 
 * @since 2024/07/12
 */
typedef struct sjme_scritchui_dim
{
	/** The width. */
	sjme_jint width;
	
	/** The height. */
	sjme_jint height;
} sjme_scritchui_dim;

/**
 * Represents a rectangle.
 * 
 * @since 2024/04/26
 */
typedef struct sjme_scritchui_rect
{
	/** The starting point of the rectangle. */
	sjme_scritchui_point s;
	
	/** The dimension of the rect. */
	sjme_scritchui_dim d;
} sjme_scritchui_rect;

/**
 * ScritchUI state.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_stateBase* sjme_scritchui;

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
 * @since 2024/04/20
 */
typedef struct sjme_scritchui_uiChoiceBase* sjme_scritchui_uiChoice;

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
typedef struct sjme_scritchui_uiChoiceItemBase* sjme_scritchui_uiChoiceItem;

/**
 * Component within ScritchUI.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_uiComponentBase* sjme_scritchui_uiComponent;

/**
 * Represents a container which can contain other components.
 * 
 * @since 2024/04/20
 */
typedef struct sjme_scritchui_uiContainerBase* sjme_scritchui_uiContainer;

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
 * @since 2024/04/06
 */
typedef struct sjme_scritchui_uiPaintableBase* sjme_scritchui_uiPaintable;

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
 * @since 2024/04/06
 */
typedef struct sjme_scritchui_uiScreenBase* sjme_scritchui_uiScreen;

/** A list of screens. */
SJME_LIST_DECLARE(sjme_scritchui_uiScreen, 0);

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
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_uiWindowBase* sjme_scritchui_uiWindow;

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

/** Arguments to pass for setting of listeners. */
#define SJME_SCRITCHUI_SET_LISTENER_ARGS(what) \
	sjme_attrInNullable SJME_TOKEN_PASTE3(sjme_scritchui_, what, \
		ListenerFunc) inListener, \
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd

/**
 * Listener that is called when an item is activated.
 * 
 * @param inState The input state.
 * @param inComponent The item which was activated.
 * @return Any resultant error, if any.
 * @since 2024/07/16
 */
typedef sjme_errorCode (*sjme_scritchui_activateListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);

/**
 * Listener that is called when a window closes.
 * 
 * @param inState The input state.
 * @param inWindow The window being closed.
 * @return Any resultant error, @c SJME_ERROR_CANCEL_WINDOW_CLOSE is handled
 * specifically in that it will not be treated as an error however normal
 * application exit will not happen.
 * @since 2024/05/13
 */
typedef sjme_errorCode (*sjme_scritchui_closeListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow);

/**
 * Listener for input events.
 * 
 * @param inState The input state.
 * @param inComponent The component this event is for.
 * @param inEvent The event which occurred.
 * @return Any resultant error, if any.
 * @since 2024/06/29
 */
typedef sjme_errorCode (*sjme_scritchui_inputListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchinput_event* inEvent);

/**
 * This is called when a menu item has been activated.
 * 
 * @param inState The input state.
 * @param inWindow The window this is activating under.
 * @param activatedItem The menu item that was activated.
 * @return Any resultant error, if any.
 * @since 2024/07/30
 */
typedef sjme_errorCode (*sjme_scritchui_menuItemActivateListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind activatedItem);

/**
 * Callback that is used to draw the given component.
 *
 * @param inState The ScritchUI state.
 * @param inComponent The component to draw on.
 * @param g The graphics used for drawing.
 * @param sw Surface width.
 * @param sh Surface height.
 * @param special Special value for painting, may be @c 0 or any
 * other value if it is meaningful to what is being painted.
 * @return Any error as required.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_paintListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint sw,
	sjme_attrInPositive sjme_jint sh,
	sjme_attrInValue sjme_jint special);

/**
 * Listener callback for when a screen has been queried or it has been
 * updated.
 * 
 * @param inState The input state.
 * @param updateType The type of update this is for.
 * @param inScreen The screen that has been updated.
 * @return Any error code if applicable.
 * @since 2024/04/09
 */
typedef sjme_errorCode (*sjme_scritchui_screenListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_scritchui_screenUpdateType updateType,
	sjme_attrInNotNull sjme_scritchui_uiScreen inScreen);

/**
 * Listener that is called when the size of a component changes.
 * 
 * @param inState The input state.
 * @param inComponent The component that was resized.
 * @param newWidth The new component width.
 * @param newHeight The new component height.
 * @return On any error if applicable.
 * @since 2024/04/26
 */
typedef sjme_errorCode (*sjme_scritchui_sizeListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositiveNonZero sjme_jint newWidth,
	sjme_attrInPositiveNonZero sjme_jint newHeight);

/**
 * Listener for views so that a sub-component can suggest a size that it
 * could be.
 * 
 * @param inState The ScritchUI state.
 * @param inView The view this is in.
 * @param subComponent The component that is suggesting a size.
 * @param subDim The size of the sub-component.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_sizeSuggestListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inView,
	sjme_attrInNotNull sjme_scritchui_uiComponent subComponent,
	sjme_attrInNotNull const sjme_scritchui_dim* subDim);

/**
 * Listener that is called before and after the state within a component
 * has changed, when @c isAfterUpdate is @c SJME_JNI_FALSE then the component
 * is about to be updated.
 * 
 * @param inState The input state.
 * @param inComponent The component where this event occurred.
 * @param isAfterUpdate Is this after the update has occurred?
 * @return Any resultant error, if any.
 * @since 2024/07/16
 */
typedef sjme_errorCode (*sjme_scritchui_valueUpdateListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInValue sjme_jboolean isAfterUpdate);

/**
 * Listener for view rectangle changes.
 * 
 * @param inState The input state.
 * @param inComponent The component that triggered this.
 * @param inViewRect The new view rectangle.
 * @return Any resultant error, if any.
 * @since 2024/07/28
 */
typedef sjme_errorCode (*sjme_scritchui_viewListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_rect* inViewRect);

/**
 * Listener for changes in if a component becomes visible or not.
 * 
 * @param inState The input state.
 * @param inComponent The component which has its visibility changed.
 * @param fromVisible The previous visible state.
 * @param toVisible The current visible state.
 * @return Any resultant error, if any.
 * @since 2024/06/28
 */
typedef sjme_errorCode (*sjme_scritchui_visibleListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInValue sjme_jboolean fromVisible,
	sjme_attrInValue sjme_jboolean toVisible);

/** Void listener function. */
typedef sjme_errorCode (*sjme_scritchui_voidListenerFunc)(void);

/**
 * Obtains the flags which describe the interface.
 * 
 * @param inState The input ScritchUI state.
 * @param outFlags The output flags for this interface.
 * @return Any error code if applicable.
 * @since 2024/03/29
 */
typedef sjme_errorCode (*sjme_scritchui_apiFlagsFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_jint outFlags);

/**
 * Initializes the native UI interface needed by ScritchUI.
 * 
 * @param inPool The allocation pool to use.
 * @param outState The resultant state.
 * @param inImplFunc The implementation functions to use.
 * @param loopExecute Optional callback for loop execution, may be @c NULL ,
 * the passed argument is always the state.
 * @param initFrontEnd Optional initial front end data.
 * @return Any error code if applicable.
 * @since 2024/03/27
 */
typedef sjme_errorCode (*sjme_scritchui_apiInitFunc)(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInOutNotNull sjme_scritchui* outState,
	sjme_attrInNotNull const sjme_scritchui_implFunctions* inImplFunc,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd);

/**
 * Gets the first selected index of a choice or otherwise @c -1 .
 * 
 * @param inState The input state.
 * @param inComponent The choice to read from.
 * @param outIndex The resultant index.
 * @return Any resultant error, if any.
 * @since 2024/07/28
 */
typedef sjme_errorCode (*sjme_scritchui_choiceGetSelectedIndexFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jint* outIndex);

/**
 * Gets the specified item template.
 * 
 * @param inState The input state.
 * @param inComponent The choice to read from.
 * @param atIndex The index to obtain the template of.
 * @param inItemTemplate A copy of the item template.
 * @return Any resultant error, if any.
 * @since 2024/07/17
 */
typedef sjme_errorCode (*sjme_scritchui_choiceItemGetFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrOutNotNull sjme_scritchui_uiChoiceItem outItemTemplate);

/**
 * Inserts a blank item at the given index.
 * 
 * @param inState The input state.
 * @param inComponent The choice to modify.
 * @param inOutIndex The input index to insert at, then resultant index
 * where it was added.
 * @return Any resultant error, if any.
 * @since 2024/07/17
 */
typedef sjme_errorCode (*sjme_scritchui_choiceItemInsertFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_jint* inOutIndex);

/**
 * Removes the specified item at the given index.
 * 
 * @param inState The input state.
 * @param inComponent The choice to modify.
 * @param atIndex The index to remove.
 * @return Any resultant error, if any.
 * @since 2024/07/17
 */
typedef sjme_errorCode (*sjme_scritchui_choiceItemRemoveFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex);
	
/**
 * Removes all items from the given choice.
 * 
 * @param inState The input state.
 * @param inComponent The choice to modify.
 * @return Any resultant error, if any.
 * @since 2024/07/17
 */
typedef sjme_errorCode (*sjme_scritchui_choiceItemRemoveAllFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);
	

/**
 * Sets whether the specified choice item is enabled.
 * 
 * @param inState The input state.
 * @param inComponent The choice to modify.
 * @param atIndex The index to modify.
 * @param isEnabled If the item should be enabled.
 * @return Any resultant error, if any.
 * @since 2024/07/25
 */
typedef sjme_errorCode (*sjme_scritchui_choiceItemSetEnabledFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isEnabled);

/**
 * Sets the image of the specified choice item.
 * 
 * @param inState The input state.
 * @param inComponent The choice to modify.
 * @param atIndex The index to modify.
 * @param inRgb The RGB data, may be @c NULL to clear the image.
 * @param inRgbOff The offset in the RGB data.
 * @param inRgbDataLen The data length of the RGB data.
 * @param inRgbScanLen The scanline length of the RGB data.
 * @param width The width of the image.
 * @param height The height of the image.
 * @return Any resultant error, if any.
 * @since 2024/07/25
 */
typedef sjme_errorCode (*sjme_scritchui_choiceItemSetImageFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_jint* inRgb,
	sjme_attrInPositive sjme_jint inRgbOff,
	sjme_attrInPositiveNonZero sjme_jint inRgbDataLen,
	sjme_attrInPositiveNonZero sjme_jint inRgbScanLen,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);

/**
 * Sets whether the specified choice item is selected.
 * 
 * @param inState The input state.
 * @param inComponent The choice to modify.
 * @param atIndex The index to modify.
 * @param isSelected If the item should be selected.
 * @return Any resultant error, if any.
 * @since 2024/07/25
 */
typedef sjme_errorCode (*sjme_scritchui_choiceItemSetSelectedFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isSelected);

/**
 * Sets the string of the specified choice item.
 * 
 * @param inState The input state.
 * @param inComponent The choice to modify.
 * @param atIndex The index to modify.
 * @param inString The string to set, @c NULL will clear it.
 * @return Any resultant error, if any.
 * @since 2024/07/25
 */
typedef sjme_errorCode (*sjme_scritchui_choiceItemSetStringFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_lpcstr inString);

/**
 * Returns the length of the choice list.
 * 
 * @param inState The input state.
 * @param inComponent The choice to get the length of.
 * @param outLength The resultant length.
 * @return Any resultant error, if any.
 * @since 2024/07/17
 */
typedef sjme_errorCode (*sjme_scritchui_choiceLengthFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jint* outLength);

/**
 * Grabs the focus onto this item.
 * 
 * @param inState The input state.
 * @param inComponent The input component.
 * @return Any resultant error, if any.
 * @since 2024/07/26
 */
typedef sjme_errorCode (*sjme_scritchui_componentFocusGrabFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);
	
/**
 * Checks whether the given component has focus.
 * 
 * @param inState The input state.
 * @param inComponent The input component.
 * @param outHasFocus The result of whether the component has focus.
 * @return Any resultant error, if any.
 * @since 2024/07/26
 */
typedef sjme_errorCode (*sjme_scritchui_componentFocusHasFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jboolean* outHasFocus);

/**
 * Gets the parent of this component.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The component to get the parent of.
 * @param outParent The resultant parent that contains this, or @c NULL if
 * there is no parent.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_componentGetParentFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_uiComponent* outParent);

/**
 * Returns the position of the given component.
 * 
 * @param inState The input state.
 * @param inComponent The component to get the position of.
 * @param outX The output X coordinate.
 * @param outY The output Y coordinate.
 * @return Any resultant error, if any.
 * @since 2024/08/06
 */
typedef sjme_errorCode (*sjme_scritchui_componentPositionFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outX,
	sjme_attrOutNullable sjme_jint* outY);

/**
 * Repaints the given component.
 * 
 * @param inState The input state.
 * @param inComponent The input component.
 * @param x The X position.
 * @param y The Y position.
 * @param width The width.
 * @param height The height.
 * @return Any error code if applicable.
 * @since 2024/04/24
 */
typedef sjme_errorCode (*sjme_scritchui_componentRepaintFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);

/**
 * Revalidates the given component.
 * 
 * @param inState The input state.
 * @param inComponent The component to be revalidated.
 * @return On any error if applicable.
 * @since 2024/04/21
 */
typedef sjme_errorCode (*sjme_scritchui_componentRevalidateFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);

/**
 * Sets the activation listener for the given choice.
 * 
 * @param inState The input state.
 * @param inComponent The choice to update.
 * @param inListener The listener to set.
 * @param copyFrontEnd Any front end data to copy.
 * @return Any resultant error, if any.
 * @since 2024/07/17
 */
typedef sjme_errorCode (*sjme_scritchui_componentSetActivateListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(activate));

/**
 * Sets the input listener for the given component.
 * 
 * @param inState The input state.
 * @param inComponent The component to set the listener for.
 * @param inListener The listener for events, may be @c NULL to clear
 * the existing listener.
 * @param copyFrontEnd The front end data to copy, may be @c NULL .
 * @return Any resultant error, if any.
 * @since 2024/06/29
 */
typedef sjme_errorCode (*sjme_scritchui_componentSetInputListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(input));

/**
 * Sets the paint listener for the given component.
 * 
 * @param inState The input state.
 * @param inComponent The component to set the listener for.
 * @param inListener The listener for paint events, may be @c NULL to clear
 * the existing listener.
 * @param copyFrontEnd The front end data to copy, may be @c NULL .
 * @return Any error code if applicable.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_componentSetPaintListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(paint));

/**
 * Sets the listener for size events.
 * 
 * @param inState The input state.
 * @param inComponent The component to set the listener for.
 * @param inListener The listener to set to or to clear.
 * @param copyFrontEnd Any front end data to be copied.
 * @return Any resultant error, if any.
 * @since 2024/04/26
 */
typedef sjme_errorCode (*sjme_scritchui_componentSetSizeListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(size));

/**
 * Sets the update listener for the given choice.
 * 
 * @param inState The input state.
 * @param inComponent The choice to update.
 * @param inListener The listener to set.
 * @param copyFrontEnd Any front end data to copy.
 * @return Any resultant error, if any.
 * @since 2024/07/17
 */
typedef sjme_errorCode (*sjme_scritchui_componentSetValueUpdateListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(valueUpdate));

/**
 * Sets the listener to call when the visibility of a component changes.
 * 
 * @param inState The input state.
 * @param inComponent The component to set for.
 * @param inListener The listener to use.
 * @param copyFrontEnd The front end data to use.
 * @return Any resultant error, if any.
 * @since 2024/06/28
 */
typedef sjme_errorCode (*sjme_scritchui_componentSetVisibleListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(visible));

/**
 * Returns the size of the given component.
 * 
 * @param inState The input state.
 * @param inComponent The component to get the size of.
 * @param outWidth The output width.
 * @param outHeight The output height.
 * @return Any resultant error, if any.
 * @since 2024/05/12
 */
typedef sjme_errorCode (*sjme_scritchui_componentSizeFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outWidth,
	sjme_attrOutNullable sjme_jint* outHeight);

/**
 * Adds the given component to the specified container.
 * 
 * @param inState The input state.
 * @param inContainer The container to place the component within.
 * @param addComponent The component to add to the container.
 * @return Any error code if applicable.
 * @since 2024/04/20
 */
typedef sjme_errorCode (*sjme_scritchui_containerAddFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent addComponent);

/**
 * Removes the given component from the specified container.
 * 
 * @param inState The input state.
 * @param inContainer The container to remove the component from.
 * @param removeComponent The component to remove from the container.
 * @return Any error code if applicable.
 * @since 2024/07/15
 */
typedef sjme_errorCode (*sjme_scritchui_containerRemoveFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent removeComponent);

/**
 * Removes all components from the container.
 * 
 * @param inState The input state.
 * @param inContainer The container to remove everything from.
 * @return Any error code if applicable.
 * @since 2024/07/15
 */
typedef sjme_errorCode (*sjme_scritchui_containerRemoveAllFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer);

/**
 * Sets the bounds of a component within the container.
 * 
 * @param inState The input state.
 * @param inContainer The container to set the component within.
 * @param inComponent The component to be placed and resized.
 * @param x The X position.
 * @param y The Y position.
 * @param width The width.
 * @param height The height.
 * @return Any error code if applicable.
 * @since 2024/04/28
 */
typedef sjme_errorCode (*sjme_scritchui_containerSetBoundsFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);

/**
 * Returns the default built-in font.
 * 
 * @param inState The input state.
 * @param outFont The resultant font.
 * @return Any resultant error, if any.
 * @since 2024/06/12
 */
typedef sjme_errorCode (*sjme_scritchui_fontBuiltinFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outFont);

/**
 * Derives a new font from an existing font.
 * 
 * @param inState The input state.
 * @param inFont The input font to derive.
 * @param inStyle The style to switch to.
 * @param inPixelSize The pixel size to use.
 * @param outDerived The resultant derived font.
 * @return Any resultant error, if any.
 * @since 2024/06/14
 */
typedef sjme_errorCode (*sjme_scritchui_fontDeriveFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_scritchui_pencilFontStyle inStyle,
	sjme_attrInPositiveNonZero sjme_jint inPixelSize,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outDerived);

/**
 * Creates a hardware reference bracket to the native hardware graphics.
 * 
 * @param inState The UI state.
 * @param outPencil The resultant pencil.
 * @param outWeakPencil The weak reference to the pencil.
 * @param pf The @c sjme_gfx_pixelFormat used for the draw.
 * @param bw The buffer width, this is the scanline width of the buffer.
 * @param bh The buffer height.
 * @param inLockFuncs The locking functions to use for buffer access.
 * @param inLockFrontEndCopy Front end copy data for locks.
 * @param sx Starting surface X coordinate.
 * @param sy Starting surface Y coordinate.
 * @param sw Surface width.
 * @param sh Surface height.
 * @param pencilFrontEndCopy Front end data that goes into the pencil.
 * @return An error if the requested graphics are not valid.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_hardwareGraphicsFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrOutNullable sjme_alloc_weak* outWeakPencil,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInPositiveNonZero sjme_jint bh,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEnd* inLockFrontEndCopy,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInNullable const sjme_frontEnd* pencilFrontEndCopy);

/**
 * Sets the label of the specified component.
 * 
 * @param inState The input state.
 * @param inCommon The item to set the label for.
 * @param inString The label to set.
 * @return Any resultant error, if any.
 * @since 2024/07/21
 */
typedef sjme_errorCode (*sjme_scritchui_labelSetStringFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInNullable sjme_lpcstr inString);

/**
 * Returns the color for the given element based on the current look and feel.
 * 
 * @param inState The current state.
 * @param inContext Optional context that can be targetted at a widget to get
 * its color themeing.
 * @param outRGB The resultant RGB color.
 * @param elementColor The color to request.
 * @return On any resultant error, if any.
 * @since 2024/07/27
 */
typedef sjme_errorCode (*sjme_scritchui_lafElementColorFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrOutNotNull sjme_jint* outRGB,
	sjme_attrInValue sjme_scritchui_lafElementColorType elementColor);

/**
 * Creates a new list.
 * 
 * @param inState The input state.
 * @param outPanel The resultant list.
 * @param inChoiceType The type of choice this is.
 * @return Any error code if applicable.
 * @since 2024/07/16
 */
typedef sjme_errorCode (*sjme_scritchui_listNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiList* outList,
	sjme_attrInValue sjme_scritchui_choiceType inChoiceType);

/**
 * Execute the given callback within the event loop of the GUI.
 * 
 * @param inState The input state.
 * @param callback The callback to execute.
 * @param anything A value that can be passed to the listener.
 * @return Any error code if applicable.
 * @since 2024/04/09
 */
typedef sjme_errorCode (*sjme_scritchui_loopExecuteFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything);

/**
 * Determines whether the current thread is in the event loop or not.
 * 
 * @param inState The input state.
 * @param outInThread The result of whether this is in the event loop.
 * @return Any error code if applicable.
 * @since 2024/04/09
 */
typedef sjme_errorCode (*sjme_scritchui_loopIsInThreadFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_jboolean* outInThread);

/**
 * Iterates a single run of the event loop.
 * 
 * @param inState The input ScritchUI state.
 * @param blocking If the iteration should block for something to happen.
 * @param outHasTerminated Has the GUI interface terminated?
 * @return Any error code if applicable.
 * @since 2024/04/02
 */
typedef sjme_errorCode (*sjme_scritchui_loopIterateFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_jboolean blocking,
	sjme_attrOutNullable sjme_jboolean* outHasTerminated);

/**
 * Creates a new menu bar.
 * 
 * @param inState The input state.
 * @param outMenuBar The resultant menu bar.
 * @return Any error code if applicable.
 * @since 2024/07/21
 */
typedef sjme_errorCode (*sjme_scritchui_menuBarNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuBar* outMenuBar);

/**
 * Inserts the given menu item into the menu at the specified index.
 * 
 * @param inState The ScritchUI state.
 * @param intoMenu The menu to insert into.
 * @param atIndex The index to insert at.
 * @param childItem The child menu item to add.
 * @return Any resultant error, if any.
 * @since 2024/07/23 
 */
typedef sjme_errorCode (*sjme_scritchui_menuInsertFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind intoMenu,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind childItem);

/**
 * Creates a new menu item.
 * 
 * @param inState The input state.
 * @param outMenuItem The resultant menu item.
 * @return Any error code if applicable.
 * @since 2024/07/21
 */
typedef sjme_errorCode (*sjme_scritchui_menuItemNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuItem* outMenuItem);

/**
 * Creates a new menu.
 * 
 * @param inState The input state.
 * @param outMenu The resultant menu.
 * @return Any error code if applicable.
 * @since 2024/07/21
 */
typedef sjme_errorCode (*sjme_scritchui_menuNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenu* outMenu);

/**
 * Removes the item at the specified index from this menu.
 * 
 * @param inState The ScritchUI state.
 * @param fromMenu The menu to remove from.
 * @param atIndex The index to remove.
 * @return Any resultant error, if any.
 * @since 2024/07/23 
 */
typedef sjme_errorCode (*sjme_scritchui_menuRemoveFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu,
	sjme_attrInPositive sjme_jint atIndex);

/**
 * Removes all items from the given menu.
 * 
 * @param inState The ScritchUI state.
 * @param fromMenu The menu to remove from.
 * @return Any resultant error, if any.
 * @since 2024/07/23 
 */
typedef sjme_errorCode (*sjme_scritchui_menuRemoveAllFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu);

/**
 * Deletes the given object.
 * 
 * @param inState The input state.
 * @param inOutObject The object to delete.
 * @return Any resultant error, if any.
 * @since 2024/07/20
 */
typedef sjme_errorCode (*sjme_scritchui_objectDeleteFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiCommon* inOutObject);

/**
 * Enables or disables focus on a panel.
 * 
 * @param inState The input state.
 * @param inPanel The input panel.
 * @param enableFocus Should focus be enabled?
 * @return Any error code if applicable.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_panelEnableFocusFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus,
	sjme_attrInValue sjme_jboolean defaultFocus);

/**
 * Creates a new panel.
 * 
 * @param inState The input state.
 * @param outPanel The resultant panel.
 * @return Any error code if applicable.
 * @since 2024/04/02
 */
typedef sjme_errorCode (*sjme_scritchui_panelNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiPanel* outPanel);

/**
 * Sets the screen listener callback for screen changes.
 * 
 * @param inState The input state.
 * @param callback The callback for screen information and changes.
 * @return Any error code if applicable.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_screenSetListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(screen));

/**
 * Obtains and queries the screens which are attached to the system displays.
 * 
 * @param inState The input state.
 * @param outScreens The resultant screens.
 * @param inOutNumScreens The number of screens for input and output.
 * @return Any error code if applicable.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_screensFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScreen* outScreens,
	sjme_attrInOutNotNull sjme_jint* inOutNumScreens);

/**
 * Creates a new scroll panel which contains other components within a viewport
 * with scrollbars.
 * 
 * @param inState The ScritchUI state.
 * @param outScrollPanel The newly created scroll panel.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_scrollPanelNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScrollPanel* outScrollPanel);

/**
 * Gets the current view rectangle of a viewport.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The viewport.
 * @param outViewRect The current view rectangle.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_viewGetViewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_rect* outViewRect);

/**
 * Sets the area that the scroll panel provides a viewport area, this area
 * may be larger than the viewport and widgets may be placed inside.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The viewport.
 * @param inViewArea The view area to set.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_viewSetAreaFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_dim* inViewArea);

/**
 * Sets the view rectangle of a viewport.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The viewport.
 * @param inViewPos The new view position to set.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_viewSetViewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_point* inViewPos);

/**
 * Sets the listener which is called for a viewport when a contained component
 * has a suggestion as to how large it should be to contain it.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The viewport.
 * @param inListener The listener to set.
 * @param copyFrontEnd Any front-end data needed for the listener.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_viewSetSizeSuggestListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(sizeSuggest));

/**
 * Sets the listener which is called whenever the viewport changes such as it
 * being scrolled.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The viewport.
 * @param inListener The listener to set.
 * @param copyFrontEnd Any front-end data needed for the listener.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_viewSetViewListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(view));

/**
 * Void listener set.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The component.
 * @param inListener The listener to set.
 * @param copyFrontEnd Any front-end data needed for the listener.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_voidSetVoidListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(void));

/**
 * Sets the minimum content size for windows.
 * 
 * @param inState The input state.
 * @param inWindow The window to set the minimum content size for.
 * @param width The width to set.
 * @param height The height to set.
 * @return Any error code if applicable, such as if the width and/or height
 * are zero or negative.
 * @since 2024/04/21
 */
typedef sjme_errorCode (*sjme_scritchui_windowContentMinimumSizeFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);
	
/**
 * Creates a new window.
 * 
 * @param inState The input state.
 * @param outWindow The resultant newly created window.
 * @return Any error code if applicable.
 * @since 2024/04/16
 */
typedef sjme_errorCode (*sjme_scritchui_windowNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiWindow* outWindow);

/**
 * Sets the close listener for a window.
 * 
 * @param inState The input state.
 * @param inWindow The window to set for.
 * @param inListener The listener to use.
 * @param copyFrontEnd The front end data to use.
 * @return Any resultant error, if any.
 * @since 2024/05/13
 */
typedef sjme_errorCode (*sjme_scritchui_windowSetCloseListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(close));

/**
 * Sets the menu bar for a window.
 * 
 * @param inState The input state.
 * @param inWindow The window to set the menu bar of.
 * @param inMenuBar The menu bar to set, if @c NULL then it is removed.
 * @return Any resultant error, if any.
 * @since 2024/07/23
 */
typedef sjme_errorCode (*sjme_scritchui_windowSetMenuBarFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_scritchui_uiMenuBar inMenuBar);

/**
 * Sets the menu item activation listener for a window.
 * 
 * @param inState The input state.
 * @param inWindow The window to set for.
 * @param inListener The listener to use.
 * @param copyFrontEnd The front end data to use.
 * @return Any resultant error, if any.
 * @since 2024/07/30
 */
typedef sjme_errorCode (*sjme_scritchui_windowSetMenuItemActivateListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(menuItemActivate));

/**
 * Sets the visibility of a window.
 * 
 * @param inState The input state.
 * @param inWindow The input window.
 * @param isVisible Should the window be visible?
 * @return Any error code if applicable.
 * @since 2024/04/21
 */
typedef sjme_errorCode (*sjme_scritchui_windowSetVisibleFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible);

/** Quicker API declaration in struct. */
#define SJME_SCRITCHUI_QUICK_API(x) \
	SJME_TOKEN_PASTE3(sjme_scritchui_, x, Func) x

struct sjme_scritchui_apiFunctions
{
	/** API flags. */
	SJME_SCRITCHUI_QUICK_API(apiFlags);
	
	/** Get the first selected index of a choice. */
	SJME_SCRITCHUI_QUICK_API(choiceGetSelectedIndex);
	
	/** Gets the item information. */
	SJME_SCRITCHUI_QUICK_API(choiceItemGet);
	
	/** Inserts an item into the given choice. */
	SJME_SCRITCHUI_QUICK_API(choiceItemInsert);
	
	/** Removes an item from the given choice. */
	SJME_SCRITCHUI_QUICK_API(choiceItemRemove);
	
	/** Removes all items from the given choice. */
	SJME_SCRITCHUI_QUICK_API(choiceItemRemoveAll);
	
	/** Sets whether the given choice item is enabled. */
	SJME_SCRITCHUI_QUICK_API(choiceItemSetEnabled);
	
	/** Sets the image of the given choice item. */
	SJME_SCRITCHUI_QUICK_API(choiceItemSetImage);
	
	/** Sets whether the given choice item is selected. */
	SJME_SCRITCHUI_QUICK_API(choiceItemSetSelected);
	
	/** Sets the string of the given choice item. */
	SJME_SCRITCHUI_QUICK_API(choiceItemSetString);
	
	/** Gets the choice length. */
	SJME_SCRITCHUI_QUICK_API(choiceLength);
	
	/** Grabs the focus for this component. */
	SJME_SCRITCHUI_QUICK_API(componentFocusGrab);
	
	/** Checks if this component has focus. */
	SJME_SCRITCHUI_QUICK_API(componentFocusHas);
	
	/** Gets the parent component of this one. */
	SJME_SCRITCHUI_QUICK_API(componentGetParent);
	
	/** Get size of component. */
	SJME_SCRITCHUI_QUICK_API(componentPosition);
	
	/** Repaints the given component. */
	SJME_SCRITCHUI_QUICK_API(componentRepaint);
	
	/** Revalidates the given component. */
	SJME_SCRITCHUI_QUICK_API(componentRevalidate);
	
	/** Set listener for when a component is activated. */
	SJME_SCRITCHUI_QUICK_API(componentSetActivateListener);
	
	/** Sets the input listener for a component. */
	SJME_SCRITCHUI_QUICK_API(componentSetInputListener);
	
	/** Sets the paint listener for a component. */
	SJME_SCRITCHUI_QUICK_API(componentSetPaintListener);
	
	/** Sets the listener for component size events. */
	SJME_SCRITCHUI_QUICK_API(componentSetSizeListener);
	
	/** Set listener for when a component value has updated. */
	SJME_SCRITCHUI_QUICK_API(componentSetValueUpdateListener);
	
	/** Sets the listener for component visible events. */
	SJME_SCRITCHUI_QUICK_API(componentSetVisibleListener);

	/** Get size of component. */
	SJME_SCRITCHUI_QUICK_API(componentSize);
	
	/** Adds component to container. */
	SJME_SCRITCHUI_QUICK_API(containerAdd);
	
	/** Remove component from container. */
	SJME_SCRITCHUI_QUICK_API(containerRemove);
	
	/** Remove all components from a container. */
	SJME_SCRITCHUI_QUICK_API(containerRemoveAll);
	
	/** Set bounds of component in a container. */
	SJME_SCRITCHUI_QUICK_API(containerSetBounds);

	/** Returns the default built-in font. */
	SJME_SCRITCHUI_QUICK_API(fontBuiltin);
	
	/** Derive a similar font. */
	SJME_SCRITCHUI_QUICK_API(fontDerive);
	
	/** Hardware graphics support on arbitrary buffers. */
	SJME_SCRITCHUI_QUICK_API(hardwareGraphics);
	
	/** Sets the close listener for a window. */
	SJME_SCRITCHUI_QUICK_API(labelSetString);
	
	/** Returns the element color for the look and feel. */
	SJME_SCRITCHUI_QUICK_API(lafElementColor);
	
	/** Creates a new list. */
	SJME_SCRITCHUI_QUICK_API(listNew);
	
	/** Execute callback within the event loop. */
	SJME_SCRITCHUI_QUICK_API(loopExecute);
	
	/** Execute callback later in the event loop. */
	sjme_scritchui_loopExecuteFunc loopExecuteLater;
	
	/** Execute callback within the event loop and wait until termination. */
	sjme_scritchui_loopExecuteFunc loopExecuteWait;
	
	/** Is the current thread in the loop? */
	SJME_SCRITCHUI_QUICK_API(loopIsInThread);
	
	/** Iterates a single run of the event loop. */
	SJME_SCRITCHUI_QUICK_API(loopIterate);
	
	/** Creates a new menu bar. */
	SJME_SCRITCHUI_QUICK_API(menuBarNew);
	
	/** Insert the given menu item into a menu. */
	SJME_SCRITCHUI_QUICK_API(menuInsert);
	
	/** Creates a new menu item. */
	SJME_SCRITCHUI_QUICK_API(menuItemNew);
	
	/** Creates a new menu. */
	SJME_SCRITCHUI_QUICK_API(menuNew);
	
	/** Removes an item from the menu. */
	SJME_SCRITCHUI_QUICK_API(menuRemove);
	
	/** Removes all items from the menu. */
	SJME_SCRITCHUI_QUICK_API(menuRemoveAll);
	
	/** Deletes an object. */
	SJME_SCRITCHUI_QUICK_API(objectDelete);
	
	/** Enable focus on a panel. */
	SJME_SCRITCHUI_QUICK_API(panelEnableFocus);
	
	/** Creates a new panel. */
	SJME_SCRITCHUI_QUICK_API(panelNew);
	
	/** Register listener. */
	SJME_SCRITCHUI_QUICK_API(screenSetListener);
	
	/** Screens available. */
	SJME_SCRITCHUI_QUICK_API(screens);
	
	/** Create a new scroll panel. */
	SJME_SCRITCHUI_QUICK_API(scrollPanelNew);
	
	/** Get the current view rect of a viewport. */
	SJME_SCRITCHUI_QUICK_API(viewGetView);
	
	/** Set the area of the viewport's bounds, the entire scrollable area. */
	SJME_SCRITCHUI_QUICK_API(viewSetArea);
	
	/** Sets the view rect of a viewport. */
	SJME_SCRITCHUI_QUICK_API(viewSetView);
	
	/** Sets the size suggestion for this view. */
	SJME_SCRITCHUI_QUICK_API(viewSetSizeSuggestListener);
	
	/** Sets the listener for tracking scrolling and viewport changes. */
	SJME_SCRITCHUI_QUICK_API(viewSetViewListener);
	
	/** Sets minimum size of the window contents. */
	SJME_SCRITCHUI_QUICK_API(windowContentMinimumSize);
	
	/** Creates a new window. */
	SJME_SCRITCHUI_QUICK_API(windowNew);
	
	/** Sets the close listener for a window. */
	SJME_SCRITCHUI_QUICK_API(windowSetCloseListener);

	/** Sets the menu bar for a window. */
	SJME_SCRITCHUI_QUICK_API(windowSetMenuBar);
	
	/** Sets the activation listener for menu items in a window. */
	SJME_SCRITCHUI_QUICK_API(windowSetMenuItemActivateListener);
	
	/** Sets visibility of window. */
	SJME_SCRITCHUI_QUICK_API(windowSetVisible);
};

#undef SJME_SCRITCHUI_QUICK_API

/**
 * Opaque internal implementation functions.
 * 
 * @since 2024/05/14
 */
typedef struct sjme_scritchui_implInternFunctions
	sjme_scritchui_implInternFunctions;

/** The number of common handles. */
#define SJME_SCRITCHUI_NUM_COMMON_HANDLES 4

/** The number of common values. */
#define SJME_SCRITCHUI_NUM_COMMON_VALUES SJME_SCRITCHUI_NUM_COMMON_HANDLES

struct sjme_scritchui_uiCommonBase
{
	/** The type of what this is. */
	sjme_scritchui_uiType type;
	
	/** The state which owns this. */
	sjme_scritchui state;
	
	/**
	 * Front-end data for this, note that ScritchUI implementations must not
	 * use this for information as this is only to be used by front-ends.
	 */
	sjme_frontEnd frontEnd;
	
	/** Opaque native handles for this, as needed. */
	sjme_scritchui_handle handle[SJME_SCRITCHUI_NUM_COMMON_HANDLES];
	
	/** Other value storage, as needed. */
	sjme_jint intVals[SJME_SCRITCHUI_NUM_COMMON_VALUES];
};

/**
 * Window manager details to use.
 * 
 * @since 2024/04/24
 */
typedef struct sjme_scritchui_wmInfo
{
	/** Default title. */
	sjme_lpcstr defaultTitle;
	
	/** X Window System Class. */
	sjme_lpcstr xwsClass;
} sjme_scritchui_wmInfo;

/**
 * Windowing system specific bugs.
 * 
 * @since 2024/08/15
 */
typedef struct sjme_scritchui_bugs
{
	/** Do not set content size when the window is made visible. */
	sjme_jboolean noContentSizeWhenVisible;
} sjme_scritchui_bugs;

struct sjme_scritchui_stateBase
{
	/** Common data. */
	sjme_scritchui_uiCommonBase common;
	
	/** Window manager information. */
	const sjme_scritchui_wmInfo* wmInfo;
	
	/** API functions to use. */
	const sjme_scritchui_apiFunctions* api;
	
	/** In thread API functions. */
	const sjme_scritchui_apiFunctions* apiInThread;
	
	/** Internal implementation functions to use. */
	const sjme_scritchui_internFunctions* intern;
	
	/** Implementation functions to use. */
	const sjme_scritchui_implFunctions* impl;
	
	/** Internal implementation functions, which are opaque. */
	const sjme_scritchui_implInternFunctions* implIntern;
	
	/** The allocation pool to use for allocations. */
	sjme_alloc_pool* pool;
	
	/** The event loop thread, if applicable. */
	sjme_thread loopThread;
	
	/** The current loop thread ID, if applicable. */
	sjme_intPointer loopThreadId;
	
	/** Loop thread initializer if one was passed. */
	sjme_thread_mainFunc loopThreadInit;
	
	/** Indicator that the main loop is ready for execution. */
	sjme_atomic_sjme_jint loopThreadReady;
	
	/** The available screens. */
	sjme_list_sjme_scritchui_uiScreen* screens;
	
	/** The window manager type used. */
	sjme_scritchui_windowManagerType wmType;
	
	/** The internal built-in font. */
	sjme_scritchui_pencilFont builtinFont; 
	
	/** The fonts which are loaded and known to the state. */
	sjme_scritchui_pencilFontLink* fontChain;
	
	/** Function to obtain the current nanotime, for input events. */
	sjme_nal_nanoTimeFunc nanoTime;
	
	/** Is this a panel only interface? */
	sjme_jboolean isPanelOnly;
	
	/** Wrapped ScritchUI state, if this is a wrapper. */
	sjme_scritchui wrappedState;
	
	/** Reference to owning state. */
	sjme_atomic_sjme_pointer topState;
	
	/** The next ID for opaque menu items. */
	sjme_jint nextMenuItemId;
	
	/** Windowing system specific bugs. */
	sjme_scritchui_bugs bugs;
};

/* If dynamic libraries are not supported, we cannot do this. */
#if !defined(SJME_CONFIG_SCRITCHUI_NO_DYLIB)

/**
 * Initializes the API through the dynamic library.
 * 
 * @param inPool The pool to allocate within.
 * @param loopExecute Optional callback for loop execution, may be @c NULL ,
 * the passed argument is always the state.
 * @param initFrontEnd Optional initial front end data.
 * @param outState The resultant newly created ScritchUI state.
 * @return Any error code that may occur.
 * @since 2024/03/29
 */
typedef sjme_errorCode (*sjme_scritchui_dylibApiFunc)(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd,
	sjme_attrInOutNotNull sjme_scritchui* outState);

/** The base name for the ScritchUI dynamic library. */
#define SJME_SCRITCHUI_DYLIB_NAME_BASE \
	"squirreljme-scritchui-"

/** The name of the dynamic library for ScritchUI. */
#define SJME_SCRITCHUI_DYLIB_NAME(x) \
	SJME_SCRITCHUI_DYLIB_NAME_BASE SJME_TOKEN_STRING_PP(x)

/** The path name for the dynamic library for ScritchUI. */
#define SJME_SCRITCHUI_DYLIB_PATHNAME(x) \
	SJME_CONFIG_DYLIB_PATHNAME(SJME_SCRITCHUI_DYLIB_NAME(x))

/** The symbol to use with @c sjme_scritchui_dylibApiFunc . */
#define SJME_SCRITCHUI_DYLIB_SYMBOL(x) \
	SJME_TOKEN_PASTE(sjme_scritchui_dylibApi, x)
		
#endif

/**
 * Check cast of a given type.
 * 
 * @param inType The input type.
 * @param inPtr The input pointer.
 * @return Always @c inPtr .
 * @since 2024/07/23
 */
sjme_pointer sjme_scritchui_checkCast(sjme_scritchui_uiType inType,
	sjme_pointer inPtr);

/**
 * Check cast of a given type against a component.
 * 
 * @param inPtr The input pointer.
 * @return Always @c inPtr .
 * @since 2024/07/23
 */
sjme_pointer sjme_scritchui_checkCast_component(sjme_pointer inPtr);

/**
 * Check cast of a given type against a menu kind.
 * 
 * @param inPtr The input pointer.
 * @return Always @c inPtr .
 * @since 2024/07/23
 */
sjme_pointer sjme_scritchui_checkCast_menuKind(sjme_pointer inPtr);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUI_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHUI_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUI_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SCRITCHUI_H */
