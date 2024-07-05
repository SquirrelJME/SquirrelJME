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
	
	fprintf(stdout, "%s", platform);
	fflush(stdout);
	return EXIT_SUCCESS;
}
