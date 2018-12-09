// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;


public interface Choice
{
	public static final int EXCLUSIVE =
		1;
	
	public static final int IMPLICIT =
		3;
	
	public static final int MULTIPLE =
		2;
	
	public static final int POPUP =
		4;
	
	public static final int TEXT_WRAP_DEFAULT =
		0;
	
	public static final int TEXT_WRAP_OFF =
		2;
	
	public static final int TEXT_WRAP_ON =
		1;
	
	public abstract int append(String __a, Image __b);
	
	public abstract void delete(int __a);
	
	public abstract void deleteAll();
	
	public abstract int getFitPolicy();
	
	public abstract Font getFont(int __a);
	
	public abstract Image getImage(int __a);
	
	public abstract int getSelectedFlags(boolean[] __a);
	
	public abstract int getSelectedIndex();
	
	public abstract String getString(int __a);
	
	public abstract void insert(int __a, String __b, Image __c);
	
	public abstract boolean isEnabled(int __i);
	
	public abstract boolean isSelected(int __a);
	
	public abstract void set(int __a, String __b, Image __c);
	
	public abstract void setEnabled(int __i, boolean __e);
	
	public abstract void setFitPolicy(int __a);
	
	public abstract void setFont(int __a, Font __b);
	
	public abstract void setSelectedFlags(boolean[] __a);
	
	public abstract void setSelectedIndex(int __a, boolean __b);
	
	public abstract int size();
}


