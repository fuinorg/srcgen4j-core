/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.srcgen4j.core.xtext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.fuin.objects4j.common.Contract;
import org.fuin.srcgen4j.commons.ParseException;
import org.fuin.srcgen4j.commons.Parser;
import org.fuin.srcgen4j.commons.ParserConfig;
import org.fuin.srcgen4j.commons.SrcGen4JContext;
import org.fuin.srcgen4j.core.emf.AbstractEMFParser;

/**
 * Parses Xtext models.
 */
public final class XtextParser extends AbstractEMFParser<XtextParserConfig> implements Parser<ResourceSet> {

    private XtextParserConfig parserConfig;

    /**
     * Default constructor.
     */
    public XtextParser() {
        super(XtextParserConfig.class);
    }

    @Override
    public final void initialize(final SrcGen4JContext context, final ParserConfig config) {

        // Xtext always needs a configuration
        Contract.requireArgNotNull("config", config);

        this.parserConfig = getConcreteConfig(config);

        setModelDirs(parserConfig.getModelDirs());
        setFileExtensions(parserConfig.getModelExt());
        setModelResources(parserConfig.getModelResources());

        doSetup();
    }

    private void doSetup() {
        final String errorMessage = "Initializing the Xtext DSL with '" + parserConfig.getSetupClassName() + ".doSetup()' failed!";
        try {
            final Method method = parserConfig.getSetupClass().getDeclaredMethod("doSetup");
            method.invoke(parserConfig.getSetupClass(), new Object[] {});
        } catch (final SecurityException ex) {
            throw new RuntimeException(errorMessage, ex);
        } catch (final NoSuchMethodException ex) {
            throw new RuntimeException(errorMessage, ex);
        } catch (final IllegalArgumentException ex) {
            throw new RuntimeException(errorMessage, ex);
        } catch (final IllegalAccessException ex) {
            throw new RuntimeException(errorMessage, ex);
        } catch (final InvocationTargetException ex) {
            throw new RuntimeException(errorMessage, ex);
        }
    }

    @Override
    public final ResourceSet parse() throws ParseException {

        parseModel();
        // resolveProxies(); TODO Do we need to resolve cross references?
        return getResourceSet();

    }

}
