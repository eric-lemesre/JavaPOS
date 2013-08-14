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

import java.util.Vector;
import java.io.*;

import java.awt.Toolkit;
import javax.swing.*;


/**
 * Utility class for the JposEntryEditor, provides utility methods for loading images,
 * number conversion and other.
 * @author Manuel M Monserrate
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (SF 2K meeting)
 */
public class JposEntryEditorUtility 
{

    //-----------------------------------------------------------------------
    // Default ctor
    //

    private JposEntryEditorUtility() {}

    //-----------------------------------------------------------------------
    // Public methods
    //

    /** 
     * @return an ImageIcon from anywhere in the classpath
     * @param imageName the String representing the image to be returned
     * @since 1.3 (SF 2K meeting)
     */
    public static ImageIcon getTreeImage( String imageName )
    {
        String imageFileName = PATHFORIMAGEFILES + imageName;

        ImageIcon treeImageIcon = null;

        try
        {
            Vector v = new Vector();

            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream( imageFileName );

            if( is == null )
                is = ClassLoader.getSystemResourceAsStream( PATHFORIMAGEFILES + "unknown.gif" );

            is = new BufferedInputStream( is );

            while( is.available() > 0 )
            {
                byte[] buffer = new byte[ is.available() ];

                is.read( buffer );

                v.addElement( buffer );
            }

            int size = 0;

            for( int i = 0; i < v.size(); ++i )
            {
                byte[] buffer = (byte[])v.elementAt( i );

                size += buffer.length;

            }

            byte[] bigBuffer = new byte[ size ];
            int dstPos = 0;

            for( int i = 0; i < v.size(); ++i )
            {
                byte[] buffer = (byte[])v.elementAt( i );

                System.arraycopy( buffer, 0, bigBuffer, dstPos, buffer.length );
                
                dstPos += buffer.length;
            }
           
            treeImageIcon = new ImageIcon( Toolkit.getDefaultToolkit().createImage( bigBuffer ) );
            
        }
        catch( IOException ioe ) { treeImageIcon = null; }

        return treeImageIcon;
    } 


    /**
     * reads an String representation of an Integer, if the showAsHexFlag is set, it converts it to hex
     * to a hex string representation for display
     * @param value the string to format
     * @param showAsHexFlag the boolean flag that determines wether to display a number as hex
     * @return a String to be displayed in the text field
     */
    public static String formatText( String value, boolean showAsHexFlag )
    {
        try
        {
            int numberToCheck = Integer.parseInt( value );
            
            if( showAsHexFlag )
            {
                StringBuffer buffer = new StringBuffer();
                buffer.append( "0x" );
                buffer.append (Integer.toHexString( numberToCheck ) );

                value = buffer.toString();
            }
        }
        catch( NumberFormatException nfe) {}

        return value;
    }

    /**
     * reads an String representation of an Integer, if the showAsHexFlag is set, it attemps to read
     * the hex number and convert it to the string representation of the integer for storage
     * @param value the string to format
     * @param showAsHexFlag the boolean flag that determines wether to display a number as hex
     * @return a String to be stored
     */
    public static String readHex( String value, boolean showAsHexFlag )
    {
        String originalValue = value;

        if( showAsHexFlag )
        {
            if( value.substring( 0, 2 ).equalsIgnoreCase( "0x" ) )
                value = value.substring(2);
            else
                return value;

            int length = value.length();
            String temp = "";
            int intValue = 0;
            int tempIntValue = 0;
            int count = 0;


            while ( length > 0 ) 
            {
                tempIntValue = 0;
                char charToCheck = value.charAt( length - 1 );

                try
                {

                    switch( charToCheck )
                    {
                    case '0': case '1': case '2': case '3': case '4':
                    case '5': case '6': case '7': case '8': case '9':
                        char[] tempChar = { charToCheck };
                        tempIntValue = Integer.parseInt( new String( tempChar ) );
                        break;
                    case 'a': case 'A':
                        tempIntValue = 10;
                        break;
                    case 'b': case 'B':
                        tempIntValue = 11;
                        break;
                    case 'c': case 'C':
                        tempIntValue = 12;
                        break;
                    case 'd': case 'D':
                        tempIntValue = 13;
                        break;
                    case 'e': case 'E':
                        tempIntValue = 14;
                        break;
                    case 'f': case 'F':
                        tempIntValue = 15;
                        break;
                    default:
                        value = originalValue;
                        return value;
                    }
                }
                catch( NumberFormatException nfe ) 
                {
                    value = originalValue;
                    return value;
                }
                
                intValue = (int)(intValue + (tempIntValue * Math.pow( 16, count )));
                
                length--;
                count++;
            }
                
            value = String.valueOf( intValue );

        }
        else
        {
            if( value.substring( 0, 2 ).equalsIgnoreCase( "0x" ) )
                JOptionPane.showMessageDialog( null, "In order to input numbers as hex, you need to select\nthe 'Show numbers as hex' option in Preferences.", "Warning", JOptionPane.WARNING_MESSAGE );
        }

        return value;
    } 

    //-----------------------------------------------------------------------
    // Class constants
    //

    public static final String PATHFORIMAGEFILES = "jpos/res/images/";

}



