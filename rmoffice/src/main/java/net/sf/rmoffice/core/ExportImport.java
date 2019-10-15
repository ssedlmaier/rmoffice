/*
 * Copyright 2012 Daniel Nettesheim
 * Modifications Copyright 2019 Stefan Sedlmaier
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
package net.sf.rmoffice.core;

import java.io.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sf.rmoffice.RMPreferences;
import net.sf.rmoffice.core.items.MagicalFeature;
import net.sf.rmoffice.core.items.MagicalItem;
import net.sf.rmoffice.meta.enums.CharImagePos;
import net.sf.rmoffice.meta.enums.LengthUnit;
import net.sf.rmoffice.meta.enums.MagicalItemFeatureType;
import net.sf.rmoffice.meta.enums.ResistanceEnum;
import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.meta.enums.TalentFlawLevel;
import net.sf.rmoffice.meta.enums.TalentFlawTransform;
import net.sf.rmoffice.meta.enums.TalentFlawType;
import net.sf.rmoffice.meta.enums.ToDoType;
import net.sf.rmoffice.meta.enums.WeightUnit;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.mapper.MapperWrapper;


/**
 *
 */
public class ExportImport {

    public static final String ENCODING = "UTF-8";

    public static boolean supportedFileExtension(String fileName) {
        return fileName.endsWith(RMPreferences.RMO_EXTENSION) || fileName.endsWith("json");
    }

    public static RMSheet importFile(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        Reader freader = new BufferedReader(new InputStreamReader(in, ExportImport.ENCODING));

        String fileName = file.getName();

        if (fileName.endsWith(RMPreferences.RMO_EXTENSION)) {
            return importXml(freader);
        } else if (fileName.endsWith("json")) {
            return importJson(freader);
        }

        throw new IOException("Unsupported file");
    }

    public static void exportFile(String fileName, RMSheet sheet, Writer out) throws IOException {
        if (fileName.endsWith(RMPreferences.RMO_EXTENSION)) {
            exportXml(sheet, out);
        } else if (fileName.endsWith("json")) {
            exportJson(sheet, out);
        } else {
            throw new IOException("Unsupported file");
        }
    }

    private static void exportXml(RMSheet sheet, Writer out) {
        XStream xStream = createXStream(true);
        xStream.toXML(sheet, out);
    }

    private static void exportJson(RMSheet sheet, Writer out) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .create();
        gson.toJson(sheet, out);
    }

    private static RMSheet importXml(Reader xmlReader) {
        XStream xStream = createXStream(false);
        return (RMSheet) xStream.fromXML(xmlReader);
    }

    private static RMSheet importJson(Reader reader) {
        Gson gson = new Gson();
        return gson.fromJson(reader, RMSheet.class);
    }

    private static XStream createXStream(boolean export) {
        XStream xStream = new XStream() {
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(@SuppressWarnings("rawtypes") Class definedIn,
                                                         String fieldName) {
                        if ("items".equals(fieldName)) {
                            return false;
                        }
                        return super.shouldSerializeMember(definedIn, fieldName);
                    }
                };
            }
        };
        xStream.alias("sheet", RMSheet.class);
        xStream.alias("attr", StatEnum.class);
        xStream.alias("rank", Rank.class);
        xStream.alias("characteristics", Characteristics.class);
        if (!export) {
            /* only for import */
            xStream.aliasField("behavior", Characteristics.class, "demeanor");
            /* refactoring SkillType workaround (4.2.2) */
            xStream.alias("net.sf.rmoffice.meta.SkillType", SkillType.class);
        }
        xStream.alias("todo", ToDo.class);
        xStream.alias("customSkill", CustomSkill.class);
        xStream.alias("magicalItem", MagicalItem.class);
        xStream.alias("feature", MagicalFeature.class);
        xStream.alias("equipment", Equipment.class);
        xStream.alias("levelup", RMLevelUp.class);
        xStream.alias("infopage", InfoPage.class);
        xStream.alias("skillType", SkillType.class);
        xStream.alias("talentflaw", TalentFlaw.class);
        xStream.alias("herb", Herb.class);
        xStream.addImmutableType(SkillType.class);
        xStream.addImmutableType(StatEnum.class);
        xStream.addImmutableType(ToDoType.class);
        xStream.addImmutableType(MagicalItemFeatureType.class);
        xStream.addImmutableType(ResistanceEnum.class);
        xStream.addImmutableType(LengthUnit.class);
        xStream.addImmutableType(WeightUnit.class);
        xStream.addImmutableType(CharImagePos.class);
        xStream.addImmutableType(TalentFlawLevel.class);
        xStream.addImmutableType(TalentFlawType.class);
        xStream.addImmutableType(TalentFlawTransform.class);
        /* StatEnum converter */
        SingleValueConverter converter = new SingleValueConverter() {

            @Override
            public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
                if (type.isEnum() && type.getName().equals(StatEnum.class.getName())) {
                    return true;
                }
                return false;
            }

            @Override
            public String toString(Object obj) {
                if (obj.getClass().isEnum()) {
                    return ((Enum<?>) obj).name();
                }
                return obj.toString();
            }

            @Override
            public Object fromString(String str) {
                if ("Ge".equals(str)) {
                    return StatEnum.AGILITY;
                } else if ("Ko".equals(str)) {
                    return StatEnum.CONSTITUTION;
                } else if ("GD".equals(str)) {
                    return StatEnum.MEMORY;
                } else if ("Lo".equals(str)) {
                    return StatEnum.REASONING;
                } else if ("SD".equals(str)) {
                    return StatEnum.SELFDISCIPLINE;
                } else if ("Em".equals(str)) {
                    return StatEnum.EMPATHY;
                } else if ("In".equals(str)) {
                    return StatEnum.INTUITION;
                } else if ("Ch".equals(str)) {
                    return StatEnum.PRESENCE;
                } else if ("Re".equals(str)) {
                    return StatEnum.QUICKNESS;
                } else if ("St".equals(str)) {
                    return StatEnum.STRENGTH;
                } else {
                    return StatEnum.valueOf(str);
                }
            }
        };
        xStream.registerConverter(converter);
        return xStream;
    }

}
