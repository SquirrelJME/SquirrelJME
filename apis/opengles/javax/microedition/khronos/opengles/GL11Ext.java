// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.khronos.opengles;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public interface GL11Ext
	extends GL
{
	public static final int GL_MATRIX_INDEX_ARRAY_BUFFER_BINDING_OES =
		35742;
	
	public static final int GL_MATRIX_INDEX_ARRAY_OES =
		34884;
	
	public static final int GL_MATRIX_INDEX_ARRAY_POINTER_OES =
		34889;
	
	public static final int GL_MATRIX_INDEX_ARRAY_SIZE_OES =
		34886;
	
	public static final int GL_MATRIX_INDEX_ARRAY_STRIDE_OES =
		34888;
	
	public static final int GL_MATRIX_INDEX_ARRAY_TYPE_OES =
		34887;
	
	public static final int GL_MATRIX_PALETTE_OES =
		34880;
	
	public static final int GL_MAX_PALETTE_MATRICES_OES =
		34882;
	
	public static final int GL_MAX_VERTEX_UNITS_OES =
		34468;
	
	public static final int GL_TEXTURE_CROP_RECT_OES =
		35741;
	
	public static final int GL_WEIGHT_ARRAY_BUFFER_BINDING_OES =
		34974;
	
	public static final int GL_WEIGHT_ARRAY_OES =
		34477;
	
	public static final int GL_WEIGHT_ARRAY_POINTER_OES =
		34476;
	
	public static final int GL_WEIGHT_ARRAY_SIZE_OES =
		34475;
	
	public static final int GL_WEIGHT_ARRAY_STRIDE_OES =
		34474;
	
	public static final int GL_WEIGHT_ARRAY_TYPE_OES =
		34473;
	
	public abstract void glCurrentPaletteMatrixOES(int __a);
	
	public abstract void glDrawTexfOES(float __a, float __b, float __c, float
		__d, float __e);
	
	public abstract void glDrawTexfvOES(float[] __a, int __b);
	
	public abstract void glDrawTexfvOES(FloatBuffer __a);
	
	public abstract void glDrawTexiOES(int __a, int __b, int __c, int __d, 
		int __e);
	
	public abstract void glDrawTexivOES(int[] __a, int __b);
	
	public abstract void glDrawTexivOES(IntBuffer __a);
	
	public abstract void glDrawTexsOES(short __a, short __b, short __c, short
		__d, short __e);
	
	public abstract void glDrawTexsvOES(short[] __a, int __b);
	
	public abstract void glDrawTexsvOES(ShortBuffer __a);
	
	public abstract void glDrawTexxOES(int __a, int __b, int __c, int __d, 
		int __e);
	
	public abstract void glDrawTexxvOES(int[] __a, int __b);
	
	public abstract void glDrawTexxvOES(IntBuffer __a);
	
	public abstract void glEnable(int __a);
	
	public abstract void glEnableClientState(int __a);
	
	public abstract void glLoadPaletteFromModelViewMatrixOES();
	
	public abstract void glMatrixIndexPointerOES(int __a, int __b, int __c, 
		int __d);
	
	public abstract void glMatrixIndexPointerOES(int __a, int __b, int __c, 
		Buffer __d);
	
	public abstract void glTexParameterfv(int __a, int __b, float[] __c, int 
		__d);
	
	public abstract void glWeightPointerOES(int __a, int __b, int __c, int 
		__d);
	
	public abstract void glWeightPointerOES(int __a, int __b, int __c, Buffer
		__d);
}


