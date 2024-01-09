/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Builder for class files.
 * 
 * @since 2024/01/09
 */

#ifndef SQUIRRELJME_CLASSBUILDER_H
#define SQUIRRELJME_CLASSBUILDER_H

#include "test.h"
#include "sjme/stream.h"
#include "sjme/list.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CLASSBUILDER_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Contains field specific information.
 *
 * @since 2024/01/09
 */
typedef struct sjme_classBuilder_field
{
	/** Offsets. */
	struct
	{
		/** Offset to the name. */
		sjme_jint name;

		/** Offset to the type. */
		sjme_jint type;

		/** Offset to the constant value attribute. */
		sjme_jint attrConstVal;
	} offsets;
} sjme_classBuilder_field;

/** List of @c sjme_classBuilder_field . */
SJME_LIST_DECLARE(sjme_classBuilder_field, 0);

/**
 * Contains method specific information.
 *
 * @since 2024/01/09
 */
typedef struct sjme_classBuilder_method
{
	/** Offsets. */
	struct
	{
		/** Offset to the name. */
		sjme_jint name;

		/** Offset to the type. */
		sjme_jint type;

		/** Offset to the code attribute. */
		sjme_jint codeVal;

		/** Offset to the byte code. */
		sjme_jint byteCode;
	} offsets;
} sjme_classBuilder_method;

/** List of @c sjme_classBuilder_method . */
SJME_LIST_DECLARE(sjme_classBuilder_method, 0);

/**
 * Represents the state of the class builder.
 *
 * @since 2024/01/09
 */
typedef struct sjme_classBuilder
{
	/** The stream where the raw class data is written to. */
	sjme_stream_output stream;

	/** Whatever data that is needed. */
	void* whatever;

	/** Offsets to various structures. */
	struct
	{
		/** Offset to the constant pool. */
		sjme_jint pool;

		/** Offset to various constant pool entries. */
		sjme_list_sjme_jint* poolEntries;

		/** Offset to access flags. */
		sjme_jint flags;

		/** Offset to interfaces. */
		sjme_jint interfacesBase;

		/** Offset to individual interfaces. */
		sjme_list_sjme_jint* interfaces;

		/** Offset to base of fields. */
		sjme_jint fieldsBase;

		/** Offset to individual fields. */
		sjme_list_sjme_classBuilder_field* fields;

		/** Offset to base of methods. */
		sjme_jint methodsBase;

		/** Offset to individual methods. */
		sjme_list_sjme_classBuilder_method* methods;
	} offsets;
} sjme_classBuilder;

/**
 * Initializes the class builder.
 *
 * @param inPool The pool to allocate within.
 * @param outState The output state of the class builder.
 * @param whatever Whatever data is needed, this is optional.
 * @return Any resultant error, if any.
 * @since 2024/01/09
 */
sjme_errorCode sjme_classBuilder_build(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_classBuilder* outState,
	sjme_attrInNullable void* whatever);

/**
 * Finishes the construction of the raw class.
 *
 * @param inState The input class builder state.
 * @param rawClass The resultant raw class bytes to be parsed.
 * @return On any resultant error, if any.
 * @since 2024/01/09
 */
sjme_errorCode sjme_classBuilder_finish(
	sjme_attrOutNotNull sjme_classBuilder* inState,
	sjme_attrOutNotNull void** rawClass);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CLASSBUILDER_H
}
		#undef SJME_CXX_SQUIRRELJME_CLASSBUILDER_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CLASSBUILDER_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CLASSBUILDER_H */
