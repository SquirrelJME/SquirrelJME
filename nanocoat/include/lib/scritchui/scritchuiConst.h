/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI related constants.
 * 
 * @file
 * @since 2026/01/21
 */

#ifndef SJME_C_SQUIRRELJME_SCRITCHUICONST_H
#define SJME_C_SQUIRRELJME_SCRITCHUICONST_H

#include "sjme/stdTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SCRITCHUICONST_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#pragma region(scritchui)

/** The size of the loop queue per chunk. */
#define SJME_SCRITCHUI_LOOP_SIZE 32

/** The number of common handles. */
#define SJME_SCRITCHUI_NUM_COMMON_HANDLES 4

/** The number of common values. */
#define SJME_SCRITCHUI_NUM_COMMON_VALUES SJME_SCRITCHUI_NUM_COMMON_HANDLES
	
/** The string length of a component ID. */
#define SJME_SCRITCHUI_UI_COMPONENT_ID_STRLEN 32

/**
 * The type of external access which is being requested, this is used
 * as a hint.
 * 
 * @since 2026/01/18
 */
typedef enum sjme_scritchui_externalAssetType
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_externalAssetType),
	
	/** Undefined asset. */
	SJME_SCRITCHUI_ASSET_TYPE_UNDEFINED = 0,
	
	/** Font asset. */
	SJME_SCRITCHUI_ASSET_TYPE_FONT = 1,
	
	/** The number of asset types. */
	SJME_SCRITCHUI_NUM_ASSET_TYPES = 2,
} sjme_scritchui_externalAssetType;

/** Magic number for ScritchUI objects. */
#define SJME_SCRITCHUI_OBJECT_MAGIC INT32_C(0x6A535549)
	
/**
 * Represents the type that this is.
 * 
 * @since 2024/04/02
 */
typedef enum sjme_scritchui_uiType
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_uiType),
	
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
	SJME_SCRITCHUI_NUM_UI_TYPES
} sjme_scritchui_uiType;

/**
 * Flags representing the type of input method that may be available, this can
 * be used to allow for text and dial-pad input on less capable devices.
 *
 * @since 2026/01/07
 */
typedef enum sjme_scritchui_inputMethodType
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_inputMethodType),
	
	/** A dial-pad such as one on a phone. */
	SJME_SCRITCHUI_INPUT_METHOD_DIAL_PAD = INT32_C(0x1),

	/** A number pad such as one on a computer keyboard. */
	SJME_SCRITCHUI_INPUT_METHOD_NUMBER_PAD = INT32_C(0x2),

	/** A basic keyboard, glyphs only, no functions. */
	SJME_SCRITCHUI_INPUT_METHOD_BASIC_KEYBOARD = INT32_C(0x4),

	/** A full keyboard with function keys, number pad is another bit. */
	SJME_SCRITCHUI_INPUT_METHOD_FULL_KEYBOARD = INT32_C(0x8),

	/** A rocker or hat capable of moving left or right. */
	SJME_SCRITCHUI_INPUT_METHOD_ROCKER_LEFT_RIGHT = INT32_C(0x10),

	/** A rocker or hat capable of moving up or down. */
	SJME_SCRITCHUI_INPUT_METHOD_ROCKER_UP_DOWN = INT32_C(0x20),

	/** A pointer that is always on the device, such as a mouse. */
	SJME_SCRITCHUI_INPUT_METHOD_ATTACHED_POINTER = INT32_C(0x40),

	/** A pointer that can appear at will, such as a stylus/finger. */
	SJME_SCRITCHUI_INPUT_METHOD_DETACHED_POINTER = INT32_C(0x80),

	/** Has button A. */
	SJME_SCRITCHUI_INPUT_METHOD_BUTTON_A = INT32_C(0x100),

	/** Has button B. */
	SJME_SCRITCHUI_INPUT_METHOD_BUTTON_B = INT32_C(0x200),

	/** Has button C. */
	SJME_SCRITCHUI_INPUT_METHOD_BUTTON_C = INT32_C(0x400),

	/** Has button D. */
	SJME_SCRITCHUI_INPUT_METHOD_BUTTON_D = INT32_C(0x800),
} sjme_scritchui_inputMethodType;
	
