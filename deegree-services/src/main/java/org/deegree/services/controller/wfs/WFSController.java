//$HeadURL: svn+ssh://mschneider@svn.wald.intevation.org/deegree/deegree3/services/trunk/src/org/deegree/services/controller/wps/WPSController.java $
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2009 by:
 Department of Geography, University of Bonn
 and
 lat/lon GmbH

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 Contact information:

 lat/lon GmbH
 Aennchenstr. 19, 53177 Bonn
 Germany
 http://lat-lon.de/

 Department of Geography, University of Bonn
 Prof. Dr. Klaus Greve
 Postfach 1147, 53001 Bonn
 Germany
 http://www.geographie.uni-bonn.de/deegree/

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/

package org.deegree.services.controller.wfs;

import static org.deegree.commons.utils.StringUtils.REMOVE_DOUBLE_FIELDS;
import static org.deegree.commons.utils.StringUtils.REMOVE_EMPTY_FIELDS;
import static org.deegree.protocol.wfs.WFSConstants.VERSION_100;
import static org.deegree.protocol.wfs.WFSConstants.VERSION_110;
import static org.deegree.protocol.wfs.WFSConstants.VERSION_200;
import static org.deegree.protocol.wfs.WFSConstants.WFS_200_NS;
import static org.deegree.protocol.wfs.WFSConstants.WFS_NS;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.fileupload.FileItem;
import org.deegree.commons.tom.ows.Version;
import org.deegree.commons.utils.Pair;
import org.deegree.commons.utils.StringUtils;
import org.deegree.commons.utils.kvp.InvalidParameterValueException;
import org.deegree.commons.utils.kvp.KVPUtils;
import org.deegree.commons.utils.kvp.MissingParameterException;
import org.deegree.commons.xml.XMLAdapter;
import org.deegree.commons.xml.XMLParsingException;
import org.deegree.commons.xml.stax.StAXParsingHelper;
import org.deegree.commons.xml.stax.XMLStreamWriterWrapper;
import org.deegree.cs.CRS;
import org.deegree.cs.exceptions.UnknownCRSException;
import org.deegree.feature.persistence.FeatureStore;
import org.deegree.feature.types.FeatureType;
import org.deegree.gml.GMLVersion;
import org.deegree.protocol.ows.capabilities.GetCapabilities;
import org.deegree.protocol.wfs.WFSConstants;
import org.deegree.protocol.wfs.WFSConstants.WFSRequestType;
import org.deegree.protocol.wfs.capabilities.GetCapabilitiesKVPAdapter;
import org.deegree.protocol.wfs.capabilities.GetCapabilitiesXMLAdapter;
import org.deegree.protocol.wfs.describefeaturetype.DescribeFeatureType;
import org.deegree.protocol.wfs.describefeaturetype.DescribeFeatureTypeKVPAdapter;
import org.deegree.protocol.wfs.describefeaturetype.DescribeFeatureTypeXMLAdapter;
import org.deegree.protocol.wfs.getfeature.GetFeature;
import org.deegree.protocol.wfs.getfeature.GetFeatureKVPAdapter;
import org.deegree.protocol.wfs.getfeature.GetFeatureXMLAdapter;
import org.deegree.protocol.wfs.getfeaturewithlock.GetFeatureWithLock;
import org.deegree.protocol.wfs.getfeaturewithlock.GetFeatureWithLockKVPAdapter;
import org.deegree.protocol.wfs.getfeaturewithlock.GetFeatureWithLockXMLAdapter;
import org.deegree.protocol.wfs.getgmlobject.GetGmlObject;
import org.deegree.protocol.wfs.getgmlobject.GetGmlObjectKVPAdapter;
import org.deegree.protocol.wfs.getgmlobject.GetGmlObjectXMLAdapter;
import org.deegree.protocol.wfs.lockfeature.LockFeature;
import org.deegree.protocol.wfs.lockfeature.LockFeatureKVPAdapter;
import org.deegree.protocol.wfs.lockfeature.LockFeatureXMLAdapter;
import org.deegree.protocol.wfs.transaction.Transaction;
import org.deegree.protocol.wfs.transaction.TransactionKVPAdapter;
import org.deegree.protocol.wfs.transaction.TransactionXMLAdapter;
import org.deegree.services.controller.AbstractOGCServiceController;
import org.deegree.services.controller.ImplementationMetadata;
import org.deegree.services.controller.OGCFrontController;
import org.deegree.services.controller.exception.ControllerException;
import org.deegree.services.controller.exception.ControllerInitException;
import org.deegree.services.controller.exception.serializer.XMLExceptionSerializer;
import org.deegree.services.controller.ows.OGCExceptionXMLAdapter;
import org.deegree.services.controller.ows.OWSException;
import org.deegree.services.controller.ows.OWSException100XMLAdapter;
import org.deegree.services.controller.utils.HttpResponseBuffer;
import org.deegree.services.i18n.Messages;
import org.deegree.services.jaxb.main.DeegreeServiceControllerType;
import org.deegree.services.jaxb.main.DeegreeServicesMetadataType;
import org.deegree.services.jaxb.main.ServiceIdentificationType;
import org.deegree.services.jaxb.main.ServiceProviderType;
import org.deegree.services.jaxb.wfs.DeegreeWFS;
import org.deegree.services.jaxb.wfs.FeatureTypeMetadata;
import org.deegree.services.jaxb.wfs.PublishedInformation;
import org.deegree.services.wfs.WFService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles WFS (WebFeatureService) protocol communication and acts as the link between the {@link OGCFrontController}
 * and the {@link WFService}.
 * <p>
 * Supported WFS protocol versions:
 * <ul>
 * <li>1.0.0 (in implementation, nearly finished)</li>
 * <li>1.1.0 (in implementation, nearly finished)</li>
 * <li>2.0.0 (started, standard is still tentative)</li>
 * </ul>
 * </p>
 * 
 * @see AbstractOGCServiceController
 * @see OGCFrontController
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 15339 $, $Date: 2008-12-11 18:40:09 +0100 (Do, 11 Dez 2008) $
 */
