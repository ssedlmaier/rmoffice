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
package net.sf.rmoffice.meta;

import java.util.ArrayList;
import java.util.List;

import net.sf.rmoffice.meta.enums.TalentFlawType;

/**
 * Meta data: Talent or flaw from configuration file. All different sub levels
 * of the talent are here in one base talent or flaw.
 */
public class TalentFlawPreset {
	private Integer id;
	private String name;
	private String source;
	private TalentFlawType type;
	private List<TalentFlawPresetLevel> values;

	/* package */TalentFlawPreset(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	/* package */void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	/* package */void setSource(String source) {
		this.source = source;
	}

	public TalentFlawType getType() {
		return type;
	}

	/* package */void setType(TalentFlawType type) {
		this.type = type;
	}

	public List<TalentFlawPresetLevel> getValues() {
		return values;
	}

	/* package */void addValues(TalentFlawPresetLevel value) {
		if (this.values == null) {
			this.values = new ArrayList<TalentFlawPresetLevel>();
		}
		this.values.add(value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TalentFlawPreset other = (TalentFlawPreset) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
