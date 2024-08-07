/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internal ScritchUI types.
 * 
 * @since 2024/04/02
 */

#ifndef SQUIRRELJME_SCRITCHUITYPES_H
#define SQUIRRELJME_SCRITCHUITYPES_H

#include "sjme/atomic.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiImpl.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiPencilFont.h"
#include "lib/scritchui/scritchuiText.h"
#include "lib/scritchui/scritchuiTypesListener.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUITYPES_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The state of the pencil lock.
 * 
 * @since 2024/07/08
 */
typedef struct sjme_scritchui_pencilLockState
{
	/** Spin lock for access to the buffer. */
	sjme_thread_spinLock spinLock;
	
	/** The times this was opened. */
	sjme_atomic_sjme_jint count;
	
	/** The front end source for drawing. */
	sjme_frontEnd source;
	
	/** The base address where drawing should occur. */
	sjme_pointer base;
	
	/** The buffer limit of the base, in bytes. */
	sjme_jint baseLimitBytes;
	
	/** Is this a copy? */
	sjme_jboolean isCopy;
} sjme_scritchui_pencilLockState;

/**
 * Base pencil drawing structure.
 * 
 * @since 2024/05/04
 */
typedef struct sjme_scritchui_pencilBase
{
	/** Common data. */
	sjme_scritchui_uiCommonBase common;
	
	/** The current state of the pencil. */
	sjme_scritchui_pencilState state;
	
	/** External API. */
	const sjme_scritchui_pencilFunctions* api;
	
	/** Implementation API. */
	const sjme_scritchui_pencilImplFunctions* impl;
	
	/** Utility functions. */
	const sjme_scritchui_pencilUtilFunctions* util;
	
	/** Optional locking functions, for buffer access as required. */
	const sjme_scritchui_pencilLockFunctions* lock;
	
	/** The lock state. */
	sjme_scritchui_pencilLockState lockState;
	
	/** Lowest level primitive pencil functions. */
	sjme_scritchui_pencilPrimFunctions prim;
	
	/** Front end information for paint. */
	sjme_frontEnd frontEnd;
	
	/** The pixel format used. */
	sjme_gfx_pixelFormat pixelFormat;
	
	/** Is there an alpha channel? */
	sjme_jboolean hasAlpha;
	
	/** The default font to use. */
	sjme_scritchui_pencilFont defaultFont;
	
	/** The width of the surface. */
	sjme_jint width;
	
	/** The height of the surface. */
	sjme_jint height;
	
	/** The scanline length, in pixels. */
	sjme_jint scanLenPixels;
	
	/** The scan line length, in bytes. */
	sjme_jint scanLenBytes;
	
	/** Bits per pixel. */
	sjme_jint bitsPerPixel;
	
	/** The bytes per pixel. */
	sjme_jint bytesPerPixel;
	
	/** Forced X/Y translate. */
	sjme_scritchui_point forceTranslate;
	
	/** Color palette. */
	struct
	{
		/** The colors available. */
		const sjme_jint* colors;
		
		/** The number of colors used. */
		sjme_jint numColors;
	} palette;
} sjme_scritchui_pencilBase;

/** The string length of a component ID. */
#define SJME_SCRITCHUI_UI_COMPONENT_ID_STRLEN 32

/**
 * Stores the mouse state.
 * 
 * @since 2024/08/07
 */
typedef struct sjme_scritchui_uiMouseState
{
	/** The mouse buttons being held down. */
	sjme_jint mouseButtons;
	
	/** The modifiers being held down. */
	sjme_jint mouseModifiers;
		
	/** Last mouse X position. */
	sjme_jint mouseX;
		
	/** Last mouse Y position. */
	sjme_jint mouseY;
} sjme_scritchui_uiMouseState;

typedef struct sjme_scritchui_uiComponentBase
{
	/** Common data. */
	sjme_scritchui_uiCommonBase common;
	
	/** The parent of this component. */
	sjme_scritchui_uiComponent parent;
	
	/** User and core listeners for the component. */
	sjme_scritchui_uiComponentListeners listeners[SJME_NUM_SCRITCHUI_LISTENER];
	
	/** The bounds which were set for this component. */
	sjme_scritchui_rect bounds;
	
	/** String ID for this component. */
	sjme_cchar strId[SJME_SCRITCHUI_UI_COMPONENT_ID_STRLEN];
	
	/** General component state. */
	struct
	{
		/** Is this component currently visible? */
		sjme_jboolean isVisible;
		
		/** Is this visible to the user? */
		sjme_jboolean isUserVisible;
		
		/** Current and next logical mouse state. */
		sjme_scritchui_uiMouseState mouse[2];
	} state;
} sjme_scritchui_uiComponentBase;

/** List of component. */
SJME_LIST_DECLARE(sjme_scritchui_uiComponent, 0);

