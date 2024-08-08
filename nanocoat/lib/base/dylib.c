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

#include <stdio.h>

#if defined(SJME_CONFIG_DYLIB_HAS_DLFCN)
	#include <dlfcn.h>
#elif defined(SJME_CONFIG_HAS_WINDOWS)
	#define WIN32_LEAN_AND_MEAN 1

	#include <windows.h>

	#undef WIN32_LEAN_AND_MEAN
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
	if (dlclose(inLib) == 0)
		return SJME_ERROR_COULD_NOT_UNLOAD_LIBRARY;
	
	/* Success! */
	return SJME_ERROR_NONE;
#elif defined(SJME_CONFIG_HAS_WINDOWS)
	if (FreeLibrary(inLib) == 0)
		return SJME_ERROR_COULD_NOT_UNLOAD_LIBRARY;
		
	/* Success! */
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
#endif
}

sjme_errorCode sjme_dylib_lookup(
	sjme_attrInNotNull sjme_dylib inLib,
	sjme_attrInNotNull sjme_lpcstr inSymbol,
	sjme_pointer* outPtr)
{
#if defined(SJME_CONFIG_DYLIB_HAS_DLFCN)
	sjme_pointer handle;
#elif defined(SJME_CONFIG_HAS_WINDOWS)
	FARPROC handle;
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
#elif defined(SJME_CONFIG_HAS_WINDOWS)
	handle = GetProcAddress(inLib, inSymbol);
	if (handle == NULL)
		return SJME_ERROR_INVALID_LIBRARY_SYMBOL;
	
	/* Success! */
	*outPtr = handle;
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
#endif
}

sjme_errorCode sjme_dylib_name(
	sjme_attrInNotNull sjme_lpcstr inLibName,
	sjme_attrOutNotNullBuf(outLen) sjme_lpstr outName,
	sjme_attrInPositive sjme_jint outLen)
{
	sjme_intPointer outNameBase;
	
	if (inLibName == NULL || outName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	outNameBase = (sjme_intPointer)outName;
	if (outLen < 0 || (outNameBase + outLen) < outNameBase)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Pointless operation? */
	if (outLen <= 1)
		return SJME_ERROR_NONE;
	
	/* Print what? */
#if defined(SJME_CONFIG_HAS_LINUX) || \
	defined(SJME_CONFIG_HAS_BSD) || \
    defined(SJME_CONFIG_HAS_BEOS)
	snprintf(outName, outLen - 1, "lib%s.so", inLibName);
#elif defined(SJME_CONFIG_HAS_CYGWIN)
	snprintf(outName, outLen - 1, "lib%s.dll", inLibName);
#elif defined(SJME_CONFIG_HAS_WINDOWS)
	snprintf(outName, outLen - 1, "%s.dll", inLibName);
#elif defined(SJME_CONFIG_HAS_MACOS)
	snprintf(outName, outLen - 1, "lib%s.dylib", inLibName);
#else
	return SJME_ERROR_NOT_IMPLEMENTED;
#endif
	
	/* Always add NULL. */
	outName[outLen - 1] = 0;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_dylib_open(
	sjme_attrInNotNull sjme_lpcstr libPath,
	sjme_attrInOutNotNull sjme_dylib* outLib)
{
#if defined(SJME_CONFIG_DYLIB_HAS_DLFCN)
	sjme_pointer handle;
#elif defined(SJME_CONFIG_HAS_WINDOWS)
	HMODULE handle;
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
#elif defined(SJME_CONFIG_HAS_WINDOWS)
	handle = LoadLibraryExA(libPath, NULL, 0);
	if (handle == NULL)
		return SJME_ERROR_COULD_NOT_LOAD_LIBRARY;
	
	/* Success! */
	*outLib = handle;
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
#endif
}
