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
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.descriptors.MethodSymbol;
import net.multiphasicapps.interpreter.program.VMCProgram;

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
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Owning method. */
	protected final JVMMethod method;
	
	/** The class constant pool. */
	protected final JVMConstantPool constantpool;
	
	/** The class file parser. */
	protected final JVMClassFile classfile;
	
	/** Current active code source, may change in special circumstances. */
	private volatile DataInputStream _source;
	
	/** Did this already? */
	private volatile boolean _did;
	
	/** The current address of the current operation. */
	private volatile int _pcaddr;
	
	/** The limit stream used for counting the "next" operation. */
	private volatile JVMCountLimitInputStream _counter;
	
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
		
		// Setup state
		MethodSymbol msym = method.type();
		
		// Read in the code array so it can be parsed later after the
		// exceptions and the (optional) StackMap/StackMapTable are parsed.
		byte rawcode[] = new byte[codelen];
		for (int rx = 0; rx < codelen;)
		{
			// Read data
			int rc = __das.read(rawcode, rx, codelen - rx);
			
			// EOF is bad
			if (rc < 0)
				throw new JVMClassFormatError(String.format("IN1r %d %d",
					rx, codelen));
			
			// Add into it
			rx += rc;
		}
		
		// Setup a byte program for translation and dynamic cache friendly
		// program parsing
		VMCProgram jbp = new VMCProgram(maxlocal, maxstack, msym,
			!method.getFlags().isStatic(), rawcode);
		
		// Read the exception table
		{
			// If there are no exceptions, then use shorter handlers
			int numex = __das.readUnsignedShort();
			if (numex == 0)
				jbp.setNoExceptions();
			
			// There are exceptions
			else
				for (int i = 0; i < numex; i++)
					throw new Error("TODO");
		}
		
		// Handle attributes, only two are cared about
		int nas = __das.readUnsignedShort();
		for (int i = 0; i < nas; i++)
		{
			// Read attribute name
			String an = classfile.__readAttributeName(__das);
			
			// Depends on the name
			boolean newstack = false;
			switch (an)
			{
					// There are two attributes which represent stack maps
					// which are used for verification (and in my case it also
					// include optimization). StackMap existed since CLDC 1.0
					// and at the basic level is the same as StackMapTable
					// which was introduced in Java 6. The newer version
					// (StackMapTable) is just more compact, but they
					// essentially provide the same data.
				case "StackMapTable":
					newstack = true;
				case "StackMap":
					// For older classes do not use this at all
					if (newstack != classfile.version().useStackMapTable())
					{
						classfile.__skipAttribute(__das);
						break;
					}
					
					// Read in and parse the stack map table
					try (DataInputStream smdi = new DataInputStream(
						new JVMCountLimitInputStream(__das,
							(((long)__das.readInt()) & 0xFFFFFFFFL))))
					{
						new __StackMapParser__(newstack, smdi, jbp);
					}
					
					// Done
					break;
					
					// Ignored
				default:
					classfile.__skipAttribute(__das);
					break;
			}
		}
		
		// Set the method program
		method.setProgram(jbp);
		
		// Self
		return this;
	}
}

