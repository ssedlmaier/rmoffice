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
package net.sf.rmoffice.meta.enums;

import com.google.gson.annotations.SerializedName;

import java.util.ResourceBundle;


/**
 *
 */
public enum StatEnum {
    @SerializedName("agility")
    AGILITY(true, false),
    @SerializedName("constitution")
    CONSTITUTION(true, false),
    @SerializedName("memory")
    MEMORY(true, false),
    @SerializedName("reasoning")
    REASONING(true, false),
    @SerializedName("selfdiscipline")
    SELFDISCIPLINE(true, false),
    @SerializedName("empathy")
    EMPATHY(false, true),
    @SerializedName("intuition")
    INTUITION(false, true),
    @SerializedName("presence")
    PRESENCE(false, true),
    @SerializedName("quickness")
    QUICKNESS(false, false),
    @SerializedName("strength")
    STRENGTH(false, false);

    private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$

    private final boolean forAPCalculation;
    private final boolean forMagic;

    private StatEnum(boolean forAPCalculation, boolean forMagic) {
        this.forAPCalculation = forAPCalculation;
        this.forMagic = forMagic;
    }


    public boolean isForAPCalculation() {
        return forAPCalculation;
    }

    public boolean isForMagic() {
        return forMagic;
    }

    /**
     * @return the long and short name combination
     */
    public String getFullI18N() {
        return RESOURCE.getString("StatEnum." + name() + ".long") + " (" + RESOURCE.getString("StatEnum." + name() + ".short") + ")";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getFullI18N();
    }
}
