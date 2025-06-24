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
 * The built-in locales which are supported.
 *
 * @since 2025/06/22
 */
typedef enum sjme_nvm_mle_builtInLocaleType
{
	/** Unspecified. */
	SJME_NVM_MLE_LOCALE_UNSPECIFIED = 0,

	/** English, US. */
	SJME_NVM_MLE_LOCALE_US_ENGLISH = 1,
} sjme_nvm_mle_builtInLocaleType;
	
/**
 * The type of line ending the system uses.
 *
 * @since 2025/03/02
 */
typedef enum sjme_nvm_mle_lineEndingType
{
	/** Unknown. */
	SJME_NVM_MLE_LINE_ENDING_UNSPECIFIED = 0,
	
	/** LF. */
	SJME_NVM_MLE_LINE_ENDING_LF = 1,
	
	/** CR. */
	SJME_NVM_MLE_LINE_ENDING_CR = 2,
	
	/** CRLF. */
	SJME_NVM_MLE_LINE_ENDING_CRLF = 3,
		
	/** Number of line ending types. */
	SJME_NVM_MLE_NUM_LINE_ENDINGS = 4,
} sjme_nvm_mle_lineEndingType;

/**
 * The memory profiles available.
 *
 * @since 2025/06/24
 */
typedef enum sjme_nvm_mle_memoryProfileType
{
	/** Minimal memory usage. */
	SJME_NVM_MLE_MEMORY_PROFILE_MINIMAL = -1,

	/** Normal memory usage. */
	SJME_NVM_MLE_MEMORY_PROFILE_NORMAL = 0,
} sjme_nvm_mle_memoryProfileType;
	
/**
 * Standard pipe descriptor IDs.
 *
 * @since 2025/02/23
 */
typedef enum sjme_nvm_mle_standardPipeType
{
	/** Standard input. */
	SJME_NVM_MLE_STD_PIPE_STDIN = 0,
	
	/** Standard output. */
	SJME_NVM_MLE_STD_PIPE_STDOUT = 1,
	
	/** Standard error. */
	SJME_NVM_MLE_STD_PIPE_STDERR = 2,
	
	/** The number of standard pipes. */
	SJME_NVM_MLE_NUM_STD_PIPES = 3,
} sjme_nvm_mle_standardPipeType;
	
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
