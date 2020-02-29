// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.Constants;
import cc.squirreljme.jvm.CallStackItem;
import cc.squirreljme.jvm.JVMFunction;
import cc.squirreljme.jvm.SystemCallError;
import cc.squirreljme.jvm.SystemCallIndex;

/**
 * This is the base class for all throwable types.
 *
 * @since 2019/05/25
 */
public class Throwable
{
	/** The message to use. */
	final transient String _message;
	
	/** The cause of this exception. */
	final transient Throwable _cause;
	
	/** The call trace. */
	final transient int[] _rawtrace;
	
	/** Suppressed exceptions. */
	transient volatile Throwable[] _suppressed;
	
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2019/05/25
	 */
	public Throwable()
	{
		this(null, null);
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2019/05/25
	 */
	public Throwable(String __m)
	{
		this(__m, null);
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __m The message.
	 * @param __t The cause.
	 * @since 2019/05/25
	 */
	public Throwable(String __m, Throwable __t)
	{
		// Hit a breakpoint if this is OOM
		if (this instanceof OutOfMemoryError)
			Assembly.breakpoint();
		
		this._message = __m;
		this._cause = __t;
		
		// Get the trace
		this._rawtrace = Throwable.__trace();
		
		// Print this trace
		this.printStackTrace();
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __t The cause.
	 * @since 2019/05/25
	 */
	public Throwable(Throwable __t)
	{
		this(null, __t);
	}
	
	/**
	 * Adds a suppressed throwable which will be thrown alongside this
	 * throwable. This is mainly used with try-with-resources although a
	 * programmer may wish to add related throwables that additionally
	 * happened.
	 *
	 * @param __t The throwable to suppress.
	 * @throws IllegalArgumentException If the passed throwable is this.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/15
	 */
	public final void addSuppressed(Throwable __t)
		throws IllegalArgumentException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error SV05 Cannot add a suppressed exception which
		// is this exception.}
		if (__t == this)
			throw new IllegalArgumentException("SV05");
		
		// No suppressed exceptions were set, initialize
		Throwable[] suppressed = this._suppressed;
		if (suppressed == null)
			this._suppressed = new Throwable[]{__t};
		
		// Otherwise rebuild the array and add it
		else
		{
			int n = suppressed.length;
			Throwable[] copy = new Throwable[n + 1];
			for (int i = 0; i < n; i++)
				copy[i] = suppressed[i];
			copy[n] = __t;
			
			// Use this instead
			this._suppressed = copy;
		}
	}
	
	/**
	 * Returns the message.
	 *
	 * @return The message used.
	 * @since 2019/06/16
	 */
	public String getMessage()
	{
		return this._message;
	}
	
	/**
	 * Prints the nicely formatted stack trace.
	 *
	 * @since 2019/06/17
	 */
	public void printStackTrace()
	{
		// This could fail in the event something is very wrong!
		try
		{
			// Print this and any causes!
			for (Throwable rover = this; rover != null; rover = rover._cause)
			{
				// Is this the main trace or a caused by?
				todo.DEBUG.note("%s Stack Trace: (%s) %s", (rover == this ?
					"Supervisor" : "Caused By"), JVMFunction.jvmLoadString(
						Assembly.pointerToClassInfo( Assembly.memReadInt(
						Assembly.objectToPointer(this),
						Constants.OBJECT_CLASS_OFFSET)).namep),
						rover.toString());
				
				// Obtain the raw trace that was captured on construction
				int[] rawtrace = this._rawtrace;
				int rawn = rawtrace.length;
				
				// Print all the items in it
				StringBuilder sb = new StringBuilder();
				for (int b = 0; b < rawn; b += CallStackItem.NUM_ITEMS)
				{
					// Print it out
					todo.DEBUG.note("    %s::%s:%s T%d (%s:%d) A@%d J@%d/%d",
						JVMFunction.jvmLoadString(
							rawtrace[b + CallStackItem.CLASS_NAME]),
						JVMFunction.jvmLoadString(
							rawtrace[b + CallStackItem.METHOD_NAME]),
						JVMFunction.jvmLoadString(
							rawtrace[b + CallStackItem.METHOD_TYPE]),
						rawtrace[b + CallStackItem.TASK_ID],
						JVMFunction.jvmLoadString(
							rawtrace[b + CallStackItem.SOURCE_FILE]),
						rawtrace[b + CallStackItem.SOURCE_LINE],
						Integer.toString(
							rawtrace[b + CallStackItem.PC_ADDRESS], 16),
						rawtrace[b + CallStackItem.JAVA_OPERATION],
						rawtrace[b + CallStackItem.JAVA_PC_ADDRESS]);
				}
			}
		}
		
		// If printing out this trace failed then use the backup mechanism!
		catch (Throwable t)
		{
			// Print the original trace
			this.printStackTraceBackup();
			
			// Print the raw backup trace for the trace we tried to print
			// to figure out what potentially went wrong?
			todo.DEBUG.codeBarrier('p', 'T');
			t.printStackTraceBackup();
			todo.DEBUG.codeBarrier('P', 't');
		}
	}
	
	/**
	 * Print a stack trace using only codes so that it may still be used
	 * accordingly as such.
	 *
	 * @since 2019/09/22
	 */
	public void printStackTraceBackup()
	{
		// Print this and any causes!
		for (Throwable rover = this; rover != null; rover = rover._cause)
		{
			// Supervisor or caused by?
			todo.DEBUG.code('T', (rover == this ? 'S' : 'C'),
				Assembly.objectToPointer(rover));
			
			// The thrown type
			todo.DEBUG.code('T', 'Y', Assembly.memReadInt(
				Assembly.objectToPointer(this),
				Constants.OBJECT_CLASS_OFFSET));
			
			// Obtain the raw trace that was captured on construction
			int[] rawtrace = this._rawtrace;
			int rawn = rawtrace.length;
			
			// Print all the items in it
			for (int base = 0; base < rawn; base += CallStackItem.NUM_ITEMS)
				try
				{
					// Indicate start of element
					todo.DEBUG.code('T', '-');
					
					// Print out the raw details
					todo.DEBUG.codeUtf('T', 'c',
						rawtrace[base + CallStackItem.CLASS_NAME]);
					todo.DEBUG.codeUtf('T', 'n',
						rawtrace[base + CallStackItem.METHOD_NAME]);
					todo.DEBUG.codeUtf('T', 'y',
						rawtrace[base + CallStackItem.METHOD_TYPE]);
					todo.DEBUG.codeUtf('T', '#',
						rawtrace[base + CallStackItem.TASK_ID]);
					todo.DEBUG.codeUtf('T', 'f',
						rawtrace[base + CallStackItem.SOURCE_FILE]);
					todo.DEBUG.code('T', 'l',
						rawtrace[base + CallStackItem.SOURCE_LINE]);
					todo.DEBUG.code('T', 'a',
						rawtrace[base + CallStackItem.PC_ADDRESS]);
					todo.DEBUG.code('T', 'j',
						rawtrace[base + CallStackItem.JAVA_PC_ADDRESS]);
					todo.DEBUG.code('T', 'o',
						rawtrace[base + CallStackItem.JAVA_OPERATION]);
				}
				
				// Error getting stack trace element
				catch (Throwable t)
				{
					todo.DEBUG.code('X', 'T', base);
				}
			
			// Indicate end of current set
			todo.DEBUG.code('T', '<');
		}
		
		// Indicate end of trace log
		todo.DEBUG.code('T', '_');
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/24
	 */
	@Override
	public String toString()
	{
		return this._message;
	}
	
	/**
	 * Returns the call stack.
	 *
	 * @return The resulting call stack.
	 * @since 2019/06/17
	 */
	private static final int[] __trace()
	{
		// Get the call height, ignore if not supported!
		int callheight = Assembly.sysCallPV(SystemCallIndex.CALL_STACK_HEIGHT);
		if (callheight <= 0 || Assembly.sysCallPV(SystemCallIndex.ERROR_GET,
			SystemCallIndex.CALL_STACK_HEIGHT) != SystemCallError.NO_ERROR)
			return new int[0];
		
		// Remove the top-most frame because it will be this method
		callheight--;
		
		// Get the call parameters
		int[] rv = new int[callheight * CallStackItem.NUM_ITEMS];
		for (int z = 0, base = 0; z < callheight; z++,
			base += CallStackItem.NUM_ITEMS)
			for (int i = 0; i < CallStackItem.NUM_ITEMS; i++)
			{
				// Get parameter
				int vx = Assembly.sysCallPV(SystemCallIndex.CALL_STACK_ITEM,
					1 + z, i);
				
				// Nullify unknown or invalid parameters
				if (Assembly.sysCallPV(SystemCallIndex.ERROR_GET,
					SystemCallIndex.CALL_STACK_ITEM) !=
					SystemCallError.NO_ERROR)
					vx = 0;
				
				// Fill in
				rv[base + i] = vx;
			}
		
		// Return the raw parameters
		return rv;
	}
}

