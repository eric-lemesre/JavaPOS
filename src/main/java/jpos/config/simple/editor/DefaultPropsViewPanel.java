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

import jpos.config.*;

/**
 * This is a default empty PropsViewPanel
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (Washington DC 2001 meeting)
 */
class DefaultPropsViewPanel extends AbstractPropsViewPanel
{
    /** Default ctor */
    public DefaultPropsViewPanel() {}

    //--------------------------------------------------------------------------
    // Public intance methods
    //

    /** Clears all the JTextField to "" */
    public void clearAll() {}

    /**
     * Makes all JTextField enabled
     * @param b the boolean param
     */
    public void setEnabledAll( boolean b ) {}

    /**
     * Makes all JTextField editable
     * @param b the boolean param
     */
    public void setEditableAll( boolean b ) {}

    /**
     * Sets the current JposEntry for this panel 
     * @param jposEntry the JposEntry
     */
    public void setJposEntry( JposEntry entry ) {}

    /** Called when this PropsViewPanel about to loose focus */
    public void aboutToLooseFocus() {}

    /** Called when the "Edit" command button is clicked */
    public void editButtonClicked() {}

    /** Called when the "OK" command button is clicked */
    public void okButtonClicked() {}

    /** Called when the "OK" command button is clicked */
    public void cancelButtonClicked() {}
}
