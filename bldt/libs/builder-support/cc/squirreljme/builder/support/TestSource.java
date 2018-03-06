// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support;

import cc.squirreljme.kernel.suiteinfo.SuiteInfo;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import net.multiphasicapps.javac.ByteArrayCompilerInput;
import net.multiphasicapps.javac.CompilerInput;
import net.multiphasicapps.javac.CompilerPathSet;
import net.multiphasicapps.javac.DistinctPathSet;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.writer.MutableJavaManifest;
import net.multiphasicapps.tool.manifest.writer.MutableJavaManifestAttributes;

/**
 * This represents source code for the tests which are made for a given project
 * which allows the invocation area of the test code to be automatically
 * generated accordingly.
 *
 * @since 2018/03/06
 */
public final class TestSource
	extends Source
{
	/** The root of the source. */
	protected final Path root;
	
	/** The project this is a test for. */
	protected final BasicSource testfor;
	
	/** The virtual main package. */
	protected final String mainpackage;
	
	/** Virtual manifest for this source. */
	private volatile Reference<JavaManifest> _manifest;
	
	/**
	 * Initializes the source for test projects.
	 *
	 * @param __root The directory for the test.
	 * @param __for The base project this is a test for.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	public TestSource(Path __root, BasicSource __for)
		throws NullPointerException
	{
		super(new SourceName(__for.name() + ".test"), ProjectType.MIDLET);
		
		if (__root == null || __for == null)
			throw new NullPointerException("NARG");
		
		this.root = __root;
		this.testfor = __for;
		
		// Setup virtual main package
		this.mainpackage = "cc.squirreljme.tests._" + __for.name().toString().
			replace('-', '_');
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	protected final CompilerPathSet internalPathSet()
	{
		// Generate input
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos, true))
		{
			// Specify test package so the main entry always goes in its
			// own package
			// Do not end in newline so the line numbers match the template
			String mainpackage = this.mainpackage;
			ps.printf("package %s;", mainpackage);
			
			// Include template test header with test information
			try (InputStream in = TestSource.class.getResourceAsStream(
				"testmain.template"))
			{
				byte[] buf = new byte[512];
				for (;;)
				{
					int rc = in.read(buf);
					
					if (rc < 0)
						break;
					
					ps.write(buf, 0, rc);
				}
			}
			
			// Store special auto-generated test information class
			
			// Build input, make sure to place it in the correct directory
			ps.flush();
			return new DistinctPathSet(new ByteArrayCompilerInput(
				mainpackage.replace('.', '/') + "/TestMain.java",
				baos.toByteArray()));
		}
		
		// {@squirreljme.error AU17 Could not generate the virtual test
		// source project.}
		catch (IOException e)
		{
			throw new RuntimeException("AU17", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final JavaManifest sourceManifest()
	{
		Reference<JavaManifest> ref = this._manifest;
		JavaManifest rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			// Build virtual manifest intended to act as tests for the input
			// library
			BasicSource testfor = this.testfor;
			MutableJavaManifest wman = new MutableJavaManifest();
			MutableJavaManifestAttributes wattr = wman.getMainAttributes();
		
			SuiteInfo bs = testfor.suiteInfo();
			
			String coolname = "Tests for " + bs.name().toString();
			
			wattr.putValue("x-squirreljme-name", coolname);
			wattr.putValue("x-squirreljme-vendor", bs.vendor().toString());
			wattr.putValue("x-squirreljme-version", bs.version().toString());
			wattr.putValue("x-squirreljme-description", coolname);
			wattr.putValue("x-squirreljme-depends", testfor.name().name() +
				" meep-midlet");
			wattr.putValue("main-class", this.mainpackage + ".TestMain");
			wattr.putValue("midlet-1", bs.name() + ",, " +
				this.mainpackage + ".TestMain");
			
			// Finalize
			this._manifest = new WeakReference<>((rv = wman.build()));
		}
		
		return rv;
	}
}

