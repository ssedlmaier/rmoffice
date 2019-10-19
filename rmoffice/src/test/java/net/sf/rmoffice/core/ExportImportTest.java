/*
 * Copyright 2012 Daniel Nettesheim
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

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import org.junit.Test;


/**
 *
 */
public class ExportImportTest {

    @Test
    public void testImportOldVersions() throws Exception {
        /* test if we can import old character files */
        File file = new File(ExportImportTest.class.getResource("test404.rmo").toURI());
        ExportImport.importFile(file);

        file = new File(ExportImportTest.class.getResource("test415.rmo").toURI());
        ExportImport.importFile(file);

        file = new File(ExportImportTest.class.getResource("test422.rmo").toURI());
        ExportImport.importFile(file);
    }

}
