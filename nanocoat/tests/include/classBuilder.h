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

#include "sjme/classy.h"
#include "sjme/list.h"
#include "sjme/stream.h"
#include "test.h"

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
	sjme_pointer whatever;

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
 * Adds an attribute to the class, field, method, or code attribute.
 * 
 * @param order The order of evaluation, or if evaluating now this will be
 * the value @c -1 .
 * @param builder The class being built.
 * @param inNameIndex The index to the name of the attribute.
 * @param inData The attribute data.
 * @param inLength The attribute length.
 * @return On any resultant errors, if any.
 * @since 2024/01/16
 */
sjme_errorCode sjme_classBuilder_addAttribute(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrInPositiveNonZero sjme_jint inNameIndex,
	sjme_attrInNotNull void* inData,
	sjme_attrInPositive sjme_jint inLength);

/**
 * Adds a @c ConstantValue attribute to the current field being built.
 * 
 * @param order The order of evaluation, or if evaluating now this will be
 * the value @c -1 .
 * @param builder The class builder.
 * @param inValueIndex The index to the constant value.
 * @return On any errors, if any.
 * @since 2024/01/16 
 */
sjme_errorCode sjme_classBuilder_addAttributeConstantValue(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrInPositiveNonZero const sjme_jint* inValueIndex);

/**
 * Adds a field member.
 * 
 * @param order The order of evaluation, or if evaluating now this will be
 * the value @c -1 .
 * @param builder The class being built.
 * @param flags The field flags.
 * @param inNameIndex The index to the name of the field.
 * @param inTypeIndex The index to the type of the field.
 * @return On any resultant errors, if any.
 * @since 2024/01/16
 */
sjme_errorCode sjme_classBuilder_addField(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrInValue sjme_jint flags,
	sjme_attrInPositiveNonZero const sjme_jint* inNameIndex,
	sjme_attrInPositiveNonZero const sjme_jint* inTypeIndex);

/**
 * Adds a method member.
 * 
 * @param order The order of evaluation, or if evaluating now this will be
 * the value @c -1 .
 * @param builder The class being built.
 * @param flags The method flags.
 * @param inNameIndex The index to the name of the method.
 * @param inTypeIndex The index to the type of the method.
 * @return On any resultant errors, if any.
 * @since 2024/01/16
 */
sjme_errorCode sjme_classBuilder_addMethod(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrInValue sjme_jint flags,
	sjme_attrInPositiveNonZero const sjme_jint* inNameIndex,
	sjme_attrInPositiveNonZero const sjme_jint* inTypeIndex);

/**
 * Adds a constant pool to the class.
 * 
 * @param order The order of evaluation, or if evaluating now this will be
 * the value @c -1 .
 * @param builder The builder to add to.
 * @param poolSize The number of entries in the pool
 * @return On any resultant error, if any.
 * @since 2024/01/16
 */
sjme_errorCode sjme_classBuilder_addPool(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrInPositiveNonZero sjme_jint poolSize);

/**
 * Adds a class info entry into the constant pool of the class being built.
 * 
 * @param order The order of evaluation, or if evaluating now this will be
 * the value @c -1 .
 * @param builder The builder to add to.
 * @param outIndex The resultant index of the entry.
 * @param inUtfIndex The index of the Utf entry to use for the class info. 
 * @return On any errors, if any.
 * @since 2024/01/16
 */
sjme_errorCode sjme_classBuilder_addPoolEntryClass(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNullable sjme_jint* outIndex,
	sjme_attrInPositiveNonZero const sjme_jint* inUtfIndex);

/**
 * Adds a constant value to the constant pool of the class being built.
 * 
 * @param order The order of evaluation, or if evaluating now this will be
 * the value @c -1 .
 * @param builder The builder to add to.
 * @param outIndex The resultant index of this entry.
 * @param javaType The type of value to add.
 * @param value The value of the given type.
 * @return On any resultant errors, if any.
 * @since 2024/01/16
 */
sjme_errorCode sjme_classBuilder_addPoolEntryConstVal(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNullable sjme_jint* outIndex,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId javaType,
	sjme_attrInValue sjme_jvalue value);

/**
 * Adds a member reference to the constant pool.
 * 
 * @param order The order of evaluation, or if evaluating now this will be
 * the value @c -1 .
 * @param builder The class builder to add.
 * @param outIndex The resultant index of this entry.
 * @param type The type of entry to add.
 * @param inClassIndex The index of the constant pool entry to the class.
 * @param inNameAndTypeIndex The index of the constant pool entry to the name
 * and type.
 * @return On any resultant error, if any.
 * @since 2024/01/16
 */
sjme_errorCode sjme_classBuilder_addPoolEntryMemberRef(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNullable sjme_jint* outIndex,
	sjme_attrInRange(SJME_CLASS_POOL_ENTRY_MEMBER_TYPE_FIELD,
		SJME_NUM_CLASS_POOL_ENTRY_MEMBER_TYPE)
			sjme_class_poolEntryMemberType type,
	sjme_attrInPositiveNonZero const sjme_jint* inClassIndex,
	sjme_attrInPositiveNonZero const sjme_jint* inNameAndTypeIndex);

/**
 * Adds a name and type entry to the constant pool.
 * 
 * @param order The order of evaluation, or if evaluating now this will be
 * the value @c -1 .
 * @param builder The class builder to add it. 
 * @param outIndex The resultant index of the added entry.
 * @param inNameUtfIndex The index to the UTF entry for the member name.
 * @param inTypeUtfIndex The index to the UTF entry for the member type.
 * @return On any resultant errors, if any.
 * @since 2024/01/16
 */
