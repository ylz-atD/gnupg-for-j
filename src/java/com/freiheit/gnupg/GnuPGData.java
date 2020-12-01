/*
 * $Id: GnuPGData.java,v 1.1 2005/01/24 13:56:58 stefan Exp $
 * (c) Copyright 2005 freiheit.com technologies gmbh, Germany.
 *
 * This file is part of Java for GnuPG  (http://www.freiheit.com).
 *
 * Java for GnuPG is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package com.freiheit.gnupg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
   Holds the data that you want to work on and stores the results
   of crypto operations.

   @author Stefan Richter, stefan@freiheit.com
 */
public class GnuPGData extends GnuPGPeer{

    /**
       Use the factory methods from GnuPGContext to
       generate GnuPGData-Objects.
       Generates an empty data object. Use this, if you want
       to create a data object to hold a result from a crypto
       operation.
     */
    protected GnuPGData(){
        setInternalRepresentation(gpgmeDataNew());
    }

    /*
       Use the factory methods from GnuPGContext to
       generate GnuPGData-Objects.
       FIXME: This is not working! Use it, and it will crash the JVM.
    */
//     public GnuPGData(File f) throws IOException{
//         this();
//         if(f != null && f.exists() && !f.isDirectory() && f.canRead()){
//             FileInputStream fin = new FileInputStream(f);
//             this.read(fin);
//         }
//     }

    /**
       Use the factory methods from GnuPGContext to
       generate GnuPGData-Objects.
       Generates a new data object containing the given String.

       @param str your string
     */
    protected GnuPGData(String str){
        this(str.getBytes());
    }

    /**
       Use the factory methods from GnuPGContext to
       generate GnuPGData-Objects.
       Generates a new Data-Object containing the given byte array.

       @param data your data
     */
    protected GnuPGData(byte[] data){
        long gpgmeDataNewFromMem = gpgmeDataNewFromMem(data);
        setInternalRepresentation(gpgmeDataNewFromMem);
    }


    /*
       FIXME: This is not working! Use it, and it will crash the JVM.
    */
//     public void read(InputStream in) throws IOException{
//         if(in != null){
//             gpgmeDataRead(getInternalRepresentation(), in);
//         }
//     }

    /**
       Writes the data/string contained in this data object
       to the given Java OutputStream.

       @param out OutputStream, where the data/string should be written
     */
    public void write(OutputStream out) throws IOException{
        if (out != null) {
            gpgmeDataWrite(getInternalRepresentation(), out);
        }
    }

    /**
       Helper method to print out the data/string from this data object.

       @return String representation of the data contained in this data object (expect weired results with binary data)
     */
    public String toString(){
        String result = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            this.write(baos);
            result = baos.toString();
        }
        catch(IOException ioe){
            result = ioe.getMessage();
        }
        return result;
    }

    /**
       This calls immediately the release method for the datastructure
       in the underlying gpgme library. This method is called by
       the finalizer of the class anyway, but you can call it yourself
       if you want to get rid of this datastructure at once and don't want to
       wait for the non-deterministic garbage-collector of the JVM.
    */
    public void destroy(){
        if(getInternalRepresentation() != 0){
            gpgmeDataRelease(getInternalRepresentation());
            setInternalRepresentation(0);
        }
    }

    /**
       Releases underlying datastructures. Simple calls the destroy() method.
     */
    protected void finalize(){
        destroy();
    }

    private native long gpgmeDataNewFromMem(byte[] plain);
    private native long gpgmeDataNew();
    private native void gpgmeDataWrite(long l, OutputStream out) throws IOException;
    private native void gpgmeDataRelease(long l);
    @SuppressWarnings("unused")
    private native void gpgmeDataRead(long data, InputStream in) throws IOException;
}
/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
