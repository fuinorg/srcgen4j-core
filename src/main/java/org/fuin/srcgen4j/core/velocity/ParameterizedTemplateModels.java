/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
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
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.srcgen4j.core.velocity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.fuin.objects4j.common.NotEmpty;
import org.fuin.srcgen4j.commons.SrcGen4JContext;
import org.fuin.utils4j.Utils4J;

/**
 * A collection of multiple {@link ParameterizedTemplateModel} objects.
 */
public final class ParameterizedTemplateModels {

    @Valid
    @NotEmpty
    private List<ParameterizedTemplateModel> paramTemplates;

    /**
     * Default constructor.
     */
    public ParameterizedTemplateModels() {
        super();
    }

    /**
     * Returns the list of templates.
     * 
     * @return Template list.
     */
    public final List<ParameterizedTemplateModel> getModelList() {
        return paramTemplates;
    }

    /**
     * Sets the list of templates to a new value.
     * 
     * @param paramTemplates
     *            Template list to set.
     */
    public final void setParamTemplates(final List<ParameterizedTemplateModel> paramTemplates) {
        this.paramTemplates = paramTemplates;
    }

    /**
     * Adds another template to the list. If the list does not exist,it will be
     * created.
     * 
     * @param paramTemplate
     *            Template to add - Cannot be NULL.
     */
    public final void addParamTemplate(final ParameterizedTemplateModel paramTemplate) {
        if (paramTemplates == null) {
            paramTemplates = new ArrayList<ParameterizedTemplateModel>();
        }
        paramTemplates.add(paramTemplate);
    }

    /**
     * Adds all templates to the list. If the list does not exist,it will be
     * created.
     * 
     * @param list
     *            LIst of templates to add - Cannot be NULL.
     */
    public final void addParamTemplates(final List<ParameterizedTemplateModel> list) {
        if (list != null) {
            for (final ParameterizedTemplateModel template : list) {
                addParamTemplate(template);
            }
        }
    }

    /**
     * Initalizes the object.
     * 
     * @param context
     *            Current context.
     * @param vars
     *            Variables to use.
     */
    public final void init(final SrcGen4JContext context, final Map<String, String> vars) {
        if (paramTemplates != null) {
            for (final ParameterizedTemplateModel paramTemplate : paramTemplates) {
                paramTemplate.init(context, vars);
            }
        }
    }

    /**
     * Returns a list that contains all models that reference the given
     * template.
     * 
     * @param templateDir
     *            Directory where all template files are located - Cannot be
     *            NULL.
     * @param templateFile
     *            File to find references to - Cannot be NULL.
     * 
     * @return List - Never NULL, but may be empty.
     */
    public final List<ParameterizedTemplateModel> findReferencesTo(final File templateDir,
            final File templateFile) {

        final List<ParameterizedTemplateModel> result = new ArrayList<ParameterizedTemplateModel>();
        if ((paramTemplates != null) && Utils4J.fileInsideDirectory(templateDir, templateFile)) {
            for (final ParameterizedTemplateModel paramTemplate : paramTemplates) {
                if (paramTemplate.hasReferenceTo(templateDir, templateFile)) {
                    result.add(paramTemplate);
                }
            }
        }
        return result;
    }

}
