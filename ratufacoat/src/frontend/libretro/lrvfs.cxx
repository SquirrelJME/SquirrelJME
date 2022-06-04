/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libretro/lrlocal.h"
#include "frontend/libretro/lrvfs.h"

static struct retro_vfs_interface* sjme_libRetro_vfs = NULL;

void sjme_libRetro_vfsSetEnvironment(retro_environment_t callback)
{
	struct retro_vfs_interface_info vfs_getter =
		{1, NULL};

	/* If VFS is available, initialize it. */
	if (callback(RETRO_ENVIRONMENT_GET_VFS_INTERFACE, &vfs_getter))
		if (vfs_getter.iface != NULL)
			g_libRetroCallbacks.vfs = vfs_getter.iface;
}
