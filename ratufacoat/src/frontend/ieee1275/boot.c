/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/ieee1275/boot.h"
#include "frontend/ieee1275/devtree.h"
#include "frontend/ieee1275/ieee1275.h"
#include "frontend/ieee1275/io.h"

sjme_ieee1275EntryFuncDef sjme_ieee1275EntryFunc;

const char* sjme_ieee1275EntryArgs;

sjme_juint sjme_ieee1275EntryNumArgs;

sjme_ieee1275PHandle sjme_ieee1275ChosenPHandle;

/** Standard output handle. */
static sjme_ieee1275IHandle sjme_ieee1275StdOutHandle;

/** Standard output file. */
static FILE sjme_ieee1275StdOutFile;

static int sjme_ieee1275StdOutWrite(FILE* file, void* buf, size_t off,
	size_t len)
{
	return sjme_ieee1275Write(sjme_ieee1275StdOutHandle,
		(void*)(((sjme_jupointer)buf) + off), len);
}

void sjme_ieee1275Boot(void)
{
	sjme_jint nodeLen;

	/* Locate the chosen device, needed for almost every function. */
	sjme_ieee1275ChosenPHandle = sjme_ieee1275FindDevice("/chosen");
	if (sjme_ieee1275ChosenPHandle == SJME_IEEE1275_INVALID_PHANDLE)
		sjme_ieee1275Exit();

	/* Locate stdout device. */
	nodeLen = sjme_ieee1275GetPropAsInt(sjme_ieee1275ChosenPHandle,
		"stdout", &sjme_ieee1275StdOutHandle,
		sizeof(sjme_ieee1275StdOutHandle));
	if (nodeLen != sizeof(sjme_ieee1275StdOutHandle))
		sjme_ieee1275Exit();

	/* Set stdout function for write. */
	stdout = &sjme_ieee1275StdOutFile;
	stdout->write = sjme_ieee1275StdOutWrite;

	/* Make stderr a copy of stdout. */
	stderr = stdout;

	/* Emit a message. */
	fputs("SquirrelJME " SJME_STRINGIFY(SQUIRRELJME_VERSION) "\r\n",
		stderr);
	fputs("Copyright (C) 2013-2022 Stephanie Gawroriski"
		  " (xer@multiphasicapps.net)\r\n", stderr);
	fputs("https://squirreljme.cc/\r\n", stderr);
	fputs("\r\n", stderr);

}

void sjme_ieee1275Exit(void)
{
	sjme_ieee1275Args args;

	/* Setup call to */
	memset(&args, 0, sizeof(args));

	/* Build */
	args.service = "exit";
	args.numArgs = 0;
	args.retVal = 0;

	/* This returns no arguments and will never ever return! */
	sjme_ieee1275Call(&args, NULL);
}
