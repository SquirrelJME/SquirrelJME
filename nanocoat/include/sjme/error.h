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
 * @file
 * @since 2024/08/09
 */

#ifndef SJME_C_ERROR_H
#define SJME_C_ERROR_H

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
	SJME_ERROR_NONE = 2,

	/** Generic unknown error (likely @link SJME_JNI_FALSE @endlink). */
	SJME_ERROR_UNKNOWN = 0,

	/** Generic unknown error (likely @link SJME_JNI_TRUE @endlink). */
	SJME_ERROR_UNKNOWN_ONE = 1,

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
	
	/** Native error. */
	SJME_ERROR_NATIVE_ERROR = -63,
	
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
	
	/** Input is too short. */
	SJME_ERROR_TOO_SHORT = -85,
	
	/** Not a Zip. */
	SJME_ERROR_NOT_ZIP = -86,
	
	/** Access it not aligned. */
	SJME_ERROR_UNALIGNED_ACCESS = -87,
	
	/** Zip is corrupt. */
	SJME_ERROR_CORRUPT_ZIP = -88,
	
	/** Unsupported Zip format. */
	SJME_ERROR_UNSUPPORTED_ZIP_FORMAT = -89,
	
	/** Path is a directory. */
	SJME_ERROR_IS_DIRECTORY = -90,
	
	/** Invalid literal length values in inflate. */
	SJME_ERROR_INFLATE_INVALID_INVERT = -91,
	
	/** Invalid block type in inflate stream. */
	SJME_ERROR_INFLATE_INVALID_BTYPE = -92,
	
	/** Invalid dynamic tree length in inflate stream. */
	SJME_ERROR_INFLATE_INVALID_TREE_LENGTH = -93,
	
	/** The code length is invalid. */
	SJME_ERROR_INFLATE_INVALID_CODE_LENGTH = -94,
	
	/** Huffman tree is full. */
	SJME_ERROR_TRAVERSE_FULL = -95,
	
	/** There is a collision in the huffman tree, which means bad data! */
	SJME_ERROR_TREE_COLLISION = -96,
	
	/** The buffer is full. */
	SJME_ERROR_BUFFER_FULL = -97,
	
	/** Huffman tree is incomplete. */
	SJME_ERROR_INFLATE_HUFF_TREE_INCOMPLETE = -98,
	
	/** Tree value has invalid code. */
	SJME_ERROR_INFLATE_INVALID_CODE = -99,
	
	/** Stop operation. */
	SJME_ERROR_STOP = -100,
	
	/** Buffer is saturated. */
	SJME_ERROR_BUFFER_SATURATED = -101,
	
	/** Window distance out of range. */
	SJME_ERROR_INFLATE_DISTANCE_OUT_OF_RANGE = -102,
	
	/** Invalid code length count. */
	SJME_ERROR_INFLATE_INVALID_CODE_LENGTH_COUNT = -103,
	
	/** Invalid repeat of first entry. */
	SJME_ERROR_INFLATE_INVALID_FIRST_REPEAT = -104,
	
	/** Inflation index overflow. */
	SJME_ERROR_INFLATE_INDEX_OVERFLOW = -105,
	
	/** End of file reached. */
	SJME_ERROR_END_OF_FILE = -106,
	
	/** Element already exists. */
	SJME_ERROR_ELEMENT_EXISTS = -107,
	
	/** Capacity exceeded? */
	SJME_ERROR_CAPACITY_EXCEEDED = -108,
	
	/** Tree traversal is too deep. */
	SJME_ERROR_TREE_TOO_DEEP = -109,
	
	/** Invalid class magic number. */
	SJME_ERROR_INVALID_CLASS_MAGIC = -110,
	
	/** Invalid class version. */
	SJME_ERROR_INVALID_CLASS_VERSION = -111,
	
	/** Invalid class pool count. */
	SJME_ERROR_INVALID_CLASS_POOL_COUNT = -112,
	
	/** Invalid class pool index reference. */
	SJME_ERROR_INVALID_CLASS_POOL_INDEX = -113,
	
	/** Reference to class pool index is the wrong type. */
	SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE = -114,
	
	/** Class flags are invalid. */
	SJME_ERROR_INVALID_CLASS_FLAGS = -115,
	
	/** Method flags are invalid. */
	SJME_ERROR_INVALID_METHOD_FLAGS = -116,
	
	/** Method has multiple code attributes. */
	SJME_ERROR_METHOD_MULTIPLE_CODE = -117,
	
	/** Invalid field flags. */
	SJME_ERROR_INVALID_FIELD_FLAGS = -118,
	
	/** Not matched. */
	SJME_ERROR_NOT_MATCHED = -119,
	
	/** Not a valid allocation link. */
	SJME_ERROR_NOT_ALLOC_LINK = -120,
	
	/** Too many class members. */
	SJME_ERROR_CLASS_TOO_MANY_MEMBERS = -121,
	
	/** Incompatible class change. */
	SJME_ERROR_CLASS_CHANGED = -122,
	
	/** Method is not bound. */
	SJME_ERROR_UNBOUND_METHOD = -123,

	/** Argument count mismatch. */
	SJME_ERROR_ARGUMENT_COUNT_MISMATCH = -124,

	/** Argument type mismatch. */
	SJME_ERROR_ARGUMENT_TYPE_MISMATCH = -125,

	/** Tread index is not valid. */
	SJME_ERROR_TREAD_INDEX_INVALID = -126,

	/** Invalid instruction. */
	SJME_ERROR_INVALID_INSTRUCTION = -127,

	/** Generic linkage error. */
	SJME_ERROR_LINKAGE_ERROR = -128,

	/** The super class is not a valid class. */
	SJME_ERROR_SUPER_CLASS_INVALID = -129,

	/** The super class is not a valid class. */
	SJME_ERROR_NO_FIELD = -130,

	/** The class loader is not valid. */
	SJME_ERROR_INVALID_CLASS_LOADER = -131,

	/** Purely virtual method call. */
	SJME_ERROR_PURE_VIRTUAL_CALL = -132,

	/** Array is of negative size. */
	SJME_ERROR_NEGATIVE_ARRAY_SIZE = -133,

	/** MLE Call error. */
	SJME_ERROR_MLE_CALL = -134,

	/** Invalid MLE Shelf. */
	SJME_ERROR_UNKNOWN_MLE_SHELF = -135,

	/** Invalid MLE Function. */
	SJME_ERROR_UNKNOWN_MLE_FUNCTION = -136,

	/** MLE call is not compatible. */
	SJME_ERROR_INCOMPATIBLE_MLE_CALL = -137,

	/** Return type is incorrect. */
	SJME_ERROR_WRONG_RETURN_TYPE = -138,

	/** The code address is not valid. */
	SJME_ERROR_INVALID_CODE_ADDRESS = -139,

	/** Stack pointer is null. */
	SJME_ERROR_NULL_STACK_POINTER = -140,

	/** Array index is out of bounds. */
	SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS = -141,

	/** No audio system is available. */
	SJME_ERROR_HEADLESS_AUDIO = -142,

	/** Unsupported audio format. */
	SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT = -143,

	/** Audio has been destroyed. */
	SJME_ERROR_AUDIO_DESTROYED = -144,

	/** The state is mismatched. */
	SJME_ERROR_AUDIO_STATE_MISMATCH = -145,

	/** No audio resources are available. */
	SJME_ERROR_AUDIO_NO_RESOURCES = -146,

	/** Mismatch between audio formats. */
	SJME_ERROR_AUDIO_FORMAT_MISMATCH = -147,

	/** Audio triggering failed. */
	SJME_ERROR_AUDIO_TRIGGER_FAILED = -148,

	/** Audio write failed. */
	SJME_ERROR_AUDIO_WRITE_FAILED = -149,

	/** Failed to prepare audio. */
	SJME_ERROR_AUDIO_PREPARE_FAILED = -150,

	/** Invalid PC adjustment. */
	SJME_ERROR_INVALID_PC_ADJUST = -151,

	/** An instruction is not valid. */
	SJME_ERROR_CLASS_VERIFY_BAD_INSTRUCTION = -152,

	/** An instruction has an invalid length. */
	SJME_ERROR_CLASS_VERIFY_BAD_INSTRUCTION_LENGTH = -153,

	/** Class member access is denied. */
	SJME_ERROR_MEMBER_ACCESS_DENIED = -154,

	/** Field is not direct. */
	SJME_ERROR_FIELD_NOT_DIRECT = -155,

	/** Could not initialize static string value. */
	SJME_ERROR_STATIC_STRING_INIT = -156,

	/** Doubly tossed exception. */
	SJME_ERROR_DOUBLE_TOSS = -157,

	/** Uncaught exception. */
	SJME_ERROR_UNCAUGHT_EXCEPTION = -158,

	/** Object is not valid. */
	SJME_ERROR_INVALID_OBJECT = -159,

	/** Walk encountered an unknown type. */
	SJME_ERROR_WALK_UNKNOWN_TYPE = -160,

	/** Skip the elements of items. */
	SJME_ERROR_WALK_SKIP_ELEMENTS = -161,

	/** Invalid preceding type. */
	SJME_ERROR_CBOR_INVALID_PRECEDE = -162,

	/** ROM is not valid. */
	SJME_ERROR_INVALID_ROM = -163,

	/** An unexpected object was matched, it should be another object. */
	SJME_ERROR_OBJECT_MISMATCHED = -164,

	/** An object was garbage collected when it should not have been. */
	SJME_ERROR_OBJECT_GONE = -165,

	/** There is still an active GC commit. */
	SJME_ERROR_ACTIVE_GC_COMMIT = -166,

	/** Connection refused. */
	SJME_ERROR_CONNECTION_REFUSED = -167,

	/** JDWP handshake not valid. */
	SJME_ERROR_JDWP_BAD_HANDSHAKE = -168,

	/** Unknown native function. */
	SJME_ERROR_UNKNOWN_NATIVE_FUNCTION = -169,

	/** No test result occurred. */
	SJME_ERROR_NO_TEST_RESULT = -170,

	/** Memory exists. */
	SJME_ERROR_MEMORY_EXISTS = -171,

	/** Cancel MLE call. */
	SJME_ERROR_CANCEL_MLE_CALL = -172,

	/** Skip default walk action after custom step is performed. */
	SJME_ERROR_WALK_SKIP_CUSTOM_DEFAULT = -173,

	/** Try the operation again. */
	SJME_ERROR_TRY_AGAIN = -174,

	/** The requested path is not defined. */
	SJME_ERROR_PATH_NOT_DEFINED = -175,

	/** Security has been violated. */
	SJME_ERROR_SECURITY_EXCEPTION = -176,

	/* Path is not absolute. */
	SJME_ERROR_PATH_NOT_ABSOLUTE = -177,

	/** Path is too deep. */
	SJME_ERROR_PATH_TOO_DEEP = -178,

	/** Path is not valid. */
	SJME_ERROR_PATH_NOT_VALID = -179,

	/** There is no user login. */
	SJME_ERROR_NO_USER_LOGIN = -180,
	
	/** This should not be happening! */
	SJME_ERROR_SHOULD_NOT_HAPPEN = -181,
	
	/** Audio is awaiting streams/sources. */
	SJME_ERROR_AUDIO_AWAITING = -182,
	
	/** NanoTest: Expected value has no value. */
	SJME_ERROR_NANOTEST_EXPECTED_NO_VALUE = -183,
	
	/** NanoTest: Expected value has no static expected field. */
	SJME_ERROR_NANOTEST_EXPECTED_MISSING = -184,
	
	/** NanoTest: Missing annotations on test. */
	SJME_ERROR_NANOTEST_NO_ANNOTATIONS = -185,
	
	/** Class has an invalid annotation tag. */
	SJME_ERROR_CLASS_UNKNOWN_ANNOTATION_TAG = -186,
	
	/** Font is already registered. */
	SJME_ERROR_FONT_ALREADY_REGISTERED = -187,
	
	/** Value or object is too large. */
	SJME_ERROR_TOO_LARGE = -188,
	
	/** The rectangle size is not valid. */
	SJME_ERROR_INVALID_RECT = -189,
	
	/** Invalid codepoint. */
	SJME_ERROR_INVALID_CODEPOINT = -190,

	/** The manifest format is not valid. */
	SJME_ERROR_INVALID_MANIFEST_FORMAT = -191,
	
	/** The number of error codes. */
	SJME_NUM_ERROR_CODES = -192,
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
 * Similar to @link sjme_error_also @endlink except this allows multiple error
 * expressions to be passed until the final is done via
 * @link sjme_error_alsoVEnd() @endlink.
 * 
 * @param error The current error state.
 * @param ... All of the expressions, ends
 * on @link sjme_error_alsoVEnd() @endlink.
 * @return The resultant error code.
 * @since 2024/01/18
 */
sjme_errorCode sjme_error_alsoV(
	sjme_errorCode error, ...);

/**
 * The end expression for @link sjme_error_alsoV() .
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
 * @param otherwise The other error code rather than @link SJME_ERROR_UNKNOWN.
 * @return Either @c error or @c otherwise if the former is not valid.
 * @since 2023/12/29
 */
sjme_errorCode sjme_error_defaultOr(
	sjme_errorCode error, sjme_errorCode otherwise);

/**
 * Masks the given error with another.
 *
 * @param error The error code to be masked.
 * @param mask The error to mask with.
 * @return Returns @c mask .
 * @since 2025/04/08
 */
sjme_errorCode sjme_error_mask(
	sjme_attrInValue sjme_errorCode error,
	sjme_attrInValue sjme_errorCode mask);

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
