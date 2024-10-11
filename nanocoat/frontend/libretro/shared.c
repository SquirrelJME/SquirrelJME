/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/nvm.h"
#include "sjme/debug.h"
#include "3rdparty/libretro/libretro.h"
#include "frontend/libretro/shared.h"

retro_environment_t sjme_libretro_envCallback = NULL;

retro_video_refresh_t sjme_libretro_videoRefreshCallback = NULL;
