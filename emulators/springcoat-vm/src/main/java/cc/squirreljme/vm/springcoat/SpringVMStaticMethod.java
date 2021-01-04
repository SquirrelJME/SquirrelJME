// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

/**
 * This is a virtual machine representation of a static method.
 *
 * @since 2018/11/20
 */
@Deprecated
public final class SpringVMStaticMethod
	extends AbstractGhostObject
{
	/** The method to execute. */
	@Deprecated
	protected final SpringMethod method;
	
	/**
	 * Initializes the static method.
	 *
	 * @param __m The method to execute.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/20
	 */
	@Deprecated
	public SpringVMStaticMethod(SpringMethod __m)
		throws NullPointerException
	{
		super(SpringVMStaticMethod.class);
		
		if (__m == null)
			throw new NullPointerException("NARG");
		
		this.method = __m;
	}
}

