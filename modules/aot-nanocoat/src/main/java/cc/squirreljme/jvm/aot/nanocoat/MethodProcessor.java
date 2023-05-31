// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import java.io.IOException;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.Method;

/**
 * This processes methods.
 *
 * @since 2023/05/31
 */
public final class MethodProcessor
{
	/** The input class file. */
	protected final ClassFile classFile;
	
	/** The glob this is being processed under. */
	protected final NanoCoatLinkGlob glob;
	
	/** The output source writer. */
	protected final CSourceWriter out;
	
	/** The method identifier in C. */
	protected final String methodIdentifier;
	
	/** The method being processed. */
	protected final Method method;
	
	/**
	 * Initializes the method processor.
	 * 
	 * @param __glob The link glob this is under.
	 * @param __out The source output.
	 * @param __classProcessor The class file this is processing under.
	 * @param __method The method to be processed.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public MethodProcessor(NanoCoatLinkGlob __glob, CSourceWriter __out,
		ClassProcessor __classProcessor, Method __method)
		throws NullPointerException
	{
		if (__glob == null || __out == null || __classProcessor == null ||
			__method == null)
			throw new NullPointerException("NARG");
		
		this.glob = __glob;
		this.out = __out;
		this.classFile = __classProcessor.classFile;
		this.method = __method;
		
		// Determine the identifier used for this class
		this.methodIdentifier = Utils.symbolMethodName(__glob,
			__method);
	}
	
	/**
	 * Processes headers for method.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	public void processHeader()
		throws IOException
	{
		// Write out the prototype
		this.out.functionPrototype(null,
			this.methodIdentifier, null,
			CFunctionArgument.of(CBasicType.SJME_NANOSTATE.pointerType(),
				"state"));
	}
	
	/**
	 * Processes source code within the class struct.
	 * 
	 * @param __struct The struct to write into.
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	public void processSourceInClass(CStructVariableBlock __struct)
		throws IOException
	{
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
	
	/**
	 * Processes the source details for this method outside of the class
	 * definition.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	public void processSourceOutside()
		throws IOException
	{
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
}
