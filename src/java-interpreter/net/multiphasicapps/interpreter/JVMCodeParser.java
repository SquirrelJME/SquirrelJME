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

import java.io.DataInputStream;
import java.io.IOException;
import net.multiphasicapps.narf.NARFProgram;

/**
 * This parses the code block of a method and translates the byte code into
 * NARF code which either gets interpreted or compiled into native code.
 *
 * @since 2016/03/22
 */
public class JVMCodeParser
{
	/** Owning method. */
	protected final JVMMethod method;
	
	/** The class constant pool. */
	protected final JVMConstantPool constantpool;
	
	/** The class file parser. */
	protected final JVMClassFile classfile;
	
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
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/22
	 */
	public JVMCodeParser parse(DataInputStream __das)
		throws IOException, NullPointerException
	{
		// Check
		if (__das == null)
			throw new NullPointerException("NARG");
		
		// Max stack and local entries
		int maxstack = __das.readUnsignedShort();
		int maxlocal = __das.readUnsignedShort();
		
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
}

