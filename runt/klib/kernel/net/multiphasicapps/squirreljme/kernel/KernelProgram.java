// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemProgramType;
import net.multiphasicapps.squirreljme.runtime.midlet.id.SuiteInfo;
import net.multiphasicapps.squirreljme.runtime.midlet.id.SuiteType;
import net.multiphasicapps.tool.manifest.JavaManifest;

/**
 * This represents a single program which exists within the kernel and maps
 * with suites within the Java ME environment. Each program is identified by
 * an identifier which represents the program index. The index remains constant
 * for the same program (unless that program has been changed). The index is
 * used to refer to the program slot.
 *
 * @since 2017/12/11
 */
public abstract class KernelProgram
{
	/** Locking object. */
	protected final Object lock =
		new Object();
	
	/** The index of the program. */
	protected final int index;
	
	/** Information about the suite. */
	private volatile Reference<SuiteInfo> _info;
	
	/** The manifest for this suite. */
	private volatile Reference<JavaManifest> _manifest;
	
	/** Cache for the type this program is. */
	private volatile int _type;
	
	/**
	 * Initializes the base program.
	 *
	 * @param __dx The index of the program, the slot it is in.
	 * @since 2017/12/25
	 */
	protected KernelProgram(int __dx)
	{
		this.index = __dx;
	}
	
	/**
	 * Returns a value from the control manifest.
	 *
	 * @param __k The control key.
	 * @return The value of the control key or {@code null} if it has not been
	 * set.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	protected abstract String accessControlGet(String __k)
		throws NullPointerException;
	
	/**
	 * Sets a new value for the given control key.
	 *
	 * @param __k The key to set.
	 * @param __v The value to set, if {@code null} then it is removed.
	 * @throws NullPointerException If no key was specified.
	 * @since 2017/12/31
	 */
	protected abstract void accessControlSet(String __k, String __v)
		throws NullPointerException;
	
	/**
	 * Returns an input stream which can be used to read the given resource
	 * data.
	 *
	 * @param __name The name of the resource to load.
	 * @return An input stream over the resource data or {@code null} if it
	 * does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/30
	 */
	protected abstract InputStream accessLoadResource(String __name)
		throws NullPointerException;
	
