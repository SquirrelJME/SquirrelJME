// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.exceptions;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * MLE call error codes.
 *
 * @since 2024/07/25
 */
@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "unused"})
@SquirrelJMENativeApi(min = "0.4.0")
public interface MLECallErrorCode
{
	/** No error. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NONE = 2;

	/** Generic unknown error (likely @link SJME_JNI_FALSE @endlink). */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UNKNOWN = 0;

	/** Generic unknown error (likely @link SJME_JNI_TRUE @endlink). */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UNKNOWN_ONE = 1;

	/** Generic unknown error. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UNKNOWN_NEGATIVE = -1;
	
	/** Null arguments. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NULL_ARGUMENTS = -2;
	
	/** Local variable out of bounds. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte LOCAL_INDEX_INVALID = -3;
	
	/** Stack variable out of bounds. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte STACK_INDEX_INVALID = -4;
	
	/** Stack underflow. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte STACK_UNDERFLOW = -5;
	
	/** Stack overflow. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte STACK_OVERFLOW = -6;
	
	/** Top is not an integer type. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte TOP_NOT_INTEGER = -7;
	
	/** Top is not a long type. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte TOP_NOT_LONG = -8;
	
	/** Top is not a float type. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte TOP_NOT_FLOAT = -9;
	
	/** Top is not a double type. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte TOP_NOT_DOUBLE = -10;
	
	/** Top is not a object type. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte TOP_NOT_OBJECT = -11;
	
	/** Frame is missing stack treads. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte FRAME_MISSING_STACK_TREADS = -12;
	
	/** Invalid read of stack. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte STACK_INVALID_READ = -13;
	
	/** Invalid write of stack. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte STACK_INVALID_WRITE = -14;
	
	/** Invalid read of stack. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte LOCAL_INVALID_READ = -15;
	
	/** Invalid write of stack. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte LOCAL_INVALID_WRITE = -16;
	
	/** Invalid reference pop. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_REFERENCE_POP = -17;
	
	/** Invalid reference push. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_REFERENCE_PUSH = -18;
	
	/** Failed to garbage collect object. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte COULD_NOT_GC_OBJECT = -19;
	
	/** Object reference count is not zero. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte OBJECT_REFCOUNT_NOT_ZERO = -20;
	
	/** Garbage collection of object cancelled. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte OBJECT_GC_CANCELLED = -21;

	/** Out of memory. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte OUT_OF_MEMORY = -22;

	/** Pool initialization failed. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte POOL_INIT_FAILED = -23;

	/** Invalid argument. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_ARGUMENT = -24;

	/** Not implemented. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NOT_IMPLEMENTED = -25;

	/** Invalid tread read. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte TREAD_INVALID_READ = -26;

	/** Invalid tread write. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte TREAD_INVALID_WRITE = -27;

	/** There are no suites available. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NO_SUITES = -28;

	/** Classpath cannot be obtained by both ID and Name. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CLASS_PATH_BY_BOTH = -29;

	/** Illegal state. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ILLEGAL_STATE = -30;

	/** A library was not found. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte LIBRARY_NOT_FOUND = -31;

	/** Boot failure. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte BOOT_FAILURE = -32;

	/** Generic JNI exception. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte JNI_EXCEPTION = -33;

	/** Memory has been corrupted. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte MEMORY_CORRUPTION = -34;

	/** Index out of bounds. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INDEX_OUT_OF_BOUNDS = -35;

	/** Unsupported operation. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UNSUPPORTED_OPERATION = -36;

	/** Resource not found. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte RESOURCE_NOT_FOUND = -37;

	/** Unexpected end of file. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UNEXPECTED_EOF = -38;
	
	/** Invalid identifier. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_IDENTIFIER = -39;
	
	/** Invalid binary name. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_BINARY_NAME = -40;
	
	/** Invalid field type. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_FIELD_TYPE = -41;
	
	/** Invalid method type. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_METHOD_TYPE = -42;
	
	/** Invalid class name. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_CLASS_NAME = -43;
	
	/** Could not load library. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte COULD_NOT_LOAD_LIBRARY = -44;
	
	/** Invalid library symbol. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_LIBRARY_SYMBOL = -45;
	
	/** There is no graphics display. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte HEADLESS_DISPLAY = -46;
	
	/** Cannot create something. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CANNOT_CREATE = -47;
	
	/** Invalid thread state. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_THREAD_STATE = -48;
	
	/** Component is already in a container. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ALREADY_IN_CONTAINER = -49;
	
	/** Not a sub component. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NOT_SUB_COMPONENT = -50;
	
	/** No such class exists. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NO_CLASS = -51;
	
	/** No such method exists. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NO_METHOD = -52;
	
	/** There is no listener. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NO_LISTENER = -53;
	
	/** Cancel close of window. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CANCEL_WINDOW_CLOSE = -54;
	
	/** The class cannot be casted. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CLASS_CAST = -55;
	
	/** The font is not valid. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_FONT = -56;
	
	/** There is no Java environment. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NO_JAVA_ENVIRONMENT = -57;
	
	/** Font has negative height. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte FONT_NEGATIVE_HEIGHT = -58;
	
	/** Could not create native widget. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NATIVE_WIDGET_CREATE_FAILED = -59;
	
	/** Clock failure. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NATIVE_SYSTEM_CLOCK_FAILURE = -60;
	
	/** A weak reference it attached. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte WEAK_REFERENCE_ATTACHED = -61;
	
	/** An enqueue has already been set for the weak reference. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ENQUEUE_ALREADY_SET = -62;
	
	/** Native error. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NATIVE_ERROR = -63;
	
	/** Not a weak reference. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NOT_WEAK_REFERENCE = -64;
	
	/** Could not access array natively. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NATIVE_ARRAY_ACCESS_FAILED = -65;
	
	/** The graphics buffer is not locked. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte BUFFER_NOT_LOCKED = -66;
	
	/** Component is not in this container. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NOT_IN_CONTAINER = -67;
	
	/** Invalid link. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_LINK = -68;
	
	/** We are not the owner of the lock. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NOT_LOCK_OWNER = -69;
	
	/** Item already has a parent. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte HAS_PARENT = -70;
	
	/** Member already exists. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte MEMBER_EXISTS = -71;
	
	/** The native widget system failed for some reason. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NATIVE_WIDGET_FAILURE = -72;
	
	/** Scan out of bounds. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SCAN_OUT_OF_BOUNDS = -73;
	
	/** Native graphics access not yet valid. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte FRAMEBUFFER_NOT_READY = -74;
	
	/** Could not unload native library. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte COULD_NOT_UNLOAD_LIBRARY = -75;
	
	/** Could not enqueue onto the message loop. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte LOOP_ENQUEUE_FAILED = -76;
	
	/** Use fallback. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte USE_FALLBACK = -77;
	
	/** Continue operation. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CONTINUE = -78;
	
	/** Interrupted. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INTERRUPTED = -79;
	
	/** Exit. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte EXIT = -80;
	
	/** Input/Output Exception. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte IO_EXCEPTION = -81;
	
	/** No such element exists. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NO_SUCH_ELEMENT = -82;
	
	/** Path too long. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte PATH_TOO_LONG = -83;
	
	/** File not found. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte FILE_NOT_FOUND = -84;
	
	/** Input is too short. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte TOO_SHORT = -85;
	
	/** Not a Zip. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NOT_ZIP = -86;
	
	/** Access it not aligned. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UNALIGNED_ACCESS = -87;
	
	/** Zip is corrupt. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CORRUPT_ZIP = -88;
	
	/** Unsupported Zip format. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UNSUPPORTED_ZIP_FORMAT = -89;
	
	/** Path is a directory. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte IS_DIRECTORY = -90;
	
	/** Invalid literal length values in inflate. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INFLATE_INVALID_INVERT = -91;
	
	/** Invalid block type in inflate stream. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INFLATE_INVALID_BTYPE = -92;
	
	/** Invalid dynamic tree length in inflate stream. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INFLATE_INVALID_TREE_LENGTH = -93;
	
	/** The code length is invalid. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INFLATE_INVALID_CODE_LENGTH = -94;
	
	/** Huffman tree is full. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte TRAVERSE_FULL = -95;
	
	/** There is a collision in the huffman tree, which means bad data! */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte TREE_COLLISION = -96;
	
