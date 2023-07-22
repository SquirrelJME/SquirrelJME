// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.m3g;


import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public class MorphingMesh
	extends Mesh
{
	@Api
	public MorphingMesh(VertexBuffer __a, VertexBuffer[] __b, IndexBuffer[] 
		__c, Appearance[] __d)
	{
		super((VertexBuffer)null, (IndexBuffer[])null, (Appearance[])null
			);
		throw Debugging.todo();
	}
	
	@Api
	public MorphingMesh(VertexBuffer __a, VertexBuffer[] __b, IndexBuffer __c
		, Appearance __d)
	{
		super((VertexBuffer)null, (IndexBuffer[])null, (Appearance[])null
			);
		throw Debugging.todo();
	}
	
	@Api
	public VertexBuffer getMorphTarget(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getMorphTargetCount()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void getWeights(float[] __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setWeights(float[] __a)
	{
		throw Debugging.todo();
	}
}


