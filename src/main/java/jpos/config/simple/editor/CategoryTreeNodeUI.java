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
 * implementation of tree nodes by device Category
 * @author Manuel M Monserrate
 * @author E. Michael Maximilien
 * @since 1.3 (SF 2K meeting)
 */
public class CategoryTreeNodeUI extends AbstractTreeNodeUI
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	// 

    /**
     * default ctor
     * @param devCat the device category String
     */
    public CategoryTreeNodeUI( String devCat ) { deviceCategory = devCat; }

    //---------------------------------------------------------------------------
    // Private methods
    //

    /** @return the DevCat name for a device Category's name to display it correctly */ 
    private String lookUpDevName( String devCat )
    {
        if( devCat.equalsIgnoreCase( "bumpbar" ) )
            return "BumpBar";
        else if( devCat.equalsIgnoreCase( "cashchanger" ) )
            return "CashChanger";
        else if( devCat.equalsIgnoreCase( "cashdrawer" ) )
            return "CashDrawer";
        else if( devCat.equalsIgnoreCase( "coindispenser" ) )
            return "CoinDispenser";
        else if( devCat.equalsIgnoreCase( "fiscalprinter" ) )
            return "FiscalPrinter";
        else if( devCat.equalsIgnoreCase( "hardtotals" ) )
            return "HardTotals";
        else if( devCat.equalsIgnoreCase( "keylock" ) )
            return "KeyLock";
        else if( devCat.equalsIgnoreCase( "linedisplay" ) )
            return "LineDisplay";
        else if( devCat.equalsIgnoreCase( "micr" ) )
            return "MICR";
        else if( devCat.equalsIgnoreCase( "msr" ) )
            return "MSR";
        else if( devCat.equalsIgnoreCase( "pinpad" ) )
            return "PinPad";
        else if( devCat.equalsIgnoreCase( "poskeyboard" ) )
            return "POSKeyboard";
        else if( devCat.equalsIgnoreCase( "posprinter" ) )
            return "POSPrinter";
        else if( devCat.equalsIgnoreCase( "remoteorderdisplay" ) )
            return "RemoteOrderDisplay";
        else if( devCat.equalsIgnoreCase( "scale" ) )
            return "Scale";
        else if( devCat.equalsIgnoreCase( "scanner" ) )
            return "Scanner";
        else if( devCat.equalsIgnoreCase( "signaturecapture" ) )
            return "SignatureCapture";
        else if( devCat.equalsIgnoreCase( "toneindicator" ) )
            return "ToneIndicator";
        
        return "Unknown";
    }

    /** @return the logical name of the JposEntry in this tree Node */
    public String getName() { return toString(); }

    /** @return the ImageIcon for drawing the node */
    public ImageIcon getImageIcon()
    {
		if( treeImageIcon == null )
		{
			String imageName = deviceCategory.toLowerCase() + ".gif";

			treeImageIcon = JposEntryEditorUtility.getTreeImage( imageName );
		}

        return treeImageIcon;
    }

    /**
     * @return string representation of this TreeNodeUI so that the correct
     * name can be viewed in the user interface
     */
    public String toString() { return lookUpDevName( deviceCategory ); }

    /** @return the text to display as tool tip text in the tree view */
    public String getToolTipText() { return CATEGORY_TTTEXT_STRING; }

    //---------------------------------------------------------------------------
    // Instance variables
    //

    private String deviceCategory = "Unknown";

    //---------------------------------------------------------------------------
    // Class constants
    //

	//<i18n>
	public static final String CATEGORY_TTTEXT_STRING = "JavaPOS categories";
	//</i18n>
}
