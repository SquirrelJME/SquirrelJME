// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm;

import java.nio.file.Paths;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.ci.CIMethodID;
import net.multiphasicapps.squirreljme.classpath.ClassPath;
import net.multiphasicapps.squirreljme.classpath.ClassUnitProvider;
import net.multiphasicapps.squirreljme.classpath.jar.fs.FSJarClassUnitProvider;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelException;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;
import net.multiphasicapps.squirreljme.kernel.KernelThread;
import net.multiphasicapps.squirreljme.terp.Interpreter;

/**
 * This is the kernel which runs on an existing JVM.
 *
 * @since 2016/05/27
 */
public class JVMKernel
	extends Kernel
{
	/** The JVM locates JAR files using a single provider. */
	private volatile ClassUnitProvider _cuprovider;
	
	/**
	 * Initializes the kernel.
	 *
	 * @param __terp The interpreter backend to use for execution.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/27
	 */
	public JVMKernel(Interpreter __terp, String... __args)
		throws NullPointerException
	{
		super(new InterpreterExecutionEngine(__terp), __args);
		
		// Check
		if (__terp == null)
			throw new NullPointerException("NARG");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/30
	 */
	@Override
	protected ClassUnitProvider[] internalClassUnitProviders()
		throws KernelException
	{
		// Need to load the class unit provider?
		ClassUnitProvider use;
		synchronized (this.lock)
		{
			use = this._cuprovider;
			
			// Load it?
			if (use == null)
				this._cuprovider = use = new FSJarClassUnitProvider(
					Paths.get(System.getProperty("user.dir")));
		}
		
		// There is only a single provider, which is the current directory on
		// the filesystem
		return new ClassUnitProvider[]{use};
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	protected KernelProcess internalCreateProcess(ClassPath __cp)
		throws KernelException
	{
		// Lock
		synchronized (this.lock)
		{
			// Create new process
			return new JVMKernelProcess(this, __cp);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	protected KernelThread internalCreateThread(KernelProcess __proc,
		ClassNameSymbol __mc, CIMethodID __mm, Object... __args)
		throws KernelException
	{
		// Lock
		synchronized (this.lock)
		{
			return new JVMKernelThread(this, (JVMKernelProcess)__proc, __mc,
				__mm, __args);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	protected KernelThread internalCurrentThread()
		throws KernelException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	protected void internalRunCycle()
		throws KernelException
	{
		// Run a single interpreter cycle
		interpreter().runCycle();
	}
	
	/**
	 * Returns the interpreter being used.
	 *
	 * @return The interpreter to use.
	 * @since 2016/05/30
	 */
	protected Interpreter interpreter()
	{
		return ((InterpreterExecutionEngine)this.executioncore).interpreter();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/27
	 */
	@Override
	public void quitKernel()
	{
		System.exit(0);
	}
	
	/**
	 * Returns the internal class unit providers which are needed by the
	 * interpreter to find alternative class units if they do need to be
	 * adjusted.
	 *
	 * @return The array of class unit providers.
	 * @since 2016/06/03
	 */
	final ClassUnitProvider[] __internalClassUnitProviders()
	{
		return internalClassUnitProviders();
	}
}

