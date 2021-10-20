// Copyright Yahoo. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.searchdefinition.derived;

import com.yahoo.config.model.application.provider.MockFileRegistry;
import com.yahoo.config.model.deploy.TestProperties;
import com.yahoo.searchdefinition.RankProfileRegistry;
import com.yahoo.searchdefinition.SearchBuilder;
import com.yahoo.searchdefinition.parser.ParseException;
import org.junit.Test;

import java.io.IOException;

/**
 * @author bratseth
 */
public class SchemaInheritanceTestCase extends AbstractExportingTestCase {

    @Test
    public void testIt() throws IOException, ParseException {
        SearchBuilder builder = SearchBuilder.createFromDirectory("src/test/derived/schemainheritance/",
                                                                  new MockFileRegistry(),
                                                                  new TestableDeployLogger(),
                                                                  new TestProperties());
        derive("schemainheritance", builder, builder.getSearch("child"));
        assertCorrectConfigFiles("schemainheritance");
    }

}
