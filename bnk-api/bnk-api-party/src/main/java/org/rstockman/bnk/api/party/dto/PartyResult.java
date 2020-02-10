package org.rstockman.bnk.api.party.dto;

import org.rstockman.bnk.common.dto.StandardResultDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class PartyResult extends StandardResultDTO {
	private Long id;
	private String name;
	private Vendor vendor;
	private Category category;
}
