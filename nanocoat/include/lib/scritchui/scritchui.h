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
 * @file
 * @since 2024/03/27
 */

#ifndef SJME_C_SCRITCHUI_H
#define SJME_C_SCRITCHUI_H

#include "sjme/config.h"
#include "sjme/multithread.h"
#include "sjme/stdTypes.h"
#include "sjme/list.h"
#include "sjme/alloc.h"
#include "sjme/dylib.h"
#include "lib/scritchany/scritchany.h"
#include "lib/scritchui/scritchuiBasic.h"
#include "lib/scritchui/scritchuiState.h"
#include "lib/scritchui/scritchuiStatePencil.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUI_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Generic cast check. */
#define SJME_SUI_CAST(uiType, type, v) \
	((type)sjme_scritchui_checkCast((uiType), (v)))

/** Common type. */
#define SJME_SUI_CAST_COMMON(v) \
	((sjme_scritchui_uiCommon)(v))

/** Pointer to common type. */
#define SJME_SUI_CAST_COMMON_P(v) \
	((sjme_scritchui_uiCommon*)(v))

/** Check cast to menu kind. */
#define SJME_SUI_CAST_MENU_KIND(v) \
	((sjme_scritchui_uiMenuKind)sjme_scritchui_checkCast_menuKind((v)))

/** Check cast to component kind. */
#define SJME_SUI_CAST_COMPONENT(v) \
	((sjme_scritchui_uiComponent)sjme_scritchui_checkCast_component((v)))

/** Check cast to container kind. */
#define SJME_SUI_CAST_CONTAINER(v) \
	((sjme_scritchui_uiComponent)sjme_scritchui_checkCast_container((v)))

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
	SJME_SUI_CAST(SJME_SCRITCHUI_TYPE_MENU_ITEM, \
	sjme_scritchui_uiMenuItem, (v))

/** Check cast to window. */
#define SJME_SUI_CAST_WINDOW(v) \
	SJME_SUI_CAST(SJME_SCRITCHUI_TYPE_WINDOW, \
	sjme_scritchui_uiWindow, (v))

/**
 * Initializes the API through the dynamic library.
 * 
 * @param outState The resultant newly created ScritchUI state.
 * @param inPool The pool to allocate within.
 * @param loopExecute Optional callback for loop execution, may be @c NULL ,
 * the passed argument is always the state.
 * @param externals Optional externals that ScritchUI may use to interact
 * with a front-end.
 * @param initFrontEnd Optional initial front end data.
 * @return Any error code that may occur.
 * @since 2024/03/29
 */
typedef sjme_errorCode (sjme_attrExportCall *sjme_scritchui_dylibApiFunc)(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrInOutNotNull sjme_scritchui* outState,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable const sjme_scritchui_externalFunctions* externals,
	sjme_attrInNullable sjme_frontEndBindable* initFrontEnd);

/** The symbol used for default API export. */
#define SJME_SCRITCHUI_DYLIB_API_EXPORT \
	sjme_scritchui_dylibApiExport

/** The default API entry export method. */
extern sjme_attrExport const sjme_scritchui_dylibApiFunc
	SJME_SCRITCHUI_DYLIB_API_EXPORT;

#if defined(SJME_CONFIG_MULTILIB_IS_DYLIB)
	/** Set the value for the default dynamic library export. */
	#define SJME_SCRITCHUI_DYLIB_API_EXPORT_SET(x) \
		sjme_attrExport \
		const sjme_scritchui_dylibApiFunc SJME_SCRITCHUI_DYLIB_API_EXPORT = \
			SJME_SCRITCHUI_DYLIB_SYMBOL(x);
#else
	/** Set the value for the default dynamic library export. */
	#define SJME_SCRITCHUI_DYLIB_API_EXPORT_SET(x)
#endif

/** The base name for the ScritchUI dynamic library. */
#define SJME_SCRITCHUI_DYLIB_NAME_BASE \
	SJME_SCRITCHANY_DYLIB_NAME_BASE(ui)

/** The name of the dynamic library for ScritchUI. */
#define SJME_SCRITCHUI_DYLIB_NAME(x) \
	SJME_SCRITCHANY_DYLIB_NAME(ui, x)

/** The path name for the dynamic library for ScritchUI. */
#define SJME_SCRITCHUI_DYLIB_PATHNAME(x) \
	SJME_SCRITCHANY_DYLIB_PATHNAME(ui, x)

/** The prefix for the dynamic library. */
#define SJME_SCRITCHUI_DYLIB_SYMBOL_PREFIX \
	SJME_SCRITCHANY_DYLIB_SYMBOL_PREFIX(ui)

/** The symbol to use with @link sjme_scritchui_dylibApiFunc @endlink . */
#define SJME_SCRITCHUI_DYLIB_SYMBOL(x) \
	SJME_SCRITCHANY_DYLIB_SYMBOL(ui, x)

/** Declares the API export . */
#define SJME_SCRITCHUI_DYLIB_SYMBOL_DECLARE(x) \
	SJME_SCRITCHANY_DYLIB_SYMBOL_DECLARE(ui, x)

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
 * Check cast of a given type against a container.
 *
 * @param inPtr The input pointer.
 * @return Always @c inPtr .
 * @since 2024/12/23
 */
sjme_pointer sjme_scritchui_checkCast_container(sjme_pointer inPtr);

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
