// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * Represents the name of a member.
 *
 * @since 2018/09/09
 */
public interface MemberName
	extends Contexual
{
	/**
	 * Returns the identifier.
	 *
	 * @return The identifier.
	 * @since 2018/09/09
	 */
	String identifier();
}

