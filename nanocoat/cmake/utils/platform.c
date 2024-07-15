/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "../../include/sjme/config.h"

/**
 * Returns the platform.
 * 
 * @param argc Ignored. 
 * @param argv Ignored.
 * @return Always success.
 * @since 2024/07/05
 */
int main(int argc, char** argv)
{
	const char* platform;
	
	if (argc > 1 && strcmp("-p", argv[1]) == 0)
	{
#if defined(SJME_CONFIG_HAS_NINTENDO_3DS)
		platform = "3ds";
#elif defined(SJME_CONFIG_HAS_BEOS)
		platform = "beos";
#elif defined(SJME_CONFIG_HAS_BSD)
		platform = "bsd";
#elif defined(SJME_CONFIG_HAS_DOS)
		platform = "dos";
#elif defined(SJME_CONFIG_HAS_CYGWIN)
		platform = "cygwin";
#elif defined(SJME_CONFIG_HAS_LINUX)
		platform = "linux";
#elif defined(SJME_CONFIG_HAS_MACOS_CLASSIC)
		platform = "macos";
#elif defined(SJME_CONFIG_HAS_MACOS)
		platform = "macosx";
#elif defined(SJME_CONFIG_HAS_PALMOS)
		platform = "palmos";
#elif defined(SJME_CONFIG_HAS_WINDOWS_16)
		platform = "win16";
#elif defined(SJME_CONFIG_HAS_WINDOWS_32)
		platform = "win32";
#elif defined(SJME_CONFIG_HAS_WINDOWS_CE)
		platform = "wince";
#else
		platform = "unknown";
#endif
	}
	
	else if (argc > 1 && strcmp("-a", argv[1]) == 0)
	{
#if defined(SJME_CONFIG_HAS_ARCH_AMD64)
		platform = "amd64";
#elif defined(SJME_CONFIG_HAS_ARCH_ARM64)
		platform = "arm64";
#elif defined(SJME_CONFIG_HAS_ARCH_IA16)
		platform = "ia16";
#elif defined(SJME_CONFIG_HAS_ARCH_IA32)
		platform = "ia32";
#elif defined(SJME_CONFIG_HAS_ARCH_IA64)
		platform = "ia64";
#elif defined(SJME_CONFIG_HAS_ARCH_POWERPC) && \
	SJME_CONFIG_HAS_ARCH_POWERPC == 32
		platform = "powerpc32";
#elif defined(SJME_CONFIG_HAS_ARCH_POWERPC) && \
	SJME_CONFIG_HAS_ARCH_POWERPC == 64
		platform = "powerpc64";
#else
		platform = "unknown";
#endif
	}
	
	else
		platform = "unknown";
	
	fprintf(stdout, "%s", platform);
	fflush(stdout);
	return EXIT_SUCCESS;
}
