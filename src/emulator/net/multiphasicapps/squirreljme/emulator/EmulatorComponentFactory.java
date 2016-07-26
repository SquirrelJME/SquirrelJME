// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

/**
 * This is a factory which is exposed by the {@link java.util.ServiceLoader}
 * and is used to initialize new components to be added to systems when
 * requested.
 *
 * @since 2016/07/26
 */
public interface EmulatorComponentFactory
{
	/**
	 * Checks if the given component is handled by this creator factory.
	 *
	 * @param __cl The class to check.
	 * @return {@code true} if the component is handled by this factory.
	 * @throws NullPointerException On null argumnets.
	 * @since 2016/07/26
	 */
	public abstract boolean isComponentHandled(
		Class<? extends EmulatorComponent> __cl)
		throws NullPointerException;
	
	/**
	 * Creates the given component.
	 *
	 * @param <C> The class type of the component.
	 * @param __cl The class of the given component.
	 * @param __es The owning system.
	 * @param __id The identifer of the component.
	 * @param __args The arguments to the component.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/26
	 */
	public abstract <C extends EmulatorComponent> C createComponent(
		Class<C> __cl, EmulatorSystem __es, String __id, String... __args)
		throws NullPointerException;
}

