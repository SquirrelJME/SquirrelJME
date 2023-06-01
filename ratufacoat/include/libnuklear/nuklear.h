/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjmejni/sjmejni.h"

/* Needed for fixed size types. */
#if !defined(NK_INCLUDE_FIXED_TYPES)
	#define NK_INCLUDE_FIXED_TYPES
#endif

/* If bare metal, skip these. */
#if defined(SJME_BARE_METAL)
	/* Skip this. */
	#define NK_INCLUDE_STANDARD_VARARGS
#endif

/* Include implementation header. */
#include "libnuklear/nuklear2.h"