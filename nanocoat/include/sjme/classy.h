/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Handling of classes.
 * 
 * @since 2024/01/01
 */

#ifndef SQUIRRELJME_CLASSY_H
#define SQUIRRELJME_CLASSY_H

#include "sjme/nvm.h"
#include "sjme/seekable.h"
#include "sjme/list.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CLASSY_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Represents a field descriptor.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_fieldDescriptor
{
	/** The hash of this descriptor. */
	sjme_jint hash;

	/** How deep this is in array terms. */
	sjme_jubyte arrayDepth;

	/** The core type, without any array signifiers. */
	sjme_cchar coreType[sjme_flexibleArrayCountUnion];
} sjme_class_fieldDescriptor;

/**
 * Field descriptor list.
 *
 * @since 2024/01/03
 */
SJME_LIST_DECLARE(sjme_class_fieldDescriptor, 0);

/** The basic type of @c sjme_class_fieldDescriptor . */
#define SJME_BASIC_TYPEOF_sjme_class_fieldDescriptor \
	SJME_BASIC_TYPE_ID_OBJECT

/**
 * Represents a method descriptor.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_methodDescriptor
{
	/** The hash of this descriptor. */
	sjme_jint hash;

	/** The return type. */
	sjme_class_fieldDescriptor returnType;

	/** Arguments to the method. */
	sjme_list_sjme_class_fieldDescriptor arguments;
} sjme_class_methodDescriptor;

/**
 * The descriptor of a class.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_classDescriptor
{
	/** The hash of this descriptor. */
	sjme_jint hash;

	/** Is this a field descriptor? */
	sjme_jboolean isField : 1;

	/** The actual descriptor. */
	union {
		/** As a field descriptor. */
		sjme_class_fieldDescriptor field;

		/** As an actual string. */
		sjme_cchar binary[sjme_flexibleArrayCountUnion];
	} descriptor;
} sjme_class_classDescriptor;

/**
 * Core class information structure.
 *
 * @since 2024/01/01
 */
typedef struct sjme_class_infoCore sjme_class_infoCore;

/**
 * Opaque class information structure.
 *
 * @since 2024/01/01
 */
typedef struct sjme_class_infoCore* sjme_class_info;

/**
 * Core method information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_methodInfoCore sjme_class_methodInfoCore;

/**
 * Opaque method information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_methodInfoCore* sjme_class_methodInfo;

/**
 * Method list.
 *
 * @since 2024/01/03
 */
SJME_LIST_DECLARE(sjme_class_methodInfo, 0);

/** The basic type of @c sjme_class_methodInfo . */
#define SJME_BASIC_TYPEOF_sjme_class_methodInfo \
	SJME_BASIC_TYPE_ID_OBJECT

/**
 * Core field information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_fieldInfoCore sjme_class_fieldInfoCore;

/**
 * Opaque field information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_fieldInfoCore* sjme_class_fieldInfo;

/**
 * Field list.
 *
 * @since 2024/01/03
 */
SJME_LIST_DECLARE(sjme_class_fieldInfo, 0);

/** The basic type of @c sjme_class_fieldInfo . */
#define SJME_BASIC_TYPEOF_sjme_class_fieldInfo \
	SJME_BASIC_TYPE_ID_OBJECT

/**
 * Exception handling information.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_exceptionHandler
{
	/** The range of the exception where it applies. */
	sjme_range range;

	/** The handler PC address. */
	sjme_jint handlerPc;

	/** The type that this catches. */
	sjme_class_classDescriptor handles;
} sjme_class_exceptionHandler;

/** A list of exceptions. */
SJME_LIST_DECLARE(sjme_class_exceptionHandler, 0);

/** The basic type of @c sjme_class_exceptionHandler . */
#define SJME_BASIC_TYPEOF_sjme_class_exceptionHandler \
	SJME_BASIC_TYPE_ID_OBJECT

