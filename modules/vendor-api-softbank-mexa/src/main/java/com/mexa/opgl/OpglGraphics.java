// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.mexa.opgl;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11ExtensionPack;

public abstract class OpglGraphics
	implements GL10, GL11, GL11ExtensionPack
{
	public abstract void bind(Object __canvas);
	
	public abstract void glColorPointer(int __a, int __b, int __c, Buffer __d);
	
	public abstract void glCompressedTexImage2D(int __a, int __b, int __c,
		int __d, int __e, int __f, ByteBuffer __g);
	
	public abstract void glDrawElements(int __a, int __b, Buffer __c);
	
	public abstract void glMatrixIndexPointerOES(int __a, int __b, int __c,
		Buffer __d);
	
	public abstract void glNormalPointer(int __a, int __b, Buffer __c);
	
	public abstract void glTexCoordPointer(int __a, int __b, int __c,
		Buffer __d);
	
	public abstract void glTexImage2D(int __a, int __b, int __c, int __d,
		int __e, int __f, int __g, int __h, Buffer __i);
	
	public abstract void glVertexPointer(int __a, int __b, int __c,
		Buffer __d);
	
	public abstract void glWeightPointerOES(int __a, int __b, int __c,
		Buffer __d);
	
	public abstract void release();
	
	public static OpglGraphics getInstance()
	{
		throw Debugging.todo();
	}
}
