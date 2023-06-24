// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CExpressionBuilder;
import cc.squirreljme.c.CPPBlock;
import cc.squirreljme.c.CSourceWriter;
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.Backend;
import cc.squirreljme.jvm.aot.CompileSettings;
import cc.squirreljme.jvm.aot.LinkGlob;
import cc.squirreljme.jvm.aot.RomSettings;
import cc.squirreljme.jvm.aot.nanocoat.common.Constants;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmTypes;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;

/**
 * Nanocoat support.
 *
 * @since 2023/05/28
 */
public class NanoCoatBackend
	implements Backend
{
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void compileClass(CompileSettings __settings, LinkGlob __glob,
		String __name, InputStream __in)
		throws IOException, NullPointerException
	{
		if (__settings == null || __glob == null || __name == null ||
			__in == null)
			throw new NullPointerException("NARG");
		
		NanoCoatLinkGlob glob = (NanoCoatLinkGlob)__glob;
		CSourceWriter out = glob.out;
		
		// Load input class
		// {@squirreljme.error NC01 Mismatched class name.}
		ClassFile classFile = ClassFile.decode(__in);
		if (!classFile.thisName().equals(new ClassName(__name)))
			throw new RuntimeException("NC01");
		
		// Start of header
		try (CPPBlock block = out.preprocessorIf(CExpressionBuilder.builder()
				.preprocessorDefined(Constants.CODE_GUARD)
				.build()))
		{	
			// Setup and perform class processing, with state
			ClassProcessor processor = new ClassProcessor(glob, out,
				classFile);
			
			// Process header code
			processor.processHeader();
			
			// Start of source
			block.preprocessorElse();
			
			// Process source code
			processor.processSource();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void compileResource(CompileSettings __settings, LinkGlob __glob,
		String __path, InputStream __in)
		throws IOException, NullPointerException
	{
		if (__settings == null || __glob == null || __path == null ||
			__in == null)
			throw new NullPointerException("NARG");
		
		NanoCoatLinkGlob glob = (NanoCoatLinkGlob)__glob;
		CSourceWriter out = glob.out;
		
		// Mangle path of this resource to name it
		String rcIdentifier = Utils.symbolResourcePath(glob, __path);
		
		// Start of header
		try (CPPBlock block = out.preprocessorIf(CExpressionBuilder.builder()
			.preprocessorDefined(Constants.CODE_GUARD)
			.build()))
		{
			// Write identifier reference
			CVariable rcVar = CVariable.of(
				JvmTypes.STATIC_RESOURCE.type().constType(),
				rcIdentifier);
			out.declare(rcVar.extern());
			
			// Else for source code
			block.preprocessorElse();
			
			// Load in byte values
			byte[] data = StreamUtils.readAll(__in);
			
			// Write values for the resource data
			try (CStructVariableBlock struct = out.define(
				CStructVariableBlock.class, rcVar))
			{
				struct.memberSet("path",
					CExpressionBuilder.builder()
						.string(__path)
						.build());
				struct.memberSet("size",
					CExpressionBuilder.builder()
						.number(Constants.JINT_C, data.length)
						.build());
				struct.memberSet("data",
					CExpressionBuilder.builder()
						.array(data)
						.build());
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void dumpGlob(byte[] __inGlob, String __name, PrintStream __out)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public LinkGlob linkGlob(CompileSettings __settings, String __name,
		OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__settings == null || __name == null || __out == null)
			throw new NullPointerException("NARG");
		
		return new NanoCoatLinkGlob(__name, __out);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public String name()
	{
		return "nanocoat";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void rom(RomSettings __settings, OutputStream __out,
		VMClassLibrary... __libs)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
}
