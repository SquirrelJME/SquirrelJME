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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.multiphasicapps.imagereader.ImageData;
import net.multiphasicapps.squirreljme.classpath.ClassUnit;
import net.multiphasicapps.squirreljme.classpath.ClassUnitProvider;
import net.multiphasicapps.squirreljme.ui.UIImage;
import net.multiphasicapps.squirreljme.ui.UIList;
import net.multiphasicapps.squirreljme.ui.UIListData;
import net.multiphasicapps.squirreljme.ui.UIManager;

/**
 * This manages the list of programs which are available for launching in
 * SquirrelJME.
 *
 * @since 2016/05/26
 */
class __ClassUnitList__
{
	/** The launcher to use. */
	protected final LauncherInterface launcher;
	
	/** The manager to use. */
	protected final UIManager displaymanager;
	
	/** The list containing the programs. */
	protected final UIList list;
	
	/** Items in the program list. */
	protected final ProgramListData listdata;
	
	/** The images used for the types that programs may be. */
	protected final UIImage[] typeicons;
	
	/**
	 * Initializes the class unit list.
	 *
	 * @param __li The owning launcher.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/26
	 */
	__ClassUnitList__(LauncherInterface __li)
		throws NullPointerException
	{
		// Check
		if (__li == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.launcher = __li;
		this.listdata = new ProgramListData(__li, this);
		UIManager dm = __li.manager();
		this.displaymanager = dm;
		
		// Create new list
		this.list = dm.createList(ClassUnit.class, listdata);
		
		// Load type icons
		ClassUnit.Type[] ts = ClassUnit.Type.values();
		int n = ts.length;
		UIImage[] typeicons = new UIImage[n];
		this.typeicons = typeicons;
		for (ClassUnit.Type t : ClassUnit.Type.values())
		{
			UIImage ii = dm.createImage();
			
			// Determine the name
			String name;
			switch (t)
			{
					// Console application
				case CONSOLE:
					name = "type_console.xpm";
					break;
					
					// Liblet
				case LIBRARY:
					name = "type_library.xpm";
					break;
					
					// Multiple
				case MULTIPLE:
					name = "type_multi.xpm";
					break;
							
					// Unknown
				default:
					name = "type_unknown.xpm";
					break;
			}
			
			// Load and associate
			ImageData id = __li.loadImage(name);
			if (id != null)
				ii.addImageData(id);
			
			// Store it
			typeicons[t.ordinal()] = ii;
		}
	}
	
	/**
	 * Returns the used UIList.
	 *
	 * @return The list containing the programs to run.
	 * @since 2016/05/26
	 */
	public UIList getUIList()
	{
		return this.list;
	}
	
	/**
	 * Returns the icon which is used for the given program type.
	 *
	 * @param __t The type of program to use.
	 * @return The icon for the given program type.
	 * @since 2016/05/26
	 */
	public UIImage iconForType(ClassUnit.Type __t)
	{
		// Make it always valid.
		if (__t == null)
			__t = ClassUnit.Type.UNKNOWN;
			
		// Return it
		return this.typeicons[__t.ordinal()];
	}
	
	/**
	 * Refreshes the program list to display all the class units that are
	 * potentially available for execution.
	 *
	 * @since 2016/05/25
	 */
	public void refresh()
	{
		// Lock
		ProgramListData listdata = this.listdata;
		synchronized (listdata)
		{
			// Get the current list size
			int n = listdata.size();
			
			// Temporary list used for sorting
			List<ClassUnit> st = new ArrayList<>();
			
			// Go through class units
			ClassUnitProvider[] cups = this.launcher.__cups();
			for (ClassUnitProvider cup : cups)
				for (ClassUnit cu : cup.classUnits())
					st.add(cu);
			
			// Sort it
			Collections.sort(st);
			
			// Add entries to the program list data
			int at;
			int sn = st.size();
			for (at = 0; at < sn; at++)
				if (at < n)
					listdata.set(at, st.get(at));
				else
					listdata.add(st.get(at));
			
			// Remove extra items at the end
			while (at < n)
			{
				listdata.remove(n - 1);
				n--;
			}
		}
	}
}

