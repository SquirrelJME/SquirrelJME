/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/dylib.h"
#include "sjme/debug.h"

sjme_errorCode sjme_dylib_close(
	sjme_attrInNotNull sjme_dylib inLib)
{
#if defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	return SJME_ERROR_UNSUPPORTED_OPERATION;
#else
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
#endif
}

sjme_errorCode sjme_dylib_lookup(
	sjme_attrInNotNull sjme_dylib inLib,
	sjme_attrInNotNull sjme_lpcstr inSymbol,
	void* outPtr)
{
#if defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	return SJME_ERROR_UNSUPPORTED_OPERATION;
#else
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
#endif
}

sjme_errorCode sjme_dylib_open(
	sjme_attrInNotNull sjme_lpcstr libPath,
	sjme_attrInOutNotNull sjme_dylib* outLib)
{
#if defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	return SJME_ERROR_UNSUPPORTED_OPERATION;
#else
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
#endif
}
