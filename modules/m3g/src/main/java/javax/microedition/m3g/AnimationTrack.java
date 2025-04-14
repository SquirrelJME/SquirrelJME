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
public class AnimationTrack
	extends Object3D
{
	@Api
	public static final int ALPHA =
		256;
	
	@Api
	public static final int AMBIENT_COLOR =
		257;
	
	@Api
	public static final int COLOR =
		258;
	
	@Api
	public static final int CROP =
		259;
	
	@Api
	public static final int DENSITY =
		260;
	
	@Api
	public static final int DIFFUSE_COLOR =
		261;
	
	@Api
	public static final int EMISSIVE_COLOR =
		262;
	
	@Api
	public static final int FAR_DISTANCE =
		263;
	
	@Api
	public static final int FIELD_OF_VIEW =
		264;
	
	@Api
	public static final int INTENSITY =
		265;
	
	@Api
	public static final int MORPH_WEIGHTS =
		266;
	
	@Api
	public static final int NEAR_DISTANCE =
		267;
	
	@Api
	public static final int ORIENTATION =
		268;
	
	@SuppressWarnings("SpellCheckingInspection")
	@Api
	public static final int PICKABILITY =
		269;
	
	@Api
	public static final int SCALE =
		270;
	
	@Api
	public static final int SHININESS =
		271;
	
	@Api
	public static final int SPECULAR_COLOR =
		272;
	
	@Api
	public static final int SPOT_ANGLE =
		273;
	
	@Api
	public static final int SPOT_EXPONENT =
		274;
	
	@Api
	public static final int TRANSLATION =
		275;
	
	@Api
	public static final int VISIBILITY =
		276;
	
	@Api
	public AnimationTrack(KeyframeSequence __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public AnimationController getController()
	{
		throw Debugging.todo();
	}
	
	@Api
	public KeyframeSequence getKeyframeSequence()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getTargetProperty()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setController(AnimationController __a)
	{
		throw Debugging.todo();
	}
}


