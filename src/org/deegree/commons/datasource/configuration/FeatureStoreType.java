//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-792 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.12.10 at 02:59:01 PM MEZ 
//


package org.deegree.commons.datasource.configuration;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FeatureStoreType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FeatureStoreType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.deegree.org/datasource}AbstractDataSourceType">
 *       &lt;sequence>
 *         &lt;element name="NamespaceHint" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="prefix" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="namespaceURI" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FeatureStoreType", propOrder = {
    "namespaceHint"
})
@XmlSeeAlso({
    ShapefileDataSourceType.class,
    PostGISFeatureStoreType.class,
    FeatureStoreReferenceType.class,
    MemoryFeatureStoreType.class,
    DirectSQLDataSourceType.class
})
public abstract class FeatureStoreType
    extends AbstractDataSourceType
{

    @XmlElement(name = "NamespaceHint")
    protected List<FeatureStoreType.NamespaceHint> namespaceHint;

    /**
     * Gets the value of the namespaceHint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the namespaceHint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNamespaceHint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FeatureStoreType.NamespaceHint }
     * 
     * 
     */
    public List<FeatureStoreType.NamespaceHint> getNamespaceHint() {
        if (namespaceHint == null) {
            namespaceHint = new ArrayList<FeatureStoreType.NamespaceHint>();
        }
        return this.namespaceHint;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="prefix" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="namespaceURI" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class NamespaceHint {

        @XmlAttribute(required = true)
        protected String prefix;
        @XmlAttribute(required = true)
        protected String namespaceURI;

        /**
         * Gets the value of the prefix property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * Sets the value of the prefix property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPrefix(String value) {
            this.prefix = value;
        }

        /**
         * Gets the value of the namespaceURI property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNamespaceURI() {
            return namespaceURI;
        }

        /**
         * Sets the value of the namespaceURI property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNamespaceURI(String value) {
            this.namespaceURI = value;
        }

    }

}
