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
#include "sjme/nvm.h"

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
 * ScritchUI state.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui* sjme_scritchui;

/**
 * Component within ScritchUI.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_uiComponent* sjme_scritchui_uiComponent;

/**
 * A panel within ScritchUI.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_uiPanel* sjme_scritchui_uiPanel;

/**
 * A window within ScritchUI.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_uiWindow* sjme_scritchui_uiWindow;

/**
 * Initializes the native UI interface needed by ScritchUI.
 * 
 * @param inPool The allocation pool to use.
 * @param outState The resultant state.
 * @return Any error code if applicable.
 * @since 2024/03/27
 */
typedef sjme_errorCode (*sjme_scritchui_apiInitFunc)(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInOutNotNull sjme_scritchui* outState);

/**
 * Obtains the flags which describe the interface.
 * 
 * @param inState The input ScritchUI state.
 * @param outFlags The output flags for this interface.
 * @return Any error code if applicable.
 * @since 2024/03/29
 */
typedef sjme_errorCode (*sjme_scritchui_apiFlags)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_jint outFlags);

/**
 * ScritchUI API functions, implemented by a native library accordingly.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_apiFunctions
{
	/** Initialize the framework library. */
	sjme_scritchui_apiInitFunc init;
	
	/** API flags. */
	sjme_scritchui_apiFlags flags;
} sjme_scritchui_apiFunctions;

/* If dynamic libraries are not supported, we cannot do this. */
#if !defined(SJME_CONFIG_SCRITCHUI_NO_DYLIB)

/**
 * Function pointer type for obtaining the ScritchUI API functions from
 * a dynamic library.
 * 
 * @return The resultant API functions set.
 * @since 2024/03/29
 */
typedef const sjme_scritchui_apiFunctions* (*sjme_scritchui_dylibApiFunc)(
	void);

/** The name of the dynamic library for ScritchUI. */
#define SJME_SCRITCHUI_DYLIB_NAME(x) \
	"squirreljme-scritchui-" SJME_TOKEN_STRING_PP(x)

/** The symbol to use with @c sjme_scritchui_dylibApiFunc . */
#define SJME_SCRITCHUI_DYLIB_SYMBOL(x) \
	SJME_TOKEN_PASTE(sjme_scritchui_dylibApi, x)
		
#endif

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
