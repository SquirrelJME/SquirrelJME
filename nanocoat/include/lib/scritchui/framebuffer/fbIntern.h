/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internal framebuffer functions.
 * 
 * @since 2024/07/24
 */

#ifndef SQUIRRELJME_FBINTERN_H
#define SQUIRRELJME_FBINTERN_H

#include "lib/scritchui/framebuffer/fb.h"
#include "lib/scritchui/scritchui.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_FBINTERN_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Callback for when a lightweight component is clicked.
 * 
 * @param inState The input state.
 * @param inComponent The input component.
 * @param selectionId The selection Id.
 * @param atX The X coordinate.
 * @param atY The Y coordinate.
 * @return On any resultant error, if any.
 * @since 2024/07/27
 */
typedef sjme_errorCode (*sjme_scritchui_fb_lightClickListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInValue sjme_jint selectionId,
	sjme_attrInValue sjme_jint atX,
	sjme_attrInValue sjme_jint atY);

/**
 * Widget state and interactions accordingly.
 * 
 * @since 2024/07/26
 */
typedef struct sjme_scritchui_fb_widgetState
{
	/** The currently focused sub-item. */
	sjme_jint subFocusIndex;
	
	/** Selection buffer, for pointer interaction. */
	sjme_jint* selBuf;
	
	/** The length of the selection buffer. */
	sjme_jint selBufLen;
	
	/** The selection buffer width. */
	sjme_jint selBufWidth;
	
	/** The selection buffer height. */
	sjme_jint selBufHeight;
	
	/** Lightweight click. */
	sjme_scritchui_fb_lightClickListenerFunc lightClickListener;
} sjme_scritchui_fb_widgetState;

/**
 * A type of entry within a display list.
 * 
 * @since 2024/07/26
 */
typedef enum sjme_scritchui_fb_displayListType
{
	/** Nothing. */
	SJME_SCRITCHUI_FB_DL_TYPE_NOTHING,
	
	/** Normal box. */
	SJME_SCRITCHUI_FB_DL_TYPE_BOX,
	
	/** Text. */
	SJME_SCRITCHUI_FB_DL_TYPE_TEXT,
	
	/** Another display list. */
	SJME_SCRITCHUI_FB_DL_TYPE_DL,
	
	/** The number of display list types. */
	SJME_SCRITCHUI_FB_NUM_DL_TYPE
} sjme_scritchui_fb_displayListType;

/**
 * Modifier for display list drawing.
 * 
 * @since 2024/07/26
 */
typedef enum sjme_scritchui_fb_displayListMod
{
	/** No modifiers. */
	SJME_SCRITCHUI_FB_DL_TYPE_MOD_NONE = 0,
	
	/** Focused. */
	SJME_SCRITCHUI_FB_DL_TYPE_MOD_FOCUS = 1,
	
	/** Selected. */
	SJME_SCRITCHUI_FB_DL_TYPE_MOD_SELECTED = 2,
	
	/** Disabled. */
	SJME_SCRITCHUI_FB_DL_TYPE_MOD_DISABLED = 4,
} sjme_scritchui_fb_displayListMod;

/**
 * Text display list data.
 * 
 * @since 2024/07/26
 */
typedef struct sjme_scritchui_fb_displayListText
{
	/** The string to render. */
	sjme_scritchui_pencilFont font;
	
	/** The font used. */
	sjme_lpcstr string;
} sjme_scritchui_fb_displayListText;

/**
 * Represents a single item within a display list.
 * 
 * @since 2024/07/26
 */
typedef struct sjme_scritchui_fb_displayList
{
	/** The type of entry this is. */
	sjme_scritchui_fb_displayListType type;
	
	/** Modifiers for this entry. */
	sjme_scritchui_fb_displayListMod mod;
	
	/** The bounds to use for this item. */
	sjme_scritchui_rect bound;
	
	/** The selection item, used for mouse actions. */
	sjme_jint selection;
	
	/** The color to use. */
	sjme_scritchui_lafElementColorType color;
	
	/** The display list entry data. */
	union
	{
		/** Text data. */
		sjme_scritchui_fb_displayListText text;
	} data;
} sjme_scritchui_fb_displayList;

