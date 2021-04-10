// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Represents the kind of view that is used for an object or otherwise.
 *
 * @since 2021/04/10
 */
public enum JDWPViewKind
{
	/** An object. */
	OBJECT(JDWPViewObject.class),
	
	/* End. */
	;
	
	/** The viewing class. */
	public final Class<? extends JDWPView> viewClass;
	
	/**
	 * Initializes the view kind.
	 * 
	 * @param __viewClass The view class.
	 * @since 2021/04/10
	 */
	JDWPViewKind(Class<? extends JDWPView> __viewClass)
	{
		this.viewClass = __viewClass;
	}
}
