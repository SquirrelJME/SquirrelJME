// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.HasMemberFlags;
import net.multiphasicapps.classfile.MemberNameAndType;

/**
 * This represents a member.
 *
 * @since 2018/09/09
 */
public interface SpringMember
	extends HasMemberFlags
{
	/**
	 * Returns the class this is a member of.
	 *
	 * @return The class which owns the method.
	 * @since 2018/09/09
	 */
	ClassName inClass();
	
	/**
	 * Returns the name and type of the member.
	 *
	 * @return The member name and type.
	 * @since 2018/09/09
	 */
	MemberNameAndType nameAndType();
}

