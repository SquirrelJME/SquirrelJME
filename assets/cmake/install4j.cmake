# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Install4J installers and portables

# Locate the Install4J builder
find_program(Install4JC_EXECUTABLE
	NAMES install4jc)

# Boolean check
if(Install4JC_EXECUTABLE)
	set(SQUIRRELJME_HAS_INSTALL4JC YES)
else()
	set(SQUIRRELJME_HAS_INSTALL4JC NO)
endif()

# Install4J Media IDs and related files
define_property(GLOBAL PROPERTY SQUIRRELJME_INSTALL4J_MEDIA_IDS
	BRIEF_DOCS "Install4J Media IDs."
	FULL_DOCS "Install4J Media IDs.")
define_property(GLOBAL PROPERTY SQUIRRELJME_INSTALL4J_BASE_NAMES
	BRIEF_DOCS "Install4J base names."
	FULL_DOCS "Install4J base names.")

# Emit locations of Install4J
message(STATUS "install4jc: ${Install4JC_EXECUTABLE}")

# Build all install4j bundles together as a single bundle? This is so they
# share the same update.xml and otherwise
option(SQUIRRELJME_INSTALL4J_BUNDLE
	"Build all Install4J installers as a single bundle." YES)

# Utilities used to convert icons
find_program(convert_EXECUTABLE
	NAMES "convert")
find_program(uudecode_EXECUTABLE
	NAMES "uudecode")
find_program(xpmtoppm_EXECUTABLE
	NAMES "xpmtoppm")
find_program(pnmtopng_EXECUTABLE
	NAMES "pnmtopng")

# Can we convert icons?
if(uudecode_EXECUTABLE OR convert_EXECUTABLE OR
	(xpmtoppm_EXECUTABLE AND pnmtopng_EXECUTABLE))
	set(SQUIRRELJME_CAN_CONVERT_ICONS YES)
else()
	set(SQUIRRELJME_CAN_CONVERT_ICONS NO)
endif()

# Install4j output directory
set(SQUIRRELJME_INSTALL4J_DIR "${CMAKE_BINARY_DIR}/install4j")
file(MAKE_DIRECTORY "${SQUIRRELJME_INSTALL4J_DIR}")
file(TO_NATIVE_PATH "${SQUIRRELJME_INSTALL4J_DIR}"
	SQUIRRELJME_INSTALL4J_DIR_NATIVE)

# Missing directory
set(SQUIRRELJME_INSTALL4J_MISSING "${CMAKE_BINARY_DIR}/install4j.missing")
file(MAKE_DIRECTORY "${SQUIRRELJME_INSTALL4J_MISSING}")
file(TO_NATIVE_PATH "${SQUIRRELJME_INSTALL4J_MISSING}"
	SQUIRRELJME_INSTALL4J_MISSING_NATIVE)

# Icon output directory
set(SQUIRRELJME_ICONS_DIR "${CMAKE_BINARY_DIR}/icons")
file(MAKE_DIRECTORY "${SQUIRRELJME_ICONS_DIR}")
file(TO_NATIVE_PATH "${SQUIRRELJME_ICONS_DIR}"
	SQUIRRELJME_ICONS_DIR_NATIVE)

# Add base target for all icons
add_custom_target(icon)

# Input icons
list(APPEND SQUIRRELJME_ICONS
	"head_8x8"
	"head_16x16"
	"head_24x24"
	"head_32x32"
	"head_48x48"
	"head_64x64"
	"head_128x128")

