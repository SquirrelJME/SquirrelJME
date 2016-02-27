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
 * Java Virtual Machine Class Support.
 *
 * @since 2016/02/26
 */

/** Header guard. */
#ifndef SJME_hGVM_CLASSH
#define SJME_hGVM_CLASSH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXVM_CLASSH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/* Configuration. */
#include "config.h"

/****************************************************************************/

/** Include the Java base. */
#include "vm_base.h"

/** Classes are objects. */
#include "vm_obj.h"

/** Class. */
struct jclass
{
	/** The self object data. */
	
	
	/** Package address area. */
	int addrpackage;	
	
	/** The owning package of this class. */
	union
	{
		sjme_addr_code jpackage* code;
		sjme_addr_mem jpackage* mem;
	} package;
};

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXVM_CLASSH
}
#undef SJME_cXVM_CLASSH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXVM_CLASSH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGVM_CLASSH */

