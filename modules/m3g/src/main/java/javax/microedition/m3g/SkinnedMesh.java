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
public class SkinnedMesh
	extends Mesh
{
	@Api
	public SkinnedMesh(VertexBuffer __a, IndexBuffer[] __b, Appearance[] __c,
		Group __d)
	{
		super((VertexBuffer)null, (IndexBuffer[])null, (Appearance[])null
			);
		throw Debugging.todo();
	}
	
	@Api
	public SkinnedMesh(VertexBuffer __a, IndexBuffer __b, Appearance __c, 
		Group __d)
	{
		super((VertexBuffer)null, (IndexBuffer[])null, (Appearance[])null
			);
		throw Debugging.todo();
	}
	
	@Api
	public void addTransform(Node __a, int __b, int __c, int __d)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void getBoneTransform(Node __a, Transform __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getBoneVertices(Node __a, int[] __b, float[] __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public Group getSkeleton()
	{
		throw Debugging.todo();
	}
}


