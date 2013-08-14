package jpos.config.simple.editor;

///////////////////////////////////////////////////////////////////////////////
//
// This software is provided "AS IS".  The JavaPOS working group (including
// each of the Corporate members, contributors and individuals)  MAKES NO
// REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED 
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
// NON-INFRINGEMENT. The JavaPOS working group shall not be liable for
// any damages suffered as a result of using, modifying or distributing this
// software or its derivatives. Permission to use, copy, modify, and distribute
// the software and its documentation for any purpose is hereby granted. 
//
// The JavaPOS Config/Loader (aka JCL) is now under the CPL license, which 
// is an OSS Apache-like license.  The complete license is located at:
//    http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
//
///////////////////////////////////////////////////////////////////////////////

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Simple filter class to accecpt xml and cfg files
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (SF 2K meeting)
 */
class JposEntryEditorFileFilter extends FileFilter
{
    /**
     * Tests whether or not the specified abstract pathname should be
     * included in a pathname list.
     * @param  pathname  The abstract pathname to be tested
     * @return  <code>true</code> if and only if <code>pathname</code>
     *          should be included
     */
    public boolean accept( File pathname ) 
	{
        String filename = pathname.getName();
        
        if( pathname.isDirectory() )
            return true;

        if( mode == XML_MODE )
			return filename.endsWith( ".xml" );
        else
            return filename.endsWith( ".cfg" );
    }

	/** @return the description for this FileFilter */
    public String getDescription() 
	{
        return ( ( mode == XML_MODE ) ? XML_ENTRIES_STRING : 
        								 SERIALIZED_ENTRIES_STRING );
    }

	/**
	 * Sets the mode for the filter e.g XML_MODE or SER_MODE
	 * @param i the mode constant
	 */
	public void setMode( int i )
	{
		if( i == XML_MODE || i == SER_MODE )
			mode = i;
		else
			i = XML_MODE;
	}

    //---------------------------------------------------------------------
    // Instance variables
    //

	private int mode = XML_MODE;

    //---------------------------------------------------------------------
    // Class constants
    //

	public static final int XML_MODE = 0;
	public static final int SER_MODE = 1;

	//<i18n>
	public static final String XML_ENTRIES_STRING = "XML entries (*.xml)";
	public static final String SERIALIZED_ENTRIES_STRING = "Serialized entries (*.cfg)";
	//</i18n>
}
