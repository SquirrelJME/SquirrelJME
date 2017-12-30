// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.kernel;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.runtime.midlet.id.SuiteInfo;
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
	/** The index of the program. */
	protected final int index;
	
	/** Information about the suite. */
	private volatile Reference<SuiteInfo> _info;
	
	/** The manifest for this suite. */
	private volatile Reference<JavaManifest> _manifest;
	
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
	 * Returns the type of program this is.
	 *
	 * @return The program type.
	 * @since 2017/12/27
	 */
	protected abstract int accessType();
	
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
			try
			{
				this._manifest = new WeakReference<>(
					(rv = new JavaManifest(
						this.accessLoadResource("META-INF/MANIFEST.MF"))));
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
		
		return this.accessType();
	}
}

