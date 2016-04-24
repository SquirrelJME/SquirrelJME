// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

/**
 * This refers to another method.
 *
 * @since 2016/04/24
 */
public final class NCIMethodReference
	extends NCIMemberReference
{
	/** Does this refer to an interface? */
	protected final boolean isinterface;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public NCIPoolTag tag()
	{
		return (isinterface ? NCIPoolTag.INTERFACEMETHODREF :
			NCIPoolTag.METHODREF);
	}
}

