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
 * @file
 * @since 2024/04/02
 */

#ifndef SJME_C_SCRITCHUITYPES_H
#define SJME_C_SCRITCHUITYPES_H

#include "sjme/atomic.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiImpl.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiPencilFont.h"
#include "lib/scritchui/scritchuiText.h"
#include "lib/scritchui/scritchuiTypesListener.h"

#include "sjme/path.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUITYPES_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The max length for a font name. */
#define SJME_MAX_FONT_NAME 64
	
/**
 * The state of the pencil lock.
 * 
 * @since 2024/07/08
 */
typedef struct sjme_scritchui_pencilLockState
{
	/** Spin lock for access to the buffer. */
	sjme_alignPointer sjme_thread_spinLock spinLock;
	
	/** The times this was opened. */
	sjme_alignPointer sjme_atomic(sjme_jint) count;
	
	/** The front end source for drawing. */
	sjme_frontEndBindable source;
	
	/** The base address where drawing should occur. */
	sjme_pointer base;
	
	/** The buffer limit of the base, in bytes. */
	sjme_jint baseLimitBytes;
	
	/** Is this a copy? */
	sjme_jboolean isCopy;
} sjme_scritchui_pencilLockState;

struct sjme_scritchui_pencilBase
{
	/** Common data. */
	sjme_scritchui_uiCommonBase common;
	
	/** The current state of the pencil. */
	sjme_scritchui_pencilState state;
	
	/** External API. */
	const sjme_scritchui_pencilFunctions* api;
	
	/** External API, in thread of execution. */
	const sjme_scritchui_pencilFunctions* apiInThread;
	
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
	sjme_frontEndBindable frontEnd;
	
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
	
	/** The scan line length, in bits. */
	sjme_jint scanLenBits;
	
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
};

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

struct sjme_scritchui_uiComponentBase
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

		/** Is this being set to visible? */
		sjme_jboolean settingVisible;
		
		/** Current and next logical mouse state. */
		sjme_scritchui_uiMouseState mouse[2];
	} state;
};

/** List of component. */
SJME_LIST_DECLARE(sjme_scritchui_uiComponent, 0);

/** Type that component pointers are. */
#define SJME_TYPEOF_BASIC_sjme_scritchui_uiComponent \
	SJME_TYPEOF_BASIC_sjme_pointer

struct sjme_scritchui_uiChoiceItemBase
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
	
	/** Should the image RGB data be freed? */
	sjme_jboolean freeImageRgb;
};

/** A list of choice items. */
SJME_LIST_DECLARE(sjme_scritchui_uiChoiceItem, 0);

struct sjme_scritchui_uiChoiceBase
{
	/** The type of choice this is. */
	sjme_scritchui_choiceType type;
	
	/** The number of valid entries on the list. */
	sjme_jint numItems;
	
	/** The items on this list. */
	sjme_list(sjme_scritchui_uiChoiceItem)* items;
};

struct sjme_scritchui_uiContainerBase
{
	/** Components within the container. */
	sjme_list(sjme_scritchui_uiComponent)* components;
	
	/** The size of the content within the container. */
	sjme_scritchui_dim contentSize;
};

struct sjme_scritchui_uiLabeledBase
{
	/** The current label, which is always a copy. */
	sjme_lpcstr label;
};

struct sjme_scritchui_uiListBase
{
	/** Common data. */
	sjme_scritchui_uiComponentBase component;
	
	/** Choice information. */
	sjme_scritchui_uiChoiceBase choice;
};

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
	sjme_list(sjme_scritchui_uiMenuKind)* children;
};

struct sjme_scritchui_uiMenuHasParentBase
{
	/** The parent menu. */
	sjme_scritchui_uiMenuKind parent;
};

struct sjme_scritchui_uiMenuBase
{
	/** The menu kind information. */
	sjme_scritchui_uiMenuKindBase menuKind;
	
	/** Labeled item. */
	sjme_scritchui_uiLabeledBase labeled;
	
	/** Menu children. */
	sjme_scritchui_uiMenuHasChildrenBase children;
	
	/** Menu parent. */
	sjme_scritchui_uiMenuHasParentBase parent;
};

struct sjme_scritchui_uiMenuBarBase
{
	/** The menu kind information. */
	sjme_scritchui_uiMenuKindBase menuKind;
	