# Process and decode/convert each icon
foreach(xpmIcon IN LISTS SQUIRRELJME_ICONS)
	# Input (XPM)
	set(inXpm "${CMAKE_SOURCE_DIR}/assets/mascot/${xpmIcon}.xpm")
	file(TO_NATIVE_PATH "${inXpm}" inXpmNative)

	# Input (MIME)
	set(inMime "${CMAKE_SOURCE_DIR}/assets/mascot/${xpmIcon}.png.__mime")
	file(TO_NATIVE_PATH "${inMime}" inMimeNative)

	# Middle (PPM)
	set(midPpm "${CMAKE_BINARY_DIR}/${xpmIcon}.ppm")
	file(TO_NATIVE_PATH "${midPpm}" midPpmNative)

	# Output
	set(outIcon "${SQUIRRELJME_ICONS_DIR}/${xpmIcon}.png")
	file(TO_NATIVE_PATH "${outIcon}" outIconNative)

	# Determine target name
	set(target "icon.${xpmIcon}")

	# Decode PNGs?
	if(uudecode_EXECUTABLE)
		add_custom_target(${target}
			COMMAND "${uudecode_EXECUTABLE}"
				"-o" "${outIconNative}" "${inMimeNative}"
			COMMENT "Decoding ${inMime} -> ${outIcon}"
			SOURCES "${inMime}"
			BYPRODUCTS "${outIcon}")

	# Round trip conversion
	elseif(xpmtoppm_EXECUTABLE AND pnmtopng_EXECUTABLE)
		add_custom_target(${target}
			COMMAND "${xpmtoppm_EXECUTABLE}"
				"--alphaout=${midPpmNative}.alpha"
				"${inXpmNative}" ">" "${midPpmNative}"
			COMMAND "${pnmtopng_EXECUTABLE}"
				"-alpha=${midPpmNative}.alpha"
				"${midPpmNative}" ">" "${outIconNative}"
			COMMENT "Converting ${inXpm} -> ${midPpm}(.alpha) -> ${outIcon}"
			SOURCES "${inIcon}"
			VERBATIM
			BYPRODUCTS "${outIcon}" "${midPpm}" "${midPpm}.alpha")

	# Use imagemagick?
	elseif(convert_EXECUTABLE)
		add_custom_target(${target}
			COMMAND "${convert_EXECUTABLE}"
				"${inXpmNative}" "${outIconNative}"
			COMMENT "Converting ${inXpm} -> ${outIcon}"
			SOURCES "${inIcon}"
			BYPRODUCTS "${outIcon}")
	endif()

	# All icons depend on this
	if(uudecode_EXECUTABLE OR convert_EXECUTABLE OR
		(xpmtoppm_EXECUTABLE AND pnmtopng_EXECUTABLE))
		add_dependencies(icon ${target})
	endif()
endforeach()

# Registers an Install4J Media
function(squirreljme_install4j_register mediaId baseName)
	# Get properties
	get_property(mediaIds GLOBAL PROPERTY SQUIRRELJME_INSTALL4J_MEDIA_IDS)
	get_property(baseNames GLOBAL PROPERTY SQUIRRELJME_INSTALL4J_BASE_NAMES)
	if(NOT "${mediaIds}" STREQUAL "mediaIds-NOTFOUND")
		# Add to the list
		list(APPEND mediaIds "${mediaId}")
		list(APPEND baseNames "${baseName}")

		# Make sure everything is unique!
		list(SORT mediaIds)
		list(REMOVE_DUPLICATES mediaIds)
		list(SORT baseNames)
		list(REMOVE_DUPLICATES baseNames)

		# Store
		set_property(GLOBAL PROPERTY SQUIRRELJME_INSTALL4J_MEDIA_IDS
			"${mediaIds}")
		set_property(GLOBAL PROPERTY SQUIRRELJME_INSTALL4J_BASE_NAMES
			"${baseNames}")
	else()
		set_property(GLOBAL PROPERTY SQUIRRELJME_INSTALL4J_MEDIA_IDS
			"${mediaId}")
		set_property(GLOBAL PROPERTY SQUIRRELJME_INSTALL4J_BASE_NAMES
			"${baseName}")
	endif()
endfunction()

# Get the systems that are part of the merge set
get_property(systemSet GLOBAL PROPERTY SQUIRRELJME_STANDALONE_SYSTEM_SET)
foreach(systemMap IN LISTS systemSet)
	# Which System and architecture?
	squirreljme_unmap(systemNormal 0 "${systemMap}")
	squirreljme_unmap(archNormal 1 "${systemMap}")

	# Windows installer and its portable
	if("${systemNormal}" STREQUAL "windows" AND
		("${archNormal}" STREQUAL "ia32" OR
		"${archNormal}" STREQUAL "amd64"))
		# Installer
		squirreljme_install4j_register(28
			"squirreljme_windows-x64_${SQUIRRELJME_VERSION_UNDER}.exe")

		# Portable
		squirreljme_install4j_register(128
			"squirreljme_windows-x64_${SQUIRRELJME_VERSION_UNDER}.zip")

	# Debian Package and Generic Linux RPM
	elseif("${systemNormal}" STREQUAL "linux" AND
		("${archNormal}" STREQUAL "ia32" OR
		"${archNormal}" STREQUAL "amd64"))
		# Generic RPM, which should have both
		squirreljme_install4j_register(129
			"squirreljme_linux_${SQUIRRELJME_VERSION_UNDER}.rpm")

		# Debian packages
		if("${archNormal}" STREQUAL "ia32")
			squirreljme_install4j_register(148
				"squirreljme_linux-i386_${SQUIRRELJME_VERSION_UNDER}.deb")
		else()
			squirreljme_install4j_register(130
				"squirreljme_linux-amd64_${SQUIRRELJME_VERSION_UNDER}.deb")
		endif()

	# Debian ARM32 Package
	elseif("${systemNormal}" STREQUAL "linux" AND
		"${archNormal}" STREQUAL "arm32l")
		squirreljme_install4j_register(143
			"squirreljme_linux-armel_${SQUIRRELJME_VERSION_UNDER}.deb")

	# Debian ARM64 Package
	elseif("${systemNormal}" STREQUAL "linux" AND
		"${archNormal}" STREQUAL "arm64l")
		squirreljme_install4j_register(146
			"squirreljme_linux-aarch64_${SQUIRRELJME_VERSION_UNDER}.deb")

	# Solaris 64-bit Package
	elseif("${systemNormal}" STREQUAL "solaris" AND
		"${archNormal}" STREQUAL "amd64")
		squirreljme_install4j_register(31
			"squirreljme_solaris-amd64_${SQUIRRELJME_VERSION_UNDER}.sh")

	# macOS Universal (hopefully) folder
	elseif("${systemNormal}" STREQUAL "macosx" AND
		("${archNormal}" STREQUAL "powerpc" OR
		"${archNormal}" STREQUAL "ia32" OR
		"${archNormal}" STREQUAL "amd64" OR
		"${archNormal}" STREQUAL "arm64l"))
		squirreljme_install4j_register(32
			"squirreljme_macos_${SQUIRRELJME_VERSION_UNDER}.dmg")
	endif()
