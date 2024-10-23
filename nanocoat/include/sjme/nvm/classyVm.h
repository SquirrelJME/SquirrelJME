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
#include "sjme/nvm/rom.h"

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
 * A list of classes.
 * 
 * @since 2024/10/22
 */
SJME_LIST_DECLARE(sjme_jclass, 0);

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
	
	/** Read/write lock. */
	sjme_thread_rwLock rwLock;
	
	/** The class path to use. */
	sjme_list_sjme_nvm_rom_library* classPath;
	
	/** Classes which have been loaded. */
	sjme_list_sjme_jclass* classes;
};

/**
 * Loads the specified class by the given name.
 * 
 * @param inLoader The loader to use. 
 * @param outClass The resultant class.
 * @param contextThread The thread where any constructors can be called if
 * needed.
 * @param className The class to load. 
 * @return Any resultant error, if any.
 * @since 2024/10/19
 */
sjme_errorCode sjme_nvm_vmClass_loaderLoad(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_lpcstr className);

/**
 * Generates an array class type of the specified component type.
 * 
 * @param inLoader The loader to use. 
 * @param outClass The resultant class.
 * @param contextThread The thread where any constructors can be called if
 * needed.
 * @param componentType The component type of the array.
 * @param dims The number of dimensions of an array for the component type. 
 * @return Any resultant error, if any.
 * @since 2024/10/19
 */
sjme_errorCode sjme_nvm_vmClass_loaderLoadArray(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jclass componentType,
	sjme_attrInPositiveNonZero sjme_jint dims);

/**
 * Generates an array class type of the specified component type.
 * 
 * @param inLoader The loader to use. 
 * @param outClass The resultant class.
 * @param contextThread The thread where any constructors can be called if
 * needed.
 * @param componentType The component type of the array.
 * @param dims The number of dimensions of an array for the component type. 
 * @return Any resultant error, if any.
 * @since 2024/10/19
 */
sjme_errorCode sjme_nvm_vmClass_loaderLoadArrayA(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_lpcstr componentType,
	sjme_attrInPositiveNonZero sjme_jint dims);

/**
 * Loads the specified class by the given binary name.
 * 
 * @param inLoader The loader to use. 
 * @param outClass The resultant class.
 * @param contextThread The thread where any constructors can be called if
 * needed.
 * @param binaryName The binary name to load. 
 * @return Any resultant error, if any.
 * @since 2024/10/22
 */
sjme_errorCode sjme_nvm_vmClass_loaderLoadB(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_lpcstr binaryName);

/**
 * Generates a primitive class type.
 * 
 * @param inLoader The loader to use. 
 * @param outClass The resultant class.
 * @param contextThread The thread where any constructors can be called if
 * needed.
 * @param basicType The type of primitive type to create for.
 * @return Any resultant error, if any.
 * @since 2024/10/19
 */
sjme_errorCode sjme_nvm_vmClass_loaderLoadPrimitive(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NUM_BASIC_TYPE_IDS) sjme_basicTypeId basicType);

/**
 * Initializes a new class loader.
 * 
 * @param inState The input state.
 * @param outLoader The resultant class loader. 
 * @param classPath The classpath to use for the loader.
 * @return Any resultant error, if any.
 * @since 2024/10/19
 */
sjme_errorCode sjme_nvm_vmClass_loaderNew(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNotNull sjme_nvm_vmClass_loader* outLoader,
	sjme_attrInNotNull sjme_list_sjme_nvm_rom_library* classPath);

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
