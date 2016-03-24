// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This parses the code block of a method and translates the byte code into
 * NARF code which either gets interpreted or compiled into native code.
 *
 * @since 2016/03/22
 */
public class JVMCodeParser
{
	/** The maximum size method code may be. */
	public static final int MAX_CODE_SIZE =
		65535;
	
	/** Handler shift count. */
	private static final int _HANDLER_SHIFT =
		4;
	
	/** Handler shift mask. */
	private static final int _HANDLER_MASK =
		(1 << _HANDLER_SHIFT) - 1;
	
	/** Handlers for operations. */
	private static final Reference<ByteOpHandler>[] _HANDLERS =
		__makeByteOpRefArray();
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Owning method. */
	protected final JVMMethod method;
	
	/** The class constant pool. */
	protected final JVMConstantPool constantpool;
	
	/** The class file parser. */
	protected final JVMClassFile classfile;
	
	/** The program bridge. */
	protected final HandlerBridge bridge =
		new HandlerBridge();
	
	/** Program state. */
	protected final JVMProgramState state =
		new JVMProgramState();
	
	/** Program output. */
	protected final JVMProgramOutput programoutput;
	
	/** Current active code source, may change in special circumstances. */
	private volatile DataInputStream _source;
	
	/** Did this already? */
	private volatile boolean _did;
	
	/** The current address of the current operation. */
	private volatile int _pcaddr;
	
	/**
	 * Initializes the code parser.
	 *
	 * @param __cfp The class file parser.
	 * @param __method The method owning the code being parsed.
	 * @param __pool The constant pool of the class.
	 * @throws NullPointerException On null arguments.
	 */
	public JVMCodeParser(JVMClassFile __cfp, JVMMethod __method,
		JVMConstantPool __pool)
		throws NullPointerException
	{
		// Check
		if (__cfp == null || __method == null || __pool == null)
			throw new NullPointerException("NARG");
		
		// Set
		method = __method;
		constantpool = __pool;
		classfile = __cfp;
		
		// Setup program output generator
		programoutput = method.outerClass().engine().programfactory.create();
	}
	
	/**
	 * Parses the code attribute and turns it into NARF code.
	 *
	 * @param __das The code attribute data.
	 * @return {@code this}.
	 * @throws IOException On read errors.
	 * @throws JVMClassFormatError If the code is malformed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/22
	 */
	public JVMCodeParser parse(DataInputStream __das)
		throws IllegalStateException, IOException, JVMClassFormatError,
			NullPointerException
	{
		// Check
		if (__das == null)
			throw new NullPointerException("NARG");
		
		// Only one
		synchronized (lock)
		{
			if (_did)
				throw new IllegalStateException("IN1g");
			_did = true;
		}
		
		// Max stack and local entries
		int maxstack = __das.readUnsignedShort();
		int maxlocal = __das.readUnsignedShort();
		
		// Read code length
		int codelen = __das.readInt();
		if (codelen <= 0 || codelen >= MAX_CODE_SIZE)
			throw new JVMClassFormatError(String.format("IN1f %d", codelen));
		
		// Setup initial stack state
		{
			if (true)
				throw new Error("TODO");
			
			/*
			// Create
			JVMOpState initstate = new JVMOpState(maxlocal, maxstack);
			
			// Get locals since they need to be filled with the arguments of
			// the method
			JVMValueState locals = initstate.getLocals();
			
			// If an instance method, the first local variable is this.
			int lx = 0;
			if (!method.getFlags().isStatic())
				locals.set(lx++, JVMVariableType.OBJECT);
			
			// Handle method arguments otherwise
			MethodSymbol sym = method.type();
			int nargs = sym.argumentCount();
			for (int i = 0; i < nargs; i++)
			{
				// Get the one to add
				JVMVariableType addme = JVMVariableType.bySymbol(sym.get(i));
				
				// Place
				locals.set(lx++, addme);
				
				// If wide, follow with a top argument
				if (addme.isWide())
					locals.set(lx++, JVMVariableType.TOP);
			}
			
			// Set initial state
			opstates.put(0, initstate);
			*/
		}
		
		// Need to count the input bytes and also make sure that if code is
		// skipped that the entire chunk is ignored.
		try (JVMCountLimitInputStream clis = new JVMCountLimitInputStream(
			__das, ((long)codelen) & 0xFFFF_FFFFL);
			DataInputStream csource = new DataInputStream(clis))
		{
			// Set the code source
			_source = csource;
			
			// The code handlers could do something evil and try to use
			// multi-threaded code generation.
			synchronized (lock)
			{
				// Parse code data
				while (clis.hasRemaining())
				{
					// Set current address
					_pcaddr = (int)clis.count();
				
					// Handle it
					__handleOp();
				}
			}
		}
		
		if (true)
			throw new Error("TODO");
		
		// Handle attributes, only two are cared about
		int nas = __das.readUnsignedShort();
		for (int i = 0; i < nas; i++)
		{
			// Read attribute name
			String an = classfile.__readAttributeName(__das);
			
			// Depends on the name
			// StackMapTable and StackMap are just ignored, this will assume
			// that the compiler actually generates sane code (the stack map
			// just verifies information that can be obtained wnile parsing
			// and is essentially a "At this point, these must be on the stack"
			// kind of deal). Note that the code must conform to the assumption
			// that it would be under if it were actually handled (each
			// operation MUST have only one explicit state, so to speak).
			switch (an)
			{
					// Ignored
				default:
					classfile.__skipAttribute(__das);
					break;
			}
		}
		
		// Self
		return this;
	}
	
