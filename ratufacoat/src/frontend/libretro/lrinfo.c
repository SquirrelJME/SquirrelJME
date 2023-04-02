/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjmerc.h"
#include "frontend/libretro/lrlocal.h"

/** Returns the supported RetroArch version. */
SJME_GCC_USED unsigned retro_api_version(void)
{
	return RETRO_API_VERSION;
}

/** Region. */
SJME_GCC_USED unsigned retro_get_region(void)
{
	return RETRO_REGION_NTSC;
}

/** Sets system information on RetroArch. */
SJME_GCC_USED void retro_get_system_info(struct retro_system_info* info)
{
	/* Wipe. */
	memset(info, 0, sizeof(*info));
	
	/* Set properties. */
	info->library_name = "squirreljme";
	info->library_version = SJME_STRINGIFY(SJME_VERSION)
		" (" SJME_STRINGIFY(SJME_VERSION_ID) ")";
	info->valid_extensions = "jar|sqc|jam|jad|kjx";
	
	/* Full path is not needed, but game data may be specified!. */
	info->need_fullpath = false;
	
	/* SquirrelJME works with JAR files, which are ZIP files. */
	info->block_extract = true;
}
