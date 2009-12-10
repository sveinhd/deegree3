//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-792 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.12.10 at 02:59:01 PM MEZ 
//


package org.deegree.commons.configuration;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.deegree.commons.configuration package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ScaleDenominators_QNAME = new QName("http://www.deegree.org/commons", "ScaleDenominators");
    private final static QName _BoundingBox_QNAME = new QName("http://www.deegree.org/commons", "BoundingBox");
    private final static QName _Keywords_QNAME = new QName("http://www.deegree.org/commons", "Keywords");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.deegree.commons.configuration
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link KeywordsType }
     * 
     */
    public KeywordsType createKeywordsType() {
        return new KeywordsType();
    }

    /**
     * Create an instance of {@link ScaleDenominatorsType }
     * 
     */
    public ScaleDenominatorsType createScaleDenominatorsType() {
        return new ScaleDenominatorsType();
    }

    /**
     * Create an instance of {@link LanguageStringType }
     * 
     */
    public LanguageStringType createLanguageStringType() {
        return new LanguageStringType();
    }

    /**
     * Create an instance of {@link BoundingBoxType }
     * 
     */
    public BoundingBoxType createBoundingBoxType() {
        return new BoundingBoxType();
    }

    /**
     * Create an instance of {@link PooledConnection }
     * 
     */
    public PooledConnection createPooledConnection() {
        return new PooledConnection();
    }

    /**
     * Create an instance of {@link JDBCConnections }
     * 
     */
    public JDBCConnections createJDBCConnections() {
        return new JDBCConnections();
    }

    /**
     * Create an instance of {@link ProxyConfiguration }
     * 
     */
    public ProxyConfiguration createProxyConfiguration() {
        return new ProxyConfiguration();
    }

    /**
     * Create an instance of {@link CodeType }
     * 
     */
    public CodeType createCodeType() {
        return new CodeType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ScaleDenominatorsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.deegree.org/commons", name = "ScaleDenominators")
    public JAXBElement<ScaleDenominatorsType> createScaleDenominators(ScaleDenominatorsType value) {
        return new JAXBElement<ScaleDenominatorsType>(_ScaleDenominators_QNAME, ScaleDenominatorsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BoundingBoxType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.deegree.org/commons", name = "BoundingBox")
    public JAXBElement<BoundingBoxType> createBoundingBox(BoundingBoxType value) {
        return new JAXBElement<BoundingBoxType>(_BoundingBox_QNAME, BoundingBoxType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeywordsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.deegree.org/commons", name = "Keywords")
    public JAXBElement<KeywordsType> createKeywords(KeywordsType value) {
        return new JAXBElement<KeywordsType>(_Keywords_QNAME, KeywordsType.class, null, value);
    }

}