public class WFSController extends AbstractOGCServiceController {

    private static final Logger LOG = LoggerFactory.getLogger( WFSController.class );

    private static final ImplementationMetadata<WFSRequestType> IMPLEMENTATION_METADATA = new ImplementationMetadata<WFSRequestType>() {
        {
            supportedVersions = new Version[] { VERSION_100, VERSION_110, VERSION_200 };
            handledNamespaces = new String[] { WFS_NS, WFS_200_NS };
            handledRequests = WFSRequestType.class;
            supportedConfigVersions = new Version[] { Version.parseVersion( "0.5.0" ) };
        }
    };

    private static final int DEFAULT_MAX_FEATURES = 15000;

    private WFService service;

    private DescribeFeatureTypeHandler dftHandler;

    private GetFeatureHandler getFeatureHandler;

    private GetGmlObjectHandler getGmlObjectHandler;

    private LockFeatureHandler lockFeatureHandler;

    private boolean enableTransactions;

    private boolean enableStreaming;

    private List<CRS> querySRS = new ArrayList<CRS>();

    private Map<QName, FeatureTypeMetadata> ftNameToFtMetadata = new HashMap<QName, FeatureTypeMetadata>();

    private ServiceIdentificationType serviceId;

    private ServiceProviderType serviceProvider;

