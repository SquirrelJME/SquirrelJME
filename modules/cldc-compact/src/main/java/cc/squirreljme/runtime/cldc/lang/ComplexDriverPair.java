// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

/**
 * Represents a complex driver pair which is used to find complex drivers.
 *
 * @since 2021/08/05
 */
public final class ComplexDriverPair
{
	/** The factory class. */
	public final String factory;
	
	/** The implementation. */
	public final String implementation;
	
	/**
	 * Initializes the complex driver pair reference.
	 * 
	 * @param __factory The factory class.
	 * @param __implementation The implementation class.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/08/05
	 */
	public ComplexDriverPair(String __factory, String __implementation)
		throws NullPointerException
	{
		if (__factory == null || __implementation == null)
			throw new NullPointerException("NARG");
		
		this.factory = __factory;
		this.implementation = __implementation;
	}
}
