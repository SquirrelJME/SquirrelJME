// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

/**
 * An operator within C.
 *
 * @since 2023/07/03
 */
public interface COperator
{
	/**
	 * The token used for the operator.
	 * 
	 * @return The operator token.
	 * @since 2023/07/03
	 */
	String token();
}