/** Type that component pointers are. */
#define SJME_TYPEOF_BASIC_sjme_scritchui_uiComponent \
	SJME_TYPEOF_BASIC_sjme_pointer

/**
 * Contains the information of a single item within a choice.
 * 
 * @since 2024/07/16
 */
typedef struct sjme_scritchui_uiChoiceItemBase
{
	/** Is this selected? */
	sjme_jboolean isSelected;
	
	/** Is this enabled? */
	sjme_jboolean isEnabled;
	
	/** The string text for the item. */
	sjme_lpcstr string;
	
	/** The font to display the text in, @c NULL is default. */
	sjme_scritchui_pencilFont font;
	
	/** The image data, if there is one for this. */
	sjme_jint* imageRgb;
	
	/** The dimensions of the image RGB data. */ 
	sjme_scritchui_dim imageRgbDim;
	
	/** The number of pixels in the image. */
	sjme_jint imageRgbNumPixels;
} sjme_scritchui_uiChoiceItemBase;

/** A list of choice items. */
SJME_LIST_DECLARE(sjme_scritchui_uiChoiceItem, 0);

/**
 * Contains all of the information on choice items.
 * 
 * @since 2024/07/16
 */
typedef struct sjme_scritchui_uiChoiceBase
{
	/** The type of choice this is. */
	sjme_scritchui_choiceType type;
	
	/** The number of valid entries on the list. */
	sjme_jint numItems;
	
	/** The items on this list. */
	sjme_list_sjme_scritchui_uiChoiceItem* items;
} sjme_scritchui_uiChoiceBase;

/**
 * Base data for containers which may contain components.
 * 
 * @since 2024/04/20
 */
typedef struct sjme_scritchui_uiContainerBase
{
	/** Components within the container. */
	sjme_list_sjme_scritchui_uiComponent* components;
} sjme_scritchui_uiContainerBase;

/**
 * Base data for anything which may have a label.
 * 
 * @since 2024/07/22
 */
typedef struct sjme_scritchui_uiLabeledBase
{
	/** The current label, which is always a copy. */
	sjme_lpcstr label;
} sjme_scritchui_uiLabeledBase;

/**
 * Base data for lists.
 * 
 * @since 2024/07/16
 */
typedef struct sjme_scritchui_uiListBase
{
	/** Common data. */
	sjme_scritchui_uiComponentBase component;
	
	/** Choice information. */
	sjme_scritchui_uiChoiceBase choice;
} sjme_scritchui_uiListBase;

/** Menu item list. */
SJME_LIST_DECLARE(sjme_scritchui_uiMenuKind, 0);

struct sjme_scritchui_uiMenuKindBase
{
	/** Common data. */
	sjme_scritchui_uiCommonBase common;
	
	/** The index of this item in the parent. */
	sjme_jint index;
};

struct sjme_scritchui_uiMenuHasChildrenBase
{
	/** The number of valid children. */
	sjme_jint numChildren;
	
	/** The children to this. */
	sjme_list_sjme_scritchui_uiMenuKind* children;
};

struct sjme_scritchui_uiMenuHasParentBase
{
	/** The parent menu. */
	sjme_scritchui_uiMenuKind parent;
};

/**
 * Base data for menus.
 * 
 * @since 2024/07/21
 */
typedef struct sjme_scritchui_uiMenuBase
{
	/** The menu kind information. */
	sjme_scritchui_uiMenuKindBase menuKind;
	
	/** Labeled item. */
	sjme_scritchui_uiLabeledBase labeled;
	
	/** Menu children. */
	sjme_scritchui_uiMenuHasChildrenBase children;
	
	/** Menu parent. */
	sjme_scritchui_uiMenuHasParentBase parent;
} sjme_scritchui_uiMenuBase;

/**
 * Base data for menu bars.
 * 
 * @since 2024/07/21
 */
typedef struct sjme_scritchui_uiMenuBarBase
{
	/** The menu kind information. */
	sjme_scritchui_uiMenuKindBase menuKind;
	
	/** Menu children. */
	sjme_scritchui_uiMenuHasChildrenBase children;
	
	/** The window this is within. */
	sjme_scritchui_uiWindow window;
} sjme_scritchui_uiMenuBarBase;

/**
 * Base data for menu items.
 * 
 * @since 2024/07/21
 */
typedef struct sjme_scritchui_uiMenuItemBase
{
	/** The menu kind information. */
	sjme_scritchui_uiMenuKindBase menuKind;
	
	/** Labeled item. */
	sjme_scritchui_uiLabeledBase labeled;
	
	/** Menu children. */
	sjme_scritchui_uiMenuHasChildrenBase children;
	
	/** Menu parent. */
	sjme_scritchui_uiMenuHasParentBase parent;
	
	/** The accelerator key @c sjme_scritchinput_key , if any. */
	sjme_jint accelKey;
	
	/** The accelerator modifiers @c sjme_scritchinput_modifier , if any. */
	sjme_jint accelMod;
	
	/** Some windowing systems need some ID to be specified. */
	sjme_jint opaqueId;
} sjme_scritchui_uiMenuItemBase;