/**
 * Method code information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_codeInfoCore sjme_class_codeInfoCore;

/**
 * Opaque method code structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_codeInfoCore* sjme_class_codeInfo;

/**
 * Access flags.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_accessFlags
{
	/** Is this public? */
	sjme_jboolean public : 1;

	/** Is this private? */
	sjme_jboolean private : 1;

	/** Is this protected? */
	sjme_jboolean protected : 1;
} sjme_class_accessFlags;

/**
 * Class flags.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_classFlags
{
	/** Access flags. */
	sjme_class_accessFlags access;

	/** Is the class final? */
	sjme_jboolean final : 1;

	/** Is the class super? */
	sjme_jboolean super : 1;

	/** Is the class an interface? */
	sjme_jboolean interface : 1;

	/** Is the class abstract? */
	sjme_jboolean abstract : 1;

	/** Is the class synthetic? */
	sjme_jboolean synthetic : 1;

	/** Is the class an annotation? */
	sjme_jboolean annotation : 1;

	/** Is the class an enum? */
	sjme_jboolean enumeration : 1;
} sjme_class_classFlags;

/**
 * Common member flags.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_memberFlags
{
	/** Access flags. */
	sjme_class_accessFlags access;

	/** Static member? */
	sjme_jboolean isStatic : 1;

	/** Final member? */
	sjme_jboolean final : 1;

	/** Synthetic member? */
	sjme_jboolean synthetic : 1;
} sjme_class_memberFlags;

/**
 * Field flags.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_fieldFlags
{
	/** Common member flags. */
	sjme_class_memberFlags member;

	/** Is this volatile? */
	sjme_jboolean isVolatile : 1;

	/** Is this transient? */
	sjme_jboolean transient : 1;

	/** Is this an enumeration. */
	sjme_jboolean enumeration : 1;
} sjme_class_fieldFlags;

/**
 * Method flags.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_methodFlags
{
	/** Common member flags. */
	sjme_class_memberFlags member;

	/** Synchronized, monitor entry/exit on call? */
	sjme_jboolean synchronized : 1;

	/** Bridge method, generated by the compiler? */
	sjme_jboolean bridge : 1;

	/** Variadic arguments? */
	sjme_jboolean varargs : 1;

	/** Is this a native method? */
	sjme_jboolean native : 1;

	/** Abstract? */
	sjme_jboolean abstract : 1;

	/** Strict floating point? */
	sjme_jboolean strictfp : 1;
} sjme_class_methodFlags;

struct sjme_class_infoCore
{
	/** The name of this class. */
	sjme_lpcstr name;

	/** The superclass of this class. */
	sjme_lpcstr superName;

	/** The interfaces this class implements. */
	const sjme_list_sjme_lpcstr* interfaceNames;

	/** Fields within the method. */
	const sjme_list_sjme_class_fieldInfo* fields;

	/** Methods within the class. */
	const sjme_list_sjme_class_methodInfo* methods;
};

struct sjme_class_fieldInfoCore
{
	/** Field flags. */
	sjme_class_fieldFlags flags;
};

struct sjme_class_methodInfoCore
{
	/** Method flags. */
	sjme_class_methodFlags flags;

	/** The method code, if it is not native. */
	sjme_class_codeInfo code;
};

struct sjme_class_codeInfoCore
{
	/** Maximum number of locals. */
	sjme_jint maxLocals;

	/** Maximum number of stack entries. */
	sjme_jint maxStack;

	/** Exception table. */
	const sjme_list_sjme_class_exceptionHandler* exceptions;

	/** Method byte code. */
	sjme_list_sjme_jubyte code;
};

/**
 * Parses a single class and loads its class information.
 *
 * @param inPool The pool to allocate within.
 * @param inStream The stream to parse from when reading the class.
 * @param outClass The resultant class information
 * @return Any resultant error code, if any.
 * @since 2024/01/03
 */
sjme_errorCode sjme_class_parse(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_class_info* outClass);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CLASSY_H
}
		#undef SJME_CXX_SQUIRRELJME_CLASSY_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CLASSY_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CLASSY_H */
