/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Class Members.
 * 
 * @file
 * @since 2026/01/12
 */

#ifndef SJME_C_SQUIRRELJME_CLASSY_VM_MEMBERS_H
#define SJME_C_SQUIRRELJME_CLASSY_VM_MEMBERS_H

#include "sjme/nvm/nvmTypeDefs.h"
#include "sjme/nvm/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_MEMBERS_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/
	
struct sjme_jmemberIDBase
{
	/** Common virtual machine info. */
	sjme_nvm_commonBase common;
	
	/** The class this member is in. */
	sjme_phantom(sjme_jclass) inClass;

	/** The identifier hash of this member. */
	sjme_jint idHash;
	
	/** The name of this member. */
	sjme_nvm_stringPool_string name;
	
	/** The type of this member. */
	sjme_nvm_stringPool_string type;
};

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_MEMBERS_H
}
#undef SJME_CXX_SQUIRRELJME_MEMBERS_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_MEMBERS_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_CLASSY_VM_MEMBERS_H */
