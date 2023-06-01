/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/ieee1275/io.h"

sjme_jint sjme_ieee1275Write(const sjme_ieee1275IHandle iHandle,
	const void* const src, const size_t srcLen)
{
	sjme_ieee1275Args args;

	/* Check */
	if (iHandle == SJME_IEEE1275_INVALID_IHANDLE || !src || !srcLen)
		return -1;

	/* Setup Call */
	memset(&args, 0, sizeof(args));

	/* Build */
	args.service = "write";
	args.numArgs = 3;
	args.retVal = 1;
	args.args[0] = iHandle;
	args.args[1] = (sjme_jupointer)src;
	args.args[2] = srcLen;

	/* Get property or just return size */
	return (sjme_jint)sjme_ieee1275Call(&args, NULL);
}