/**
 * Base data for paintable components.
 * 
 * @since 2024/04/06
 */
typedef struct sjme_scritchui_uiPaintableBase
{
	/** Listeners. */
	sjme_scritchui_uiPaintableListeners listeners[SJME_NUM_SCRITCHUI_LISTENER];
	
	/** Extra data if needed. */
	sjme_intPointer extra;
	
	/** Is this currently in paint? */
	sjme_atomic_sjme_jint inPaint;
	
	/** Belayed painting. */
	sjme_scritchui_rect belayRect;
	
	/** Last error while in paint. */
	sjme_errorCode lastError;
	
	/** Pencil drawing information. */
	sjme_scritchui_pencilBase pencil;
} sjme_scritchui_uiPaintableBase;

typedef struct sjme_scritchui_uiPanelBase
{
	/** Common data. */
	sjme_scritchui_uiComponentBase component;
	
	/** Container related. */
	sjme_scritchui_uiContainerBase container;
	
	/** Paint related. */
	sjme_scritchui_uiPaintableBase paint;
	
	/** Is focus enabled? */
	sjme_jboolean enableFocus;
	
	/** Is default focus enabled? */
	sjme_jboolean defaultFocus;
} sjme_scritchui_uiPanelBase;

typedef struct sjme_scritchui_uiScreenBase
{
	/** Common data. */
	sjme_scritchui_uiCommonBase common;
	
	/** The screen Id. */
	sjme_jint id;
	
	/** Generic display handle such as for X11. */
	sjme_scritchui_handle displayHandle;
} sjme_scritchui_uiScreenBase;

struct sjme_scritchui_uiViewBase
{
	/** User and core listeners for the view. */
	sjme_scritchui_uiViewListeners listeners[SJME_NUM_SCRITCHUI_LISTENER];
	
	/** The current view area. */
	sjme_scritchui_dim area;
	
	/** The current view rectangle. */
	sjme_scritchui_rect view;
};

struct sjme_scritchui_uiScrollPanelBase
{
	/** Common data. */
	sjme_scritchui_uiComponentBase component;
	
	/** Container related. */
	sjme_scritchui_uiContainerBase container;
	
	/** Viewport data. */
	sjme_scritchui_uiViewBase view;
};

typedef struct sjme_scritchui_uiWindowBase
{
	/** Common data. */
	sjme_scritchui_uiComponentBase component;
	
	/** Container related. */
	sjme_scritchui_uiContainerBase container;
	
	/** Labeled item. */
	sjme_scritchui_uiLabeledBase labeled;
	
	/** The current menu bar. */
	sjme_scritchui_uiMenuBar menuBar;
	
	/** Listeners. */
	sjme_scritchui_uiWindowListeners listeners[SJME_NUM_SCRITCHUI_LISTENER];
	
	/** The minimum window size. */
	sjme_scritchui_dim min;
	
	/** The window overhead size, to account for menus, titlebar, etc. */
	sjme_scritchui_dim minOverhead;
} sjme_scritchui_uiWindowBase;

struct sjme_scritchui_pencilFontBase
{
	/** Common data. */
	sjme_scritchui_uiCommonBase common;
	
	/** Internal context pointer for implementation needs. */
	sjme_pointer context;
	
	/** External API. */
	const sjme_scritchui_pencilFontFunctions* api;
	
	/** Internal implementation. */
	const sjme_scritchui_pencilFontImplFunctions* impl;
	
	/** Font cache details. */
	struct
	{
		/** The name of the font. */
		sjme_lpcstr name;
		
		/** The face of the font. */
		sjme_scritchui_pencilFontFace face;
		
		/** The style of the font. */
		sjme_scritchui_pencilFontStyle style;
		
		/** The pixel size of the font. */
		sjme_jint pixelSize;
		
		/** The height of the font. */
		sjme_jint height;
		
		/** The baseline of the font. */
		sjme_jint baseline;
		
		/** The leading of the font. */
		sjme_jint leading;
		
		/** The ascent of the font. */
		sjme_jint ascent[2];
		
		/** The descent of the font. */
		sjme_jint descent[2];
		
		/** Font fraction, for pseudo fonts. */
		sjme_fixed fraction;
		
		/** Inverted font fraction, for pseudo fonts. */
		sjme_fixed ifraction;
	} cache;
};

struct sjme_scritchui_textBase
{
	/** Common data. */
	sjme_scritchui_uiCommonBase common;
};

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUITYPES_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHUITYPES_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUITYPES_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SCRITCHUITYPES_H */
