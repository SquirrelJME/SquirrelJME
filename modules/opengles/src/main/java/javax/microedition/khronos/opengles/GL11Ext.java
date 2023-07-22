// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.khronos.opengles;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

@Api
@SuppressWarnings({"FieldNamingConvention", "NewMethodNamingConvention"})
public interface GL11Ext
	extends GL
{
	@Api
	int GL_MATRIX_INDEX_ARRAY_BUFFER_BINDING_OES =
		35742;
	
	@Api
	int GL_MATRIX_INDEX_ARRAY_OES =
		34884;
	
	@Api
	int GL_MATRIX_INDEX_ARRAY_POINTER_OES =
		34889;
	
	@Api
	int GL_MATRIX_INDEX_ARRAY_SIZE_OES =
		34886;
	
	@Api
	int GL_MATRIX_INDEX_ARRAY_STRIDE_OES =
		34888;
	
	@Api
	int GL_MATRIX_INDEX_ARRAY_TYPE_OES =
		34887;
	
	@Api
	int GL_MATRIX_PALETTE_OES =
		34880;
	
	@Api
	int GL_MAX_PALETTE_MATRICES_OES =
		34882;
	
	@Api
	int GL_MAX_VERTEX_UNITS_OES =
		34468;
	
	@Api
	int GL_TEXTURE_CROP_RECT_OES =
		35741;
	
	@Api
	int GL_WEIGHT_ARRAY_BUFFER_BINDING_OES =
		34974;
	
	@Api
	int GL_WEIGHT_ARRAY_OES =
		34477;
	
	@Api
	int GL_WEIGHT_ARRAY_POINTER_OES =
		34476;
	
	@Api
	int GL_WEIGHT_ARRAY_SIZE_OES =
		34475;
	
	@Api
	int GL_WEIGHT_ARRAY_STRIDE_OES =
		34474;
	
	@Api
	int GL_WEIGHT_ARRAY_TYPE_OES =
		34473;
	
	@Api
	void glCurrentPaletteMatrixOES(int __a);
	
	@Api
	void glDrawTexfOES(float __a, float __b, float __c, float __d, float __e);
	
	@Api
	void glDrawTexfvOES(float[] __a, int __b);
	
	@Api
	void glDrawTexfvOES(FloatBuffer __a);
	
	@Api
	void glDrawTexiOES(int __a, int __b, int __c, int __d, int __e);
	
	@Api
	void glDrawTexivOES(int[] __a, int __b);
	
	@Api
	void glDrawTexivOES(IntBuffer __a);
	
	@Api
	void glDrawTexsOES(short __a, short __b, short __c, short __d, short __e);
	
	@Api
	void glDrawTexsvOES(short[] __a, int __b);
	
	@Api
	void glDrawTexsvOES(ShortBuffer __a);
	
	@Api
	void glDrawTexxOES(int __a, int __b, int __c, int __d, int __e);
	
	@Api
	void glDrawTexxvOES(int[] __a, int __b);
	
	@Api
	void glDrawTexxvOES(IntBuffer __a);
	
	@Api
	void glEnable(int __a);
	
	@Api
	void glEnableClientState(int __a);
	
	@Api
	void glLoadPaletteFromModelViewMatrixOES();
	
	@Api
	void glMatrixIndexPointerOES(int __a, int __b, int __c, int __d);
	
	@Api
	void glMatrixIndexPointerOES(int __a, int __b, int __c, Buffer __d);
	
	@Api
	void glTexParameterfv(int __a, int __b, float[] __c, int __d);
	
	@Api
	void glWeightPointerOES(int __a, int __b, int __c, int __d);
	
	@Api
	void glWeightPointerOES(int __a, int __b, int __c, Buffer __d);
}