	/** The buffer is full. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte BUFFER_FULL = -97;
	
	/** Huffman tree is incomplete. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INFLATE_HUFF_TREE_INCOMPLETE = -98;
	
	/** Tree value has invalid code. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INFLATE_INVALID_CODE = -99;
	
	/** Stop operation. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte STOP = -100;
	
	/** Buffer is saturated. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte BUFFER_SATURATED = -101;
	
	/** Window distance out of range. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INFLATE_DISTANCE_OUT_OF_RANGE = -102;
	
	/** Invalid code length count. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INFLATE_INVALID_CODE_LENGTH_COUNT = -103;
	
	/** Invalid repeat of first entry. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INFLATE_INVALID_FIRST_REPEAT = -104;
	
	/** Inflation index overflow. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INFLATE_INDEX_OVERFLOW = -105;
	
	/** End of file reached. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte END_OF_FILE = -106;
	
	/** Element already exists. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ELEMENT_EXISTS = -107;
	
	/** Capacity exceeded? */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CAPACITY_EXCEEDED = -108;
	
	/** Tree traversal is too deep. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte TREE_TOO_DEEP = -109;
	
	/** Invalid class magic number. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_CLASS_MAGIC = -110;
	
	/** Invalid class version. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_CLASS_VERSION = -111;
	
	/** Invalid class pool count. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_CLASS_POOL_COUNT = -112;
	
	/** Invalid class pool index reference. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_CLASS_POOL_INDEX = -113;
	
	/** Reference to class pool index is the wrong type. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte WRONG_CLASS_POOL_INDEX_TYPE = -114;
	
	/** Class flags are invalid. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_CLASS_FLAGS = -115;
	
	/** Method flags are invalid. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_METHOD_FLAGS = -116;
	
	/** Method has multiple code attributes. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte METHOD_MULTIPLE_CODE = -117;
	
	/** Invalid field flags. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_FIELD_FLAGS = -118;
	
	/** Not matched. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NOT_MATCHED = -119;
	
	/** Not a valid allocation link. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NOT_ALLOC_LINK = -120;
	
	/** Too many class members. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CLASS_TOO_MANY_MEMBERS = -121;
	
	/** Incompatible class change. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CLASS_CHANGED = -122;
	
	/** Method is not bound. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UNBOUND_METHOD = -123;

	/** Argument count mismatch. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ARGUMENT_COUNT_MISMATCH = -124;

	/** Argument type mismatch. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ARGUMENT_TYPE_MISMATCH = -125;

	/** Tread index is not valid. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte TREAD_INDEX_INVALID = -126;

	/** Invalid instruction. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INVALID_INSTRUCTION = -127;

	/** Generic linkage error. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte LINKAGE_ERROR = -128;

	/** The super class is not a valid class. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short SUPER_CLASS_INVALID = -129;

	/** The super class is not a valid class. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short NO_FIELD = -130;

	/** The class loader is not valid. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short INVALID_CLASS_LOADER = -131;

	/** Purely virtual method call. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short PURE_VIRTUAL_CALL = -132;

	/** Array is of negative size. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short NEGATIVE_ARRAY_SIZE = -133;

	/** MLE Call error. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short MLE_CALL = -134;

	/** Invalid MLE Shelf. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short UNKNOWN_MLE_SHELF = -135;

	/** Invalid MLE Function. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short UNKNOWN_MLE_FUNCTION = -136;

	/** MLE call is not compatible. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short INCOMPATIBLE_MLE_CALL = -137;

	/** Return type is incorrect. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short WRONG_RETURN_TYPE = -138;

	/** The code address is not valid. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short INVALID_CODE_ADDRESS = -139;

	/** Stack pointer is null. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short NULL_STACK_POINTER = -140;

	/** Array index is out of bounds. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short ARRAY_INDEX_OUT_OF_BOUNDS = -141;

	/** No audio system is available. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short HEADLESS_AUDIO = -142;

	/** Unsupported audio format. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short UNSUPPORTED_AUDIO_FORMAT = -143;

	/** Audio has been destroyed. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short AUDIO_DESTROYED = -144;

	/** The state is mismatched. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short AUDIO_STATE_MISMATCH = -145;

	/** No audio resources are available. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short AUDIO_NO_RESOURCES = -146;

	/** Mismatch between audio formats. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short AUDIO_FORMAT_MISMATCH = -147;

	/** Audio triggering failed. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short AUDIO_TRIGGER_FAILED = -148;

	/** Audio write failed. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short AUDIO_WRITE_FAILED = -149;

	/** Failed to prepare audio. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short AUDIO_PREPARE_FAILED = -150;

	/** Invalid PC adjustment. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short INVALID_PC_ADJUST = -151;

	/** An instruction is not valid. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short CLASS_VERIFY_BAD_INSTRUCTION = -152;

	/** An instruction has an invalid length. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short CLASS_VERIFY_BAD_INSTRUCTION_LENGTH = -153;

	/** Class member access is denied. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short MEMBER_ACCESS_DENIED = -154;

	/** Field is not direct. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short FIELD_NOT_DIRECT = -155;

	/** Could not initialize static string value. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short STATIC_STRING_INIT = -156;

	/** Doubly tossed exception. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short DOUBLE_TOSS = -157;

	/** Uncaught exception. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short UNCAUGHT_EXCEPTION = -158;

	/** Object is not valid. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short INVALID_OBJECT = -159;

	/** Walk encountered an unknown type. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short WALK_UNKNOWN_TYPE = -160;

	/** Skip the elements of items. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short WALK_SKIP_ELEMENTS = -161;

	/** Invalid preceding type. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short CBOR_INVALID_PRECEDE = -162;

	/** ROM is not valid. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short INVALID_ROM = -163;

	/** An unexpected object was matched, it should be another object. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short OBJECT_MISMATCHED = -164;

	/** An object was garbage collected when it should not have been. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short OBJECT_GONE = -165;

	/** There is still an active GC commit. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short ACTIVE_GC_COMMIT = -166;

	/** Connection refused. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short CONNECTION_REFUSED = -167;

	/** JDWP handshake not valid. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short JDWP_BAD_HANDSHAKE = -168;

	/** Unknown native function. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short UNKNOWN_NATIVE_FUNCTION = -169;

	/** No test result occurred. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short NO_TEST_RESULT = -170;

	/** Memory exists. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short MEMORY_EXISTS = -171;

	/** Cancel MLE call. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short CANCEL_MLE_CALL = -172;

	/** Skip default walk action after custom step is performed. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short WALK_SKIP_CUSTOM_DEFAULT = -173;

	/** Try the operation again. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short TRY_AGAIN = -174;

	/** The requested path is not defined. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short PATH_NOT_DEFINED = -175;

	/** Security has been violated. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short SECURITY_EXCEPTION = -176;

	/* Path is not absolute. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short PATH_NOT_ABSOLUTE = -177;

	/** Path is too deep. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short PATH_TOO_DEEP = -178;

	/** Path is not valid. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short PATH_NOT_VALID = -179;

	/** There is no user login. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short NO_USER_LOGIN = -180;
	
	/** This should not be happening! */
	@SquirrelJMENativeApi(min = "0.4.0")
	short SHOULD_NOT_HAPPEN = -181;
	
