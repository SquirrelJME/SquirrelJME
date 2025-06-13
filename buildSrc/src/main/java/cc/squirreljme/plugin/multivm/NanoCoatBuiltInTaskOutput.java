// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import org.gradle.api.provider.Provider;

/**
 * Task output names for ROMs.
 *
 * @since 2024/10/12
 */
public class NanoCoatBuiltInTaskOutput
	implements Callable<Path>
{
	/** The base path. */
	protected final Provider<Path> base;
	
	/** Is this the source for this? */
	protected final boolean isSource;
	
	/** Is this for tests? */
	protected final boolean isTest;
	
	/** The classifier used. */
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the output.
	 *
	 * @param __classifier The classifier to use.
	 * @param __base The base path.
	 * @param __source Is this the source for it?
	 * @param __test Is this for tests?
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/12
	 */
	public NanoCoatBuiltInTaskOutput(SourceTargetClassifier __classifier,
		Provider<Path> __base, boolean __source, boolean __test)
		throws NullPointerException
	{
		if (__classifier == null || __base == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
		this.base = __base;
		this.isSource = __source;
		this.isTest = __test;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/10/12
	 */
	@Override
	public Path call()
		throws Exception
	{
		Path result = this.jar();
		
		// Use source?
		if (this.isSource)
			return result.resolveSibling(result.getFileName().toString()
				.replace(".jar", ".c"));
		
		// Is test?
		else if (this.isTest)
			return result.resolveSibling(result.getFileName().toString()
				.replace(".jar", ".list"));
		
		// Just use normal Jar
		return result;
	}
	
	/**
	 * Returns the Jar name.
	 * 
	 * @return The Jar name.
	 * @since 2024/10/12
	 */
	public Path jar()
	{
		Path base = this.base.get();
		
		// Is fast or slow main code?
		SourceTargetClassifier classifier = this.classifier;
		if (classifier.isMainSourceSet())
			if (classifier.getTargetClassifier().getClutterLevel().isDebug())
				return base.resolve("squirreljme-slow.jar");
			else
				return base.resolve("squirreljme.jar");
			
		// With tests?
		if (classifier.isTestSourceSet())
			if (classifier.getTargetClassifier().getClutterLevel().isDebug())
				return base.resolve("squirreljme-slow-test.jar");
			else
				return base.resolve("squirreljme-test.jar");
		
		// Unknown otherwise
		return base.resolve("squirreljme-unknown.jar");
	}
}
