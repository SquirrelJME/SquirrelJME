// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.views;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * Represents the kind of view that is used for an object or otherwise.
 *
 * @since 2021/04/10
 */
@Exported
public enum JDWPViewKind
{
	/** Thread frames. */
	@Exported
	FRAME(JDWPViewFrame.class),
	
	/** An object. */
	@Exported
	OBJECT(JDWPViewObject.class),
	
	/** Thread. */
	@Exported
	THREAD(JDWPViewThread.class),
	
	/** A group of threads. */
	@Exported
	THREAD_GROUP(JDWPViewThreadGroup.class),
	
	/** A type such as a class. */
	@Exported
	TYPE(JDWPViewType.class),
	
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
