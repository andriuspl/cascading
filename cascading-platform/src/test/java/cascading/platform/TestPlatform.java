/*
 * Copyright (c) 2007-2013 Concurrent, Inc. All Rights Reserved.
 *
 * Project and contact information: http://www.cascading.org/
 *
 * This file is part of the Cascading project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cascading.platform;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;

import cascading.flow.FlowConnector;
import cascading.flow.FlowProcess;
import cascading.scheme.Scheme;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tuple.Fields;

/**
 *
 */
public abstract class TestPlatform
  {
  public static final String CLUSTER_TESTING_PROPERTY = "test.cluster.enabled";

  private boolean useCluster = false;
  private boolean enableCluster = true;
  private int numMappers = 0;
  private int numReducers = 0;

  protected TestPlatform()
    {
    enableCluster = Boolean.parseBoolean( System.getProperty( CLUSTER_TESTING_PROPERTY, Boolean.toString( enableCluster ) ) );
    }

  public String getName()
    {
    return getClass().getSimpleName().replaceAll( "^(.*)Platform$", "$1" ).toLowerCase();
    }

  public boolean isMapReduce()
    {
    return false;
    }

  public int getNumMappers()
    {
    return numMappers;
    }

  public void setNumMappers( int numMappers )
    {
    this.numMappers = numMappers;
    }

  public int getNumReducers()
    {
    return numReducers;
    }

  public void setNumReducers( int numReducers )
    {
    this.numReducers = numReducers;
    }

  public abstract void setUp() throws IOException;

  public abstract Map<Object, Object> getProperties();

  public abstract void tearDown();

  public void setUseCluster( boolean useCluster )
    {
    this.useCluster = useCluster;
    }

  public boolean isUseCluster()
    {
    return enableCluster && useCluster;
    }

  public abstract void copyFromLocal( String inputFile ) throws IOException;

  public abstract void copyToLocal( String outputFile ) throws IOException;

  public abstract boolean remoteExists( String outputFile ) throws IOException;

  public abstract FlowProcess getFlowProcess();

  public abstract FlowConnector getFlowConnector( Map<Object, Object> properties );

  public FlowConnector getFlowConnector()
    {
    return getFlowConnector( getProperties() );
    }

  public abstract Tap getTap( Scheme scheme, String filename, SinkMode mode );

  public Tap getTextFile( Fields sourceFields, String filename )
    {
    return getTextFile( sourceFields, filename, SinkMode.KEEP );
    }

  public Tap getTextFile( String filename )
    {
    return getTextFile( filename, SinkMode.KEEP );
    }

  public Tap getTextFile( String filename, SinkMode mode )
    {
    return getTextFile( null, filename, mode );
    }

  public Tap getTextFile( Fields sourceFields, String filename, SinkMode mode )
    {
    return getTextFile( sourceFields, Fields.ALL, filename, mode );
    }

  public abstract Tap getTextFile( Fields sourceFields, Fields sinkFields, String filename, SinkMode mode );

  public Tap getDelimitedFile( Fields fields, String delimiter, String filename )
    {
    return getDelimitedFile( fields, false, delimiter, "\"", null, filename, SinkMode.KEEP );
    }

  public Tap getDelimitedFile( Fields fields, String delimiter, String filename, SinkMode mode )
    {
    return getDelimitedFile( fields, false, delimiter, "\"", null, filename, mode );
    }

  public Tap getDelimitedFile( Fields fields, String filename, SinkMode mode )
    {
    return getDelimitedFile( fields, false, "\t", "\"", null, filename, mode );
    }

  public Tap getDelimitedFile( Fields fields, boolean hasHeader, String filename, SinkMode mode )
    {
    return getDelimitedFile( fields, hasHeader, "\t", "\"", null, filename, mode );
    }

  public Tap getDelimitedFile( Fields fields, String delimiter, Class[] types, String filename, SinkMode mode )
    {
    return getDelimitedFile( fields, false, delimiter, "", types, filename, mode );
    }

  public abstract Tap getDelimitedFile( Fields fields, boolean hasHeader, String delimiter, String quote, Class[] types, String filename, SinkMode mode );

  public abstract Tap getDelimitedFile( Fields fields, boolean skipHeader, boolean writeHeader, String delimiter, String quote, Class[] types, String filename, SinkMode mode );

  public abstract Tap getTemplateTap( Tap sink, String pathTemplate, int openThreshold );

  public abstract Tap getTemplateTap( Tap sink, String pathTemplate, Fields fields, int openThreshold );

  public abstract Scheme getTestConfigDefScheme();

  public abstract Scheme getTestFailScheme();

  public abstract Comparator getLongComparator( boolean reverseSort );

  public abstract Comparator getStringComparator( boolean reverseSort );

  public abstract String getHiddenTemporaryPath();
  }
