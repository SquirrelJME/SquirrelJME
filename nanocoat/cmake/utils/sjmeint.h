/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Standard int sizes.
 * 
 * @since 2023/12/01
 */

#ifndef SQUIRRELJME_SJMEINT_H
#define SQUIRRELJME_SJMEINT_H

#if defined(_MSC_VER)
	typedef unsigned __int8 uint8_t;
	typedef signed __int32 int32_t; 
#else
	#include <stdint.h>
#endif

#endif /* SQUIRRELJME_SJMEINT_H */
