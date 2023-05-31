// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.jvm.aot.LinkGlob;
import java.io.IOException;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.Method;

/**
 * This processes the class and keeps track of any needed state.
 *
 * @since 2023/05/31
 */
public class ClassProcessor
{
	/** The input class file. */
	protected final ClassFile classFile;
	
	/** The glob this is being processed under. */
	protected final NanoCoatLinkGlob glob;
	
	/** The output source writer. */
	protected final CSourceWriter out;
	
	/** The C identifier for this class. */
	protected final String classIdentifier;
	
	/**
	 * Initializes the class processor.
	 *
	 * @param __glob The owning glob.
	 * @param __out The output source writer.
	 * @param __classFile The class file being processed.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public ClassProcessor(NanoCoatLinkGlob __glob, CSourceWriter __out,
		ClassFile __classFile)
		throws NullPointerException
	{
		if (__glob == null || __out == null || __classFile == null)
			throw new NullPointerException("NARG");
		
		this.glob = __glob;
		this.out = __out;
		this.classFile = __classFile;
		
		// Some initial information processing
		this.classIdentifier = Utils.symbolClassName(__glob,
			this.classFile.thisName());
	}
	
	/**
	 * Processes the class header output.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	protected void processHeader()
		throws IOException
	{
		CSourceWriter out = this.out;
		ClassFile classFile = this.classFile;
		NanoCoatLinkGlob glob = this.glob;
		
		// Write class identifier
		out.variableSet(CModifiers.EXTERN_CONST,
			CBasicType.JCLASS, this.classIdentifier);
		
		// Write method prototypes
		for (Method method : classFile.methods())
			out.functionPrototype(null,
				Utils.symbolMethodName(glob, method),
				null,
				CFunctionArgument.of(CBasicType.SJME_NANOSTATE.pointerType(),
					"state"));
	}
	
	/**
	 * Processes the class source output.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	protected void processSource()
		throws IOException
	{
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
}
