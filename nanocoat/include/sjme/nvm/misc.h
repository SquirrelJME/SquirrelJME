/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Miscellaneous functions for NanoCoat.
 * 
 * @since 2026/11/05
 */

#ifndef SJME_C_SQUIRRELJME_MISC_H
#define SJME_C_SQUIRRELJME_MISC_H

#include "sjme/config.h"
#include "sjme/error.h"
#include "sjme/nvm/nvmTypeDefs.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_MISC_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Returns the calculated UUID of this device.
 * 
 * @param inState The input virtual machine state.
 * @param outUUID The resultant UUID.
 * @return The UUID of this device.
 * @since 2026/11/05
 */
sjme_errorCode sjme_nvm_misc_deviceId(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNotNull sjme_lpcstr* outUUID);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_MISC_H
}
#undef SJME_CXX_SQUIRRELJME_MISC_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_MISC_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_MISC_H */
