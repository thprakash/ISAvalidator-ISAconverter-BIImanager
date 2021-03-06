/**

 The ISAconverter, ISAvalidator & BII Management Tool are components of the ISA software suite (http://www.isa-tools.org)

 Exhibit A
 The ISAconverter, ISAvalidator & BII Management Tool are licensed under the Mozilla Public License (MPL) version
 1.1/GPL version 2.0/LGPL version 2.1

 "The contents of this file are subject to the Mozilla Public License
 Version 1.1 (the "License"). You may not use this file except in compliance with the License.
 You may obtain copies of the Licenses at http://www.mozilla.org/MPL/MPL-1.1.html.

 Software distributed under the License is distributed on an "AS IS"
 basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 License for the specific language governing rights and limitations
 under the License.

 The Original Code is the ISAconverter, ISAvalidator & BII Management Tool.

 The Initial Developer of the Original Code is the ISA Team (Eamonn Maguire, eamonnmag@gmail.com;
 Philippe Rocca-Serra, proccaserra@gmail.com; Susanna-Assunta Sansone, sa.sanson@gmail.com;
 http://www.isa-tools.org). All portions of the code written by the ISA Team are Copyright (c)
 2007-2011 ISA Team. All Rights Reserved.

 Contributor(s):
 Rocca-Serra P, Brandizi M, Maguire E, Sklyar N, Taylor C, Begley K, Field D,
 Harris S, Hide W, Hofmann O, Neumann S, Sterk P, Tong W, Sansone SA. ISA software suite:
 supporting standards-compliant experimental annotation and enabling curation at the community level.
 Bioinformatics 2010;26(18):2354-6.

 Alternatively, the contents of this file may be used under the terms of either the GNU General
 Public License Version 2 or later (the "GPL") - http://www.gnu.org/licenses/gpl-2.0.html, or
 the GNU Lesser General Public License Version 2.1 or later (the "LGPL") -
 http://www.gnu.org/licenses/lgpl-2.1.html, in which case the provisions of the GPL
 or the LGPL are applicable instead of those above. If you wish to allow use of your version
 of this file only under the terms of either the GPL or the LGPL, and not to allow others to
 use your version of this file under the terms of the MPL, indicate your decision by deleting
 the provisions above and replace them with the notice and other provisions required by the
 GPL or the LGPL. If you do not delete the provisions above, a recipient may use your version
 of this file under the terms of any one of the MPL, the GPL or the LGPL.

 Sponsors:
 The ISA Team and the ISA software suite have been funded by the EU Carcinogenomics project
 (http://www.carcinogenomics.eu), the UK BBSRC (http://www.bbsrc.ac.uk), the UK NERC-NEBC
 (http://nebc.nerc.ac.uk) and in part by the EU NuGO consortium (http://www.nugo.org/everyone).

 */

package org.isatools.tablib.export;

import org.apache.log4j.Logger;
import org.isatools.tablib.export.properties.PropertyExportingHelper;
import org.isatools.tablib.schema.Record;
import org.isatools.tablib.schema.SectionInstance;
import org.isatools.tablib.utils.BIIObjectStore;
import uk.ac.ebi.bioinvindex.model.Identifiable;

import java.util.List;

/**
 * Exports the rows of a section, on a line-by-line basis, and by using {@link PropertyExportingHelper}
 * to map fields into object's properties.
 * <p/>
 * date: Jun 9, 2008
 *
 * @author brandizi
 * @param <XT>
 */
public abstract class ClassTabExporter<XT extends Identifiable>
        extends RecordOrientedExporter<XT> {
    protected List<PropertyExportingHelper<XT>> propertyExportingHelpers;

    protected static final Logger log = Logger.getLogger(ClassTabExporter.class);

    public ClassTabExporter(BIIObjectStore store) {
        super(store);
    }


    @Override
    public Record export(XT source, Record record) {
        if (source == null) {
            log.trace("Ignoring exported null value");
            return record;
        }

        if (propertyExportingHelpers == null || propertyExportingHelpers.size() == 0) {
            log.trace("Class exporter " + this.getClass().getSimpleName() + " has no exporing helper");
        } else {
            for (PropertyExportingHelper<XT> helper : propertyExportingHelpers) {
                helper.export(source, record);
            }
        }

        return record;
    }


    /**
     * Create a new section instance and setup the fields, by means of {@link PropertyExportingHelper#setFields(SectionInstance)}
     */
    @Override
    public SectionInstance createSectionInstance() {
        SectionInstance sectionInstance = super.createSectionInstance();

        if (propertyExportingHelpers == null || propertyExportingHelpers.size() == 0) {
            log.trace("WARNING, Class exporter " + this.getClass().getSimpleName() + " has no exporing helper");
        } else {
            for (PropertyExportingHelper<XT> helper : propertyExportingHelpers) {
                helper.setFields(sectionInstance);
            }
        }

        return sectionInstance;
    }

}
