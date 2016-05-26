// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launcher;

import net.multiphasicapps.squirreljme.classpath.ClassPath;
import net.multiphasicapps.squirreljme.classpath.ClassUnit;
import net.multiphasicapps.squirreljme.classpath.ClassUnitProvider;
import net.multiphasicapps.squirreljme.ui.UILabel;
import net.multiphasicapps.squirreljme.ui.UIList;
import net.multiphasicapps.squirreljme.ui.UIManager;

/**
 * This provides an interface which has the ability to glean information
 * from a given class unit and the requirements for it to be launched.
 *
 * @since 2016/05/25
 */
public class ClassUnitInterface
{
	/** The used class unit. */
	protected final ClassUnit unit;
	
	/** The owning launcher. */
	protected final LauncherInterface launcher;
	
	/** The display label for this interface. */
	protected final UILabel label;
	
	/**
	 * Initializes a class unit interface.
	 *
	 * @param __li The owning launcher interface.
	 * @param __cu The class unit.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/25
	 */
	public ClassUnitInterface(LauncherInterface __li, ClassUnit __cu)
		throws NullPointerException
	{
		// Check
		if (__li == null || __cu == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.launcher = __li;
		this.unit = __cu;
		
		// Create label
		this.label = __li.manager().createLabel();
	}
	
	/**
	 * Returns the associated label for this class unit.
	 *
	 * @return The class unit label.
	 * @since 2016/05/25
	 */
	public UILabel label()
	{
		return this.label;
	}
}

