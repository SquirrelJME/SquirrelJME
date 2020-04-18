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
import java.io.IOException;
import java.util.Enumeration;

public class LandmarkStore
{
	public void addCategory(String var1)
		throws LandmarkException, IOException
	{
		throw Debugging.todo();
	}
	
	public void addLandmark(Landmark var1, String var2)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	public void deleteCategory(String var1)
		throws LandmarkException, IOException
	{
		throw Debugging.todo();
	}
	
	public void deleteLandmark(Landmark var1)
		throws LandmarkException, IOException
	{
		throw Debugging.todo();
	}
	
	public Enumeration getCategories()
	{
		throw Debugging.todo();
	}
	
	public Enumeration getLandmarks(String var1, String var2)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	public Enumeration getLandmarks()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	public Enumeration getLandmarks(String var1, double var2, double var4,
		double var6, double var8)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	public void removeLandmarkFromCategory(Landmark var1, String var2)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
	
	public void updateLandmark(Landmark var1)
		throws LandmarkException, IOException
	{
		throw Debugging.todo();
	}
	
	public static void createLandmarkStore(String var0)
		throws IOException, LandmarkException
	{
		throw Debugging.todo();
	}
	
	public static void deleteLandmarkStore(String var0)
		throws IOException, LandmarkException
	{
		throw Debugging.todo();
	}
	
	public static LandmarkStore getInstance(String var0)
	{
		throw Debugging.todo();
	}
	
	public static String[] listLandmarkStores()
		throws IOException
	{
		throw Debugging.todo();
	}
}
