// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.debug;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.CallStackItem;
import cc.squirreljme.jvm.SystemCallError;
import cc.squirreljme.jvm.SystemCallIndex;
import cc.squirreljme.jvm.mle.brackets.TracePointBracket;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * This represents a single entry within the call stack. This is used for
 * debugging purposes to determine where code has thrown an exception.
 *
 * @since 2018/02/21
 */
public final class CallTraceElement
{
	/** The class name. */
	protected final String className;
	
	/** The method name. */
	protected final String methodName;
	
	/** The method descriptor. */
	protected final String methodType;
	
	/** The execution pointer of the address. */
	protected final long address;
	
	/** The source code file. */
	protected final String file;
	
	/** The line in the file. */
	protected final int line;
	
	/** The Java byte code instruction. */
	protected final int byteCodeOp;
	
	/** The Java byte code address. */
	protected final int byteCodeAddr;
	
	/** The task ID. */
	protected final int taskId;
	
	/** String representation. */
	private Reference<String> _string;
	
	/** At line form. */
	private Reference<String> _stringatl;
	
	/** Class header form. */
	private Reference<String> _stringclh;
	
	/** Hash code. */
	private int _hash;
	
	/**
	 * Initializes an empty call trace element.
	 *
	 * @since 2018/02/21
	 */
	public CallTraceElement()
	{
		this(null, null, null, -1);
	}
	
	/**
	 * Initializes a call trace element.
	 *
	 * @param __cl The class name.
	 * @param __mn The method name.
	 * @param __md The method descriptor.
	 * @param __addr The address the method executes at.
	 * @since 2018/02/21
	 */
	public CallTraceElement(String __cl, String __mn, String __md, long __addr)
	{
		this(__cl, __mn, __md, __addr, null, -1);
	}
	
	/**
	 * Initializes a call trace element.
	 *
	 * @param __cl The class name.
	 * @param __mn The method name.
	 * @param __md The method descriptor.
	 * @param __addr The address the method executes at.
	 * @param __file The file.
	 * @param __line The line in the file.
	 * @since 2018/04/02
	 */
	public CallTraceElement(String __cl, String __mn, String __md, long __addr,
		String __file, int __line)
	{
		this(__cl, __mn, __md, __addr, __file, __line, -1, -1);
	}
	
	/**
	 * Initializes a call trace element.
	 *
	 * @param __cl The class name.
	 * @param __mn The method name.
	 * @param __md The method descriptor.
	 * @param __addr The address the method executes at.
	 * @param __file The file.
	 * @param __line The line in the file.
	 * @param __jbc The Java byte code instruction used.
	 * @param __jpc The Java PC address.
	 * @since 2019/04/26
	 */
	public CallTraceElement(String __cl, String __mn, String __md, long __addr,
		String __file, int __line, int __jbc, int __jpc)
	{
		this(__cl, __mn, __md, __addr, __file, __line, __jbc, __jpc, 0);
	}
	
	/**
	 * Initializes a call trace element.
	 *
	 * @param __cl The class name.
	 * @param __mn The method name.
	 * @param __md The method descriptor.
	 * @param __addr The address the method executes at.
	 * @param __file The file.
	 * @param __line The line in the file.
	 * @param __jbc The Java byte code instruction used.
	 * @param __jpc The Java PC address.
	 * @param __tid The task ID.
	 * @since 2019/10/05
	 */
	public CallTraceElement(String __cl, String __mn, String __md, long __addr,
		String __file, int __line, int __jbc, int __jpc, int __tid)
	{
		this.className = __cl;
		this.methodName = __mn;
		this.methodType = __md;
		this.address = __addr;
		this.file = __file;
		this.line = __line;
		this.byteCodeOp = __jbc;
		this.byteCodeAddr = __jpc;
		this.taskId = __tid;
	}
	