	/** Menu children. */
	sjme_scritchui_uiMenuHasChildrenBase children;
	
	/** The window this is within. */
	sjme_scritchui_uiWindow window;
};

struct sjme_scritchui_uiMenuItemBase
{
	/** The menu kind information. */
	sjme_scritchui_uiMenuKindBase menuKind;
	
	/** Labeled item. */
	sjme_scritchui_uiLabeledBase labeled;
	
	/** Menu children. */
	sjme_scritchui_uiMenuHasChildrenBase children;
	
	/** Menu parent. */
	sjme_scritchui_uiMenuHasParentBase parent;
	
	/** The accelerator key @link sjme_scritchinput_key @endlink , if any. */
	sjme_jint accelKey;
	
	/**
	 * The accelerator modifiers @link sjme_scritchinput_modifier @endlink ,
	 * if any.
	 */
	sjme_jint accelMod;
	
	/** Some windowing systems need some ID to be specified. */
	sjme_jint opaqueId;
};

struct sjme_scritchui_uiPaintableBase
{
	/** Listeners. */
	sjme_scritchui_uiPaintableListeners listeners[SJME_NUM_SCRITCHUI_LISTENER];
	
	/** Extra data if needed. */
	sjme_intPointer extra;
	
	/** Is this currently in paint? */
	sjme_alignPointer sjme_atomic(sjme_jint) inPaint;
	
	/** Belayed painting. */
	sjme_scritchui_rect belayRect;
	
	/** Last error while in paint. */
	sjme_errorCode lastError;
	
	/** Pencil drawing information. */
	sjme_scritchui_pencilBase pencil;
};

struct sjme_scritchui_uiPanelBase
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
};

struct sjme_scritchui_uiScreenBase
{
	/** Common data. */
	sjme_scritchui_uiCommonBase common;
	
	/** The screen Id. */
	sjme_jint id;
	
	/** Generic display handle such as for X11. */
	sjme_scritchui_handle displayHandle;
	
	/** Generic display handle for the specific screen. */
	sjme_scritchui_handle screenHandle;
	
	/** Cached screen bounds. */
	sjme_scritchui_rect pixelBound;
	
	/** Cached millimeter bounds. */
	sjme_scritchui_rect mmBound;
};

struct sjme_scritchui_uiViewBase
{
	/** User and core listeners for the view. */
	sjme_scritchui_uiViewListeners listeners[SJME_NUM_SCRITCHUI_LISTENER];
	
	/** The current view area. */
	sjme_scritchui_dim area;
	
	/** The current view rectangle. */
	sjme_scritchui_rect view;
	
	/** The current page size. */
	sjme_scritchui_dim pageSize;
	
	/** The last suggested viewing size. */
	sjme_scritchui_dim lastSuggest;
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

struct sjme_scritchui_uiWindowBase
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
	
	/** The component that has the focus. */
	sjme_scritchui_uiComponent focusedComponent;
};

/**
 * Contains the identifying information for a font.
 * 
 * @since 2026/01/19
 */
typedef struct sjme_scritchui_pencilFontId
{
	/** The name of the font. */
	sjme_cchar name[SJME_MAX_FONT_NAME];
	
	/** The face of the font. */
	sjme_scritchui_pencilFontFace face;
		
	/** The style of the font. */
	sjme_scritchui_pencilFontStyle style;
	
	/** The pixel size of the font. */
	sjme_jint pixelSize;
} sjme_scritchui_pencilFontId;
	
typedef struct sjme_scritchui_pencilFontCompare
{
	/** The font that is identified. */
	sjme_scritchui_pencilFont font;
	
	/** The ID of the font. */
	sjme_scritchui_pencilFontId id;
} sjme_scritchui_pencilFontCompare;

struct sjme_scritchui_pencilFontBase
{
	/** Common data. */
	sjme_scritchui_uiCommonBase common;
	
	/** The ID of the font. */
	sjme_scritchui_pencilFontId id;
	
	/** The depth of this font, that is the number of fonts this wraps. */
	sjme_jint depth;
	
	/** Internal handle pointer for implementation needs. */
	sjme_pointer handle;
	
	/** External API. */
	const sjme_scritchui_pencilFontFunctions* api;
	
	/** Internal implementation. */
	const sjme_scritchui_pencilFontImplFunctions* impl;
	
	/** Font cache details. */
	struct
	{
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
