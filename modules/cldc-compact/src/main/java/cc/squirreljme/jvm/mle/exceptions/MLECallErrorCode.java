// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.exceptions;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * MLE call error codes.
 *
 * @since 2024/07/25
 */
@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
@SquirrelJMEVendorApi
public interface MLECallErrorCode
{
	/** No error. */
	@SquirrelJMEVendorApi
	byte NONE = 1;

	/** Generic unknown error. */
	@SquirrelJMEVendorApi
	byte UNKNOWN = 0;

	/** Generic unknown error. */
	@SquirrelJMEVendorApi
	byte UNKNOWN_NEGATIVE = -1;
	
	/** Null arguments. */
	@SquirrelJMEVendorApi
	byte NULL_ARGUMENTS = -2;
	
	/** Local variable out of bounds. */
	@SquirrelJMEVendorApi
	byte LOCAL_INDEX_INVALID = -3;
	
	/** Stack variable out of bounds. */
	@SquirrelJMEVendorApi
	byte STACK_INDEX_INVALID = -4;
	
	/** Stack underflow. */
	@SquirrelJMEVendorApi
	byte STACK_UNDERFLOW = -5;
	
	/** Stack overflow. */
	@SquirrelJMEVendorApi
	byte STACK_OVERFLOW = -6;
	
	/** Top is not an integer type. */
	@SquirrelJMEVendorApi
	byte TOP_NOT_INTEGER = -7;
	
	/** Top is not a long type. */
	@SquirrelJMEVendorApi
	byte TOP_NOT_LONG = -8;
	
	/** Top is not a float type. */
	@SquirrelJMEVendorApi
	byte TOP_NOT_FLOAT = -9;
	
	/** Top is not a double type. */
	@SquirrelJMEVendorApi
	byte TOP_NOT_DOUBLE = -10;
	
	/** Top is not a object type. */
	@SquirrelJMEVendorApi
	byte TOP_NOT_OBJECT = -11;
	
	/** Frame is missing stack treads. */
	@SquirrelJMEVendorApi
	byte FRAME_MISSING_STACK_TREADS = -12;
	
	/** Invalid read of stack. */
	@SquirrelJMEVendorApi
	byte STACK_INVALID_READ = -13;
	
	/** Invalid write of stack. */
	@SquirrelJMEVendorApi
	byte STACK_INVALID_WRITE = -14;
	
	/** Invalid read of stack. */
	@SquirrelJMEVendorApi
	byte LOCAL_INVALID_READ = -15;
	
	/** Invalid write of stack. */
	@SquirrelJMEVendorApi
	byte LOCAL_INVALID_WRITE = -16;
	
	/** Invalid reference pop. */
	@SquirrelJMEVendorApi
	byte INVALID_REFERENCE_POP = -17;
	
	/** Invalid reference push. */
	@SquirrelJMEVendorApi
	byte INVALID_REFERENCE_PUSH = -18;
	
	/** Failed to garbage collect object. */
	@SquirrelJMEVendorApi
	byte COULD_NOT_GC_OBJECT = -19;
	
	/** Object reference count is not zero. */
	@SquirrelJMEVendorApi
	byte OBJECT_REFCOUNT_NOT_ZERO = -20;
	
	/** Garbage collection of object cancelled. */
	@SquirrelJMEVendorApi
	byte OBJECT_GC_CANCELLED = -21;

	/** Out of memory. */
	@SquirrelJMEVendorApi
	byte OUT_OF_MEMORY = -22;

	/** Pool initialization failed. */
	@SquirrelJMEVendorApi
	byte POOL_INIT_FAILED = -23;

	/** Invalid argument. */
	@SquirrelJMEVendorApi
	byte INVALID_ARGUMENT = -24;

	/** Not implemented. */
	@SquirrelJMEVendorApi
	byte NOT_IMPLEMENTED = -25;

	/** Invalid tread read. */
	@SquirrelJMEVendorApi
	byte TREAD_INVALID_READ = -26;

	/** Invalid tread write. */
	@SquirrelJMEVendorApi
	byte TREAD_INVALID_WRITE = -27;

	/** There are no suites available. */
	@SquirrelJMEVendorApi
	byte NO_SUITES = -28;

	/** Classpath cannot be obtained by both ID and Name. */
	@SquirrelJMEVendorApi
	byte CLASS_PATH_BY_BOTH = -29;

	/** Illegal state. */
	@SquirrelJMEVendorApi
	byte ILLEGAL_STATE = -30;

	/** A library was not found. */
	@SquirrelJMEVendorApi
	byte LIBRARY_NOT_FOUND = -31;

	/** Boot failure. */
	@SquirrelJMEVendorApi
	byte BOOT_FAILURE = -32;

	/** Generic JNI exception. */
	@SquirrelJMEVendorApi
	byte JNI_EXCEPTION = -33;

	/** Memory has been corrupted. */
	@SquirrelJMEVendorApi
	byte MEMORY_CORRUPTION = -34;

	/** Index out of bounds. */
	@SquirrelJMEVendorApi
	byte INDEX_OUT_OF_BOUNDS = -35;

