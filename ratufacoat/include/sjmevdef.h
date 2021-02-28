/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * SquirrelJME Virtual Machine Definitions.
 *
 * @since 2019/10/06
 */

/** Header guard. */
#ifndef SJME_hGRATUFACOATSJMFHSJMEVDEFH
#define SJME_hGRATUFACOATSJMFHSJMEVDEFH

#include "sjmerc.h"
#include "sjmecon.h"

/* Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXRATUFACOATSJMFHSJMEVDEFH
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Long return value result
 *
 * @since 2019/12/07
 */
typedef struct sjme_jlong_combine
{
	/** Low. */
	sjme_jint lo;
	
	/** High. */
	sjme_jint hi;
} sjme_jlong_combine;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXRATUFACOATSJMFHSJMEVDEFH
}
#undef SJME_cXRATUFACOATSJMFHSJMEVDEFH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXRATUFACOATSJMFHSJMEVDEFH */
#endif /* #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGRATUFACOATSJMFHSJMEVDEFH */

