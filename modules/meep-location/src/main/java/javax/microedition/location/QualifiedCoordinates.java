// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.location;

import cc.squirreljme.runtime.cldc.debug.Debugging;

public class QualifiedCoordinates
	extends Coordinates
{
	public QualifiedCoordinates(double var1, double var3, float var5,
		float var6, float var7)
	{
		super(0.0D, 0.0D, 0.0F);
		
		throw Debugging.todo();
	}
	
	public float getHorizontalAccuracy()
	{
		throw Debugging.todo();
	}
	
	public float getVerticalAccuracy()
	{
		throw Debugging.todo();
	}
	
	public void setHorizontalAccuracy(float var1)
	{
		throw Debugging.todo();
	}
	
	public void setVerticalAccuracy(float var1)
	{
		throw Debugging.todo();
	}
	
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
}
