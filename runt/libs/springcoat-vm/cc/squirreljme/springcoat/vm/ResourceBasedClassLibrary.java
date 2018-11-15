// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

/**
 * This is a class library resource which is based on class resources.
 *
 * @since 2018/11/14
 */
public final class ResourceBasedClassLibrary
	implements SpringClassLibrary
{
	/**
	 * {@inheritDoc}
	 * @since 2018/11/14
	 */
	@Override
	public final String name()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/14
	 */
	@Override
	public final InputStream resourceAsStream(String __rc)
		throws IOException, NullPointerException
	{
		if (__rc == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