/**
 * Which type of screen update has occurred?
 * 
 * @since 2024/04/09
 */
typedef enum sjme_scritchui_screenUpdateType
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_screenUpdateType),
	
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
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_windowManagerType),
	
	/** One frame/window per screen. */
	SJME_SCRITCHUI_WM_TYPE_ONE_FRAME_PER_SCREEN = 0,
	
	/** Standard desktop interface. */
	SJME_SCRITCHUI_WM_TYPE_STANDARD_DESKTOP = 1,
	
	/** The number of window manager types. */
	SJME_SCRITCHUI_NUM_WM_TYPES
} sjme_scritchui_windowManagerType;

/**
 * Represents the type of choice that a choice selection may be.
 * 
 * @since 2024/07/17
 */
typedef enum sjme_scritchui_choiceType
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_choiceType),
	
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
 * Indicates a flag that specifies how a look and feel operates.
 *
 * @since 2025/05/15
 */
typedef enum sjme_scritchui_lafPlatformFlag
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_lafPlatformFlag),
	
	/** Dark mode is enabled. */
	SJME_SCRITCHUI_LAF_PLATFORM_DARK_MODE = 1,

	/** The number pad follows the calculator layout. */
	SJME_SCRITCHUI_LAF_PLATFORM_NUMPAD_CALC_LAYOUT = 2,

	/** Panel only interface. */
	SJME_SCRITCHUI_LAF_PLATFORM_PANEL_ONLY = 4,

	/** Are native alerts available? */
	SJME_SCRITCHUI_LAF_PLATFORM_HAS_ALERTS = 8,
} sjme_scritchui_lafPlatformFlag;

/**
 * The element color type for look and feel.
 * 
 * @since 2024/07/27 
 */
typedef enum sjme_scritchui_lafElementColorType
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_lafElementColorType),
	
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
	
	/** Top accent color. */
	SJME_SCRITCHUI_LAF_ELEMENT_COLOR_ACCENT_TOP = 9,
	
	/** Bottom accent color. */
	SJME_SCRITCHUI_LAF_ELEMENT_COLOR_ACCENT_BOTTOM = 10,
	
	/** The number of element colors. */
	SJME_SCRITCHUI_NUM_LAF_ELEMENT_COLOR = 11,
} sjme_scritchui_lafElementColorType;

/**
 * The type of metric to obtain.
 *
 * @since 2026/04/18
 */
typedef enum sjme_scritchui_lafMetricType
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_lafMetricType),
	
	/** Unknown metric. */
	SJME_SCRITCHUI_LAF_METRIC_UNKNOWN = 0,

	/** Default font size when unspecified. */
	SJME_SCRITCHUI_LAF_METRIC_FONT_SIZE_DEFAULT = 1,

	/** The number of available metrics. */
	SJME_SCRITCHUI_NUM_LAF_METRICS = 2,
} sjme_scritchui_lafMetricType;

/**
 * Look-and-feel coordinate direction.
 *
 * @since 2026/04/18
 */
typedef enum sjme_scritchui_lafCoordDir
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_lafCoordDir),
	
	/** Unspecified direction. */
	SJME_SCRITCHUI_COORD_DIR_UNSPECIFIED,

	/** X direction. */
	SJME_SCRITCHUI_COORD_DIR_X,

	/** Y direction. */
	SJME_SCRITCHUI_COORD_DIR_Y,

	/** Width direction. */
	SJME_SCRITCHUI_COORD_DIR_W,

	/** Height direction. */
	SJME_SCRITCHUI_COORD_DIR_H,

	/** The number of coordinate directions available. */
	SJME_SCRITCHUI_NUM_COORD_DIRS,
} sjme_scritchui_lafCoordDir;