endforeach()

# Only create the Install4J target if it is available
# And if we can actually make the Standalone Jar
if(Install4JC_EXECUTABLE AND TARGET standalone.jar)
	# Get everything to be built
	get_property(mediaIds GLOBAL PROPERTY SQUIRRELJME_INSTALL4J_MEDIA_IDS)
	get_property(baseNames GLOBAL PROPERTY SQUIRRELJME_INSTALL4J_BASE_NAMES)

	# Install4J requires a comma separated list
	string(REPLACE ";" "," mediaIdsComma "${mediaIds}")

	# Determine full paths to the output
	set(fullPaths)
	foreach(baseName IN LISTS baseNames)
		list(APPEND fullPaths "${SQUIRRELJME_INSTALL4J_DIR}/${baseName}")
	endforeach()

	# There is an updates.xml as well! This is generated by Install4J to show
	# that files have been updated!
	list(APPEND fullPaths "${SQUIRRELJME_INSTALL4J_DIR}/updates.xml")

	# Get the native path to the Standalone Jar
	get_target_property(jarPath standalone.jar SQUIRRELJME_OUTPUT_PATH)
	file(TO_NATIVE_PATH jarPath "${jarPath}")

	# Able to include the bundled sources?
	if(TARGET sourceZip)
		get_target_property(sourcePath sourceZip SQUIRRELJME_OUTPUT_PATH)
		file(TO_NATIVE_PATH sourcePath "${sourcePath}")
	else()
		set(sourcePath "${SQUIRRELJME_INSTALL4J_MISSING_NATIVE}")
	endif()

	# Install4J makes an XML as well, so everything needs to be uploaded
	# together as a single unit!
	add_custom_target(install4j
		COMMAND "${CMAKE_COMMAND}" "-E"
			"make_directory" "${SQUIRRELJME_INSTALL4J_DIR}"
		COMMAND "${Install4JC_EXECUTABLE}"
			"-D" "squirreljme.standalone.path=${jarPath}"
			"-D" "squirreljme.source.path=${sourcePath}"
			"-D" "squirreljme.icon.path=${SQUIRRELJME_ICONS_DIR_NATIVE}"
			"-r" "${SQUIRRELJME_VERSION}"
			"-d" "${SQUIRRELJME_INSTALL4J_DIR_NATIVE}"
			"-b" "${mediaIdsComma}"
			"${CMAKE_SOURCE_DIR}/squirreljme.install4j"
		DEPENDS standalone.jar
		BYPRODUCTS "${fullPaths}"
		SOURCES "${CMAKE_SOURCE_DIR}/squirreljme.install4j"
		COMMAND_EXPAND_LISTS)

	# Depend on sources?
	if(TARGET sourceZip)
		add_dependencies(install4j sourceZip)
	endif()

	# Depend on icons?
	if(TARGET icon)
		add_dependencies(install4j icon)
	endif()

	# Properties for uploading later
	set_target_properties(install4j PROPERTIES
		SQUIRRELJME_OUTPUT_PATH "${fullPaths}"
		SQUIRRELJME_OUTPUT_TYPE "install4j")

	# Enable the upload
	squirreljme_fossil_upload(install4j)
endif()
