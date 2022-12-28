/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdio.h>
#include "frontend/ieee1275/ieee1275.h"
#include "frontend/ieee1275/boot.h"
#include "frontend/ieee1275/devtree.h"

sjme_jupointer sjme_ieee1275BaseCall(sjme_ieee1275EntryFuncDef entryFunc,
	sjme_ieee1275Args* const args, sjme_jint* numRetVals)
{
	sjme_jint argCount;
	sjme_jint retCount;

	/* Remember return value count. */
	argCount = args->numArgs;
	retCount = args->retVal;

	/* Interrupts need to be disabled? */
#if defined(SJME_IEEE1275_DISABLE_INTS_FOR_CALLS)
	sjme_ieee1275InterruptsDisable();
#endif

	/* Call Function */
#if !defined(SJME_IEEE1275_UNSAFESAFE_ENTRY_CALL)
	/* Safe direct call. */
	entryFunc(args);
#else
	/* Calling directly from C is not safe */
#if 0
	IEEE1275_WrappedEntryCall(a_Entry, (IEEE1275_Addr_t)a_Data);
#endif
#endif

	/* Interrupts need to be enabled again? */
#if defined(SJME_IEEE1275_DISABLE_INTS_FOR_CALLS)
	sjme_ieee1275InterruptsEnable();
#endif

	/* Eh? Just always set number of return values? */
	if (numRetVals)
		*numRetVals = retCount;

	/* Returning something? */
	if (retCount > 0)
		return args->args[argCount];

	/* Zero for success otherwise. */
	return 0;
}

sjme_jupointer sjme_ieee1275Call(sjme_ieee1275Args* const args,
	sjme_jint* numRetVals)
{
	return sjme_ieee1275BaseCall(sjme_ieee1275EntryFunc,
		args, numRetVals);
}

