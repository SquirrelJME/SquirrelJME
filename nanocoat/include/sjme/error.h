/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Error codes.
 * 
 * @since 2024/08/09
 */

#ifndef SQUIRRELJME_ERROR_H
#define SQUIRRELJME_ERROR_H

#include "sjme/stdTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_ERROR_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Error codes.
 * 
 * @since 2023/11/14
 */
typedef enum sjme_errorCode
{
	/** No error. */
	SJME_ERROR_NONE = 1,

	/** Generic unknown error. */
	SJME_ERROR_UNKNOWN = 0,

	/** Generic unknown error. */
	SJME_ERROR_UNKNOWN_NEGATIVE = -1,
	
	/** Null arguments. */
	SJME_ERROR_NULL_ARGUMENTS = -2,
	
	/** Local variable out of bounds. */
	SJME_ERROR_LOCAL_INDEX_INVALID = -3,
	
	/** Stack variable out of bounds. */
	SJME_ERROR_STACK_INDEX_INVALID = -4,
	
	/** Stack underflow. */
	SJME_ERROR_STACK_UNDERFLOW = -5,
	
	/** Stack overflow. */
	SJME_ERROR_STACK_OVERFLOW = -6,
	
	/** Top is not an integer type. */
	SJME_ERROR_TOP_NOT_INTEGER = -7,
	
	/** Top is not a long type. */
	SJME_ERROR_TOP_NOT_LONG = -8,
	
	/** Top is not a float type. */
	SJME_ERROR_TOP_NOT_FLOAT = -9,
	
	/** Top is not a double type. */
	SJME_ERROR_TOP_NOT_DOUBLE = -10,
	
	/** Top is not a object type. */
	SJME_ERROR_TOP_NOT_OBJECT = -11,
	
	/** Frame is missing stack treads. */
	SJME_ERROR_FRAME_MISSING_STACK_TREADS = -12,
	
	/** Invalid read of stack. */
	SJME_ERROR_STACK_INVALID_READ = -13,
	
	/** Invalid write of stack. */
	SJME_ERROR_STACK_INVALID_WRITE = -14,
	
	/** Invalid read of stack. */
	SJME_ERROR_LOCAL_INVALID_READ = -15,
	
	/** Invalid write of stack. */
	SJME_ERROR_LOCAL_INVALID_WRITE = -16,
	
	/** Invalid reference pop. */
	SJME_ERROR_INVALID_REFERENCE_POP = -17,
	
	/** Invalid reference push. */
	SJME_ERROR_INVALID_REFERENCE_PUSH = -18,
	
	/** Failed to garbage collect object. */
	SJME_ERROR_COULD_NOT_GC_OBJECT = -19,
	
	/** Object reference count is not zero. */
	SJME_ERROR_OBJECT_REFCOUNT_NOT_ZERO = -20,
	
	/** Garbage collection of object cancelled. */
	SJME_ERROR_OBJECT_GC_CANCELLED = -21,

	/** Out of memory. */
	SJME_ERROR_OUT_OF_MEMORY = -22,

	/** Pool initialization failed. */
	SJME_ERROR_POOL_INIT_FAILED = -23,

	/** Invalid argument. */
	SJME_ERROR_INVALID_ARGUMENT = -24,

	/** Not implemented. */
	SJME_ERROR_NOT_IMPLEMENTED = -25,

	/** Invalid tread read. */
	SJME_ERROR_TREAD_INVALID_READ = -26,

	/** Invalid tread write. */
	SJME_ERROR_TREAD_INVALID_WRITE = -27,

	/** There are no suites available. */
	SJME_ERROR_NO_SUITES = -28,

	/** Classpath cannot be obtained by both ID and Name. */
	SJME_ERROR_CLASS_PATH_BY_BOTH = -29,

	/** Illegal state. */
	SJME_ERROR_ILLEGAL_STATE = -30,

	/** A library was not found. */
	SJME_ERROR_LIBRARY_NOT_FOUND = -31,

	/** Boot failure. */
	SJME_ERROR_BOOT_FAILURE = -32,

	/** Generic JNI exception. */
	SJME_ERROR_JNI_EXCEPTION = -33,

	/** Memory has been corrupted. */
	SJME_ERROR_MEMORY_CORRUPTION = -34,

	/** Index out of bounds. */
	SJME_ERROR_INDEX_OUT_OF_BOUNDS = -35,

	/** Unsupported operation. */
	SJME_ERROR_UNSUPPORTED_OPERATION = -36,

	/** Resource not found. */
	SJME_ERROR_RESOURCE_NOT_FOUND = -37,

	/** Unexpected end of file. */
	SJME_ERROR_UNEXPECTED_EOF = -38,
	
	/** Invalid identifier. */
	SJME_ERROR_INVALID_IDENTIFIER = -39,
	
	/** Invalid binary name. */
	SJME_ERROR_INVALID_BINARY_NAME = -40,
	
	/** Invalid field type. */
	SJME_ERROR_INVALID_FIELD_TYPE = -41,
	
	/** Invalid method type. */
	SJME_ERROR_INVALID_METHOD_TYPE = -42,
	
	/** Invalid class name. */
	SJME_ERROR_INVALID_CLASS_NAME = -43,
	
	/** Could not load library. */
	SJME_ERROR_COULD_NOT_LOAD_LIBRARY = -44,
	
	/** Invalid library symbol. */
	SJME_ERROR_INVALID_LIBRARY_SYMBOL = -45,
	
	/** There is no graphics display. */
	SJME_ERROR_HEADLESS_DISPLAY = -46,
	
	/** Cannot create something. */
	SJME_ERROR_CANNOT_CREATE = -47,
	
	/** Invalid thread state. */
	SJME_ERROR_INVALID_THREAD_STATE = -48,
	
	/** Component is already in a container. */
	SJME_ERROR_ALREADY_IN_CONTAINER = -49,
	
	/** Not a sub component. */
	SJME_ERROR_NOT_SUB_COMPONENT = -50,
	
	/** No such class exists. */
	SJME_ERROR_NO_CLASS = -51,
	
	/** No such method exists. */
	SJME_ERROR_NO_METHOD = -52,
	
	/** There is no listener. */
	SJME_ERROR_NO_LISTENER = -53,
	
	/** Cancel close of window. */
	SJME_ERROR_CANCEL_WINDOW_CLOSE = -54,
	
	/** The class cannot be casted. */
	SJME_ERROR_CLASS_CAST = -55,
	
	/** The font is not valid. */
	SJME_ERROR_INVALID_FONT = -56,
	
	/** There is no Java environment. */
	SJME_ERROR_NO_JAVA_ENVIRONMENT = -57,
	
	/** Font has negative height. */
	SJME_ERROR_FONT_NEGATIVE_HEIGHT = -58,
	
	/** Could not create native widget. */
	SJME_ERROR_NATIVE_WIDGET_CREATE_FAILED = -59,
	
	/** Clock failure. */
	SJME_ERROR_NATIVE_SYSTEM_CLOCK_FAILURE = -60,
	
	/** A weak reference it attached. */
	SJME_ERROR_WEAK_REFERENCE_ATTACHED = -61,
	
	/** An enqueue has already been set for the weak reference. */
	SJME_ERROR_ENQUEUE_ALREADY_SET = -62,
	
	/** Keep the weak reference, do not free it on zero references. */
	SJME_ERROR_ENQUEUE_KEEP_WEAK = -63,
	
	/** Not a weak reference. */
	SJME_ERROR_NOT_WEAK_REFERENCE = -64,
	
	/** Could not access array natively. */
	SJME_ERROR_NATIVE_ARRAY_ACCESS_FAILED = -65,
	
	/** The graphics buffer is not locked. */
	SJME_ERROR_BUFFER_NOT_LOCKED = -66,
	
	/** Component is not in this container. */
	SJME_ERROR_NOT_IN_CONTAINER = -67,
	
	/** Invalid link. */
	SJME_ERROR_INVALID_LINK = -68,
	
	/** We are not the owner of the lock. */
	SJME_ERROR_NOT_LOCK_OWNER = -69,
	
	/** Item already has a parent. */
	SJME_ERROR_HAS_PARENT = -70,
	
	/** Member already exists. */
	SJME_ERROR_MEMBER_EXISTS = -71,
	
	/** The native widget system failed for some reason. */
	SJME_ERROR_NATIVE_WIDGET_FAILURE = -72,
	
	/** Scan out of bounds. */
	SJME_ERROR_SCAN_OUT_OF_BOUNDS = -73,
	
	/** Native graphics access not yet valid. */
	SJME_ERROR_FRAMEBUFFER_NOT_READY = -74,
	
	/** Could not unload native library. */
	SJME_ERROR_COULD_NOT_UNLOAD_LIBRARY = -75,
	
	/** Could not enqueue onto the message loop. */
	SJME_ERROR_LOOP_ENQUEUE_FAILED = -76,
	
	/** Use fallback. */
	SJME_ERROR_USE_FALLBACK = -77,
	
	/** Continue operation. */
	SJME_ERROR_CONTINUE = -78,
	
	/** Interrupted. */
	SJME_ERROR_INTERRUPTED = -79,
	
	/** Exit. */
	SJME_ERROR_EXIT = -80,
	
	/** Input/Output Exception. */
	SJME_ERROR_IO_EXCEPTION = -81,
	
	/** No such element exists. */
	SJME_ERROR_NO_SUCH_ELEMENT = -82,
	
	/** Path too long. */
	SJME_ERROR_PATH_TOO_LONG = -83,
	
	/** File not found. */
	SJME_ERROR_FILE_NOT_FOUND = -84,
	
	/** The number of error codes. */
	SJME_NUM_ERROR_CODES = -85
} sjme_errorCode;