sjme_errorCode sjme_classBuilder_addPoolEntryNameAndType(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNullable sjme_jint* outIndex,
	sjme_attrInPositiveNonZero const sjme_jint* inNameUtfIndex,
	sjme_attrInPositiveNonZero const sjme_jint* inTypeUtfIndex);

/**
 * Adds a string entry to the constant pool.
 * 
 * @param order The order of evaluation, or if evaluating now this will be
 * the value @c -1 .
 * @param builder The class to add the entry to.
 * @param outIndex The resultant index of the added entry.
 * @param utfIndex The entry to the UTF entry.
 * @return On any errors, if any.
 * @since 2024/01/16
 */
sjme_errorCode sjme_classBuilder_addPoolEntryString(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNullable sjme_jint* outIndex,
	sjme_attrInPositiveNonZero const sjme_jint* inUtfIndex);

/**
 * Adds a single UTF constant pool entry.
 * 
 * @param order The order of evaluation, or if evaluating now this will be
 * the value @c -1 .
 * @param builder The builder to add to.
 * @param outIndex The resultant index of the added entry.
 * @param inUtf The string to store into the pool.
 * @return On any errors, if any.
 * @since 2024/01/16
 */
sjme_errorCode sjme_classBuilder_addPoolEntryUtf(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNullable sjme_jint* outIndex,
	sjme_attrInNotNull sjme_lpcstr inUtf);

/**
 * Initializes the class builder.
 *
 * @param inPool The pool to allocate within.
 * @param outBuilder The output state of the class builder.
 * @param allowInvalid Allow invalid classes to be generated, this can be used
 * for testing.
 * @param whatever Whatever data is needed, this is optional.
 * @return Any resultant error, if any.
 * @since 2024/01/09
 */
sjme_errorCode sjme_classBuilder_build(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_classBuilder* outBuilder,
	sjme_attrInValue sjme_jboolean allowInvalid,
	sjme_attrInNullable void* whatever);

/**
 * Declares the primary class information.
 * 
 * @param order The order of evaluation, or if evaluating now this will be
 * the value @c -1 .
 * @param builder The class being built.
 * @param classFlags The flags for the class.
 * @param inClassName The index to the class name.
 * @param inSuperName The index to the super class name, may be @c NULL .
 * @param numInterfaceNames The number of interface names.
 * @param inInterfaceNameIndexes An array of interface names.
 * @return On any errors, if any.
 * @since 2024/01/16
 */
sjme_errorCode sjme_classBuilder_declareClassA(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrInValue sjme_jint classFlags,
	sjme_attrInPositive const sjme_jint* inClassNameIndex,
	sjme_attrInPositive const sjme_jint* inSuperNameIndex,
	sjme_attrInPositive sjme_jint numInterfaceNames,
	sjme_attrInNotNull const sjme_jint** inInterfaceNameIndexes);

/**
 * Declares the primary class information.
 * 
 * @param order The order of evaluation, or if evaluating now this will be
 * the value @c -1 .
 * @param builder The class being built.
 * @param classFlags The flags for the class.
 * @param inClassName The index to the class name.
 * @param inSuperName The index to the super class name, may be @c NULL .
 * @param inInterfaceNameIndexes The list of interface names.
 * @return On any errors, if any.
 * @since 2024/01/16
 */
sjme_errorCode sjme_classBuilder_declareClassL(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrInValue sjme_jint classFlags,
	sjme_attrInPositive const sjme_jint* inClassNameIndex,
	sjme_attrInPositive const sjme_jint* inSuperNameIndex,
	sjme_attrInNotNull sjme_list_sjme_jintP* inInterfaceNameIndexes);

/**
 * Declares the primary class information.
 * 
 * @param order The order of evaluation, or if evaluating now this will be
 * the value @c -1 .
 * @param builder The class being built.
 * @param classFlags The flags for the class.
 * @param inClassName The index to the class name.
 * @param inSuperName The index to the super class name, may be @c NULL .
 * @param numInterfaceNames The number of interface names.
 * @param ... The indexes to the interface names.
 * @return On any errors, if any.
 * @since 2024/01/16
 */
sjme_errorCode sjme_classBuilder_declareClassV(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrInValue sjme_jint classFlags,
	sjme_attrInPositive const sjme_jint* inClassNameIndex,
	sjme_attrInPositive const sjme_jint* inSuperNameIndex,
	sjme_attrInPositive sjme_jint numInterfaceNames,
	...);

/**
 * Finishes the construction of the raw class.
 *
 * @param builder The input class builder state.
 * @param rawClass The resultant raw class bytes to be parsed.
 * @return On any resultant error, if any.
 * @since 2024/01/09
 */
sjme_errorCode sjme_classBuilder_finish(
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNotNull sjme_pointer* rawClass);

/**
 * Finishes the current attribute and moves onto the next. 
 * 
 * @param order The order of evaluation, or if evaluating now this will be
 * the value @c -1 .
 * @param builder The class currently being built.
 * @return On any resultant errors, if any.
 * @since 2024/01/16 
 */
sjme_errorCode sjme_classBuilder_nextAttribute(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder);
	
/**
 * Finishes the current member and moves onto the next. 
 * 
 * @param order The order of evaluation, or if evaluating now this will be
 * the value @c -1 .
 * @param builder The class currently being built.
 * @return On any resultant errors, if any.
 * @since 2024/01/16 
 */
sjme_errorCode sjme_classBuilder_nextMember(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder);

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