/**
 * Shaders used to modify how items are drawn.
 * 
 * @since 2024/07/26
 */
typedef struct sjme_scritchui_fb_displayShaders
{
} sjme_scritchui_fb_displayShaders; 

/**
 * Initializes the lightweight component.
 * 
 * @param inState The input state.
 * @param inComponent The input component.
 * @param outWState The output widget state.
 * @param isInteractive Is this an interactive component?
 * @param paintListener The listener for paint operations.
 * @since 2024/07/26
 */
typedef sjme_errorCode (*sjme_scritchui_fb_intern_lightweightInitFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_fb_widgetState** outWState,
	sjme_attrInValue sjme_jboolean isInteractive,
	sjme_attrInNotNull sjme_scritchui_paintListenerFunc paintListener);

/**
 * Renders the given display list.
 * 
 * @param inState The input state.
 * @param inComponent Optional component being drawn, used for selections.
 * @param g The pencil to draw with.
 * @param dlFull The display list to draw.
 * @param dlCount The size of the display list.
 * @param focusRect The focus rectangle.
 * @param shaders Shaders that may modify rendering.
 * @param shaderData Any data to pass to shaders.
 * @return Any resultant error, if any.
 * @since 2024/07/26
 */
typedef sjme_errorCode (*sjme_scritchui_fb_intern_renderFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_scritchui_fb_displayList* dlFull,
	sjme_attrInPositive sjme_jint dlCount,
	sjme_attrOutNullable sjme_scritchui_rect* focusRect,
	sjme_attrInNullable const sjme_scritchui_fb_displayShaders* shaders,
	sjme_attrInNullable sjme_pointer shaderData);

/**
 * Renders the given display list in a scroll window for the given component.
 * 
 * @param inState The input state.
 * @param inComponent The component to render for within a scroll window.
 * @param g The pencil to draw with.
 * @param dlFull The display list to draw.
 * @param dlCount The size of the display list.
 * @param focusRect The focus rectangle.
 * @param shaders Shaders that may modify rendering.
 * @param shaderData Any data to pass to shaders.
 * @return Any resultant error, if any.
 * @since 2024/07/26
 */
typedef sjme_errorCode (*sjme_scritchui_fb_intern_renderInScrollFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_scritchui_fb_displayList* dlFull,
	sjme_attrInPositive sjme_jint dlCount,
	sjme_attrOutNullable sjme_scritchui_rect* focusRect,
	sjme_attrInNullable const sjme_scritchui_fb_displayShaders* shaders,
	sjme_attrInNullable sjme_pointer shaderData);

struct sjme_scritchui_implInternFunctions
{
	/** Initialize lightweight component. */
	sjme_scritchui_fb_intern_lightweightInitFunc lightweightInit;  
	
	/** Render directly. */
	sjme_scritchui_fb_intern_renderFunc render;
		
	/** Render in scroll window. */
	sjme_scritchui_fb_intern_renderInScrollFunc renderInScroll;
};

sjme_errorCode sjme_scritchui_fb_intern_lightweightInit(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_fb_widgetState** outWState,
	sjme_attrInValue sjme_jboolean isInteractive,
	sjme_attrInNotNull sjme_scritchui_paintListenerFunc paintListener);

sjme_errorCode sjme_scritchui_fb_intern_render(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_scritchui_fb_displayList* dlFull,
	sjme_attrInPositive sjme_jint dlCount,
	sjme_attrOutNullable sjme_scritchui_rect* focusRect,
	sjme_attrInNullable const sjme_scritchui_fb_displayShaders* shaders,
	sjme_attrInNullable sjme_pointer shaderData);

sjme_errorCode sjme_scritchui_fb_intern_renderInScroll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_scritchui_fb_displayList* dlFull,
	sjme_attrInPositive sjme_jint dlCount,
	sjme_attrOutNullable sjme_scritchui_rect* focusRect,
	sjme_attrInNullable const sjme_scritchui_fb_displayShaders* shaders,
	sjme_attrInNullable sjme_pointer shaderData);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_FBINTERN_H
}
		#undef SJME_CXX_SQUIRRELJME_FBINTERN_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_FBINTERN_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_FBINTERN_H */