    @Override
    public void init( XMLAdapter controllerConf, DeegreeServicesMetadataType serviceMetadata,
                      DeegreeServiceControllerType mainConf )
                            throws ControllerInitException {

        LOG.info( "Initializing WFS controller." );
        init( serviceMetadata, mainConf, IMPLEMENTATION_METADATA, controllerConf );

        // TODO merge with WFS configuration
        serviceId = serviceMetadata.getServiceIdentification();
        serviceProvider = serviceMetadata.getServiceProvider();

        // unmarshall ServiceConfiguration and PublishedInformation
        DeegreeWFS jaxbConfig = null;
        try {
            Unmarshaller u = JAXBContext.newInstance( "org.deegree.services.jaxb.wfs" ).createUnmarshaller();
            // turn the application schema location into an absolute URL
            jaxbConfig = (DeegreeWFS) u.unmarshal( controllerConf.getRootElement().getXMLStreamReaderWithoutCaching() );
        } catch ( XMLParsingException e ) {
            LOG.error( e.getMessage(), e );
            throw new ControllerInitException( "Error parsing WFS configuration: " + e.getMessage(), e );
        } catch ( JAXBException e ) {
            LOG.error( e.getMessage(), e );
            throw new ControllerInitException( "Error parsing WFS configuration: " + e.getMessage(), e );
        }

        PublishedInformation pi = jaxbConfig.getPublishedInformation();

        validateAndSetOfferedVersions( pi.getSupportedVersions().getVersion() );
        enableTransactions = pi.isEnableTransactions();
        enableStreaming = ( pi.isEnableStreaming() != null ) ? pi.isEnableStreaming() : false;
        int maxFeatures = pi.getQueryMaxFeatures() == null ? DEFAULT_MAX_FEATURES : pi.getQueryMaxFeatures().intValue();
        boolean checkAreaOfUse = pi.isCheckAreaOfUse() == null ? false : pi.isCheckAreaOfUse();

        try {
            if ( jaxbConfig.getPublishedInformation().getQuerySRS() != null ) {
                String[] querySrs = StringUtils.split( jaxbConfig.getPublishedInformation().getQuerySRS(), " ",
                                                       REMOVE_EMPTY_FIELDS | REMOVE_DOUBLE_FIELDS );
                for ( String srs : querySrs ) {
                    LOG.debug( "Query SRS: " + srs );
                    CRS crs = new CRS( srs );
                    crs.getWrappedCRS();
                    this.querySRS.add( crs );
                }
            }
        } catch ( UnknownCRSException e ) {
            String msg = "Invalid QuerySRS parameter: " + e.getMessage();
            throw new ControllerInitException( msg );
        }

        // fill metadata map
        for ( FeatureTypeMetadata ftMd : jaxbConfig.getPublishedInformation().getFeatureTypeMetadata() ) {
            ftNameToFtMetadata.put( ftMd.getName(), ftMd );
        }

        service = new WFService();
        try {
            service.init( jaxbConfig.getServiceConfiguration(), controllerConf.getSystemId() );
        } catch ( Exception e ) {
            throw new ControllerInitException( "Error initializing WFS / FeatureStores: " + e.getMessage(), e );
        }
        dftHandler = new DescribeFeatureTypeHandler( service );
        getFeatureHandler = new GetFeatureHandler( this, service, enableStreaming, maxFeatures, checkAreaOfUse );
        getGmlObjectHandler = new GetGmlObjectHandler( this, service );
        lockFeatureHandler = new LockFeatureHandler( this, service, enableStreaming, maxFeatures, checkAreaOfUse );
    }

    @Override
    public void destroy() {
        LOG.debug( "destroy" );
        service.destroy();
    }

    /**
     * Returns the underlying {@link WFService} instance.
     * 
     * @return the underlying {@link WFService}
     */
    public WFService getService() {
        return service;
    }

