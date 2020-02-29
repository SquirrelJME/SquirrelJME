// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.khronos.opengles;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@SuppressWarnings("FieldNamingConvention")
public interface GL11
	extends GL10
{
	int GL_ACTIVE_TEXTURE =
		34016;
	
	int GL_ADD_SIGNED =
		34164;
	
	int GL_ALPHA_SCALE =
		3356;
	
	int GL_ALPHA_TEST_FUNC =
		3009;
	
	int GL_ALPHA_TEST_REF =
		3010;
	
	int GL_ARRAY_BUFFER =
		34962;
	
	int GL_ARRAY_BUFFER_BINDING =
		34964;
	
	int GL_BLEND_DST =
		3040;
	
	int GL_BLEND_SRC =
		3041;
	
	int GL_BUFFER_ACCESS =
		35003;
	
	int GL_BUFFER_SIZE =
		34660;
	
	int GL_BUFFER_USAGE =
		34661;
	
	int GL_CLIENT_ACTIVE_TEXTURE =
		34017;
	
	int GL_CLIP_PLANE0 =
		12288;
	
	int GL_CLIP_PLANE1 =
		12289;
	
	int GL_CLIP_PLANE2 =
		12290;
	
	int GL_CLIP_PLANE3 =
		12291;
	
	int GL_CLIP_PLANE4 =
		12292;
	
	int GL_CLIP_PLANE5 =
		12293;
	
	int GL_COLOR_ARRAY_BUFFER_BINDING =
		34968;
	
	int GL_COLOR_ARRAY_POINTER =
		32912;
	
	int GL_COLOR_ARRAY_SIZE =
		32897;
	
	int GL_COLOR_ARRAY_STRIDE =
		32899;
	
	int GL_COLOR_ARRAY_TYPE =
		32898;
	
	int GL_COLOR_CLEAR_VALUE =
		3106;
	
	int GL_COLOR_WRITEMASK =
		3107;
	
	int GL_COMBINE =
		34160;
	
	int GL_COMBINE_ALPHA =
		34162;
	
	int GL_COMBINE_RGB =
		34161;
	
	int GL_CONSTANT =
		34166;
	
	int GL_COORD_REPLACE_OES =
		34914;
	
	int GL_CULL_FACE_MODE =
		2885;
	
	int GL_CURRENT_COLOR =
		2816;
	
	int GL_CURRENT_NORMAL =
		2818;
	
	int GL_CURRENT_TEXTURE_COORDS =
		2819;
	
	int GL_DEPTH_CLEAR_VALUE =
		2931;
	
	int GL_DEPTH_FUNC =
		2932;
	
	int GL_DEPTH_RANGE =
		2928;
	
	int GL_DEPTH_WRITEMASK =
		2930;
	
	int GL_DOT3_RGB =
		34478;
	
	int GL_DOT3_RGBA =
		34479;
	
	int GL_DYNAMIC_DRAW =
		35048;
	
	int GL_ELEMENT_ARRAY_BUFFER =
		34963;
	
	int GL_ELEMENT_ARRAY_BUFFER_BINDING =
		34965;
	
	int GL_FRONT_FACE =
		2886;
	
	int GL_GENERATE_MIPMAP =
		33169;
	
	int GL_GENERATE_MIPMAP_HINT =
		33170;
	
	int GL_INTERPOLATE =
		34165;
	
	int GL_LINE_WIDTH =
		2849;
	
	int GL_LOGIC_OP_MODE =
		3056;
	
	int GL_MATRIX_MODE =
		2976;
	
	int GL_MAX_CLIP_PLANES =
		3378;
	
	int GL_MODELVIEW_MATRIX =
		2982;
	
	int GL_MODELVIEW_MATRIX_FLOAT_AS_INT_BITS_OES =
		35213;
	
	int GL_MODELVIEW_STACK_DEPTH =
		2979;
	
	int GL_NORMAL_ARRAY_BUFFER_BINDING =
		34967;
	
	int GL_NORMAL_ARRAY_POINTER =
		32911;
	
	int GL_NORMAL_ARRAY_STRIDE =
		32895;
	
	int GL_NORMAL_ARRAY_TYPE =
		32894;
	
	int GL_OPERAND0_ALPHA =
		34200;
	
	int GL_OPERAND0_RGB =
		34192;
	
	int GL_OPERAND1_ALPHA =
		34201;
	
	int GL_OPERAND1_RGB =
		34193;
	
	int GL_OPERAND2_ALPHA =
		34202;
	
	int GL_OPERAND2_RGB =
		34194;
	
	int GL_POINT_DISTANCE_ATTENUATION =
		33065;
	
	int GL_POINT_FADE_THRESHOLD_SIZE =
		33064;
	
	int GL_POINT_SIZE =
		2833;
	
	int GL_POINT_SIZE_ARRAY_BUFFER_BINDING_OES =
		35743;
	
	int GL_POINT_SIZE_ARRAY_OES =
		35740;
	
	int GL_POINT_SIZE_ARRAY_POINTER_OES =
		35212;
	
	int GL_POINT_SIZE_ARRAY_STRIDE_OES =
		35211;
	
	int GL_POINT_SIZE_ARRAY_TYPE_OES =
		35210;
	
	int GL_POINT_SIZE_MAX =
		33063;
	
	int GL_POINT_SIZE_MIN =
		33062;
	
	int GL_POINT_SPRITE_OES =
		34913;
	
	int GL_POLYGON_OFFSET_FACTOR =
		32824;
	
	int GL_POLYGON_OFFSET_UNITS =
		10752;
	
	int GL_PREVIOUS =
		34168;
	
	int GL_PRIMARY_COLOR =
		34167;
	
	int GL_PROJECTION_MATRIX =
		2983;
	
	int GL_PROJECTION_MATRIX_FLOAT_AS_INT_BITS_OES =
		35214;
	
	int GL_PROJECTION_STACK_DEPTH =
		2980;
	
	int GL_RGB_SCALE =
		34163;
	
	int GL_SAMPLES =
		32937;
	
	int GL_SAMPLE_BUFFERS =
		32936;
	
	int GL_SAMPLE_COVERAGE_INVERT =
		32939;
	
	int GL_SAMPLE_COVERAGE_VALUE =
		32938;
	
	int GL_SCISSOR_BOX =
		3088;
	
	int GL_SHADE_MODEL =
		2900;
	
	int GL_SRC0_ALPHA =
		34184;
	
	int GL_SRC0_RGB =
		34176;
	
	int GL_SRC1_ALPHA =
		34185;
	
	int GL_SRC1_RGB =
		34177;
	
	int GL_SRC2_ALPHA =
		34186;
	
	int GL_SRC2_RGB =
		34178;
	
	int GL_STATIC_DRAW =
		35044;
	
	int GL_STENCIL_CLEAR_VALUE =
		2961;
	
	int GL_STENCIL_FAIL =
		2964;
	
	int GL_STENCIL_FUNC =
		2962;
	
	int GL_STENCIL_PASS_DEPTH_FAIL =
		2965;
	
	int GL_STENCIL_PASS_DEPTH_PASS =
		2966;
	
	int GL_STENCIL_REF =
		2967;
	
	int GL_STENCIL_VALUE_MASK =
		2963;
	
	int GL_STENCIL_WRITEMASK =
		2968;
	
	int GL_SUBTRACT =
		34023;
	
	int GL_TEXTURE_BINDING_2D =
		32873;
	
	int GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING =
		34970;
	
	int GL_TEXTURE_COORD_ARRAY_POINTER =
		32914;
	
	int GL_TEXTURE_COORD_ARRAY_SIZE =
		32904;
	
	int GL_TEXTURE_COORD_ARRAY_STRIDE =
		32906;
	
	int GL_TEXTURE_COORD_ARRAY_TYPE =
		32905;
	
	int GL_TEXTURE_MATRIX =
		2984;
	
	int GL_TEXTURE_MATRIX_FLOAT_AS_INT_BITS_OES =
		35215;
	
	int GL_TEXTURE_STACK_DEPTH =
		2981;
	
	int GL_VERTEX_ARRAY_BUFFER_BINDING =
		34966;
	
	int GL_VERTEX_ARRAY_POINTER =
		32910;
	
	int GL_VERTEX_ARRAY_SIZE =
		32890;
	
	int GL_VERTEX_ARRAY_STRIDE =
		32892;
	
	int GL_VERTEX_ARRAY_TYPE =
		32891;
	
	int GL_VIEWPORT =
		2978;
	
	int GL_WRITE_ONLY =
		35001;
	
	void glBindBuffer(int __a, int __b);
	
	void glBufferData(int __a, int __b, Buffer __c, int __d);
	
	void glBufferSubData(int __a, int __b, int __c, Buffer __d);
	
	void glClipPlanef(int __a, float[] __b, int __c);
	
	void glClipPlanef(int __a, FloatBuffer __b);
	
	void glClipPlanex(int __a, int[] __b, int __c);
	
	void glClipPlanex(int __a, IntBuffer __b);
	
	void glColor4ub(byte __a, byte __b, byte __c, byte __d);
	
	void glColorPointer(int __a, int __b, int __c, int __d);
	
	void glDeleteBuffers(int __a, int[] __b, int __c);
	
	void glDeleteBuffers(int __a, IntBuffer __b);
	
	void glDrawElements(int __a, int __b, int __c, int __d);
	
	void glGenBuffers(int __a, int[] __b, int __c);
	
	void glGenBuffers(int __a, IntBuffer __b);
	
	void glGetBooleanv(int __a, boolean[] __b, int __c);
	
	void glGetBooleanv(int __a, IntBuffer __b);
	
	void glGetBufferParameteriv(int __a, int __b, int[] __c, int __d);
	
	void glGetBufferParameteriv(int __a, int __b, IntBuffer __c);
	
	void glGetClipPlanef(int __a, float[] __b, int __c);
	
	void glGetClipPlanef(int __a, FloatBuffer __b);
	
	void glGetClipPlanex(int __a, int[] __b, int __c);
	
	void glGetClipPlanex(int __a, IntBuffer __b);
	
	void glGetFixedv(int __a, int[] __b, int __c);
	
	void glGetFixedv(int __a, IntBuffer __b);
	
	void glGetFloatv(int __a, float[] __b, int __c);
	
	void glGetFloatv(int __a, FloatBuffer __b);
	
	void glGetLightfv(int __a, int __b, float[] __c, int __d);
	
	void glGetLightfv(int __a, int __b, FloatBuffer __c);
	
	void glGetLightxv(int __a, int __b, int[] __c, int __d);
	
	void glGetLightxv(int __a, int __b, IntBuffer __c);
	
	void glGetMaterialfv(int __a, int __b, float[] __c, int __d);
	
	void glGetMaterialfv(int __a, int __b, FloatBuffer __c);
	
	void glGetMaterialxv(int __a, int __b, int[] __c, int __d);
	
	void glGetMaterialxv(int __a, int __b, IntBuffer __c);
	
	void glGetPointerv(int __a, Buffer[] __b);
	
	void glGetTexEnvfv(int __a, int __b, float[] __c, int __d);
	
	void glGetTexEnvfv(int __a, int __b, FloatBuffer __c);
	
	void glGetTexEnviv(int __a, int __b, int[] __c, int __d);
	
	void glGetTexEnviv(int __a, int __b, IntBuffer __c);
	
	void glGetTexEnvxv(int __a, int __b, int[] __c, int __d);
	
	void glGetTexEnvxv(int __a, int __b, IntBuffer __c);
	
	void glGetTexParameterfv(int __a, int __b, float[] __c, int __d);
	
	void glGetTexParameterfv(int __a, int __b, FloatBuffer __c);
	
	void glGetTexParameteriv(int __a, int __b, int[] __c, int __d);
	
	void glGetTexParameteriv(int __a, int __b, IntBuffer __c);
	
	void glGetTexParameterxv(int __a, int __b, int[] __c, int __d);
	
	void glGetTexParameterxv(int __a, int __b, IntBuffer __c);
	
	boolean glIsBuffer(int __a);
	
	boolean glIsEnabled(int __a);
	
	boolean glIsTexture(int __a);
	
	void glNormalPointer(int __a, int __b, int __c);
	
	void glPointParameterf(int __a, float __b);
	
	void glPointParameterfv(int __a, float[] __b, int __c);
	
	void glPointParameterfv(int __a, FloatBuffer __b);
	
	void glPointParameterx(int __a, int __b);
	
	void glPointParameterxv(int __a, int[] __b, int __c);
	
	void glPointParameterxv(int __a, IntBuffer __b);
	
	void glPointSizePointerOES(int __a, int __b, int __c);
	
	void glPointSizePointerOES(int __a, int __b, Buffer __c);
	
	void glTexCoordPointer(int __a, int __b, int __c, int __d);
	
	void glTexEnvi(int __a, int __b, int __c);
	
	void glTexEnviv(int __a, int __b, int[] __c, int __d);
	
	void glTexEnviv(int __a, int __b, IntBuffer __c);
	
	void glTexParameterfv(int __a, int __b, float[] __c, int __d);
	
	void glTexParameterfv(int __a, int __b, FloatBuffer __c);
	
	void glTexParameteri(int __a, int __b, int __c);
	
	void glTexParameteriv(int __a, int __b, int[] __c, int __d);
	
	void glTexParameteriv(int __a, int __b, IntBuffer __c);
	
	void glTexParameterxv(int __a, int __b, int[] __c, int __d);
	
	void glTexParameterxv(int __a, int __b, IntBuffer __c);
	
	void glVertexPointer(int __a, int __b, int __c, int __d);
}