#pragma endregion(scritchui)
#pragma region(scritchui_font)

/** The max length for a font name. */
#define SJME_MAX_FONT_NAME 64
	
/** Mask for anchor points which are valid for text rendering. */
#define SJME_SCRITCHUI_ANCHOR_TEXT_MASK \
	((SJME_SCRITCHUI_ANCHOR_HCENTER | SJME_SCRITCHUI_ANCHOR_LEFT | \
	SJME_SCRITCHUI_ANCHOR_RIGHT | SJME_SCRITCHUI_ANCHOR_TOP | \
	SJME_SCRITCHUI_ANCHOR_BOTTOM | SJME_SCRITCHUI_ANCHOR_BASELINE))
	
/**
 * Font face for pencil fonts.
 * 
 * @since 2024/06/13
 */
typedef enum sjme_scritchui_pencilFontFace
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_pencilFontFace),
	
	/** Unknown. */
	SJME_SCRITCHUI_PENCIL_FONT_FACE_UNKNOWN = 0,

	/** Monospaced. */
	SJME_SCRITCHUI_PENCIL_FONT_FACE_MONOSPACE = 1,
	
	/** Serifs. */
	SJME_SCRITCHUI_PENCIL_FONT_FACE_SERIF = 2,
	
	/** Symbol. */
	SJME_SCRITCHUI_PENCIL_FONT_FACE_SYMBOL = 4,
	
	/** Normal, nothing different from anything. */
	SJME_SCRITCHUI_PENCIL_FONT_FACE_NORMAL = 8,
	
	/** Special case for automatic font selection. */
	SJME_SCRITCHUI_PENCIL_FONT_FACE_AUTOMATIC = 16,

	/** Stylistic and artistic fonts. */
	SJME_SCRITCHUI_PENCIL_FONT_FACE_STYLISTIC = 32,
} sjme_scritchui_pencilFontFace;
	
/**
 * Font style for pencil fonts.
 * 
 * @since 2024/06/13
 */
typedef enum sjme_scritchui_pencilFontStyle
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_pencilFontStyle),
	
	/** Plain font style. */
	SJME_SCRITCHUI_PENCIL_FONT_STYLE_PLAIN = 0,
	
	/** Bold text. */
	SJME_SCRITCHUI_PENCIL_FONT_STYLE_BOLD = 1,
	
	/** Italic (slanted) text. */
	SJME_SCRITCHUI_PENCIL_FONT_STYLE_ITALIC = 2,
	
	/** Underlined text. */
	SJME_SCRITCHUI_PENCIL_FONT_STYLE_UNDERLINED = 4,
	
	/** Special case for automatic style selection. */
	SJME_SCRITCHUI_PENCIL_FONT_STYLE_AUTOMATIC = 8,
	
	/** All styles. */
	SJME_SCRITCHUI_PENCIL_FONT_STYLE_ALL =
		SJME_SCRITCHUI_PENCIL_FONT_STYLE_BOLD |
		SJME_SCRITCHUI_PENCIL_FONT_STYLE_ITALIC |
		SJME_SCRITCHUI_PENCIL_FONT_STYLE_UNDERLINED,
} sjme_scritchui_pencilFontStyle;

/**
 * Pencil font parameter index.
 *
 * @since 2026/12/22
 */
typedef enum sjme_scritchui_pencilFontParamIndex
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_pencilFontParamIndex),
	
	/** @link sjme_scritchui_pencilFontParam.style @endlink. */
	SJME_SCRITCHUI_PENCIL_FONT_PARAM_STYLE = 1,

	/** @link sjme_scritchui_pencilFontParam.pixelSize @endlink. */
	SJME_SCRITCHUI_PENCIL_FONT_PARAM_PIXEL_SIZE = 2,

	/** The number of available font parameters. */
	SJME_SCRITCHUI_PENCIL_NUM_FONT_PARAMS = 3,
} sjme_scritchui_pencilFontParamIndex;
	