    @Override
    public void doKVP( Map<String, String> kvpParamsUC, HttpServletRequest request, HttpResponseBuffer response,
                       List<FileItem> multiParts )
                            throws ServletException, IOException {

        LOG.debug( "doKVP" );
        Version requestVersion = null;
        try {
            requestVersion = getVersion( kvpParamsUC.get( "VERSION" ) );

            String requestName = KVPUtils.getRequired( kvpParamsUC, "REQUEST" );
            WFSRequestType requestType = getRequestTypeByName( requestName );

            // check if requested version is supported and offered (except for GetCapabilities)
            if ( requestType != WFSRequestType.GetCapabilities ) {
                if ( requestVersion == null ) {
                    throw new OWSException( "Missing version parameter.", OWSException.MISSING_PARAMETER_VALUE,
                                            "version" );
                }

                checkVersion( requestVersion );

                // needed for CITE 1.1.0 compliance
                if ( requestVersion.equals( VERSION_110 ) ) {
                    String serviceAttr = KVPUtils.getRequired( kvpParamsUC, "SERVICE" );
                    if ( !"WFS".equals( serviceAttr ) ) {
                        throw new OWSException( "Wrong service attribute: '" + serviceAttr + "' -- must be 'WFS'.",
                                                OWSException.INVALID_PARAMETER_VALUE, "service" );
                    }
                }
            }

            // build namespaces from NamespaceHints given in the configuration
            Map<String, String> nsMap = service.getPrefixToNs();

            if ( enableStreaming ) {
                response.disableBuffering();
            }

            switch ( requestType ) {
            case DescribeFeatureType:
                DescribeFeatureType describeFt = DescribeFeatureTypeKVPAdapter.parse( kvpParamsUC );
                dftHandler.doDescribeFeatureType( describeFt, response );
                break;
            case GetCapabilities:
                GetCapabilities getCapabilities = GetCapabilitiesKVPAdapter.parse( requestVersion, kvpParamsUC );
                doGetCapabilities( getCapabilities, response );
                break;
            case GetFeature:
                GetFeature getFeature = GetFeatureKVPAdapter.parse( kvpParamsUC, nsMap );
                getFeatureHandler.doGetFeature( getFeature, response );
                break;
            case GetFeatureWithLock:
                checkTransactionsEnabled( requestName );
                GetFeatureWithLock getFeatureWithLock = GetFeatureWithLockKVPAdapter.parse( kvpParamsUC );
                getFeatureHandler.doGetFeature( getFeatureWithLock, response );
                break;
            case GetGmlObject:
                GetGmlObject getGmlObject = GetGmlObjectKVPAdapter.parse( kvpParamsUC );
                getGmlObjectHandler.doGetGmlObject( getGmlObject, response );
                break;
            case LockFeature:
                checkTransactionsEnabled( requestName );
                LockFeature lockFeature = LockFeatureKVPAdapter.parse( kvpParamsUC );
                lockFeatureHandler.doLockFeature( lockFeature, response );
                break;
            case Transaction:
                checkTransactionsEnabled( requestName );
                Transaction transaction = TransactionKVPAdapter.parse( kvpParamsUC );
                new TransactionHandler( this, service, transaction ).doTransaction( response );
                break;
            // WFS 2.0.0 only request types
            case CreateStoredQuery:
            case DescribeStoredQueries:
            case DropStoredQuery:
            case GetPropertyValue:
            case ListStoredQueries:
                throw new OWSException( Messages.get( "WFS_OPERATION_NOT_IMPLEMENTED_YET", requestName ),
                                        OWSException.OPERATION_NOT_SUPPORTED );
            }
        } catch ( OWSException e ) {
            LOG.debug( e.getMessage(), e );
            if ( requestVersion != null && requestVersion.equals( VERSION_100 ) ) {
                sendServiceException100( e, response );
            } else {
                // for any other version...
                sendServiceException110( e, response );
            }
        } catch ( MissingParameterException e ) {
            LOG.debug( e.getMessage(), e );
            sendServiceException110( new OWSException( e ), response );
        } catch ( InvalidParameterValueException e ) {
            LOG.debug( e.getMessage(), e );
            sendServiceException110( new OWSException( e ), response );
        } catch ( Exception e ) {
            LOG.debug( e.getMessage(), e );
            sendServiceException110( new OWSException( e.getMessage(), ControllerException.NO_APPLICABLE_CODE ),
                                     response );
        }
    }

    private void checkTransactionsEnabled( String requestName )
                            throws OWSException {
        if ( !enableTransactions ) {
            throw new OWSException( Messages.get( "WFS_TRANSACTIONS_DISABLED", requestName ),
                                    OWSException.OPERATION_NOT_SUPPORTED );
        }
    }

    private void sendServiceException100( OWSException e, HttpResponseBuffer response )
                            throws ServletException {
        LOG.debug( "Sending WFS 1.0.0 service exception " + e );
        sendException( "application/vnd.ogc.se_xml", "UTF-8", null, 300, new OGCExceptionXMLAdapter(), e, response );
    }

