// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.lang.c;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.JITNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.lang.LangOutput;
import net.multiphasicapps.zip.ZipCompressionType;
import net.multiphasicapps.zip.streamreader.ZipStreamEntry;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * This output generates C source code in ZIP archives.
 *
 * @since 2016/07/09
 */
public class CLangOutput
	extends LangOutput
{
	/**
	 * Initializes the C language output system.
	 *
	 * @param __config The used configuration.
	 * @since 2016/07/09
	 */
	public CLangOutput(JITOutputConfig.Immutable __config)
	{
		super(__config);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/09
	 */
	@Override
	public JITNamespaceWriter beginNamespace(String __ns)
		throws JITException, NullPointerException
	{
		// Check
		if (__ns == null)
			throw new NullPointerException("NARG");
		
		return new CLangNamespaceWriter(__ns, this.config);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/18
	 */
	@Override
	public String executableName()
	{
		// The output binary is always just a ZIP file
		return "squirreljme_c.zip";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/21
	 */
	@Override
	protected void globalEntries(ZipStreamWriter __zsw,
		String[] __names)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__zsw == null || __names == null)
			throw new Error("TODO");
		
		// Write class path output
		try (OutputStream os = __zsw.nextEntry("classp.c",
			ZipCompressionType.DEFAULT_COMPRESSION);
			PrintStream ps = new PrintStream(os, true, "utf-8"))
		{
			// Include global header
			ps.println("#include \"squirrel.h\"");
			ps.println();
			
			// Convert names
			int n = __names.length;
			String[] conv = new String[n];
			for (int i = 0; i < n; i++)
				conv[i] = CLangNamespaceWriter.escapeToCIdentifier(__names[i]);
			
			// Include headers
			for (int i = 0; i < n; i++)
			{
				ps.print("extern const SJME_Namespace ");
				ps.print(conv[i]);
				ps.println(';');
			}
			
			// Set JVM namespace list
			ps.println("const SJME_Namespaces initialNamespaces =");
			ps.println("{");
			ps.println("\tSJME_STRUCTURETYPE_NAMESPACES,\n");
			for (int i = 0; i < n; i++)
			{
				ps.print("\t&");
				ps.print(conv[i]);
				ps.println(',');
			}
			ps.println("\tNULL");
			ps.println("};");
			ps.println();
		}
	}
}

