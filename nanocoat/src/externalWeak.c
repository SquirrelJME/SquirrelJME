/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/externalWeak.h"

sjme_attrWeak sjme_errorCode sjme_extern_scritchUiInterface(
	sjme_attrOutNotNull sjme_lpcstr* outInterface)
{
	if (outInterface == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	*outInterface = NULL;
	return SJME_ERROR_NONE;
}
