// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.system.target.webdemo;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.build.system.target.AbstractTarget;
import net.multiphasicapps.squirreljme.build.system.target.TargetConfig;
import net.multiphasicapps.squirreljme.executable.ExecutableClass;
import net.multiphasicapps.squirreljme.jit.webdemo.JSEngine;
import net.multiphasicapps.squirreljme.jit.webdemo.JSEngineProvider;

/**
 * This is the target which generates the Web demo which runs on top of
 * Javascript.
 *
 * @since 2017/03/13
 */
public class WebDemoTarget
	extends AbstractTarget
{
	/** The output stream where the generated HTML goes. */
	protected final PrintStream output;
	
	/**
	 * Initializes the target to the Web demo.
	 *
	 * @param __pm The projects available for usage.
	 * @param __conf The configuration to use during build.
	 * @param __os The stream where the output target is to be placed.
	 * @throws IOException On read/write errors.
	 * @since 2017/03/13
	 */
	public WebDemoTarget(ProjectManager __pm, TargetConfig __conf,
		OutputStream __os)
		throws IOException
	{
		super(__pm, __conf, __os);
		
		// Setup output, always write in UTF-8
		this.output = new PrintStream(new __DebugOutput__(__os),
			true, "utf-8");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/15
	 */
	@Override
	protected void acceptClass(String __jn, ExecutableClass __ec)
		throws IOException, NullPointerException
	{
		// Check
		if (__jn == null || __ec == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/15
	 */
	@Override
	protected void acceptResource(String __jn, String __fn, InputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__jn == null || __fn == null || __is == null)
			throw new NullPointerException("NARG");
		
		PrintStream output = this.output;
		
		// Declare a string named by this resource
		output.print("var ");
		output.print(__jn);
		output.print(__fn);
		output.print(" = \"");
		
		// Write string data, but use a fancy encoding format because
		// Javascript encodes data as the character set of the string and on
		// top of that there are no binary data types used.
		// \ ' and " must be escaped
		// It is easier to have a binary mode in place which switches encoding
		// as such.
		boolean binmode = false;
		for (;;)
		{
			// Read a byte
			int v = __is.read();
			if (v < 0)
				break;
			
			// Is a binary character being input?
			boolean isbin = (v < ' ' || v == '\\' || v == '\'' || v == '\"' ||
				v == '~' || v >= 127);
			
			// Switch of mode
			if (isbin != binmode)
			{
				output.print("~");
				binmode = isbin;
			}
			
			// Encode binary data
			if (binmode)
			{
				output.print(Character.forDigit((v >>> 4) & 0xF, 16));
				output.print(Character.forDigit(v & 0xF, 16));
			}
			
			// Normal mode directly copies
			else
				output.print((char)v);
		}
		
		// End it
		output.println("\";");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/13
	 */
	@Override
	public void run()
		throws IOException
	{
		// Close when finished
		try (PrintStream output = this.output)
		{
			// Generate code
			try
			{
				// Copy header
				char[] buf = new char[256];
				try (Reader r = new InputStreamReader(WebDemoTarget.class.
					getResourceAsStream("header.html"), "utf-8"))
				{
					for (;;)
					{
						int rc = r.read(buf);
						
						// EOF?
						if (rc < 0)
							break;
						
						for (int i = 0; i < rc; i++)
							output.print(buf[i]);
					}
				}
				
				// Perform JIT compilation of all the code
				output.flush();
				super.compile(new JSEngineProvider());
				
				// Pack final executable bits
				if (true)
					throw new todo.TODO();
				
				// Copy footer
				try (Reader r = new InputStreamReader(WebDemoTarget.class.
					getResourceAsStream("footer.html"), "utf-8"))
				{
					for (;;)
					{
						int rc = r.read(buf);
						
						// EOF?
						if (rc < 0)
							break;
						
						for (int i = 0; i < rc; i++)
							output.print(buf[i]);
					}
				}
				output.flush();
			}
			
			// Always flush
			finally
			{
				output.flush();
			}
		}
	}
}

