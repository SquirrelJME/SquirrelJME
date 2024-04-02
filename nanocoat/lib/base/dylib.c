/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"

#if !defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	#if defined(SJME_CONFIG_HAS_LINUX) || \
        defined(SJME_CONFIG_HAS_BSD) || \
		defined(SJME_CONFIG_HAS_MACOS)
		/** Uses dlfcn.h. */
		#define SJME_CONFIG_DYLIB_HAS_DLFCN
	#endif
#endif

#if defined(SJME_CONFIG_DYLIB_HAS_DLFCN)
	#include <dlfcn.h>
#endif

#include "sjme/dylib.h"
#include "sjme/debug.h"

sjme_errorCode sjme_dylib_close(
	sjme_attrInNotNull sjme_dylib inLib)
{
	if (inLib == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	return SJME_ERROR_UNSUPPORTED_OPERATION;
#elif defined(SJME_CONFIG_DYLIB_HAS_DLFCN)
#else
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
#endif
}

sjme_errorCode sjme_dylib_lookup(
	sjme_attrInNotNull sjme_dylib inLib,
	sjme_attrInNotNull sjme_lpcstr inSymbol,
	void** outPtr)
{
#if defined(SJME_CONFIG_DYLIB_HAS_DLFCN)
	void* handle;
#endif

	if (inLib == NULL || inSymbol == NULL || outPtr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
#if defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	return SJME_ERROR_UNSUPPORTED_OPERATION;
#elif defined(SJME_CONFIG_DYLIB_HAS_DLFCN)
	/* Attempt symbol lookup. */
	handle = dlsym(inLib, inSymbol);
	if (handle == NULL)
	{
		/* Debug. */
		sjme_message("sjme_dylib_lookup(%p, %s): %s",
			inLib, inSymbol, dlerror());
		
		return SJME_ERROR_INVALID_LIBRARY_SYMBOL;
	}
		
	/* Success! */
	*outPtr = handle;
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
#endif
}

sjme_errorCode sjme_dylib_open(
	sjme_attrInNotNull sjme_lpcstr libPath,
	sjme_attrInOutNotNull sjme_dylib* outLib)
{
#if defined(SJME_CONFIG_DYLIB_HAS_DLFCN)
	void* handle;
#endif
	
	if (libPath == NULL || outLib == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
#if defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	return SJME_ERROR_UNSUPPORTED_OPERATION;
#elif defined(SJME_CONFIG_DYLIB_HAS_DLFCN)
	/* Attempt loading the library. */
	handle = dlopen(libPath, RTLD_NOW | RTLD_LOCAL);
	if (handle == NULL)
	{
		/* Debug. */
		sjme_message("sjme_dylib_open(%s): %s",
			libPath, dlerror());
		
		return SJME_ERROR_COULD_NOT_LOAD_LIBRARY;
	}
	
	/* Success! */
	*outLib = handle;
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
#endif
}
