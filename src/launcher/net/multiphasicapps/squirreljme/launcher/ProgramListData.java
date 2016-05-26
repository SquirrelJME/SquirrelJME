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

import net.multiphasicapps.squirreljme.classpath.ClassUnit;
import net.multiphasicapps.squirreljme.ui.UIException;
import net.multiphasicapps.squirreljme.ui.UIImage;
import net.multiphasicapps.squirreljme.ui.UIList;
import net.multiphasicapps.squirreljme.ui.UIListData;

/**
 * This is the list which is associated with the {@link UIList} containing the
 * applications and libraries which are listed on the main program list.
 *
 * @since 2016/05/26
 */
public class ProgramListData
	extends UIListData<ClassUnit>
{
	/** The launcher which owns this list. */
	protected final LauncherInterface launcher;
	
	/** The class unit list interface. */
	protected final __ClassUnitList__ unitlist;
	
	/**
	 * Initialize the program list data.
	 *
	 * @param __li The launcher which owns this list.
	 * @param __cul The class unit list.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/26
	 */
	public ProgramListData(LauncherInterface __li, __ClassUnitList__ __cul)
		throws NullPointerException
	{
		super(ClassUnit.class);
		
		// Check
		if (__li == null || __cul == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.launcher = __li;
		this.unitlist = __cul;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/26
	 */
	@Override
	public UIImage generateIcon(int __dx, ClassUnit __v)
		throws UIException
	{
		return this.unitlist.iconForType(ClassUnit.Type.UNKNOWN);
	}
}