#pragma endregion(scritchui_font)
#pragma region(scritchui_pencil)
	
/**
 * The flags indicating the anchor point when rendering.
 * 
 * @since 2024/06/27
 */
typedef enum sjme_scritchui_pencilAnchor
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_pencilAnchor),
	
	/** Horizontal center. */
	SJME_SCRITCHUI_ANCHOR_HCENTER = 1,
	
	/** Vertical center. */
	SJME_SCRITCHUI_ANCHOR_VCENTER = 2,
	
	/** Left. */
	SJME_SCRITCHUI_ANCHOR_LEFT = 4,
	
	/** Right. */
	SJME_SCRITCHUI_ANCHOR_RIGHT = 8,
	
	/** Top. */
	SJME_SCRITCHUI_ANCHOR_TOP = 16,
	
	/** Bottom. */
	SJME_SCRITCHUI_ANCHOR_BOTTOM = 32,
	
	/** Baseline. */
	SJME_SCRITCHUI_ANCHOR_BASELINE = 64,
} sjme_scritchui_pencilAnchor;

/**
 * Translations which may be performed on images.
 * 
 * @since 2024/07/09
 */
typedef enum sjme_scritchui_pencilTranslate
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_pencilTranslate),
	
	/** None. */
	SJME_SCRITCHUI_TRANS_NONE = 0,
	
	/** Mirror and rotate 180 degrees. */
	SJME_SCRITCHUI_TRANS_MIRROR_ROT180 = 1,
	
	/** Mirror. */
	SJME_SCRITCHUI_TRANS_MIRROR = 2,
	
	/** Rotate 180 degrees. */
	SJME_SCRITCHUI_TRANS_ROT180 = 3,
	
	/** Mirror and rotate 270 degrees. */ 
	SJME_SCRITCHUI_TRANS_MIRROR_ROT270 = 4,
	
	/** Rotate 90 degrees. */
	SJME_SCRITCHUI_TRANS_ROT90 = 5,
	
	/** Rotate 270 degrees. */
	SJME_SCRITCHUI_TRANS_ROT270 = 6,
	
	/** Mirror and rotate 90 degrees. */
	SJME_SCRITCHUI_TRANS_MIRROR_ROT90 = 7,
	
	/** The number of translations available. */
	SJME_SCRITCHUI_NUM_TRANS = 8,
} sjme_scritchui_pencilTranslate;

/**
 * The blending mode for a pencil.
 * 
 * @since 2024/05/06
 */
typedef enum sjme_scritchui_pencilBlendingMode
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_pencilBlendingMode),
	
	/** Blend with source and multiply. */
	SJME_SCRITCHUI_PENCIL_BLEND_SRC_OVER = 0,
	
	/** Use only the source alpha color. */
	SJME_SCRITCHUI_PENCIL_BLEND_SRC = 1,
	
	/** Discard source pixels that do not overlap the destination. */
	SJME_SCRITCHUI_PENCIL_BLEND_SRC_ATOP = 2,
	
	/** Keep source pixels that overlap the destination, discard others. */
	SJME_SCRITCHUI_PENCIL_BLEND_SRC_IN = 3,
	
	/** Keep source pixels that do not overlap the destination. */
	SJME_SCRITCHUI_PENCIL_BLEND_SRC_OUT = 4,
	
	/** Blend destination and source. */
	SJME_SCRITCHUI_PENCIL_BLEND_DEST_OVER = 5,
	
	/** Use only the destination. */
	SJME_SCRITCHUI_PENCIL_BLEND_DEST = 6,
	
	/** Discard destination pixels that do not overlap the source. */
	SJME_SCRITCHUI_PENCIL_BLEND_DEST_ATOP = 7,
	
	/** Keep destination pixels that overlap the source, discard others. */
	SJME_SCRITCHUI_PENCIL_BLEND_DEST_IN = 8,
	
	/** Keep destination pixels that do not overlap the source. */
	SJME_SCRITCHUI_PENCIL_BLEND_DEST_OUT = 9,
	
	/** Clear everything. */
	SJME_SCRITCHUI_PENCIL_BLEND_CLEAR = 10,
	
	/** XOR. */
	SJME_SCRITCHUI_PENCIL_BLEND_XOR = 11,
	
	/** The number of blending modes. */
	SJME_NUM_SCRITCHUI_PENCIL_BLENDS = 12,
} sjme_scritchui_pencilBlendingMode;

