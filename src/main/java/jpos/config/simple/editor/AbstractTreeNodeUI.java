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

import javax.swing.ImageIcon;

/**
 * Common methods and variables for different types of classes that implement 
 * the TreeNodeUI interface
 * @author Manuel M Monserrate
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (SF 2K meeting)
 */
public abstract class AbstractTreeNodeUI implements TreeNodeUI
{
    //---------------------------------------------------------------------------
    // Public methods
    //

    /** @return the JposEntry's logical name for the JposEntry inside the TreeNodeUI object */
    public abstract String getName();

    /** @return the ImageIcon for displaying the node on the tree */
    public abstract ImageIcon getImageIcon();

    /** @return the String representation of this object */
    public abstract String toString();

    /** @return the text to display as tool tip text in the tree view */
    public abstract String getToolTipText();

    //---------------------------------------------------------------------------
    // Instance variables
    //
    
    protected ImageIcon treeImageIcon;
}
