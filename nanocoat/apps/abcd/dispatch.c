/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/debug.h"
#include "dispatch.h"

/** Alternative name for invalid tokens. */
#define SJME_DISPATCH_ALT(altName, command) \
	{ \
		altName, \
		sjme_abcd_command_main(command), \
		sjme_abcd_command_help(command) \
	}

/** Dispatch which uses the same name. */
#define SJME_DISPATCH(command) \
	SJME_DISPATCH_ALT(#command, command)

static const sjme_dispatch_info sjme_abcd_dispatch[] =
{
	SJME_DISPATCH(cat),
	SJME_DISPATCH(ant),
	SJME_DISPATCH(cat),
	SJME_DISPATCH_ALT("device-address", device_address),
	SJME_DISPATCH_ALT("device-manager", device_manager),
	SJME_DISPATCH(doja),
	SJME_DISPATCH(doja_g),
	SJME_DISPATCH(eclipse),
	SJME_DISPATCH(emulator),
	SJME_DISPATCH(iconv),
	SJME_DISPATCH(jadtool),
	SJME_DISPATCH(jar),
	SJME_DISPATCH(jar2prc),
	SJME_DISPATCH(jasmin),
	SJME_DISPATCH(javac),
	SJME_DISPATCH(javadoc),
	SJME_DISPATCH(javap),
	SJME_DISPATCH(jdb),
	SJME_DISPATCH(mekeytool),
	SJME_DISPATCH(midp),
	SJME_DISPATCH(netbeans),
	SJME_DISPATCH(preverify),
	SJME_DISPATCH(star),
	SJME_DISPATCH(touch),
	SJME_DISPATCH(wscompile),
	SJME_DISPATCH(zip),
	
	/* End. */
	{NULL, NULL, NULL},
};

int main(int argc, char** argv)
{
	const sjme_nal* nal;
	
	/* Always use the default NAL. */
	nal = &sjme_nal_default;

	sjme_todo("Impl?");
	return EXIT_FAILURE;
}