	/**
	 * Returns a value from the control manifest for the given key.
	 *
	 * @param __by The task requesting the value.
	 * @param __k The value to read.
	 * @return The value of the control key.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the task is not permitted to read control
	 * values.
	 * @since 2017/12/31
	 */
	public final String controlGet(KernelTask __by, String __k)
		throws NullPointerException, SecurityException
	{
		if (__by == null || __k == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AP06 The specified task is not permitted to
		// get a control value. (The task requesting the value)}
		if (!__by.hasSimplePermissions(__by,
			KernelSimplePermission.GET_PROGRAM_CONTROL))
			throw new SecurityException(
				String.format("AP06 %s", __by));
		
		return this.accessControlGet(__k);
	}
	
	/**
	 * Sets a new value for the given control key.
	 *
	 * @param __by The task setting the value.
	 * @param __k The key to set.
	 * @param __v The value to set, if {@code null} then it is removed.
	 * @throws NullPointerException If no task or key was specified.
	 * @since 2017/12/31
	 */
	public final void controlSet(KernelTask __by, String __k, String __v)
		throws NullPointerException, SecurityException
	{
		if (__by == null || __k == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AP08 The specified task is not permitted to
		// set a control value. (The task requesting the value)}
		if (!__by.hasSimplePermissions(__by,
			KernelSimplePermission.SET_PROGRAM_CONTROL))
			throw new SecurityException(
				String.format("AP08 %s", __by));
		
		this.accessControlSet(__k, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	public final boolean equals(Object __o)
	{
		return this == __o;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	public final int hashCode()
	{
		return this.index;
	}
	
	/**
	 * This loads a resource from this program.
	 *
	 * @param __by The task which wants to load the resource.
	 * @param __name The name of the resource to load.
	 * @return An input stream which will return the resource data or
	 * {@code null} if the specified resource does not exist.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the task cannot load resources from
	 * programs.
	 * @since 2017/12/20
	 */
	public final InputStream loadResource(KernelTask __by, String __name)
		throws NullPointerException, SecurityException
	{
		if (__by == null || __name == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AP01 The specified task is not permitted to
		// load a program resource. (The task requesting the resource)}
		if (!__by.hasSimplePermissions(__by,
			KernelSimplePermission.GET_PROGRAM_PROPERTY))
			throw new SecurityException(
				String.format("AP01 %s", __by));
		
		return accessLoadResource(__name);
	}
	
	/**
	 * Returns the program index.
	 *
	 * @return The program index.
	 * @since 2017/12/25
	 */
	public final int index()
	{
		return this.index;
	}
	
	/**
	 * This returns the manifest for the program which will contain the
	 * information about it.
	 *
	 * @return The manifest for this program.
	 * @since 2017/12/30
	 */
	public final JavaManifest manifest()
	{
		Reference<JavaManifest> ref = this._manifest;
		JavaManifest rv;
		
		if (ref == null || null == (rv = ref.get()))
			try (InputStream in = this.accessLoadResource(
				"META-INF/MANIFEST.MF"))
			{
				// {@squirreljme.error AP03 A program with no manifest
				// resource exists within the kernel.}
				if (in == null)
					throw new RuntimeException("AP03");
				
				this._manifest = new WeakReference<>(
					(rv = new JavaManifest(in)));
			}
			
			// {@squirreljme.error AP02 Could not read the program manifest.}
			catch (IOException e)
			{
				throw new RuntimeException("AP02", e);
			}
		
		return rv;
	}
	
	/**
	 * This returns information about the program suite such as its
	 * dependencies and such.
	 *
	 * @return The suite information.
	 * @since 2017/12/30
	 */
	public final SuiteInfo suiteInfo()
	{
		Reference<SuiteInfo> ref = this._info;
		SuiteInfo rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._info = new WeakReference<>(
				(rv = new SuiteInfo(this.manifest())));
		
		return rv;
	}
	
	/**
	 * Returns the type of this program.
	 *
	 * @param __by The task requesting the program type.
	 * @return The type of program this is.
	 * @throws SecurityException If the task cannot obtain the program type.
	 * @since 2017/12/27
	 */
	public final int type(KernelTask __by)
		throws NullPointerException, SecurityException
	{
		if (__by == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0i The specified task is not permitted to
		// obtain the program type. (The task requesting the program list)}
		if (!__by.hasSimplePermissions(__by,
			KernelSimplePermission.GET_PROGRAM_PROPERTY))
			throw new SecurityException(
				String.format("ZZ0i %s", __by));
		
		// The zero index is always the system program
		if (this.index == 0)
			return SystemProgramType.SYSTEM;
		
		// Use cached value to prevent manifest read
		int rv = this._type;
		if (rv != 0)
			return rv;
		
		// Otherwise it depends on the JAR itself
		SuiteInfo info = this.suiteInfo();
		this._type = (rv = (info.type() == SuiteType.MIDLET ?
			SystemProgramType.APPLICATION : SystemProgramType.LIBRARY));
		return rv;
	}
	
	/**
	 * This method allows classes to get the value of control codes without
	 * performing access checks.
	 *
	 * @param __k The key to get.
	 * @return The value of the given key or {@code null} if it is not set.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	final String __controlGet(String __k)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		return this.accessControlGet(__k);
	}
	
	/**
	 * This method allows classes to get the control codes set without
	 * having access checks be performed.
	 *
	 * @param __k The key to set.
	 * @param __v The value to set, {@code null} clears it.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	final void __controlSet(String __k, String __v)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		this.accessControlSet(__k, __v);
	}
}

