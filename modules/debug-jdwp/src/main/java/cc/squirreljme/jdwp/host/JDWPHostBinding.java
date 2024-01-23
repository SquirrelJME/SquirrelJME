// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.host.views.JDWPView;
import cc.squirreljme.jdwp.host.views.JDWPViewKind;
import java.lang.ref.Reference;

/**
 * This interface is used by anything that JDWP needs to communicate with,
 * other virtual machines or otherwise to perform actions.
 *
 * @since 2021/03/08
 */
public interface JDWPHostBinding
{
	/**
	 * Returns the libraries for the debugger.
	 * 
	 * @return The debugger libraries.
	 * @since 2021/03/14
	 */
	String[] debuggerLibraries();
	
	/**
	 * Returns the thread groups that are available.
	 * 
	 * @return The thread groups for the debugger.
	 * @since 2021/04/10
	 */
	Object[] debuggerThreadGroups();
	
	/**
	 * Returns the view of the given type.
	 * 
	 * @param <V> The type to view.
	 * @param __type The type to view.
	 * @param __kind The kind of viewer to use.
	 * @param __state The debugging state to link to.
	 * @return The view for the given type.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/10
	 */
	<V extends JDWPView> V debuggerView(Class<V> __type, JDWPViewKind __kind,
		Reference<JDWPHostState> __state)
		throws NullPointerException;
	
	/**
	 * Returns the virtual machine description.
	 * 
	 * @return The virtual machine description.
	 * @since 2021/03/13
	 */
	String vmDescription();
	
	/**
	 * The virtual machine name, as {@code java.vm.name}.
	 * 
	 * @return The virtual machine name, as {@code java.vm.name}.
	 * @since 2021/03/13
	 */
	String vmName();
	
	/**
	 * The virtual machine version, as {@code java.vm.version}.
	 * 
	 * @return The virtual machine version, as {@code java.vm.version}.
	 * @since 2021/03/13
	 */
	String vmVersion();
}
