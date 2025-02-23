/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * MLE constants.
 * 
 * @since 2025/02/23
 */

#ifndef SQUIRRELJME_MLECONST_H
#define SQUIRRELJME_MLECONST_H

#include "sjme/nvm/mle.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_MLECONST_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The virtual machine type.
 *
 * @since 2025/02/23
 */
typedef enum sjme_nvm_mle_vmType
{
	/** Not known. */
	SJME_NVM_MLE_VM_TYPE_UNKNOWN = 0,
	
	/** Running on Standard Java SE. */
	SJME_NVM_MLE_VM_TYPE_JAVA_SE = 1,
	
	/** Running on SpringCoat. */
	SJME_NVM_MLE_VM_TYPE_SPRINGCOAT = 2,
	
	/** Running on SummerCoat. */
	SJME_NVM_MLE_VM_TYPE_SUMMERCOAT = 3,
	
	/** Running on NanoCoat. */
	SJME_NVM_MLE_VM_TYPE_NANOCOAT = 4,
	
	/** The number of VM types. */
	SJME_NVM_MLE_NUM_VM_TYPES = 5,
} sjme_nvm_mle_vmType;

/** The NanoCoat Virtual Machine Type. */
#define SJME_NVM_MLE_VM_TYPE_NANOCOAT 4

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_MLECONST_H
}
#undef SJME_CXX_MLECONST_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_MLECONST_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MLECONST_H */