    @Override
    public void doXML( XMLStreamReader xmlStream, HttpServletRequest request, HttpResponseBuffer response,
                       List<FileItem> multiParts )
                            throws ServletException, IOException {

        LOG.debug( "doXML" );
        Version requestVersion = null;
        try {
            String requestName = xmlStream.getLocalName();
            WFSRequestType requestType = getRequestTypeByName( requestName );

            // check if requested version is supported and offered (except for GetCapabilities)
            requestVersion = getVersion( StAXParsingHelper.getAttributeValue( xmlStream, "version" ) );
            if ( requestType != WFSRequestType.GetCapabilities ) {
                requestVersion = checkVersion( requestVersion );

                // needed for CITE 1.1.0 compliance
                String serviceAttr = StAXParsingHelper.getAttributeValue( xmlStream, "service" );
                if ( serviceAttr != null && !( "WFS".equals( serviceAttr ) || "".equals( serviceAttr ) ) ) {
                    throw new OWSException( "Wrong service attribute: '" + serviceAttr + "' -- must be 'WFS'.",
                                            OWSException.INVALID_PARAMETER_VALUE, "service" );
                }
            }

            if ( enableStreaming ) {
                response.disableBuffering();
            }

            switch ( requestType ) {
            case DescribeFeatureType:
                DescribeFeatureTypeXMLAdapter describeFtAdapter = new DescribeFeatureTypeXMLAdapter();
                describeFtAdapter.setRootElement( new XMLAdapter( xmlStream ).getRootElement() );
                DescribeFeatureType describeFt = describeFtAdapter.parse( requestVersion );
                dftHandler.doDescribeFeatureType( describeFt, response );
                break;
            case GetCapabilities:
                GetCapabilitiesXMLAdapter getCapabilitiesAdapter = new GetCapabilitiesXMLAdapter();
                getCapabilitiesAdapter.setRootElement( new XMLAdapter( xmlStream ).getRootElement() );
                GetCapabilities wfsRequest = getCapabilitiesAdapter.parse( requestVersion );
                doGetCapabilities( wfsRequest, response );
                break;
            case GetFeature:
                GetFeatureXMLAdapter getFeatureAdapter = new GetFeatureXMLAdapter();
                getFeatureAdapter.setRootElement( new XMLAdapter( xmlStream ).getRootElement() );
                GetFeature getFeature = getFeatureAdapter.parse( requestVersion );
                getFeatureHandler.doGetFeature( getFeature, response );
                break;
            case GetFeatureWithLock:
                checkTransactionsEnabled( requestName );
                GetFeatureWithLockXMLAdapter getFeatureWithLockAdapter = new GetFeatureWithLockXMLAdapter();
                getFeatureWithLockAdapter.setRootElement( new XMLAdapter( xmlStream ).getRootElement() );
                GetFeatureWithLock getFeatureWithLock = getFeatureWithLockAdapter.parse();
                getFeatureHandler.doGetFeature( getFeatureWithLock, response );
                break;
            case GetGmlObject:
                GetGmlObjectXMLAdapter getGmlObjectAdapter = new GetGmlObjectXMLAdapter();
                getGmlObjectAdapter.setRootElement( new XMLAdapter( xmlStream ).getRootElement() );
                GetGmlObject getGmlObject = getGmlObjectAdapter.parse();
                getGmlObjectHandler.doGetGmlObject( getGmlObject, response );
                break;
            case LockFeature:
                checkTransactionsEnabled( requestName );
                LockFeatureXMLAdapter lockFeatureAdapter = new LockFeatureXMLAdapter();
                lockFeatureAdapter.setRootElement( new XMLAdapter( xmlStream ).getRootElement() );
                LockFeature lockFeature = lockFeatureAdapter.parse();
                lockFeatureHandler.doLockFeature( lockFeature, response );
                break;
            case Transaction:
                checkTransactionsEnabled( requestName );
                Transaction transaction = TransactionXMLAdapter.parse( xmlStream );
                new TransactionHandler( this, service, transaction ).doTransaction( response );
                break;
            // WFS 2.0.0 only request types
            case CreateStoredQuery:
            case DescribeStoredQueries:
            case DropStoredQuery:
            case GetPropertyValue:
            case ListStoredQueries:
                throw new OWSException( Messages.get( "WFS_OPERATION_NOT_IMPLEMENTED_YET", requestName ),
                                        OWSException.OPERATION_NOT_SUPPORTED );
            }
        } catch ( OWSException e ) {
            LOG.debug( e.getMessage(), e );
            if ( requestVersion != null && requestVersion.equals( VERSION_100 ) ) {
                sendServiceException100( e, response );
            } else {
                // for any other version...
                sendServiceException110( e, response );
            }
        } catch ( XMLParsingException e ) {
            LOG.debug( e.getMessage(), e );
            sendServiceException110( new OWSException( e.getMessage(), OWSException.INVALID_PARAMETER_VALUE ), response );
        } catch ( MissingParameterException e ) {
            LOG.debug( e.getMessage(), e );
            sendServiceException110( new OWSException( e ), response );
        } catch ( InvalidParameterValueException e ) {
            LOG.debug( e.getMessage(), e );
            sendServiceException110( new OWSException( e ), response );
        } catch ( Exception e ) {
            LOG.debug( e.getMessage(), e );
            e.printStackTrace();
            sendServiceException110( new OWSException( e.getMessage(), ControllerException.NO_APPLICABLE_CODE ),
                                     response );
        }
    }

