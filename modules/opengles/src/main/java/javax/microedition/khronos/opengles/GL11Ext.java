// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.khronos.opengles;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

@SuppressWarnings({"FieldNamingConvention", "NewMethodNamingConvention"})
public interface GL11Ext
	extends GL
{
	int GL_MATRIX_INDEX_ARRAY_BUFFER_BINDING_OES =
		35742;
	
	int GL_MATRIX_INDEX_ARRAY_OES =
		34884;
	
	int GL_MATRIX_INDEX_ARRAY_POINTER_OES =
		34889;
	
	int GL_MATRIX_INDEX_ARRAY_SIZE_OES =
		34886;
	
	int GL_MATRIX_INDEX_ARRAY_STRIDE_OES =
		34888;
	
	int GL_MATRIX_INDEX_ARRAY_TYPE_OES =
		34887;
	
	int GL_MATRIX_PALETTE_OES =
		34880;
	
	int GL_MAX_PALETTE_MATRICES_OES =
		34882;
	
	int GL_MAX_VERTEX_UNITS_OES =
		34468;
	
	int GL_TEXTURE_CROP_RECT_OES =
		35741;
	
	int GL_WEIGHT_ARRAY_BUFFER_BINDING_OES =
		34974;
	
	int GL_WEIGHT_ARRAY_OES =
		34477;
	
	int GL_WEIGHT_ARRAY_POINTER_OES =
		34476;
	
	int GL_WEIGHT_ARRAY_SIZE_OES =
		34475;
	
	int GL_WEIGHT_ARRAY_STRIDE_OES =
		34474;
	
	int GL_WEIGHT_ARRAY_TYPE_OES =
		34473;
	
	void glCurrentPaletteMatrixOES(int __a);
	
	void glDrawTexfOES(float __a, float __b, float __c, float __d, float __e);
	
	void glDrawTexfvOES(float[] __a, int __b);
	
	void glDrawTexfvOES(FloatBuffer __a);
	
	void glDrawTexiOES(int __a, int __b, int __c, int __d, int __e);
	
	void glDrawTexivOES(int[] __a, int __b);
	
	void glDrawTexivOES(IntBuffer __a);
	
	void glDrawTexsOES(short __a, short __b, short __c, short __d, short __e);
	
	void glDrawTexsvOES(short[] __a, int __b);
	
	void glDrawTexsvOES(ShortBuffer __a);
	
	void glDrawTexxOES(int __a, int __b, int __c, int __d, int __e);
	
	void glDrawTexxvOES(int[] __a, int __b);
	
	void glDrawTexxvOES(IntBuffer __a);
	
	void glEnable(int __a);
	
	void glEnableClientState(int __a);
	
	void glLoadPaletteFromModelViewMatrixOES();
	
	void glMatrixIndexPointerOES(int __a, int __b, int __c, int __d);
	
	void glMatrixIndexPointerOES(int __a, int __b, int __c, Buffer __d);
	
	void glTexParameterfv(int __a, int __b, float[] __c, int __d);
	
	void glWeightPointerOES(int __a, int __b, int __c, int __d);
	
	void glWeightPointerOES(int __a, int __b, int __c, Buffer __d);
}


