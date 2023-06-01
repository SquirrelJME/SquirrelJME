/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjmejni/sjmejni.h"
#include "frontend/ieee1275/ieee1275.h"
#include "frontend/ieee1275/devtree.h"

sjme_ieee1275PHandle sjme_ieee1275FindDevice(const char* const name)
{
	sjme_ieee1275Args args;

	/* Check */
	if (!name)
		return SJME_IEEE1275_INVALID_PHANDLE;

	/* Setup call */
	memset(&args, 0, sizeof(args));

	/* Build */
	args.service = "finddevice";
	args.numArgs = 1;
	args.retVal = 1;
	args.args[0] = (sjme_jupointer)name;

	return sjme_ieee1275Call(&args, NULL);
}

sjme_jint sjme_ieee1275GetIHandlePath(const sjme_ieee1275IHandle iHandle,
	char* const dest, const size_t destLen)
{
	sjme_ieee1275Args args;
	sjme_jint retVal;

	/* Check */
	if (iHandle == SJME_IEEE1275_INVALID_IHANDLE || dest == NULL ||
		destLen <= 0)
		return -1;

	/* Setup Call */
	memset(&args, 0, sizeof(args));

	/* Build */
	args.service = "instance-to-path";
	args.numArgs = 3;
	args.retVal = 1;
	args.args[0] = iHandle;
	args.args[1] = (sjme_jupointer)dest;
	args.args[2] = destLen - 1;

	/* Call */
	retVal = (sjme_jint)sjme_ieee1275Call(&args, NULL);

	/* Make last character in buffer always NUL */
	dest[destLen - 1] = 0;

	/* Return result */
	return retVal;
}

sjme_jint sjme_ieee1275GetProp(const sjme_ieee1275PHandle devNode,
	const char* const name, void* const dest, const size_t destLen)
{
	sjme_ieee1275Args args;
	sjme_jint fakeDest;

	/* Check */
	if (devNode == SJME_IEEE1275_INVALID_PHANDLE || name == NULL)
		return -1;

	/* Setup Call */
	memset(&args, 0, sizeof(args));

	/* Build */
	args.service = "getprop";
	args.numArgs = 4;
	args.retVal = 1;
	args.args[0] = devNode;
	args.args[1] = (sjme_jupointer)name;

	/* Place actual property value into destination */
	if (dest != NULL && destLen != 0)
	{
		args.args[2] = (sjme_jupointer)dest;
		args.args[3] = destLen;
	}

	/* Otherwise, just a size request */
	else
	{
		args.args[2] = (sjme_jupointer)&fakeDest;
		args.args[3] = sizeof(fakeDest);
	}

	/* Get property or just return size */
	return (sjme_jint)sjme_ieee1275Call(&args, NULL);
}

sjme_jint sjme_ieee1275GetPropAsInt(const sjme_ieee1275PHandle devNode,
	const char* const name, void* const dest, const size_t destLen)
{
	sjme_jint retVal;
	int32_t i;
	uint8_t temp;

	/* Call Handler */
	retVal = sjme_ieee1275GetProp(devNode, name, dest, destLen);

	/* Invalid? */
	if (retVal == -1)
		return -1;

	/* Swap all ends */
	/* Only do this for platforms that ask for it */
	/* If it turns out even the same platforms are screwed, then I will make */
	/* this a bug. */
#if defined(SJME_IEEE1275_SWAP_GET_PROP_AS_INT)
	for (i = 0; i < (RetVal >> 1); i++)
	{
		/* Remember left side */
		Temp = ((uint8_t*)a_Dest)[i];

		/* Set left from right */
		((uint8_t*)a_Dest)[i] = ((uint8_t*)a_Dest)[(RetVal - 1) - i];

		/* Set right from temporary */
		((uint8_t*)a_Dest)[(RetVal - 1) - i] = Temp;
	}
#endif

	/* Return the return value */
	return retVal;
}
