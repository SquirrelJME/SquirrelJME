/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm.h"
#include "sjme/debug.h"
#include "3rdparty/libretro/libretro.h"

sjme_attrUnused RETRO_API void retro_set_input_poll(retro_input_poll_t poll)
{
	static sjme_jint trigger;
	if (!(trigger++))
		sjme_message("Implement this?");
}

sjme_attrUnused RETRO_API void retro_set_input_state(
	retro_input_state_t state)
{
	static sjme_jint trigger;
	if (!(trigger++))
		sjme_message("Implement this?");
}

sjme_attrUnused RETRO_API void retro_set_controller_port_device(
	unsigned port, unsigned device)
{
	sjme_todo("Implement this?");
}
