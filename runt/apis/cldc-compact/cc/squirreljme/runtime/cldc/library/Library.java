// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.library;

import cc.squirreljme.runtime.cldc.task.SystemTaskLaunchable;
import cc.squirreljme.runtime.cldc.trust.SystemTrustGroup;
import java.io.InputStream;

/**
 * This represents a single program which exists within the kernel and maps
 * with suites within the Java ME environment. Each program is identified by
 * an identifier which represents the program index. The index remains constant
 * for the same program (unless that program has been changed). The index is
 * used to refer to the program slot.
 *
 * Instances of this class will be used as keys so it must implement
 * {@link #equals(Object)} and {@link #hashCode()} to where even two
 * instances of this class which point to the same program refer to that
 * instance. The comparison as such is performed on the index.
 *
 * @since 2017/12/11
 */
public interface Library
	extends SystemTaskLaunchable
{
	/**
	 * Returns the value of the given control key.
	 *
	 * @param __k The control key.
	 * @return The value of the key or {@code null} if it is not set.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/02
	 */
	public abstract String controlGet(LibraryControlKey __k)
		throws NullPointerException;
	
	/**
	 * Sets the value of the given control key.
	 *
	 * @param __k The control key.
	 * @param __v The new value to set, {@code null} clears it.
	 * @throws NullPointerException If no key was specified.
	 * @since 2018/01/02
	 */
	public abstract void controlSet(LibraryControlKey __k, String __v)
		throws NullPointerException;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Override
	public abstract boolean equals(Object __o);
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Override
	public abstract int hashCode();
	
	/**
	 * Returns the index of the library.
	 *
	 * @return The library index.
	 * @since 2018/03/03
	 */
	public abstract int index();
	
	/**
	 * Loads the specified resource in the given scope.
	 *
	 * @param __scope The scope to load the resource from.
	 * @param __name The name of the resource to load.
	 * @return The resource with the given name under the given scope or
	 * {@code null} if it does not exist.
	 * @since 2018/02/11
	 */
	public abstract InputStream loadResource(LibraryResourceScope __scope,
		String __name)
		throws NullPointerException;
	
	/**
	 * Returns the library type.
	 *
	 * @return The library type.
	 * @since 2018/01/02
	 */
	public abstract LibraryType type();
}