    /**
     * Returns an URL template for requesting individual objects (feature or geometries) from the server by the object's
     * id.
     * <p>
     * The form of the URL depends on the protocol version:
     * <ul>
     * <li>WFS 1.0.0: not possible, an <code>UnsupportedOperation</code> exception is thrown</li>
     * <li>WFS 1.1.0: GetGmlObject request</li>
     * <li>WFS 2.0.0: GetPropertyValue request</li>
     * </ul>
     * </p>
     * 
     * @param version
     *            WFS protocol version, must not be <code>null</code>
     * @param gmlVersion
     *            GML version, must not be <code>null</code>
     * @return URI template that contains <code>{}</code> as the placeholder for the object id
     * @throws UnsupportedOperationException
     *             if the protocol version does not support requesting individual objects by id
     */
    String getObjectXlinkTemplate( Version version, GMLVersion gmlVersion ) {

        String baseUrl = OGCFrontController.getHttpGetURL() + "SERVICE=WFS&VERSION=" + version + "&";
        String template = null;
        try {
            if ( VERSION_100.equals( version ) ) {
                baseUrl = OGCFrontController.getHttpGetURL() + "SERVICE=WFS&VERSION=1.1.0&";
                template = baseUrl + "REQUEST=GetGmlObject&OUTPUTFORMAT="
                           + URLEncoder.encode( gmlVersion.getMimeType(), "UTF-8" )
                           + "&TRAVERSEXLINKDEPTH=0&GMLOBJECTID={}#{}";
            } else if ( VERSION_110.equals( version ) ) {
                template = baseUrl + "REQUEST=GetGmlObject&OUTPUTFORMAT="
                           + URLEncoder.encode( gmlVersion.getMimeType(), "UTF-8" )
                           + "&TRAVERSEXLINKDEPTH=0&GMLOBJECTID={}#{}";
            } else if ( VERSION_200.equals( version ) ) {
                // TODO check spec.
                template = baseUrl + "REQUEST=GetPropertyValue&OUTPUTFORMAT="
                           + URLEncoder.encode( gmlVersion.getMimeType(), "UTF-8" )
                           + "&TRAVERSEXLINKDEPTH=0&GMLOBJECTID={}#{}";
            } else {
                throw new UnsupportedOperationException( Messages.getMessage( "WFS_BACKREFERENCE_UNSUPPORTED", version ) );
            }
        } catch ( UnsupportedEncodingException e ) {
            // should never happen (UTF-8 is known)
        }
        return template;
    }