/**
 * Stroke style for lines.
 * 
 * @since 2024/05/06
 */
typedef enum sjme_scritchui_pencilStrokeMode
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_pencilStrokeMode),
	
	/** Solid line. */
	SJME_SCRITCHUI_PENCIL_STROKE_SOLID,
	
	/** Dotted line. */
	SJME_SCRITCHUI_PENCIL_STROKE_DOTTED,
	
	/** The number of stroke modes. */
	SJME_NUM_SCRITCHUI_PENCIL_STROKES
} sjme_scritchui_pencilStrokeMode;

/**
 * Mode flags for @code sjme_scritchui_transferRegionFunc @endcode .
 * 
 * @since 2026/01/01
 */
typedef enum sjme_scritchui_transferRegionMode
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_transferRegionMode),
	
	/**
	 * Disregard the current blending mode and force SRC when drawing
	 * ontop of the destination.
	 */
	SJME_SCRITCHUI_TRANSFER_SRC_FORCE = INT32_C(1),

	/**
	 * Optimized transfer of regions going scanline by scanline in the
	 * fashion of @code memmove() @endcode, any transformations are
	 * disregarded.
	 * 
	 * This requires that the source and destination pixel format have the
	 * same density, so a 16-bit pixel format may only ever be copied to
	 * another 16-bit pixel format.
	 * 
	 * This implies @link SJME_SCRITCHUI_TRANSFER_SRC_FORCE @endlink .
	 */
	SJME_SCRITCHUI_TRANSFER_MEMMOVE = INT32_C(2),
} sjme_scritchui_transferRegionMode;
	
#pragma endregion(scritchui_pencil)
#pragma region(scritchui_listener)
	
/**
 * Represents a class for a listener for common operation.
 * 
 * @since 2024/04/28
 */
typedef enum sjme_scritchui_listenerClass
{
	/** Integer enum. */
	sjme_enumInt(sjme_scritchui_listenerClass),
	
	/** User based listener. */
	SJME_SCRITCHUI_LISTENER_USER = 0,
	
	/** Core based listener. */
	SJME_SCRITCHUI_LISTENER_CORE = 1,
	
	/** The number of listener classes. */
	SJME_NUM_SCRITCHUI_LISTENER = 2,
} sjme_scritchui_listenerClass;
	
#pragma endregion(scritchui_listener)
#pragma region(scritchui_window)
	
/**
 * Window flags which affect how a window behaves.
 * 
 * Not all ScritchUI implementations may support specific window flags,
 * additionally ScritchUI may implement some flags in software if the
 * core implementation does not support it natively.
 * 
 * @since 2026/07/05
 */