	/** Audio is awaiting streams/sources. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short AUDIO_AWAITING = -182;
	
	/** NanoTest: Expected value has no value. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short NANOTEST_EXPECTED_NO_VALUE = -183;
	
	/** NanoTest: Expected value has no static expected field. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short NANOTEST_EXPECTED_MISSING = -184;
	
	/** NanoTest: Missing annotations on test. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short NANOTEST_NO_ANNOTATIONS = -185;
	
	/** Class has an invalid annotation tag. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short CLASS_UNKNOWN_ANNOTATION_TAG = -186;
	
	/** Font is already registered. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short FONT_ALREADY_REGISTERED = -187;
	
	/** Value or object is too large. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short TOO_LARGE = -188;
	
	/** The rectangle size is not valid. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short INVALID_RECT = -189;
	
	/** Invalid codepoint. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short INVALID_CODEPOINT = -190;

	/** The manifest format is not valid. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short INVALID_MANIFEST_FORMAT = -191;

	/** The type of class this is, is not valid. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short INVALID_CLASS_TYPE = -192;

	/** Minimum frame level has been reached. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short MINIMUM_FRAME_LEVEL = -193;

	/** This type of thread cannot be called in a nested matter. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short INVALID_EXPLICIT_NEST = -194;

	/** Invalid manifest value. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short INVALID_MANIFEST_VALUE = -195;

	/** The main runtime library is missing. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short MISSING_MAIN_RUNTIME = -196;

	/** Main class is not initialized. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short CLASS_NOT_INITIALIZED = -197;
	
	/** The number of error codes. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short NUM_ERROR_CODES = -198;
}
