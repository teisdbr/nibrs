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
package org.search.nibrs.stagingdata.service.xml;

import org.junit.Assert;
import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.ByNameAndTextRecSelector;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelector;
import org.xmlunit.diff.ElementSelectors;

public class SelectingNodesExamplesTest {
    private static final String CONTROL = "<flowers>"
        + "  <tbody>"
        + "    <flower>"
        + "      <id>some key</id>"
        + "      <td>some value</td>"
        + "    </flower>"
        + "    <flower>"
        + "      <id>another key</id>"
        + "      <td>another value</td>"
        + "    </flower>"
        + "  </tbody>"
        + "</flowers>";
    private static final String TEST = "<flowers>"
        + "  <tbody>"
        + "    <flower>"
        + "      <id>another key</id>"
        + "      <td>another value</td>"
        + "    </flower>"
        + "    <flower>"
        + "      <id>some key</id>"
        + "      <td>some value</td>"
        + "    </flower>"
        + "  </tbody>"
        + "</flowers>";

    @Test
    public void conditionalWithXPath() throws Exception {
        ElementSelector es = ElementSelectors.conditionalBuilder()
            .whenElementIsNamed("flower")
            .thenUse(ElementSelectors.byXPath("./id", ElementSelectors.byNameAndText))
            .elseUse(ElementSelectors.byName)
            .build();

        Diff myDiff = DiffBuilder.compare(CONTROL).withTest(TEST)
            .withNodeMatcher(new DefaultNodeMatcher(es))
            .checkForSimilar()
            .build();

        Assert.assertFalse(myDiff.toString(), myDiff.hasDifferences());
    }


    @Test
    public void conditionalWithByNameAndTextRec() throws Exception {
        ElementSelector es = ElementSelectors.conditionalBuilder()
            .whenElementIsNamed("flower")
            .thenUse(new ByNameAndTextRecSelector())
            .elseUse(ElementSelectors.byName)
            .build();

        Diff myDiff = DiffBuilder.compare(CONTROL).withTest(TEST)
            .withNodeMatcher(new DefaultNodeMatcher(es))
            .checkForSimilar()
            .build();

        Assert.assertFalse(myDiff.toString(), myDiff.hasDifferences());
    }
}