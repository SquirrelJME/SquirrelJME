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

public interface GL11
	extends GL10
{
	public static final int GL_ACTIVE_TEXTURE =
		34016;
	
	public static final int GL_ADD_SIGNED =
		34164;
	
	public static final int GL_ALPHA_SCALE =
		3356;
	
	public static final int GL_ALPHA_TEST_FUNC =
		3009;
	
	public static final int GL_ALPHA_TEST_REF =
		3010;
	
	public static final int GL_ARRAY_BUFFER =
		34962;
	
	public static final int GL_ARRAY_BUFFER_BINDING =
		34964;
	
	public static final int GL_BLEND_DST =
		3040;
	
	public static final int GL_BLEND_SRC =
		3041;
	
	public static final int GL_BUFFER_ACCESS =
		35003;
	
	public static final int GL_BUFFER_SIZE =
		34660;
	
	public static final int GL_BUFFER_USAGE =
		34661;
	
	public static final int GL_CLIENT_ACTIVE_TEXTURE =
		34017;
	
	public static final int GL_CLIP_PLANE0 =
		12288;
	
	public static final int GL_CLIP_PLANE1 =
		12289;
	
	public static final int GL_CLIP_PLANE2 =
		12290;
	
	public static final int GL_CLIP_PLANE3 =
		12291;
	
	public static final int GL_CLIP_PLANE4 =
		12292;
	
	public static final int GL_CLIP_PLANE5 =
		12293;
	
	public static final int GL_COLOR_ARRAY_BUFFER_BINDING =
		34968;
	
	public static final int GL_COLOR_ARRAY_POINTER =
		32912;
	
	public static final int GL_COLOR_ARRAY_SIZE =
		32897;
	
	public static final int GL_COLOR_ARRAY_STRIDE =
		32899;
	
	public static final int GL_COLOR_ARRAY_TYPE =
		32898;
	
	public static final int GL_COLOR_CLEAR_VALUE =
		3106;
	
	public static final int GL_COLOR_WRITEMASK =
		3107;
	
	public static final int GL_COMBINE =
		34160;
	
	public static final int GL_COMBINE_ALPHA =
		34162;
	
	public static final int GL_COMBINE_RGB =
		34161;
	
	public static final int GL_CONSTANT =
		34166;
	
	public static final int GL_COORD_REPLACE_OES =
		34914;
	
	public static final int GL_CULL_FACE_MODE =
		2885;
	
	public static final int GL_CURRENT_COLOR =
		2816;
	
	public static final int GL_CURRENT_NORMAL =
		2818;
	
	public static final int GL_CURRENT_TEXTURE_COORDS =
		2819;
	
	public static final int GL_DEPTH_CLEAR_VALUE =
		2931;
	
	public static final int GL_DEPTH_FUNC =
		2932;
	
	public static final int GL_DEPTH_RANGE =
		2928;
	
	public static final int GL_DEPTH_WRITEMASK =
		2930;
	
	public static final int GL_DOT3_RGB =
		34478;
	
	public static final int GL_DOT3_RGBA =
		34479;
	
	public static final int GL_DYNAMIC_DRAW =
		35048;
	
	public static final int GL_ELEMENT_ARRAY_BUFFER =
		34963;
	
	public static final int GL_ELEMENT_ARRAY_BUFFER_BINDING =
		34965;
	
	public static final int GL_FRONT_FACE =
		2886;
	
	public static final int GL_GENERATE_MIPMAP =
		33169;
	
	public static final int GL_GENERATE_MIPMAP_HINT =
		33170;
	
	public static final int GL_INTERPOLATE =
		34165;
	
	public static final int GL_LINE_WIDTH =
		2849;
	
	public static final int GL_LOGIC_OP_MODE =
		3056;
	
	public static final int GL_MATRIX_MODE =
		2976;
	
	public static final int GL_MAX_CLIP_PLANES =
		3378;
	
	public static final int GL_MODELVIEW_MATRIX =
		2982;
	
	public static final int GL_MODELVIEW_MATRIX_FLOAT_AS_INT_BITS_OES =
		35213;
	
	public static final int GL_MODELVIEW_STACK_DEPTH =
		2979;
	
	public static final int GL_NORMAL_ARRAY_BUFFER_BINDING =
		34967;
	
	public static final int GL_NORMAL_ARRAY_POINTER =
		32911;
	
	public static final int GL_NORMAL_ARRAY_STRIDE =
		32895;
	
	public static final int GL_NORMAL_ARRAY_TYPE =
		32894;
	
	public static final int GL_OPERAND0_ALPHA =
		34200;
	
	public static final int GL_OPERAND0_RGB =
		34192;
	
	public static final int GL_OPERAND1_ALPHA =
		34201;
	
	public static final int GL_OPERAND1_RGB =
		34193;
	
	public static final int GL_OPERAND2_ALPHA =
		34202;
	
	public static final int GL_OPERAND2_RGB =
		34194;
	
	public static final int GL_POINT_DISTANCE_ATTENUATION =
		33065;
	
	public static final int GL_POINT_FADE_THRESHOLD_SIZE =
		33064;
	
	public static final int GL_POINT_SIZE =
		2833;
	
	public static final int GL_POINT_SIZE_ARRAY_BUFFER_BINDING_OES =
		35743;
	
	public static final int GL_POINT_SIZE_ARRAY_OES =
		35740;
	
	public static final int GL_POINT_SIZE_ARRAY_POINTER_OES =
		35212;
	
	public static final int GL_POINT_SIZE_ARRAY_STRIDE_OES =
		35211;
	
	public static final int GL_POINT_SIZE_ARRAY_TYPE_OES =
		35210;
	
	public static final int GL_POINT_SIZE_MAX =
		33063;
	
	public static final int GL_POINT_SIZE_MIN =
		33062;
	
	public static final int GL_POINT_SPRITE_OES =
		34913;
	
	public static final int GL_POLYGON_OFFSET_FACTOR =
		32824;
	
	public static final int GL_POLYGON_OFFSET_UNITS =
		10752;
	
	public static final int GL_PREVIOUS =
		34168;
	
	public static final int GL_PRIMARY_COLOR =
		34167;
	
	public static final int GL_PROJECTION_MATRIX =
		2983;
	
	public static final int GL_PROJECTION_MATRIX_FLOAT_AS_INT_BITS_OES =
		35214;
	
	public static final int GL_PROJECTION_STACK_DEPTH =
		2980;
	
	public static final int GL_RGB_SCALE =
		34163;
	
	public static final int GL_SAMPLES =
		32937;
	
	public static final int GL_SAMPLE_BUFFERS =
		32936;
	
	public static final int GL_SAMPLE_COVERAGE_INVERT =
		32939;
	
	public static final int GL_SAMPLE_COVERAGE_VALUE =
		32938;
	
	public static final int GL_SCISSOR_BOX =
		3088;
	
	public static final int GL_SHADE_MODEL =
		2900;
	
	public static final int GL_SRC0_ALPHA =
		34184;
	
	public static final int GL_SRC0_RGB =
		34176;
	
	public static final int GL_SRC1_ALPHA =
		34185;
	
	public static final int GL_SRC1_RGB =
		34177;
	
	public static final int GL_SRC2_ALPHA =
		34186;
	
	public static final int GL_SRC2_RGB =
		34178;
	
	public static final int GL_STATIC_DRAW =
		35044;
	
	public static final int GL_STENCIL_CLEAR_VALUE =
		2961;
	
	public static final int GL_STENCIL_FAIL =
		2964;
	
	public static final int GL_STENCIL_FUNC =
		2962;
	
	public static final int GL_STENCIL_PASS_DEPTH_FAIL =
		2965;
	
	public static final int GL_STENCIL_PASS_DEPTH_PASS =
		2966;
	
	public static final int GL_STENCIL_REF =
		2967;
	
	public static final int GL_STENCIL_VALUE_MASK =
		2963;
	
	public static final int GL_STENCIL_WRITEMASK =
		2968;
	
	public static final int GL_SUBTRACT =
		34023;
	
	public static final int GL_TEXTURE_BINDING_2D =
		32873;
	
	public static final int GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING =
		34970;
	
	public static final int GL_TEXTURE_COORD_ARRAY_POINTER =
		32914;
	
	public static final int GL_TEXTURE_COORD_ARRAY_SIZE =
		32904;
	
	public static final int GL_TEXTURE_COORD_ARRAY_STRIDE =
		32906;
	
	public static final int GL_TEXTURE_COORD_ARRAY_TYPE =
		32905;
	
	public static final int GL_TEXTURE_MATRIX =
		2984;
	
	public static final int GL_TEXTURE_MATRIX_FLOAT_AS_INT_BITS_OES =
		35215;
	
	public static final int GL_TEXTURE_STACK_DEPTH =
		2981;
	
	public static final int GL_VERTEX_ARRAY_BUFFER_BINDING =
		34966;
	
	public static final int GL_VERTEX_ARRAY_POINTER =
		32910;
	
	public static final int GL_VERTEX_ARRAY_SIZE =
		32890;
	
	public static final int GL_VERTEX_ARRAY_STRIDE =
		32892;
	
	public static final int GL_VERTEX_ARRAY_TYPE =
		32891;
	
	public static final int GL_VIEWPORT =
		2978;
	
	public static final int GL_WRITE_ONLY =
		35001;
	
	public abstract void glBindBuffer(int __a, int __b);
	
	public abstract void glBufferData(int __a, int __b, Buffer __c, int __d);
	
	public abstract void glBufferSubData(int __a, int __b, int __c, Buffer 
		__d);
	
	public abstract void glClipPlanef(int __a, float[] __b, int __c);
	
	public abstract void glClipPlanef(int __a, FloatBuffer __b);
	
	public abstract void glClipPlanex(int __a, int[] __b, int __c);
	
	public abstract void glClipPlanex(int __a, IntBuffer __b);
	
	public abstract void glColor4ub(byte __a, byte __b, byte __c, byte __d);
	
	public abstract void glColorPointer(int __a, int __b, int __c, int __d);
	
	public abstract void glDeleteBuffers(int __a, int[] __b, int __c);
	
	public abstract void glDeleteBuffers(int __a, IntBuffer __b);
	
	public abstract void glDrawElements(int __a, int __b, int __c, int __d);
	
	public abstract void glGenBuffers(int __a, int[] __b, int __c);
	
	public abstract void glGenBuffers(int __a, IntBuffer __b);
	
	public abstract void glGetBooleanv(int __a, boolean[] __b, int __c);
	
	public abstract void glGetBooleanv(int __a, IntBuffer __b);
	
	public abstract void glGetBufferParameteriv(int __a, int __b, int[] __c, 
		int __d);
	
	public abstract void glGetBufferParameteriv(int __a, int __b, IntBuffer 
		__c);
	
	public abstract void glGetClipPlanef(int __a, float[] __b, int __c);
	
	public abstract void glGetClipPlanef(int __a, FloatBuffer __b);
	
	public abstract void glGetClipPlanex(int __a, int[] __b, int __c);
	
	public abstract void glGetClipPlanex(int __a, IntBuffer __b);
	
	public abstract void glGetFixedv(int __a, int[] __b, int __c);
	
	public abstract void glGetFixedv(int __a, IntBuffer __b);
	
	public abstract void glGetFloatv(int __a, float[] __b, int __c);
	
	public abstract void glGetFloatv(int __a, FloatBuffer __b);
	
	public abstract void glGetLightfv(int __a, int __b, float[] __c, int __d
		);
	
	public abstract void glGetLightfv(int __a, int __b, FloatBuffer __c);
	
	public abstract void glGetLightxv(int __a, int __b, int[] __c, int __d);
	
	public abstract void glGetLightxv(int __a, int __b, IntBuffer __c);
	
	public abstract void glGetMaterialfv(int __a, int __b, float[] __c, int 
		__d);
	
	public abstract void glGetMaterialfv(int __a, int __b, FloatBuffer __c);
	
	public abstract void glGetMaterialxv(int __a, int __b, int[] __c, int __d
		);
	
	public abstract void glGetMaterialxv(int __a, int __b, IntBuffer __c);
	
	public abstract void glGetPointerv(int __a, Buffer[] __b);
	
	public abstract void glGetTexEnvfv(int __a, int __b, float[] __c, int __d
		);
	
	public abstract void glGetTexEnvfv(int __a, int __b, FloatBuffer __c);
	
	public abstract void glGetTexEnviv(int __a, int __b, int[] __c, int __d);
	
	public abstract void glGetTexEnviv(int __a, int __b, IntBuffer __c);
	
	public abstract void glGetTexEnvxv(int __a, int __b, int[] __c, int __d);
	
	public abstract void glGetTexEnvxv(int __a, int __b, IntBuffer __c);
	
	public abstract void glGetTexParameterfv(int __a, int __b, float[] __c, 
		int __d);
	
	public abstract void glGetTexParameterfv(int __a, int __b, FloatBuffer 
		__c);
	
	public abstract void glGetTexParameteriv(int __a, int __b, int[] __c, int
		__d);
	
	public abstract void glGetTexParameteriv(int __a, int __b, IntBuffer __c
		);
	
	public abstract void glGetTexParameterxv(int __a, int __b, int[] __c, int
		__d);
	
	public abstract void glGetTexParameterxv(int __a, int __b, IntBuffer __c
		);
	
	public abstract boolean glIsBuffer(int __a);
	
	public abstract boolean glIsEnabled(int __a);
	
	public abstract boolean glIsTexture(int __a);
	
	public abstract void glNormalPointer(int __a, int __b, int __c);
	
	public abstract void glPointParameterf(int __a, float __b);
	
	public abstract void glPointParameterfv(int __a, float[] __b, int __c);
	
	public abstract void glPointParameterfv(int __a, FloatBuffer __b);
	
	public abstract void glPointParameterx(int __a, int __b);
	
	public abstract void glPointParameterxv(int __a, int[] __b, int __c);
	
	public abstract void glPointParameterxv(int __a, IntBuffer __b);
	
	public abstract void glPointSizePointerOES(int __a, int __b, int __c);
	
	public abstract void glPointSizePointerOES(int __a, int __b, Buffer __c);
	
	public abstract void glTexCoordPointer(int __a, int __b, int __c, int __d
		);
	
	public abstract void glTexEnvi(int __a, int __b, int __c);
	
	public abstract void glTexEnviv(int __a, int __b, int[] __c, int __d);
	
	public abstract void glTexEnviv(int __a, int __b, IntBuffer __c);
	
	public abstract void glTexParameterfv(int __a, int __b, float[] __c, int 
		__d);
	
	public abstract void glTexParameterfv(int __a, int __b, FloatBuffer __c);
	
	public abstract void glTexParameteri(int __a, int __b, int __c);
	
	public abstract void glTexParameteriv(int __a, int __b, int[] __c, int 
		__d);
	
	public abstract void glTexParameteriv(int __a, int __b, IntBuffer __c);
	
	public abstract void glTexParameterxv(int __a, int __b, int[] __c, int 
		__d);
	
	public abstract void glTexParameterxv(int __a, int __b, IntBuffer __c);
	
	public abstract void glVertexPointer(int __a, int __b, int __c, int __d);
}