	/**
	 * Returns the address of the element.
	 *
	 * @return The element address.
	 * @since 2018/03/15
	 */
	public final long address()
	{
		return this.address;
	}
	
	/**
	 * Returns the address of the instruction at the Java byte code position.
	 *
	 * @return The address of the instruction in Java byte code.
	 * @since 2019/04/26
	 */
	public final int byteCodeAddress()
	{
		return this.byteCodeAddr;
	}
	
	/**
	 * Returns the byte code instruction that was used for this.
	 *
	 * @return The used byte code instruction or {@code 0xFF} if it is not
	 * valid or specified.
	 * @since 2019/04/26
	 */
	public final int byteCodeInstruction()
	{
		return this.byteCodeOp & 0xFF;
	}
	
	/**
	 * Returns the name of the associated class.
	 *
	 * @return The associated class.
	 * @since 2018/03/15
	 */
	public final String className()
	{
		return this.className;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/21
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (this.hashCode() != __o.hashCode())
			return false;
		
		if (!(__o instanceof CallTraceElement))
			return false;
		
		CallTraceElement o = (CallTraceElement)__o;
		return Objects.equals(this.className, o.className) &&
			Objects.equals(this.methodName, o.methodName) &&
			Objects.equals(this.methodType, o.methodType) &&
			Objects.equals(this.file, o.file) &&
			this.address == o.address &&
			this.line == o.line &&
			this.byteCodeOp == o.byteCodeOp &&
			this.byteCodeAddr == o.byteCodeAddr &&
			this.taskId == o.taskId;
	}
	
