/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Instances of objects.
 * 
 * @since 2024/09/08
 */

#ifndef SQUIRRELJME_INSTANCE_H
#define SQUIRRELJME_INSTANCE_H

#include "sjme/nvm/nvm.h"
#include "sjme/nvm/classyVm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_INSTANCE_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

struct sjme_jobjectBase
{
	/** Common base for all objects. */
	sjme_nvm_commonBase common;
	
	/** The identity hashcode. */
	sjme_jint identityHash;
	
	/** The current class that this is. */
	sjme_jclass isClass;
};

/**
 * Stores multiple field values for a given type.
 * 
 * @since 2024/10/27
 */
typedef struct sjme_nvm_fieldValues
{
	/** The number of items in this tread. */
	sjme_jint count;
	
	/** Values within the tread. */
	union
	{
		/** Boolean values. */
		sjme_jboolean jbooleans[sjme_flexibleArrayCountUnion];
		
		/** Byte values. */
		sjme_jbyte jbytes[sjme_flexibleArrayCountUnion];
		
		/** Short values. */
		sjme_jshort jshorts[sjme_flexibleArrayCountUnion];
		
		/** Character values. */
		sjme_jchar jchars[sjme_flexibleArrayCountUnion];
		
		/** Integer values. */
		sjme_jint jints[sjme_flexibleArrayCountUnion];
		
		/** Long values. */
		sjme_jlong jlongs[sjme_flexibleArrayCountUnion];
		
		/** Float values. */
		sjme_jfloat jfloats[sjme_flexibleArrayCountUnion];
		
		/** Double values. */
		sjme_jdouble jdoubles[sjme_flexibleArrayCountUnion];
		
		/** Object reference values. */
		sjme_jobject jobjects[sjme_flexibleArrayCountUnion];
	} values;
} sjme_nvm_fieldValues;

struct sjme_jclassBase
{
	/** All classes are objects. */
	sjme_jobjectBase object;
	
	/** The binary name of this class. */
	sjme_lpcstr binaryName;
	
	/** The has of the binary name. */
	sjme_jint binaryHash;
	
	/** Has the backing class data been loaded? */
	sjme_atomic_sjme_jint isLoaded;
	
	/** Is this class initialized? */
	sjme_atomic_sjme_jint isInitialized;
	
	/** The parsed class file information. */
	sjme_nvm_class_info info;
	
	/** The super class of this class. */
	sjme_atomic_sjme_jclass superClass;
	
	/** Interface classes for this class. */
	sjme_list_sjme_jclass* interfaceClasses;
	
	/** Field value storage. */
	sjme_nvm_fieldValues* staticFields[SJME_NUM_BASIC_TYPE_IDS];
};

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_INSTANCE_H
}
		#undef SJME_CXX_SQUIRRELJME_INSTANCE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_INSTANCE_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_INSTANCE_H */
