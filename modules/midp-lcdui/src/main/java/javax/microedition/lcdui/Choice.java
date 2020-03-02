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
	int EXCLUSIVE =
		1;
	
	int IMPLICIT =
		3;
	
	int MULTIPLE =
		2;
	
	int POPUP =
		4;
	
	int TEXT_WRAP_DEFAULT =
		0;
	
	int TEXT_WRAP_OFF =
		2;
	
	int TEXT_WRAP_ON =
		1;
	
	int append(String __a, Image __b);
	
	void delete(int __a);
	
	void deleteAll();
	
	int getFitPolicy();
	
	Font getFont(int __a);
	
	Image getImage(int __a);
	
	int getSelectedFlags(boolean[] __a);
	
	int getSelectedIndex();
	
	String getString(int __a);
	
	void insert(int __a, String __b, Image __c);
	
	boolean isEnabled(int __i);
	
	boolean isSelected(int __a);
	
	void set(int __a, String __b, Image __c);
	
	void setEnabled(int __i, boolean __e);
	
	void setFitPolicy(int __a);
	
	void setFont(int __a, Font __b);
	
	void setSelectedFlags(boolean[] __a);
	
	void setSelectedIndex(int __a, boolean __b);
	
	int size();
}


