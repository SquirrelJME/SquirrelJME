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
 * Java Virtual Machine Objects.
 *
 * @since 2016/02/26
 */

/** Header guard. */
#ifndef SJME_hGVM_OBJH
#define SJME_hGVM_OBJH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXVM_OBJH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/* Configuration. */
#include "config.h"

/****************************************************************************/

/** Include the Java base. */
#include "vm_base.h"

/** Java object. */
struct jobject
{
	/** Class address area. */
	int addrclass;	
	
	/** The class that this object is owned by, if NULL then is a class. */
	union
	{
		sjme_addr_code jclass* code;
		sjme_addr_mem jclass* mem;
	} class;
	
	/** The identity hash code of this object. */
	jint idhashcode;
};

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXVM_OBJH
}
#undef SJME_cXVM_OBJH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXVM_OBJH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGVM_OBJH */

