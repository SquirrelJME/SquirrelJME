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

#include "lib/scritchui/scritchuiTypeDefs.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUITYPES_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#pragma region(scritchui_core)

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
	
#pragma endregion(scritchui_core)
#pragma region(scritchui_independent)
	
/**
 * Windowing system specific bugs.
 * 
 * @since 2024/08/15
 */
typedef struct sjme_scritchui_bugs
{
	/** Manual event polling. */
	sjme_jboolean manualEventPoll;
	
	/** Do not set content size when the window is made visible. */
	sjme_jboolean noContentSizeWhenVisible;
	
	/** It is unknown when a window is visible or not. */
	sjme_jboolean windowVisibilityUnknown;
} sjme_scritchui_bugs;
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
	
#pragma endregion(scritchui_independent)
#pragma region(scritchui_base)
	
struct sjme_scritchui_uiCommonBase
{
	/** The type of what this is. */
	sjme_scritchui_uiType type;
	
	/** The state which owns this. */
	sjme_scritchui state;
	
	/**
	 * Front-end data for this, note that ScritchUI implementations must not
	 * use this for information as this is only to be used by front-ends.
	 *
	 * Bindings may be used as needed.
	 */
	sjme_frontEndBindable frontEnd;
	
	/** Opaque native handles for this, as needed. */
	sjme_scritchui_handle handle[SJME_SCRITCHUI_NUM_COMMON_HANDLES];
	
	/** Other value storage, as needed. */
	sjme_jint intVals[SJME_SCRITCHUI_NUM_COMMON_VALUES];
};
	

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
	
#pragma endregion(scritchui_base)
#pragma region(scritchui_baseChoice)
	
struct sjme_scritchui_uiChoiceBase
{
	/** The type of choice this is. */
	sjme_scritchui_choiceType type;
	
	/** The number of valid entries on the list. */
	sjme_jint numItems;
	
	/** The items on this list. */
	sjme_list(sjme_scritchui_uiChoiceItem)* items;
};
	
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

#pragma endregion(scritchui_baseChoice)
#pragma region(scritchui_baseMenu)
	
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

#pragma endregion(scritchui_baseMenu)
#pragma region(scritchui_baseScreen)
	
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
	
#pragma endregion(scritchui_baseScreen)
#pragma endregion(scritchui_baseText)
	
struct sjme_scritchui_textBase
{
	/** Common data. */
	sjme_scritchui_uiCommonBase common;
};
	
#pragma endregion(scritchui_baseText)
#pragma region(scritchui_baseView)
	
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
	
#pragma endregion(scritchui_baseView)
#pragma region(scritchui_midLevel)
	
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
	
struct sjme_scritchui_uiScrollPanelBase
{
	/** Common data. */
	sjme_scritchui_uiComponentBase component;
	
	/** Container related. */
	sjme_scritchui_uiContainerBase container;
	
	/** Viewport data. */
	sjme_scritchui_uiViewBase view;
};

#pragma endregion(scritchui_midLevel)
#pragma region(scritchui_highLevel)
	
	
struct sjme_scritchui_uiListBase
{
	/** Common data. */
	sjme_scritchui_uiComponentBase component;
	
	/** Choice information. */
	sjme_scritchui_uiChoiceBase choice;
};
	
#pragma endregion(scritchui_highLevel)
#pragma region(scritchui_loopQueue)

/**
 * The loop queue which contains multiple loop items.
 * 
 * @since 2024/12/31
 */
typedef struct sjme_scritchui_loopQueue
{
	/** The lock for the queue items. */
	sjme_alignPointer sjme_thread_spinLock lock;
	
	/** The first chunk. */
	sjme_scritchui_loopQueueChunk* firstChunk;
	
	/** The next item in the queue. */
	sjme_scritchui_loopQueueItem* next;
	
	/** The last item in the queue. */
	sjme_scritchui_loopQueueItem* last;
} sjme_scritchui_loopQueue;
	
