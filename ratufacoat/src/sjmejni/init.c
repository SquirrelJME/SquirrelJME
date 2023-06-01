/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "error.h"
#include "sjmejni/sjmejni.h"
#include "sjmejni/shadow.h"

sjme_jboolean sjme_vmNew(sjme_vmState** outVm, sjme_vmThread** outThread,
	sjme_vmCmdLine* vmArgs, const sjme_vmSysApi* sysApi, sjme_error* error)
{
	sjme_vmStateShadow* vmShadow;

	if (outVm == NULL || outThread == NULL || sysApi == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	

	sjme_todo("Implement this?");
	return sjme_false;
}
