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
 * Base virtual machine definitions.
 *
 * @since 2016/02/26
 */

/** Header guard. */
#ifndef SJME_hGVM_BASEH
#define SJME_hGVM_BASEH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXVM_BASEH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/* Configuration. */
#include "config.h"

/****************************************************************************/

/** Reference to parent is in code. */
#define SJME_IN_CODE	1

/** Reference to parent is in memory. */
#define SJME_IN_MEM		2

/** Objects. */
typedef struct jobject jobject;

/** Classes. */
typedef struct jclass jclass;

/** Packages. */
typedef struct jpackage jpackage;

/** String. */
typedef struct jstring jstring;

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXVM_BASEH
}
#undef SJME_cXVM_BASEH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXVM_BASEH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGVM_BASEH */

