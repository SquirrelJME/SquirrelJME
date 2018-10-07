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
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.SortedTreeSet;
import net.multiphasicapps.javac.ByteArrayCompilerInput;
import net.multiphasicapps.javac.CompilerInput;
import net.multiphasicapps.javac.CompilerPathSet;
import net.multiphasicapps.javac.DistinctPathSet;
import net.multiphasicapps.javac.FilePathSet;
import net.multiphasicapps.javac.MergedPathSet;
import net.multiphasicapps.javac.structure.RuntimeInput;
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
	
	/** Last modified time of the source code. */
	private volatile long _lastmodtime =
		Long.MIN_VALUE;
	
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
	public final CompilerPathSet pathSet(SourcePathSetType __spst)
		throws NullPointerException
	{
		if (__spst == null)
			throw new NullPointerException("NARG");
		
		// Always use the root source set
		return new FilePathSet(this.root);
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
				" meep-midlet unit-testing");
			
			// All of the tests are just MIDlets to be ran accordingly as if
			// they were unique individual programs within the JAR
			Set<String> classes = this.__testClass();
			int next = 1;
			for (String cl : classes)
				wattr.putValue("midlet-" + (next++),
					"TEST " + cl + ",," + cl);
			
			// Finalize
			this._manifest = new WeakReference<>((rv = wman.build()));
		}
		
		return rv;
	}
	
	/**
	 * Returns classes which make up the test classes.
	 *
	 * @return The tests which are available.
	 * @since 2018/10/06
	 */
	private Set<String> __testClass()
	{
		// Classes which are available for testing
		Set<String> classes = new SortedTreeSet<>();
		
		// Parse every input class file and look for tests according to
		// a given file syntax
		for (CompilerInput ci : this.pathSet(SourcePathSetType.SOURCE))
		{
			// Only consider Java source files
			String name = ci.fileName();
			if (!name.endsWith(".java"))
				continue;
			
			// Remove the extension
			name = name.substring(0, name.length() - 5);
			
			// Get the basename, because the test could be organized into
			// packages which might mess with parsing. Although generally
			// tests are likely in just the default package
			int ls = name.lastIndexOf('/');
			String base = (ls < 0 ? name : name.substring(ls + 1));
			
			// Only if the given patterns are matched is the test added
			if (base.startsWith("Do") ||
				base.startsWith("Test") ||
				base.endsWith("Test"))
				classes.add(name.replace('/', '.'));
		}
		
		// Return all the discovered classes
		return classes;
	}
}

