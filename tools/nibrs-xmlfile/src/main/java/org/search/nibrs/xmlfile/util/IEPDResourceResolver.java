/*
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.search.nibrs.xmlfile.util;

import java.io.InputStream;
import java.io.Reader;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * A DOM Resource Resolver implementation that helps with reading schemas out of IEPDs.
 *
 */
public class IEPDResourceResolver implements LSResourceResolver {
        
    protected String schemaRootFolderName;
    protected String iepdRootPath;
    
    public IEPDResourceResolver(String schemaRootFolderName, String iepdRootPath){
    	
        this.schemaRootFolderName = schemaRootFolderName;
        this.iepdRootPath = iepdRootPath;
    }

    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
    	
        String fullPath = reformatResourcePath(systemId, baseURI);
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(fullPath);
        return new LSInputImpl(publicId, systemId, resourceAsStream);
    }

    /**
     * Reformat the resource path as necessary.  The default removes "../" portions of the relative path references (since they just refer back to the root).  Subclasses
     * are free to override to do something different if needed.
     * @param systemId the unformatted relative path (from the iepd root)
     * @return the reformatted path
     */
    protected String reformatResourcePath(String systemId, String baseURI) {
    	
        String doctoredSystemId = systemId;
        if (systemId.contains("../"))
        {
            // this will work with NIEM and this specific IEPD...needs to be changed for IEPDs with other structures...
            doctoredSystemId = schemaRootFolderName + "/" + systemId.replaceAll("\\.\\./", "");
        }
        if (!iepdRootPath.endsWith("/"))
        {
            iepdRootPath = iepdRootPath + "/";
        }
        String fullPath = iepdRootPath + doctoredSystemId;
        if (fullPath.contains("/./")) {
        	fullPath = fullPath.replaceAll("/\\./", "/");
        }
        return fullPath;
    }

    public static final class LSInputImpl implements LSInput {

        private String publicId;

        private String systemId;

        public String getPublicId() {
            return publicId;
        }

        public void setPublicId(String publicId) {
            this.publicId = publicId;
        }

        @Override
        public String getBaseURI() {
            return null;
        }

        @Override
        public InputStream getByteStream() {
            return inputStream;
        }

        @Override
        public boolean getCertifiedText() {
            return false;
        }

        @Override
        public Reader getCharacterStream() {
            return null;
        }

        @Override
        public String getEncoding() {
            return null;
        }

        @Override
        public String getStringData() {        	        	
        	return null;
        }

        @Override
        public void setBaseURI(String baseURI) {
        }

        @Override
        public void setByteStream(InputStream byteStream) {
        }

        @Override
        public void setCertifiedText(boolean certifiedText) {
        }

        @Override
        public void setCharacterStream(Reader characterStream) {
        }

        @Override
        public void setEncoding(String encoding) {
        }

        @Override
        public void setStringData(String stringData) {
        }

        public String getSystemId() {
            return systemId;
        }

        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }
        
        private InputStream inputStream; 

        public LSInputImpl(String publicId, String sysId, InputStream inputStream) {
            this.publicId = publicId;
            this.systemId = sysId;            
            this.inputStream = inputStream;
        }
    }
}
