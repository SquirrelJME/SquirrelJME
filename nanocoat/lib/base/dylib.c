/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/path.h"

#if !defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	#if defined(SJME_CONFIG_HAS_OS_WINDOWS)
		#define WIN32_LEAN_AND_MEAN 1

		#include <windows.h>

		#undef WIN32_LEAN_AND_MEAN

		/* Make sure this is not included, on say mingw32. */
		#if defined(SJME_CONFIG_HAS_DLFCN_H)
			#undef SJME_CONFIG_HAS_DLFCN_H
		#endif
	#elif defined(SJME_CONFIG_HAS_DLFCN_H)
		#include <dlfcn.h>
	#endif
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
#elif defined(SJME_CONFIG_HAS_DLFCN_H)
	if (dlclose(inLib) == 0)
		return SJME_ERROR_COULD_NOT_UNLOAD_LIBRARY;
	
	/* Success! */
	return SJME_ERROR_NONE;
#elif defined(SJME_CONFIG_HAS_OS_WINDOWS)
	if (FreeLibrary(inLib) == 0)
		return SJME_ERROR_COULD_NOT_UNLOAD_LIBRARY;
		
	/* Success! */
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
}

sjme_errorCode sjme_dylib_lookup(
	sjme_attrInNotNull sjme_dylib inLib,
	sjme_attrInNotNull sjme_lpcstr inSymbol,
	sjme_pointer* outPtr)
{
#if defined(SJME_CONFIG_HAS_DLFCN_H)
	sjme_pointer handle;
#elif defined(SJME_CONFIG_HAS_OS_WINDOWS)
	FARPROC handle;
#if defined(SJME_CONFIG_HAS_ARCH_IA32)
#define BUF_SIZE 128
#define MAX_ATTEMPTS 64
	sjme_cchar mangled[BUF_SIZE];
	sjme_jint attempt;
#endif
#endif

	if (inLib == NULL || inSymbol == NULL || outPtr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
#if defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	return SJME_ERROR_UNSUPPORTED_OPERATION;
#elif defined(SJME_CONFIG_HAS_DLFCN_H)
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
#elif defined(SJME_CONFIG_HAS_OS_WINDOWS)
	handle = GetProcAddress(inLib, inSymbol);
	if (handle == NULL)
	{
#if defined(SJME_CONFIG_HAS_ARCH_IA32)
		/* Is the symbol mangled by stdcall? */
		for (attempt = 0; attempt < MAX_ATTEMPTS; attempt++)
		{
			/* Build a new symbol to lookup. */
			memset(mangled, 0, sizeof(mangled));
			snprintf(mangled, BUF_SIZE - 1, "_%s@%d",
				inSymbol, attempt);
			mangled[BUF_SIZE - 1] = 0;

			/* Lookup this symbol. */
			handle = GetProcAddress(inLib, mangled);
			if (handle != NULL)
				break;
		}

		/* Still not valid? */
		if (handle == NULL)
#endif
			return SJME_ERROR_INVALID_LIBRARY_SYMBOL;
	}
	
	/* Success! */
	*outPtr = handle;
	return SJME_ERROR_NONE;
#if defined(SJME_CONFIG_HAS_ARCH_IA32)
#undef BUF_SIZE
#undef MAX_ATTEMPTS
#endif
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
}

sjme_errorCode sjme_dylib_name(
	sjme_attrInNotNull sjme_lpcstr inLibName,
	sjme_attrInNullable sjme_lpcstr inLibSuffix,
	sjme_attrOutNotNullBuf(outLen) sjme_lpstr outName,
	sjme_attrInPositive sjme_jint outLen)
{
#if !defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	sjme_intPointer outNameBase;
#endif
	
	if (inLibName == NULL || outName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
#if !defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	outNameBase = (sjme_intPointer)outName;
	if (outLen < 0 || (outNameBase + outLen) < outNameBase)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Pointless operation? */
	if (outLen <= 1)
		return SJME_ERROR_NONE;
	
	/* Print what? */
#if defined(SJME_CONFIG_HAS_OS_LINUX) || \
	defined(SJME_CONFIG_HAS_OS_BSD) || \
    defined(SJME_CONFIG_HAS_OS_BEOS)
	snprintf(outName, outLen - 1, "lib%s%s.so",
		inLibName, (inLibSuffix != NULL ? inLibSuffix : ""));
#elif defined(SJME_CONFIG_HAS_OS_CYGWIN)
	snprintf(outName, outLen - 1, "lib%s%s.dll",
		inLibName, (inLibSuffix != NULL ? inLibSuffix : ""));
#elif defined(SJME_CONFIG_HAS_OS_WINDOWS)
	snprintf(outName, outLen - 1, "%s%s.dll",
		inLibName, (inLibSuffix != NULL ? inLibSuffix : ""));
#elif defined(SJME_CONFIG_HAS_OS_MACOS)
	snprintf(outName, outLen - 1, "lib%s%s.dylib",
		inLibName, (inLibSuffix != NULL ? inLibSuffix : ""));
#else
	return sjme_error_notImplemented(0);
#endif
	
	/* Always add NULL. */
	outName[outLen - 1] = 0;
	
	/* Success! */
	return SJME_ERROR_NONE;
#else
	return sjme_error_notImplemented(0);
#endif
}

sjme_errorCode sjme_dylib_open(
	sjme_attrInNotNull sjme_lpcstr libPath,
	sjme_attrInOutNotNull sjme_dylib* outLib)
{
#if defined(SJME_CONFIG_HAS_DLFCN_H)
	sjme_pointer handle;
#elif defined(SJME_CONFIG_HAS_OS_WINDOWS)
	HMODULE handle;
#endif
	
	if (libPath == NULL || outLib == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
#if defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	return SJME_ERROR_UNSUPPORTED_OPERATION;
#elif defined(SJME_CONFIG_HAS_DLFCN_H)
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
#elif defined(SJME_CONFIG_HAS_OS_WINDOWS)
	handle = LoadLibraryExA(libPath, NULL, 0);
	if (handle == NULL)
		return SJME_ERROR_COULD_NOT_LOAD_LIBRARY;
	
	/* Success! */
	*outLib = handle;
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
}

sjme_errorCode sjme_dylib_self(
	sjme_attrInOutNotNull sjme_dylib* outLib)
{
#if defined(SJME_CONFIG_HAS_DLFCN_H)
	sjme_pointer handle;
#elif defined(SJME_CONFIG_HAS_OS_WINDOWS)
	HMODULE handle;
#endif
	
	if (outLib == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
#if defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	return SJME_ERROR_UNSUPPORTED_OPERATION;
#elif defined(SJME_CONFIG_HAS_DLFCN_H)
	/* Attempt loading the library. */
	handle = dlopen(NULL, RTLD_NOW | RTLD_LOCAL);
	if (handle == NULL)
		return SJME_ERROR_COULD_NOT_LOAD_LIBRARY;
	
	/* Success! */
	*outLib = handle;
	return SJME_ERROR_NONE;
#elif defined(SJME_CONFIG_HAS_OS_WINDOWS)
	handle = GetModuleHandle(NULL);
	if (handle == NULL)
		return SJME_ERROR_COULD_NOT_LOAD_LIBRARY;
	
	/* Success! */
	*outLib = handle;
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
}