	/**
	 * Returns the source file.
	 *
	 * @return The source file.
	 * @since 2018/04/02
	 */
	public final String file()
	{
		return this.file;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/21
	 */
	@Override
	public final int hashCode()
	{
		int rv = this._hash;
		if (rv == 0)
		{
			long address = this.address;
			this._hash = (rv = Objects.hashCode(this.className) ^
				Objects.hashCode(this.methodName) ^
				Objects.hashCode(this.methodType) ^
				Objects.hashCode(this.file) ^
				(int)((address >>> 32) | address) ^
				~this.line +
				~this.byteCodeOp +
				~this.byteCodeAddr +
				~this.taskId);
		}
		return rv;
	}
	
	/**
	 * Returns the source file line.
	 *
	 * @return The source file line.
	 * @since 2018/04/02
	 */
	public final int line()
	{
		return this.line;
	}
	
	/**
	 * Returns the descriptor of the method.
	 *
	 * @return The method descriptor.
	 * @since 2018/03/15
	 */
	public final String methodDescriptor()
	{
		return this.methodType;
	}
	
	/**
	 * Returns the name of the method.
	 *
	 * @return The method name.
	 * @since 2018/03/15
	 */
	public final String methodName()
	{
		return this.methodName;
	}
	
	/**
	 * Formats the call trace element but having it only represent the method
	 * point without the class information.
	 *
	 * @return The at line string.
	 * @since 2019/05/11
	 */
	public final String toAtLineString()
	{
		Reference<String> ref = this._stringatl;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			// Get all fields to determine how to print it pretty
			String methodname = this.methodName,
				methoddescriptor = this.methodType;
			long address = this.address;
			int line = this.line;
			int jbcinst = this.byteCodeOp & 0xFF;
			int jbcaddr = this.byteCodeAddr;
			int taskid = this.taskId;
			
			// Format it nicely
			StringBuilder sb = new StringBuilder();
			
			// Method name
			sb.append('.');
			sb.append((methodname == null ? "<unknown>" : methodname));
			
			// Method type
			if (methoddescriptor != null)
			{
				sb.append(':');
				sb.append(methoddescriptor);
			}
			
			// Task ID?
			if (taskid != 0)
			{
				sb.append(" T");
				sb.append(taskid);
			}
			
			// Execution address
			if (address != Long.MIN_VALUE)
			{
				sb.append(" @");
				
				// If the address is really high then it is very likely that
				// this is some RAM/ROM address rather than some easily read
				// index. This makes them more readable and understandable
				if (address > 4096)
				{
					sb.append(Long.toString(address, 16).toUpperCase());
					sb.append('h');
				}
				
				// Otherwise use an index
				else
					sb.append(address);
			}
			
			// File, Line, and/or Java instruction/address
			boolean hasline = (line >= 0),
				hasjbcinst = (jbcinst > 0xFF && jbcinst < 0xFF),
				hasjbcaddr = (jbcaddr >= 0);
			if (hasline || hasjbcinst || hasjbcaddr)
			{
				sb.append(" (");
				
				// Line
				boolean sp = false;
				if ((sp |= hasline))
				{
					sb.append(':');
					sb.append(line);
				}
				
				// Java instruction info
				if (hasjbcinst || hasjbcaddr)
				{
					// Using space?
					if (sp)
						sb.append(' ');
					
					// Used to indicate Java specific stuff
					sb.append('J');
					
					// Write instruction
					if (hasjbcinst)
						sb.append(jbcinst);
					
					// Write address of Java operation
					if (hasjbcaddr)
					{
						sb.append('@');
						sb.append(jbcaddr);
					}
				}
				
				sb.append(')');
			}
			
			this._stringatl = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
	
	/**
	 * Formats the call trace element but having it only represent the class.
	 *
	 * @return The class header string.
	 * @since 2019/05/11
	 */
	public final String toClassHeaderString()
	{
		Reference<String> ref = this._stringclh;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			// Get all fields to determine how to print it pretty
			String classname = this.className,
				file = this.file;
			
			// Format it nicely
			StringBuilder sb = new StringBuilder();
			
			// Class name
			sb.append((classname == null ? "<unknown>" : classname));
			
			// Is this in a file?
			boolean hasfile = (file != null);
			if (hasfile)
			{
				sb.append(" (");
				
				// File
				boolean sp = false;
				if ((sp |= hasfile))
					sb.append(file);
				
				sb.append(')');
			}
			
			this._stringclh = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/14
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			// Get all fields to determine how to print it pretty
			String classname = this.className,
				methodname = this.methodName,
				methoddescriptor = this.methodType,
				file = this.file;
			long address = this.address;
			int line = this.line;
			int jbcinst = this.byteCodeOp & 0xFF;
			int jbcaddr = this.byteCodeAddr;
			int taskid = this.taskId;
			
			// Format it nicely
			StringBuilder sb = new StringBuilder();
			
			sb.append((classname == null ? "<unknown>" : classname));
			sb.append('.');
			sb.append((methodname == null ? "<unknown>" : methodname));
			
			if (methoddescriptor != null)
			{
				sb.append(':');
				sb.append(methoddescriptor);
			}
			
			// Task ID?
			if (taskid != 0)
			{
				sb.append(" T");
				sb.append(taskid);
			}
			
			if (address != Long.MIN_VALUE)
			{
				sb.append(" @");
				
				// If the address is really high then it is very likely that
				// this is some RAM/ROM address rather than some easily read
				// index. This makes them more readable and understandable
				if (address > 4096)
				{
					sb.append(Long.toString(address, 16).toUpperCase());
					sb.append('h');
				}
				
				// Otherwise use an index
				else
					sb.append(address);
			}
			
			// File, Line, and/or Java instruction/address
			boolean hasfile = (file != null),
				hasline = (line >= 0),
				hasjbcinst = (jbcinst > 0x00 && jbcinst < 0xFF),
				hasjbcaddr = (jbcaddr >= 0);
			if (hasfile || hasline || hasjbcinst || hasjbcaddr)
			{
				sb.append(" (");
				
				// File
				boolean sp = false;
				if ((sp |= hasfile))
					sb.append(file);
				
				// Line
				if ((sp |= hasline))
				{
					sb.append(':');
					sb.append(line);
				}
				
				// Java instruction info
				if (hasjbcinst || hasjbcaddr)
				{
					// Using space?
					if (sp)
						sb.append(' ');
					
					// Used to indicate Java specific stuff
					sb.append('J');
					
					// Write instruction
					if (hasjbcinst)
						sb.append(jbcinst);
					
					// Write address of Java operation
					if (hasjbcaddr)
					{
						sb.append('@');
						sb.append(jbcaddr);
					}
				}
				
				sb.append(')');
			}
			
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
	
	/**
	 * Obtains the current raw call trace which has not been resolved.
	 *
	 * @return The raw call trace.
	 * @since 2019/06/16
	 */
	@Deprecated
	public static final int[] traceRaw()
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
	
	/**
	 * Resolves the specified call trace into call trace elements.
	 *
	 * @param __trace The trace to resolve.
	 * @return The resolved trace.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/16
	 */
	@Deprecated
	public static final CallTraceElement[] traceResolve(int[] __trace)
		throws NullPointerException
	{
		if (__trace == null)
			throw new NullPointerException("NARG");
		
		// Get the call height
		int callheight = __trace.length / CallStackItem.NUM_ITEMS;
		
		// Process all the items
		CallTraceElement[] rv = new CallTraceElement[callheight];
		for (int z = 0, base = 0; z < callheight; z++,
			base += CallStackItem.NUM_ITEMS)
		{
			// Load class name
			int xcl = Assembly.sysCallV(SystemCallIndex.LOAD_STRING,
				__trace[base + CallStackItem.CLASS_NAME]);
			String scl = ((xcl == 0 || Assembly.sysCallV(
				SystemCallIndex.ERROR_GET, SystemCallIndex.LOAD_STRING) !=
				SystemCallError.NO_ERROR) ?
				(String)null : (String)Assembly.pointerToObject(xcl));
				
			// Load method name
			int xmn = Assembly.sysCallV(SystemCallIndex.LOAD_STRING,
				__trace[base + CallStackItem.METHOD_NAME]);
			String smn = ((xmn == 0 || Assembly.sysCallV(
				SystemCallIndex.ERROR_GET, SystemCallIndex.LOAD_STRING) !=
				SystemCallError.NO_ERROR) ?
				(String)null : (String)Assembly.pointerToObject(xmn));
			
			// Load method type
			int xmt = Assembly.sysCallV(SystemCallIndex.LOAD_STRING,
				__trace[base + CallStackItem.METHOD_NAME]);
			String smt = ((xmt == 0 || Assembly.sysCallV(
				SystemCallIndex.ERROR_GET, SystemCallIndex.LOAD_STRING) !=
				SystemCallError.NO_ERROR) ?
				(String)null : (String)Assembly.pointerToObject(xmt));
			
			// Load source file
			int xsf = Assembly.sysCallV(SystemCallIndex.LOAD_STRING,
				__trace[base + CallStackItem.SOURCE_FILE]);
			String ssf = ((xsf == 0 || Assembly.sysCallV(
				SystemCallIndex.ERROR_GET, SystemCallIndex.LOAD_STRING) !=
				SystemCallError.NO_ERROR) ?
				(String)null : (String)Assembly.pointerToObject(xsf));
			
			// The PC address
			int pcaddr = __trace[base + CallStackItem.PC_ADDRESS];
			
			// Task ID
			int tid = __trace[base + CallStackItem.TASK_ID];
			
			// Build elements
			rv[z] = new CallTraceElement(
				scl,
				smn,
				smt,
				(pcaddr == 0 ? -1 : pcaddr),
				ssf,
				__trace[base + CallStackItem.SOURCE_LINE],
				__trace[base + CallStackItem.JAVA_OPERATION],
				__trace[base + CallStackItem.JAVA_PC_ADDRESS],
				tid);
		}
		
		// Use the resolved form
		return rv;
	}
}

