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
import java.io.Reader;
import java.io.Writer;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.build.system.target.AbstractTarget;
import net.multiphasicapps.squirreljme.build.system.target.TargetConfig;
import net.multiphasicapps.squirreljme.executable.ExecutableClass;
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
	protected final Writer output;
	
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
		this.output = new OutputStreamWriter(__os, "utf-8");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/15
	 */
	@Override
	protected void acceptClass(ExecutableClass __ec)
		throws IOException, NullPointerException
	{
		// Check
		if (__ec == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/15
	 */
	@Override
	protected void acceptResource(String __n, InputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__n == null || __is == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
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
		try (Writer output = this.output)
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
						
						output.write(buf, 0, rc);
					}
				}
				
				// Perform JIT compilation of all the code
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
						
						output.write(buf, 0, rc);
					}
				}
			}
			
			// Always flush
			finally
			{
				output.flush();
			}
		}
	}
}

