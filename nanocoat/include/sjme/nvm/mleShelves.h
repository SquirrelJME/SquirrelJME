/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Contains references to the MLE shelves.
 * 
 * @since 2025/02/22
 */

#ifndef SQUIRRELJME_MLESHELVES_H
#define SQUIRRELJME_MLESHELVES_H

#include "sjme/nvm/mle.h"
#include "sjme/nvm/mleBrackets.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_MLESHELVES_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Integer type. */
#define SJME_MI SJME_JAVA_TYPE_ID_INTEGER

/** Object type. */
#define SJME_ML SJME_JAVA_TYPE_ID_OBJECT

/** Long type. */
#define SJME_MJ SJME_JAVA_TYPE_ID_LONG
	
/** Void type. */
#define SJME_MV SJME_JAVA_TYPE_ID_VOID

/** Builds a type descriptor. */
#define SJME_MD(rv, args) "(" args ")" rv

/** Array type. */
#define SJME_MD_A(component) "[" component

/** Boolean type. */
#define SJME_MD_Z "Z"
	
/** Byte type. */
#define SJME_MD_B "B"
	
/** Short type. */
#define SJME_MD_S "S"

/** Character type. */
#define SJME_MD_C "C"

/** Integer type. */
#define SJME_MD_I "I"

/** Long type. */
#define SJME_MD_J "J"

/** Float type. */
#define SJME_MD_F "F"

/** Double type. */
#define SJME_MD_D "D"

/** Void Type. */
#define SJME_MD_V "V"

/** Class type. */
#define SJME_MD_L(name) "L" name ";"

/** Array of booleans. */
#define SJME_MD_AZ SJME_MD_A(SJME_MD_Z)

/** Array of bytes. */
#define SJME_MD_AB SJME_MD_A(SJME_MD_B)

/** Array of shorts. */
#define SJME_MD_AS SJME_MD_A(SJME_MD_S)

/** Array of characters. */
#define SJME_MD_AC SJME_MD_A(SJME_MD_C)

/** Array of integers. */
#define SJME_MD_AI SJME_MD_A(SJME_MD_I)

/** Array of longs. */
#define SJME_MD_AJ SJME_MD_A(SJME_MD_J)

/** Array of floats. */
#define SJME_MD_AF SJME_MD_A(SJME_MD_F)

/** Array of doubles. */
#define SJME_MD_AD SJME_MD_A(SJME_MD_D)

/** Class. */
#define SJME_MD_CLASS SJME_MD_L("java/lang/Class")

/** Object. */
#define SJME_MD_OBJECT SJME_MD_L("java/lang/Object")

/** Pipe descriptor. */
#define SJME_MD_PIPE SJME_MD_L(SJME_NVM_BRACKET_NAME_PIPE)

/** Reference. */
#define SJME_MD_REFERENCE SJME_MD_L("java/lang/ref/Reference")

/** Reference Queue. */
#define SJME_MD_REFERENCE_QUEUE SJME_MD_L("java/lang/ref/ReferenceQueue")

/** Runnable. */
#define SJME_MD_RUNNABLE SJME_MD_L("java/lang/Runnable")

/** String. */
#define SJME_MD_STRING SJME_MD_L("java/lang/String")

/** Task. */
#define SJME_MD_TASK SJME_MD_L("cc/squirreljme/jvm/mle/brackets/TaskBracket")

/** Type. */
#define SJME_MD_TYPE SJME_MD_L("cc/squirreljme/jvm/mle/brackets/TypeBracket")

/** Thread. */
#define SJME_MD_THREAD SJME_MD_L("java/lang/Thread")

/** Trace point. */
#define SJME_MD_TRACE_POINT \
	SJME_MD_L("cc/squirreljme/jvm/mle/brackets/TracePointBracket")

/** VM Thread. */
#define SJME_MD_VM_THREAD \
	SJME_MD_L("cc/squirreljme/jvm/mle/brackets/VMThreadBracket")

/** MLE Function name. */
#define SJME_NVM_MLE_FUNCTION_NAME(name, alt) \
	SJME_TOKEN_PASTE4(sjme_nvm_mleFunc_, name, _, alt)

/** MLE Function definition. */
#define SJME_NVM_MLE_FUNCTION_DECL_ALT(name, alt) \
	sjme_errorCode SJME_NVM_MLE_FUNCTION_NAME(name, alt)( \
		sjme_attrInNotNull sjme_nvm_frame inFrame, \
		sjme_attrInNotNull sjme_jvalueTyped* argR, \
		sjme_attrInPositive sjme_jint argC, \
		sjme_attrInNullable sjme_jvalueTyped* argV)

/** MLE Function definition. */
#define SJME_NVM_MLE_FUNCTION_DECL(name) \
	SJME_NVM_MLE_FUNCTION_DECL_ALT(name, none)

/** Defines an MLE function. */
#define SJME_NVM_MLE_DEFINE_ALT(name, alt, type, argXR, argXA) \
	{ \
		#name, type, \
		argXR argXA, \
		SJME_NVM_MLE_FUNCTION_NAME(name, alt) \
	}

/** Defines an MLE function. */
#define SJME_NVM_MLE_DEFINE(name, type, argXR, argXA) \
	SJME_NVM_MLE_DEFINE_ALT(name, none, type, argXR, argXA)

/** Stop MLE definitions. */
#define SJME_NVM_MLE_STOP() \
	{NULL, NULL, NULL, NULL}

#define SJME_NVM_MLE_SHELF_DECLARE(what) \
	const sjme_nvm_mleShelf SJME_TOKEN_PASTE(sjme_nvm_mle, what)[]

extern SJME_NVM_MLE_SHELF_DECLARE(AtomicShelf);
extern SJME_NVM_MLE_SHELF_DECLARE(DebugShelf);
extern SJME_NVM_MLE_SHELF_DECLARE(JarPackageShelf);
extern SJME_NVM_MLE_SHELF_DECLARE(MathShelf);
extern SJME_NVM_MLE_SHELF_DECLARE(MidiShelf);
extern SJME_NVM_MLE_SHELF_DECLARE(NativeArchiveShelf);
extern SJME_NVM_MLE_SHELF_DECLARE(ObjectShelf);
extern SJME_NVM_MLE_SHELF_DECLARE(PencilFontShelf);
extern SJME_NVM_MLE_SHELF_DECLARE(PencilShelf);
extern SJME_NVM_MLE_SHELF_DECLARE(ReferenceShelf);
extern SJME_NVM_MLE_SHELF_DECLARE(ReflectionShelf);
extern SJME_NVM_MLE_SHELF_DECLARE(RuntimeShelf);
extern SJME_NVM_MLE_SHELF_DECLARE(StringShelf);
extern SJME_NVM_MLE_SHELF_DECLARE(TaskShelf);
extern SJME_NVM_MLE_SHELF_DECLARE(TerminalShelf);
extern SJME_NVM_MLE_SHELF_DECLARE(ThreadShelf);
extern SJME_NVM_MLE_SHELF_DECLARE(TypeShelf);
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_MLESHELVES_H
}
#undef SJME_CXX_MLESHELVES_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_MLESHELVES_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MLESHELVES_H */
