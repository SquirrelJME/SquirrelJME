// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.util.Collection;
import java.util.Map;
import org.gradle.process.ExecSpec;

/**
 * Fills Gradle's {@link ExecSpec} which just takes plain command line
 * arguments.
 *
 * @since 2025/02/14
 */
public class GradleExecSpecFiller
	extends SimpleJavaExecSpecFiller
{
	/** The spec to fill. */
	protected final ExecSpec spec;
	
	/**
	 * The Gradle execution specifier to set into.
	 *
	 * @param __spec The Gradle execution specifier to set into
	 * @throws NullPointerException On null arguments.
	 * @since 2025/02/14
	 */
	public GradleExecSpecFiller(ExecSpec __spec)
		throws NullPointerException
	{
		if (__spec == null)
			throw new NullPointerException("NARG");
		
		this.spec = __spec;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/02/14
	 */
	@Override
	public void classpath(Collection<Object> __classPath)
	{
		super.classpath(__classPath);
		
		this.spec.args(this.getCommandLine());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/02/14
	 */
	@Override
	public void setMain(String __mainClass)
	{
		super.setMain(__mainClass);
		
		this.spec.args(this.getCommandLine());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/02/14
	 */
	@Override
	public void setArgs(Collection<String> __args)
	{
		super.setArgs(__args);
		
		this.spec.args(this.getCommandLine());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/02/14
	 */
	@Override
	public void setJvmArgs(Collection<String> __args)
	{
		super.setJvmArgs(__args);
		
		this.spec.args(this.getCommandLine());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/02/14
	 */
	@Override
	public void systemProperties(Map<String, String> __sysProps)
	{
		super.systemProperties(__sysProps);
		
		this.spec.args(this.getCommandLine());
	}
}
