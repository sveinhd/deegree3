//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-792 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.12.10 at 02:59:01 PM MEZ 
//


package org.deegree.commons.datasource.configuration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *         A web based data source, sends a request to a service some were in the web using the http protocol. The CapabilitesDocument location defines
 *         the location of the service, refresh can be true if the capabilities should not be cached and/or evaluated anew.
 *       
 * 
 * <p>Java class for AbstractWebBasedDataSourceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AbstractWebBasedDataSourceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.deegree.org/datasource}AbstractGeospatialDataSourceType">
 *       &lt;sequence>
 *         &lt;element name="CapabilitiesDocumentLocation">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="location" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="refreshTime" type="{http://www.w3.org/2001/XMLSchema}int" default="-1" />
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
@XmlType(name = "AbstractWebBasedDataSourceType", propOrder = {
    "capabilitiesDocumentLocation"
})
@XmlSeeAlso({
    WMSDataSourceType.class
})
public abstract class AbstractWebBasedDataSourceType
    extends AbstractGeospatialDataSourceType
{

    @XmlElement(name = "CapabilitiesDocumentLocation", required = true)
    protected AbstractWebBasedDataSourceType.CapabilitiesDocumentLocation capabilitiesDocumentLocation;

    /**
     * Gets the value of the capabilitiesDocumentLocation property.
     * 
     * @return
     *     possible object is
     *     {@link AbstractWebBasedDataSourceType.CapabilitiesDocumentLocation }
     *     
     */
    public AbstractWebBasedDataSourceType.CapabilitiesDocumentLocation getCapabilitiesDocumentLocation() {
        return capabilitiesDocumentLocation;
    }

    /**
     * Sets the value of the capabilitiesDocumentLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link AbstractWebBasedDataSourceType.CapabilitiesDocumentLocation }
     *     
     */
    public void setCapabilitiesDocumentLocation(AbstractWebBasedDataSourceType.CapabilitiesDocumentLocation value) {
        this.capabilitiesDocumentLocation = value;
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
     *       &lt;attribute name="location" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="refreshTime" type="{http://www.w3.org/2001/XMLSchema}int" default="-1" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class CapabilitiesDocumentLocation {

        @XmlAttribute(required = true)
        protected String location;
        @XmlAttribute
        protected Integer refreshTime;

        /**
         * Gets the value of the location property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLocation() {
            return location;
        }

        /**
         * Sets the value of the location property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLocation(String value) {
            this.location = value;
        }

        /**
         * Gets the value of the refreshTime property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public int getRefreshTime() {
            if (refreshTime == null) {
                return -1;
            } else {
                return refreshTime;
            }
        }

        /**
         * Sets the value of the refreshTime property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setRefreshTime(Integer value) {
            this.refreshTime = value;
        }

    }

}
