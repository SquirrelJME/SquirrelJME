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
public interface MLECallErrorCode
{
	/** No error. */
	byte NONE = 1;

	/** Generic unknown error. */
	byte UNKNOWN = 0;

	/** Generic unknown error. */
	byte UNKNOWN_NEGATIVE = -1;
	
	/** Null arguments. */
	byte NULL_ARGUMENTS = -2;
	
	/** Local variable out of bounds. */
	byte LOCAL_INDEX_INVALID = -3;
	
	/** Stack variable out of bounds. */
	byte STACK_INDEX_INVALID = -4;
	
	/** Stack underflow. */
	byte STACK_UNDERFLOW = -5;
	
	/** Stack overflow. */
	byte STACK_OVERFLOW = -6;
	
	/** Top is not an integer type. */
	byte TOP_NOT_INTEGER = -7;
	
	/** Top is not a long type. */
	byte TOP_NOT_LONG = -8;
	
	/** Top is not a float type. */
	byte TOP_NOT_FLOAT = -9;
	
	/** Top is not a double type. */
	byte TOP_NOT_DOUBLE = -10;
	
	/** Top is not a object type. */
	byte TOP_NOT_OBJECT = -11;
	
	/** Frame is missing stack treads. */
	byte FRAME_MISSING_STACK_TREADS = -12;
	
	/** Invalid read of stack. */
	byte STACK_INVALID_READ = -13;
	
	/** Invalid write of stack. */
	byte STACK_INVALID_WRITE = -14;
	
	/** Invalid read of stack. */
	byte LOCAL_INVALID_READ = -15;
	
	/** Invalid write of stack. */
	byte LOCAL_INVALID_WRITE = -16;
	
	/** Invalid reference pop. */
	byte INVALID_REFERENCE_POP = -17;
	
	/** Invalid reference push. */
	byte INVALID_REFERENCE_PUSH = -18;
	
	/** Failed to garbage collect object. */
	byte COULD_NOT_GC_OBJECT = -19;
	
	/** Object reference count is not zero. */
	byte OBJECT_REFCOUNT_NOT_ZERO = -20;
	
	/** Garbage collection of object cancelled. */
	byte OBJECT_GC_CANCELLED = -21;

	/** Out of memory. */
	byte OUT_OF_MEMORY = -22;

	/** Pool initialization failed. */
	byte POOL_INIT_FAILED = -23;

	/** Invalid argument. */
	byte INVALID_ARGUMENT = -24;

	/** Not implemented. */
	byte NOT_IMPLEMENTED = -25;

	/** Invalid tread read. */
	byte TREAD_INVALID_READ = -26;

	/** Invalid tread write. */
	byte TREAD_INVALID_WRITE = -27;

	/** There are no suites available. */
	byte NO_SUITES = -28;

	/** Classpath cannot be obtained by both ID and Name. */
	byte CLASS_PATH_BY_BOTH = -29;

	/** Illegal state. */
	byte ILLEGAL_STATE = -30;

	/** A library was not found. */
	byte LIBRARY_NOT_FOUND = -31;

	/** Boot failure. */
	byte BOOT_FAILURE = -32;

	/** Generic JNI exception. */
	byte JNI_EXCEPTION = -33;

	/** Memory has been corrupted. */
	byte MEMORY_CORRUPTION = -34;

	/** Index out of bounds. */
	byte INDEX_OUT_OF_BOUNDS = -35;

	/** Unsupported operation. */
	byte UNSUPPORTED_OPERATION = -36;

	/** Resource not found. */
	byte RESOURCE_NOT_FOUND = -37;

	/** Unexpected end of file. */
	byte UNEXPECTED_EOF = -38;
	
	/** Invalid identifier. */
	byte INVALID_IDENTIFIER = -39;
	
	/** Invalid binary name. */
	byte INVALID_BINARY_NAME = -40;
	
	/** Invalid field type. */
	byte INVALID_FIELD_TYPE = -41;
	
	/** Invalid method type. */
	byte INVALID_METHOD_TYPE = -42;
	
	/** Invalid class name. */
	byte INVALID_CLASS_NAME = -43;
	
	/** Could not load library. */
	byte COULD_NOT_LOAD_LIBRARY = -44;
	
	/** Invalid library symbol. */
	byte INVALID_LIBRARY_SYMBOL = -45;
	
	/** There is no graphics display. */
	byte HEADLESS_DISPLAY = -46;
	
	/** Cannot create something. */
	byte CANNOT_CREATE = -47;
	
	/** Invalid thread state. */
	byte INVALID_THREAD_STATE = -48;
	
	/** Component is already in a container. */
	byte ALREADY_IN_CONTAINER = -49;
	
	/** Not a sub-component. */
	byte NOT_SUB_COMPONENT = -50;
	
	/** No such class exists. */
	byte NO_CLASS = -51;
	
	/** No such method exists. */
	byte NO_METHOD = -52;
	
	/** There is no listener. */
	byte NO_LISTENER = -53;
	
	/** Cancel close of window. */
	byte CANCEL_WINDOW_CLOSE = -54;
	
	/** The class cannot be casted. */
	byte CLASS_CAST = -55;
	
	/** The font is not valid. */
	byte INVALID_FONT = -56;
	
	/** There is no Java environment. */
	byte NO_JAVA_ENVIRONMENT = -57;
	
	/** Font has negative height. */
	byte FONT_NEGATIVE_HEIGHT = -58;
	
	/** Could not create native widget. */
	byte NATIVE_WIDGET_CREATE_FAILED = -59;
	
	/** Clock failure. */
	byte NATIVE_SYSTEM_CLOCK_FAILURE = -60;
	
	/** A weak reference it attached. */
	byte WEAK_REFERENCE_ATTACHED = -61;
	
	/** An enqueue has already been set for the weak reference. */
	byte ENQUEUE_ALREADY_SET = -62;
	
	/** Keep the weak reference; do not free it on zero references. */
	byte ENQUEUE_KEEP_WEAK = -63;
	
	/** Not a weak reference. */
	byte NOT_WEAK_REFERENCE = -64;
	
	/** Could not access array natively. */
	byte NATIVE_ARRAY_ACCESS_FAILED = -65;
	
	/** The graphics buffer is not locked. */
	byte BUFFER_NOT_LOCKED = -66;
	
	/** Component is not in this container. */
	byte NOT_IN_CONTAINER = -67;
	
	/** Invalid link. */
	byte INVALID_LINK = -68;
	
	/** We are not the owner of the lock. */
	byte NOT_LOCK_OWNER = -69;
	
	/** Item already has a parent. */
	byte HAS_PARENT = -70;
	
	/** Member already exists. */
	byte MEMBER_EXISTS = -71;
	
	/** The native widget system failed for some reason. */
	byte NATIVE_WIDGET_FAILURE = -72;
	
	/** The number of error codes. */
	byte SJME_NUM_ERROR_CODES = -73;
}
