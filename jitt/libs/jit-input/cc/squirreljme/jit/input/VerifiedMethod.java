// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.input;

import cc.squirreljme.jit.classfile.ByteCode;
import cc.squirreljme.jit.classfile.Instruction;
import cc.squirreljme.jit.classfile.MethodHandle;
import cc.squirreljme.jit.program.BasicProgram;
import cc.squirreljme.jit.program.ByteCodeParser;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is a single method which has been verified.
 *
 * @since 2017/10/09
 */
public final class VerifiedMethod
{
	/** The handle of this method. */
	protected final MethodHandle handle;
	
	/** The index of this method in the verification order. */
	protected final int order;
	
	/** The program for this method. */
	protected final BasicProgram program;
	
	/**
	 * Initializes the verified method.
	 *
	 * @param __h The handle of the verified method.
	 * @param __o The verification order of the method.
	 * @param __p The program for this method.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/15
	 */
	private VerifiedMethod(MethodHandle __h, int __o, BasicProgram __p)
		throws NullPointerException
	{
		if (__h == null)
			throw new NullPointerException("NARG");
		
		this.handle = __h;
		this.order = __o;
		this.program = __p;
	}
	
	/**
	 * Verifies the specified method.
	 *
	 * @param __structs The structures for the class.
	 * @param __mi The method index.
	 * @param __vid The the index of this method in the sort list.
	 * @param __bc The byte code for this method.
	 * @throws NullPointerException On null arguments.
	 * @throws VerificationException If the method could not be verified.
	 * @since 2017/10/14
	 */
	final static VerifiedMethod __verify(ClassStructures __structs,
		MethodHandle __mi, int __vid, ByteCode __bc)
		throws NullPointerException, VerificationException
	{
		if (__structs == null || __mi == null || __bc == null)
			throw new NullPointerException("NARG");
		
		// Initialize the instruction parser
		ByteCodeParser parser = new ByteCodeParser(__bc);
		
		// Generate verified program
		return new VerifiedMethod(__mi, __vid, parser.parse());
	}
}