/**
 * Propagates an error code which allows others to run accordingly.
 * 
 * @param error The current error code.
 * @param expression The result from the expression.
 * @return If @c expression is an error, that will be returned otherwise
 * the value in @c error provided @c error is not an error.
 * @since 2024/01/18
 */
sjme_errorCode sjme_error_also(
	sjme_errorCode error, sjme_errorCode expression);

/**
 * Similar to @c sjme_error_also except this allows multiple error expressions
 * to be passed until the final is done via @c sjme_error_alsoVEnd() .
 * 
 * @param error The current error state.
 * @param ... All of the expressions, ends on @c sjme_error_alsoVEnd() .
 * @return The resultant error code.
 * @since 2024/01/18
 */
sjme_errorCode sjme_error_alsoV(
	sjme_errorCode error, ...);

/**
 * The end expression for @c sjme_error_alsoV() .
 * 
 * @return The ending sequence for error codes.
 * @since 2024/01/18 
 */
sjme_errorCode sjme_error_alsoVEnd(void);

/**
 * Is this expression considered an error?
 *
 * @param error The expression.
 * @since 2023/12/08
 */
sjme_jboolean sjme_error_is(
	sjme_errorCode error);

/**
 * Determines the default error code to use.
 *
 * @param error The error code.
 * @return Either @c error or a default error.
 * @since 2023/12/29
 */
sjme_errorCode sjme_error_default(
	sjme_errorCode error);

/**
 * Determines the default error code to use.
 *
 * @param error The error code.
 * @param otherwise The other error code rather than @c SJME_ERROR_UNKNOWN.
 * @return Either @c error or @c otherwise if the former is not valid.
 * @since 2023/12/29
 */
sjme_errorCode sjme_error_defaultOr(
	sjme_errorCode error, sjme_errorCode otherwise);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_ERROR_H
}
		#undef SJME_CXX_SQUIRRELJME_ERROR_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_ERROR_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_ERROR_H */
