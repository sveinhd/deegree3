//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-792 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.12.08 at 05:30:06 PM CET 
//


package org.deegree.commons.datasource.configuration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *         A fileset type defines a file pattern to search for in a given directory, as well if it should search recursively in the given directory.
 *       
 * 
 * <p>Java class for FileSetType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FileSetType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.deegree.org/datasource>FileType">
 *       &lt;attribute name="filePattern" type="{http://www.w3.org/2001/XMLSchema}string" default="*" />
 *       &lt;attribute name="recursive" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FileSetType")
@XmlSeeAlso({
    RasterFileSetType.class
})
public class FileSetType
    extends FileType
{

    @XmlAttribute
    protected String filePattern;
    @XmlAttribute
    protected Boolean recursive;

    /**
     * Gets the value of the filePattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilePattern() {
        if (filePattern == null) {
            return "*";
        } else {
            return filePattern;
        }
    }

    /**
     * Sets the value of the filePattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilePattern(String value) {
        this.filePattern = value;
    }

    /**
     * Gets the value of the recursive property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRecursive() {
        return recursive;
    }

    /**
     * Sets the value of the recursive property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRecursive(Boolean value) {
        this.recursive = value;
    }

}