struct sjme_scritchui_loopQueueItem
{
	/** The function to execute. */
	sjme_thread_mainFunc function;
	
	/** The "anything" value. */
	sjme_thread_parameter anything;
	
	/** The next item in the queue. */
	sjme_scritchui_loopQueueItem* next;
};
struct sjme_scritchui_loopQueueChunk
{
	/** The items in the loop queue. */
	sjme_scritchui_loopQueueItem items[SJME_SCRITCHUI_LOOP_SIZE];
	
	/** The next chunk, if this is full. */
	sjme_scritchui_loopQueueChunk* nextChunk;
};

#pragma endregion(scritchui_loopQueue)
#pragma region(scritchui_font)
	
struct sjme_scritchui_pencilFontLink
{
	/** The loaded font for this link. */
	sjme_scritchui_pencilFont font;
	
	/** The previous link. */
	sjme_scritchui_pencilFontLink* prev;
	
	/** The next link. */
	sjme_scritchui_pencilFontLink* next;
};
	
#pragma endregion(scritchui_font)
#pragma region(scritchui_pencil)
	
/**
 * Pencil drawing sub-translation matrix.
 * 
 * @since 2024/07/09
 */
typedef struct sjme_scritchui_pencilMatrixSub
{
	/** Step for source X coordinate. */
	sjme_fixed wx;
	
	/** Step for source Y coordinate. */
	sjme_fixed zy;
} sjme_scritchui_pencilMatrixSub;

/**
 * Pencil drawing matrix, for any translations, rotations, and mirroring.
 * 
 * @since 2024/07/09
 */
typedef struct sjme_scritchui_pencilMatrix
{
	/** Translation for input X coordinates. */
	sjme_scritchui_pencilMatrixSub x;
	
	/** Translation for input Y coordinates. */
	sjme_scritchui_pencilMatrixSub y;
	
	/** Target width after transformations. */
	sjme_jint tw;
	
	/** Target width after transformations. */
	sjme_jint th;
} sjme_scritchui_pencilMatrix;

/**
 * Represents the color of a pixel.
 * 
 * @since 2024/07/09
 */
typedef struct sjme_scritchui_pencilColor
{
	/** The raw pencil color, which is placed in the buffer. */
	sjme_jint v;
	
	/** The RGBA color. */
	sjme_jint argb;
	
	/** Red. */
	sjme_jubyte r;
	
	/** Green. */
	sjme_jubyte g;
	
	/** Blue. */
	sjme_jubyte b;
	
	/** Alpha. */
	sjme_jubyte a;
	
	/** Indexed color. */
	sjme_jchar i;
} sjme_scritchui_pencilColor;
	
#pragma endregion(scritchui_pencil)


/** List of component. */
SJME_LIST_DECLARE(sjme_scritchui_uiComponent, 0);

/** Type that component pointers are. */
#define SJME_TYPEOF_BASIC_sjme_scritchui_uiComponent \
	SJME_TYPEOF_BASIC_sjme_pointer

/** A list of choice items. */
SJME_LIST_DECLARE(sjme_scritchui_uiChoiceItem, 0);

/** Menu item list. */
SJME_LIST_DECLARE(sjme_scritchui_uiMenuKind, 0);


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
		/** The baseline of the font. */
		sjme_jint baseline;
		
		/** The leading of the font. */
		sjme_jint leading;
		
		/** The ascent of the font. */
		sjme_jint ascent[2];
		
		/** The descent of the font. */
		sjme_jint descent[2];
	} cache;
};
	
struct sjme_scritchui_pencilFontParam
{
	/** The style of font to render. */
	sjme_scritchui_pencilFontStyle style;
	
	/** The pixel size to render at. */
	sjme_jint pixelSize;
};
	
struct sjme_scritchui_pencilFontWithParam
{
	/** The pencil font. */
	sjme_scritchui_pencilFont font;
	
	/** The font parameters. */
	sjme_scritchui_pencilFontParam params;
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
