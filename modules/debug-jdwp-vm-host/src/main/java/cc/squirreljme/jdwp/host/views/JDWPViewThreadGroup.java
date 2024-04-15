// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host.views;

/**
 * A view of thread groups.
 *
 * @since 2021/04/10
 */
public interface JDWPViewThreadGroup
	extends JDWPViewHasInstance, JDWPViewValidObject
{
	/**
	 * Returns all the types that are available.
	 * 
	 * @param __which Which thread group?
	 * @return All of the types that are known by this thread group.
	 * @since 2021/04/25
	 */
	Object[] allTypes(Object __which);
	
	/**
	 * Forces the virtual machine to exit completely.
	 *
	 * @param __which Which thread group to exit?
	 * @param __code The exit code.
	 * @since 2021/04/30
	 */
	void exit(Object __which, int __code);
	
	/**
	 * Finds a type of the given name from this thread group.
	 * 
	 * @param __which Which thread group?
	 * @param __name The name of the type to find.
	 * @return The found type.
	 * @since 2021/04/18
	 */
	Object findType(Object __which, String __name);
	
	/**
	 * Returns the instance object for this thread group.
	 * 
	 * @param __threadGroup The thread group.
	 * @return The instance object of this group.
	 * @since 2022/09/24
	 */
	@Override
	Object instance(Object __threadGroup);
	
	/**
	 * Returns the name of the thread group.
	 * 
	 * @param __which Which thread group to get the name of?
	 * @return The name of the group.
	 * @since 2021/04/10
	 */
	String name(Object __which);
	
	/**
	 * Returns the threads which are a part of this group.
	 * 
	 * @param __which The object being referred to as a thread group.
	 * @return The threads that are part of this thread group.
	 * @since 2021/04/10
	 */
	Object[] threads(Object __which);
}