	/**
	 * Handles an input operation.
	 *
	 * @throws IOException On read errors.
	 * @since 2016/03/23
	 */
	private void __handleOp()
		throws IOException
	{
		// Get the code source
		DataInputStream source = _source;
		
		// Read in a single byte
		int code = source.readUnsignedByte();
		
		// If it is the wide specifier, read another byte and use a special
		// code for it
		if (code == 0xC4)
			code = 0x100 | source.readUnsignedByte();
		
		// Subroutines not supported)
		if (code == 0xA8 || code == 0xC9 || code == 0xA9 || code == 0xC4A9)
			throw new JVMClassFormatError(String.format("IN1m %d", code));
		
		// Nor is invokedynamic
		if (code == 0xBA)
			throw new JVMClassFormatError(String.format("IN1n %d", code));
		
		// Get operation handler
		ByteOpHandler handler = __obtainByteOpHandler(code);
		
		// Call it
		handler.handle(code, bridge);
	}
	
	/**
	 * This creates the byte operation handler reference array.
	 *
	 * @return The handler cache array.
	 * @since 2016/03/23
	 */
	@SuppressWarnings({"unchecked"})
	private static Reference<ByteOpHandler>[] __makeByteOpRefArray()
	{
		return (Reference<ByteOpHandler>[])
			((Object)new Reference[512 >>> _HANDLER_SHIFT]);
	}
	
	/**
	 * Obtains from the cache or caches a class which is used for the handling
	 * of byte code operations. This is to prevent this file from being a
	 * massive 5000 line file.
	 *
	 * @param __code The opcode, if the value is >= 0x100 then.
	 * @return The handler for the given operation.
	 * @throws IndexOutOfBoundsException If the code is not within the bounds
	 * of the cache table.
	 * @throws JVMClassFormatError If the opcode is not valid.
	 * @since 2016/03/23
	 */
	private static ByteOpHandler __obtainByteOpHandler(int __code)
		throws IndexOutOfBoundsException, JVMClassFormatError
	{
		// Major shift
		int major = __code >>> _HANDLER_SHIFT;
		
		// Get the handler array and check bounds
		Reference<ByteOpHandler>[] refs = _HANDLERS;
		if (major < 0 || major >= refs.length)
			throw new JVMClassFormatError(String.format("IN1h %d", __code));
		
		// Lock on the handlers
		synchronized (refs)
		{
			// Get reference
			Reference<ByteOpHandler> ref = refs[major];
			ByteOpHandler rv;
			
			// Needs caching?
			if (ref == null || null == (rv = ref.get()))
			{
				// Can fail
				try
				{
					// Find class first
					Class<?> ohcl = Class.forName("net.multiphasicapps." +
						"interpreter.jvmops.JVMOpHandler" +
						(__code & ~_HANDLER_MASK) + "To" +
						(__code | _HANDLER_MASK));
					
					// Create instance of it
					rv = ByteOpHandler.class.cast(ohcl.newInstance());
				}
				
				// Could not find, create, or cast.
				catch (InstantiationException|IllegalAccessException|
					ClassNotFoundException|ClassCastException e)
				{
					throw new JVMClassFormatError(
						String.format("IN1h %d", __code), e);
				}
				
				// Cache it
				refs[major] = new WeakReference<>(rv);
			}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * This is a bridge which is used to interact with the code parser because
	 * the operation handlers are for the most part statically used.
	 *
	 * @since 2016/03/23
	 */
	public class HandlerBridge
	{
		/**
		 * Internally initialized only.
		 *
		 * @since 2016/03/23
		 */
		private HandlerBridge()
		{
		}
		
		/**
		 * Returns the address of the current instruction.
		 *
		 * @return The current instruction address.
		 * @since 2016/03/23
		 */
		public int pcAddress()
		{
			// Lock
			synchronized (lock)
			{
				return _pcaddr;
			}
		}
		
		/**
		 * Returns the program state.
		 *
		 * @return The program state.
		 * @since 2016/03/24
		 */
		public JVMProgramState programState()
		{
			return state;
		}
		
		/**
		 * Returns the stream to read bytes from.
		 *
		 * @return The stream to read bytes from
		 * @since 2016/03/23
		 */
		public DataInputStream source()
		{
			// Lock
			synchronized (lock)
			{
				return _source;
			}
		}
	}
	
	/**
	 * This class contains the base for a byte operation handler, when an
	 * operation needs to be handled, it is searched for in a lookup table.
	 * If it is not there, then it is created and cached.
	 *
	 * @since 2016/03/23
	 */
	public static interface ByteOpHandler
	{
		/**
		 * Handles the given operation.
		 *
		 * @param __op The opcode which is requested to be handled.
		 * @param __br The bridge which interfaces with this code parser.
		 * @throws IOException On read errors.
		 * @since 2016/03/23
		 */
		public abstract void handle(int __op, JVMCodeParser.HandlerBridge __br)
			throws IOException;
	}
}

