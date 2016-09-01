// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

/**
 * This interface is associated with single registers.
 *
 * @since 2016/08/31
 */
public interface GenericRegister
{
	/**
	 * Returns the type of float that can be stored in this register.
	 *
	 * @return Either {@link JITVariableType#FLOAT},
	 * {@link JITVariableType#DOUBLE}, or {@code null} if a float is not
	 * stored in this register.
	 * @since 2016/08/31
	 */
	public abstract GenericRegisterType floatType();
	
	/**
	 * Returns the type of integer that can be stored in this register.
	 *
	 * @return Either {@link JITVariableType#INTEGER},
	 * {@link JITVariableType#LONG}, or {@code null} if an integer is not
	 * stored in this register.
	 * @since 2016/08/31
	 */
	public abstract GenericRegisterType intType();
}

