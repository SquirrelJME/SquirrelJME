// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.deviceinfo;

import java.lang.ref.Reference;
import java.util.Objects;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;

/**
 * Displays fields for a specific group.
 *
 * @since 2025/12/06
 */
public class FieldList
	extends List
{
	/**
	 * Initializes the viewer for field data.
	 *
	 * @param __title The group title.
	 * @param __fields The fields to obtain.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/06
	 */
	public FieldList(String __title, SpecificField... __fields)
		throws NullPointerException
	{
		super(__title, Choice.IMPLICIT);
		
		if (__fields == null)
			throw new NullPointerException("NARG");
		
		// Add all field values from the group
		for (SpecificField field : __fields)
		{
			// Must be valid
			if (field == null)
				throw new NullPointerException("NARG");
			
			// Make sure both key and value are valid!
			String key = "Unknown";
			String val = "Unknown";
			try
			{
				key = field.key();
				val = field.value();
			}
			catch (Throwable __t)
			{
				__t.printStackTrace();
			}
			
			// Place two items on the list for this, makes it far easier to
			// read
			this.append("> " + key, null);
			this.append(Objects.toString(val), null);
		}
	}
}
