/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/win32/win32.h"
#include "lib/scritchui/win32/win32Intern.h"

sjme_errorCode sjme_scritchui_win32_intern_getLastError(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_errorCode ifOkay)
{
	DWORD winErr;
	
	if (inState == NULL)
		return SJME_ERROR_NONE;
	
	/* Get Windows error, but then also clear it. */
	winErr = GetLastError();
	SetLastError(0);
	if (winErr == ERROR_SUCCESS)
		return ifOkay;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("GetLastError() == %d 0x%08x",
		winErr, winErr);
#endif
	
	/* Did not actually map to anything? */
	return sjme_error_default(ifOkay);
}
