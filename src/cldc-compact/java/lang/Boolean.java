// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.lang;

public final class Boolean
	implements Comparable<Boolean>
{
	public static final Boolean FALSE =
		new Boolean(false);
	
	public static final Boolean TRUE =
		new Boolean(true);
	
	public static final Class<Boolean> TYPE =
		__getType();
	
	public Boolean(boolean __a)
	{
		super();
		throw new Error("TODO");
	}
	
	public Boolean(String __a)
	{
		super();
		throw new Error("TODO");
	}
	
	public boolean booleanValue()
	{
		throw new Error("TODO");
	}
	
	public int compareTo(Boolean __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new Error("TODO");
	}
	
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
	
	public static boolean getBoolean(String __a)
	{
		throw new Error("TODO");
	}
	
	public static boolean parseBoolean(String __a)
	{
		return (__a != null && __a.equalsIgnoreCase("true"));
	}
	
	public static String toString(boolean __a)
	{
		if (__a)
			return "true";
		return "false";
	}
	
	/**
	 * Boxes the given boolean value.
	 *
	 * @param __a The boolean to box.
	 * @return Either {@link #TRUE} or {@link #FALSE}.
	 * @since 2016/03/21
	 */
	public static Boolean valueOf(boolean __a)
	{
		if (__a)
			return TRUE;
		return FALSE;
	}
	
	public static Boolean valueOf(String __a)
	{
		if (__a != null && __a.equalsIgnoreCase("true"))
			return TRUE;
		return FALSE;
	}
	
	/**
	 * The {@link #TYPE} field is magically initialized by the virtual machine.
	 *
	 * @return {@link #TYPE}.
	 * @since 2016/06/16
	 */
	private static Class<Boolean> __getType()
	{
		return TYPE;
	}
}

