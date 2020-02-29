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

public interface GL10
	extends GL
{
	int GL_ADD =
		260;
	
	int GL_ALIASED_LINE_WIDTH_RANGE =
		33902;
	
	int GL_ALIASED_POINT_SIZE_RANGE =
		33901;
	
	int GL_ALPHA =
		6406;
	
	int GL_ALPHA_BITS =
		3413;
	
	int GL_ALPHA_TEST =
		3008;
	
	int GL_ALWAYS =
		519;
	
	int GL_AMBIENT =
		4608;
	
	int GL_AMBIENT_AND_DIFFUSE =
		5634;
	
	int GL_AND =
		5377;
	
	int GL_AND_INVERTED =
		5380;
	
	int GL_AND_REVERSE =
		5378;
	
	int GL_BACK =
		1029;
	
	int GL_BLEND =
		3042;
	
	int GL_BLUE_BITS =
		3412;
	
	int GL_BYTE =
		5120;
	
	int GL_CCW =
		2305;
	
	int GL_CLAMP_TO_EDGE =
		33071;
	
	int GL_CLEAR =
		5376;
	
	int GL_COLOR_ARRAY =
		32886;
	
	int GL_COLOR_BUFFER_BIT =
		16384;
	
	int GL_COLOR_LOGIC_OP =
		3058;
	
	int GL_COLOR_MATERIAL =
		2903;
	
	int GL_COMPRESSED_TEXTURE_FORMATS =
		34467;
	
	int GL_CONSTANT_ATTENUATION =
		4615;
	
	int GL_COPY =
		5379;
	
	int GL_COPY_INVERTED =
		5388;
	
	int GL_CULL_FACE =
		2884;
	
	int GL_CW =
		2304;
	
	int GL_DECAL =
		8449;
	
	int GL_DECR =
		7683;
	
	int GL_DEPTH_BITS =
		3414;
	
	int GL_DEPTH_BUFFER_BIT =
		256;
	
	int GL_DEPTH_TEST =
		2929;
	
	int GL_DIFFUSE =
		4609;
	
	int GL_DITHER =
		3024;
	
	int GL_DONT_CARE =
		4352;
	
	int GL_DST_ALPHA =
		772;
	
	int GL_DST_COLOR =
		774;
	
	int GL_EMISSION =
		5632;
	
	int GL_EQUAL =
		514;
	
	int GL_EQUIV =
		5385;
	
	int GL_EXP =
		2048;
	
	int GL_EXP2 =
		2049;
	
	int GL_EXTENSIONS =
		7939;
	
	int GL_FALSE =
		0;
	
	int GL_FASTEST =
		4353;
	
	int GL_FIXED =
		5132;
	
	int GL_FLAT =
		7424;
	
	int GL_FLOAT =
		5126;
	
	int GL_FOG =
		2912;
	
	int GL_FOG_COLOR =
		2918;
	
	int GL_FOG_DENSITY =
		2914;
	
	int GL_FOG_END =
		2916;
	
	int GL_FOG_HINT =
		3156;
	
	int GL_FOG_MODE =
		2917;
	
	int GL_FOG_START =
		2915;
	
	int GL_FRONT =
		1028;
	
	int GL_FRONT_AND_BACK =
		1032;
	
	int GL_GEQUAL =
		518;
	
	int GL_GREATER =
		516;
	
	int GL_GREEN_BITS =
		3411;
	
	int GL_IMPLEMENTATION_COLOR_READ_FORMAT_OES =
		35739;
	
	int GL_IMPLEMENTATION_COLOR_READ_TYPE_OES =
		35738;
	
	int GL_INCR =
		7682;
	
	int GL_INVALID_ENUM =
		1280;
	
	int GL_INVALID_OPERATION =
		1282;
	
	int GL_INVALID_VALUE =
		1281;
	
	int GL_INVERT =
		5386;
	
	int GL_KEEP =
		7680;
	
	int GL_LEQUAL =
		515;
	
	int GL_LESS =
		513;
	
	int GL_LIGHT0 =
		16384;
	
	int GL_LIGHT1 =
		16385;
	
	int GL_LIGHT2 =
		16386;
	
	int GL_LIGHT3 =
		16387;
	
	int GL_LIGHT4 =
		16388;
	
	int GL_LIGHT5 =
		16389;
	
	int GL_LIGHT6 =
		16390;
	
	int GL_LIGHT7 =
		16391;
	
	int GL_LIGHTING =
		2896;
	
	int GL_LIGHT_MODEL_AMBIENT =
		2899;
	
	int GL_LIGHT_MODEL_TWO_SIDE =
		2898;
	
	int GL_LINEAR =
		9729;
	
	int GL_LINEAR_ATTENUATION =
		4616;
	
	int GL_LINEAR_MIPMAP_LINEAR =
		9987;
	
	int GL_LINEAR_MIPMAP_NEAREST =
		9985;
	
	int GL_LINES =
		1;
	
	int GL_LINE_LOOP =
		2;
	
	int GL_LINE_SMOOTH =
		2848;
	
	int GL_LINE_SMOOTH_HINT =
		3154;
	
	int GL_LINE_STRIP =
		3;
	
	int GL_LUMINANCE =
		6409;
	
	int GL_LUMINANCE_ALPHA =
		6410;
	
	int GL_MAX_ELEMENTS_INDICES =
		33001;
	
	int GL_MAX_ELEMENTS_VERTICES =
		33000;
	
	int GL_MAX_LIGHTS =
		3377;
	
	int GL_MAX_MODELVIEW_STACK_DEPTH =
		3382;
	
	int GL_MAX_PROJECTION_STACK_DEPTH =
		3384;
	
	int GL_MAX_TEXTURE_SIZE =
		3379;
	
	int GL_MAX_TEXTURE_STACK_DEPTH =
		3385;
	
	int GL_MAX_TEXTURE_UNITS =
		34018;
	
	int GL_MAX_VIEWPORT_DIMS =
		3386;
	
	int GL_MODELVIEW =
		5888;
	
	int GL_MODULATE =
		8448;
	
	int GL_MULTISAMPLE =
		32925;
	
	int GL_NAND =
		5390;
	
	int GL_NEAREST =
		9728;
	
	int GL_NEAREST_MIPMAP_LINEAR =
		9986;
	
	int GL_NEAREST_MIPMAP_NEAREST =
		9984;
	
	int GL_NEVER =
		512;
	
	int GL_NICEST =
		4354;
	
	int GL_NOOP =
		5381;
	
	int GL_NOR =
		5384;
	
	int GL_NORMALIZE =
		2977;
	
	int GL_NORMAL_ARRAY =
		32885;
	
	int GL_NOTEQUAL =
		517;
	
	int GL_NO_ERROR =
		0;
	
	int GL_NUM_COMPRESSED_TEXTURE_FORMATS =
		34466;
	
	int GL_ONE =
		1;
	
	int GL_ONE_MINUS_DST_ALPHA =
		773;
	
	int GL_ONE_MINUS_DST_COLOR =
		775;
	
	int GL_ONE_MINUS_SRC_ALPHA =
		771;
	
	int GL_ONE_MINUS_SRC_COLOR =
		769;
	
	int GL_OR =
		5383;
	
	int GL_OR_INVERTED =
		5389;
	
	int GL_OR_REVERSE =
		5387;
	
	int GL_OUT_OF_MEMORY =
		1285;
	
	int GL_PACK_ALIGNMENT =
		3333;
	
	int GL_PALETTE4_R5_G6_B5_OES =
		35730;
	
	int GL_PALETTE4_RGB5_A1_OES =
		35732;
	
	int GL_PALETTE4_RGB8_OES =
		35728;
	
	int GL_PALETTE4_RGBA4_OES =
		35731;
	
	int GL_PALETTE4_RGBA8_OES =
		35729;
	
	int GL_PALETTE8_R5_G6_B5_OES =
		35735;
	
	int GL_PALETTE8_RGB5_A1_OES =
		35737;
	
	int GL_PALETTE8_RGB8_OES =
		35733;
	
	int GL_PALETTE8_RGBA4_OES =
		35736;
	
	int GL_PALETTE8_RGBA8_OES =
		35734;
	
	int GL_PERSPECTIVE_CORRECTION_HINT =
		3152;
	
	int GL_POINTS =
		0;
	
	int GL_POINT_SMOOTH =
		2832;
	
	int GL_POINT_SMOOTH_HINT =
		3153;
	
	int GL_POLYGON_OFFSET_FILL =
		32823;
	
	int GL_POLYGON_SMOOTH_HINT =
		3155;
	
	int GL_POSITION =
		4611;
	
	int GL_PROJECTION =
		5889;
	
	int GL_QUADRATIC_ATTENUATION =
		4617;
	
	int GL_RED_BITS =
		3410;
	
	int GL_RENDERER =
		7937;
	
	int GL_REPEAT =
		10497;
	
	int GL_REPLACE =
		7681;
	
	int GL_RESCALE_NORMAL =
		32826;
	
	int GL_RGB =
		6407;
	
	int GL_RGBA =
		6408;
	
	int GL_SAMPLE_ALPHA_TO_COVERAGE =
		32926;
	
	int GL_SAMPLE_ALPHA_TO_ONE =
		32927;
	
	int GL_SAMPLE_COVERAGE =
		32928;
	
	int GL_SCISSOR_TEST =
		3089;
	
	int GL_SET =
		5391;
	
	int GL_SHININESS =
		5633;
	
	int GL_SHORT =
		5122;
	
	int GL_SMOOTH =
		7425;
	
	int GL_SMOOTH_LINE_WIDTH_RANGE =
		2850;
	
	int GL_SMOOTH_POINT_SIZE_RANGE =
		2834;
	
	int GL_SPECULAR =
		4610;
	
	int GL_SPOT_CUTOFF =
		4614;
	
	int GL_SPOT_DIRECTION =
		4612;
	
	int GL_SPOT_EXPONENT =
		4613;
	
	int GL_SRC_ALPHA =
		770;
	
	int GL_SRC_ALPHA_SATURATE =
		776;
	
	int GL_SRC_COLOR =
		768;
	
	int GL_STACK_OVERFLOW =
		1283;
	
	int GL_STACK_UNDERFLOW =
		1284;
	
	int GL_STENCIL_BITS =
		3415;
	
	int GL_STENCIL_BUFFER_BIT =
		1024;
	
	int GL_STENCIL_TEST =
		2960;
	
	int GL_SUBPIXEL_BITS =
		3408;
	
	int GL_TEXTURE =
		5890;
	
	int GL_TEXTURE0 =
		33984;
	
	int GL_TEXTURE1 =
		33985;
	
	int GL_TEXTURE10 =
		33994;
	
	int GL_TEXTURE11 =
		33995;
	
	int GL_TEXTURE12 =
		33996;
	
	int GL_TEXTURE13 =
		33997;
	
	int GL_TEXTURE14 =
		33998;
	
	int GL_TEXTURE15 =
		33999;
	
	int GL_TEXTURE16 =
		34000;
	
	int GL_TEXTURE17 =
		34001;
	
	int GL_TEXTURE18 =
		34002;
	
	int GL_TEXTURE19 =
		34003;
	
	int GL_TEXTURE2 =
		33986;
	
	int GL_TEXTURE20 =
		34004;
	
	int GL_TEXTURE21 =
		34005;
	
	int GL_TEXTURE22 =
		34006;
	
	int GL_TEXTURE23 =
		34007;
	
	int GL_TEXTURE24 =
		34008;
	
	int GL_TEXTURE25 =
		34009;
	
	int GL_TEXTURE26 =
		34010;
	
	int GL_TEXTURE27 =
		34011;
	
	int GL_TEXTURE28 =
		34012;
	
	int GL_TEXTURE29 =
		34013;
	
	int GL_TEXTURE3 =
		33987;
	
	int GL_TEXTURE30 =
		34014;
	
	int GL_TEXTURE31 =
		34015;
	
	int GL_TEXTURE4 =
		33988;
	
	int GL_TEXTURE5 =
		33989;
	
	int GL_TEXTURE6 =
		33990;
	
	int GL_TEXTURE7 =
		33991;
	
	int GL_TEXTURE8 =
		33992;
	
	int GL_TEXTURE9 =
		33993;
	
	int GL_TEXTURE_2D =
		3553;
	
	int GL_TEXTURE_COORD_ARRAY =
		32888;
	
	int GL_TEXTURE_ENV =
		8960;
	
	int GL_TEXTURE_ENV_COLOR =
		8705;
	
	int GL_TEXTURE_ENV_MODE =
		8704;
	
	int GL_TEXTURE_MAG_FILTER =
		10240;
	
	int GL_TEXTURE_MIN_FILTER =
		10241;
	
	int GL_TEXTURE_WRAP_S =
		10242;
	
	int GL_TEXTURE_WRAP_T =
		10243;
	
	int GL_TRIANGLES =
		4;
	
	int GL_TRIANGLE_FAN =
		6;
	
	int GL_TRIANGLE_STRIP =
		5;
	
	int GL_TRUE =
		1;
	
	int GL_UNPACK_ALIGNMENT =
		3317;
	
	int GL_UNSIGNED_BYTE =
		5121;
	
	int GL_UNSIGNED_SHORT =
		5123;
	
	int GL_UNSIGNED_SHORT_4_4_4_4 =
		32819;
	
	int GL_UNSIGNED_SHORT_5_5_5_1 =
		32820;
	
	int GL_UNSIGNED_SHORT_5_6_5 =
		33635;
	
	int GL_VENDOR =
		7936;
	
	int GL_VERSION =
		7938;
	
	int GL_VERTEX_ARRAY =
		32884;
	
	int GL_XOR =
		5382;
	
	int GL_ZERO =
		0;
	
	void glActiveTexture(int __a);
	
	void glAlphaFunc(int __a, float __b);
	
	void glAlphaFuncx(int __a, int __b);
	
	void glBindTexture(int __a, int __b);
	
	void glBlendFunc(int __a, int __b);
	
	void glClear(int __a);
	
	void glClearColor(float __a, float __b, float __c, float __d);
	
	void glClearColorx(int __a, int __b, int __c, int __d);
	
	void glClearDepthf(float __a);
	
	void glClearDepthx(int __a);
	
	void glClearStencil(int __a);
	
	void glClientActiveTexture(int __a);
	
	void glColor4f(float __a, float __b, float __c, float __d);
	
	void glColor4x(int __a, int __b, int __c, int __d);
	
	void glColorMask(boolean __a, boolean __b, boolean __c, boolean __d);
	
	void glColorPointer(int __a, int __b, int __c, Buffer __d);
	
	void glCompressedTexImage2D(int __a, int __b, int __c, int __d, int __e,
		int __f, int __g, Buffer __h);
	
	void glCompressedTexSubImage2D(int __a, int __b, int __c, int __d,
	 int __e,
		int __f, int __g, int __h, Buffer __i);
	
	void glCopyTexImage2D(int __a, int __b, int __c, int __d, int __e,
	 int __f,
		int __g, int __h);
	
	void glCopyTexSubImage2D(int __a, int __b, int __c, int __d, int __e,
		int __f, int __g, int __h);
	
	void glCullFace(int __a);
	
	void glDeleteTextures(int __a, int[] __b, int __c);
	
	void glDeleteTextures(int __a, IntBuffer __b);
	
	void glDepthFunc(int __a);
	
	void glDepthMask(boolean __a);
	
	void glDepthRangef(float __a, float __b);
	
	void glDepthRangex(int __a, int __b);
	
	void glDisable(int __a);
	
	void glDisableClientState(int __a);
	
	void glDrawArrays(int __a, int __b, int __c);
	
	void glDrawElements(int __a, int __b, int __c, Buffer __d);
	
	void glEnable(int __a);
	
	void glEnableClientState(int __a);
	
	void glFinish();
	
	void glFlush();
	
	void glFogf(int __a, float __b);
	
	void glFogfv(int __a, float[] __b, int __c);
	
	void glFogfv(int __a, FloatBuffer __b);
	
	void glFogx(int __a, int __b);
	
	void glFogxv(int __a, int[] __b, int __c);
	
	void glFogxv(int __a, IntBuffer __b);
	
	void glFrontFace(int __a);
	
	void glFrustumf(float __a, float __b, float __c, float __d, float __e,
		float __f);
	
	void glFrustumx(int __a, int __b, int __c, int __d, int __e, int __f);
	
	void glGenTextures(int __a, int[] __b, int __c);
	
	void glGenTextures(int __a, IntBuffer __b);
	
	int glGetError();
	
	void glGetIntegerv(int __a, int[] __b, int __c);
	
	void glGetIntegerv(int __a, IntBuffer __b);
	
	String glGetString(int __a);
	
	void glHint(int __a, int __b);
	
	void glLightModelf(int __a, float __b);
	
	void glLightModelfv(int __a, float[] __b, int __c);
	
	void glLightModelfv(int __a, FloatBuffer __b);
	
	void glLightModelx(int __a, int __b);
	
	void glLightModelxv(int __a, int[] __b, int __c);
	
	void glLightModelxv(int __a, IntBuffer __b);
	
	void glLightf(int __a, int __b, float __c);
	
	void glLightfv(int __a, int __b, float[] __c, int __d);
	
	void glLightfv(int __a, int __b, FloatBuffer __c);
	
	void glLightx(int __a, int __b, int __c);
	
	void glLightxv(int __a, int __b, int[] __c, int __d);
	
	void glLightxv(int __a, int __b, IntBuffer __c);
	
	void glLineWidth(float __a);
	
	void glLineWidthx(int __a);
	
	void glLoadIdentity();
	
	void glLoadMatrixf(float[] __a, int __b);
	
	void glLoadMatrixf(FloatBuffer __a);
	
	void glLoadMatrixx(int[] __a, int __b);
	
	void glLoadMatrixx(IntBuffer __a);
	
	void glLogicOp(int __a);
	
	void glMaterialf(int __a, int __b, float __c);
	
	void glMaterialfv(int __a, int __b, float[] __c, int __d);
	
	void glMaterialfv(int __a, int __b, FloatBuffer __c);
	
	void glMaterialx(int __a, int __b, int __c);
	
	void glMaterialxv(int __a, int __b, int[] __c, int __d);
	
	void glMaterialxv(int __a, int __b, IntBuffer __c);
	
	void glMatrixMode(int __a);
	
	void glMultMatrixf(float[] __a, int __b);
	
	void glMultMatrixf(FloatBuffer __a);
	
	void glMultMatrixx(int[] __a, int __b);
	
	void glMultMatrixx(IntBuffer __a);
	
	void glMultiTexCoord4f(int __a, float __b, float __c, float __d,
	 float __e);
	
	void glMultiTexCoord4x(int __a, int __b, int __c, int __d, int __e);
	
	void glNormal3f(float __a, float __b, float __c);
	
	void glNormal3x(int __a, int __b, int __c);
	
	void glNormalPointer(int __a, int __b, Buffer __c);
	
	void glOrthof(float __a, float __b, float __c, float __d, float __e,
		float __f);
	
	void glOrthox(int __a, int __b, int __c, int __d, int __e, int __f);
	
	void glPixelStorei(int __a, int __b);
	
	void glPointSize(float __a);
	
	void glPointSizex(int __a);
	
	void glPolygonOffset(float __a, float __b);
	
	void glPolygonOffsetx(int __a, int __b);
	
	void glPopMatrix();
	
	void glPushMatrix();
	
	void glReadPixels(int __a, int __b, int __c, int __d, int __e, int __f,
		Buffer __g);
	
	void glRotatef(float __a, float __b, float __c, float __d);
	
	void glRotatex(int __a, int __b, int __c, int __d);
	
	void glSampleCoverage(float __a, boolean __b);
	
	void glSampleCoveragex(int __a, boolean __b);
	
	void glScalef(float __a, float __b, float __c);
	
	void glScalex(int __a, int __b, int __c);
	
	void glScissor(int __a, int __b, int __c, int __d);
	
	void glShadeModel(int __a);
	
	void glStencilFunc(int __a, int __b, int __c);
	
	void glStencilMask(int __a);
	
	void glStencilOp(int __a, int __b, int __c);
	
	void glTexCoordPointer(int __a, int __b, int __c, Buffer __d);
	
	void glTexEnvf(int __a, int __b, float __c);
	
	void glTexEnvfv(int __a, int __b, float[] __c, int __d);
	
	void glTexEnvfv(int __a, int __b, FloatBuffer __c);
	
	void glTexEnvx(int __a, int __b, int __c);
	
	void glTexEnvxv(int __a, int __b, int[] __c, int __d);
	
	void glTexEnvxv(int __a, int __b, IntBuffer __c);
	
	void glTexImage2D(int __a, int __b, int __c, int __d, int __e, int __f,
		int __g, int __h, Buffer __i);
	
	void glTexParameterf(int __a, int __b, float __c);
	
	void glTexParameterx(int __a, int __b, int __c);
	
	void glTexSubImage2D(int __a, int __b, int __c, int __d, int __e, int __f,
		int __g, int __h, Buffer __i);
	
	void glTranslatef(float __a, float __b, float __c);
	
	void glTranslatex(int __a, int __b, int __c);
	
	void glVertexPointer(int __a, int __b, int __c, Buffer __d);
	
	void glViewport(int __a, int __b, int __c, int __d);
}


