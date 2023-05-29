// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.jvm.aot.Backend;
import cc.squirreljme.jvm.aot.CompileSettings;
import cc.squirreljme.jvm.aot.LinkGlob;
import cc.squirreljme.jvm.aot.RomSettings;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.Method;

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
		ClassName className = new ClassName(__name);
		String classIdentifier = Utils.symbolClassName(glob, className);
		
		// Load input class
		// {@squirreljme.error NC01 Mismatched class name.}
		ClassFile classFile = ClassFile.decode(__in);
		if (!classFile.thisName().equals(new ClassName(__name)))
			throw new RuntimeException("NC01");
		
		// Start of header
		out.preprocessorLine("ifdef", "SJME_C_CH");
		
		// Write class identifier
		out.printf("extern SJME_CONST sjme_nanoclass %s;",
			classIdentifier);
		out.freshLine();
		
		// Write method identifiers
		for (Method method : classFile.methods())
		{
			out.printf("sjme_nanostatus %s(sjme_nanostate* state);",
				Utils.symbolMethodName(glob, method));
			out.freshLine();
		}
		
		// Start of source
		out.preprocessorLine("else", "");
		
		if (true)
			throw Debugging.todo();
		
		// End source/header
		out.preprocessorLine("endif", "");
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
		out.preprocessorLine("ifdef", "SJME_C_CH");
		
		// Write identifier reference
		out.printf("extern SJME_CONST sjme_jbyte %s;", rcIdentifier);
		out.freshLine();
		
		// Start of source
		out.preprocessorLine("else", "");
		
		// Load in byte values
		out.printf("SJME_CONST sjme_jbyte %s = {", rcIdentifier);
		out.freshLine();
		out.byteValues(StreamUtils.readAll(__in));
		out.printf("};");
		out.freshLine();
		
		// End source/header
		out.preprocessorLine("endif", "");
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