typedef enum sjme_scritchui_windowFlag
{
	/** This is an integer enum. */
	sjme_enumInt(sjme_scritchui_windowFlag),
	
	/** Window does not appear in the task switcher. */
	SJME_SCRITCHUI_WINDOW_FLAG_NO_TASK_SWITCHER = 1,
	
	/**
	 * Window does not have any frame or window manager elements.
	 * 
	 * This is alternatively referred to as being a borderless window. This
	 * should not be used with the window
	 * state @link SJME_SCRITCHUI_WINDOW_STATE_FULLSCREEN @endlink as this
	 * flag will break native window manager support for fullscreen
	 * windows.
	 */
	SJME_SCRITCHUI_WINDOW_FLAG_UNDECORATED = 2,
	
	/** Window is a utility window. */
	SJME_SCRITCHUI_WINDOW_FLAG_UTILITY = 4,
	
	/** Window is always on top of the draw stack. */
	SJME_SCRITCHUI_WINDOW_FLAG_ALWAYS_ON_TOP = 8,
	
	/** Window is always on the bottom of the draw stack. */
	SJME_SCRITCHUI_WINDOW_FLAG_ALWAYS_ON_BOTTOM = 16,
	
	/** Window does not permit resize. */
	SJME_SCRITCHUI_WINDOW_FLAG_NO_RESIZE = 32,
	
	/** Window does not permit moving. */
	SJME_SCRITCHUI_WINDOW_FLAG_NO_MOVE = 64,
	
	/** Window is floating and cannot be tiled in tiling window managers. */
	SJME_SCRITCHUI_WINDOW_FLAG_FORCE_FLOATING = 128,
	
	/** Window is a dock app to be embedded in a panel or similar. */
	SJME_SCRITCHUI_WINDOW_FLAG_DOCK_APP = 256,
	
	/** Window is part of a torn off menu. */
	SJME_SCRITCHUI_WINDOW_FLAG_TORN_MENU = 512,
	
	/** Window is part of a torn off toolbar. */
	SJME_SCRITCHUI_WINDOW_FLAG_TORN_TOOLBAR = 1024,
	
	/** Disable all glass effects so the window cannot be seen through. */
	SJME_SCRITCHUI_WINDOW_FLAG_NO_GLASS = 2048,
	
	/** Optimize for drawing, this may disable compositing or adjust vsync. */
	SJME_SCRITCHUI_WINDOW_FLAG_OPTIMIZE_DRAWING = 4096,
	
	/** Show window on all desktops. */
	SJME_SCRITCHUI_WINDOW_FLAG_ALL_DESKTOPS = 8192,
} sjme_scritchui_windowFlag;

/**
 * The state that a window may be in.
 *
 * Not all ScritchUI implementations may support specific window states,
 * additionally ScritchUI may implement some states in software if the
 * core implementation does not support it natively.
 * 
 * @since 2026/07/06
 */
typedef enum sjme_scritchui_windowState
{
	/** This is an integer enum. */
	sjme_enumInt(sjme_scritchui_windowState),
	
	/** Window is "restored" to its default state. */
	SJME_SCRITCHUI_WINDOW_STATE_RESTORED = 0,
	
	/** Window is minimized */
	SJME_SCRITCHUI_WINDOW_STATE_MINIMIZED = 1,
	
	/** Window is maximized horizontally. */
	SJME_SCRITCHUI_WINDOW_STATE_MAXIMIZED_HORIZ = 2,
	
	/** Window is maximized vertically. */
	SJME_SCRITCHUI_WINDOW_STATE_MAXIMIZED_VERT = 3,
	
	/** Window is maximized both horizontally and vertically. */
	SJME_SCRITCHUI_WINDOW_STATE_MAXIMIZED_BOTH = 4,
	
	/** Window is shaded, only the title bar is visible. */
	SJME_SCRITCHUI_WINDOW_STATE_SHADED = 5,
	
	/**
	 * Window is fullscreen.
	 * 
	 * Note that this does not imply in any way that the window is undecorated
	 * and/or borderless. Window managers that support native
	 * fullscreen for applications may provide access to an autohidden
	 * title bar and/or menu through a screen edge or mnemonic, as such
	 * this should not be used with the window
	 * flag @link SJME_SCRITCHUI_WINDOW_FLAG_UNDECORATED @endlink.
	 */
	SJME_SCRITCHUI_WINDOW_STATE_FULLSCREEN = 6,
	
	/** The number of valid window states. */
	SJME_SCRITCHUI_WINDOW_NUM_STATES = 7,
} sjme_scritchui_windowState;
	
#pragma endregion(scritchui_window)
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUICONST_H
}
#undef SJME_CXX_SQUIRRELJME_SCRITCHUICONST_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUICONST_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SCRITCHUICONST_H */
