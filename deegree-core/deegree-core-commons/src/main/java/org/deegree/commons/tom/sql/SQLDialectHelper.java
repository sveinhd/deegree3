//$HeadURL$
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2010 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -

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
package org.deegree.commons.tom.sql;

/**
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public interface SQLDialectHelper {

    /**
     * @return the default table schema to use for the database (eg. for requesting table metadata)
     */
    String getDefaultSchema();

    /**
     * @return the string concatenation operator
     */
    String stringPlus();

    /**
     * @param pattern
     * @param string
     * @return an expression that yields the string index
     */
    String stringIndex( String pattern, String string );

    /**
     * @param expr
     * @param type
     * @return expr cast to type
     */
    String cast( String expr, String type );

    /**
     * @param dbSchema
     * @param table
     * @param column
     * @return statement to determine the coordinate dimension, the srid and the geometry type of a given column (in
     *         this order)
     */
    String geometryMetadata( String dbSchema, String table, String column );

}