    /**
     * Returns the value for the 'xsi:schemaLocation' attribute to be included in a <code>GetGmlObject</code> or
     * <code>GetFeature</code> response.
     * 
     * @param version
     *            WFS protocol version, must not be <code>null</code>
     * @param gmlVersion
     *            requested GML version, must not be <code>null</code>
     * @param fts
     *            types of features included in the response
     * @return schemaLocation value
     */
    static String getSchemaLocation( Version version, GMLVersion gmlVersion, QName... fts ) {

        if ( fts.length == 0 ) {
            return OGCFrontController.getHttpGetURL() + "SERVICE=WFS&VERSION=" + version
                   + "&REQUEST=DescribeFeatureType";
        }

        String baseUrl = OGCFrontController.getHttpGetURL() + "SERVICE=WFS&VERSION=" + version
                         + "&REQUEST=DescribeFeatureType&OUTPUTFORMAT=";

        // String baseUrl = OGCFrontController.getHttpGetURL() + "SERVICE=WFS&VERSION=" + version
        // + "&REQUEST=DescribeFeatureType";

        try {
            if ( VERSION_100.equals( version ) && gmlVersion == GMLVersion.GML_2 ) {
                baseUrl += "XMLSCHEMA";
            } else {
                baseUrl += URLEncoder.encode( gmlVersion.getMimeType(), "UTF-8" );
            }
            baseUrl += "&TYPENAME=";

            Map<String, String> bindings = new HashMap<String, String>();
            for ( int i = 0; i < fts.length; i++ ) {
                QName ftName = fts[i];
                bindings.put( ftName.getPrefix(), ftName.getNamespaceURI() );
                baseUrl += URLEncoder.encode( ftName.getPrefix(), "UTF-8" ) + ":"
                           + URLEncoder.encode( ftName.getLocalPart(), "UTF-8" );
                if ( i != fts.length - 1 ) {
                    baseUrl += ",";
                }
            }

            if ( !VERSION_100.equals( version ) ) {
                baseUrl += "&NAMESPACE=xmlns(";
                int i = 0;
                for ( String prefix : bindings.keySet() ) {
                    baseUrl += URLEncoder.encode( prefix, "UTF-8" ) + "="
                               + URLEncoder.encode( bindings.get( prefix ), "UTF-8" );
                    if ( i != bindings.size() - 1 ) {
                        baseUrl += ",";
                    }
                }
                baseUrl += ")";
            }
        } catch ( UnsupportedEncodingException e ) {
            // should never happen (UTF-8 *is* known)
        }

        return fts[0].getNamespaceURI() + " " + baseUrl;
    }

    private Version getVersion( String versionString )
                            throws OWSException {
        Version version = null;
        if ( versionString != null && !"".equals( versionString ) ) {
            try {
                version = Version.parseVersion( versionString );
            } catch ( InvalidParameterValueException e ) {
                throw new OWSException( e.getMessage(), OWSException.INVALID_PARAMETER_VALUE, "version" );
            }
        }
        return version;
    }

    private WFSRequestType getRequestTypeByName( String requestName )
                            throws OWSException {

        WFSRequestType requestType = IMPLEMENTATION_METADATA.getRequestTypeByName( requestName );
        if ( requestType == null ) {
            String msg = "Request type '" + requestName + "' is not supported.";
            throw new OWSException( msg, OWSException.OPERATION_NOT_SUPPORTED, "request" );
        }
        return requestType;
    }

    private void doGetCapabilities( GetCapabilities request, HttpResponseBuffer response )
                            throws XMLStreamException, IOException, OWSException {

        LOG.debug( "doGetCapabilities: " + request );
        Version negotiatedVersion = negotiateVersion( request );
        response.setContentType( "text/xml; charset=UTF-8" );

        // cope with the 'All' section specifier
        Set<String> sections = request.getSections();
        Set<String> sectionsUC = new HashSet<String>();
        for ( String section : sections ) {
            if ( section.equalsIgnoreCase( "ALL" ) ) {
                sectionsUC = null;
                break;
            }
            sectionsUC.add( section.toUpperCase() );
        }
        // never empty (only null)
        if ( sectionsUC != null && sectionsUC.size() == 0 ) {
            sectionsUC = null;
        }

        // sort the information on the served feature types
        Comparator<FeatureType> comp = new Comparator<FeatureType>() {
            @Override
            public int compare( FeatureType ftMd1, FeatureType ftMd2 ) {
                QName a = ftMd1.getName();
                QName b = ftMd2.getName();
                int order = a.getNamespaceURI().compareTo( b.getNamespaceURI() );
                if ( order == 0 ) {
                    order = a.getLocalPart().compareTo( b.getLocalPart() );
                }
                return order;
            }
        };
        Collection<FeatureType> sortedFts = new TreeSet<FeatureType>( comp );
        for ( FeatureStore fs : service.getStores() ) {
            for ( FeatureType ft : fs.getSchema().getFeatureTypes() ) {
                if ( !ft.isAbstract() ) {
                    sortedFts.add( ft );
                }
            }
        }

        XMLStreamWriter xmlWriter = getXMLResponseWriter( response, null );
        GetCapabilitiesHandler adapter = new GetCapabilitiesHandler( this, service, negotiatedVersion, xmlWriter,
                                                                     serviceId, serviceProvider, sortedFts,
                                                                     ftNameToFtMetadata, sectionsUC,
                                                                     enableTransactions, querySRS );
        adapter.export();
        xmlWriter.flush();
    }