	/** Unsupported operation. */
	@SquirrelJMEVendorApi
	byte UNSUPPORTED_OPERATION = -36;

	/** Resource not found. */
	@SquirrelJMEVendorApi
	byte RESOURCE_NOT_FOUND = -37;

	/** Unexpected end of file. */
	@SquirrelJMEVendorApi
	byte UNEXPECTED_EOF = -38;
	
	/** Invalid identifier. */
	@SquirrelJMEVendorApi
	byte INVALID_IDENTIFIER = -39;
	
	/** Invalid binary name. */
	@SquirrelJMEVendorApi
	byte INVALID_BINARY_NAME = -40;
	
	/** Invalid field type. */
	@SquirrelJMEVendorApi
	byte INVALID_FIELD_TYPE = -41;
	
	/** Invalid method type. */
	@SquirrelJMEVendorApi
	byte INVALID_METHOD_TYPE = -42;
	
	/** Invalid class name. */
	@SquirrelJMEVendorApi
	byte INVALID_CLASS_NAME = -43;
	
	/** Could not load library. */
	@SquirrelJMEVendorApi
	byte COULD_NOT_LOAD_LIBRARY = -44;
	
	/** Invalid library symbol. */
	@SquirrelJMEVendorApi
	byte INVALID_LIBRARY_SYMBOL = -45;
	
	/** There is no graphics display. */
	@SquirrelJMEVendorApi
	byte HEADLESS_DISPLAY = -46;
	
	/** Cannot create something. */
	@SquirrelJMEVendorApi
	byte CANNOT_CREATE = -47;
	
	/** Invalid thread state. */
	@SquirrelJMEVendorApi
	byte INVALID_THREAD_STATE = -48;
	
	/** Component is already in a container. */
	@SquirrelJMEVendorApi
	byte ALREADY_IN_CONTAINER = -49;
	
	/** Not a sub component. */
	@SquirrelJMEVendorApi
	byte NOT_SUB_COMPONENT = -50;
	
	/** No such class exists. */
	@SquirrelJMEVendorApi
	byte NO_CLASS = -51;
	
	/** No such method exists. */
	@SquirrelJMEVendorApi
	byte NO_METHOD = -52;
	
	/** There is no listener. */
	@SquirrelJMEVendorApi
	byte NO_LISTENER = -53;
	
	/** Cancel close of window. */
	@SquirrelJMEVendorApi
	byte CANCEL_WINDOW_CLOSE = -54;
	
	/** The class cannot be casted. */
	@SquirrelJMEVendorApi
	byte CLASS_CAST = -55;
	
	/** The font is not valid. */
	@SquirrelJMEVendorApi
	byte INVALID_FONT = -56;
	
	/** There is no Java environment. */
	@SquirrelJMEVendorApi
	byte NO_JAVA_ENVIRONMENT = -57;
	
	/** Font has negative height. */
	@SquirrelJMEVendorApi
	byte FONT_NEGATIVE_HEIGHT = -58;
	
	/** Could not create native widget. */
	@SquirrelJMEVendorApi
	byte NATIVE_WIDGET_CREATE_FAILED = -59;
	
	/** Clock failure. */
	@SquirrelJMEVendorApi
	byte NATIVE_SYSTEM_CLOCK_FAILURE = -60;
	
	/** A weak reference it attached. */
	@SquirrelJMEVendorApi
	byte WEAK_REFERENCE_ATTACHED = -61;
	
	/** An enqueue has already been set for the weak reference. */
	@SquirrelJMEVendorApi
	byte ENQUEUE_ALREADY_SET = -62;
	
	/** Keep the weak reference; do not free it on zero references. */
	@SquirrelJMEVendorApi
	byte ENQUEUE_KEEP_WEAK = -63;
	
	/** Not a weak reference. */
	@SquirrelJMEVendorApi
	byte NOT_WEAK_REFERENCE = -64;
	
	/** Could not access array natively. */
	@SquirrelJMEVendorApi
	byte NATIVE_ARRAY_ACCESS_FAILED = -65;
	
	/** The graphics buffer is not locked. */
	@SquirrelJMEVendorApi
	byte BUFFER_NOT_LOCKED = -66;
	
	/** Component is not in this container. */
	@SquirrelJMEVendorApi
	byte NOT_IN_CONTAINER = -67;
	
	/** Invalid link. */
	@SquirrelJMEVendorApi
	byte INVALID_LINK = -68;
	
	/** We are not the owner of the lock. */
	@SquirrelJMEVendorApi
	byte NOT_LOCK_OWNER = -69;
	
	/** Item already has a parent. */
	@SquirrelJMEVendorApi
	byte HAS_PARENT = -70;
	
	/** Member already exists. */
	@SquirrelJMEVendorApi
	byte MEMBER_EXISTS = -71;
	
	/** The native widget system failed for some reason. */
	@SquirrelJMEVendorApi
	byte NATIVE_WIDGET_FAILURE = -72;
	
	/** The number of error codes. */
	@SquirrelJMEVendorApi
	byte SJME_NUM_ERROR_CODES = -73;
}
