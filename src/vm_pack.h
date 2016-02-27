/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// --------------------------------------------------------------------------*/

/**
 * Virtual machine package support.
 *
 * @since 2016/02/26
 */

/** Header guard. */
#ifndef SJME_hGVM_PACKH
#define SJME_hGVM_PACKH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXVM_PACKH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/* Configuration. */
#include "config.h"

/****************************************************************************/

/** Include the Java base. */
#include "vm_base.h"

/** Package. */
struct jpackage
{
};

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXVM_PACKH
}
#undef SJME_cXVM_PACKH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXVM_PACKH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGVM_PACKH */