    /**
     * Returns an <code>XMLStreamWriter</code> for writing an XML response document.
     * 
     * @param writer
     *            writer to write the XML to, must not be <code>null</code>
     * @param schemaLocation
     *            value for the 'xsi:schemaLocation' attribute in the root element, can be <code>null</code>
     * @return XML stream writer object that takes care of putting the schemaLocation in the root element
     * @throws XMLStreamException
     * @throws IOException
     */
    static XMLStreamWriter getXMLResponseWriter( HttpResponseBuffer writer, String schemaLocation )
                            throws XMLStreamException, IOException {

        if ( schemaLocation == null ) {
            return writer.getXMLWriter();
        }
        return new XMLStreamWriterWrapper( writer.getXMLWriter(), schemaLocation );
    }

    private void sendServiceException110( OWSException ex, HttpResponseBuffer response )
                            throws ServletException {

        LOG.debug( "Sending WFS 1.1.0 service exception " + ex );
        sendException( "application/vnd.ogc.se_xml", "UTF-8", null, 300, new OWSException100XMLAdapter(), ex, response );
    }

    @Override
    public Pair<XMLExceptionSerializer<OWSException>, String> getExceptionSerializer( Version requestVersion ) {
        String mime = "application/vnd.ogc.se_xml";
        XMLExceptionSerializer<OWSException> serializer = new OWSException100XMLAdapter();
        if ( WFSConstants.VERSION_100.equals( requestVersion ) ) {
            serializer = new OGCExceptionXMLAdapter();
        } else if ( WFSConstants.VERSION_110.equals( requestVersion ) ) {
            serializer = new OWSException100XMLAdapter();
        }
        return new Pair<XMLExceptionSerializer<OWSException>, String>( serializer, mime );
    }

    /**
     * Determines the requested (GML) output/input format.
     * 
     * TODO integrate handling for custom formats
     * 
     * @param requestVersion
     *            version of the WFS request, must not be <code>null</code>
     * @param format
     *            mimeType or identifier for the format, can be <code>null</code>
     * @return version to use for the written GML, or <code>null</code> if it is not supported
     */
    GMLVersion determineFormat( Version requestVersion, String format ) {

        GMLVersion gmlVersion = null;

        if ( format == null ) {
            // default values for the different WFS version
            if ( VERSION_100.equals( requestVersion ) ) {
                gmlVersion = GMLVersion.GML_2;
            } else if ( VERSION_110.equals( requestVersion ) ) {
                gmlVersion = GMLVersion.GML_31;
            } else if ( VERSION_200.equals( requestVersion ) ) {
                gmlVersion = GMLVersion.GML_32;
            } else {
                throw new RuntimeException( "Internal error: Unhandled WFS version: " + requestVersion );
            }
        } else {
            if ( "text/xml; subtype=gml/2.1.2".equals( format ) || "GML2".equals( format ) ) {
                gmlVersion = GMLVersion.GML_2;
            } else if ( "text/xml; subtype=gml/3.0.1".equals( format ) ) {
                gmlVersion = GMLVersion.GML_30;
            } else if ( "text/xml; subtype=gml/3.1.1".equals( format ) || "GML3".equals( format ) ) {
                gmlVersion = GMLVersion.GML_31;
            } else if ( "text/xml; subtype=gml/3.2.1".equals( format ) ) {
                gmlVersion = GMLVersion.GML_32;
            }
        }
        return gmlVersion;
    }

}
