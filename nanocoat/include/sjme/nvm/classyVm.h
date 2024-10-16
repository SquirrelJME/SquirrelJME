/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Classes as they appear to the runtime virtual machine.
 * 
 * @since 2024/09/08
 */

#ifndef SQUIRRELJME_CLASSYVM_H
#define SQUIRRELJME_CLASSYVM_H

#include "sjme/nvm/classy.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CLASSYVM_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Base structure for the class loader.
 * 
 * @since 2024/09/08
 */
typedef struct sjme_nvm_vmClass_loaderBase sjme_nvm_vmClass_loaderBase;

/**
 * Virtual machine equivalent to Java's @c ClassLoader .
 * 
 * @since 2024/09/08
 */
typedef sjme_nvm_vmClass_loaderBase* sjme_nvm_vmClass_loader;

struct sjme_nvm_vmClass_loaderBase
{
	/** Common NanoCoat storage. */
	sjme_nvm_commonBase common;
};

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CLASSYVM_H
}
		#undef SJME_CXX_SQUIRRELJME_CLASSYVM_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CLASSYVM_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CLASSYVM_H */
