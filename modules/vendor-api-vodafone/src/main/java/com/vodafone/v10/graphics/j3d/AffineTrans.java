// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.vodafone.v10.graphics.j3d;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
@SuppressWarnings("FieldNamingConvention")
public class AffineTrans
{
	@Api
	public int m00;
	
	@Api
	public int m01;
	
	@Api
	public int m02;
	
	@Api
	public int m03;
	
	@Api
	public int m10;
	
	@Api
	public int m11;
	
	@Api
	public int m12;
	
	@Api
	public int m13;
	
	@Api
	public int m20;
	
	@Api
	public int m21;
	
	@Api
	public int m22;
	
	@Api
	public int m23;
	
	@Api
	public AffineTrans()
	{
		throw Debugging.todo();
	}
	
	@Api
	public AffineTrans(int[][] var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void set(int[][] var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public Vector3D transPoint(Vector3D var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void multiply(AffineTrans var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void multiply(AffineTrans var1, AffineTrans var2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void rotationX(int var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void rotationY(int var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void rotationZ(int var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void rotationV(Vector3D var1, int var2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setViewTrans(Vector3D var1, Vector3D var2, Vector3D var3)
	{
		throw Debugging.todo();
	}
}
